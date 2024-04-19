package tests.misc;

import org.junit.jupiter.api.Test;
import xml.spreadsheet.utils.DateFormatHelper;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

public class TestDateFormat {

	@Test
	public void testNull() {
		assertNull(DateFormatHelper.format(null));
	}
	
	@Test
	public void testDateFormat() {
		try {
			var sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
			var dateTest1 = sdf.parse("22-03-2014 00:00:00:000");
			assertEquals("2014-03-22T00:00:00.000", DateFormatHelper.format(dateTest1));

			var dateTest2 = sdf.parse("02-03-2014 00:15:00:000");
			assertEquals("2014-03-02T00:15:00.000", DateFormatHelper.format(dateTest2));

			var dateTest3 = sdf.parse("22-01-2014 13:00:00:000");
			assertEquals("2014-01-22T13:00:00.000", DateFormatHelper.format(dateTest3));

			var dateTest4 = sdf.parse("22-03-1974 01:00:01:000");
			assertEquals("1974-03-22T01:00:01.000", DateFormatHelper.format(dateTest4));

			var dateTest5 = sdf.parse("22-03-2014 00:00:00:167");
			assertEquals("2014-03-22T00:00:00.167", DateFormatHelper.format(dateTest5));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
