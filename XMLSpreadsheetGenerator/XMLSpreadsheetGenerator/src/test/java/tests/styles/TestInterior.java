package tests.styles;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Interior;

public class TestInterior {

	Interior interior = null;
	
	// TEST SETUP
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			interior = style.interior();
			Assert.assertNotNull(interior);	
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
	public void testSetColor() {
		// null by default
		StyleTestUtils.checkAttributeValue(interior, "Color", null);
		
		// Try some values
		// dark red
		interior.setColor("#C14949");
		StyleTestUtils.checkAttributeValue(interior, "Color", "#C14949");
		
		// dark blue
		interior.setColor("#1B1F97");
		StyleTestUtils.checkAttributeValue(interior, "Color", "#1B1F97");
		
		// dark green
		interior.setColor("#096F27");
		StyleTestUtils.checkAttributeValue(interior, "Color", "#096F27");
	}

}
