package xml.spreadsheet;

import xml.spreadsheet.Style.StyleBuilder;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.DateFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;
import xml.spreadsheet.utils.XmlHelper;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static xml.spreadsheet.style.NumberFormat.LONG_DATE;
import static xml.spreadsheet.utils.AssertionHelper.assertion;
import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Partial implementation of the spreadsheet format described in the article<br/>
 * <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)">Microsoft XML Spreadsheet Reference</a>
 * <br/>
 * The generator will follow this state machine:
 * <br/>
 * <code>
 * Constructor -&gt; [Create style]* -&gt; startDocument -&gt; 
 * [openSheet -&gt; [column]* -&gt; [openRow -&gt; [writeCell]* -&gt; closeRow]* -&gt; closeSheet ]* -&gt; 
 * closeDocument
 * </code><br/>
 * The Generator will throw a <code>XMLSpreadsheetException</code> if 
 * the API user tries to break the state machine (say for example, try to write a cell
 * after closing the document, or writing a row before opening a sheet)
 */
public class XMLSpreadsheetGenerator implements AutoCloseable {

	//---------------------------------------------------------------
	// Class constants
	
	/** Buffer size. 10k looks fine to me and performs right in production
	 * environment AFAIK
	 */
	private static final int BUFFER_SIZE = 10 * 1024;
	/** Spreadsheets are defined in cp1252 */
	private static final java.nio.charset.Charset CHARSET = 
		java.nio.charset.Charset.forName("cp1252");
	
	//---------------------------------------------------------------
	// Class members
	
	/** Every instance of the generator is tied to an OutputStream. */
	final private Writer writer;
	/** Generator state.  The machine state validations are implemented on this
	 * variable. */
	private GeneratorState state = GeneratorState.INITIALIZATION;
	/** Style counter. Will be used to generate the Style IDs. */
	private int styleCounter = 1;
	/** Styles. Every new Style will be added to this List and flushed into
	 * the output stream as soon as the document gets started. */
	final private List<Style> styles;

	/** Is empty the current row?  This will be set to true every time
	 * a new row is started, and false if a cell is written into it. */
	private boolean emptyCurrentRow = true;
	
	/* This class used to have a row counter in order to display the
	 *	ss:Index attribute, which was needed by Libre Office.  Since the rest
	 *	of spreadsheet products do not need it, or even fail to render the 
	 *	spreadsheet if it is included, I have dropped it. */

	/** The generator stores a predefined default date format */
	private Style dateFormat = null;
	
	/** The generator keeps a column count for the current sheet */
	private long columnCount = 0;
	
	//---------------------------------------------------------------
	// Class methods
	
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * Sets the <code>INITIALIZATION</code> state.  Takes the default BUFFER_SIZE.
	 * @param output Where the generator is going to write its output to.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public XMLSpreadsheetGenerator(OutputStream output) 
				throws XMLSpreadsheetException {
		this(output, BUFFER_SIZE);
	}
	
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * Sets the <code>INITIALIZATION</code> state.
	 * @param output Where the generator is going to write its output to.
	 * @param bufferSize Output buffer size in bytes
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public XMLSpreadsheetGenerator(OutputStream output, int bufferSize) 
				throws XMLSpreadsheetException {
		writer = new OutputStreamWriter(
			new BufferedOutputStream(output, bufferSize), CHARSET);
		// Initialization state: we can define styles
		styles = new LinkedList<>();
		
		// Default styles
		
		// LibreOffice and OpenOffice engine expect some 'Default' empty style to
		//	exist
		createStyle("Default", "Default", null);
		// Create the default date format
		dateFormat = createStyle().withNumberFormat(LONG_DATE).build();
	}
	
	/**
	 * This method creates a named style attached to this spreadsheet generator object,
	 * extending the specified parent style.
	 * It is only able to do that if the generator is in INITIALIZATION state
	 * @param name Style name
	 * @param parent Parent style 
	 * @return Empty style object, inheriting parent's attributes
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public StyleBuilder createStyle(String name, Style parent) throws XMLSpreadsheetException {
		return createStyle("ce" + Integer.toString(styleCounter++), name, parent);
	}
	
	/**
	 * This method creates a named style attached to this spreadsheet generator object,
	 * extending the specified parent style.
	 * It is only able to do that if the generator is in INITIALIZATION state
	 * @param name Style name
	 * @param parent Parent style 
	 * @return Empty style object, inheriting parent's attributes
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	private StyleBuilder createStyle(String id, String name, Style parent) throws XMLSpreadsheetException {
		assertion(state == GeneratorState.INITIALIZATION, 
				"It is not possible to add styles to a generator in state: " + state);
		return Style.builder(styles).withId(id).withName(name).withParent(parent);
	}
	
	/**
	 * This method creates an anonymous style attached to this spreadsheet generator object,
	 * extending the specified parent style.
	 * It is only able to do that if the generator is in INITIALIZATION state
	 * @param parent Parent style 
	 * @return Empty style object, inheriting parent's attributes
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public StyleBuilder createStyle(Style parent) throws XMLSpreadsheetException {
		return createStyle(null, parent);
	}
	
	/**
	 * This method creates an anonymous style attached to this spreadsheet generator object.
	 * It is only able to do that if the generator is in INITIALIZATION state 
	 * @return Empty style object
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public StyleBuilder createStyle() throws XMLSpreadsheetException {
		return createStyle(null, null);
	}
	
	/**
	 * This method creates a named style attached to this spreadsheet generator object.
	 * It is only able to do that if the generator is in INITIALIZATION state
	 * @param name Name that will be shown for the style 
	 * @return Empty style object
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public StyleBuilder createStyle(String name) throws XMLSpreadsheetException {
		return createStyle(name, null);
	}
	
	// Flushes a string down the output stream
	private void flush(String s) throws XMLSpreadsheetException {
		try {
			writer.write(s);
		}
		catch(IOException ioe) {
			throw new XMLSpreadsheetException(ioe);
		}
	}
	
	// Flush and close the output stream
	private void endStreaming() throws XMLSpreadsheetException {
		try {
			writer.flush();
			writer.close();
		}
		catch(IOException ioe) {
			throw new XMLSpreadsheetException(ioe);
		}
	}
	
	/**
	 * Writes an empty row into the document.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void emptyRow() throws XMLSpreadsheetException {
		startRow();
		closeRow();
	}

	// Fixed header for every workbook
	private void workbookHeader() throws XMLSpreadsheetException {
		flush("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		flush("<?mso-application progid=\"Excel.Sheet\"?>");
		flush(
			element(
				"Workbook",
				mapOf(
					"xmlns", "urn:schemas-microsoft-com:office:spreadsheet",
					"xmlns:c", "urn:schemas-microsoft-com:office:component:spreadsheet",
					"xmlns:html", "http://www.w3.org/TR/REC-html40",
					"xmlns:o", "urn:schemas-microsoft-com:office:office",
					"xmlns:ss", "urn:schemas-microsoft-com:office:spreadsheet",
					"xmlns:x2", "http://schemas.microsoft.com/office/excel/2003/xml",
					"xmlns:x", "urn:schemas-microsoft-com:office:excel",
					"xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"
				),
				FALSE
			)
		);
		flush(element("OfficeDocumentSettings", mapOf("xmlns", "urn:schemas-microsoft-com:office:office"), FALSE));
		flush(element("Colors", FALSE));
		flush(element("Color", FALSE));
		flush(element("Index", "3"));
		flush(element("RGB", "#c0c0c0"));
		flush("</Color>");
		flush(element("Color", FALSE));
		flush(element("Index", "4"));
		flush(element("RGB", "#ff0000"));
		flush("</Color>");
		flush("</Colors>");
		flush("</OfficeDocumentSettings>");
	}
	
	/**
	 * Use when all the styles are defined (no further definition of styles after this).
	 * Streams the header of the document.  Sets the 
	 * <code>CLEAN_DOCUMENT</code> state.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, 
				GeneratorState.CLEAN_DOCUMENT);

		// Header of the document		
		workbookHeader();
		
		// Flush all the styles on the document
		if (styles != null) {
			flush("<ss:Styles>");
			for (Style style: styles) {
				flush(style.toString());
			}
			flush("</ss:Styles>");
		}
		
	}
	
	/**
	 * Streams the end of the document.  Sets the <code>DONE</code> state.   End
	 * of the streaming and flush.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	@Override
	public void close() throws Exception {
		// It will ignore further attempts to close it once it is done
		if (state != GeneratorState.DONE) {
			state = GeneratorState.validateTransition(state, GeneratorState.DONE);
			flush("</Workbook>");
			endStreaming();
		}
	}
	
	/**
	 * Begins a row.  Sets the <code>WRITING_ROW</code> state.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startRow() throws XMLSpreadsheetException {
		startRow(null, null, null, null, null);
	}
	
	/**
	 * Begins a row.  Sets the <code>WRITING_ROW</code> state.
	 * @param caption Specifies the caption that should appear when the component's custom row and column headers are showing
	 * @param autoFitHeight f this attribute is True, it means that this row should be autosized
	 * @param height Specifies the height of a row in points. This value must be greater than or equal to 0
	 * @param hidden True specifies that this row is hidden. False (or omitted) specifies that this row is shown
	 * @param style Style object to be applied to this row
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startRow(
			String caption, 
			Boolean autoFitHeight, 
			Double height, 
			Boolean hidden, 
			Style style) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
		emptyCurrentRow = true;
		// Create current row
		flush(
			element(
				"ss:Row",
				mapOf(
					"ss:Caption", caption,
					"ss:Height", height,
					"ss:AutoFitHeight", autoFitHeight,
					"ss:Hidden", hidden,
					"ss:StyleID", style != null ? style.id() : null
				),
				FALSE // Don't close!
			)
		);
	}
	
	/**
	 * Writes a single empty <code>&lt;ss:Row&gt;</code> element with the desired span and formatting
	 * @param emptyRows Number of empty rows to write with this method
	 * @param autoFitHeight f this attribute is True, it means that this row should be autosized
	 * @param height Specifies the height of a row in points. This value must be greater than or equal to 0
	 * @param style Style object to be applied to these rows
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeEmptyRows(
			Long emptyRows, 
			Boolean autoFitHeight, 
			Double height, 
			Style style) throws XMLSpreadsheetException {
		// This method used to implement a single ss:Row with an ss:Span attribute, from which
		// 	follows its documentation:
		
		/*	Specifies the number of adjacent rows with the same formatting as this row. 
		 * When a Span attribute is used, the spanned row elements are not written out.
		 * Rows must not overlap. Doing so results in an XML Spreadsheet document that is invalid. 
		 * Care must be taken with this attribute to ensure that the span does not include another 
		 * row index that is specified. Unlike columns, rows with the Span attribute must be empty. 
		 * A row that contains a Span attribute and one or more Cell elements is considered invalid. 
		 * The Span attribute for rows is a short-hand method for setting formatting properties for multiple, empty rows. */
		
		// However, since this way to render the empty rows is not well supported by every 
		//	spreadsheet product I tried, I decided to remain with this procedural implementation of
		//	the same functionality.
		long times = emptyRows == null? 1 : emptyRows;
		for (int i = 0; i < times; i++) {
			startRow(null, autoFitHeight, height, false, style);
			writeEmptyCell();
			closeRow();
		}
	}
	
	/**
	 * Streams the ending of a row.  Sets the <code>WRITING_SHEET</code> state.
	 * If we have created no cells in the row, it will flush nothing to the output,
	 * but increase the counter for the next row.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void closeRow() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET_ROWS);
		if (emptyCurrentRow) {
			flush(element("ss:Cell", mapOf("ss:Index", "1")));
		}
		flush("</ss:Row>");
	}
	
	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 * @param sheetName Sheet tab caption
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startSheet(String sheetName) throws XMLSpreadsheetException {
		startSheet(sheetName, false);
	}

	// Creates a common header for every sheet
	private void sheetHeader(String name, boolean isProtected) throws XMLSpreadsheetException {
		flush(
			element(
				"ss:Worksheet",
				mapOf(
					"ss:Name", name,
					"ss:Protected", BooleanFormatHelper.format(isProtected)
				),
				FALSE
			)
		);
		flush(element("Table", FALSE));
	}

	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 * @param sheetName Sheet tab caption
	 * @param protectedSheet If true, the sheet is protected
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startSheet(String sheetName, boolean protectedSheet) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		// Validate that the sheet name is not null
		assertion(sheetName != null, "The sheet name must be specified");
		// Flush the start of the sheet template
		sheetHeader(sheetName, protectedSheet);
		columnCount = 0;
	}

	// Common foot for every sheet
	private void sheetFoot() throws XMLSpreadsheetException {
		flush("</Table>");
		flush("<x:WorksheetOptions/>");
		flush("</ss:Worksheet>");
	}

	/**
	 * Streams the ending of a sheet.  Sets the <code>CLEAN_DOCUMENT</code> state.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void closeSheet() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.CLEAN_DOCUMENT);
		sheetFoot();
	}
	
	/**
	 * Starts a columns section (must be included in a sheet, before the first row
	 * is written)
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises 
	 */
	public void startColumns() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, 
				GeneratorState.WRITING_COLUMNS);
	}
	
	/**
	 * Closes a columns section (must be included in a sheet, before the first row
	 * is written)
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises 
	 */
	public void closeColumns() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, 
				GeneratorState.WRITING_SHEET);
		// Empty column
		flush(element("ss:Column"));
	}
	
	/**
	 * Defines the formatting for one or more adjacent columns. This element contains no data; 
	 * all cell data is stored within Row elements. All Column
	 * elements must appear before the first Row element.
	 * @param caption (optional) Specifies the caption that should appear when the 
	 * Component's custom row and column headers are showing 
	 * @param autoFitWidth (optional) If this attribute is specified as True, 
	 * it means that this column should be autosized for numeric and date values only. 
	 * We do not autofit textual values.<br/>

		If both ss:Width and ss:AutoFitWidth exist, the behavior is as follows:<br/>

	    ss:AutoFitWidth="1" and ss:Width is unspecified: Autofit the column width to fit the content.<br/>
	    ss:AutoFitWidth="1" and ss:Width is specified: Set the column to the specified width and 
	    only autofit if the size of the content is larger than the specified width.<br/>
	    ss:AutoFitWidth="0" and ss:Width is unspecified: Use the default column width.<br/>
	    ss:AutoFitWidth="0" and ss:Width is specified: Use the specified width.<br/>

	 * @param hidden (optional) True specifies that this column is hidden. False (or omitted) 
	 * specifies that this column is shown
	 * @param index (optional) Specifies the position of this column within the table.<br/>

			If this tag is not specified, the first instance has an assumed Index="1". 
			Each additional Column element has an assumed Index that is one higher.<br/>
			
			Indices must appear in strictly increasing order. Failure to do so will result in an 
			XML Spreadsheet document that is invalid. Indices do not need to be sequential, however.
			Omitted indices are formatted with the default style's format.<br/>
			
			Indices must not overlap. If duplicates exist, the behavior is unspecified and the 
			XML Spreadsheet document is considered invalid. An easy way to create overlap is 
			through careless use of the Span attribute. 
			
			1 &lt;= Index &lt;= Total columns
	 * @param span (optional) Specifies the number of adjacent columns with the same formatting as 
	 * this column. When a Span attribute is used, the spanned column elements are not written out.<br/>
		As mentioned in the Index tag, columns must not overlap. Doing so results in an XML 
		Spreadsheet document that is invalid. Care must be taken with this attribute to ensure 
		that the span does not include another column index that is specified. 
		This value follows the rules of the span attribute in HTML rather than the specified in the documentation,
		so a value of 'X' will mean a total number of X columns with the specified style, width, etc., counting the first one<br/>
	 * @param style (optional) Specifies a reference to a previously defined <code>Style</code>. 
	 * This reference indicates that this <code>Style</code> should be used to format this element. 
	 * If this attribute is not present, the default <code>Style</code> should be applied to this element. 
	 * @param width (optional) Specifies the width of a column in points. This value must be greater than or equal to 0. 
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void column(String caption, Boolean autoFitWidth, 
					   Boolean hidden, Long index, Long span, 
					   Style style, Double width) throws XMLSpreadsheetException {
		assertion(state == GeneratorState.WRITING_COLUMNS, 
			"Cannot write a column if not inside the columns section");
		// Do some validations on indexes
		if (index != null) {
			assertion(index > columnCount, "Column overlap!");
			// Should we cover a gap?
			if ((index - columnCount) > 1) {
				long gap = (index - columnCount) - 1;
				flush(
					element(
						"ss:Column",
						mapOf("ss:Span", gap > 1 ? gap - 1 : null)
					)
				);
			}
			// Jump to the index
			columnCount = index;
		}
		else {
			columnCount++;
		}
		// add the span to the column count
		if (span != null) {
			columnCount += (span - 1);
		}
		flush(
			element(
				"ss:Column",
				mapOf(
					"c:Caption", caption,
					"ss:AutoFitWidth", autoFitWidth,
					"ss:Hidden", hidden,
					"ss:Index", index,
					"ss:Span", span != null && span > 1 ? span-1 : null,
					"ss:StyleID", style == null ? null : style.id(),
					"ss:Width", width
				)
			)
		);
	}
	
	/**
	 * Defines the formatting for one or more adjacent columns. This element contains no data; 
	 * all cell data is stored within Row elements. All Column
	 * elements must appear before the first Row element.  This method tries to give a useful
	 * shortcut for the (hopefuly) more often used options. 
	 * @param style (optional) Specifies a reference to a previously defined <code>Style</code>. 
	 * This reference indicates that this <code>Style</code> should be used to format this element. 
	 * If this attribute is not present, the default <code>Style</code> should be applied to this element. 
	 * @param width (optional) Specifies the width of a column in points. This value must be greater than or equal to 0. 
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void column(Style style, Double width) throws XMLSpreadsheetException {
		column(null, null, null, null, null, style, width);
	}
	
	/**
	 * Writes a String to a cell
	 * @param style Style object to apply to the cell
	 * @param value String value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(Style style, String value) throws XMLSpreadsheetException {		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_CELL);
		writeCellImpl(style, value, CellType.String);		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
	}
	
	/**
	 * Writes a String to a cell
	 * @param value String value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(String value) throws XMLSpreadsheetException {		
		writeCell(null, value);
	}
	
	/**
	 * Writes a number to a cell
	 * @param style Style object to apply to the cell
	 * @param value Number value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(Style style, Double value) throws XMLSpreadsheetException {		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_CELL);
		writeCellImpl(style, NumberFormatHelper.format(value), CellType.Number);		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
	}
	
	/**
	 * Writes a number to a cell
	 * @param value Number value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(Double value) throws XMLSpreadsheetException {		
		writeCell(null, value);
	}
	
	/**
	 * Writes a date to a cell
	 * @param style Style object to apply to the cell.  Remember that the proper way
	 * to create a date format is to create a <code>Style</code> object, and then
	 * define for it a proper <code>NumberFormat</code>.
	 * @see <a href="https://support.microsoft.com/en-us/office/format-a-date-the-way-you-want-8e10019e-d5d8-47a1-ba95-db95123d273e">Custom date formatting</a>
	 * @param value Date value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(Style style, Date value) throws XMLSpreadsheetException {		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_CELL);
		// Desired format: 1987-10-30T00:00:00.000
		writeCellImpl(style == null?dateFormat:style, DateFormatHelper.format(value), CellType.DateTime);		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
	}
	
	/**
	 * Writes a date to a cell.  It will print it with the standard format of
	 * the library (yyyy-MM-dd)
	 * @param value Date value to write
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeCell(Date value) throws XMLSpreadsheetException {		
		writeCell(null, value);
	}
	
	/**
	 * Writes an empty cell to the document.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeEmptyCell() throws XMLSpreadsheetException {
		writeEmptyCell(null);
	}
	
	/**
	 * Writes an empty cell to the document.
	 * @param style Cell style
	 * @throws XMLSpreadsheetException If called in an inappropriate state or
	 * any other library-related exception arises
	 */
	public void writeEmptyCell(Style style) throws XMLSpreadsheetException {
		writeCellImpl(style, null, CellType.String);
	}
	
	/**
	 * Writes a cell value into the stream
	 */
	private void writeCellImpl(Style style, String value, CellType type) throws XMLSpreadsheetException {
		if (emptyCurrentRow) {
			emptyCurrentRow = false;
		}
		// write the contents of the cell
		flush(
			element("ss:Cell",
				mapOf("ss:StyleID", style != null ? style.id() : null),
				element("ss:Data",
					mapOf("ss:Type", type.toString()),
					XmlHelper.cdata(value)
				)
			)
		);
	}
	
	
}
