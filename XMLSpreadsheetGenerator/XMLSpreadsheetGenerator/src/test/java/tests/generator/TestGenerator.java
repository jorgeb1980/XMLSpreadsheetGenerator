/**
 * 
 */
package tests.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Test;

import test.XmlTestUtils;
import tests.styles.StyleTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Borders;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Font.VerticalAlignment;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

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
			assertNotNull(doc);
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
			assertNotNull(doc);
			
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
			assertNotNull(doc);
			
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
			assertNotNull(doc);
			
			StyleTestUtils.checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Borders/ss:Border", "Position", BorderPosition.Bottom.toString());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createEmptyFile() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.closeDocument();
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created empty file -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createFileCells() {
		try {
			
			final String TEXT_FIRST_ROW = "aaa";
			final String TEXT_FOURTH_ROW = "fesfefsf";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet("My first sheet");
			generator.startRow();
			generator.writeCell(TEXT_FIRST_ROW);
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(TEXT_FOURTH_ROW);
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			// Validate the position of cells
			// Are there 4 rows?
			List<Element> rows = GeneratorTestUtils.searchRows(doc, "My first sheet");
			assertEquals(4, rows.size());
			// For each row
			Element firstRow = rows.get(0);
			List<Element> firstRowCells = GeneratorTestUtils.searchCells(firstRow);
			assertEquals(1, firstRowCells.size());
			Element cell = firstRowCells.get(0);
			assertEquals(TEXT_FIRST_ROW, ((Element)cell.getContent().get(0)).getText());
			// 2nd and 3rd left empty
			Element fourthRow = rows.get(3);
			List<Element> fourthRowCells = GeneratorTestUtils.searchCells(fourthRow);
			assertEquals(1, fourthRowCells.size());
			cell = fourthRowCells.get(0);
			assertEquals(TEXT_FOURTH_ROW, ((Element)cell.getContent().get(0)).getText());
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with cells -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void createFileSheets() {
		try {
			
			final String TEXT_FIRST_ROW = "aaa";
			final Double NUMBER_THIRD_SHEET = 123.3d;
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet("a sheet");
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(TEXT_FIRST_ROW);
			generator.closeRow();
			generator.closeSheet();
			generator.startSheet("yet another sheet");
			generator.closeSheet();
			generator.startSheet("the third sheet!");
			generator.startRow();
			generator.writeEmptyCell();
			generator.writeCell(NUMBER_THIRD_SHEET);
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			List<Element> rows1 = GeneratorTestUtils.searchRows(doc, "a sheet");
			assertEquals(2, rows1.size());
			List<Element> cells1 = GeneratorTestUtils.searchCells(rows1.get(1));
			assertEquals(TEXT_FIRST_ROW, ((Element)cells1.get(0).getContent().get(0)).getText());
			List<Element> rows2 = GeneratorTestUtils.searchRows(doc, "yet another sheet");
			assertEquals(0, rows2.size());
			List<Element> rows3 = GeneratorTestUtils.searchRows(doc, "the third sheet!");
			assertEquals(1, rows3.size());
			List<Element> cells3 = GeneratorTestUtils.searchCells(rows3.get(0));
			assertEquals(2, cells3.size());
			assertEquals(NumberFormatHelper.format(NUMBER_THIRD_SHEET), 
					((Element)cells3.get(1).getContent().get(0)).getText());
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with sheets -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDates() {
		try {
			
			final Date NO_FORMAT_DATE = new Date();
			final Date FORMAT_DATE = new Date();
			final String DATE_FORMAT = "dd\\-mm\\.yyyy";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			Style dateStyle = generator.createStyle();
			NumberFormat numberFormatObj = dateStyle.numberFormat();
			numberFormatObj.setFormat(DATE_FORMAT);
			assertTrue(numberFormatObj == dateStyle.numberFormat());
			generator.startDocument();
			generator.startSheet("a sheet with dates");
			generator.startRow();
			generator.writeCell(NO_FORMAT_DATE);
			generator.closeRow();
			generator.startRow();
			generator.writeCell(dateStyle, FORMAT_DATE);
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			List<Element> rows1 = GeneratorTestUtils.searchRows(doc, "a sheet with dates");
			assertEquals(2, rows1.size());
			// test date formats
			Element cellNoFormat = GeneratorTestUtils.searchCells(rows1.get(0)).get(0);
			assertNotEquals(dateStyle.getId(), XmlTestUtils.getAttributeValue(cellNoFormat, "StyleID", "ss"));
			Element cellWithFormat = GeneratorTestUtils.searchCells(rows1.get(1)).get(0);
			assertEquals(dateStyle.getId(), XmlTestUtils.getAttributeValue(cellWithFormat, "StyleID", "ss"));
			
			Element style = GeneratorTestUtils.searchStyle(doc, 
				XmlTestUtils.getAttributeValue(cellWithFormat, "StyleID", "ss"));
			assertNotNull(style);
			// Get the format in the style
			Element numberFormat = ((Element)style.getContent().get(0));
			assertEquals(DATE_FORMAT, XmlTestUtils.getAttributeValue(numberFormat, "Format", "ss"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with dates -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testFont() {
		try {
			final String GREEN_COLOR = "#00ff00";
			final String BLUE_COLOR = "#0000ff";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
				
			Style bold = generator.createStyle();
			Font boldFont = bold.font();
			boldFont.setBold(true);
			assertTrue(boldFont == bold.font());
			
			Style italic = generator.createStyle();
			Font italicFont = italic.font();
			italicFont.setItalic(true);
			assertTrue(italicFont == italic.font());
			
			Style color = generator.createStyle();
			Font colorFont = color.font();
			colorFont.setColor(GREEN_COLOR);
			assertTrue(colorFont == color.font());
			
			Style blueBold = generator.createStyle();
			Font blueBoldFont = blueBold.font();
			blueBoldFont.setColor(BLUE_COLOR);
			blueBoldFont.setBold(true);
			assertTrue(blueBoldFont == blueBold.font());
			
			Style bottom = generator.createStyle();
			Font bottomFont = bottom.font();
			bottomFont.setVerticalAlign(VerticalAlignment.Subscript);
			assertTrue(bottomFont == bottom.font());
			
			Style big = generator.createStyle();
			Font bigFont = big.font();
			bigFont.setSize(20.0d);
			assertTrue(bigFont == big.font());
			
			generator.startDocument();
			generator.startSheet("a sheet with font styles");
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(bold, new Date());
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(italic, "lalalalala");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(color, "this is green");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(blueBold, "this is blue and bold");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(bottom, "this is a subscript");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(big, "this is a 20 size text");
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, "a sheet with font styles");
			// Validate bold style
			Element row1 = rows.get(1);
			Element boldCell = GeneratorTestUtils.searchCells(row1).get(0);
			assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(boldCell, "Bold"));
			
			// Validate italic style
			Element row3 = rows.get(3);
			Element italicCell = GeneratorTestUtils.searchCells(row3).get(0);
			assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(italicCell, "Italic"));
			
			// Validate green style
			Element row5 = rows.get(5);
			Element greenCell = GeneratorTestUtils.searchCells(row5).get(0);
			assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));
			
			// Validate blue and bold style
			Element row7 = rows.get(7);
			Element blueBoldCell = GeneratorTestUtils.searchCells(row7).get(0);
			assertEquals(BLUE_COLOR, getFontStyleAttribute(blueBoldCell, "Color"));
			assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(blueBoldCell, "Bold"));
			
			// Validate vertical alignment style
			Element row9 = rows.get(9);
			Element verticalCell = GeneratorTestUtils.searchCells(row9).get(0);
			assertEquals(VerticalAlignment.Subscript.toString(), getFontStyleAttribute(verticalCell, "VerticalAlign"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with font styles -> " + file.getAbsolutePath());	
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}
	
	private String getFontStyleAttribute(Element cell, String attribute) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(cell.getDocument(), cell);
		List<Element> font =
			XmlTestUtils.getDescendants(style, "ss:Font");
		if (font != null && font.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(font.get(0), attribute, "ss");
		}
		else {
			fail();
		}
		return ret;
	}
	
	@Test
	public void testBorders() {
		try {
			final Double FORMAT_WEIGHT = 2.0d;
			final String RED_COLOR = "#ff0000";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style lightBorderStyle = generator.createStyle();
			Borders lightBorders = lightBorderStyle.borders();
			lightBorders.createBorder(BorderPosition.Bottom).setWeight(BorderWeight.Thin);
			assertTrue(lightBorders == lightBorderStyle.borders());
			Style mediumBorderStyle = generator.createStyle();
			Borders mediumBorders = mediumBorderStyle.borders();
			Border mediumBorder = mediumBorders.createBorder(BorderPosition.Bottom);
			mediumBorder.setWeight(BorderWeight.Medium);
			mediumBorder.setColor(RED_COLOR);
			assertTrue(mediumBorders == mediumBorderStyle.borders());
			Style thickBorderStyle = generator.createStyle();
			Borders thickBorders = thickBorderStyle.borders();
			Border thickBorder = thickBorders.createBorder(BorderPosition.Bottom);
			thickBorder.setWeight(BorderWeight.Thick);
			thickBorder.setLineStyle(LineStyle.Dash);
			assertTrue(thickBorders == thickBorderStyle.borders());
			Style customBorderStyle = generator.createStyle();
			Borders customBorders = customBorderStyle.borders();
			customBorders.createBorder(BorderPosition.Bottom).setWeight(FORMAT_WEIGHT);
			assertTrue(customBorders == customBorderStyle.borders());
			// Empty borders
			Style emptyBorderStyle = generator.createStyle();
			Borders emptyBorders = emptyBorderStyle.borders();
			assertTrue(emptyBorders == emptyBorderStyle.borders());
			generator.startDocument();
			generator.startSheet("a sheet with border styles");
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(lightBorderStyle, "aaa");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(mediumBorderStyle, "bbb");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(thickBorderStyle, "ccc");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(customBorderStyle, "ddd");
			generator.closeRow();
			generator.startRow(); generator.closeRow();
			generator.startRow();
			generator.writeCell(emptyBorderStyle, "eee");
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);			
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, "a sheet with border styles");
			// remember empty rows left for space
			assertEquals(10, rows.size());
			// Check the bottom border position for every even row
			// Light
			assertEquals(
				NumberFormatHelper.format(BorderWeight.Thin.getValue()), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(1)).get(0), BorderPosition.Bottom.toString(), "Weight"));
			Element mediumBorderCell = GeneratorTestUtils.searchCells(rows.get(3)).get(0);
			assertEquals(
					NumberFormatHelper.format(BorderWeight.Medium.getValue()), 
					getBorderStyleAttribute(mediumBorderCell, BorderPosition.Bottom.toString(), "Weight"));
			assertEquals(RED_COLOR, getBorderStyleAttribute(mediumBorderCell, BorderPosition.Bottom.toString(), "Color"));
			
			assertEquals(
				NumberFormatHelper.format(BorderWeight.Thick.getValue()), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(5)).get(0), BorderPosition.Bottom.toString(), "Weight"));
			assertEquals(
				NumberFormatHelper.format(FORMAT_WEIGHT), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(7)).get(0), BorderPosition.Bottom.toString(), "Weight"));
			// Empty style
			Element cellEmptyStyle = GeneratorTestUtils.searchCells(rows.get(9)).get(0);
			Element xmlEmptyStyle = GeneratorTestUtils.searchStyle(cellEmptyStyle.getDocument(),  
					XmlTestUtils.getAttributeValue(cellEmptyStyle, "StyleID", "ss"));
			assertEquals(0, xmlEmptyStyle.getChildren().size());
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with border styles -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	// Style of the bottom border
	private String getBorderStyleAttribute(Element cell, String position, String attribute) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(cell.getDocument(), cell);
		List<Element> bottomBorder = 
			XmlTestUtils.getDescendants(style, "ss:Borders/ss:Border[@ss:Position='" + position + "']");
		if (bottomBorder != null && bottomBorder.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(bottomBorder.get(0), attribute, "ss");
		}
		else {
			fail();
		}
		return ret;
	}
	
	@Test
	public void controlIOerror() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.startRow();
			// This will make the generator to fail, not later than the call to
			//	closeDocument
			os.close();
			generator.writeCell("aaa");
			generator.closeRow();
			generator.closeSheet();
			generator.closeDocument();
			fail();
		}
		catch(XMLSpreadsheetException xse) {
			assertNotNull(xse);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testStartNamelessSheet() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet(null);
			fail();
		}
		catch(XMLSpreadsheetException xse) {
			assertNotNull(xse);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
