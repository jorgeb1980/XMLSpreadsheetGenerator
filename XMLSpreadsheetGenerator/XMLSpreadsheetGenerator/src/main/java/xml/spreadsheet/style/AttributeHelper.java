/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.utils.NumberFormatHelper;

/**
 * Fills attributes
 */
public class AttributeHelper {
	
	//------------------------------------------------------------------
	// Class methods
	
	/** 
	 * Appends an xml attribute into the StringBuilder
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
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, double value) {
		att(sb, att, NumberFormatHelper.formatDouble(value));
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, int value) {
		att(sb, att, Integer.toString(value));
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string.  1 = true; 0 = false
	 */
	public static void att(StringBuilder sb, String att, boolean value) {
		att(sb, att, value?"1":"0");
	}
} 
