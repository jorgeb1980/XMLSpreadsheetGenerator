package tests.misc;

import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.XmlTestUtils.parseElement;
import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

public class TestXmlHelper {

	@Test
	public void testEmptyElement() {
		String element = element("ss:random_element");
		assertEquals("<ss:random_element/>", element);
	}
	
	@Test
	public void testElement() {
		String element = 
			element("ss:yet_another_element",
				mapOf(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key3", "value3"
				)
			);

		Document doc = parseElement(element);
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", getAttributeValue(yetAnotherElement, "key2", "ss"));
		assertEquals("value3", getAttributeValue(yetAnotherElement, "key3", "ss"));
	}
	
	@Test
	public void testNullAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			element(
				"ss:yet_another_element",
				mapOf(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key4", "value4"
				)
			);
		Document doc = parseElement(element);
		
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", getAttributeValue(yetAnotherElement, "key2", "ss"));
		// Is key3 present?
		assertNull(getAttributeValue(yetAnotherElement, "key3", "ss"));
		assertEquals("value4", getAttributeValue(yetAnotherElement, "key4", "ss"));
		// Is key5 present?
		assertNull(getAttributeValue(yetAnotherElement, "key5", "ss"));
	}
	
	@Test
	public void testEmptyAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			element("ss:yet_another_element",
				mapOf(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key3", "",
					"ss:key4", "value4",
					"ss:key5", ""
				)
			);
		Document doc = parseElement(element);
		
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", getAttributeValue(yetAnotherElement, "key2", "ss"));
		// Is key3 present?
		assertEquals("", getAttributeValue(yetAnotherElement, "key3", "ss"));
		assertEquals("value4", getAttributeValue(yetAnotherElement, "key4", "ss"));
		// Is key5 present?
		assertEquals("", getAttributeValue(yetAnotherElement, "key5", "ss"));
	}
	
}
