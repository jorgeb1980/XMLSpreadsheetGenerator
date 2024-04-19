package tests.styles;

import org.junit.jupiter.api.Test;
import xml.spreadsheet.style.NumberFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.style.NumberFormat.from;

public class TestNumberFormat {

	private void testNumberFormat(String nf) {
		NumberFormat format = new NumberFormat(nf);
		checkAttributeValue(format, "Format", nf);
	}

	@Test
	public void testSetFormatString() {
		NumberFormat nf1 = new NumberFormat(null);
		// null by default
		checkAttributeValue(nf1, "Format", null);
		testNumberFormat("####.#");
		testNumberFormat("#.0#");
		testNumberFormat("# ???/???");
	}

	@Test
	public void testSetFormatFormat() {
		// null by default
		checkAttributeValue(new NumberFormat(null), "Format", null);

		try {
			// Check all the constants of type NumberFormat
			for (Field f : NumberFormat.class.getDeclaredFields()) {
				int modifiers = f.getModifiers();
				if (f.getAnnotatedType().getType().getTypeName().equals("NumberFormat")
					&& Modifier.isStatic(modifiers)
					&& Modifier.isFinal(modifiers)) {
					testNumberFormat(((NumberFormat) f.get(null)).format());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

}
