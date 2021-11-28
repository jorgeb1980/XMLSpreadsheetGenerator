package tests.misc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import xml.spreadsheet.utils.PropertiesReader;

public class TestPropertiesReader {

	@Test
	public void testIndexedProperties() {
		try {
			List<String> properties = PropertiesReader.READER.propertiesByPrefix("property");
			assertEquals(3, properties.size());
			assertEquals("aaa", PropertiesReader.READER.property(properties.get(0)));
			assertEquals("bbb", PropertiesReader.READER.property(properties.get(1)));
			assertEquals("ccc", PropertiesReader.READER.property(properties.get(2)));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
