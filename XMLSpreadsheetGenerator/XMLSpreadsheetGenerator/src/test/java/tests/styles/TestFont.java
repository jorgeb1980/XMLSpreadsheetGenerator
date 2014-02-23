package tests.styles;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xml.spreadsheet.Charset;
import xml.spreadsheet.Style;
import xml.spreadsheet.XMLSpreadsheetGenerator;
import xml.spreadsheet.style.Borders;
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
			XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(null);
			Style style = generator.createStyle();
			
			font = style.font();
			Assert.assertNotNull(font);	
		}
		catch (Throwable e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetBold() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Bold", null);
		
		// 0 if false
		font.setBold(false);
		StyleTestUtils.checkAttributeValue(font, "Bold", "0");
		
		// 1 if true
		font.setBold(true);
		StyleTestUtils.checkAttributeValue(font, "Bold", "1");
		
	}

	@Test
	public void testSetColor() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Color", null);
		
		// Check different values
		
		font.setColor(Font.COLOR_AUTOMATIC);
		StyleTestUtils.checkAttributeValue(font, "Color", Font.COLOR_AUTOMATIC);
		
		// dark red
		font.setColor("#C14949");
		StyleTestUtils.checkAttributeValue(font, "Color", "#C14949");
		
		// dark blue
		font.setColor("#1B1F97");
		StyleTestUtils.checkAttributeValue(font, "Color", "#1B1F97");
		
		// dark green
		font.setColor("#096F27");
		StyleTestUtils.checkAttributeValue(font, "Color", "#096F27");
		
	}

	@Test
	public void testSetFontName() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "FontName", null);
		
		// Arial
		font.setFontName("Arial");
		StyleTestUtils.checkAttributeValue(font, "FontName", "Arial");
		
		// Arial
		font.setFontName("Verdana");
		StyleTestUtils.checkAttributeValue(font, "FontName", "Verdana");
	}

	@Test
	public void testSetItalic() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Italic", null);
		
		// 0 if false
		font.setItalic(false);
		StyleTestUtils.checkAttributeValue(font, "Italic", "0");
		
		// 1 if true
		font.setItalic(true);
		StyleTestUtils.checkAttributeValue(font, "Italic", "1");
	}

	@Test
	public void testSetOutline() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Outline", null);
		
		// 0 if false
		font.setOutline(false);
		StyleTestUtils.checkAttributeValue(font, "Outline", "0");
		
		// 1 if true
		font.setOutline(true);
		StyleTestUtils.checkAttributeValue(font, "Outline", "1");
	}

	@Test
	public void testSetShadow() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Shadow", null);
		
		// 0 if false
		font.setShadow(false);
		StyleTestUtils.checkAttributeValue(font, "Shadow", "0");
		
		// 1 if true
		font.setShadow(true);
		StyleTestUtils.checkAttributeValue(font, "Shadow", "1");
	}

	@Test
	public void testSetSize() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Size", null);
		
		try {
			// Must fail
			font.setSize(-1);
			fail();
		}
		catch(Exception e) {
			Assert.assertNotNull(e);
		}
		
		// 12 points
		font.setSize(12);
		StyleTestUtils.checkAttributeValue(font, "Size", "12");
		
		// 13 points
		font.setSize(13);
		StyleTestUtils.checkAttributeValue(font, "Size", "13");
		
		// 14 points
		font.setSize(14);
		StyleTestUtils.checkAttributeValue(font, "Size", "14");
	}

	@Test
	public void testSetStrikeThrough() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "StrikeThrough", null);
		
		// 0 if false
		font.setStrikeThrough(false);
		StyleTestUtils.checkAttributeValue(font, "StrikeThrough", "0");
		
		// 1 if true
		font.setStrikeThrough(true);
		StyleTestUtils.checkAttributeValue(font, "StrikeThrough", "1");
	}

	@Test
	public void testSetUnderline() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "Underline", null);
		
		// Try all alternatives
		for (Underline underline: Underline.values()) {
			font.setUnderline(underline);
			StyleTestUtils.checkAttributeValue(font, "Underline", underline.toString());
		}
	}

	@Test
	public void testSetVerticalAlign() {
		// null by default
		StyleTestUtils.checkAttributeValue(font, "VerticalAlign", null);
		
		// check all alternatives
		for (VerticalAlignment alignment: VerticalAlignment.values()) {
			font.setVerticalAlign(alignment);
			StyleTestUtils.checkAttributeValue(font, "VerticalAlign", alignment.toString());
		}
	}

	@Test
	public void testSetCharSet() {
		// null by default
		StyleTestUtils.checkAttributeValue("x", font, "CharSet", null);
		
		// try some values
		font.setCharSet(Charset.ANSI_Latin.getValue());
		StyleTestUtils.checkAttributeValue("x", font, "CharSet", 
			Integer.toString(Charset.ANSI_Latin.getValue()));
		
		font.setCharSet(Charset.ANSI_Arabic.getValue());
		StyleTestUtils.checkAttributeValue("x", font, "CharSet", 
			Integer.toString(Charset.ANSI_Arabic.getValue()));
		
		font.setCharSet(Charset.Apple_Roman.getValue());
		StyleTestUtils.checkAttributeValue("x", font, "CharSet", 
			Integer.toString(Charset.Apple_Roman.getValue()));
		
	}

	@Test
	public void testSetFamily() {
		// null by default
		StyleTestUtils.checkAttributeValue("x", font, "Family", null);
		
		// try all the possible values
		for (FontFamily family: FontFamily.values()) {
			font.setFamily(family);
			StyleTestUtils.checkAttributeValue("x", font, "Family", 
				family.toString());
		}
	}

}
