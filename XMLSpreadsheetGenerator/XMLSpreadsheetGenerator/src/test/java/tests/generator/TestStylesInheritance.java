package tests.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static tests.generator.GeneratorTestUtils.searchStyle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import tests.XmlTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;

public class TestStylesInheritance {
	
	@Test 
	public void testInheritColorStyle() {
		try {
			final String GREEN_COLOR = "#00ff00";
			final String SHEET_CAPTION = "a sheet with inherited color styles";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style color = generator.createStyle();
			Font colorFont = color.font();
			colorFont.setColor(GREEN_COLOR);
			assertTrue(colorFont == color.font());
			
			
			Style rightStyle = generator.createStyle(color);
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Right);
			assertTrue(rightAlignment == rightStyle.alignment());		
			
			
			generator.startDocument();
			generator.startSheet(SHEET_CAPTION);
			
			generator.startRow();
			generator.writeCell(color, "aa>>aa");
			generator.closeRow();
			
			generator.emptyRow();
			generator.emptyRow();
			generator.emptyRow();
			
			generator.startRow();
			generator.writeCell(rightStyle, "bb<<bb");
			generator.closeRow();
			
			
			generator.closeSheet();
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);			
			
			// Further validations
			// does the right alignment style have a parent?
			Element rightStyleElement = searchStyle(doc, rightStyle.getId());
			assertEquals(color.getId(), XmlTestUtils.getAttributeValue(rightStyleElement, "Parent", "ss"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited color styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}