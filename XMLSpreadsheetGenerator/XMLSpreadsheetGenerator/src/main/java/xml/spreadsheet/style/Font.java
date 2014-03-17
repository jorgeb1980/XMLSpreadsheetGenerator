/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.utils.Table;
import xml.spreadsheet.utils.XmlHelper;

/**
 * Defines the font attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:font
 */
public class Font {

	//-----------------------------------------------------------
	// Subtypes and constants
	
	/** Automatic value for font color. */
	public static final String COLOR_AUTOMATIC = "Automatic";
	/** Default value for font. */
	public static final String FONT_ARIAL = "Arial";
	
	/** Underline state of a font */
	public enum Underline {
		None, 
		Single, 
		Double, 
		SingleAccounting, 
		DoubleAccounting;
		
		// No need to override toString, it is enough as is
	}
	
	/** Vertical alignment of a font */
	public enum VerticalAlignment {
		None, 
		Subscript, 
		Superscript;
		
		// No need to override toString, it is enough as is
	}
	
	/** Win-32 font family */
	public enum FontFamily {
		Automatic, 
		Decorative, 
		Modern, 
		Roman, 
		Script, 
		Swiss;
		
		// No need to override toString, it is enough as is
	}
	
	
	
	//-------------------------------------------------------------------
	// Class properties
	
	// Attributes
	
	/** Specifies the bold state of the font */
	private Boolean bold = null;
	
	/** Specifies the color of the font. This value can be either a 6-hexadecimal digit number in "#rrggbb" format
	 * among other things.  This string can also be special value of Automatic. This string is case insensitive. 
	 */
	private String color = null; 
	
	/** Specifies the name of the font. This string is case insensitive. */
	private String fontName = null; 
	
	/** Specifies the italic state of the font */
	private Boolean italic = null;
	
	/** Specifies whether the font is rendered as an outline. 
	 * This property originates in Macintosh Office, and is not used on Windows.
	 */
	private Boolean outline = null;
	
	/**
	 * This attribute specifies whether the font is shadowed. This property 
	 * originates in Macintosh Office, and is not used on Windows 
	 */
	private Boolean shadow = null;
	
	/**
	 * Specifies the size of the font in points. 
	 * This value must be strictly greater than 0
	 */
	private Double size = null;
	
	/**
	 *  This attribute specifies the strike-through state of the font. 
	 *  If this attribute is not specified within an element, the default is assumed
	 */
	private Boolean strikeThrough = null;
	
	/**
	 * Specifies the underline state of the font.
	 */
	private Underline underline = null; 
	
	/**
	 * This attribute specifies the subscript or superscript state of the font
	 */
	private VerticalAlignment verticalAlign = null;
	
	/**
	 * Win32-dependent character set value. 
	 */
	private Long charSet = null;
	
	/**
	 * Win32-dependent font family. 
	 */
	private FontFamily family = null;
	
	
	//-------------------------------------------------------------------
	// Class methods
	
	
	@Override
	public String toString() {		
		return XmlHelper.emptyElement("ss:Font",
			new Table<Object>().
				add("ss:Bold", bold != null?bold.booleanValue():null).
				add("ss:Color", color).
				add("ss:FontName", fontName).
				add("ss:Italic", italic).
				add("ss:Outline", outline).
				add("ss:Shadow", shadow).
				add("ss:Size", size).
				add("ss:StrikeThrough", strikeThrough).				
				add("ss:Underline", underline != null?underline.toString():null).		
				add("ss:VerticalAlign", verticalAlign != null?verticalAlign.toString():null).		
				add("x:CharSet", charSet).		
				add("x:Family", family != null?family.toString():null));
	}
	
	public Font() {}
	
	/**
	 * @param bold Specifies the bold state of the font. If the parent 
	 * style has Bold="1" and the child style wants to override the setting, 
	 * it must explicitly set the value to Bold="0"
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	/**
	 * @param color Specifies the color of the font. This value can be 
	 * either a 6-hexadecimal digit number in "#rrggbb" format among other things
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @param fontName Specifies the name of the font. 
	 * This string is case insensitive.
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	/**
	 * @param italic Specifies the italic state of the font
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	/**
	 * @param outline Specifies whether the font is rendered as an outline.
	 *  This property originates in Macintosh Office, and is not used on Windows.
	 */
	public void setOutline(boolean outline) {
		this.outline = outline;
	}

	/**
	 * @param shadow Specifies whether the font is shadowed. This property 
	 * originates in Macintosh Office, and is not used on Windows
	 */
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}

	/**
	 * @param size Specifies the size of the font in points. 
	 * This value must be strictly greater than 0.
	 */
	public void setSize(double size) {
		if (size <= 0.0) {
			throw new IllegalArgumentException(
				"The font size must be greater than zero [" + size + "]");
		}
		this.size = size;
	}

	/**
	 * @param strikeThrough Specifies the strike-through state of the font.
	 */
	public void setStrikeThrough(boolean strikeThrough) {
		this.strikeThrough = strikeThrough;
	}

	/**
	 * @param underline Specifies the underline state of the font.
	 */
	public void setUnderline(Underline underline) {
		this.underline = underline;
	}

	/**
	 * @param verticalAlign This attribute specifies the subscript or 
	 * superscript state of the font.
	 */
	public void setVerticalAlign(VerticalAlignment verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	/**
	 * @param charSet Win32-dependent character set value. 
	 */
	public void setCharSet(long charSet) {
		this.charSet = charSet;
	}

	/**
	 * @param family Win32-dependent font family. 
	 */
	public void setFamily(FontFamily family) {
		this.family = family;
	}
	
}
