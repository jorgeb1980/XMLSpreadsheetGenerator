package tests.generator;

import org.junit.jupiter.api.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.*;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.utils.NumberFormatHelper;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;
import static tests.XmlTestUtils.executeWithTempFile;
import static tests.XmlTestUtils.getAttributeValue;
import static tests.generator.GeneratorTestUtils.*;
import static xml.spreadsheet.style.Alignment.HorizontalAlignment.Left;
import static xml.spreadsheet.style.Alignment.HorizontalAlignment.Right;
import static xml.spreadsheet.style.Border.BorderWeight.Thick;
import static xml.spreadsheet.style.Border.LineStyle.Dash;

public class TestStylesInheritance {
	
	@Test
	public void testInheritColorStyle() {
		executeWithTempFile( baos -> {
			try {
				final var GREEN_COLOR = "#00ff00";
				final var SHEET_CAPTION = "a sheet with inherited color styles";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style color;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var colorFont = Font.builder().withColor(GREEN_COLOR).build();
					color = generator.createStyle().withFont(colorFont).build();
					assertSame(colorFont, color.font());

					var rightAlignment = Alignment.builder().withHorizontal(Right).build();
					rightStyle = generator.createStyle(color).withAlignment(rightAlignment).build();
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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				// Further validations
				// does the right alignment style have a parent?
				var rightStyleElement = searchStyle(doc, rightStyle.id());
				assertEquals(color.id(), getAttributeValue(rightStyleElement, "Parent", "ss"));

				var rows = searchRows(doc, SHEET_CAPTION);

				// Validate green style
				var row0 = rows.get(0);
				var greenCell = searchCells(row0).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate inheritance
				var row4 = rows.get(4);
				var rightCell = searchCells(row4).get(0);
				assertEquals(Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));
				assertEquals(GREEN_COLOR, getFontStyleAttribute(rightCell, "Color"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritAlignmentStyle() {
		executeWithTempFile( baos -> {
			try {
				final var GREEN_COLOR = "#00ff00";
				final var SHEET_CAPTION = "a sheet with inherited alignment styles";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style color;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var rightAlignment = Alignment.builder().withHorizontal(Right).build();
					rightStyle = generator.createStyle().withAlignment(rightAlignment).build();
					assertSame(rightAlignment, rightStyle.alignment());

					var colorFont = Font.builder().withColor(GREEN_COLOR).build();;
					color = generator.createStyle(rightStyle).withFont(colorFont).build();
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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);

				// Validate green style
				var row0 = rows.get(0);
				var greenCell = searchCells(row0).get(0);
				assertEquals(GREEN_COLOR, getFontStyleAttribute(greenCell, "Color"));

				// Validate horizontal alignment style
				var row4 = rows.get(4);
				var rightCell = searchCells(row4).get(0);
				assertEquals(Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				var colorElement = searchStyle(doc, color.id());
				assertEquals(rightStyle.id(), getAttributeValue(colorElement, "Parent", "ss"));
				assertEquals(Right.toString(), getAlignmentAttribute(greenCell, "Horizontal"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritBorderStyle() {
		executeWithTempFile( baos -> {
			try {
				final String SHEET_CAPTION = "a sheet with inherited border styles";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style thickBorderStyle;
				Style rightStyle;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var thickBorder = Border.builder().
						withPosition(BorderPosition.Right).
						withLineStyle(Dash).
						withWeight(Thick).
						build();
					var thickBorders = Borders.builder().withBorder(thickBorder).build();
					thickBorderStyle = generator.createStyle().withBorders(thickBorders).build();

					var rightAlignment = Alignment.builder().withHorizontal(Right).build();
					rightStyle = generator.createStyle(thickBorderStyle).withAlignment(rightAlignment).build();
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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);

				// Validate thick style
				assertEquals(
					NumberFormatHelper.format(Thick.getValue()),
					getBorderStyleAttribute(
						searchCells(rows.get(0)).get(0),
						BorderPosition.Right.toString(), "Weight"));

				// Validate horizontal alignment style
				var row4 = rows.get(4);
				var rightCell = searchCells(row4).get(0);
				assertEquals(Right.toString(),
					getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				var rightStyleElement = searchStyle(doc, rightStyle.id());
				assertEquals(thickBorderStyle.id(),
					getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(NumberFormatHelper.format(Thick.getValue()),
					getBorderStyleAttribute(rightCell,
						BorderPosition.Right.toString(), "Weight"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritInteriorStyle() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with inherited interior styles";
				final var RED_COLOR = "#ff0000";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style rightStyle;
				Style redInteriorStyle;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var redInterior = Interior.builder().withColor(RED_COLOR).build();
					redInteriorStyle = generator.createStyle().withInterior(redInterior).build();
					assertSame(redInterior, redInteriorStyle.interior());

					var rightAlignment = Alignment.builder().withHorizontal(Right).build();
					rightStyle = generator.createStyle(redInteriorStyle).withAlignment(rightAlignment).build();
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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);

				// Validate red style
				var cellRedBackground = searchCells(rows.get(0)).get(0);
				assertEquals(RED_COLOR, getInteriorStyleAttribute(cellRedBackground, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(cellRedBackground, "Pattern"));

				// Validate horizontal alignment style
				var row4 = rows.get(4);
				var rightCell = searchCells(row4).get(0);
				assertEquals(Right.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				var rightStyleElement = searchStyle(doc, rightStyle.id());
				assertEquals(redInteriorStyle.id(), getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(RED_COLOR, getInteriorStyleAttribute(rightCell, "Color"));
				// Make sure the generator has written the solid pattern too
				assertEquals("Solid", getInteriorStyleAttribute(rightCell, "Pattern"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
	
	@Test 
	public void testInheritNumberFormatStyle() {
		executeWithTempFile( baos -> {
			try {
				final var SHEET_CAPTION = "a sheet with inherited number format styles";
				final var NUMBER_FORMAT = "#####.000";

				// The references to the styles are kept in order to make assertions
				//	on them later, if it were not in a unit test it would not be necessary
				Style numberFormatStyle;
				Style rightStyle;
				try (var generator = new XMLSpreadsheetGenerator(baos)) {
					var nf = new NumberFormat(NUMBER_FORMAT);
					numberFormatStyle = generator.createStyle().withNumberFormat(nf).build();
					assertSame(nf, numberFormatStyle.numberFormat());

					var leftAlignment = Alignment.builder().withHorizontal(Left).build();
					rightStyle = generator.createStyle(numberFormatStyle).withAlignment(leftAlignment).build();
					assertSame(leftAlignment, rightStyle.alignment());

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

				var document = baos.toString(Charset.forName("cp1252"));
				// Not empty and correct document
				var doc = parseDocument(document);
				assertNotNull(doc);

				var rows = searchRows(doc, SHEET_CAPTION);

				// Validate number format style
				var cellNumberFormat = searchCells(rows.get(0)).get(0);
				assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(cellNumberFormat, "Format"));


				// Validate horizontal alignment style
				var row4 = rows.get(4);
				var rightCell = searchCells(row4).get(0);
				assertEquals(HorizontalAlignment.Left.toString(), getAlignmentAttribute(rightCell, "Horizontal"));

				// Validate inheritance
				var rightStyleElement = searchStyle(doc, rightStyle.id());
				assertEquals(numberFormatStyle.id(), getAttributeValue(rightStyleElement, "Parent", "ss"));
				assertEquals(NUMBER_FORMAT, getNumberFormatAttribute(rightCell, "Format"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});
	}
}