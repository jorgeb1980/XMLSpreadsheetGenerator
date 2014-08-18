package tests.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static tests.generator.GeneratorTestUtils.getAlignmentAttribute;
import static tests.generator.GeneratorTestUtils.getBorderStyleAttribute;
import static tests.generator.GeneratorTestUtils.getFontStyleAttribute;
import static tests.generator.GeneratorTestUtils.getInteriorStyleAttribute;
import static tests.generator.GeneratorTestUtils.getNumberFormatAttribute;
import static tests.generator.GeneratorTestUtils.searchStyle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import tests.XmlTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Borders;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.utils.NumberFormatHelper;

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
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			// Validate green style
			Element row0 = rows.get(0);
			Element greenCell = GeneratorTestUtils.searchCells(row0).get(0);
			assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));
			
			// Validate inheritance
			Element row4 = rows.get(4);
			Element rightCell = GeneratorTestUtils.searchCells(row4).get(0);
			assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
			assertEquals(GREEN_COLOR, getFontStyleAttribute(rightCell, "Color"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited color styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testInheritAlignmentStyle() {
		try {
			final String GREEN_COLOR = "#00ff00";
			final String SHEET_CAPTION = "a sheet with inherited alignment styles";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			
			Style rightStyle = generator.createStyle();
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Right);
			assertTrue(rightAlignment == rightStyle.alignment());		
			
			Style color = generator.createStyle(rightStyle);
			Font colorFont = color.font();
			colorFont.setColor(GREEN_COLOR);
			assertTrue(colorFont == color.font());
			
			
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
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			// Validate green style
			Element row0 = rows.get(0);
			Element greenCell = GeneratorTestUtils.searchCells(row0).get(0);
			assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));
			
			// Validate horizontal alignment style
			Element row4 = rows.get(4);
			Element rightCell = GeneratorTestUtils.searchCells(row4).get(0);
			assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
			
			// Validate inheritance 
			Element colorElement = searchStyle(doc, color.getId());
			assertEquals(rightStyle.getId(), XmlTestUtils.getAttributeValue(colorElement, "Parent", "ss"));
			assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(greenCell, "Horizontal"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited alignment styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testInheritBorderStyle() {
		try {
			final String SHEET_CAPTION = "a sheet with inherited border styles";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);	
			
			Style thickBorderStyle = generator.createStyle();
			Borders thickBorders = thickBorderStyle.borders();
			Border thickBorder = thickBorders.createBorder(BorderPosition.Right);
			thickBorder.setLineStyle(LineStyle.Dash);
			thickBorder.setWeight(BorderWeight.Thick);
			
			Style rightStyle = generator.createStyle(thickBorderStyle);
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Right);
			assertTrue(rightAlignment == rightStyle.alignment());	
			
			
			generator.startDocument();
			generator.startSheet(SHEET_CAPTION);
			
			generator.startRow();
			generator.writeCell(thickBorderStyle, "aa>>aa");
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
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			// Validate thick style
			assertEquals(
				NumberFormatHelper.format(BorderWeight.Thick.getValue()), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(0)).get(0), BorderPosition.Right.toString(), "Weight"));
			
			// Validate horizontal alignment style
			Element row4 = rows.get(4);
			Element rightCell = GeneratorTestUtils.searchCells(row4).get(0);
			assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
			
			// Validate inheritance
			Element rightStyleElement = searchStyle(doc, rightStyle.getId());
			assertEquals(thickBorderStyle.getId(), XmlTestUtils.getAttributeValue(rightStyleElement, "Parent", "ss"));
			assertEquals(NumberFormatHelper.format(BorderWeight.Thick.getValue()), 
					getBorderStyleAttribute(rightCell, BorderPosition.Right.toString(), "Weight"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited border styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testInheritInteriorStyle() {
		try {
			final String SHEET_CAPTION = "a sheet with inherited interior styles";
			final String RED_COLOR = "#ff0000";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);	
			
			Style redInteriorStyle = generator.createStyle();
			Interior redInterior = redInteriorStyle.interior();
			redInterior.setColor(RED_COLOR);
			assertTrue(redInterior == redInteriorStyle.interior());
			
			Style rightStyle = generator.createStyle(redInteriorStyle);
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Right);
			assertTrue(rightAlignment == rightStyle.alignment());	
			
			
			generator.startDocument();
			generator.startSheet(SHEET_CAPTION);
			
			generator.startRow();
			generator.writeCell(redInteriorStyle, "aa>>aa");
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
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			// Validate red style
			Element cellRedBackground = GeneratorTestUtils.searchCells(rows.get(0)).get(0);
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
			// Make sure the generator has written the solid pattern too
			assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));
			
			// Validate horizontal alignment style
			Element row4 = rows.get(4);
			Element rightCell = GeneratorTestUtils.searchCells(row4).get(0);
			assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
			
			// Validate inheritance
			Element rightStyleElement = searchStyle(doc, rightStyle.getId());
			assertEquals(redInteriorStyle.getId(), XmlTestUtils.getAttributeValue(rightStyleElement, "Parent", "ss"));
			assertEquals(RED_COLOR, getInteriorStyleAttribute(rightCell, "Color"));
			// Make sure the generator has written the solid pattern too
			assertEquals("Solid", getInteriorStyleAttribute(rightCell, "Pattern"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited interior styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testInheritNumberFormatStyle() {
		try {
			final String SHEET_CAPTION = "a sheet with inherited number format styles";
			final String NUMBER_FORMAT = "#####.000";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);	
			
			Style numberFormatStyle = generator.createStyle();
			NumberFormat nf = numberFormatStyle.numberFormat();
			nf.setFormat(NUMBER_FORMAT);
			assertTrue(nf == numberFormatStyle.numberFormat());
			
			Style rightStyle = generator.createStyle(numberFormatStyle);
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Left);
			assertTrue(rightAlignment == rightStyle.alignment());	
			
			
			generator.startDocument();
			generator.startSheet(SHEET_CAPTION);
			
			generator.startRow();
			generator.writeCell(numberFormatStyle, 134d);
			generator.closeRow();
			
			generator.emptyRow();
			generator.emptyRow();
			generator.emptyRow();
			
			generator.startRow();
			generator.writeCell(rightStyle, 155d);
			generator.closeRow();
			
			
			generator.closeSheet();
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);			
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			// Validate number format style
			Element cellNumberFormat = GeneratorTestUtils.searchCells(rows.get(0)).get(0);
			assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(cellNumberFormat, "Format"));
			
			
			// Validate horizontal alignment style
			Element row4 = rows.get(4);
			Element rightCell = GeneratorTestUtils.searchCells(row4).get(0);
			assertEquals(HorizontalAlignment.Left.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
			
			// Validate inheritance
			Element rightStyleElement = searchStyle(doc, rightStyle.getId());
			assertEquals(numberFormatStyle.getId(), XmlTestUtils.getAttributeValue(rightStyleElement, "Parent", "ss"));
			assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(rightCell, "Format"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with inherited number format styles -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
}