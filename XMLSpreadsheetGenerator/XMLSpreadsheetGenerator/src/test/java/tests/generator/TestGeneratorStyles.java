package tests.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static tests.generator.GeneratorTestUtils.*;

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

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import tests.XmlTestUtils;
import tests.styles.StyleTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Borders;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.Protection;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Font.VerticalAlignment;
import xml.spreadsheet.utils.BooleanFormatHelper;
import xml.spreadsheet.utils.NumberFormatHelper;

public class TestGeneratorStyles {
	
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
	public void testAlignment() {
		try {
			final String SHEET_CAPTION = "a sheet with alignment styles";
			final String VERY_LONG_TEXT = "very long text very long text very long text very long text very long text very long text very long text ";
			final Double ROTATE_DEGREES = 45d;
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style leftStyle = generator.createStyle();
			Alignment leftAlignment = leftStyle.alignment();
			leftAlignment.setHorizontal(HorizontalAlignment.Left);
			assertTrue(leftAlignment == leftStyle.alignment());
			
			Style rightStyle = generator.createStyle();
			Alignment rightAlignment = rightStyle.alignment();
			rightAlignment.setHorizontal(HorizontalAlignment.Right);
			assertTrue(rightAlignment == rightStyle.alignment());			
			
			Style topStyle = generator.createStyle();
			Alignment topAlignment = topStyle.alignment();
			topAlignment.setVertical(xml.spreadsheet.style.Alignment.VerticalAlignment.Top);
			assertTrue(topAlignment == topStyle.alignment());		
			
			Style bottomStyle = generator.createStyle();
			Alignment bottomAlignment = bottomStyle.alignment();
			bottomAlignment.setVertical(xml.spreadsheet.style.Alignment.VerticalAlignment.Bottom);
			assertTrue(bottomAlignment == bottomStyle.alignment());
			
			Style wrapStyle = generator.createStyle();
			Alignment wrapAlignment = wrapStyle.alignment();
			wrapAlignment.setWrapText(true);
			assertTrue(wrapAlignment == wrapStyle.alignment());
			
			Style rotateStyle = generator.createStyle();
			Alignment rotateAlignment = rotateStyle.alignment();
			rotateAlignment.setRotate(ROTATE_DEGREES);
			assertTrue(rotateAlignment == rotateStyle.alignment());
			
			Style shrinkStyle = generator.createStyle();
			Alignment shrinkAlignment = shrinkStyle.alignment();
			shrinkAlignment.setShrinkToFit(true);
			assertTrue(shrinkAlignment == shrinkStyle.alignment());
			
			Style verticalTextStyle = generator.createStyle();
			Alignment verticalTextAlignment = verticalTextStyle.alignment();
			verticalTextAlignment.setVerticalText(true);
			assertTrue(verticalTextAlignment == verticalTextStyle.alignment());
			 
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
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			Element row1 = rows.get(1);
			Element leftCell = GeneratorTestUtils.searchCells(row1).get(0);
			assertEquals(HorizontalAlignment.Left.toString(),
					getAlignmentAttribute(leftCell, "Horizontal"));
			
			Element row3 = rows.get(3);
			Element rightCell = GeneratorTestUtils.searchCells(row3).get(0);
			assertEquals(HorizontalAlignment.Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));
			
			Element row5 = rows.get(5);
			Element topCell = GeneratorTestUtils.searchCells(row5).get(0);
			assertEquals(xml.spreadsheet.style.Alignment.VerticalAlignment.Top.toString(),
					getAlignmentAttribute(topCell, "Vertical"));
			
			Element row7 = rows.get(7);
			Element bottomCell = GeneratorTestUtils.searchCells(row7).get(0);
			assertEquals(xml.spreadsheet.style.Alignment.VerticalAlignment.Bottom.toString(),
					getAlignmentAttribute(bottomCell, "Vertical"));
			
			Element row10 = rows.get(10);
			Element wrapCell = GeneratorTestUtils.searchCells(row10).get(0);
			assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(wrapCell, "WrapText"));
			
			Element row12 = rows.get(12);
			Element rotateCell = GeneratorTestUtils.searchCells(row12).get(0);
			assertEquals(NumberFormatHelper.format(ROTATE_DEGREES),
					getAlignmentAttribute(rotateCell, "Rotate"));
			
			Element row14 = rows.get(14);
			Element shrinkCell = GeneratorTestUtils.searchCells(row14).get(0);
			assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(shrinkCell, "ShrinkToFit"));
			
			Element row16 = rows.get(16);
			Element verticalTextCell = GeneratorTestUtils.searchCells(row16).get(0);
			assertEquals(BooleanFormatHelper.format(true),
					getAlignmentAttribute(verticalTextCell, "VerticalText"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with alignment styles -> " + file.getAbsolutePath());	
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
			final String RED_COLOR = "#ff0000";
			final String BLUE_COLOR = "#0000ff";
			final Double FONT_SIZE = 20.0d;
			final String FONT_NAME = "Verdana";
			final String SHEET_CAPTION = "a sheet with font styles";
			
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
			bigFont.setSize(FONT_SIZE);
			assertTrue(bigFont == big.font());
			
			Style verdana = generator.createStyle();
			Font verdanaFont = verdana.font();
			verdanaFont.setFontName(FONT_NAME);
			assertTrue(verdanaFont == verdana.font());
			
			Style redBoldVerdana = generator.createStyle();
			Font redBoldVerdanaFont = redBoldVerdana.font();
			redBoldVerdanaFont.setFontName(FONT_NAME);
			redBoldVerdanaFont.setColor(RED_COLOR);
			redBoldVerdanaFont.setBold(true);
			assertTrue(redBoldVerdanaFont == redBoldVerdana.font());
			
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
			generator.closeDocument();
			
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
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
			
			// Validate font  size
			Element row11 = rows.get(11);
			Element fontSizeCell = GeneratorTestUtils.searchCells(row11).get(0);
			assertEquals(NumberFormatHelper.format(FONT_SIZE), getFontStyleAttribute(fontSizeCell, "Size"));
			
			// Validate font  name
			Element row13 = rows.get(13);
			Element fontNameCell = GeneratorTestUtils.searchCells(row13).get(0);
			assertEquals(FONT_NAME, getFontStyleAttribute(fontNameCell, "FontName"));
			
			// Validate font name, color, bold
			Element row15 = rows.get(15);
			Element miscFontCell = GeneratorTestUtils.searchCells(row15).get(0);
			assertEquals(FONT_NAME, getFontStyleAttribute(miscFontCell, "FontName"));
			assertEquals(RED_COLOR, getFontStyleAttribute(miscFontCell, "Color"));
			assertEquals(BooleanFormatHelper.format(true), getFontStyleAttribute(miscFontCell, "Bold"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with font styles -> " + file.getAbsolutePath());	
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}
	
	
	
	@Test
	public void testProtection() {
		try {
			final String SHEET_CAPTION = "a sheet with protected cells";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);

			Style protectedCellStyle = generator.createStyle();
			Protection protection = protectedCellStyle.protection();
			protection.setProtectedCell(true);
			assertTrue(protection == protectedCellStyle.protection());
			
			generator.startDocument();
			generator.startSheet(SHEET_CAPTION, true);
			
			generator.emptyRow();
			generator.startRow();
			generator.writeCell(protectedCellStyle, 35.09d);
			generator.closeRow();
			
			generator.closeSheet();
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));		
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);	
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			Element cellProtection = GeneratorTestUtils.searchCells(rows.get(1)).get(0);
			assertEquals(BooleanFormatHelper.format(true),
				getProtectionStyleAttribute(cellProtection, "Protected"));
			
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with protected cells -> " + file.getAbsolutePath());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testInterior() {
		try {
			final String SHEET_CAPTION = "a sheet with interior styles";
			final String RED_COLOR = "#ff0000";
			final String GREEN_COLOR = "#00ff00";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style redInteriorStyle = generator.createStyle();
			Interior redInterior = redInteriorStyle.interior();
			redInterior.setColor(RED_COLOR);
			assertTrue(redInterior == redInteriorStyle.interior());
			
			Style greenInteriorStyle = generator.createStyle();
			Interior greenInterior = greenInteriorStyle.interior();
			greenInterior.setColor(GREEN_COLOR);
			assertTrue(greenInterior == greenInteriorStyle.interior());
			
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
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);	
			
			// rows
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			
			Element cellRedBackground = GeneratorTestUtils.searchCells(rows.get(1)).get(0);
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
			// Make sure the generator has written the solid pattern too
			assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));
			
			Element cellGreenBackground = GeneratorTestUtils.searchCells(rows.get(3)).get(0);
			assertEquals(GREEN_COLOR, getInteriorStyleAttribute(cellGreenBackground, "Color"));
			// Make sure the generator has written the solid pattern too
			assertEquals("Solid", getInteriorStyleAttribute(cellGreenBackground, "Pattern"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with interior styles -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testCombinedStyles() {
		try {
			final String SHEET_CAPTION = "a sheet with combined styles";
			final String BLUE_COLOR = "#0000ff";
			final String RED_COLOR = "#ff0000";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style blueBackground = generator.createStyle();
			Interior blueInterior = blueBackground.interior();
			blueInterior.setColor(BLUE_COLOR);
			assertTrue(blueInterior == blueBackground.interior());
			
			Style redBackground = generator.createStyle();
			Interior redInterior = redBackground.interior();
			redInterior.setColor(RED_COLOR);
			assertTrue(redInterior == redBackground.interior());
			
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
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);		
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			assertEquals(2, rows.size());
			
			Element row = rows.get(1);
						
			assertEquals(BLUE_COLOR, getInteriorStyleAttribute(row, "Color"));
			
			List<Element> cells = GeneratorTestUtils.searchCells(row);
			// 2nd, 4th, 6th, 8th, 10th cells have the same style
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(1), "Color"));
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(3), "Color"));
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(5), "Color"));
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(7), "Color"));
			assertEquals(RED_COLOR, getInteriorStyleAttribute(cells.get(9), "Color"));
			
			os.write(baos.toByteArray());			
			os.close();
			System.out.println("Created file with combined styles -> " + file.getAbsolutePath());
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testParentStyle() {
		try {
			final String GREEN_COLOR = "#00ff00";
			final String SHEET_CAPTION = "a sheet with parent styles";
			
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
			generator.writeCell(color, "aaaa");
			generator.closeRow();
			
			generator.emptyRow();
			generator.emptyRow();
			generator.emptyRow();
			
			generator.startRow();
			generator.writeCell(rightStyle, "bbbb");
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
			System.out.println("Created file with parent styles -> " + file.getAbsolutePath());
			
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
			final String RED_COLOR = "#ff0000";
			final String SHEET_CAPTION = "a sheet with border styles";
			
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
			Style lightBorderStyle = generator.createStyle();
			Borders lightBorders = lightBorderStyle.borders();
			Border lightBorder = lightBorders.createBorder(BorderPosition.Bottom);
			lightBorder.setWeight(BorderWeight.Thin);
			lightBorder.setLineStyle(LineStyle.Double);
			assertTrue(lightBorders == lightBorderStyle.borders());
			Style mediumBorderStyle = generator.createStyle();
			Borders mediumBorders = mediumBorderStyle.borders();
			Border mediumBorder = mediumBorders.createBorder(BorderPosition.Bottom);
			mediumBorder.setWeight(BorderWeight.Medium);
			mediumBorder.setColor(RED_COLOR);
			assertTrue(mediumBorders == mediumBorderStyle.borders());
			Style thickBorderStyle = generator.createStyle();
			Borders thickBorders = thickBorderStyle.borders();
			Border thickBorder = thickBorders.createBorder(BorderPosition.Right);
			thickBorder.setLineStyle(LineStyle.Dash);
			thickBorder.setWeight(BorderWeight.Thick);
			assertTrue(thickBorders == thickBorderStyle.borders());
			Style customBorderStyle = generator.createStyle();
			Borders customBorders = customBorderStyle.borders();
			customBorders.createBorder(BorderPosition.Left).setWeight(FORMAT_WEIGHT);
			assertTrue(customBorders == customBorderStyle.borders());
			// Empty borders
			Style emptyBorderStyle = generator.createStyle();
			Borders emptyBorders = emptyBorderStyle.borders();
			assertTrue(emptyBorders == emptyBorderStyle.borders());
			
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
			generator.closeDocument();
			
			String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
			
			// Not empty and correct document
			Document doc = GeneratorTestUtils.parseDocument(document);
			assertNotNull(doc);			
			
			List<Element> rows = GeneratorTestUtils.searchRows(doc, SHEET_CAPTION);
			// remember empty rows left for space
			assertEquals(10, rows.size());
			// Check the bottom border position for every even row
			// Light
			assertEquals(
				NumberFormatHelper.format(BorderWeight.Thin.getValue()), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(1)).get(1), BorderPosition.Bottom.toString(), "Weight"));
			assertEquals(
				LineStyle.Double.toString(), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(1)).get(1), BorderPosition.Bottom.toString(), "LineStyle"));
			
			Element mediumBorderCell = GeneratorTestUtils.searchCells(rows.get(3)).get(1);
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
					GeneratorTestUtils.searchCells(rows.get(5)).get(1), BorderPosition.Right.toString(), "Weight"));
			assertEquals(
				LineStyle.Dash.toString(), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(5)).get(1), BorderPosition.Right.toString(), "LineStyle"));
			
			assertEquals(
				NumberFormatHelper.format(FORMAT_WEIGHT), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(7)).get(1), BorderPosition.Left.toString(), "Weight"));
			assertEquals(
				LineStyle.Continuous.toString(), 
				getBorderStyleAttribute(
					GeneratorTestUtils.searchCells(rows.get(7)).get(1), BorderPosition.Left.toString(), "LineStyle"));
			
			// Empty style
			Element cellEmptyStyle = GeneratorTestUtils.searchCells(rows.get(9)).get(1);
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
	
	
}