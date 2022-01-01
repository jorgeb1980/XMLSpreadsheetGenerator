package tests.styles;

import static org.junit.Assert.fail;
import static tests.styles.StyleTestUtils.checkAttributeValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Charset;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Font.FontFamily;
import xml.spreadsheet.style.Font.Underline;
import xml.spreadsheet.style.Font.VerticalAlignment;

public class TestFont {

	// TEST SETUP
	Font font = null;
	
	@Before
	public void init() {
		try { 
			// Don't mind here to have a warning that the resource is never closed
			@SuppressWarnings("resource")
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			font = style.font();
			Assert.assertNotNull(font);	
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetBold() {
		// null by default
		checkAttributeValue(font, "Bold", null);
		
		// 0 if false
		font.setBold(false);
		checkAttributeValue(font, "Bold", "0");
		
		// 1 if true
		font.setBold(true);
		checkAttributeValue(font, "Bold", "1");
	}

	@Test
	public void testSetColor() {
		// null by default
		checkAttributeValue(font, "Color", null);
		
		// Check different values
		
		font.setColor(Font.COLOR_AUTOMATIC);
		checkAttributeValue(font, "Color", Font.COLOR_AUTOMATIC);
		
		// dark red
		font.setColor("#C14949");
		checkAttributeValue(font, "Color", "#C14949");
		
		// dark blue
		font.setColor("#1B1F97");
		checkAttributeValue(font, "Color", "#1B1F97");
		
		// dark green
		font.setColor("#096F27");
		checkAttributeValue(font, "Color", "#096F27");
	}

	@Test
	public void testSetFontName() {
		// null by default
		checkAttributeValue(font, "FontName", null);
		
		// Arial
		font.setFontName("Arial");
		checkAttributeValue(font, "FontName", "Arial");
		
		// Arial
		font.setFontName("Verdana");
		checkAttributeValue(font, "FontName", "Verdana");
	}

	@Test
	public void testSetItalic() {
		// null by default
		checkAttributeValue(font, "Italic", null);
		
		// 0 if false
		font.setItalic(false);
		checkAttributeValue(font, "Italic", "0");
		
		// 1 if true
		font.setItalic(true);
		checkAttributeValue(font, "Italic", "1");
	}

	@Test
	public void testSetOutline() {
		// null by default
		checkAttributeValue(font, "Outline", null);
		
		// 0 if false
		font.setOutline(false);
		checkAttributeValue(font, "Outline", "0");
		
		// 1 if true
		font.setOutline(true);
		checkAttributeValue(font, "Outline", "1");
	}

	@Test
	public void testSetShadow() {
		// null by default
		checkAttributeValue(font, "Shadow", null);
		
		// 0 if false
		font.setShadow(false);
		checkAttributeValue(font, "Shadow", "0");
		
		// 1 if true
		font.setShadow(true);
		checkAttributeValue(font, "Shadow", "1");
	}

	@Test
	public void testSetSize() {
		// null by default
		checkAttributeValue(font, "Size", null);
		
		try {
			// Must fail
			font.setSize(-1);
			fail();
		}
		catch(IllegalArgumentException e) {
			Assert.assertNotNull(e);
		}
		
		// 12 points
		font.setSize(12);
		checkAttributeValue(font, "Size", "12");
		
		// 13 points
		font.setSize(13);
		checkAttributeValue(font, "Size", "13");
		
		// 14 points
		font.setSize(14);
		checkAttributeValue(font, "Size", "14");
	}

	@Test
	public void testSetStrikeThrough() {
		// null by default
		checkAttributeValue(font, "StrikeThrough", null);
		
		// 0 if false
		font.setStrikeThrough(false);
		checkAttributeValue(font, "StrikeThrough", "0");
		
		// 1 if true
		font.setStrikeThrough(true);
		checkAttributeValue(font, "StrikeThrough", "1");
	}

	@Test
	public void testSetUnderline() {
		// null by default
		checkAttributeValue(font, "Underline", null);
		
		// Try all alternatives
		for (Underline underline: Underline.values()) {
			font.setUnderline(underline);
			checkAttributeValue(font, "Underline", underline.toString());
		}
	}

	@Test
	public void testSetVerticalAlign() {
		// null by default
		checkAttributeValue(font, "VerticalAlign", null);
		
		// check all alternatives
		for (VerticalAlignment alignment: VerticalAlignment.values()) {
			font.setVerticalAlign(alignment);
			checkAttributeValue(font, "VerticalAlign", alignment.toString());
		}
	}

	@Test
	public void testSetCharSet() {
		// null by default
		checkAttributeValue("x", font, "CharSet", null);
		
		// try some values
		font.setCharSet(Charset.ANSI_Latin.getValue());
		checkAttributeValue("x", font, "CharSet",
			Integer.toString(Charset.ANSI_Latin.getValue()));
		
		font.setCharSet(Charset.ANSI_Arabic.getValue());
		checkAttributeValue("x", font, "CharSet",
			Integer.toString(Charset.ANSI_Arabic.getValue()));
		
		font.setCharSet(Charset.Apple_Roman.getValue());
		checkAttributeValue("x", font, "CharSet",
			Integer.toString(Charset.Apple_Roman.getValue()));
	}

	@Test
	public void testSetFamily() {
		// null by default
		checkAttributeValue("x", font, "Family", null);
		
		// try all the possible values
		for (FontFamily family: FontFamily.values()) {
			font.setFamily(family);
			checkAttributeValue("x", font, "Family",
				family.toString());
		}
	}

}
