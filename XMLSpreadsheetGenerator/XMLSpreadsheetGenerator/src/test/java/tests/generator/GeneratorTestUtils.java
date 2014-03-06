/**
 * 
 */
package tests.generator;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

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
}
