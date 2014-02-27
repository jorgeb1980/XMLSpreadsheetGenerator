package tests.styles;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.style.NumberFormat.Format;

public class TestNumberFormat {

	// TEST SETUP
	
	NumberFormat numberFormat = null;
	
	@Before
	public void init() {
		try { 
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			numberFormat = style.numberFormat();
			Assert.assertNotNull(numberFormat);	
		}
		catch (Throwable e) {
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
		StyleTestUtils.checkAttributeValue(numberFormat, "Format", null);
		
		// try some formats
		numberFormat.setFormat("####.#");
		StyleTestUtils.checkAttributeValue(numberFormat, "Format", "####.#");
		
		numberFormat.setFormat("#.0#");
		StyleTestUtils.checkAttributeValue(numberFormat, "Format", "#.0#");
		
		numberFormat.setFormat("# ???/???");
		StyleTestUtils.checkAttributeValue(numberFormat, "Format", "# ???/???");
	}

	@Test
	public void testSetFormatFormat() {
		// null by default
		StyleTestUtils.checkAttributeValue(numberFormat, "Format", null);
		
		// try all values
		for (Format format: Format.values()) {
			numberFormat.setFormat(format);
			StyleTestUtils.checkAttributeValue(numberFormat, "Format", format.toString());
		}
	}

}
