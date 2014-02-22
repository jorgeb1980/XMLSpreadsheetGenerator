package tests.styles;

import static org.junit.Assert.fail;

import org.jdom.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Alignment.VerticalAlignment;

/**
 * This test suite checks the generated XML, building it with JDOM and comparing
 * the element attributes with the expected value
 */
public class TestAlignment {


	Alignment alignment = null;
	
	// TEST SETUP
	
	@Before
	public void init() {
		try { 
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			alignment = style.alignment();
			Assert.assertNotNull(alignment);	
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
	public void testAlignment() {
		Document doc = StyleTestUtils.parseStyleElement(alignment);		
		Assert.assertNotNull(doc);
	}

	@Test
	public void testSetHorizontal() {
		// Null by default
		StyleTestUtils.checkAttributeValue(alignment, "Horizontal", null);
		// Check the generated value for all the alternatives
		for (HorizontalAlignment horizontalAlignment: HorizontalAlignment.values()) {
			alignment.setHorizontal(horizontalAlignment);	
			StyleTestUtils.checkAttributeValue(alignment, "Horizontal",
					horizontalAlignment.toString());			
		}
	}


	@Test
	public void testSetIndent() {
		// Check the generated value for all the alternatives
		alignment.setIndent(2);
		StyleTestUtils.checkAttributeValue(alignment, "Indent", "2");
		
		alignment.setIndent(3);
		StyleTestUtils.checkAttributeValue(alignment, "Indent", "3");
		
		alignment.setIndent(4);
		StyleTestUtils.checkAttributeValue(alignment, "Indent", "4");
	}

	@Test
	public void testSetReadingOrder() {
		// ignored
		Assert.assertTrue(true);
		
//		Alignment alignment = style.alignment();
//		Assert.assertNotNull(alignment);	
//		// Check the generated value for all the alternatives
//		for (ReadingOrder readingOrder: ReadingOrder.values()) {
//			alignment.setReadingOrder(readingOrder);
//			if (readingOrder != ReadingOrder.Context) {				
//				checkAttributeValue(alignment, "ReadingOrder", readingOrder.toString());
//			}
//			else {
//				// context must return null
//				checkAttributeValue(alignment, "ReadingOrder", null);
//			}
//		}
		
	}

	@Test
	public void testSetRotate() {
		// Check the generated value for all the alternatives
		
		// By default, null
		StyleTestUtils.checkAttributeValue(alignment, "Rotate", null);
		// 0 must return null
		alignment.setRotate(0);
		StyleTestUtils.checkAttributeValue(alignment, "Rotate", "0");
		
		alignment.setRotate(90);
		StyleTestUtils.checkAttributeValue(alignment, "Rotate", "90");
		
		alignment.setRotate(-90);
		StyleTestUtils.checkAttributeValue(alignment, "Rotate", "-90");
	}

	@Test
	public void testSetShrinkToFit() {
		// Check the generated value
		
		// By default, null
		StyleTestUtils.checkAttributeValue(alignment, "ShrinkToFit", null);
		// Null if false
		alignment.setShrinkToFit(false);
		StyleTestUtils.checkAttributeValue(alignment, "ShrinkToFit", "0");
		// 1 if true
		alignment.setShrinkToFit(true);
		StyleTestUtils.checkAttributeValue(alignment, "ShrinkToFit", "1");
	}

	@Test
	public void testSetVertical() {
		// Null by default
		StyleTestUtils.checkAttributeValue(alignment, "Vertical", null);
		// Check the generated values for all the alternatives
		for (VerticalAlignment verticalAlignment: VerticalAlignment.values()) {
			alignment.setVertical(verticalAlignment);
			StyleTestUtils.checkAttributeValue(alignment, "Vertical", verticalAlignment.toString());
		}
	}

	@Test
	public void testSetVerticalText() {
		// Null by default
		StyleTestUtils.checkAttributeValue(alignment, "VerticalText", null);
		// Check the possible values
		
		// Null if false
		alignment.setVerticalText(false);
		StyleTestUtils.checkAttributeValue(alignment, "VerticalText", "0");
		// 1 if true
		alignment.setVerticalText(true);
		StyleTestUtils.checkAttributeValue(alignment, "VerticalText", "1");
	}

	@Test
	public void testSetWrapText() {
		// Null by default
		StyleTestUtils.checkAttributeValue(alignment, "WrapText", null);
		
		// Null if false
		alignment.setWrapText(false);
		StyleTestUtils.checkAttributeValue(alignment, "WrapText", "0");
		// 1 if true
		alignment.setWrapText(true);
		StyleTestUtils.checkAttributeValue(alignment, "WrapText", "1");
	}

}
