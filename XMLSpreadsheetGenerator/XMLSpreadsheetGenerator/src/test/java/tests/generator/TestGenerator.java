/**
 * 
 */
package tests.generator;

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
import org.junit.Assert;
import org.junit.Test;

import test.XmlTestUtils;
import tests.styles.StyleTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
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
			Assert.assertNotNull(doc);
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
			Assert.assertNotNull(doc);
			// Validate the position of cells
			// Are there 4 rows?
			List<Element> rows = GeneratorTestUtils.searchRows(doc, "My first sheet");
			Assert.assertEquals(4, rows.size());
			// For each row
			Element firstRow = rows.get(0);
			List<Element> firstRowCells = GeneratorTestUtils.searchCells(firstRow);
			Assert.assertEquals(1, firstRowCells.size());
			Element cell = firstRowCells.get(0);
			Assert.assertEquals(TEXT_FIRST_ROW, ((Element)cell.getContent().get(0)).getText());
			// 2nd and 3rd left empty
			Element fourthRow = rows.get(3);
			List<Element> fourthRowCells = GeneratorTestUtils.searchCells(fourthRow);
			Assert.assertEquals(1, fourthRowCells.size());
			cell = fourthRowCells.get(0);
			Assert.assertEquals(TEXT_FOURTH_ROW, ((Element)cell.getContent().get(0)).getText());
			
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
			Assert.assertNotNull(doc);
			List<Element> rows1 = GeneratorTestUtils.searchRows(doc, "a sheet");
			Assert.assertEquals(2, rows1.size());
			List<Element> cells1 = GeneratorTestUtils.searchCells(rows1.get(1));
			Assert.assertEquals(TEXT_FIRST_ROW, ((Element)cells1.get(0).getContent().get(0)).getText());
			List<Element> rows2 = GeneratorTestUtils.searchRows(doc, "yet another sheet");
			Assert.assertEquals(0, rows2.size());
			List<Element> rows3 = GeneratorTestUtils.searchRows(doc, "the third sheet!");
			Assert.assertEquals(1, rows3.size());
			List<Element> cells3 = GeneratorTestUtils.searchCells(rows3.get(0));
			Assert.assertEquals(2, cells3.size());
			Assert.assertEquals(NumberFormatHelper.format(NUMBER_THIRD_SHEET), 
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
			dateStyle.numberFormat().setFormat(DATE_FORMAT);
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
			Assert.assertNotNull(doc);
			List<Element> rows1 = GeneratorTestUtils.searchRows(doc, "a sheet with dates");
			Assert.assertEquals(2, rows1.size());
			// test date formats
			Element cellNoFormat = GeneratorTestUtils.searchCells(rows1.get(0)).get(0);
			Assert.assertNotEquals(dateStyle.getId(), XmlTestUtils.getAttributeValue(cellNoFormat, "StyleID", "ss"));
			Element cellWithFormat = GeneratorTestUtils.searchCells(rows1.get(1)).get(0);
			Assert.assertEquals(dateStyle.getId(), XmlTestUtils.getAttributeValue(cellWithFormat, "StyleID", "ss"));
			
			Element style = GeneratorTestUtils.searchStyle(doc, 
				XmlTestUtils.getAttributeValue(cellWithFormat, "StyleID", "ss"));
			Assert.assertNotNull(style);
			// Get the format in the style
			Element numberFormat = ((Element)style.getContent().get(0));
			Assert.assertEquals(DATE_FORMAT, XmlTestUtils.getAttributeValue(numberFormat, "Format", "ss"));
			
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
	public void testBorders() {
		try {
			final Double FORMAT_WEIGHT = 2.0d;
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			Style lightBorderStyle = generator.createStyle();
			lightBorderStyle.borders().createBorder(BorderPosition.Bottom).setWeight(BorderWeight.Thin);
			Style mediumBorderStyle = generator.createStyle();
			mediumBorderStyle.borders().createBorder(BorderPosition.Bottom).setWeight(BorderWeight.Medium);
			Style thickBorderStyle = generator.createStyle();
			thickBorderStyle.borders().createBorder(BorderPosition.Bottom).setWeight(BorderWeight.Thick);
			Style customBorderStyle = generator.createStyle();
			customBorderStyle.borders().createBorder(BorderPosition.Bottom).setWeight(FORMAT_WEIGHT);
			// Empty borders
			Style emptyBorderStyle = generator.createStyle();
			emptyBorderStyle.borders();
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
			Assert.assertNotNull(doc);			
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, "a sheet with border styles");
			// remember empty rows left for space
			Assert.assertEquals(10, rows.size());
			// Check the bottom border position for every even row
			// Light
			Assert.assertEquals(
					NumberFormatHelper.format(BorderWeight.Thin.getValue()), 
					bottomBorderStyle(GeneratorTestUtils.searchCells(rows.get(1)).get(0)));
			Assert.assertEquals(
					NumberFormatHelper.format(BorderWeight.Medium.getValue()), 
					bottomBorderStyle(GeneratorTestUtils.searchCells(rows.get(3)).get(0)));
			Assert.assertEquals(
					NumberFormatHelper.format(BorderWeight.Thick.getValue()), 
					bottomBorderStyle(GeneratorTestUtils.searchCells(rows.get(5)).get(0)));
			Assert.assertEquals(
					NumberFormatHelper.format(FORMAT_WEIGHT), 
					bottomBorderStyle(GeneratorTestUtils.searchCells(rows.get(7)).get(0)));
			// Empty style
			
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
	private String bottomBorderStyle(Element cell) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(cell.getDocument(), 
			 XmlTestUtils.getAttributeValue(cell, "StyleID", "ss"));
		List<Element> bottomBorder = 
			XmlTestUtils.getDescendants(style, "ss:Borders/ss:Border[@ss:Position='Bottom']");
		if (bottomBorder != null && bottomBorder.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(bottomBorder.get(0), "Weight", "ss");
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
			Assert.assertNotNull(xse);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
