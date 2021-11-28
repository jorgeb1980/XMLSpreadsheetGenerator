package xml.spreadsheet.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Offers support for date format across the entire library
 */
public class DateFormatHelper {

	//------------------------------------------------------------------
	// Class properties

	/** Date format: 1987-10-30T00:00:00.000 */
	private static DateFormat DATE_FORMAT = 
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");		
	
	//------------------------------------------------------------------
	// Class methods
	
	// We do not want this to be instantiated
	private DateFormatHelper() {}	
	
	
	/**
	 * Formats a date to the necessary format
	 * @param d Date value
	 * @return String presentation of the date
	 */
	public static String format(Date d) {
		String ret = null;
		if (d != null) {
			ret = DATE_FORMAT.format(d);
		}
		return ret;
	}
}
