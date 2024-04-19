package tests.misc;

import org.junit.jupiter.api.Test;
import xml.spreadsheet.utils.BooleanFormatHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
