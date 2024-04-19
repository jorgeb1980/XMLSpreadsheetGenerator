package tests.generator;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static tests.XmlTestUtils.executeWithTempFile;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.generator.GeneratorTestUtils.*;
import static xml.spreadsheet.style.NumberFormat.STANDARD;

public class TestGeneratorMisc {

	@Test
	public void testEmptyDocument() {
		var baos = new ByteArrayOutputStream();
		try {		
			try (var generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
			}
			var document = baos.toString(Charset.forName("cp1252"));
			var doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
		}
		catch(Exception e) {
			fail();
		}
	}

	@Test
	public void createEmptyFile() {
		executeWithTempFile( baos -> {
			try {
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					generator.startDocument();
				}
				var document = baos.toString(Charset.forName("cp1252"));
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void createFileCells() {
		executeWithTempFile( baos -> {
			try {
				final var TEXT_FIRST_ROW = "a<<4~~4~~aa";
				final var TEXT_FOURTH_ROW = "fesfe>>&&fsf";

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
				var document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);
				// Validate the position of cells
				// Are there 4 rows?
				var rows = searchRows(doc, "My first sheet");
				assertEquals(4, rows.size());
				// For each row
				var firstRow = rows.get(0);
				var firstRowCells = searchCells(firstRow);
				assertEquals(1, firstRowCells.size());
				var cell = firstRowCells.get(0);
				assertEquals(TEXT_FIRST_ROW, ((Element) cell.getContent().get(0)).getText());
				// 2nd and 3rd left empty
				var fourthRow = rows.get(3);
				var fourthRowCells = searchCells(fourthRow);
				assertEquals(1, fourthRowCells.size());
				cell = fourthRowCells.get(0);
				assertEquals(TEXT_FOURTH_ROW, ((Element) cell.getContent().get(0)).getText());
			}
			catch(Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void createFileSheets() {
		executeWithTempFile( baos -> {
			try {
				final var TEXT_FIRST_ROW = "<aaa<>";
				final var NUMBER_THIRD_SHEET = 123.3d;

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
				var document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);
				var rows1 = searchRows(doc, "a sheet");
				assertEquals(2, rows1.size());
				var cells1 = searchCells(rows1.get(1));
				assertEquals(TEXT_FIRST_ROW, ((Element) cells1.get(0).getContent().get(0)).getText());
				var rows2 = searchRows(doc, "yet another sheet");
				assertEquals(0, rows2.size());
				var rows3 = searchRows(doc, "the third sheet!");
				assertEquals(1, rows3.size());
				var cells3 = searchCells(rows3.get(0));
				assertEquals(2, cells3.size());
				assertEquals(NumberFormatHelper.format(NUMBER_THIRD_SHEET),
					((Element) cells3.get(1).getContent().get(0)).getText());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testDates() {
		executeWithTempFile( baos -> {
			try {
				final var NO_FORMAT_DATE = new Date();
				final var FORMAT_DATE = new Date();
				final var DATE_FORMAT = "dd\\-mm\\.yyyy";

				// Keep a reference to it just for verification purposes, should
				//	never be needed out of the try-with-resources statemente in any
				//	other case
				Style dateStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					NumberFormat numberFormatObj = new NumberFormat(DATE_FORMAT);
					dateStyle = generator.createStyle()
						.withNumberFormat(numberFormatObj).build();
					assertSame(numberFormatObj, dateStyle.numberFormat());
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
				var document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);
				var rows1 = searchRows(doc, "a sheet with dates");
				assertEquals(2, rows1.size());
				// test date formats
				var cellNoFormat = searchCells(rows1.get(0)).get(0);
				assertNotEquals(dateStyle.id(), getAttributeValue(cellNoFormat, "StyleID", "ss"));
				var cellWithFormat = searchCells(rows1.get(1)).get(0);
				assertEquals(dateStyle.id(), getAttributeValue(cellWithFormat, "StyleID", "ss"));

				var style = GeneratorTestUtils.searchStyle(doc,
					getAttributeValue(cellWithFormat, "StyleID", "ss"));
				assertNotNull(style);
				// Get the format in the style
				var numberFormat = ((Element) style.getContent().get(0));
				assertEquals(DATE_FORMAT, getAttributeValue(numberFormat, "Format", "ss"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testEmptyRows() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with empty rows";
				final var BLUE_COLOR = "#0000ff";
				final var RED_COLOR = "#ff0000";
				final var GREEN_COLOR = "#00ff00";
				final var BLUE_HEIGHT = 33d;
				final var RED_HEIGHT = 12d;
				final var GREEN_FONT_SIZE = 45d;

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {

					var blueInterior = Interior.builder().withColor(BLUE_COLOR).build();
					var blueBackground = generator.createStyle().withInterior(blueInterior).build();
					assertSame(blueInterior, blueBackground.interior());

					Interior redInterior = Interior.builder().withColor(RED_COLOR).build();
					var redBackground = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, redBackground.interior());

					var greenInterior = Interior.builder().withColor(GREEN_COLOR).build();
					var greenFont = Font.builder().withSize(GREEN_FONT_SIZE).build();
					var greenBackground = generator.createStyle().
						withInterior(greenInterior).
						withFont(greenFont).
						build();
					assertSame(greenInterior, greenBackground.interior());
					assertSame(greenFont, greenBackground.font());

					generator.startDocument();

					generator.startSheet(SHEET_CAPTION);
					generator.startRow();
					generator.writeCell("Here come 3 empty blue rows");
					generator.closeRow();
					generator.writeEmptyRows(3L, null, BLUE_HEIGHT, blueBackground);
					generator.startRow();
					generator.writeCell("After 3 empty rows");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell("Here come 5 empty red rows");
					generator.closeRow();
					generator.writeEmptyRows(5L, null, RED_HEIGHT, redBackground);
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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				assertEquals(17, rows.size());

				// the second to the fourth should have blue background
				for (int indexBlue = 1; indexBlue < 4; indexBlue++) {
					// Its style should have blue background
					var blueRow = rows.get(indexBlue);
					assertEquals(BLUE_COLOR,
						getInteriorStyleAttribute(blueRow, "Color"));
					assertEquals(NumberFormatHelper.format(BLUE_HEIGHT),
						getAttributeValue(blueRow, "Height", "ss"));
				}

				// the eighth to twelfth should have red background
				for (int indexRed = 7; indexRed < 12; indexRed++) {
					// Its style should have blue background
					var redRow = rows.get(indexRed);
					assertEquals(RED_COLOR,
						getInteriorStyleAttribute(redRow, "Color"));
					assertEquals(NumberFormatHelper.format(RED_HEIGHT),
						getAttributeValue(redRow, "Height", "ss"));
				}

				// the fifteenth row should have green background
				var greenRow = rows.get(15);
				assertEquals(GREEN_COLOR,
					getInteriorStyleAttribute(greenRow, "Color"));
				assertEquals(NumberFormatHelper.format(GREEN_FONT_SIZE),
					getFontStyleAttribute(greenRow, "Size"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testColumns() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with columns";
				final var BLUE_BACKGROUND = "#0000ff";
				final var RED_BACKGROUND = "#ff0000";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {


					var blueInterior = Interior.builder().withColor(BLUE_BACKGROUND).build();
					var styleBlueBackground = generator.createStyle().withInterior(blueInterior).build();
					assertSame(blueInterior, styleBlueBackground.interior());

					var redInterior = Interior.builder().withColor(RED_BACKGROUND).build();
					var styleRedBackground = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, styleRedBackground.interior());

					var redItalicInterior = Interior.builder().withColor(RED_BACKGROUND).build();
					var styleItalicRedBackground = generator.createStyle().
						withInterior(redItalicInterior).
						withFont(Font.builder().withItalic(true).build()).
						build();
					assertSame(redItalicInterior, styleItalicRedBackground.interior());

					var blueBoldInterior = Interior.builder().withColor(BLUE_BACKGROUND).build();
					var styleBlueBoldBackground = generator.createStyle().
						withInterior(blueBoldInterior).
						withFont(Font.builder().withBold(true).build()).
						withNumberFormat(STANDARD).
						build();
					assertSame(blueBoldInterior, styleBlueBoldBackground.interior());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);
					generator.startColumns();
					// first column
					generator.column(null, null, null, null, 2L, styleRedBackground, null);
					// second column: gap (width: 1)
					// third column
					generator.column(null, null, null, 4L, null, styleBlueBackground, 35d);
					// fourth column
					generator.column(styleItalicRedBackground, null);
					// fifth column
					generator.column(null, null, true, null, 2L, styleBlueBackground, null);
					// sixth column
					generator.column(null, null, null, null, 2L, styleRedBackground, 250d);
					// seventh column: gap (width: 2)
					// eighth column
					generator.column(null, true, null, 12L, 3L, styleBlueBoldBackground, null);
					// ninth column: gap (width: 1)
					// tenth column
					generator.column(null, null, null, 16L, null, styleRedBackground, null);
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

				var document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				var doc = GeneratorTestUtils.parseDocument(document);
				assertNotNull(doc);

				var columns = GeneratorTestUtils.searchColumns(doc, SHEET_CAPTION);
				// There is a column without styles closing the set and another two closing the gaps
				//	before #4 , #9 and #15
				assertEquals(11, columns.size());

				// Test column styles
				var col0 = columns.get(0);
				// Span = 2 -> an additional column
				// red background style
				assertEquals(NumberFormatHelper.format(1d),
					getAttributeValue(col0, "Span", "ss"));
				assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col0, "Color"));

				// Gap of 1
				var col1 = columns.get(1);
				verifyGap(col1, 1);

				// blue background
				// width of 35d
				var col2 = columns.get(2);
				assertEquals(NumberFormatHelper.format(4d),
					getAttributeValue(col2, "Index", "ss"));
				assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col2, "Color"));
				assertEquals(NumberFormatHelper.format(35d),
					getAttributeValue(col2, "Width", "ss"));

				// italic, red background
				var col3 = columns.get(3);
				assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col3, "Color"));
				assertEquals(BooleanFormatHelper.format(Boolean.TRUE),
					getFontStyleAttribute(col3, "Italic"));

				// blue background
				/// Span = 2 -> 1 additional column
				var col4 = columns.get(4);
				assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col4, "Color"));
				assertEquals(NumberFormatHelper.format(1d),
					getAttributeValue(col0, "Span", "ss"));

				// red background
				// width: 250
				// span = 2 -> 1 additional column
				var col5 = columns.get(5);
				assertEquals(NumberFormatHelper.format(1d),
					getAttributeValue(col5, "Span", "ss"));
				assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col5, "Color"));
				assertEquals(NumberFormatHelper.format(250d),
					getAttributeValue(col5, "Width", "ss"));

				// Gap of 2
				var col6 = columns.get(6);
				verifyGap(col6, 2);

				// blue background
				// index = 12
				// span = 3 -> 2 additional columns
				var col7 = columns.get(7);
				assertEquals(BLUE_BACKGROUND, getInteriorStyleAttribute(col7, "Color"));
				assertEquals(NumberFormatHelper.format(12d),
					getAttributeValue(col7, "Index", "ss"));
				assertEquals(NumberFormatHelper.format(2d),
					getAttributeValue(col7, "Span", "ss"));

				// Gap of 1
				var col8 = columns.get(8);
				verifyGap(col8, 1);

				// index = 16
				// red background
				var col9 = columns.get(9);
				assertEquals(RED_BACKGROUND, getInteriorStyleAttribute(col9, "Color"));
				assertEquals(NumberFormatHelper.format(16d),
					getAttributeValue(col9, "Index", "ss"));

				// last column: the closing one
				var col10 = columns.get(10);
				assertNull(getAttributeValue(col10, "StyleID", "ss"));
				assertNull(getAttributeValue(col10, "Span", "ss"));
				assertNull(getAttributeValue(col10, "Index", "ss"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	// Asserts that the element is a column and is empty
	private void verifyGap(Element column, int width) throws JDOMException {
		assertEquals(column.getName(), "Column");
		if (width > 1) {
			assertEquals(NumberFormatHelper.format(width - 1d),
				getAttributeValue(column, "Span", "ss"));
		} else if (width == 1) {
			assertNull(getAttributeValue(column, "Span", "ss"));
		}
		// No style
		assertNull(searchStyle(column.getDocument(), column));
	}
	
	@Test
	public void controlIOerror() {
		try {
			var file = File.createTempFile("xmlspreadsheet", ".xml");
			try (var os = new FileOutputStream(file)) {
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
			} catch (XMLSpreadsheetException xse) {
				assertNotNull(xse);
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			} finally {
				Files.delete(file.toPath());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Test 
	public void testStartNamelessSheet() {
		try {
			var baos = new ByteArrayOutputStream();
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			var generator = new XMLSpreadsheetGenerator(baos);
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
			var baos = new ByteArrayOutputStream();
			// Don't mind here to have a warning that the resource is never closed
			var generator = new XMLSpreadsheetGenerator(baos);
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
			var baos = new ByteArrayOutputStream();
			// Don't mind here to have a warning that the resource is never closed
			var generator = new XMLSpreadsheetGenerator(baos);
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
