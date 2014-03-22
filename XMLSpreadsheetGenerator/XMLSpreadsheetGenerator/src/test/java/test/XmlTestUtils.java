package test;

import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;
import org.jdom.Namespace;

public class XmlTestUtils {

	private static Map<String, String> namespaces = null;
	
	static {
		namespaces = new HashMap<String, String>();
		namespaces.put("x", "urn:schemas-microsoft-com:office:excel");
		namespaces.put("ss", "urn:schemas-microsoft-com:office:spreadsheet");
	}
	
	// Reads the value of an attribute
	public static String getAttributeValue(Element element, String attributeName, String prefix) {
		return element.getAttributeValue(attributeName,	Namespace.getNamespace(prefix, namespaces.get(prefix)));
	}
}
