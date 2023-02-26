package tests.styles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Protection.from;
import static xml.spreadsheet.style.Protection.builder;

public class TestProtection {

	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle().build();
			Assert.assertNull(style.protection());
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetProtectedCell() {
		// Null by default
		checkAttributeValue(builder().build(), "Protected", null);
		// 1 if true
		checkAttributeValue(builder().withProtectedCell(true).build(), "Protected", "1");
		// 0 if false
		checkAttributeValue(builder().withProtectedCell(false).build(), "Protected", "0");
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

	@Test
	public void testSetHideFormula() {
		// Null by default
		checkAttributeValue("x", builder().build(), "HideFormula", null);
		// 1 if true
		checkAttributeValue("x", builder().withHideFormula(true).build(), "HideFormula", "1");
		// 0 if false
		checkAttributeValue("x", builder().withHideFormula(false).build(), "HideFormula", "0");
	}

}
