/**
 * 
 */
package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines a single border within this style's Borders collection. The Borders 
 * collection may contain up to six unique Border elements. If duplicate Border 
 * elements exist, the behavior is unspecified and the XML Spreadsheet document is considered invalid. 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)#ssborder-tag">MSDN Border element reference</a>
 */
public record Border(
	/*
	 * Specifies which of the six possible borders this element represents.
	 * Duplicate borders are not permitted and are considered invalid.
	 */
	BorderPosition position,
	/*
	 * Specifies the color of this border. This value can be, among other things,
	 * a 6-hexadecimal digit number in "#rrggbb" format.
	 * This string can also be the special
	 * value of "Automatic." This string is case insensitive.
	 */
	String color,
	/* Specifies the appearance of this border. */
	LineStyle lineStyle,
	/*
	 *  Specifies the weight (or thickness) of this border. This measurement
	 *  is specified in points
		 0-Hairline
		 1-Thin
		 2-Medium
		 3-Thick
	 */
	Double weight
) {

	//-----------------------------------------------------------
	// Subtypes and constants
	
	/** Automatic value for border color. */
	public static final String COLOR_AUTOMATIC = "Automatic";
	
	/** Predefined border weights */
	public enum BorderWeight {
		Hairline (0),
		Thin (1),
		Medium (2),
		Thick (3);
		
		final private double weight;
		
		BorderWeight (double weight) {
			this.weight = weight;
		}
		
		public double getValue() {
			return weight;
		}
	}
	
	/** Position of the border */
	public enum BorderPosition {
		Left, 
		Top, 
		Right, 
		Bottom, 
		DiagonalLeft, 
		DiagonalRight;
		
		// No need to override toString, it is enough as is
	}
	
	/** Line style. */
	public enum LineStyle {
		None, 
		Continuous, 
		Dash, 
		Dot, 
		DashDot, 
		DashDotDot,
		SlantDashDot, 
		Double;
		
		// No need to override toString, it is enough as is
	}
	
	/**
	 * Copy constructor.
	 * @param border original border
	 */
	public static Border from(Border border) {
		return border != null ?
			new Border(
				border.position,
				border.color,
				border.lineStyle,
				border.weight
			) : null;
	}

	public static BorderBuilder builder() { return new BorderBuilder(); }

	public static class BorderBuilder {
		private BorderPosition position;
		private String color;
		private LineStyle lineStyle;
		private Double weight;

		private BorderBuilder() {}

		public Border build() {
			return new Border(
				position,
				color,
				lineStyle,
				weight
			);
		}

		/**
		 * @param color Specifies the color of this border. This value can be, among other things,
		 * a 6-hexadecimal digit number in "#rrggbb" format.
		 * This string can also be the special
		 * value of "Automatic." This string is case insensitive.
		 */
		public BorderBuilder withColor(String color) {
			this.color = color;
			// sets the line style if it is empty
			if (lineStyle == null) {
				lineStyle = LineStyle.Continuous;
			}
			return this;
		}

		/**
		 * @param lineStyle Specifies the appearance of this border.
		 */
		public BorderBuilder withLineStyle(LineStyle lineStyle) {
			this.lineStyle = lineStyle;
			return this;
		}

		/**
		 * @param weight Specifies the weight (or thickness) of this border.
		 * This measurement is specified in points, and the following values map to: <br/>
			0-Hairline
			<br/>
			1-Thin
			<br/>
			2-Medium
			<br/>
			3-Thick
		 */
		public BorderBuilder withWeight(double weight) {
			this.weight = weight;
			// sets the line style if it is empty
			if (lineStyle == null) {
				lineStyle = LineStyle.Continuous;
			}
			return this;
		}

		public BorderBuilder withPosition(BorderPosition position) {
			this.position = position;
			return this;
		}

		/**
		 * @param weight Specifies the weight (or thickness) of this border
		 */
		public BorderBuilder withWeight(BorderWeight weight) {
			return withWeight(weight.getValue());
		}
	}

	@Override
	public String toString() {		
		return element(
			"ss:Border",
			mapOf(
				"ss:Position", position != null ? position.toString() : null,
				"ss:Color", color,
				"ss:LineStyle", lineStyle != null ? lineStyle.toString() : null,
				"ss:Weight", weight
			)
		);
	}
}
