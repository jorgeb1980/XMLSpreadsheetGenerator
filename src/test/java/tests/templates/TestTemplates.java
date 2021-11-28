package tests.templates;

import org.jdom.Document;
import org.junit.Test;
import tests.generator.GeneratorTestUtils;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.templates.TemplateEngine;
import xml.spreadsheet.templates.TemplateEngineFactory;
import xml.spreadsheet.templates.TemplateException;
import xml.spreadsheet.utils.ClasspathFileReader;
import xml.spreadsheet.utils.MapBuilder;

import java.util.HashMap;

import static org.junit.Assert.*;

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
	public void testEmptyValuesFileTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testEmptyValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testEmptyValuesVelocityTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testEmptyValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	// Tests a template substitution with an empty values map
	private void testEmptyValues(TemplateEngine engine) {
		try {
			// Read the contents of the template
			String template = ClasspathFileReader.retrieveFile("templates/test_template.xml");
			assertEquals(template, engine.applyTemplate("test_template", new HashMap<String, String>()));
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testWrongValuesFileTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testWrongValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testWrongValuesVelocityTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testWrongValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	// Tests a template substitution with a wrong values map
	private void testWrongValues(TemplateEngine engine) {
		final String TEST = "just a try to change the first parameter and the $parameter2 for its respective values";
		try {
			// Read the contents of the template
			assertEquals(TEST,
				engine.applyTemplate(
					"values_template",
					MapBuilder.of(
						"wrong1", "first parameter",
						"wrong2", "second parameter",
						"parameter1", "first parameter"
					)
				)
			);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNullValuesFileTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testNullValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNullValuesVelocityTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testNullValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	// Tests a template substitution with a null values map
	private void testNullValues(TemplateEngine engine) {
		try {
			// Read the contents of the template
			String template = ClasspathFileReader.retrieveFile("templates/test_template.xml");
			assertEquals(template, engine.applyTemplate("test_template", null));
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void testValuesVelocityTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testValuesFileTemplateEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testValues(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testValues(TemplateEngine engine) {
		final String TEST = "just a try to change the first parameter and the second parameter for its respective values";
		try {
			// Read the contents of the template
			assertEquals(TEST,
				engine.applyTemplate(
					"values_template",
					MapBuilder.of(
						"parameter1", "first parameter",
						"parameter2", "second parameter"
					)
				)
			);
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
	
	@Test
	public void testFileEngineErrors() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testEngineErrors(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVelocityEngineErrors() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testEngineErrors(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testEngineErrors(TemplateEngine engine) {
		try {
			engine.applyTemplate("does_not_exist");
		}
		catch (TemplateException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testUnspecifiedTemplateFileEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testUnspecifiedTemplate(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testUnspecifiedTemplateVelocityEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testUnspecifiedTemplate(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testUnspecifiedTemplate(TemplateEngine engine) {
		try {
			engine.applyTemplate(null);
		}
		catch (TemplateException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testEmptyTemplateFileEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(FILE_TEMPLATE_ENGINE);
			testEmptyTemplate(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testEmptyTemplateVelocityEngine() {
		try {
			// Test default engine creation
			TemplateEngine engine = TemplateEngineFactory.factory().engine(VELOCITY_TEMPLATE_ENGINE);
			testEmptyTemplate(engine);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testEmptyTemplate(TemplateEngine engine) {
		try {
			engine.applyTemplate("");
		}
		catch (TemplateException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testWrongEngine() {
		try {
			TemplateEngineFactory.factory().engine("does not exist");
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testNullEngine() {
		try {
			TemplateEngineFactory.factory().engine(null);
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testEmptyEngine() {
		try {
			TemplateEngineFactory.factory().engine("");
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
}
