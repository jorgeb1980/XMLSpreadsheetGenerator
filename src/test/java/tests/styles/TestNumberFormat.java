package tests.styles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.style.NumberFormat.Format;

import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;

public class TestNumberFormat {

	// TEST SETUP
	NumberFormat numberFormat = null;
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			numberFormat = style.numberFormat();
			Assert.assertNotNull(numberFormat);	
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	///////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	// UNIT TESTS
	
	@Test
	public void testSetFormatString() {
		// null by default
		checkAttributeValue(numberFormat, "Format", null);
		
		// try some formats
		numberFormat.setFormat("####.#");
		checkAttributeValue(numberFormat, "Format", "####.#");
		
		numberFormat.setFormat("#.0#");
		checkAttributeValue(numberFormat, "Format", "#.0#");
		
		numberFormat.setFormat("# ???/???");
		checkAttributeValue(numberFormat, "Format", "# ???/???");
	}

	@Test
	public void testSetFormatFormat() {
		// null by default
		checkAttributeValue(numberFormat, "Format", null);
		
		// try all values
		for (Format format: Format.values()) {
			numberFormat.setFormat(format);
			checkAttributeValue(numberFormat, "Format", format.toString());
		}
	}

}
