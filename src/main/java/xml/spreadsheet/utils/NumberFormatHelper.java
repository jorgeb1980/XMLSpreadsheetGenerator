package xml.spreadsheet.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Offers support for number format across the entire library
 */
public class NumberFormatHelper {

	//------------------------------------------------------------------
	// Class properties

	/** Number format for doubles */
	private static DecimalFormat DOUBLE_FORMAT = null;
		
	
	//------------------------------------------------------------------
	// Class methods
	
	static {
		DOUBLE_FORMAT = new DecimalFormat("#.#####");
		var dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DOUBLE_FORMAT.setDecimalFormatSymbols(dfs);
	}
	
	// We do not want this to be instantiated
	private NumberFormatHelper() {}

	/**
	 * Formats a double number to the necessary format
	 * @param d Double value
	 * @return String presentation of the double
	 */
	public static String format(Double d) {
		String ret = null;
		if (d != null) {
			ret = DOUBLE_FORMAT.format(d);
		}
		return ret;
	}
}
