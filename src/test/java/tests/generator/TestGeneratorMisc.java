/**
 * 
 */
package tests.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static tests.generator.GeneratorTestUtils.getFontStyleAttribute;
import static tests.generator.GeneratorTestUtils.getInteriorStyleAttribute;

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

import tests.XmlTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.style.NumberFormat.Format;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

public class TestGeneratorMisc {

	@Test
	public void testEmptyDocument() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {		
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
			}
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
		}
		catch(Exception e) {
			fail();
		}
	}
	
	
	
	@Test
	public void createEmptyFile() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
			}
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
			
			final String TEXT_FIRST_ROW = "a<<4~~4~~aa";
			final String TEXT_FOURTH_ROW = "fesfe>>&&fsf";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
				generator.startSheet("My first sheet");
				generator.startRow();
				generator.writeCell(TEXT_FIRST_ROW);
				generator.closeRow();
				generator.emptyRow();
				generator.emptyRow();
				generator.startRow();
				generator.writeCell(TEXT_FOURTH_ROW);
				generator.closeRow();
				generator.closeSheet();
			}
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
			
			final String TEXT_FIRST_ROW = "<aaa<>";
			final Double NUMBER_THIRD_SHEET = 123.3d;
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
				generator.startSheet("a sheet");
				generator.emptyRow();
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
			}
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
			// Keep a reference to it just for verification purposes, should 
			//	never be needed out of the try-with-resources statemente in any
			//	other case
			Style dateStyle = null;
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
				dateStyle = generator.createStyle();
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
			}
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
	public void testEmptyRows() {
		try {
			final String SHEET_CAPTION = "a sheet with empty rows";
			final String BLUE_COLOR = "#0000ff";
			final String RED_COLOR = "#ff0000";
			final String GREEN_COLOR = "#00ff00";
			final Double BLUE_HEIGHT = 33d;
			final Double RED_HEIGHT = 12d;
			final Double GREEN_FONT_SIZE = 45d;
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
			
				Style blueBackground = generator.createStyle();
				Interior blueInterior = blueBackground.interior();
				blueInterior.setColor(BLUE_COLOR);
				assertTrue(blueInterior == blueBackground.interior());
				
				Style redBackground = generator.createStyle();
				Interior redInterior = redBackground.interior();
				redInterior.setColor(RED_COLOR);
				assertTrue(redInterior == redBackground.interior());
				
				Style greenBackground = generator.createStyle();
				Interior greenInterior = greenBackground.interior();
				greenInterior.setColor(GREEN_COLOR);
				assertTrue(greenInterior == greenBackground.interior());
				Font greenFont = greenBackground.font();
				greenFont.setSize(GREEN_FONT_SIZE);
				assertTrue(greenFont == greenBackground.font());
							
				generator.startDocument();
				
				generator.startSheet(SHEET_CAPTION);
				generator.startRow();
				generator.writeCell("Here come 3 empty blue rows");
				generator.closeRow();
				generator.writeEmptyRows(3l, null, BLUE_HEIGHT, blueBackground);			
				generator.startRow();
				generator.writeCell("After 3 empty rows");
				generator.closeRow();
				generator.emptyRow();
				generator.startRow();
				generator.writeCell("Here come 5 empty red rows");
				generator.closeRow();
				generator.writeEmptyRows(5l, null, RED_HEIGHT, redBackground);
				generator.startRow();
				generator.writeCell("After 5 empty red rows");
				generator.closeRow();
				generator.emptyRow();
				generator.startRow();
				generator.writeCell("Here comes an empty green row");
				generator.closeRow();
				generator.writeEmptyRows(null, true, null, greenBackground);
				generator.startRow();
				generator.writeCell("After an empty green row");
				generator.closeRow();
				
				generator.closeSheet();
			}
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);			
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			assertEquals(17, rows.size());
			
			// the second to the fourth should have blue background
			for (int indexBlue = 1; indexBlue < 4; indexBlue++) {
				// Its style should have blue background
				Element blueRow = rows.get(indexBlue);
				assertEquals(BLUE_COLOR,
					getInteriorStyleAttribute(blueRow, "Color"));
				assertEquals(NumberFormatHelper.format(BLUE_HEIGHT),
					XmlTestUtils.getAttributeValue(blueRow, "Height", "ss"));
			}
			
			// the eighth to twelfth should have red background
			for (int indexRed = 7; indexRed < 12; indexRed++) {
				// Its style should have blue background
				Element redRow = rows.get(indexRed);
				assertEquals(RED_COLOR,
					getInteriorStyleAttribute(redRow, "Color"));
				assertEquals(NumberFormatHelper.format(RED_HEIGHT),
					XmlTestUtils.getAttributeValue(redRow, "Height", "ss"));
			}
			
			// the fifteenth row should have green background
			Element greenRow = rows.get(15);
			assertEquals(GREEN_COLOR,
					getInteriorStyleAttribute(greenRow, "Color"));
			assertEquals(NumberFormatHelper.format(GREEN_FONT_SIZE),
					getFontStyleAttribute(greenRow, "Size"));
			
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with empty rows -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testColumns() {
		try {
			final String SHEET_CAPTION = "a sheet with columns";
			final String BLUE_BACKGROUND = "#0000ff";
			final String RED_BACKGROUND = "#ff0000";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try(XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
			
				Style styleBlueBackground = generator.createStyle();
				Interior blueInterior = styleBlueBackground.interior();
				blueInterior.setColor(BLUE_BACKGROUND);
				assertTrue(blueInterior == styleBlueBackground.interior());
				
				Style styleRedBackground = generator.createStyle();
				Interior redInterior = styleRedBackground.interior();
				redInterior.setColor(RED_BACKGROUND);
				assertTrue(redInterior == styleRedBackground.interior());
				
				Style styleItalicRedBackground = generator.createStyle();
				Interior redItalicInterior = styleItalicRedBackground.interior();
				redItalicInterior.setColor(RED_BACKGROUND);
				styleItalicRedBackground.font().setItalic(true);
				assertTrue(redItalicInterior == styleItalicRedBackground.interior());
				
				Style styleBlueBoldBackground = generator.createStyle();
				Interior blueBoldInterior = styleBlueBoldBackground.interior();
				styleBlueBoldBackground.font().setBold(true);
				styleBlueBoldBackground.numberFormat().setFormat(Format.Standard);
				blueBoldInterior.setColor(BLUE_BACKGROUND);
				assertTrue(blueBoldInterior == styleBlueBoldBackground.interior());
				
				generator.startDocument();
				generator.startSheet(SHEET_CAPTION);
				generator.startColumns();
				// first column
				generator.column(null, null, null, null, 2l, styleRedBackground, null);
				// second column: gap (width: 1)
				// third column
				generator.column(null, null, null, 4l, null, styleBlueBackground, 35d);
				// fourth column
				generator.column(styleItalicRedBackground, null);
				// fifth column
				generator.column(null, null, true, null, 2l, styleBlueBackground, null);
				// sixth column
				generator.column(null, null, null, null, 2l, styleRedBackground, 250d);
				// seventh column: gap (width: 2)
				// eighth column
				generator.column(null, true, null, 12l, 3l, styleBlueBoldBackground, null);
				// ninth column: gap (width: 1)
				// tenth column
				generator.column(null, null, null, 16l, null, styleRedBackground, null);
				// eleventh column: closing
				generator.closeColumns();
				for (int i = 0; i < 15; i++) {
					generator.startRow();
					for (double j = 0; j < 20; j++) {
						generator.writeCell(j);
					}
					generator.closeRow();
				}
				generator.closeSheet();
			}
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);		
			
			List<Element> columns = GeneratorTestUtils.searchColumns(doc, SHEET_CAPTION);
			// There is a column without styles closing the set and another two closing the gaps
			//	before #4 , #9 and #15
			assertEquals(11, columns.size());
			
			// Test column styles
			Element col0 = columns.get(0);
			// Span = 2 -> an additional column
			// red background style
			assertEquals(NumberFormatHelper.format(1d), 
					XmlTestUtils.getAttributeValue(col0, "Span", "ss"));
			assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col0, "Color"));
			
			// Gap of 1
			Element col1 = columns.get(1);
			verifyGap(col1, 1);
			
			// blue background
			// width of 35d
			Element col2 = columns.get(2);
			assertEquals(NumberFormatHelper.format(4d), 
					XmlTestUtils.getAttributeValue(col2, "Index", "ss"));
			assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col2, "Color"));
			assertEquals(NumberFormatHelper.format(35d), 
					XmlTestUtils.getAttributeValue(col2, "Width", "ss"));
			
			// italic, red background
			Element col3 = columns.get(3);
			assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col3, "Color"));
			assertEquals(BooleanFormatHelper.format(Boolean.TRUE), 
					getFontStyleAttribute(col3, "Italic"));
			
			// blue background
			/// Span = 2 -> 1 additional column
			Element col4 = columns.get(4);
			assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col4, "Color"));
			assertEquals(NumberFormatHelper.format(1d), 
					XmlTestUtils.getAttributeValue(col0, "Span", "ss"));
			
			// red background
			// width: 250
			// span = 2 -> 1 additional column
			Element col5 = columns.get(5);
			assertEquals(NumberFormatHelper.format(1d), 
					XmlTestUtils.getAttributeValue(col5, "Span", "ss"));
			assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col5, "Color"));
			assertEquals(NumberFormatHelper.format(250d), 
					XmlTestUtils.getAttributeValue(col5, "Width", "ss"));
			
			// Gap of 2
			Element col6 = columns.get(6);
			verifyGap(col6, 2);
			
			// blue background
			// index = 12
			// span = 3 -> 2 additional columns
			Element col7 = columns.get(7);
			assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col7, "Color"));
			assertEquals(NumberFormatHelper.format(12d), 
					XmlTestUtils.getAttributeValue(col7, "Index", "ss"));
			assertEquals(NumberFormatHelper.format(2d), 
					XmlTestUtils.getAttributeValue(col7, "Span", "ss"));
			
			// Gap of 1
			Element col8 = columns.get(8);
			verifyGap(col8, 1);
			
			// index = 16
			// red background
			Element col9 = columns.get(9);
			assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col9, "Color"));
			assertEquals(NumberFormatHelper.format(16d), 
					XmlTestUtils.getAttributeValue(col9, "Index", "ss"));
			
			// last column: the closing one
			Element col10 = columns.get(10);
			assertTrue(null == XmlTestUtils.getAttributeValue(col10, "StyleID", "ss"));
			assertTrue(null == XmlTestUtils.getAttributeValue(col10, "Span", "ss"));
			assertTrue(null == XmlTestUtils.getAttributeValue(col10, "Index", "ss"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with columns -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	// Asserts that the element is a column and is empty
	private void verifyGap(Element column, int width) throws JDOMException {
		assertEquals(column.getName(), "Column");
		if (width > 1) {
			assertEquals(NumberFormatHelper.format(new Double(width - 1)), 
				XmlTestUtils.getAttributeValue(column, "Span", "ss"));
		}
		else if (width == 1) {
			assertTrue(null == XmlTestUtils.getAttributeValue(column, "Span", "ss"));
		}
		// No style
		assertTrue(null == GeneratorTestUtils.searchStyle(column.getDocument(), column));
	}
	
	@Test
	public void controlIOerror() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os)) {
				generator.startDocument();
				generator.startSheet("this will fail");
				generator.startRow();
				// This will make the generator to fail, not later than the call to
				//	closeDocument
				os.close();
				generator.writeCell("aaa");
				generator.closeRow();
				generator.closeSheet();
			}
			fail(); // Should not get here!
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
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet(null);  // Should jump with an XMLSpreadsheetException
			fail();  // Should not get here!
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
	public void testMisplacedColumn() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet("This will fail");
			generator.startRow();
			generator.column(null, 34d); // Should pop an exception
			fail(); // Should not get here!
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
	public void testOverlapColumns() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			generator.startDocument();
			generator.startSheet("This will fail");
			generator.startColumns();
			generator.column(null, null, null, null, 2L, null, 15d); // 2 columns
			generator.column(null, null, null, 2L, null, null, 34d); // Should pop an exception: tried to fit it
																	 // into index=2, but it is already occupied
			fail(); // Should not get here!
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
