/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Defines a single border within this style's Borders collection. The Borders 
 * collection may contain up to six unique Border elements. If duplicate Border 
 * elements exist, the behavior is unspecified and the XML Spreadsheet document is considered invalid. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:border
 */
public class Border {

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
		
		private double weight;
		
		private BorderWeight (double weight) {
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
	
	//-----------------------------------------------------------
	// Class properties

	// Attributes
	/** Specifies which of the six possible borders this element represents. 
	 * Duplicate borders are not permitted and are considered invalid. */
	private BorderPosition position;
	
	/** 
	 * Specifies the color of this border. This value can be, among other things,
	 * a 6-hexadecimal digit number in "#rrggbb" format.  
	 * This string can also be the special 
	 * value of "Automatic." This string is case insensitive. 
	 */
	private String color = COLOR_AUTOMATIC;
	
	/** Specifies the appearance of this border. */
	private LineStyle lineStyle = LineStyle.None;
	
	/**
	 *  Specifies the weight (or thickness) of this border. This measurement 
	 *  is specified in points

			0—Hairline
			1—Thin
			2—Medium
			3—Thick 
	 */
	private double weight = BorderWeight.Hairline.getValue();
	
	//-----------------------------------------------------------
	// Class methods
		
	// Standard visibility constructor - not intended to be used out of
	//	the package
	Border(BorderPosition position) {
		this.position = position;
	}

	/**
	 * @param position Specifies which of the six possible borders this 
	 * element represents. Duplicate borders are not permitted and are considered invalid
	 */
	public void setPosition(BorderPosition position) {
		this.position = position;
	}

	/**
	 * @param color Specifies the color of this border. This value can be, among other things,
	 * a 6-hexadecimal digit number in "#rrggbb" format.  
	 * This string can also be the special 
	 * value of "Automatic." This string is case insensitive.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @param lineStyle Specifies the appearance of this border.
	 */
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * @param weight Specifies the weight (or thickness) of this border. 
	 * This measurement is specified in points, and the following values map to: <br/>
			0—Hairline
			<br/>
			1—Thin
			<br/>
			2—Medium
			<br/>
			3—Thick 
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * @param weight Specifies the weight (or thickness) of this border
	 */
	public void setWeight(BorderWeight weight) {
		this.weight = weight.getValue();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append("<ss:Border");
		AttributeHelper.att(sb, "ss:Position", position);
		
		if (color != null && !color.equals(COLOR_AUTOMATIC)) {
			AttributeHelper.att(sb, "ss:Color", color);
		}
		
		if (lineStyle != LineStyle.None) {
			AttributeHelper.att(sb, "ss:LineStyle", lineStyle);
		}
		
		if (weight != BorderWeight.Hairline.getValue()) {
			AttributeHelper.att(sb, "ss:Weight", weight);
		}
		
		sb.append("/>");
		
		return sb.toString();
	}
}
