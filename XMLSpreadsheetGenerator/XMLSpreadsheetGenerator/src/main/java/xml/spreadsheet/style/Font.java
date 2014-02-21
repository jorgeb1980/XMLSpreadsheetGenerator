/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Defines the font attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_font
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
	private boolean bold = false;
	
	/** Specifies the color of the font. This value can be either a 6-hexadecimal digit number in "#rrggbb" format
	 * among other things.  This string can also be special value of Automatic. This string is case insensitive. 
	 */
	private String color = COLOR_AUTOMATIC; 
	
	/** Specifies the name of the font. This string is case insensitive. */
	private String fontName = FONT_ARIAL; 
	
	/** Specifies the italic state of the font */
	private boolean italic = false;
	
	/** Specifies whether the font is rendered as an outline. 
	 * This property originates in Macintosh Office, and is not used on Windows.
	 */
	private boolean outline = false;
	
	/**
	 * This attribute specifies whether the font is shadowed. This property 
	 * originates in Macintosh Office, and is not used on Windows 
	 */
	private boolean shadow = false;
	
	/**
	 * Specifies the size of the font in points. 
	 * This value must be strictly greater than 0
	 */
	private double size = 10;
	
	/**
	 *  This attribute specifies the strike-through state of the font. 
	 *  If this attribute is not specified within an element, the default is assumed
	 */
	private boolean strikeThrough = false;
	
	/**
	 * Specifies the underline state of the font.
	 */
	private Underline underline; 
	
	/**
	 * This attribute specifies the subscript or superscript state of the font
	 */
	private VerticalAlignment verticalAlign = VerticalAlignment.None;
	
	/**
	 * Win32-dependent character set value. 
	 */
	private long charSet = 0;
	
	/**
	 * Win32-dependent font family. 
	 */
	private FontFamily family = FontFamily.Automatic;
	
	
	//-------------------------------------------------------------------
	// Class methods
	
	/**
	 * @return the bold
	 */
	public boolean isBold() {
		return bold;
	}
	/**
	 * @param bold the bold to set
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the fontName
	 */
	public String getFontName() {
		return fontName;
	}
	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	/**
	 * @return the italic
	 */
	public boolean isItalic() {
		return italic;
	}
	/**
	 * @param italic the italic to set
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	/**
	 * @return the outline
	 */
	public boolean isOutline() {
		return outline;
	}
	/**
	 * @param outline the outline to set
	 */
	public void setOutline(boolean outline) {
		this.outline = outline;
	}
	/**
	 * @return the shadow
	 */
	public boolean isShadow() {
		return shadow;
	}
	/**
	 * @param shadow the shadow to set
	 */
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}
	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}
	/**
	 * @return the strikeThrough
	 */
	public boolean isStrikeThrough() {
		return strikeThrough;
	}
	/**
	 * @param strikeThrough the strikeThrough to set
	 */
	public void setStrikeThrough(boolean strikeThrough) {
		this.strikeThrough = strikeThrough;
	}
	/**
	 * @return the underline
	 */
	public Underline getUnderline() {
		return underline;
	}
	/**
	 * @param underline the underline to set
	 */
	public void setUnderline(Underline underline) {
		this.underline = underline;
	}
	/**
	 * @return the verticalAlign
	 */
	public VerticalAlignment getVerticalAlign() {
		return verticalAlign;
	}
	/**
	 * @param verticalAlign the verticalAlign to set
	 */
	public void setVerticalAlign(VerticalAlignment verticalAlign) {
		this.verticalAlign = verticalAlign;
	}
	/**
	 * @return the charSet
	 */
	public long getCharSet() {
		return charSet;
	}
	/**
	 * @param charSet the charSet to set
	 */
	public void setCharSet(long charSet) {
		this.charSet = charSet;
	}
	/**
	 * @return the family
	 */
	public FontFamily getFamily() {
		return family;
	}
	/**
	 * @param family the family to set
	 */
	public void setFamily(FontFamily family) {
		this.family = family;
	}
	
	@Override
	public String toString() {
		// TODO: XML rendering of the borders
		return "";
	}
	
	
}
