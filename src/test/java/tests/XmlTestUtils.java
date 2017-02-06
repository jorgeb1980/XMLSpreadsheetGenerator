package tests;

import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class XmlTestUtils {

	private static Map<String, String> namespaces = null;
	
	static {
		namespaces = new HashMap<String, String>();
		namespaces.put("x", "urn:schemas-microsoft-com:office:excel");
		namespaces.put("ss", "urn:schemas-microsoft-com:office:spreadsheet");
	}
	
	public static Document parseElement(Object styleElement) {
		Document doc = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(
				new StringReader(
					"<xml_test xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" " +
					" xmlns:x=\"urn:schemas-microsoft-com:office:excel\">"
					+ styleElement.toString()
					+ "</xml_test>"));
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
		return doc;
	}
	
	// Reads the value of an attribute
	public static String getAttributeValue(Element element, String attributeName, String prefix) {
		return element.getAttributeValue(attributeName,	Namespace.getNamespace(prefix, namespaces.get(prefix)));
	}
	
	// Given a certain element, returns a list of descendants determined by an XPath query
	@SuppressWarnings("unchecked")
	public static List<Element> getDescendants(Element element, String query) throws JDOMException {
		return (List<Element>) XPath.selectNodes(element, query);
	}
}
