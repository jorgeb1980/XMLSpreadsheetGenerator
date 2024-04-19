package tests.styles;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
import xml.spreadsheet.style.Border.BorderWeight;
import xml.spreadsheet.style.Border.LineStyle;
import xml.spreadsheet.style.Borders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Border.BorderPosition.Bottom;
import static xml.spreadsheet.style.Border.from;
import static xml.spreadsheet.style.Borders.builder;

public class TestBorders {
	
	@BeforeAll
	public static void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			var generator = new XMLSpreadsheetGenerator(null);
			var style = generator.createStyle().build();
			assertNull(style.borders());
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
			var bottomBorder = Border.builder().withPosition(Bottom).build();
			builder().withBorder(bottomBorder).withBorder(bottomBorder).build();
			fail();
		}
		catch (XMLSpreadsheetException e) {
			assertEquals("The Bottom border is already defined", e.getMessage());
		}
	}
	
	@Test
	public void testSetLinestyle() {
		try {
			for (var position: BorderPosition.values()) {
				// For each possible position, try each possible line style
				for (var lineStyle: LineStyle.values()) {
					var b = Border.builder().withPosition(position).withLineStyle(lineStyle).build();
					var borders = builder().withBorder(b).build();
					checkAttributeValue(borders,
						"//ss:Border[@ss:Position='" + position + "']",
						"LineStyle", lineStyle.toString()
					);
				}
			}
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testSetColor() {
		var colors = Arrays.asList(
			Border.COLOR_AUTOMATIC,
			"#C14949",
			"#1B1F97",
			"#096F27"
		);
		try {
			for (var position: BorderPosition.values()) {
				for (var color: colors) {
					var borders = builder().
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
			for (var position: BorderPosition.values()) {
				// For each possible position, try weights
				for (BorderWeight weight: BorderWeight.values()) {
					var borders = builder().withBorder(
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
			for (var position: BorderPosition.values()) {
				final var WEIGHT_VALUE = 3.3d;
				var borders = builder().
					withBorder(
						Border.builder().
							withWeight(WEIGHT_VALUE).
							withPosition(position).
							build()
					).build();
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
		assertEquals("", builder().build().toString().trim());
		assertEquals("", new Borders(null).toString().trim());
	}
}
