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
	
	//---------------------------------------------------------------
	// Class methods
	
		
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * Sets the <code>INITIALIZATION</code> state.
	 * @param os Where the generator is going to write its output to.
	 * in advance the number of sheets that the spreadsheet will have
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
	 */
	public void startDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, 
				GeneratorState.CLEAN_DOCUMENT);
		
		// Header of the document
		String beginHeader = 
			engine.applyTemplate("workbook_header", null);
		
		flush(beginHeader);
		
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
	 */
	public void closeDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.DONE);
		String endDocument = engine.applyTemplate("workbook_foot", null);
		flush(endDocument);
		endStreaming();
	}
	
	/**
	 * Streams the begin of a row.  Sets the <code>WRITING_ROW</code> state.
	 */
	public void startRow() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_ROW);
	}
	
	/**
	 * Streams the ending of a row.  Sets the <code>WRITING_SHEET</code> state.
	 */
	public void closeRow() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
	}
	
	/**
	 * Streams the begin of a sheet.  Sets the <code>WRITING_SHEET</code> state.
	 */
	public void startSheet() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
	}
	
	/**
	 * Streams the ending of a sheet.  Sets the <code>CLEAN_DOCUMENT</code> state.
	 */
	public void closeSheet() throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.CLEAN_DOCUMENT);
	}
}
