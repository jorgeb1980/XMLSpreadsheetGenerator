package tests.styles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Alignment.VerticalAlignment;

import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;

/**
 * This test suite checks the generated XML, building it with JDOM and comparing
 * the element attributes with the expected value
 */
public class TestAlignment {

	Alignment alignment = null;
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			alignment = style.alignment();
			Assert.assertNotNull(alignment);	
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	
	
	// UNIT TESTS
	
	@Test
	public void testSetHorizontal() {
		// Null by default
		checkAttributeValue(alignment, "Horizontal", null);
		// Check the generated value for all the alternatives
		for (HorizontalAlignment horizontalAlignment: HorizontalAlignment.values()) {
			alignment.setHorizontal(horizontalAlignment);	
			checkAttributeValue(alignment, "Horizontal",
					horizontalAlignment.toString());			
		}
	}

	@Test
	public void testSetRotate() {
		// Check the generated value for all the alternatives
		
		// By default, null
		checkAttributeValue(alignment, "Rotate", null);
		// 0 must return null
		alignment.setRotate(0);
		checkAttributeValue(alignment, "Rotate", "0");
		
		alignment.setRotate(90);
		checkAttributeValue(alignment, "Rotate", "90");
		
		alignment.setRotate(-90);
		checkAttributeValue(alignment, "Rotate", "-90");
	}

	@Test
	public void testSetShrinkToFit() {
		// Check the generated value
		
		// By default, null
		checkAttributeValue(alignment, "ShrinkToFit", null);
		// Null if false
		alignment.setShrinkToFit(false);
		checkAttributeValue(alignment, "ShrinkToFit", "0");
		// 1 if true
		alignment.setShrinkToFit(true);
		checkAttributeValue(alignment, "ShrinkToFit", "1");
	}

	@Test
	public void testSetVertical() {
		// Null by default
		checkAttributeValue(alignment, "Vertical", null);
		// Check the generated values for all the alternatives
		for (VerticalAlignment verticalAlignment: VerticalAlignment.values()) {
			alignment.setVertical(verticalAlignment);
			checkAttributeValue(alignment, "Vertical", verticalAlignment.toString());
		}
	}

	@Test
	public void testSetVerticalText() {
		// Null by default
		checkAttributeValue(alignment, "VerticalText", null);
		// Check the possible values
		
		// Null if false
		alignment.setVerticalText(false);
		checkAttributeValue(alignment, "VerticalText", "0");
		// 1 if true
		alignment.setVerticalText(true);
		checkAttributeValue(alignment, "VerticalText", "1");
	}

	@Test
	public void testSetWrapText() {
		// Null by default
		checkAttributeValue(alignment, "WrapText", null);
		
		// Null if false
		alignment.setWrapText(false);
		checkAttributeValue(alignment, "WrapText", "0");
		// 1 if true
		alignment.setWrapText(true);
		checkAttributeValue(alignment, "WrapText", "1");
	}

}
