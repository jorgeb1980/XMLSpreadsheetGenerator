package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines the fill properties to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)#ssinterior-tag">MSDN Interior element reference</a>
 */
public record Interior(
	/*
	 * Specifies the fill color of the cell. This value can be a
	 * 6-hexadecimal digit number in "#rrggbb" format among other things.
	 * This string is case insensitive.
	 */
	String color,
	/*
	 * Specifies the fill pattern in the cell. The fill pattern determines
	 * how to blend the Color and PatternColor attributes to produce the
	 * cell's appearance.
	 */
	FillPattern pattern
) {

	//-------------------------------------------------------------------
	// Subtypes and constants
	
	/** Automatic color for cell interior. */
	public static final String COLOR_AUTOMATIC = "Automatic";
	
	/** Fill pattern for the cell interior color. */
	public enum FillPattern {
		None, Solid 
		// LibreOffice does not support these patterns... not exactly sure what
		//	to do with them
		,Gray75, Gray50, Gray25, Gray125, Gray0625, HorzStripe, 
		VertStripe, ReverseDiagStripe, DiagStripe, DiagCross, ThickDiagCross, 
		ThinHorzStripe, ThinVertStripe, ThinReverseDiagStripe, ThinDiagStripe, 
		ThinHorzCross, ThinDiagCross
	}
	
	@Override
	public String toString() {
		return element(
			"ss:Interior",
			mapOf(
				"ss:Color", color,
				"ss:Pattern", pattern != null ? pattern.toString() : null
			)
		);
	}
	
	/**
	 * Copy constructor.
	 * @param interior Original interior to copy
	 */
	public static Interior from(Interior interior) {
		return interior != null ?
			new Interior(
				interior.color,
				interior.pattern
			) : null;
	}

	public static InteriorBuilder builder() { return new InteriorBuilder(); }

	public static class InteriorBuilder {
		private String color;
		// If the fill pattern is unspecified, use a solid fill pattern
		private FillPattern pattern = FillPattern.Solid;

		public Interior build() {
			return new Interior(color, pattern);
		}

		/**
		 * @param color Specifies the fill color of the cell. This value can be a
		 * 6-hexadecimal digit number in "#rrggbb" format among other things.
		 */
		public InteriorBuilder withColor(String color) {
			this.color = color;
			return this;
		}

		public InteriorBuilder withPattern(FillPattern pattern) {
			this.pattern = pattern;
			return this;
		}
	}


	
	
}
