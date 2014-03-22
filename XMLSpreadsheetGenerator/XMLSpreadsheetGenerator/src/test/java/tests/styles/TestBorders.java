package tests.styles;

import static org.junit.Assert.fail;

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

public class TestBorders {

	// TEST SETUP
	
	Borders borders = null;
	
	@Before
	public void init() {
		try { 
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			borders = style.borders();
			Assert.assertNotNull(borders);	
		}
		catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	
	
	// UNIT TESTS
	
	@Test
	public void testCreateBorders() {
		Assert.assertNotNull(borders);
	}
	
	@Test
	public void testRepeatBorder() {
		try {
			// Reset the borders object
			borders = new Borders();
			borders.createBorder(BorderPosition.Bottom);
			borders.createBorder(BorderPosition.Bottom);
			Assert.assertTrue(false);
		}
		catch (XMLSpreadsheetException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testCreateBorder() {
		try {
			// Check every possible value
			for (BorderPosition borderPosition: BorderPosition.values()) {
				Border b = borders.createBorder(borderPosition);
				Assert.assertNotNull(b);
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + borderPosition.toString()+"']", 
						"Position", borderPosition.toString());
			}
		}
		catch(Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testSetLinestyle() {
		try {
			// Reset the borders object
			borders = new Borders();
			for (BorderPosition position: BorderPosition.values()) {
				Border b = borders.createBorder(position);
				// Null if default
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"LineStyle", null);
				// For each possible position, try each possible line style
				for (LineStyle lineStyle: LineStyle.values()) {
					b.setLineStyle(lineStyle);
					StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"LineStyle", lineStyle.toString());
				}
			}
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void testSetColor() {
		try {
			// Reset the borders object
			borders = new Borders();
			for (BorderPosition position: BorderPosition.values()) {
				Border b = borders.createBorder(position);
				// Null if default
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Color", null);
				
				// For each possible position, try colors
				b.setColor(Border.COLOR_AUTOMATIC);
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Color", Border.COLOR_AUTOMATIC);
				
				// dark red
				b.setColor("#C14949");
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Color", "#C14949");
				
				// dark blue
				b.setColor("#1B1F97");
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Color", "#1B1F97");
				
				// dark green
				b.setColor("#096F27");
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Color", "#096F27");
			}
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	public void testSetWeight() {
		try {
			// Reset the borders object
			borders = new Borders();
			for (BorderPosition position: BorderPosition.values()) {
				Border b = borders.createBorder(position);
				// Null if default
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Weight", null);
				
				// For each possible position, try weights
				for (BorderWeight weight: BorderWeight.values()) {
					b.setWeight(weight);
					StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Weight", weight.getValue());
				}
			}
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
	}
	
	@Test
	public void setCustomSetWeight() {
		try {
			borders = new Borders();
			for (BorderPosition position: BorderPosition.values()) {
				Border b = borders.createBorder(position);
				// Null if default
				StyleTestUtils.checkAttributeValue(borders, 
						"//ss:Border[@ss:Position='" + position.toString()+"']", 
						"Weight", null);
				final Double WEIGHT_VALUE = 3.3d;
				b.setWeight(WEIGHT_VALUE);
				StyleTestUtils.checkAttributeValue(borders, 
					"//ss:Border[@ss:Position='" + position.toString()+"']", 
					"Weight", WEIGHT_VALUE);
				
			}
		}
		catch (Throwable t) {
			fail(t.getMessage());
		}
	}
}
