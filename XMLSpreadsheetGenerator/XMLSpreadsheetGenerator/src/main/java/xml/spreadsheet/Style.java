/**
 * 
 */
package xml.spreadsheet;

import xml.spreadsheet.style.Alignment;
import xml.spreadsheet.style.Borders;
import xml.spreadsheet.style.Font;
import xml.spreadsheet.style.Interior;
import xml.spreadsheet.style.NumberFormat;
import xml.spreadsheet.style.Protection;
import xml.spreadsheet.utils.XmlHelper;

/**
 * This class contains the information for a style definition in an XML
 * spreadsheet.  It can only be built attached to a certain <code>XMLSpreadsheetGenerator</code> by calling <code>createStyle</code> 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:style">MSDN Style element reference</a>
 * @see xml.spreadsheet.XMLSpreadsheetGenerator#createStyle()
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
	
	
	Style(String id) {
		this.id = id;
	}
	
	/** Creates an Alignment element */
	public Alignment alignment() {
		if (alignment == null) {
			alignment = new Alignment();
		}
		return  alignment;
	}
	
	/** Creates a Borders element */
	public Borders borders() {
		if (borders == null) {
			borders = new Borders();
		}
		return borders;
	}
	
	/** Creates a Font element */
	public Font font() {
		if (font == null) {
			font = new Font();
		}
		return font;
	}
	
	/** Creates an Interior element */
	public Interior interior() {
		if (interior == null) {
			interior = new Interior();
		}
		return interior;
	}
	
	/** Creates a NumberFormat element */
	public NumberFormat numberFormat() {
		if (numberFormat == null) {
			numberFormat = new NumberFormat();
		}
		return numberFormat;
		
	}
	
	/** Creates a Protection element */
	public Protection protection() {
		if (protection == null) {
			protection = new Protection();
		}
		return protection;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<ss:Style");
		
		XmlHelper.att(sb, "ID", id);
		XmlHelper.att(sb, "Name", name);
		XmlHelper.att(sb, "Parent", parent);
		
		sb.append(">");
		
		// Flush the inner elements
		sb.append(alignment != null?alignment.toString():"");
		sb.append(borders != null?borders.toString():"");
		sb.append(font != null?font.toString():"");
		sb.append(interior != null?interior.toString():"");
		sb.append(numberFormat != null?numberFormat.toString():"");
		sb.append(protection != null?protection.toString():"");
		
		sb.append("</ss:Style>");
		
		return sb.toString();
	}

	/**
	 * @return Style ID to be used as reference when building the XML
	 */
	public String getId() {
		return id;
	}
	
}
