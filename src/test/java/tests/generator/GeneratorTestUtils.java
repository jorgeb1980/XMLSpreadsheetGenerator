package tests.generator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static tests.XmlTestUtils.*;

@SuppressWarnings("unchecked")
public class GeneratorTestUtils {

	private GeneratorTestUtils() {}
	
	public static Document parseDocument(String value) {
		Document doc = null;
		try {
			var builder = new SAXBuilder();
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
		return generateXPathExpression("//ss:Worksheet[@ss:Name='" + sheetName + "']//ss:Row").evaluate(doc);
	}
	
	// List of columns of a certain sheet
	// Returns columns as Element objects
	public static List<Element> searchColumns(Document doc, String sheetName) throws JDOMException {
		return generateXPathExpression( "//ss:Worksheet[@ss:Name='" + sheetName + "']//ss:Column").evaluate(doc);
	}
	
	// List of cells of a certain row
	// Returns cells as Element objects
	public static List<Element> searchCells(Element row) throws JDOMException {
		return generateXPathExpression("ss:Cell").evaluate(row);
	}
	
	// Style of a certain id
	// Returns the style as an Element object
	public static Element searchStyle(Document doc, String styleId) throws JDOMException {
		return generateXPathExpression("//ss:Style[@ss:ID='" + styleId + "']").evaluateFirst(doc);
	}
	
	// Style of a certain cell
	// Returns the style as an Element object
	public static Element searchStyle(Document doc, Element cell) throws JDOMException {
		Element ret = null;
		var id = getAttributeValue(cell, "StyleID", "ss");
		if (id != null && !id.trim().isEmpty()) {
			ret = generateXPathExpression( "//ss:Style[@ss:ID='" + id + "']").evaluateFirst(doc);
		}
		return ret;
	}
	
	// Get the value of an attribute of a certain element
	public static String getChildAttribute(String child, Element element, String attribute) throws JDOMException {
		String ret = null;
		var style = GeneratorTestUtils.searchStyle(element.getDocument(), element);
		var children = getDescendants(style, child);
		if (children != null && children.size() == 1) {
			ret = getAttributeValue(children.get(0), attribute, "ss");
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
	
	public static String getNumberFormatAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:NumberFormat", element, attribute);
	}
	
	// Style of the bottom border
	public static String getBorderStyleAttribute(Element cell, String position, String attribute) throws JDOMException {
		String ret = null;
		var style = GeneratorTestUtils.searchStyle(cell.getDocument(), cell);
		var bottomBorder =  getDescendants(style, "ss:Borders/ss:Border[@ss:Position='" + position + "']");
		if (bottomBorder != null && bottomBorder.size() == 1) {
			ret = getAttributeValue(bottomBorder.get(0), attribute, "ss");
		}
		else {
			fail(); 
		}
		return ret;
	}
	
}
