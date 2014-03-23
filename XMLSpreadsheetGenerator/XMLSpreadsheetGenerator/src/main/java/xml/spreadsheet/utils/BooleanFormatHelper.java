/**
 * 
 */
package xml.spreadsheet.utils;

/**
 * Offers support for boolean format across the entire library
 */
public class BooleanFormatHelper {

	//------------------------------------------------------------------
	// Class methods
	
	// We do not want this to be instantiated
	private BooleanFormatHelper() {}
	
	/**
	 * Returns the string representation for a Boolean in the XMLSpreadsheet 
	 * format.
	 * @param b Boolean
	 * @return b?1:0
	 */
	public static String format(Boolean b) {
		String ret = null;
		if (b != null) {
			ret = b?"1":"0";
		}
		return ret;
	}
}
