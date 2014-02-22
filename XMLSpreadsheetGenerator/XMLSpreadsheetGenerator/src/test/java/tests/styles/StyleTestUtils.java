/**
 * 
 */
package tests.styles;

import static org.junit.Assert.fail;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.junit.Assert;

/**
 * Utility methods for style elements testing
 */
public class StyleTestUtils {

	public static Document parseStyleElement(Object styleElement) {
		Document doc = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(
				new StringReader(
					"<alignment_test xmlns:ss =\"urn:schemas-microsoft-com:office:spreadsheet\">"
					+ styleElement.toString()
					+ "</alignment_test>"));
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
		return doc;
	}
	
	public static String attributeValue(Document doc, String selector, String attribute) {
		String ret = null;
		try {
			ret = 
				((Element)XPath.selectSingleNode(doc, selector)).getAttributeValue(attribute, 
					Namespace.getNamespace("ss", "urn:schemas-microsoft-com:office:spreadsheet"));
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
		return ret;
	}
	
	public static void checkAttributeValue(Object styleElement, String attribute, String value) {
		Document doc = parseStyleElement(styleElement);
		if (value != null) {
			Assert.assertEquals(value, 
				StyleTestUtils.attributeValue(doc, "//ss:" + styleElement.getClass().getSimpleName(), attribute));
		}
		else {
			Assert.assertNull(StyleTestUtils.attributeValue(doc, 
					"//ss:" + styleElement.getClass().getSimpleName(), attribute));
		}
	}
	
	public static void checkAttributeValue(Object styleElement, String selector, String attribute, String value) {
		Document doc = parseStyleElement(styleElement);
		if (value != null) {
			Assert.assertEquals(value, 
				StyleTestUtils.attributeValue(doc, selector, attribute));
		}
		else {
			Assert.assertNull(StyleTestUtils.attributeValue(doc, 
					"ss:" + styleElement.getClass().getSimpleName(), attribute));
		}
	}
}
