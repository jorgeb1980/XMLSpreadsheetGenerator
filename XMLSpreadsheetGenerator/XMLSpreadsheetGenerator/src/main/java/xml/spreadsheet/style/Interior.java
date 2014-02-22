/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Defines the fill properties to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:interior
 */
public class Interior {

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
	
	//-------------------------------------------------------------------
	// Class properties
	
	/**
	 * Specifies the fill color of the cell. This value can be a 
	 * 6-hexadecimal digit number in "#rrggbb" format among other things.  
	 * This string is case insensitive.
	 */
	private String color = COLOR_AUTOMATIC;
	
	/**
	 * Specifies the fill pattern in the cell. The fill pattern determines 
	 * how to blend the Color and PatternColor attributes to produce the 
	 * cell's appearance. 
	 */
	private FillPattern pattern = FillPattern.None;
	
	/**
	 * Specifies the secondary fill color of the cell when 
	 * Pattern does not equal Solid.
	 */
	private String patternColor = COLOR_AUTOMATIC;
	
	//-------------------------------------------------------------------
	// Class methods

	public Interior () {}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(FillPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param patternColor the patternColor to set
	 */
	public void setPatternColor(String patternColor) {
		this.patternColor = patternColor;
	}
	
	@Override
	public String toString() {
		// TODO: XML rendering of the borders
		return "";
	}
	
	
}
