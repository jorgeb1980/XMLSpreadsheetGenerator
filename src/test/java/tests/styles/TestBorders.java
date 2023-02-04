package tests.styles;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Border.BorderPosition.Bottom;
import static xml.spreadsheet.style.Border.from;
import static xml.spreadsheet.style.Borders.builder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Borders;

import java.util.Arrays;
import java.util.List;

public class TestBorders {
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle().build();
			Assert.assertNull(style.borders());
		}
		catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////

	
	@Test
	public void testRepeatBorder() {
		try {
			// Reset the borders object
			Border bottomBorder = Border.builder().withPosition(Bottom).build();
			builder().withBorder(bottomBorder).withBorder(bottomBorder).build();
			fail();
		}
		catch (XMLSpreadsheetException e) {
			Assert.assertEquals("The Bottom border is already defined", e.getMessage());
		}
	}
	
	@Test
	public void testSetLinestyle() {
		try {
			for (BorderPosition position: BorderPosition.values()) {
				// For each possible position, try each possible line style
				for (LineStyle lineStyle: LineStyle.values()) {
					Border b = Border.builder().withPosition(position).withLineStyle(lineStyle).build();
					Borders borders = builder().withBorder(b).build();
					checkAttributeValue(borders,
						"//ss:Border[@ss:Position='" + position + "']",
						"LineStyle", lineStyle.toString());
				}
			}
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testSetColor() {
		List<String> colors = Arrays.asList(
			Border.COLOR_AUTOMATIC,
			"#C14949",
			"#1B1F97",
			"#096F27"
		);
		try {
			for (BorderPosition position: BorderPosition.values()) {
				for (String color: colors) {
					Borders borders = builder().
						withBorder(
							Border.builder().
								withPosition(position).
								withColor(color).
								build()
						).build();
					// For each possible position, try colors
					checkAttributeValue(
						borders,
						"//ss:Border[@ss:Position='" + position + "']",
						"Color",
						color
					);
				}
			}
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	public void testSetWeight() {
		try {
			for (BorderPosition position: BorderPosition.values()) {
				// For each possible position, try weights
				for (BorderWeight weight: BorderWeight.values()) {
					Borders borders = builder().withBorder(
						Border.builder().
							withPosition(position).
							withWeight(weight).
							build()
					).build();
					checkAttributeValue(
						borders,
						"//ss:Border[@ss:Position='" + position + "']",
						"Weight",
						weight.getValue()
					);
				}
			}
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void setCustomSetWeight() {
		try {
			for (BorderPosition position: BorderPosition.values()) {
				final double WEIGHT_VALUE = 3.3d;
				Borders borders = builder().
					withBorder(
						Border.builder().
							withWeight(WEIGHT_VALUE).
							withPosition(position).
							build()
					).
					build();
				checkAttributeValue(
					borders,
					"//ss:Border[@ss:Position='" + position + "']",
					"Weight",
					WEIGHT_VALUE
				);
			}
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

	@Test
	public void testEmptyBorders() {
		Assert.assertEquals("", builder().build().toString().trim());
		Assert.assertEquals("", new Borders(null).toString().trim());
	}
}
