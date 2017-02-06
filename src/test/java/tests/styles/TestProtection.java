package tests.styles;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Protection;

public class TestProtection {

	Protection protection = null;
	
	// TEST SETUP
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			protection = style.protection();
			Assert.assertNotNull(protection);	
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
	public void testSetProtectedCell() {
		// Null by default
		StyleTestUtils.checkAttributeValue(protection, "Protected", null);
		
		// 1 if true
		protection.setProtectedCell(true);
		StyleTestUtils.checkAttributeValue(protection, "Protected", "1");
		
		// 0 if false
		protection.setProtectedCell(false);
		StyleTestUtils.checkAttributeValue(protection, "Protected", "0");
	}

	@Test
	public void testSetHideFormula() {
		// Null by default
		StyleTestUtils.checkAttributeValue("x", protection, "HideFormula", null);
		
		// 1 if true
		protection.setHideFormula(true);
		StyleTestUtils.checkAttributeValue("x", protection, "HideFormula", "1");
		
		// 0 if false
		protection.setHideFormula(false);
		StyleTestUtils.checkAttributeValue("x", protection, "HideFormula", "0");
	}

}
