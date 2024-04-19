package tests.generator;

import org.junit.jupiter.api.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.*;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static tests.XmlTestUtils.executeWithTempFile;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.generator.GeneratorTestUtils.*;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Alignment.HorizontalAlignment.*;
import static xml.spreadsheet.style.Alignment.VerticalAlignment.Top;
import static xml.spreadsheet.style.Border.BorderPosition.Bottom;
import static xml.spreadsheet.style.Border.BorderWeight.Medium;
import static xml.spreadsheet.style.Border.BorderWeight.Thin;
import static xml.spreadsheet.style.Border.LineStyle.Dash;
import static xml.spreadsheet.style.Font.VerticalAlignment.Subscript;

public class TestGeneratorStyles {
	
	@Test
	public void testCreateStyleAfterStarting() {
		try(var baos = new ByteArrayOutputStream()) {
			// Don't mind here to have a warning that the resource is never closed
			try (var generator = new XMLSpreadsheetGenerator(baos)) {
				generator.startDocument();
				generator.createStyle();
				fail();
			}
		} catch(XMLSpreadsheetException e) {
			// success
		} catch(Exception e) {
			fail();
		}
	}

	@Test
	public void testCreateStyleAfterEnding() {
		try (var baos = new ByteArrayOutputStream();
			 var generator = new XMLSpreadsheetGenerator(baos)) {
			generator.startDocument();
			generator.close();
			
			generator.createStyle();
			fail();
		} catch(XMLSpreadsheetException e) {
			// success
		} catch(Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCreateEmptyStyle() {
		try(var baos = new ByteArrayOutputStream()) {
			try (var generator = new XMLSpreadsheetGenerator(baos)) {
				generator.createStyle();				
				generator.startDocument();				
			}
			var document = baos.toString(Charset.forName("cp1252"));
			var doc = parseDocument(document);
			assertNotNull(doc);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateAlignmentStyle() {
		try (var baos = new ByteArrayOutputStream()){
			try (var generator = new XMLSpreadsheetGenerator(baos)) {
				generator.createStyle().withAlignment(
					Alignment.builder().withHorizontal(Center).build()
				).build();
				generator.startDocument();			
			}
			var document = baos.toString(Charset.forName("cp1252"));
			var doc = parseDocument(document);
			assertNotNull(doc);
			checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Alignment", "Horizontal", Center.toString());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateBordersStyle() {
		try(var baos = new ByteArrayOutputStream()) {
			try (var generator = new XMLSpreadsheetGenerator(baos)) {
				generator.createStyle().withBorders(
					Borders.builder().withBorder(
						Border.builder().withPosition(Bottom).build()
					).build()
				).build();
				generator.startDocument();
			}
			var document = baos.toString(Charset.forName("cp1252"));
			var doc = parseDocument(document);
			assertNotNull(doc);
			checkAttributeValue("ss", doc, "//ss:Style/ss:Borders/ss:Border", "Position", Bottom.toString());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAlignment() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with alignment styles";
				final var VERY_LONG_TEXT = "very long text very long text very long text very long text very long text very long text very long text ";
				final var ROTATE_DEGREES = 45d;

				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var leftAlignment = Alignment.builder().withHorizontal(Left).build();
					var leftStyle = generator.createStyle().withAlignment(leftAlignment).build();
					assertSame(leftAlignment, leftStyle.alignment());

					var rightAlignment = Alignment.builder().withHorizontal(Right).build();
					var rightStyle = generator.createStyle().withAlignment(rightAlignment).build();
					assertSame(rightAlignment, rightStyle.alignment());

					var topAlignment = Alignment.builder().withVertical(Top).build();
					var topStyle = generator.createStyle().withAlignment(topAlignment).build();
					assertSame(topAlignment, topStyle.alignment());

					var bottomAlignment = Alignment.builder().withVertical(Alignment.VerticalAlignment.Bottom).build();
					var bottomStyle = generator.createStyle().withAlignment(bottomAlignment).build();
					assertSame(bottomAlignment, bottomStyle.alignment());

					var wrapAlignment = Alignment.builder().withWrapText(true).build();
					var wrapStyle = generator.createStyle().withAlignment(wrapAlignment).build();
					assertSame(wrapAlignment, wrapStyle.alignment());

					var rotateAlignment = Alignment.builder().withRotate(ROTATE_DEGREES).build();
					var rotateStyle = generator.createStyle().withAlignment(rotateAlignment).build();
					assertSame(rotateAlignment, rotateStyle.alignment());

					var shrinkAlignment = Alignment.builder().withShrinkToFit(true).build();
					var shrinkStyle = generator.createStyle().withAlignment(shrinkAlignment).build();
					assertSame(shrinkAlignment, shrinkStyle.alignment());

					var verticalTextAlignment = Alignment.builder().withVerticalText(true).build();
					var verticalTextStyle = generator.createStyle().withAlignment(verticalTextAlignment).build();
					assertSame(verticalTextAlignment, verticalTextStyle.alignment());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(leftStyle, "left alignment");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(rightStyle, "right alignment");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow(null, null, 55d, null, null);
					generator.writeCell(topStyle, "top alignment");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow(null, null, 55d, null, null);
					generator.writeCell(bottomStyle, "bottom alignment");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow(null, null, 35d, null, null);
					generator.writeCell(VERY_LONG_TEXT);
					generator.closeRow();
					generator.startRow(null, null, 35d, null, null);
					generator.writeCell(wrapStyle, VERY_LONG_TEXT);
					generator.closeRow();

					generator.emptyRow();
					generator.startRow(null, null, 55d, null, null);
					generator.writeCell(rotateStyle, "rotated text");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(shrinkStyle, VERY_LONG_TEXT);
					generator.closeRow();

					generator.emptyRow();
					generator.startRow(null, null, 170d, null, null);
					generator.writeCell(verticalTextStyle, "vertical text");
					generator.closeRow();

					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);

				var row1 = rows.get(1);
				var leftCell = searchCells(row1).get(0);
				assertEquals(Left.toString(),
					getAlignmentAttribute(leftCell, "Horizontal"));

				var row3 = rows.get(3);
				var rightCell = searchCells(row3).get(0);
				assertEquals(Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));

				var row5 = rows.get(5);
				var topCell = searchCells(row5).get(0);
				assertEquals(Top.toString(),
					getAlignmentAttribute(topCell, "Vertical"));

				var row7 = rows.get(7);
				var bottomCell = searchCells(row7).get(0);
				assertEquals(Alignment.VerticalAlignment.Bottom.toString(),
					getAlignmentAttribute(bottomCell, "Vertical"));

				var row10 = rows.get(10);
				var wrapCell = searchCells(row10).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(wrapCell, "WrapText"));

				var row12 = rows.get(12);
				var rotateCell = searchCells(row12).get(0);
				assertEquals(NumberFormatHelper.format(ROTATE_DEGREES),
					getAlignmentAttribute(rotateCell, "Rotate"));

				var row14 = rows.get(14);
				var shrinkCell = searchCells(row14).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(shrinkCell, "ShrinkToFit"));

				var row16 = rows.get(16);
				var verticalTextCell = searchCells(row16).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(verticalTextCell, "VerticalText"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testFont() {
		executeWithTempFile( baos -> {
			try {
				final var GREEN_COLOR = "#00ff00";
				final var RED_COLOR = "#ff0000";
				final var BLUE_COLOR = "#0000ff";
				final var FONT_SIZE = 20.0d;
				final var FONT_NAME = "Verdana";
				final var SHEET_CAPTION = "a sheet with font styles";

				try (var generator = new XMLSpreadsheetGenerator(baos)) {

					var boldFont = Font.builder().withBold(true).build();
					var bold = generator.createStyle().withFont(boldFont).build();
					assertSame(boldFont, bold.font());

					var italicFont = Font.builder().withItalic(true).build();
					var italic = generator.createStyle().withFont(italicFont).build();
					assertSame(italicFont, italic.font());

					var colorFont = Font.builder().withColor(GREEN_COLOR).build();
					var color = generator.createStyle().withFont(colorFont).build();
					assertSame(colorFont, color.font());

					var blueBoldFont = Font.builder().withBold(true).withColor(BLUE_COLOR).build();
					var blueBold = generator.createStyle().withFont(blueBoldFont).build();
					assertSame(blueBoldFont, blueBold.font());

					var bottomFont = Font.builder().withVerticalAlign(Subscript).build();
					var bottom = generator.createStyle().withFont(bottomFont).build();
					assertSame(bottomFont, bottom.font());

					var bigFont = Font.builder().withSize(FONT_SIZE).build();
					var big = generator.createStyle().withFont(bigFont).build();
					assertSame(bigFont, big.font());

					var verdanaFont = Font.builder().withFontName(FONT_NAME).build();
					var verdana = generator.createStyle().withFont(verdanaFont).build();
					assertSame(verdanaFont, verdana.font());

					var redBoldVerdanaFont = Font.builder().
						withFontName(FONT_NAME).
						withBold(true).
						withColor(RED_COLOR).
						build();
					var redBoldVerdana = generator.createStyle().withFont(redBoldVerdanaFont).build();
					assertSame(redBoldVerdanaFont, redBoldVerdana.font());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(bold, new Date());
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(italic, "lalalalala");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(color, "this is green");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(blueBold, "this is blue and bold");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(bottom, "this is a subscript");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow(null, true, 55d, null, null);
					generator.writeCell(big, "this is a 20 size text");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(verdana, "this is verdana");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeCell(redBoldVerdana, "this is red and bold verdana");
					generator.closeRow();
					generator.closeSheet();
				}


				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				// Validate bold style
				var row1 = rows.get(1);
				var boldCell = searchCells(row1).get(0);
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(boldCell, "Bold"));

				// Validate italic style
				var row3 = rows.get(3);
				var italicCell = searchCells(row3).get(0);
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(italicCell, "Italic"));

				// Validate green style
				var row5 = rows.get(5);
				var greenCell = searchCells(row5).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate blue and bold style
				var row7 = rows.get(7);
				var blueBoldCell = searchCells(row7).get(0);
				assertEquals(BLUE_COLOR, getFontStyleAttribute(blueBoldCell, "Color"));
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(blueBoldCell, "Bold"));

				// Validate vertical alignment style
				var row9 = rows.get(9);
				var verticalCell = searchCells(row9).get(0);
				assertEquals(Subscript.toString(), getFontStyleAttribute(verticalCell, "VerticalAlign"));

				// Validate font  size
				var row11 = rows.get(11);
				var fontSizeCell = searchCells(row11).get(0);
				assertEquals(NumberFormatHelper.format(FONT_SIZE), getFontStyleAttribute(fontSizeCell, "Size"));

				// Validate font  name
				var row13 = rows.get(13);
				var fontNameCell = searchCells(row13).get(0);
				assertEquals(FONT_NAME, getFontStyleAttribute(fontNameCell, "FontName"));

				// Validate font name, color, bold
				var row15 = rows.get(15);
				var miscFontCell = searchCells(row15).get(0);
				assertEquals(FONT_NAME, getFontStyleAttribute(miscFontCell, "FontName"));
				assertEquals(RED_COLOR, getFontStyleAttribute(miscFontCell, "Color"));
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(miscFontCell, "Bold"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	
	
	@Test
	public void testProtection() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with protected cells";

				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var protection = Protection.builder().withProtectedCell(true).build();
					var protectedCellStyle = generator.createStyle().withProtection(protection).build();
					assertSame(protection, protectedCellStyle.protection());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION, true);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(protectedCellStyle, 35.09d);
					generator.closeRow();

					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				var cellProtection = searchCells(rows.get(1)).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getProtectionStyleAttribute(cellProtection, "Protected")
				);
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testInterior() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with interior styles";
				final var RED_COLOR = "#ff0000";
				final var GREEN_COLOR = "#00ff00";

				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var redInterior = Interior.builder().withColor(RED_COLOR).build();
					var redInteriorStyle = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, redInteriorStyle.interior());

					var greenInterior = Interior.builder().withColor(GREEN_COLOR).build();
					var greenInteriorStyle = generator.createStyle().withInterior(greenInterior).build();
					assertSame(greenInterior, greenInteriorStyle.interior());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(redInteriorStyle, "red background");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(greenInteriorStyle, "green background");
					generator.closeRow();

					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				// rows
				var rows = searchRows(doc, SHEET_CAPTION);

				var cellRedBackground = searchCells(rows.get(1)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				var cellGreenBackground = searchCells(rows.get(3)).get(0);
				assertEquals(GREEN_COLOR, getInteriorStyleAttribute(cellGreenBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellGreenBackground, "Pattern"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testNamedStyles() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with named styles";
				final var RED_COLOR = "#ff0000";
				final var GREEN_COLOR = "#00ff00";

				final var RED_STYLE = "Red_background";
				final var GREEN_STYLE = "Green_background";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style redInteriorStyle;
				Style greenInteriorStyle;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var redInterior = Interior.builder().withColor(RED_COLOR).build();
					redInteriorStyle = generator.createStyle(RED_STYLE).withInterior(redInterior).build();
					assertSame(redInterior, redInteriorStyle.interior());

					var greenInterior = Interior.builder().withColor(GREEN_COLOR).build();
					greenInteriorStyle = generator.createStyle(GREEN_STYLE).withInterior(greenInterior).build();
					assertSame(greenInterior, greenInteriorStyle.interior());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(redInteriorStyle, "red background");
					generator.closeRow();

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(greenInteriorStyle, "green background");
					generator.closeRow();

					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				// rows
				var rows = searchRows(doc, SHEET_CAPTION);

				var cellRedBackground = searchCells(rows.get(1)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				var cellGreenBackground = searchCells(rows.get(3)).get(0);
				assertEquals(GREEN_COLOR, getInteriorStyleAttribute(cellGreenBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellGreenBackground, "Pattern"));

				var greenStyle = searchStyle(doc, greenInteriorStyle.id());
				var redStyle = searchStyle(doc, redInteriorStyle.id());

				assertEquals(GREEN_STYLE, getAttributeValue(greenStyle, "Name", "ss"));
				assertEquals(RED_STYLE, getAttributeValue(redStyle, "Name", "ss"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testCombinedNamedStyles() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with combined named styles";
				final var BLUE_COLOR = "#0000ff";
				final var RED_COLOR = "#ff0000";
				final var BLUE_STYLE = "Blue background";
				final var RED_STYLE = "Red background";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style blueBackground;
				Style redBackground;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var blueInterior = Interior.builder().withColor(BLUE_COLOR).build();
					blueBackground = generator.createStyle(BLUE_STYLE).withInterior(blueInterior).build();
					assertSame(blueInterior, blueBackground.interior());

					var redInterior = Interior.builder().withColor(RED_COLOR).build();
					redBackground = generator.createStyle(RED_STYLE).withInterior(redInterior).build();
					assertSame(redInterior, redBackground.interior());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);
					generator.emptyRow();
					generator.startRow(null, null, null, null, blueBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					generator.closeRow();
					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				assertEquals(2, rows.size());

				var row = rows.get(1);

				assertEquals(BLUE_COLOR, getInteriorStyleAttribute(row, "Color"));

				var cells = searchCells(row);
				// 2nd, 4th, 6th, 8th, 10th cells have the same style
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(1), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(3), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(5), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(7), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(9), "Color"));

				var blueStyle = searchStyle(doc, blueBackground.id());
				var redStyle = searchStyle(doc, redBackground.id());

				assertEquals(BLUE_STYLE, getAttributeValue(blueStyle, "Name", "ss"));
				assertEquals(RED_STYLE, getAttributeValue(redStyle, "Name", "ss"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testCombinedStyles() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with combined styles";
				final var BLUE_COLOR = "#0000ff";
				final var RED_COLOR = "#ff0000";

				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var blueInterior = Interior.builder().withColor(BLUE_COLOR).build();
					var blueBackground = generator.createStyle().withInterior(blueInterior).build();
					assertSame(blueInterior, blueBackground.interior());

					var redInterior = Interior.builder().withColor(RED_COLOR).build();
					var redBackground = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, redBackground.interior());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);
					generator.emptyRow();
					generator.startRow(null, null, null, null, blueBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					// blue
					generator.writeEmptyCell();
					// red
					generator.writeEmptyCell(redBackground);
					generator.closeRow();
					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				assertEquals(2, rows.size());

				var row = rows.get(1);

				assertEquals(BLUE_COLOR, getInteriorStyleAttribute(row, "Color"));

				var cells = searchCells(row);
				// 2nd, 4th, 6th, 8th, 10th cells have the same style
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(1), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(3), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(5), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(7), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(9), "Color"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testBorders() {
		executeWithTempFile( baos -> {
			try {
				final var FORMAT_WEIGHT = 2.0d;
				final var RED_COLOR = "#ff0000";
				final var SHEET_CAPTION = "a sheet with border styles";

				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var lightBorder = Border.builder().
						withPosition(Bottom).
						withWeight(Thin).
						withLineStyle(LineStyle.Double).
						build();
					var lightBorders = Borders.builder().withBorder(lightBorder).build();
					var lightBorderStyle = generator.createStyle().withBorders(lightBorders).build();
					assertSame(lightBorders, lightBorderStyle.borders());

					var mediumBorder = Border.builder().
						withPosition(Bottom).
						withWeight(Medium).
						withColor(RED_COLOR).
						build();
					var mediumBorders = Borders.builder().withBorder(mediumBorder).build();
					var mediumBorderStyle = generator.createStyle().withBorders(mediumBorders).build();
					assertSame(mediumBorders, mediumBorderStyle.borders());

					var thickBorder = Border.builder().
						withPosition(BorderPosition.Right).
						withLineStyle(Dash).
						withWeight(BorderWeight.Thick).
						build();
					var thickBorders = Borders.builder().withBorder(thickBorder).build();
					var thickBorderStyle = generator.createStyle().withBorders(thickBorders).build();
					assertSame(thickBorders, thickBorderStyle.borders());

					var customBorders = Borders.builder().withBorder(
						Border.builder().
							withPosition(BorderPosition.Left).
							withWeight(FORMAT_WEIGHT).
							build()
					).build();
					var customBorderStyle = generator.createStyle().withBorders(customBorders).build();
					assertSame(customBorders, customBorderStyle.borders());
					// Empty borders
					var emptyBorders = Borders.builder().build();
					var emptyBorderStyle = generator.createStyle().withBorders(emptyBorders).build();
					assertSame(emptyBorders, emptyBorderStyle.borders());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION);
					generator.emptyRow();
					generator.startRow();
					generator.writeEmptyCell();
					generator.writeCell(lightBorderStyle, "light border");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeEmptyCell();
					generator.writeCell(mediumBorderStyle, "medium (and red) border");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeEmptyCell();
					generator.writeCell(thickBorderStyle, "thick border");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeEmptyCell();
					generator.writeCell(customBorderStyle, "custom border");
					generator.closeRow();
					generator.emptyRow();
					generator.startRow();
					generator.writeEmptyCell();
					generator.writeCell(emptyBorderStyle, "no borders defined");
					generator.closeRow();
					generator.closeSheet();
				}

				var document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);
				// remember empty rows left for space
				assertEquals(10, rows.size());
				// Check the bottom border position for every even row
				// Light
				assertEquals(
					NumberFormatHelper.format(Thin.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(1)).get(1), Bottom.toString(), "Weight"));
				assertEquals(
					LineStyle.Double.toString(),
					getBorderStyleAttribute(
						searchCells(rows.get(1)).get(1), Bottom.toString(), "LineStyle"));

				var mediumBorderCell = searchCells(rows.get(3)).get(1);
				assertEquals(
					NumberFormatHelper.format(Medium.getValue()),
					getBorderStyleAttribute(mediumBorderCell, Bottom.toString(), "Weight"));
				assertEquals(RED_COLOR, getBorderStyleAttribute(mediumBorderCell, Bottom.toString(), "Color"));
				assertEquals(
					LineStyle.Continuous.toString(),
					getBorderStyleAttribute(mediumBorderCell, Bottom.toString(), "LineStyle"));

				assertEquals(
					NumberFormatHelper.format(BorderWeight.Thick.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(5)).get(1), BorderPosition.Right.toString(), "Weight"));
				assertEquals(
					Dash.toString(),
					getBorderStyleAttribute(
						searchCells(rows.get(5)).get(1), BorderPosition.Right.toString(), "LineStyle"));

				assertEquals(
					NumberFormatHelper.format(FORMAT_WEIGHT),
					getBorderStyleAttribute(
						searchCells(rows.get(7)).get(1), BorderPosition.Left.toString(), "Weight"));
				assertEquals(
					LineStyle.Continuous.toString(),
					getBorderStyleAttribute(
						searchCells(rows.get(7)).get(1), BorderPosition.Left.toString(), "LineStyle"));

				// Empty style
				var cellEmptyStyle = searchCells(rows.get(9)).get(1);
				var xmlEmptyStyle = searchStyle(cellEmptyStyle.getDocument(),
					getAttributeValue(cellEmptyStyle, "StyleID", "ss"));
				assertEquals(0, xmlEmptyStyle.getChildren().size());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
}