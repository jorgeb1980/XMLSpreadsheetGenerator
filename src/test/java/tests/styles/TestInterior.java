package tests.styles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Interior.FillPattern;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.Interior.FillPattern.Solid;
import static xml.spreadsheet.style.Interior.builder;
import static xml.spreadsheet.style.Interior.from;

public class TestInterior {
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle().build();
			Assert.assertNull(style.interior());
		}
		catch (Throwable e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetColor() {
		// null by default
		checkAttributeValue(builder().build(), "Color", null);
		// dark red
		checkAttributeValue(builder().withColor("#C14949").build(), "Color", "#C14949");
		// dark blue
		checkAttributeValue(builder().withColor("#1B1F97").build(), "Color", "#1B1F97");
		// dark green
		checkAttributeValue(builder().withColor("#096F27").build(), "Color", "#096F27");
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

	@Test
	public void testSetPattern() {
		// Solid by default
		checkAttributeValue(builder().build(), "Pattern", Solid.toString());
		for (FillPattern pattern: FillPattern.values()) {
			checkAttributeValue(builder().withPattern(pattern).build(), "Pattern", pattern.toString());
		}
	}

}
