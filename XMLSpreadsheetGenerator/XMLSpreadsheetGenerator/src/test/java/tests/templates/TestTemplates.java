package tests.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.jdom.Document;
import org.junit.Test;

import tests.generator.GeneratorTestUtils;
import xml.spreadsheet.templates.TemplateEngine;
import xml.spreadsheet.templates.TemplateEngineFactory;

public class TestTemplates {

	private static final String VELOCITY_TEMPLATE_ENGINE = "VelocityTemplateEngine";
	private static final String FILE_TEMPLATE_ENGINE = "FileTemplateEngine";

	@Test
	public void testDefaultEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine();
			assertNotNull(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFileTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			assertNotNull(engine);
			assertEquals(engine.getClass().getName(), "xml.spreadsheet.templates.FileTemplateEngine");
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVelocityTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			assertNotNull(engine);
			assertEquals(engine.getClass().getName(), "xml.spreadsheet.templates.VelocityTemplateEngine");
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testWorkBookVelocity() {
		testWorkBookImpl(VELOCITY_TEMPLATE_ENGINE);
	}
	
	@Test
	public void testWorkBookFile() {
		testWorkBookImpl(FILE_TEMPLATE_ENGINE);
	}

	private void testWorkBookImpl(String engineId) {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(engineId);
			StringBuilder sb = new StringBuilder();
			
			sb.append(engine.applyTemplate("workbook_header", null));
			sb.append(engine.applyTemplate("workbook_foot", null));
			
			Document doc = GeneratorTestUtils.parseDocument(sb.toString());
			assertNotNull(doc);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
