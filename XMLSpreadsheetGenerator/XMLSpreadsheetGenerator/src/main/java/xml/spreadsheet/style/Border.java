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
	
	// Border predefined weights
	/** 0—Hairline */
	public static final double BORDER_HAIRLINE = 0;
	/** 1—Thin */
	public static final double BORDER_THIN = 1;
	/** 2—Medium */
	public static final double BORDER_MEDIUM = 2;
	/** 3—Thick */
	public static final double BORDER_THICK = 3;
	
	/** Position of the border */
	public enum BorderPosition {
		Left, Top, Right, Bottom, DiagonalLeft, DiagonalRight
	}
	
	/** Line style. */
	public enum LineStyle {
		None, Continuous, Dash, Dot, DashDot, DashDotDot, SlantDashDot, Double;
	}
	
	//-----------------------------------------------------------
	// Class properties

	// Attributes
	/** Position of the border. */
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
	private double weight = BORDER_HAIRLINE;
	
	//-----------------------------------------------------------
	// Class methods
	
	// Standard visibility constructor - not intended to be used out of
	//	the package
	Border(BorderPosition position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public BorderPosition getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(BorderPosition position) {
		this.position = position;
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
	 * @return the lineStyle
	 */
	public LineStyle getLineStyle() {
		return lineStyle;
	}

	/**
	 * @param lineStyle the lineStyle to set
	 */
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		// TODO: XML rendering of the border
		return "";
	}
}
