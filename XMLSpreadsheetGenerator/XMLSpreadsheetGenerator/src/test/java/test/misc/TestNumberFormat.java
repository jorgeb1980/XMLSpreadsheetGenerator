/**
 * 
 */
package test.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import xml.spreadsheet.utils.NumberFormatHelper;

public class TestNumberFormat {

	@Test
	public void testNull() {
		assertNull(NumberFormatHelper.formatDouble(null));
	}

	@Test
	public void testSimple() {
		assertEquals("3", NumberFormatHelper.formatDouble(3d));
	}
	
	@Test
	public void testDecimals() {
		assertEquals("3.56", NumberFormatHelper.formatDouble(3.56));
	}
	
	@Test
	public void testZero() {
		assertEquals("0.56", NumberFormatHelper.formatDouble(0.56d));
	}
	
	@Test
	public void testZeroes() {
		assertEquals("1.00056", NumberFormatHelper.formatDouble(1.00056d));
	}
}
