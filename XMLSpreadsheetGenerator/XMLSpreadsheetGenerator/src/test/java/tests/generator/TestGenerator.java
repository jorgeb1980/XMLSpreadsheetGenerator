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

import tests.XmlTestUtils;
import tests.styles.StyleTestUtils;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Borders;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Font.VerticalAlignment;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.style.Protection;
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
			generator.emptyRow();
			generator.emptyRow();
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
	
	private String getChildAttribute(String child, Element element, String attribute) throws JDOMException {
		String ret = null;
		Element style = GeneratorTestUtils.searchStyle(element.getDocument(), element);
		List<Element> children =
			XmlTestUtils.getDescendants(style, child);
		if (children != null && children.size() == 1) {
			ret = XmlTestUtils.getAttributeValue(children.get(0), attribute, "ss");
		}
		else {
			fail();
		}
		return ret;
	}
	
	private String getFontStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Font", element, attribute);
	}
	
	private String getInteriorStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Interior", element, attribute);
	}
	
	private String getProtectionStyleAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Protection", element, attribute);
	}
	
	private String getAlignmentAttribute(Element element, String attribute) throws JDOMException {
		return getChildAttribute("ss:Alignment", element, attribute);
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
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos);
			
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
			
			generator.closeDocument();
			
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
	
	@Test 
	public void testInvalidTransitionSheetToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.writeCell("adasf");
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionDocumentToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.writeCell("adasf");
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionSheetToDocument() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.closeDocument();
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionRowToDocument() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.startSheet("this will fail");
			generator.startRow();
			generator.closeDocument();
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionCleanToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.startDocument();
			generator.writeCell(new Date());
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
	}
	
	@Test 
	public void testInvalidTransitionNotInitializedToCell() {
		try {
			File file = File.createTempFile("xmlspreadsheet", ".xml");
			OutputStream os = new FileOutputStream(file);
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(os);
			generator.writeCell(new Date());
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertNotNull(e);
		}
		catch (Throwable t) {
			fail();
		}
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
