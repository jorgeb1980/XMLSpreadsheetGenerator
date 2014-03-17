/**
 * 
 */
package tests.styles;

import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.junit.Assert;

import xml.spreadsheet.utils.NumberFormatHelper;

/**
 * Utility methods for style elements testing
 */
public class StyleTestUtils {
	
	private static Map<String, String> namespaces = null;
	
	static {
		namespaces = new HashMap<String, String>();
		namespaces.put("x", "urn:schemas-microsoft-com:office:excel");
		namespaces.put("ss", "urn:schemas-microsoft-com:office:spreadsheet");
	}
	
	private StyleTestUtils() {}
	
 	private static final String PREFIX = "ss";

	public static Document parseStyleElement(Object styleElement) {
		Document doc = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(
				new StringReader(
					"<alignment_test xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" " +
					" xmlns:x=\"urn:schemas-microsoft-com:office:excel\">"
					+ styleElement.toString()
					+ "</alignment_test>"));
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
		return doc;
	}
	
	public static String attributeValue(Document doc, String selector, String attribute) {
		return attributeValue(PREFIX, doc, selector, attribute);
	}
	
	public static String attributeValue(String prefix, Document doc, String selector, String attribute) {
		String ret = null;
		try {
			ret = 
				((Element)XPath.selectSingleNode(doc, selector)).getAttributeValue(attribute, 
					Namespace.getNamespace(prefix, namespaces.get(prefix)));
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
		return ret;
	}
	
	public static void checkAttributeValue(Object styleElement, String attribute, String value) {
		checkAttributeValue(PREFIX, styleElement, attribute, value);
	}
	
	public static void checkAttributeValue(String prefix, Object styleElement, String attribute, String value) {
		checkAttributeValue(prefix, styleElement, "//ss:" + styleElement.getClass().getSimpleName(), attribute, value);
	}
	
	public static void checkAttributeValue(Object styleElement, String selector, String attribute, double value) {
		checkAttributeValue(styleElement, selector, attribute, NumberFormatHelper.format(value));
	}
	
	public static void checkAttributeValue(Object styleElement, String attribute, double value) {
		checkAttributeValue(styleElement, attribute, NumberFormatHelper.format(value));
	}
	
	public static void checkAttributeValue(Object styleElement, String selector, String attribute, String value) {
		checkAttributeValue(PREFIX, styleElement, selector, attribute, value);
	}
	
	public static void checkAttributeValue(String prefix, Object styleElement, String selector, String attribute, String value) {
		Document doc = parseStyleElement(styleElement);
		checkAttributeValue(prefix, doc, selector, attribute, value);
	}
	
	public static void checkAttributeValue(String prefix, Document doc, String selector, String attribute, String value) {
		if (value != null) {
			Assert.assertEquals(value, 
				StyleTestUtils.attributeValue(prefix, doc, selector, attribute));
		}
		else {
			Assert.assertNull(StyleTestUtils.attributeValue(prefix, doc, 
					selector, attribute));
		}
	}
}
