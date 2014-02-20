/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Element Border inside Borders.<br/>
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:border
 */
public class Border {

	/** Automatic value for border color. */
	public static final String COLOR_AUTOMATIC = "Automatic";
	
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
	
	
}
