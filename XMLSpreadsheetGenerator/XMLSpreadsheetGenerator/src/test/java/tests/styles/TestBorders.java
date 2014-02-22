package tests.styles;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Border;
import xml.spreadsheet.style.Border.BorderPosition;
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
	
	//public void 

}
