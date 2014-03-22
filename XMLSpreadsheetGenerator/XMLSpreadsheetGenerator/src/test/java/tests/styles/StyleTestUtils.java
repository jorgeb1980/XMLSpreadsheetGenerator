/**
 * 
 */
package tests.styles;

import static org.junit.Assert.fail;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.junit.Assert;

import test.XmlTestUtils;
import xml.spreadsheet.utils.NumberFormatHelper;

/**
 * Utility methods for style elements testing
 */
public class StyleTestUtils {
	
	
	
	private StyleTestUtils() {}
	
 	private static final String PREFIX = "ss";

	
	
	public static String attributeValue(Document doc, String selector, String attribute) {
		return attributeValue(PREFIX, doc, selector, attribute);
	}
	
	public static String attributeValue(String prefix, Document doc, String selector, String attribute) {
		String ret = null;
		try {
			Element element = ((Element)XPath.selectSingleNode(doc, selector)); 
			ret = XmlTestUtils.getAttributeValue(element, attribute, 
					prefix);
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
		Document doc = XmlTestUtils.parseElement(styleElement);
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
