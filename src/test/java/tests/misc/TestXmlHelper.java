package tests.misc;

import static org.junit.Assert.assertEquals;
import static tests.XmlTestUtils.getAttributeValue;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import tests.XmlTestUtils;
import xml.spreadsheet.utils.MapBuilder;
import xml.spreadsheet.utils.XmlHelper;

import java.util.Map;

public class TestXmlHelper {

	@Test
	public void testEmptyElement() {
		String element = XmlHelper.element("ss:random_element");
		assertEquals("<ss:random_element/>", element);
	}
	
	@Test
	public void testElement() {
		String element = 
			XmlHelper.element("ss:yet_another_element", 
				MapBuilder.of(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key3", "value3"
				)
			);

		Document doc = XmlTestUtils.parseElement(element);
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", getAttributeValue(yetAnotherElement, "key2", "ss"));
		assertEquals("value3", getAttributeValue(yetAnotherElement, "key3", "ss"));
	}
	
	@Test
	public void testNullAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			XmlHelper.element(
				"ss:yet_another_element",
				MapBuilder.of(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key4", "value4"
				)
			);
		Document doc = XmlTestUtils.parseElement(element);
		
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", getAttributeValue(yetAnotherElement, "key2", "ss"));
		// Is key3 present?
		assertEquals(null, getAttributeValue(yetAnotherElement, "key3", "ss"));
		assertEquals("value4", getAttributeValue(yetAnotherElement, "key4", "ss"));
		// Is key5 present?
		assertEquals(null, getAttributeValue(yetAnotherElement, "key5", "ss"));
	}
	
	@Test
	public void testEmptyAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			XmlHelper.element("ss:yet_another_element",
				MapBuilder.of(
					"ss:key1", "value1",
					"ss:key2", "value2",
					"ss:key3", "",
					"ss:key4", "value4",
					"ss:key5", ""
				)
			);
		Document doc = XmlTestUtils.parseElement(element);
		
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
