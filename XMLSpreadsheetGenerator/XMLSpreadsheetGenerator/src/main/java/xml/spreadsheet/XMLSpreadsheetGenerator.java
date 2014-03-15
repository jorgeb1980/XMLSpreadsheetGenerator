/**
 * 
 */
package xml.spreadsheet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import xml.spreadsheet.templates.TemplateEngine;
import xml.spreadsheet.templates.TemplateEngineFactory;
import xml.spreadsheet.utils.NumberFormatHelper;

/**
 * Partial implementation of the spreadsheet format described in the article<br/>
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx 
 * <br/>
 * The generator will follow this sequence:
 * <br/>
 * <code>
 * Constructor -> [Create style]* -> startDocument -> 
 * [openSheet -> [openRow -> [openCell -> closeCell]* -> closeRow]* -> closeSheet ]* -> 
 * closeDocument
 * </code>
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
	// Should the next row show its row counter?
	private boolean showRowCounter = false;
	
	//---------------------------------------------------------------
	// Class methods
	
		
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * Sets the <code>INITIALIZATION</code> state.
	 * @param os Where the generator is going to write its output to.
	 * in advance the number of sheets that the spreadsheet will have
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
	}
	
	/**
	 * This method creates a style attached to this spreadsheet generator object.
	 * It is only able to do that if the generator is in INITIALIZATION state 
	 * @return Empty style object
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public Style createStyle() throws XMLSpreadsheetException {
		if (state != GeneratorState.INITIALIZATION) {
			// Throw an exception
			throw new XMLSpreadsheetException(
				"It is not possible to add styles to a generator in state: " + state);
		}
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
		rowCounter++;
	}
	
	/**
	 * Writes a single empty <ss:Row> with the desired span and formatting
	 * @param span Specifies the number of adjacent rows with the same formatting as this row. 
	 * When a Span attribute is used, the spanned row elements are not written out.
	 * Rows must not overlap. Doing so results in an XML Spreadsheet document that is invalid. 
	 * Care must be taken with this attribute to ensure that the span does not include another 
	 * row index that is specified. Unlike columns, rows with the Span attribute must be empty. 
	 * A row that contains a Span attribute and one or more Cell elements is considered invalid. 
	 * The Span attribute for rows is a short-hand method for setting formatting properties for multiple, empty rows.
	 * @param height Specifies the height of a row in points. This value must be greater than or equal to 0
	 * @param style Style object to be applied to these rows
	 * @throws XMLSpreadsheetException If called in an inappropiate state or 
	 * any other library-related exception arises
	 */
	public void writeEmptyRows(
		Integer span, 
		Double height, 
		Style style) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
		rowCounter += span;
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
	}
	
	/**
	 * Streams the ending of a row.  Sets the <code>WRITING_SHEET</code> state.
	 */
	public void closeRow() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		if (!emptyCurrentRow) {
			flush(engine.applyTemplate("row_foot.xml"));
		}
		else {
			// Current row is empty, next row comes with an index attribute
			showRowCounter = true;
		}
	}
	
	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 */
	public void startSheet(String sheetName, Double columnWidth) throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		// Flush the start of the sheet template
		flush(engine.applyTemplate("sheet_header.xml", 
			new MapClosure().add("sheetName", sheetName).map()));
		if (columnWidth != null) {
			// Column width specified?
			flush(engine.applyTemplate("column.xml", 
				new MapClosure().add(
					"width", NumberFormatHelper.formatDouble(columnWidth)).
						map()));
		}
		// Current sheet row counter
		rowCounter = 0;
	}
	
	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 */
	public void startSheet(String sheetName) throws XMLSpreadsheetException {
		startSheet(sheetName, null);
	}
	
	/**
	 * Streams the ending of a sheet.  Sets the <code>CLEAN_DOCUMENT</code> state.
	 */
	public void closeSheet() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.CLEAN_DOCUMENT);
		flush(engine.applyTemplate("sheet_foot.xml"));
	}
	
	/**
	 * Writes a cell value into the stream
	 */
	public void writeCell() throws XMLSpreadsheetException {		
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_CELL);
		if (emptyCurrentRow) {
			// flush the start of the row
			flush("<ss:Row");
			emptyCurrentRow = false;
			if (showRowCounter) {
				// Show row counter
				
				showRowCounter = false;
			}
		}
		// write the contents of the cell
	}
}
