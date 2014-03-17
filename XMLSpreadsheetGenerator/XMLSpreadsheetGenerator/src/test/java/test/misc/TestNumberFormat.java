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
		assertNull(NumberFormatHelper.format(null));
	}

	@Test
	public void testSimple() {
		assertEquals("3", NumberFormatHelper.format(3d));
	}
	
	@Test
	public void testDecimals() {
		assertEquals("3.56", NumberFormatHelper.format(3.56));
	}
	
	@Test
	public void testZero() {
		assertEquals("0.56", NumberFormatHelper.format(0.56d));
	}
	
	@Test
	public void testZeroes() {
		assertEquals("1.00056", NumberFormatHelper.format(1.00056d));
	}
}
