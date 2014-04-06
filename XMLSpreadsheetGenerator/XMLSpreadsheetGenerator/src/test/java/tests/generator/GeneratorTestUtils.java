/**
 * 
 */
package tests.generator;

import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import tests.XmlTestUtils;

@SuppressWarnings("unchecked")
public class GeneratorTestUtils {

	private GeneratorTestUtils() {}
	
	public static Document parseDocument(String value) {
		Document doc = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(
				new StringReader(value));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	// List of rows of a certain sheet
	// Returns rows as Element objects
	public static List<Element> searchRows(Document doc, String sheetName) throws JDOMException {
		return (List<Element>) XPath.selectNodes(doc, "//ss:Worksheet[@ss:Name='" + sheetName + "']//ss:Row");
	}
	
	// List of cells of a certain row
	// Returns cells as Element objects
	public static List<Element> searchCells(Element row) throws JDOMException {
		return (List<Element>) XPath.selectNodes(row, "ss:Cell");
	}
	
	// Style of a certain id
	// Returns the style as an Element object
	public static Element searchStyle(Document doc, String styleId) throws JDOMException {
		return (Element) XPath.selectSingleNode(doc, "//ss:Style[@ss:ID='" + styleId + "']");
	}
	
	// Style of a certain cell
	// Returns the style as an Element object
	public static Element searchStyle(Document doc, Element cell) throws JDOMException {
		Element ret = null;
		String id = XmlTestUtils.getAttributeValue(cell, "StyleID", "ss");
		if (id != null && id.trim().length() > 0) {
			ret = (Element) XPath.selectSingleNode(doc, "//ss:Style[@ss:ID='" + id + "']");
		}
		return ret;
	}
}
