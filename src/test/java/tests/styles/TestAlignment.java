package tests.styles;

import org.junit.Before;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Alignment.HorizontalAlignment;
import xml.spreadsheet.style.Alignment.VerticalAlignment;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Alignment.builder;
import static xml.spreadsheet.style.Alignment.from;

/**
 * This test suite checks the generated XML, building it with JDOM and comparing
 * the element attributes with the expected value
 */
public class TestAlignment {
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle().build();
			assertNull(style.alignment());
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
		checkAttributeValue(builder().build(), "Horizontal", null);
		// Check the generated value for all the alternatives
		for (HorizontalAlignment horizontalAlignment: HorizontalAlignment.values()) {
			Alignment alignment = builder().withHorizontal(horizontalAlignment).build();
			checkAttributeValue(alignment, "Horizontal",
					horizontalAlignment.toString());			
		}
	}

	@Test
	public void testSetRotate() {
		// By default, null
		checkAttributeValue(builder().build(), "Rotate", null);
		// 0 must return null
		checkAttributeValue(builder().withRotate(0).build(), "Rotate", "0");
		
		checkAttributeValue(builder().withRotate(90).build(), "Rotate", "90");
		
		checkAttributeValue(builder().withRotate(-90).build(), "Rotate", "-90");
	}

	@Test
	public void testSetShrinkToFit() {
		// By default, null
		checkAttributeValue(builder().build(), "ShrinkToFit", null);
		// Null if false
		checkAttributeValue(builder().withShrinkToFit(false).build(), "ShrinkToFit", "0");
		// 1 if true
		checkAttributeValue(builder().withShrinkToFit(true).build(), "ShrinkToFit", "1");
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

	@Test
	public void testSetVertical() {
		// Null by default
		checkAttributeValue(builder().build(), "Vertical", null);
		// Check the generated values for all the alternatives
		for (VerticalAlignment verticalAlignment: VerticalAlignment.values()) {
			Alignment alignment = builder().withVertical(verticalAlignment).build();
			checkAttributeValue(alignment, "Vertical", verticalAlignment.toString());
		}
	}

	@Test
	public void testSetVerticalText() {
		// Null by default
		checkAttributeValue(builder().build(), "VerticalText", null);
		// Null if false
		checkAttributeValue(builder().withVerticalText(false).build(), "VerticalText", "0");
		// 1 if true
		checkAttributeValue(builder().withVerticalText(true).build(), "VerticalText", "1");
	}

	@Test
	public void testSetWrapText() {
		// Null by default
		checkAttributeValue(builder().build(), "WrapText", null);
		// Null if false
		checkAttributeValue(builder().withWrapText(false).build(), "WrapText", "0");
		// 1 if true
		checkAttributeValue(builder().withWrapText(true).build(), "WrapText", "1");
	}

}
