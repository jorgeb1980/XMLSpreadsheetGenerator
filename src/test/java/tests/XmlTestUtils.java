package tests;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

public class XmlTestUtils {

	private static final String KEEP_FILES = "KEEP_FILES";
	private static final List<String> YES_VALUES = Arrays.asList("YES", "TRUE");

	private static Map<String, String> namespaces;
	
	static {
		namespaces = new HashMap<>();
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

	/**
	 * Runs a function within a context in which an output file will be created and deleted unless
	 * we have set the appropriate env var.  The method will output instructions for it if launched with default env vars.
	 * @param f Lambda that will consume the created OutputStream
	 */
	public static void executeWithTempFile(Consumer<ByteArrayOutputStream> f) {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			f.accept(baos);
			if (YES_VALUES.contains(getSystemVariable(KEEP_FILES))) {
				File file = File.createTempFile("xmlspreadsheet", ".xml");
				try (OutputStream os = new FileOutputStream(file)) { os.write(baos.toByteArray()); }
				System.out.println("Created temp file " + file);
			} else  System.out.printf("If you wish to keep and inspect later temporary files created during tests, please declare the env var %s=true%n", KEEP_FILES);
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	private static String getSystemVariable(String variable) {
		String value = System.getenv(variable);
		String ret = "";
		if (value != null) { ret = value.toUpperCase(); }
		return ret;
	}
}
