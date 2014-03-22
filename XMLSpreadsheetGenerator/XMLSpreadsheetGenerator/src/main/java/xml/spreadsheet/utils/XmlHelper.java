/**
 * 
 */
package xml.spreadsheet.utils;

import java.util.Map;


/**
 * Provides support to XML generation across the library
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
	 * Creates an empty and closed xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @param closure Table with the attributes
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Table<Object> closure) {
		return element(elementName, closure, null, true);		
	}
	
	/**
	 * Creates an empty xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @param closure Table with the attributes
	 * @param close If it is true, the method closes the xml node; if false, it
	 * does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Table<Object> closure, boolean close) {
		return element(elementName, closure, null, close);		
	}
	
	/**
	 * Creates a closed xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param closure Table with the attributes
	 * @param content Content of the XML node 
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Table<Object> closure, 
			String content) {
		return element(elementName, closure, content, true);
	}
	
	/**
	 * Creates an xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param closure Table with the attributes
	 * @param content Content of the XML node 
	 * @param close If it is true, the method closes the xml node; if false, it
	 * does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Table<Object> closure, 
			String content, boolean close) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(elementName);
		if (closure != null) {
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
		}
		if (content != null) {
			sb.append(">");
			sb.append(content);
			if (close) {
				sb.append("</");
				sb.append(elementName);
				sb.append(">");
			}
		}
		else {
			sb.append(close?"/>":">");
		}
		return sb.toString();
	}
} 
