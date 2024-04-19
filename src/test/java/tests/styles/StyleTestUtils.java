package tests.styles;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import tests.XmlTestUtils;
import xml.spreadsheet.utils.NumberFormatHelper;

import static org.junit.jupiter.api.Assertions.*;
import static tests.XmlTestUtils.parseElement;

/**
 * Utility methods for style elements testing
 */
public class StyleTestUtils {

	private StyleTestUtils() {}
	
 	private static final String PREFIX = "ss";
	
	/** 
	 * It will retrieve the actual value of a certain attribute of an 
	 * element of the XML document.
	 * @param prefix XSL schema prefix of the element
	 * @param doc XML document
	 * @param selector XPath selector to find the element
	 * @param attribute Attribute of the element to be retrieved
	 * @return Value of the specified attribute in the element located in the
	 * specified XPath of the document
	 */
	public static String attributeValue(String prefix, Document doc, String selector, String attribute) {
		String ret = null;
		try {
			Element element = ((Element)XPath.selectSingleNode(doc, selector)); 
			ret = XmlTestUtils.getAttributeValue(element, attribute, 
					prefix);
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
		return ret;
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(Object styleElement, String attribute, String value) {
		checkAttributeValue(PREFIX, styleElement, attribute, value);
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(String prefix, Object styleElement, String attribute, String value) {
		checkAttributeValue(prefix, styleElement, "//ss:" + styleElement.getClass().getSimpleName(), attribute, value);
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(Object styleElement, String selector, String attribute, double value) {
		checkAttributeValue(styleElement, selector, attribute, NumberFormatHelper.format(value));
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(Object styleElement, String attribute, double value) {
		checkAttributeValue(styleElement, attribute, NumberFormatHelper.format(value));
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(Object styleElement, String selector, String attribute, String value) {
		checkAttributeValue(PREFIX, styleElement, selector, attribute, value);
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(String prefix, Object styleElement, String selector, String attribute, String value) {
		checkAttributeValue(prefix, parseElement(styleElement), selector, attribute, value);
	}
	
	// Compares the value of an attribute in an element of an XML document against
	//	an expected value
	public static void checkAttributeValue(String prefix, Document doc, String selector, String attribute, String value) {
		if (value != null) {
			assertEquals(value,
				StyleTestUtils.attributeValue(prefix, doc, selector, attribute));
		} else {
			assertNull(StyleTestUtils.attributeValue(prefix, doc,
					selector, attribute));
		}
	}
}
