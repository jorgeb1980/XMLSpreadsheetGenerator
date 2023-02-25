package xml.spreadsheet.utils;

import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Supports validation through the code
 */
public class AssertionHelper {
	
	//------------------------------------------------------------------
	// Class methods
	// We do not want this to be instantiated
	private AssertionHelper() {}

	/**
	 * Throws an exception if the condition to check is false
	 * @param condition Condition to check
	 * @param message Message to be thrown in case of error
	 * @throws XMLSpreadsheetException Thrown if condition is false
	 */
	public static void assertion(boolean condition, String message)
		throws XMLSpreadsheetException {
		if (!condition) {
			throw new XMLSpreadsheetException(message);
		}
	}
}
