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
	 * @param value Non null value to fill into the string
	 */
	private static void att(StringBuilder sb, String att, String value) {
		sb.append(" ");
		sb.append(att);
		sb.append("=\"");		
		sb.append(value);
		sb.append("\"");
	}
	
	/**
	 * Encloses a string into a CDATA construct as defined in its 
	 * <a href="http://www.w3schools.com/xml/xml_cdata.asp">documentation</a> 
	 * @param string Original string
	 * @return String enclosed by CDATA structure
	 */
	public static String cdata(String string) {
		String ret = null;
		if (string != null) {
			StringBuilder sb = new StringBuilder("<![CDATA[");
			sb.append(string);
			sb.append("]]>");
			ret = sb.toString();
		}
		return ret;
	}

	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void att(StringBuilder sb, String att, Double value) {
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
	private static void att(StringBuilder sb, String att, Integer value) {
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
	private static void att(StringBuilder sb, String att, Long value) {
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
	private static void att(StringBuilder sb, String att, Boolean value) {
		if (value != null) {
			att(sb, att, value?"1":"0");
		}
	}
	
	/**
	 * Creates a closed, empty xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @return String representation of the XML node
	 */
	public static String element(String elementName) {
		return element(elementName, null, null, true);		
	}

	/**
	 * Creates an empty xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @param close If it is true, the method closes the xml node; if false, it does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, boolean close) {
		return element(elementName, null, null, close);
	}
	
	/**
	 * Creates an empty and closed xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @param table Table with the attributes
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> table) {
		return element(elementName, table, null, true);		
	}

	/**
	 * Creates a closed xml node with the indicated content
	 * @param elementName Name of the element
	 * @param content Content of the XML node
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, String content) {
		return element(elementName, null, content, true);
	}
	
	/**
	 * Creates an empty xml node with the indicated attributes
	 * @param elementName Name of the element
	 * @param table Table with the attributes
	 * @param close If it is true, the method closes the xml node; if false, it does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> table, boolean close) {
		return element(elementName, table, null, close);		
	}
	
	/**
	 * Creates a closed xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param table Table with the attributes
	 * @param content Content of the XML node 
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> table, String content) {
		return element(elementName, table, content, true);
	}
	
	/**
	 * Creates an xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param table Table with the attributes
	 * @param content Content of the XML node 
	 * @param close If it is true, the method closes the xml node; if false, it
	 * does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> table,
			String content, boolean close) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(elementName);
		if (table != null && table.size() != 0) {
			sb.append(" ");
			for (String key: table.keySet()) {
				Object value = table.get(key);
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
