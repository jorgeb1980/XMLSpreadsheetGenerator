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
	private static void attr(StringBuilder sb, String att, String value) {
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
			ret = String.format("<![CDATA[%s]]>", string);
		}
		return ret;
	}

	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void attr(StringBuilder sb, String att, Double value) {
		if (value != null) {
			attr(sb, att, NumberFormatHelper.format(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void attr(StringBuilder sb, String att, Integer value) {
		if (value != null) {
			attr(sb, att, Integer.toString(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void attr(StringBuilder sb, String att, Long value) {
		if (value != null) {
			attr(sb, att, Long.toString(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param att Name of the attribute
	 * @param value Value to fill into the string.  1 = true; 0 = false
	 */
	private static void attr(StringBuilder sb, String att, Boolean value) {
		if (value != null) {
			attr(sb, att, value?"1":"0");
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
	 * @param attributes Table with the attributes
	 * @param close If it is true, the method closes the xml node; if false, it does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> attributes, boolean close) {
		return element(elementName, attributes, null, close);
	}
	
	/**
	 * Creates a closed xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param attributes Table with the attributes
	 * @param content Content of the XML node 
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> attributes, String content) {
		return element(elementName, attributes, content, true);
	}
	
	/**
	 * Creates an xml node with the indicated attributes and content
	 * @param elementName Name of the element
	 * @param attributes Table with the attributes
	 * @param content Content of the XML node 
	 * @param close If it is true, the method closes the xml node; if false, it
	 * does not
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> attributes,
			String content, boolean close) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(elementName);
		if (attributes != null && attributes.size() != 0) {
			sb.append(" ");
			for (String key: attributes.keySet()) {
				Object value = attributes.get(key);
				if (value instanceof String) {
					attr(sb, key, (String) value);
				}
				else if (value instanceof Double) {
					attr(sb, key, (Double) value);
				}
				else if (value instanceof Boolean) {
					attr(sb, key, (Boolean) value);
				}
				else if (value instanceof Long) {
					attr(sb, key, (Long) value);
				}
				else if (value instanceof Integer) {
					attr(sb, key, (Integer) value);
				}
				else {
					attr(sb, key, value.toString());
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
