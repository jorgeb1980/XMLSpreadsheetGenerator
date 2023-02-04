package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines the font attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:font">MSDN Font element reference</a>
 */
public record Font(
	/* Specifies the bold state of the font */
	Boolean bold,
	/*
	 * Specifies the color of the font. This value can be either a 6-hexadecimal digit number in "#rrggbb" format
	 * among other things.  This string can also be special value of Automatic. This string is case insensitive.
	 */
	String color,
	/* Specifies the name of the font. This string is case insensitive. */
	String fontName,
	/* Specifies the italic state of the font */
	Boolean italic,
	/*
	 * Specifies whether the font is rendered as an outline.
	 * This property originates in Macintosh Office, and is not used on Windows.
	 */
	Boolean outline,
	/*
	 * This attribute specifies whether the font is shadowed. This property
	 * originates in Macintosh Office, and is not used on Windows
	 */
	Boolean shadow,
	/*
	 * Specifies the size of the font in points.
	 * This value must be strictly greater than 0
	 */
	Double size,
	/*
	 * This attribute specifies the strike-through state of the font.
	 * If this attribute is not specified within an element, the default is assumed
	 */
	Boolean strikeThrough,
	/*
	 * Specifies the underline state of the font.
	 */
	Underline underline,
	/*
	 * This attribute specifies the subscript or superscript state of the font
	 */
	VerticalAlignment verticalAlign,
	/*
	 * Win32-dependent character set value.
	 */
	Long charSet,
	/*
	 * Win32-dependent font family.
	 */
	FontFamily family
) {

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
	}
	
	/** Vertical alignment of a font */
	public enum VerticalAlignment {
		None, 
		Subscript, 
		Superscript;
	}
	
	/** Win-32 font family */
	public enum FontFamily {
		Automatic, 
		Decorative, 
		Modern, 
		Roman, 
		Script, 
		Swiss;
	}
	
	//-------------------------------------------------------------------
	// Class methods

	@Override
	public String toString() {
		return element(
			"ss:Font",
			mapOf(
				"ss:Bold", bold,
				"ss:Color", color,
				"ss:FontName", fontName,
				"ss:Italic", italic,
				"ss:Outline", outline,
				"ss:Shadow", shadow,
				"ss:Size", size,
				"ss:StrikeThrough", strikeThrough,
				"ss:Underline", underline != null ? underline.toString() : null,
				"ss:VerticalAlign", verticalAlign != null ? verticalAlign.toString() : null,
				"x:CharSet", charSet,
				"x:Family", family != null ? family.toString() : null
			)
		);
	}
	
	/**
	 * Copy constructor.
	 * @param font Original font to copy
	 */
	public static Font from(Font font) {
		return font != null?
			new Font(
				font.bold,
				font.color,
				font.fontName,
				font.italic,
				font.outline,
				font.shadow,
				font.size,
				font.strikeThrough,
				font.underline,
				font.verticalAlign,
				font.charSet,
				font.family
			) : null;
	}

	public static FontBuilder builder() { return new FontBuilder(); }

	public static class FontBuilder {

		private Boolean bold;
		private String color;
		private String fontName;
		private Boolean italic;
		private Boolean outline;
		private Boolean shadow;
		private Double size;
		private Boolean strikeThrough;
		private Underline underline;
		private VerticalAlignment verticalAlign;
		private Long charSet;
		private FontFamily family;

		public Font build() {
			return new Font(
				bold,
				color,
				fontName,
				italic,
				outline,
				shadow,
				size,
				strikeThrough,
				underline,
				verticalAlign,
				charSet,
				family
			);
		}

		/**
		 * @param bold Specifies the bold state of the font. If the parent
		 *             style has Bold="1" and the child style wants to override the setting,
		 *             it must explicitly set the value to Bold="0"
		 */
		public FontBuilder withBold(boolean bold) {
			this.bold = bold;
			return this;
		}

		/**
		 * @param color Specifies the color of the font. This value can be
		 *              either a 6-hexadecimal digit number in "#rrggbb" format among other things
		 */
		public FontBuilder withColor(String color) {
			this.color = color;
			return this;
		}

		/**
		 * @param fontName Specifies the name of the font.
		 *                 This string is case insensitive.
		 */
		public FontBuilder withFontName(String fontName) {
			this.fontName = fontName;
			return this;
			
		}

		/**
		 * @param italic Specifies the italic state of the font
		 */
		public FontBuilder withItalic(boolean italic) {
			this.italic = italic;
			return this;
			
		}

		/**
		 * @param outline Specifies whether the font is rendered as an outline.
		 *                This property originates in Macintosh Office, and is not used on Windows.
		 */
		public FontBuilder withOutline(boolean outline) {
			this.outline = outline;
			return this;
			
		}

		/**
		 * @param shadow Specifies whether the font is shadowed. This property
		 *               originates in Macintosh Office, and is not used on Windows
		 */
		public FontBuilder withShadow(boolean shadow) {
			this.shadow = shadow;
			return this;
		}

		/**
		 * @param size Specifies the size of the font in points.
		 *             This value must be strictly greater than 0.
		 */
		public FontBuilder withSize(double size) {
			if (size <= 0.0) {
				throw new IllegalArgumentException(
					"The font size must be greater than zero [" + size + "]");
			}
			this.size = size;
			return this;
		}

		/**
		 * @param strikeThrough Specifies the strike-through state of the font.
		 */
		public FontBuilder withStrikeThrough(boolean strikeThrough) {
			this.strikeThrough = strikeThrough;
			return this;
		}

		/**
		 * @param underline Specifies the underline state of the font.
		 */
		public FontBuilder withUnderline(Underline underline) {
			this.underline = underline;
			return this;
		}

		/**
		 * @param verticalAlign This attribute specifies the subscript or
		 *                      superscript state of the font.
		 */
		public FontBuilder withVerticalAlign(VerticalAlignment verticalAlign) {
			this.verticalAlign = verticalAlign;
			return this;
		}

		/**
		 * @param charSet Win32-dependent character set value.
		 */
		public FontBuilder withCharSet(long charSet) {
			this.charSet = charSet;
			return this;
		}

		/**
		 * @param family Win32-dependent font family.
		 */
		public FontBuilder withFamily(FontFamily family) {
			this.family = family;
			return this;
		}

	}
	
}
