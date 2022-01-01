package tests.generator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.*;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.utils.NumberFormatHelper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;
import static tests.XmlTestUtils.executeWithTempFile;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.generator.GeneratorTestUtils.*;

public class TestStylesInheritance {
	
	@Test 
	public void testInheritColorStyle() {
		executeWithTempFile( os -> {
			try {
				final String GREEN_COLOR = "#00ff00";
				final String SHEET_CAPTION = "a sheet with inherited color styles";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style color;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					color = generator.createStyle();
					Font colorFont = color.font();
					colorFont.setColor(GREEN_COLOR);
					assertSame(colorFont, color.font());


					rightStyle = generator.createStyle(color);
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Right);
					assertSame(rightAlignment, rightStyle.alignment());


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
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				// Further validations
				// does the right alignment style have a parent?
				Element rightStyleElement = searchStyle(doc, rightStyle.getId());
				assertEquals(color.getId(), getAttributeValue(rightStyleElement, "Parent", "ss"));

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				// Validate green style
				Element row0 = rows.get(0);
				Element greenCell = searchCells(row0).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate inheritance
				Element row4 = rows.get(4);
				Element rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
				assertEquals(GREEN_COLOR, getFontStyleAttribute(rightCell, "Color"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritAlignmentStyle() {
		executeWithTempFile( os -> {
			try {
				final String GREEN_COLOR = "#00ff00";
				final String SHEET_CAPTION = "a sheet with inherited alignment styles";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style color;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					rightStyle = generator.createStyle();
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Right);
					assertSame(rightAlignment, rightStyle.alignment());

					color = generator.createStyle(rightStyle);
					Font colorFont = color.font();
					colorFont.setColor(GREEN_COLOR);
					assertSame(colorFont, color.font());


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
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				// Validate green style
				Element row0 = rows.get(0);
				Element greenCell = searchCells(row0).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate horizontal alignment style
				Element row4 = rows.get(4);
				Element rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				Element colorElement = searchStyle(doc, color.getId());
				assertEquals(rightStyle.getId(), getAttributeValue(colorElement, "Parent", "ss"));
				assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(greenCell, "Horizontal"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritBorderStyle() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with inherited border styles";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style thickBorderStyle;
				Style rightStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					thickBorderStyle = generator.createStyle();
					Borders thickBorders = thickBorderStyle.borders();
					Border thickBorder = thickBorders.createBorder(BorderPosition.Right);
					thickBorder.setLineStyle(LineStyle.Dash);
					thickBorder.setWeight(BorderWeight.Thick);

					rightStyle = generator.createStyle(thickBorderStyle);
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Right);
					assertSame(rightAlignment, rightStyle.alignment());


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
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				// Validate thick style
				assertEquals(
					NumberFormatHelper.format(BorderWeight.Thick.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(0)).get(0),
						BorderPosition.Right.toString(), "Weight"));

				// Validate horizontal alignment style
				Element row4 = rows.get(4);
				Element rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				Element rightStyleElement = searchStyle(doc, rightStyle.getId());
				assertEquals(thickBorderStyle.getId(),
					getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(NumberFormatHelper.format(BorderWeight.Thick.getValue()),
					getBorderStyleAttribute(rightCell,
						BorderPosition.Right.toString(), "Weight"));

				os.write(baos.toByteArray());

			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritInteriorStyle() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with inherited interior styles";
				final String RED_COLOR = "#ff0000";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style redInteriorStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					redInteriorStyle = generator.createStyle();
					Interior redInterior = redInteriorStyle.interior();
					redInterior.setColor(RED_COLOR);
					assertSame(redInterior, redInteriorStyle.interior());

					rightStyle = generator.createStyle(redInteriorStyle);
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Right);
					assertSame(rightAlignment, rightStyle.alignment());


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
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				// Validate red style
				Element cellRedBackground = searchCells(rows.get(0)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				// Validate horizontal alignment style
				Element row4 = rows.get(4);
				Element rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				Element rightStyleElement = searchStyle(doc, rightStyle.getId());
				assertEquals(redInteriorStyle.getId(), getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(rightCell, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(rightCell, "Pattern"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritNumberFormatStyle() {
		executeWithTempFile( os -> {
			try {
				final String SHEET_CAPTION = "a sheet with inherited number format styles";
				final String NUMBER_FORMAT = "#####.000";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style numberFormatStyle;
				Style rightStyle;
				try (XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(baos)) {
					numberFormatStyle = generator.createStyle();
					NumberFormat nf = numberFormatStyle.numberFormat();
					nf.setFormat(NUMBER_FORMAT);
					assertSame(nf, numberFormatStyle.numberFormat());

					rightStyle = generator.createStyle(numberFormatStyle);
					Alignment rightAlignment = rightStyle.alignment();
					rightAlignment.setHorizontal(HorizontalAlignment.Left);
					assertSame(rightAlignment, rightStyle.alignment());


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
				}

				String document = new String(baos.toByteArray(), Charset.forName("cp1252"));
				// Not empty and correct document
				Document doc = parseDocument(document);
				assertNotNull(doc);

				List<Element> rows = searchRows(doc, SHEET_CAPTION);

				// Validate number format style
				Element cellNumberFormat = searchCells(rows.get(0)).get(0);
				assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(cellNumberFormat, "Format"));


				// Validate horizontal alignment style
				Element row4 = rows.get(4);
				Element rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Left.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				Element rightStyleElement = searchStyle(doc, rightStyle.getId());
				assertEquals(numberFormatStyle.getId(), getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(rightCell, "Format"));

				os.write(baos.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
}