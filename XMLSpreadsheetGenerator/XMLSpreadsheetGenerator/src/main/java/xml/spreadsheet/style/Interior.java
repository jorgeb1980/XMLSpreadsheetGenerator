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
	private String color = null;
	
	/**
	 * Specifies the fill pattern in the cell. The fill pattern determines 
	 * how to blend the Color and PatternColor attributes to produce the 
	 * cell's appearance. 
	 */
	private FillPattern pattern = null;
	
	/**
	 * Specifies the secondary fill color of the cell when 
	 * Pattern does not equal Solid.
	 */
	private String patternColor = null;
	
	//-------------------------------------------------------------------
	// Class methods
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<ss:Interior");
		
		AttributeHelper.att(sb, "ss:Color", color);
		
		if (pattern != null) {
			AttributeHelper.att(sb, "ss:Pattern", pattern.toString());
		}
		
		AttributeHelper.att(sb, "ss:PatternColor", patternColor);
		
		sb.append("/>");
		
		return sb.toString();
	}

	public Interior () {}
	
	/**
	 * @param color Specifies the fill color of the cell. This value can be a 
	 * 6-hexadecimal digit number in "#rrggbb" format among other things.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @param pattern Specifies the fill pattern in the cell. The fill pattern determines 
	 * how to blend the Color and PatternColor attributes to produce the 
	 * cell's appearance. 
	 */
	public void setPattern(FillPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param patternColor Specifies the secondary fill color of the cell when 
	 * Pattern does not equal Solid.
	 */
	public void setPatternColor(String patternColor) {
		this.patternColor = patternColor;
	}
	
	
}
