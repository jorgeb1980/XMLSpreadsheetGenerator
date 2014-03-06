import java.util.HashMap;
import java.util.Map;

import xml.spreadsheet.templates.TemplateEngine;
import xml.spreadsheet.templates.TemplateEngineFactory;

/**
 * 
 */
public class TestEngine {

	public static void main(String args[]) {
		try {
			//TemplateEngine motor = TemplateEngineFactory.factory().engine("FileTemplateEngine");
			//TemplateEngine motor = TemplateEngineFactory.factory().engine("VelocityTemplateEngine");
			TemplateEngine motor = TemplateEngineFactory.factory().engine();
			Map<String, String> valores = new HashMap<String, String>();
			valores.put("width", "200");
			System.out.println(motor.applyTemplate("workbook_header", valores));
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
