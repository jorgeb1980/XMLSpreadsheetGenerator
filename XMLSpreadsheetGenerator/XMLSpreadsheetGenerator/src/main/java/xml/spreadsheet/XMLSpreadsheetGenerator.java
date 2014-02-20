/**
 * 
 */
package xml.spreadsheet;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

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
	
	//---------------------------------------------------------------
	// Class members
	
	/** Every instance of the generator is tied to an OutputStream. */
	private OutputStream os;
	/** Generator state. */
	private GeneratorState state = GeneratorState.INITIALIZATION;
	/** Style counter. */
	private int styles = 0;
	/** Styles. */
	
	
	//---------------------------------------------------------------
	// Class methods
	
		
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * It begins streaming the first part of the header of the document.
	 * Sets the <code>INITIALIZATION</code> state.
	 * @param os Where the generator is going to write its output to.
	 * in advance the number of sheets that the spreadsheet will have
	 */
	public XMLSpreadsheetGenerator(OutputStream output) 
				throws XMLSpreadsheetException {
		os = new BufferedOutputStream(output, BUFFER_SIZE);
		// Initialization state: we can define styles
		
	}
	
	/**
	 * Use when all the styles are defined.
	 * Streams the end of the header of the document.  Sets the 
	 * <code>CLEAN_DOCUMENT</code> state.
	 */
	public void startDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.CLEAN_DOCUMENT);
	}
	
	/**
	 * Streams the end of the document.  Sets the <code>DONE</code> state.   End
	 * of the streaming and flush.
	 */
	public void closeDocument() 
				throws XMLSpreadsheetException {
		state = GeneratorState.validateTransition(state, GeneratorState.DONE);
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
