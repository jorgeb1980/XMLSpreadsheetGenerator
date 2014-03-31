package tests.misc;

import static org.junit.Assert.*;

import org.junit.Test;

import xml.spreadsheet.utils.BooleanFormatHelper;

public class TestBooleanFormat {

	@Test
	public void testNull() {
		assertNull(BooleanFormatHelper.format(null));
	}
	
	@Test
	public void testTrue() {
		assertEquals("1", BooleanFormatHelper.format(Boolean.TRUE));
	}
	
	@Test
	public void testFalse() {
		assertEquals("0", BooleanFormatHelper.format(Boolean.FALSE));
	}
}
