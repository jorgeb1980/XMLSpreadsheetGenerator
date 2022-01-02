package tests.generator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.*;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Font.VerticalAlignment;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static tests.XmlTestUtils.executeWithTempFile;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.generator.GeneratorTestUtils.*;
import static tests.styles.StyleTestUtils.checkAttributeValue;

public class TestGeneratorStyles {
	
	@Test
	public void testCreateStyleAfterStarting() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {		
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				generator.createStyle();				
				generator.startDocument();				
			}
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = parseDocument(document);
			assertNotNull(doc);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateAlignmentStyle() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {		
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				Style style = generator.createStyle();				
				style.alignment().setHorizontal(HorizontalAlignment.Center);				
				generator.startDocument();			
			}
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = parseDocument(document);
			assertNotNull(doc);
			checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Alignment", "Horizontal", HorizontalAlignment.Center.toString());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateBordersStyle() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {		
			try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {			
				Style style = generator.createStyle();				
				style.borders().createBorder(BorderPosition.Bottom);				
				generator.startDocument();
			}
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			Document doc = parseDocument(document);
			assertNotNull(doc);
			checkAttributeValue(
				"ss", doc, "//ss:Style/ss:Borders/ss:Border", "Position", BorderPosition.Bottom.toString());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAlignment() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with alignment styles";
				final String VERY_LONG_TEXT = "very long text very long text very long text very long text very long text very long text very long text ";
				final double ROTATE_DEGREES = 45d;

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Style leftStyle = generator.createStyle();
					Alignment leftAlignment = leftStyle.alignment();
					leftAlignment.setHorizontal(HorizontalAlignment.Left);
					assertSame(leftAlignment, leftStyle.alignment());

					Style rightStyle = generator.createStyle();
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Right);
					assertSame(rightAlignment, rightStyle.alignment());

					Style topStyle = generator.createStyle();
					Alignment topAlignment = topStyle.alignment();
					topAlignment.setVertical(xml.spreadsheet.style.Alignment.VerticalAlignment.Top);
					assertSame(topAlignment, topStyle.alignment());

					Style bottomStyle = generator.createStyle();
					Alignment bottomAlignment = bottomStyle.alignment();
					bottomAlignment.setVertical(xml.spreadsheet.style.Alignment.VerticalAlignment.Bottom);
					assertSame(bottomAlignment, bottomStyle.alignment());

					Style wrapStyle = generator.createStyle();
					Alignment wrapAlignment = wrapStyle.alignment();
					wrapAlignment.setWrapText(true);
					assertSame(wrapAlignment, wrapStyle.alignment());

					Style rotateStyle = generator.createStyle();
					Alignment rotateAlignment = rotateStyle.alignment();
					rotateAlignment.setRotate(ROTATE_DEGREES);
					assertSame(rotateAlignment, rotateStyle.alignment());

					Style shrinkStyle = generator.createStyle();
					Alignment shrinkAlignment = shrinkStyle.alignment();
					shrinkAlignment.setShrinkToFit(true);
					assertSame(shrinkAlignment, shrinkStyle.alignment());

					Style verticalTextStyle = generator.createStyle();
					Alignment verticalTextAlignment = verticalTextStyle.alignment();
					verticalTextAlignment.setVerticalText(true);
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				Element row1 = rows.get(1);
				Element leftCell = searchCells(row1).get(0);
				assertEquals(HorizontalAlignment.Left.toString(),
					getAlignmentAttribute(leftCell, "Horizontal"));

				Element row3 = rows.get(3);
				Element rightCell = searchCells(row3).get(0);
				assertEquals(HorizontalAlignment.Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));

				Element row5 = rows.get(5);
				Element topCell = searchCells(row5).get(0);
				assertEquals(xml.spreadsheet.style.Alignment.VerticalAlignment.Top.toString(),
					getAlignmentAttribute(topCell, "Vertical"));

				Element row7 = rows.get(7);
				Element bottomCell = searchCells(row7).get(0);
				assertEquals(xml.spreadsheet.style.Alignment.VerticalAlignment.Bottom.toString(),
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

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testFont() {
		executeWithTempFile( os -> {
			try {
				final String GREEN_COLOR = "#00ff00";
				final String RED_COLOR = "#ff0000";
				final String BLUE_COLOR = "#0000ff";
				final double FONT_SIZE = 20.0d;
				final String FONT_NAME = "Verdana";
				final String SHEET_CAPTION = "a sheet with font styles";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {

					Style bold = generator.createStyle();
					Font boldFont = bold.font();
					boldFont.setBold(true);
					assertSame(boldFont, bold.font());

					Style italic = generator.createStyle();
					Font italicFont = italic.font();
					italicFont.setItalic(true);
					assertSame(italicFont, italic.font());

					Style color = generator.createStyle();
					Font colorFont = color.font();
					colorFont.setColor(GREEN_COLOR);
					assertSame(colorFont, color.font());

					Style blueBold = generator.createStyle();
					Font blueBoldFont = blueBold.font();
					blueBoldFont.setColor(BLUE_COLOR);
					blueBoldFont.setBold(true);
					assertSame(blueBoldFont, blueBold.font());

					Style bottom = generator.createStyle();
					Font bottomFont = bottom.font();
					bottomFont.setVerticalAlign(VerticalAlignment.Subscript);
					assertSame(bottomFont, bottom.font());

					Style big = generator.createStyle();
					Font bigFont = big.font();
					bigFont.setSize(FONT_SIZE);
					assertSame(bigFont, big.font());

					Style verdana = generator.createStyle();
					Font verdanaFont = verdana.font();
					verdanaFont.setFontName(FONT_NAME);
					assertSame(verdanaFont, verdana.font());

					Style redBoldVerdana = generator.createStyle();
					Font redBoldVerdanaFont = redBoldVerdana.font();
					redBoldVerdanaFont.setFontName(FONT_NAME);
					redBoldVerdanaFont.setColor(RED_COLOR);
					redBoldVerdanaFont.setBold(true);
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


				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
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
				assertEquals(VerticalAlignment.Subscript.toString(), getFontStyleAttribute(verticalCell, "VerticalAlign"));

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

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	
	
	@Test
	public void testProtection() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with protected cells";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Style protectedCellStyle = generator.createStyle();
					Protection protection = protectedCellStyle.protection();
					protection.setProtectedCell(true);
					assertSame(protection, protectedCellStyle.protection());

					generator.startDocument();
					generator.startSheet(SHEET_CAPTION, true);

					generator.emptyRow();
					generator.startRow();
					generator.writeCell(protectedCellStyle, 35.09d);
					generator.closeRow();

					generator.closeSheet();
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				Element cellProtection = searchCells(rows.get(1)).get(0);
				assertEquals(BooleanFormatHelper.format(true),
					getProtectionStyleAttribute(cellProtection, "Protected"));


				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testInterior() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with interior styles";
				final String RED_COLOR = "#ff0000";
				final String GREEN_COLOR = "#00ff00";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Style redInteriorStyle = generator.createStyle();
					Interior redInterior = redInteriorStyle.interior();
					redInterior.setColor(RED_COLOR);
					assertSame(redInterior, redInteriorStyle.interior());

					Style greenInteriorStyle = generator.createStyle();
					Interior greenInterior = greenInteriorStyle.interior();
					greenInterior.setColor(GREEN_COLOR);
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
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

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testNamedStyles() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with named styles";
				final String RED_COLOR = "#ff0000";
				final String GREEN_COLOR = "#00ff00";

				final String RED_STYLE = "Red_background";
				final String GREEN_STYLE = "Green_background";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style redInteriorStyle;
				Style greenInteriorStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					redInteriorStyle = generator.createStyle(RED_STYLE);
					Interior redInterior = redInteriorStyle.interior();
					redInterior.setColor(RED_COLOR);
					assertSame(redInterior, redInteriorStyle.interior());

					greenInteriorStyle = generator.createStyle(GREEN_STYLE);
					Interior greenInterior = greenInteriorStyle.interior();
					greenInterior.setColor(GREEN_COLOR);
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
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

				Element greenStyle = searchStyle(doc, greenInteriorStyle.getId());
				Element redStyle = searchStyle(doc, redInteriorStyle.getId());

				assertEquals(GREEN_STYLE, getAttributeValue(greenStyle, "Name", "ss"));
				assertEquals(RED_STYLE, getAttributeValue(redStyle, "Name", "ss"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testCombinedNamedStyles() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with combined named styles";
				final String BLUE_COLOR = "#0000ff";
				final String RED_COLOR = "#ff0000";
				final String BLUE_STYLE = "Blue background";
				final String RED_STYLE = "Red background";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style blueBackground;
				Style redBackground;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					blueBackground = generator.createStyle(BLUE_STYLE);
					Interior blueInterior = blueBackground.interior();
					blueInterior.setColor(BLUE_COLOR);
					assertSame(blueInterior, blueBackground.interior());

					redBackground = generator.createStyle(RED_STYLE);
					Interior redInterior = redBackground.interior();
					redInterior.setColor(RED_COLOR);
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
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

				Element blueStyle = searchStyle(doc, blueBackground.getId());
				Element redStyle = searchStyle(doc, redBackground.getId());

				assertEquals(BLUE_STYLE, getAttributeValue(blueStyle, "Name", "ss"));
				assertEquals(RED_STYLE, getAttributeValue(redStyle, "Name", "ss"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testCombinedStyles() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with combined styles";
				final String BLUE_COLOR = "#0000ff";
				final String RED_COLOR = "#ff0000";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Style blueBackground = generator.createStyle();
					Interior blueInterior = blueBackground.interior();
					blueInterior.setColor(BLUE_COLOR);
					assertSame(blueInterior, blueBackground.interior());

					Style redBackground = generator.createStyle();
					Interior redInterior = redBackground.interior();
					redInterior.setColor(RED_COLOR);
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
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

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test
	public void testBorders() {
		executeWithTempFile( os -> {
			try {
				final double FORMAT_WEIGHT = 2.0d;
				final String RED_COLOR = "#ff0000";
				final String SHEET_CAPTION = "a sheet with border styles";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					Style lightBorderStyle = generator.createStyle();
					Borders lightBorders = lightBorderStyle.borders();
					Border lightBorder = lightBorders.createBorder(BorderPosition.Bottom);
					lightBorder.setWeight(BorderWeight.Thin);
					lightBorder.setLineStyle(LineStyle.Double);
					assertSame(lightBorders, lightBorderStyle.borders());
					Style mediumBorderStyle = generator.createStyle();
					Borders mediumBorders = mediumBorderStyle.borders();
					Border mediumBorder = mediumBorders.createBorder(BorderPosition.Bottom);
					mediumBorder.setWeight(BorderWeight.Medium);
					mediumBorder.setColor(RED_COLOR);
					assertSame(mediumBorders, mediumBorderStyle.borders());
					Style thickBorderStyle = generator.createStyle();
					Borders thickBorders = thickBorderStyle.borders();
					Border thickBorder = thickBorders.createBorder(BorderPosition.Right);
					thickBorder.setLineStyle(LineStyle.Dash);
					thickBorder.setWeight(BorderWeight.Thick);
					assertSame(thickBorders, thickBorderStyle.borders());
					Style customBorderStyle = generator.createStyle();
					Borders customBorders = customBorderStyle.borders();
					customBorders.createBorder(BorderPosition.Left).setWeight(FORMAT_WEIGHT);
					assertSame(customBorders, customBorderStyle.borders());
					// Empty borders
					Style emptyBorderStyle = generator.createStyle();
					Borders emptyBorders = emptyBorderStyle.borders();
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

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));

				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);
				// remember empty rows left for space
				assertEquals(10, rows.size());
				// Check the bottom border position for every even row
				// Light
				assertEquals(
					NumberFormatHelper.format(BorderWeight.Thin.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(1)).get(1), BorderPosition.Bottom.toString(), "Weight"));
				assertEquals(
					LineStyle.Double.toString(),
					getBorderStyleAttribute(
						searchCells(rows.get(1)).get(1), BorderPosition.Bottom.toString(), "LineStyle"));

				Element mediumBorderCell = searchCells(rows.get(3)).get(1);
				assertEquals(
					NumberFormatHelper.format(BorderWeight.Medium.getValue()),
					getBorderStyleAttribute(mediumBorderCell, BorderPosition.Bottom.toString(), "Weight"));
				assertEquals(RED_COLOR, getBorderStyleAttribute(mediumBorderCell, BorderPosition.Bottom.toString(), "Color"));
				assertEquals(
					LineStyle.Continuous.toString(),
					getBorderStyleAttribute(mediumBorderCell, BorderPosition.Bottom.toString(), "LineStyle"));

				assertEquals(
					NumberFormatHelper.format(BorderWeight.Thick.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(5)).get(1), BorderPosition.Right.toString(), "Weight"));
				assertEquals(
					LineStyle.Dash.toString(),
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

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
}