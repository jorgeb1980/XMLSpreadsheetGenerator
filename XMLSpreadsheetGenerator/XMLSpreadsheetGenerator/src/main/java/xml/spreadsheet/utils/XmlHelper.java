/**
 * 
 */
package xml.spreadsheet.utils;

import java.util.Map;


/**
 * Fills attributes
 */
public class XmlHelper {
	
	//------------------------------------------------------------------
	// Class methods
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, String value) {
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
	public static void att(StringBuilder sb, String att, Double value) {
		if (value != null) {
			att(sb, att, NumberFormatHelper.format(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, Integer value) {
		if (value != null) {
			att(sb, att, Integer.toString(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	public static void att(StringBuilder sb, String att, Long value) {
		if (value != null) {
			att(sb, att, Long.toString(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string.  1 = true; 0 = false
	 */
	public static void att(StringBuilder sb, String att, Boolean value) {
		if (value != null) {
			att(sb, att, value?"1":"0");
		}
	}
	
	/**
	 * Creates an empty xml node with the indicated attributes
	 * @param element Name of the element
	 * @param closure Table with the attributes
	 * @return String representation of the XML node
	 */
	public static String emptyElement(String element, Table<Object> closure) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(element);
		Map<String, Object> map = closure.map();
		if (map != null && map.values().size() != 0) {
			sb.append(" ");
			for (String key: map.keySet()) {
				Object value = map.get(key);
				if (value instanceof String) {
					att(sb, key, (String) value);
				}
				else if (value instanceof Double) {
					att(sb, key, (Double) value);
				}
				else if (value instanceof Boolean) {
					att(sb, key, (Boolean) value);
				}
				else if (value instanceof Long) {
					att(sb, key, (Long) value);
				}
				else if (value instanceof Integer) {
					att(sb, key, (Integer) value);
				}
				else {
					att(sb, key, value.toString());
				}
			}
		}
		sb.append("/>");
		return sb.toString();
	}
} 
