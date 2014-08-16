/**
 * 
 */
package tests.generator;

import static org.junit.Assert.fail;

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
	
	// List of columns of a certain sheet
	// Returns columns as Element objects
	public static List<Element> searchColumns(Document doc, String sheetName) throws JDOMException {
		return (List<Element>) XPath.selectNodes(doc, "//ss:Worksheet[@ss:Name='" + sheetName + "']//ss:Column");
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
	
	// Get the value of an attribute of a certain element
	public static String getChildAttribute(String child, Element element, String attribute) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(element.getDocument(), element);
		List<Element> children =
			XmlTestUtils.getDescendants(style, child);
		if (children != null && children.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(children.get(0), attribute, "ss");
		}
		else {
			fail();
		}
		return ret;
	}
	
	public static String getFontStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Font", element, attribute);
	}
	
	public static String getInteriorStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Interior", element, attribute);
	}
	
	public static String getProtectionStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Protection", element, attribute);
	}
	
	public static String getAlignmentAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Alignment", element, attribute);
	}
	
	// Style of the bottom border
	public static String getBorderStyleAttribute(Element cell, String position, String attribute) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(cell.getDocument(), cell);
		List<Element> bottomBorder = 
			XmlTestUtils.getDescendants(style, "ss:Borders/ss:Border[@ss:Position='" + position + "']");
		if (bottomBorder != null && bottomBorder.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(bottomBorder.get(0), attribute, "ss");
		}
		else {
			fail(); 
		}
		return ret;
	}
	
}
