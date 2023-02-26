package xml.spreadsheet.utils;

import java.util.Map;

import static java.lang.String.format;

/**
 * Provides support to XML generation across the library
 */
public class XmlHelper {
	
	//------------------------------------------------------------------
	// Class methods
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param name Name of the attribute
	 * @param value Non null value to fill into the string
	 */
	private static void attr(StringBuilder sb, String name, String value) {
		sb.append(format(" %s=\"%s\"", name, value));
	}
	
	/**
	 * Encloses a string into a CDATA construct as defined in its 
	 * <a href="https://www.w3resource.com/xml/CDATA-sections.php">documentation</a>
	 * @param string Original string
	 * @return String enclosed by CDATA structure
	 */
	public static String cdata(String string) {
		String ret = null;
		if (string != null) {
			ret = format("<![CDATA[%s]]>", string);
		}
		return ret;
	}

	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param name Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void attr(StringBuilder sb, String name, Double value) {
		if (value != null) {
			attr(sb, name, NumberFormatHelper.format(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param name Name of the attribute
	 * @param value Value to fill into the string
	 */
	private static void attr(StringBuilder sb, String name, Long value) {
		if (value != null) {
			attr(sb, name, Long.toString(value));
		}
	}
	
	/** 
	 * Appends an xml attribute into the StringBuilder
	 * @param sb Mutable String
	 * @param name Name of the attribute
	 * @param value Value to fill into the string.  1 = true; 0 = false
	 */
	private static void attr(StringBuilder sb, String name, Boolean value) {
		if (value != null) {
			attr(sb, name, value?"1":"0");
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
	 * @param attributes Table with the attributes
	 * @return String representation of the XML node
	 */
	public static String element(String elementName, Map<String, Object> attributes) {
		return element(elementName, attributes, null, true);
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
	 * @param close If it is true, the method closes the xml node; if false, it does not
	 * @return String representation of the XML node
	 */
	public static String element(
		String elementName,
		Map<String, Object> attributes,
		String content,
		boolean close
	) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(elementName);
		if (attributes != null && attributes.size() != 0) {
			sb.append(" ");
			for (String key: attributes.keySet()) {
				Object value = attributes.get(key);
				switch (value) {
					case String s -> attr(sb, key, s);
					case Double d -> attr(sb, key, d);
					case Boolean b -> attr(sb, key, b);
					case Long l -> attr(sb, key, l);
					case null, default -> attr(sb, key, value == null ? "" : value.toString());
				}
			}
		}
		if (content != null) {
			sb.append(">");
			sb.append(content);
			if (close) {
				sb.append(format("</%s>", elementName));
			}
		}
		else {
			sb.append(close?"/>":">");
		}
		return sb.toString();
	}
} 
