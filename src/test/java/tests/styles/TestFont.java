package tests.styles;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Font.*;

import static org.junit.jupiter.api.Assertions.*;
import static tests.styles.StyleTestUtils.checkAttributeValue;
import static xml.spreadsheet.Charset.*;
import static xml.spreadsheet.style.Font.*;

public class TestFont {

	@BeforeAll
	public static void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			var generator = new XMLSpreadsheetGenerator(null);
			var style = generator.createStyle().build();
			assertNull(style.font());
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetBold() {
		// null by default
		checkAttributeValue(builder().build(), "Bold", null);
		// 0 if false
		checkAttributeValue(builder().withBold(false).build(), "Bold", "0");
		// 1 if true
		checkAttributeValue(builder().withBold(true).build(), "Bold", "1");
	}

	@Test
	public void testSetColor() {
		// null by default
		checkAttributeValue(builder().build(), "Color", null);
		// Check different values
		checkAttributeValue(builder().withColor(COLOR_AUTOMATIC).build(), "Color", COLOR_AUTOMATIC);
		// dark red
		checkAttributeValue(builder().withColor("#C14949").build(), "Color", "#C14949");
		// dark blue
		checkAttributeValue(builder().withColor("#1B1F97").build(), "Color", "#1B1F97");
		// dark green
		checkAttributeValue(builder().withColor("#096F27").build(), "Color", "#096F27");
	}

	@Test
	public void testSetFontName() {
		// null by default
		checkAttributeValue(builder().build(), "FontName", null);
		// Arial
		checkAttributeValue(builder().withFontName("Arial").build(), "FontName", "Arial");
		// Arial
		checkAttributeValue(builder().withFontName("Verdana").build(), "FontName", "Verdana");
	}

	@Test
	public void testSetItalic() {
		// null by default
		checkAttributeValue(builder().build(), "Italic", null);
		// 0 if false
		checkAttributeValue(builder().withItalic(false).build(), "Italic", "0");
		// 1 if true
		checkAttributeValue(builder().withItalic(true).build(), "Italic", "1");
	}

	@Test
	public void testSetOutline() {
		// null by default
		checkAttributeValue(builder().build(), "Outline", null);
		// 0 if false
		checkAttributeValue(builder().withOutline(false).build(), "Outline", "0");
		// 1 if true
		checkAttributeValue(builder().withOutline(true).build(), "Outline", "1");
	}

	@Test
	public void testSetShadow() {
		// null by default
		checkAttributeValue(builder().build(), "Shadow", null);
		// 0 if false
		checkAttributeValue(builder().withShadow(false).build(), "Shadow", "0");
		// 1 if true
		checkAttributeValue(builder().withShadow(true).build(), "Shadow", "1");
	}

	@Test
	public void testSetSize() {
		// null by default
		checkAttributeValue(builder().build(), "Size", null);
		try {
			// Must fail
			builder().withSize(-1).build();
			fail();
		}
		catch(IllegalArgumentException e) {
			assertNotNull(e);
		}
		// 12 points
		checkAttributeValue(builder().withSize(12).build(), "Size", "12");
		// 13 points
		checkAttributeValue(builder().withSize(13).build(), "Size", "13");
		// 14 points
		checkAttributeValue(builder().withSize(14).build(), "Size", "14");
	}

	@Test
	public void testSetStrikeThrough() {
		// null by default
		checkAttributeValue(builder().build(), "StrikeThrough", null);
		// 0 if false
		checkAttributeValue(builder().withStrikeThrough(false).build(), "StrikeThrough", "0");
		// 1 if true
		checkAttributeValue(builder().withStrikeThrough(true).build(), "StrikeThrough", "1");
	}

	@Test
	public void testSetUnderline() {
		// null by default
		checkAttributeValue(builder().build(), "Underline", null);
		// Try all alternatives
		for (var underline: Underline.values()) {
			checkAttributeValue(builder().withUnderline(underline).build(), "Underline", underline.toString());
		}
	}

	@Test
	public void testSetVerticalAlign() {
		// null by default
		checkAttributeValue(builder().build(), "VerticalAlign", null);
		// check all alternatives
		for (VerticalAlignment alignment: VerticalAlignment.values()) {
			checkAttributeValue(builder().withVerticalAlign(alignment).build(), "VerticalAlign", alignment.toString());
		}
	}

	@Test
	public void testSetCharSet() {
		// null by default
		checkAttributeValue("x", builder().build(), "CharSet", null);
		// try some values
		checkAttributeValue("x", builder().withCharSet(ANSI_Latin.getValue()).build(), "CharSet",
			Integer.toString(ANSI_Latin.getValue()));
		checkAttributeValue("x", builder().withCharSet(ANSI_Arabic.getValue()).build(), "CharSet",
			Integer.toString(ANSI_Arabic.getValue()));
		checkAttributeValue("x", builder().withCharSet(Apple_Roman.getValue()).build(), "CharSet",
			Integer.toString(Apple_Roman.getValue()));
	}

	@Test
	public void copyConstructor() {
		assertNull(from(null));
	}

	@Test
	public void testSetFamily() {
		// null by default
		checkAttributeValue("x", builder().build(), "Family", null);
		
		// try all the possible values
		for (var family: FontFamily.values()) {
			checkAttributeValue("x", builder().withFamily(family).build(), "Family", family.toString());
		}
	}

}
