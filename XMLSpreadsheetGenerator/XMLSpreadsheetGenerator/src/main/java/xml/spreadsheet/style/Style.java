/**
 * 
 */
package xml.spreadsheet.style;

/**
 * This class contains the information for a style definition in an XML
 * spreadsheet.<br/>
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:style
 */
public class Style {

	//-------------------------------------------------
	// Properties of the style
	
	
	// Attributes
	/** Unique id */
	private String id;
	
	/** Name */
	private String name = "";
	
	/** Parent */
	private String parent = "";
	
	// Children
	
	/** Alignment */
	private Alignment alignment;
	
	/** Borders */
	private Borders borders;
	
	/** Font */
	private Font font;
	
	/** Interior */
	private Interior interior;
	
	/** NumberFormat */
	private NumberFormat numberFormat;
	
	/** Protection */
	private Protection protection;
	
	//-----------------------------------------------------------
	// Style methods
	
	
	/** Creates an Alignment element */
	public Alignment alignment() {
		alignment = new Alignment();
		return  alignment;
	}
	
	/** Creates a Borders element */
	public Borders borders() {
		borders = new Borders();
		return borders;
	}
	
	/** Creates a Font element */
	public Font font() {
		font = new Font();
		return font;
	}
	
	/** Creates an Interior element */
	public Interior interior() {
		interior = new Interior();
		return interior;
	}
	
	/** Creates a NumberFormat element */
	public NumberFormat numberFormat() {
		numberFormat = new NumberFormat();
		return numberFormat;
		
	}
	
	/** Creates a Protection element */
	public Protection protection() {
		protection = new Protection();
		return protection;
	}
	
}
