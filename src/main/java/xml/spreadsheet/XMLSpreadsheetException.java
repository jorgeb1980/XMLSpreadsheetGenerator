package xml.spreadsheet;

/**
 * Checked exception type for XML Spreadsheet generation 
 */
public class XMLSpreadsheetException extends Exception {

	//---------------------------------------------------------
	// Class properties	
	
	/**
	 * Generated field for serialization
	 */
	private static final long serialVersionUID = 4518309302323015134L;

	//---------------------------------------------------------
	// Class methods
	
	/**
	 * Builds an Exception with an error message (no internationalization, etc.)
	 * @param message Error message
	 */
	public XMLSpreadsheetException(String message) {
		super(message);
	}
	
	/**
	 * Builds a wrapper for another kind of exception
	 * @param cause Original error that will be wrapped
	 */
	public XMLSpreadsheetException(Throwable cause) {
		super(cause);
	}
}
