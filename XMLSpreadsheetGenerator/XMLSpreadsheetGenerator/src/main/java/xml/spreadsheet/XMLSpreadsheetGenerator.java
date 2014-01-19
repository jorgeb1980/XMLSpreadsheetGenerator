/**
 * 
 */
package xml.spreadsheet;

import java.io.OutputStream;

/**
 * Partial implementation of the spreadsheet format described in the article
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx 
 */
public class XMLSpreadsheetGenerator {

	//---------------------------------------------------------------
	// Class constants
	
	/** Buffer size. 10k looks fine to me and performs right in production
	 * environment AFAIK
	 */
	private static final int BUFFER_SIZE = 10000;
	
	//---------------------------------------------------------------
	// Class members
	
	/** Every instance of the generator is tied to an OutputStream. */
	private OutputStream os;
	/** Generator state. */
	private GeneratorState state = GeneratorState.BASE;
	
	//---------------------------------------------------------------
	// Class methods
	
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * The generated file will have just one sheet.
	 * @param os Where the generator is going to write its output to.
	 */
	public XMLSpreadsheetGenerator(OutputStream os) 
			throws XMLSpreadsheetException {
		this(os, 1);
	}
	
	/**
	 * Builds a generator tied to the OutputStream passed as a parameter.
	 * @param os Where the generator is going to write its output to.
	 * @param sheets Due to the format specification, it is necessary to know
	 * in advance the number of sheets that the spreadsheet will have
	 */
	public XMLSpreadsheetGenerator(OutputStream output, int sheets) 
				throws XMLSpreadsheetException {
		os = output;
		// We assume that the output is initialized and we can write on it
		state = GeneratorState.validateTransition(state, GeneratorState.WRITING_SHEET);
		// How many sheets?
		if (sheets <= 0) {
			throw new XMLSpreadsheetException("Invalid sheets number for the workbook: " + sheets);
		}
		
	}
}
