/**
 * 
 */
package xml.spreadsheet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import xml.spreadsheet.style.NumberFormat.Format;
import xml.spreadsheet.templates.TemplateEngine;
import xml.spreadsheet.templates.TemplateEngineFactory;
import xml.spreadsheet.utils.AssertionHelper;
import xml.spreadsheet.utils.Table;
import xml.spreadsheet.utils.XmlHelper;
import xml.spreadsheet.utils.DateFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

/**
 * Partial implementation of the spreadsheet format described in the article<br/>
 * <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx">Microsoft XML Spreadsheet Reference</a> 
 * <br/>
 * The generator will follow this state machine:
 * <br/>
 * <code>
 * Constructor -> [Create style]* -> startDocument -> 
 * [openSheet -> [openRow -> [openCell -> closeCell]* -> closeRow]* -> closeSheet ]* -> 
 * closeDocument
 * </code><br/>
 * The Generator will throw a <code>XMLSpreadsheetException</code> if 
 * the API user tries to break the state machine (say for example, try to write a cell
 * after closing the document, or writing a row before opening a sheet)
 */
public class XMLSpreadsheetGenerator {

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
	private Writer writer;
	/** Generator state. */
	private GeneratorState state = GeneratorState.INITIALIZATION;
	/** Style counter. */
	private int styleCounter = 1;
	/** Styles. */
	private List<Style> styles;
	/** Template engine */
	private TemplateEngine engine = null;
	
	// Is empty the current row?  This will be set to true every time
	//	a new row is started, and false if a cell is written into it.
	private boolean emptyCurrentRow = true;
	// Row counter for the current sheet
	private int rowCounter = 0;
	// Cell counter for the current row
	//private int cellCounter = 0;
	// Should the next row show its row counter?
	private boolean showRowCounter = false;
	// Should the next cell show its cell counter?
	//private boolean showCellCounter = false;
	
	// The generator stores a predefined default date format
	private Style dateFormat = null;
	
	//---------------------------------------------------------------
	// Class methods
	
		
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * Sets the <code>INITIALIZATION</code> state.
	 * @param output Where the generator is going to write its output to.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public XMLSpreadsheetGenerator(OutputStream output) 
				throws XMLSpreadsheetException {
		writer = new OutputStreamWriter(
			new BufferedOutputStream(output, BUFFER_SIZE), CHARSET);
		// Initialization state: we can define styles
		styles = new LinkedList<Style>();
		// Template engine
		engine = TemplateEngineFactory.factory().engine();
		// Create the date format
		dateFormat = createStyle();
		dateFormat.numberFormat().setFormat(Format.LongDate);
	}
	
	/**
	 * This method creates a style attached to this spreadsheet generator object.
	 * It is only able to do that if the generator is in INITIALIZATION state 
	 * @return Empty style object
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public Style createStyle() throws XMLSpreadsheetException {
		AssertionHelper.assertion(state == GeneratorState.INITIALIZATION, 
				"It is not possible to add styles to a generator in state: " + state);
		Style style = new Style("ce" + Integer.toString(styleCounter++));
		styles.add(style);
		return style;
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
		flush(engine.applyTemplate("workbook_header"));
		
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
	public void closeDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.DONE);
		flush(engine.applyTemplate("workbook_foot"));
		endStreaming();
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
		flush(XmlHelper.element("ss:Row", 
			new Table<Object>().
				add("ss:Caption", caption).
				add("ss:Height", height).
				add("ss:AutoFitHeight", autoFitHeight).
				add("ss:Hidden", hidden).
				add("ss:StyleID", style != null?style.getId():null).
				add("ss:Index", showRowCounter?rowCounter:null),
			// Don't close!
			false));
		rowCounter++;
	}
	
	/**
	 * Writes a single empty <code>&lt;ss:Row&gt;</code> element with the desired span and formatting
	 * @param span Specifies the number of adjacent rows with the same formatting as this row. 
	 * When a Span attribute is used, the spanned row elements are not written out.
	 * Rows must not overlap. Doing so results in an XML Spreadsheet document that is invalid. 
	 * Care must be taken with this attribute to ensure that the span does not include another 
	 * row index that is specified. Unlike columns, rows with the Span attribute must be empty. 
	 * A row that contains a Span attribute and one or more Cell elements is considered invalid. 
	 * The Span attribute for rows is a short-hand method for setting formatting properties for multiple, empty rows.
	 * @param autoFitHeight f this attribute is True, it means that this row should be autosized
	 * @param height Specifies the height of a row in points. This value must be greater than or equal to 0
	 * @param style Style object to be applied to these rows
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeEmptyRows(
			Long span, 
			Boolean autoFitHeight, 
			Double height, 
			Style style) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
		if (span != null) {
			// Flush the row
			rowCounter += span;
		}
		else {
			rowCounter++;
		}
		flush(XmlHelper.element("ss:Row", new Table<Object>().
			add("ss:Span", span).
			add("ss:Height", height!=null?height.doubleValue():null).
			add("ss:AutoFitHeight", autoFitHeight).
			add("ss:StyleID", style!=null?style.getId():null)
			));
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
		showRowCounter = true;
	}
	
	/**
	 * Streams the ending of a row.  Sets the <code>WRITING_SHEET</code> state.
	 * If we have created no cells in the row, it will flush nothing to the output,
	 * but increase the counter for the next row.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void closeRow() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		if (emptyCurrentRow) {
			flush(XmlHelper.element("ss:Cell", 
				new Table<Object>().
					add("ss:Index", "1")));
		}
		flush(engine.applyTemplate("row_foot"));
		showRowCounter = false;
	}
	
	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void startSheet(String sheetName) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		// Validate that the sheet name is not null
		AssertionHelper.assertion(sheetName != null, "The sheet name must be specified");
		// Flush the start of the sheet template
		flush(engine.applyTemplate("sheet_header", 
			new Table<String>().add("sheetName", sheetName).map()));
		// Current sheet row counter
		rowCounter = 0;
	}
	
	/**
	 * Streams the ending of a sheet.  Sets the <code>CLEAN_DOCUMENT</code> state.
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void closeSheet() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.CLEAN_DOCUMENT);
		flush(engine.applyTemplate("sheet_foot"));
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
	 * @see <a href="http://office.microsoft.com/en-us/excel-help/format-a-date-the-way-you-want-HA102809474.aspx">Custom date formatting</a>
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
	 * @style Cell style
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
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
			if (showRowCounter) {
				// Show row counter only if necessary
				showRowCounter = false;
			}
		}
		// write the contents of the cell
		flush(XmlHelper.element("ss:Cell", 
			new Table<Object>().add("ss:StyleID", style!=null?style.getId():null), 
			XmlHelper.element("ss:Data", 
				new Table<Object>().add("ss:Type", type.toString()), value)));
	}
	
	
}
