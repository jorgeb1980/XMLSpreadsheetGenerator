package tests.generator;

import org.jdom2.Document;
import org.jdom2.Element;
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
import java.util.List;

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
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			// Don't mind here to have a warning that the resource is never closed
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
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
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
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
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				generator.createStyle();				
				generator.startDocument();				
			}
			String document = baos.toString(Charset.forName("cp1252"));
			Document doc = parseDocument(document);
			assertNotNull(doc);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateAlignmentStyle() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				generator.createStyle().withAlignment(
					Alignment.builder().withHorizontal(Center).build()
				).build();
				generator.startDocument();			
			}
			String document = baos.toString(Charset.forName("cp1252"));
			Document doc = parseDocument(document);
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
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				generator.createStyle().withBorders(
					Borders.builder().withBorder(
						Border.builder().withPosition(Bottom).build()
					).build()
				).build();
				generator.startDocument();
			}
			String document = baos.toString(Charset.forName("cp1252"));
			Document doc = parseDocument(document);
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
				final String SHEET_CAPTION = "a sheet with alignment styles";
				final String VERY_LONG_TEXT = "very long text very long text very long text very long text very long text very long text very long text ";
				final double ROTATE_DEGREES = 45d;

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Alignment leftAlignment = Alignment.builder().withHorizontal(Left).build();
					Style leftStyle = generator.createStyle().withAlignment(leftAlignment).build();
					assertSame(leftAlignment, leftStyle.alignment());

					Alignment rightAlignment = Alignment.builder().withHorizontal(Right).build();
					Style rightStyle = generator.createStyle().withAlignment(rightAlignment).build();
					assertSame(rightAlignment, rightStyle.alignment());

					Alignment topAlignment = Alignment.builder().withVertical(Top).build();
					Style topStyle = generator.createStyle().withAlignment(topAlignment).build();
					assertSame(topAlignment, topStyle.alignment());

					Alignment bottomAlignment = Alignment.builder().withVertical(Alignment.VerticalAlignment.Bottom).build();
					Style bottomStyle = generator.createStyle().withAlignment(bottomAlignment).build();
					assertSame(bottomAlignment, bottomStyle.alignment());

					Alignment wrapAlignment = Alignment.builder().withWrapText(true).build();
					Style wrapStyle = generator.createStyle().withAlignment(wrapAlignment).build();
					assertSame(wrapAlignment, wrapStyle.alignment());

					Alignment rotateAlignment = Alignment.builder().withRotate(ROTATE_DEGREES).build();
					Style rotateStyle = generator.createStyle().withAlignment(rotateAlignment).build();
					assertSame(rotateAlignment, rotateStyle.alignment());

					Alignment shrinkAlignment = Alignment.builder().withShrinkToFit(true).build();
					Style shrinkStyle = generator.createStyle().withAlignment(shrinkAlignment).build();
					assertSame(shrinkAlignment, shrinkStyle.alignment());

					Alignment verticalTextAlignment = Alignment.builder().withVerticalText(true).build();
					Style verticalTextStyle = generator.createStyle().withAlignment(verticalTextAlignment).build();
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

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				Element row1 = rows.get(1);
				Element leftCell = searchCells(row1).get(0);
				assertEquals(Left.toString(),
					getAlignmentAttribute(leftCell, "Horizontal"));

				Element row3 = rows.get(3);
				Element rightCell = searchCells(row3).get(0);
				assertEquals(Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));

				Element row5 = rows.get(5);
				Element topCell = searchCells(row5).get(0);
				assertEquals(Top.toString(),
					getAlignmentAttribute(topCell, "Vertical"));

				Element row7 = rows.get(7);
				Element bottomCell = searchCells(row7).get(0);
				assertEquals(Alignment.VerticalAlignment.Bottom.toString(),
					getAlignmentAttribute(bottomCell, "Vertical"));

				Element row10 = rows.get(10);
				Element wrapCell = searchCells(row10).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(wrapCell, "WrapText"));

				Element row12 = rows.get(12);
				Element rotateCell = searchCells(row12).get(0);
				assertEquals(NumberFormatHelper.format(ROTATE_DEGREES),
					getAlignmentAttribute(rotateCell, "Rotate"));

				Element row14 = rows.get(14);
				Element shrinkCell = searchCells(row14).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(shrinkCell, "ShrinkToFit"));

				Element row16 = rows.get(16);
				Element verticalTextCell = searchCells(row16).get(0);
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
				final String GREEN_COLOR = "#00ff00";
				final String RED_COLOR = "#ff0000";
				final String BLUE_COLOR = "#0000ff";
				final double FONT_SIZE = 20.0d;
				final String FONT_NAME = "Verdana";
				final String SHEET_CAPTION = "a sheet with font styles";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {

					Font boldFont = Font.builder().withBold(true).build();
					Style bold = generator.createStyle().withFont(boldFont).build();
					assertSame(boldFont, bold.font());

					Font italicFont = Font.builder().withItalic(true).build();
					Style italic = generator.createStyle().withFont(italicFont).build();
					assertSame(italicFont, italic.font());

					Font colorFont = Font.builder().withColor(GREEN_COLOR).build();
					Style color = generator.createStyle().withFont(colorFont).build();
					assertSame(colorFont, color.font());

					Font blueBoldFont = Font.builder().withBold(true).withColor(BLUE_COLOR).build();
					Style blueBold = generator.createStyle().withFont(blueBoldFont).build();
					assertSame(blueBoldFont, blueBold.font());

					Font bottomFont = Font.builder().withVerticalAlign(Subscript).build();
					Style bottom = generator.createStyle().withFont(bottomFont).build();
					assertSame(bottomFont, bottom.font());

					Font bigFont = Font.builder().withSize(FONT_SIZE).build();
					Style big = generator.createStyle().withFont(bigFont).build();
					assertSame(bigFont, big.font());

					Font verdanaFont = Font.builder().withFontName(FONT_NAME).build();
					Style verdana = generator.createStyle().withFont(verdanaFont).build();
					assertSame(verdanaFont, verdana.font());

					Font redBoldVerdanaFont = Font.builder().
						withFontName(FONT_NAME).
						withBold(true).
						withColor(RED_COLOR).
						build();
					Style redBoldVerdana = generator.createStyle().withFont(redBoldVerdanaFont).build();
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


				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				// Validate bold style
				Element row1 = rows.get(1);
				Element boldCell = searchCells(row1).get(0);
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(boldCell, "Bold"));

				// Validate italic style
				Element row3 = rows.get(3);
				Element italicCell = searchCells(row3).get(0);
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(italicCell, "Italic"));

				// Validate green style
				Element row5 = rows.get(5);
				Element greenCell = searchCells(row5).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate blue and bold style
				Element row7 = rows.get(7);
				Element blueBoldCell = searchCells(row7).get(0);
				assertEquals(BLUE_COLOR, getFontStyleAttribute(blueBoldCell, "Color"));
				assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(blueBoldCell, "Bold"));

				// Validate vertical alignment style
				Element row9 = rows.get(9);
				Element verticalCell = searchCells(row9).get(0);
				assertEquals(Subscript.toString(), getFontStyleAttribute(verticalCell, "VerticalAlign"));

				// Validate font  size
				Element row11 = rows.get(11);
				Element fontSizeCell = searchCells(row11).get(0);
				assertEquals(NumberFormatHelper.format(FONT_SIZE), getFontStyleAttribute(fontSizeCell, "Size"));

				// Validate font  name
				Element row13 = rows.get(13);
				Element fontNameCell = searchCells(row13).get(0);
				assertEquals(FONT_NAME, getFontStyleAttribute(fontNameCell, "FontName"));

				// Validate font name, color, bold
				Element row15 = rows.get(15);
				Element miscFontCell = searchCells(row15).get(0);
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
				final String SHEET_CAPTION = "a sheet with protected cells";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Protection protection = Protection.builder().withProtectedCell(true).build();
					Style protectedCellStyle = generator.createStyle().withProtection(protection).build();
					assertSame(protection, protectedCellStyle.protection());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION, true);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(protectedCellStyle, 35.09d);
					generator.closeRow();

					generator.closeSheet();
				}

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				Element cellProtection = searchCells(rows.get(1)).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getProtectionStyleAttribute(cellProtection, "Protected"));
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
				final String SHEET_CAPTION = "a sheet with interior styles";
				final String RED_COLOR = "#ff0000";
				final String GREEN_COLOR = "#00ff00";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Interior redInterior = Interior.builder().withColor(RED_COLOR).build();
					Style redInteriorStyle = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, redInteriorStyle.interior());

					Interior greenInterior = Interior.builder().withColor(GREEN_COLOR).build();
					Style greenInteriorStyle = generator.createStyle().withInterior(greenInterior).build();
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

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				// rows
				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				Element cellRedBackground = searchCells(rows.get(1)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				Element cellGreenBackground = searchCells(rows.get(3)).get(0);
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
				final String SHEET_CAPTION = "a sheet with named styles";
				final String RED_COLOR = "#ff0000";
				final String GREEN_COLOR = "#00ff00";

				final String RED_STYLE = "Red_background";
				final String GREEN_STYLE = "Green_background";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style redInteriorStyle;
				Style greenInteriorStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Interior redInterior = Interior.builder().withColor(RED_COLOR).build();
					redInteriorStyle = generator.createStyle(RED_STYLE).withInterior(redInterior).build();
					assertSame(redInterior, redInteriorStyle.interior());

					Interior greenInterior = Interior.builder().withColor(GREEN_COLOR).build();
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

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				// rows
				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				Element cellRedBackground = searchCells(rows.get(1)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				Element cellGreenBackground = searchCells(rows.get(3)).get(0);
				assertEquals(GREEN_COLOR, getInteriorStyleAttribute(cellGreenBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellGreenBackground, "Pattern"));

				Element greenStyle = searchStyle(doc, greenInteriorStyle.id());
				Element redStyle = searchStyle(doc, redInteriorStyle.id());

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
				final String SHEET_CAPTION = "a sheet with combined named styles";
				final String BLUE_COLOR = "#0000ff";
				final String RED_COLOR = "#ff0000";
				final String BLUE_STYLE = "Blue background";
				final String RED_STYLE = "Red background";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style blueBackground;
				Style redBackground;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Interior blueInterior = Interior.builder().withColor(BLUE_COLOR).build();
					blueBackground = generator.createStyle(BLUE_STYLE).withInterior(blueInterior).build();
					assertSame(blueInterior, blueBackground.interior());

					Interior redInterior = Interior.builder().withColor(RED_COLOR).build();
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

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				assertEquals(2, rows.size());

				Element row = rows.get(1);

				assertEquals(BLUE_COLOR, getInteriorStyleAttribute(row, "Color"));

				List<Element> cells = searchCells(row);
				// 2nd, 4th, 6th, 8th, 10th cells have the same style
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(1), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(3), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(5), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(7), "Color"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(9), "Color"));

				Element blueStyle = searchStyle(doc, blueBackground.id());
				Element redStyle = searchStyle(doc, redBackground.id());

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
				final String SHEET_CAPTION = "a sheet with combined styles";
				final String BLUE_COLOR = "#0000ff";
				final String RED_COLOR = "#ff0000";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Interior blueInterior = Interior.builder().withColor(BLUE_COLOR).build();
					Style blueBackground = generator.createStyle().withInterior(blueInterior).build();
					assertSame(blueInterior, blueBackground.interior());

					Interior redInterior = Interior.builder().withColor(RED_COLOR).build();
					Style redBackground = generator.createStyle().withInterior(redInterior).build();
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

				String document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				assertEquals(2, rows.size());

				Element row = rows.get(1);

				assertEquals(BLUE_COLOR, getInteriorStyleAttribute(row, "Color"));

				List<Element> cells = searchCells(row);
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
				final double FORMAT_WEIGHT = 2.0d;
				final String RED_COLOR = "#ff0000";
				final String SHEET_CAPTION = "a sheet with border styles";

				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Border lightBorder = Border.builder().
						withPosition(Bottom).
						withWeight(Thin).
						withLineStyle(LineStyle.Double).
						build();
					Borders lightBorders = Borders.builder().withBorder(lightBorder).build();
					Style lightBorderStyle = generator.createStyle().withBorders(lightBorders).build();
					assertSame(lightBorders, lightBorderStyle.borders());

					Border mediumBorder = Border.builder().
						withPosition(Bottom).
						withWeight(Medium).
						withColor(RED_COLOR).
						build();
					Borders mediumBorders = Borders.builder().withBorder(mediumBorder).build();
					Style mediumBorderStyle = generator.createStyle().withBorders(mediumBorders).build();
					assertSame(mediumBorders, mediumBorderStyle.borders());

					Border thickBorder = Border.builder().
						withPosition(BorderPosition.Right).
						withLineStyle(Dash).
						withWeight(BorderWeight.Thick).
						build();
					Borders thickBorders = Borders.builder().withBorder(thickBorder).build();
					Style thickBorderStyle = generator.createStyle().withBorders(thickBorders).build();
					assertSame(thickBorders, thickBorderStyle.borders());

					Borders customBorders = Borders.builder().withBorder(
						Border.builder().
							withPosition(BorderPosition.Left).
							withWeight(FORMAT_WEIGHT).
							build()
					).build();
					Style customBorderStyle = generator.createStyle().withBorders(customBorders).build();
					assertSame(customBorders, customBorderStyle.borders());
					// Empty borders
					Borders emptyBorders = Borders.builder().build();
					Style emptyBorderStyle = generator.createStyle().withBorders(emptyBorders).build();
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

				String document = baos.toString(Charset.forName("cp1252"));

				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
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

				Element mediumBorderCell = searchCells(rows.get(3)).get(1);
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
				Element cellEmptyStyle = searchCells(rows.get(9)).get(1);
				Element xmlEmptyStyle = searchStyle(cellEmptyStyle.getDocument(),
					getAttributeValue(cellEmptyStyle, "StyleID", "ss"));
				assertEquals(0, xmlEmptyStyle.getChildren().size());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
}