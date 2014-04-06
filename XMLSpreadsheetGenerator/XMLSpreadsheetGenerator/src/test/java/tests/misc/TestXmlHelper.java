package tests.misc;

import static org.junit.Assert.assertEquals;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import tests.XmlTestUtils;
import xml.spreadsheet.utils.Table;
import xml.spreadsheet.utils.XmlHelper;

public class TestXmlHelper {

	@Test
	public void testEmptyElement() {
		String element = XmlHelper.element("ss:random_element", null);
		assertEquals("<ss:random_element/>", element);
	}
	
	@Test
	public void testElement() {
		String element = 
			XmlHelper.element("ss:yet_another_element", 
				new Table<Object>().
					add("ss:key1", "value1").
					add("ss:key2", "value2").
					add("ss:key3", "value3"));

		Document doc = XmlTestUtils.parseElement(element);
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", XmlTestUtils.getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", XmlTestUtils.getAttributeValue(yetAnotherElement, "key2", "ss"));
		assertEquals("value3", XmlTestUtils.getAttributeValue(yetAnotherElement, "key3", "ss"));
	}
	
	@Test
	public void testNullAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			XmlHelper.element("ss:yet_another_element", 
				new Table<Object>().
					add("ss:key1", "value1").
					add("ss:key2", "value2").
					add("ss:key3", null).
					add("ss:key4", "value4").
					add("ss:key5", null));
		Document doc = XmlTestUtils.parseElement(element);
		
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", XmlTestUtils.getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", XmlTestUtils.getAttributeValue(yetAnotherElement, "key2", "ss"));
		// Is key3 present?
		assertEquals(null, XmlTestUtils.getAttributeValue(yetAnotherElement, "key3", "ss"));
		assertEquals("value4", XmlTestUtils.getAttributeValue(yetAnotherElement, "key4", "ss"));
		// Is key5 present?
		assertEquals(null, XmlTestUtils.getAttributeValue(yetAnotherElement, "key5", "ss"));
	}
	
	@Test
	public void testEmptyAttributes() {
		// Try to trick empty attributes into the element
		String element = 
			XmlHelper.element("ss:yet_another_element", 
				new Table<Object>().
					add("ss:key1", "value1").
					add("ss:key2", "value2").
					add("ss:key3", "").
					add("ss:key4", "value4").
					add("ss:key5", ""));
		Document doc = XmlTestUtils.parseElement(element);
		
		Element yetAnotherElement = (Element) doc.getRootElement().getContent().get(0);
		assertEquals("value1", XmlTestUtils.getAttributeValue(yetAnotherElement, "key1", "ss"));
		assertEquals("value2", XmlTestUtils.getAttributeValue(yetAnotherElement, "key2", "ss"));
		// Is key3 present?
		assertEquals("", XmlTestUtils.getAttributeValue(yetAnotherElement, "key3", "ss"));
		assertEquals("value4", XmlTestUtils.getAttributeValue(yetAnotherElement, "key4", "ss"));
		// Is key5 present?
		assertEquals("", XmlTestUtils.getAttributeValue(yetAnotherElement, "key5", "ss"));
	}
	
}
