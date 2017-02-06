/**
 * 
 */
package xml.spreadsheet.templates;

import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Specialized exception for template errors
 */
public class TemplateException extends XMLSpreadsheetException {

	
	//-----------------------------------------------------
	// Class properties
	
	/**
	 * Generated field for serialization
	 */
	private static final long serialVersionUID = -6599743877223186497L;

	//-----------------------------------------------------
	// Class methods
	
	/**
	 * Builds an Exception with an error message (no internationalization, etc.)
	 * @param message Error message
	 */
	public TemplateException(String message) {
		super(message);
	}
	
	/**
	 * Builds a wrapper for another kind of exception
	 * @param cause Original error that will be wrapped
	 */
	public TemplateException(Throwable cause) {
		super(cause);
	}
}
