/**
 * 
 */
package xml.spreadsheet.style;

import java.text.DecimalFormat;

/**
 * Fills attributes
 */
public class AttributeHelper {
	
	//------------------------------------------------------------------
	// Class properties

	/** Number format for doubles */
	private static final DecimalFormat DOUBLE_FORMAT =
		new DecimalFormat("#.#");
	
	//------------------------------------------------------------------
	// Class methods

	
	/** 
	 * Fills an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, Object value) {
		if (value != null && value.toString().trim().length() > 0) {
			sb.append(" " + att + "=\"");
			sb.append(value.toString());
			sb.append("\"");
		}
	}
	
	/** 
	 * Fills an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, double value) {
		att(sb, att, DOUBLE_FORMAT.format(value));
	}
	
	/** 
	 * Fills an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, int value) {
		att(sb, att, Integer.toString(value));
	}
	
	/** 
	 * Fills an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string.  1 = true; 0 = false
	 */
	public static void att(StringBuilder sb, String att, boolean value) {
		att(sb, att, value?"1":"0");
	}
} 
