package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines the fill properties to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:interior">MSDN Interior element reference</a>
 */
public class Interior {

	//-------------------------------------------------------------------
	// Subtypes and constants
	
	/** Automatic color for cell interior. */
	public static final String COLOR_AUTOMATIC = "Automatic";
	
	/** Fill pattern for the cell interior color. */
	private enum FillPattern {
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
	
	//-------------------------------------------------------------------
	// Class methods
	
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

	/** Default constructor. */
	public Interior () {}
	
	/**
	 * Copy constructor.
	 * @param interior Original interior to copy
	 */
	public Interior(Interior interior) {
		this.color = interior.color;
		this.pattern = interior.pattern;
	}
	
	/**
	 * @param color Specifies the fill color of the cell. This value can be a 
	 * 6-hexadecimal digit number in "#rrggbb" format among other things.
	 */
	public void setColor(String color) {
		this.color = color;
		// If the fill pattern is unspecified, use a solid fill pattern
		if (this.pattern == null) {
			this.pattern = FillPattern.Solid;
		}
	}
	
	
}
