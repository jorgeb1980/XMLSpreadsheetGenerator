/**
 * 
 */
package tests.generator;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import org.jdom.Document;
import org.junit.Assert;
import org.junit.Test;

import tests.styles.StyleTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;

public class TestGenerator {

	
	@Test
	public void testEmptyDocument() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			Assert.assertNotNull(doc);
		}
		catch(Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCreateStyleAfterStarting() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			
			generator.createStyle();
			
			fail();
		}
		catch(XMLSpreadsheetException e) {
			// success
			return;
		}
		catch(Exception e) {
			fail();
		}
	}
	

	@Test
	public void testCreateStyleAfterEnding() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.closeDocument();
			
			generator.createStyle();
			
			fail();
		}
		catch(XMLSpreadsheetException e) {
			// success
			return;
		}
		catch(Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCreateEmptyStyle() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			generator.createStyle();
			
			generator.startDocument();
			
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			Assert.assertNotNull(doc);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateAlignmentStyle() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style style = generator.createStyle();
			
			style.alignment().setHorizontal(HorizontalAlignment.Center);
			
			generator.startDocument();
			
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			Assert.assertNotNull(doc);
			
			StyleTestUtils.checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Alignment", "Horizontal", HorizontalAlignment.Center.toString());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateBordersStyle() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style style = generator.createStyle();
			
			style.borders().createBorder(BorderPosition.Bottom);
			
			generator.startDocument();
			
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			Assert.assertNotNull(doc);
			
			StyleTestUtils.checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Borders/ss:Border", "Position", BorderPosition.Bottom.toString());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
