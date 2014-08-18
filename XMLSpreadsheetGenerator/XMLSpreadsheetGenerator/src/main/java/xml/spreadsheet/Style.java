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
import xml.spreadsheet.utils.Table;
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
	
	/** Name.  This will be used by the software to identify named styles and
	 * offer them to the user in some dialogs. */
	private String name = "";
	
	/** Parent */
	private String parent = null;
	
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
	
	/**
	 * Standalone style
	 * @param id Current style id
	 */
	Style(String id) {
		this.id = id;
	}
	
	/**
	 * Standalone, named style
	 * @param id Current style id
	 * @param name Style name
	 */
	Style(String id, String name) {
		this(id);
		this.name = name;
	}
	
	/**
	 * Style built on another available style
	 * @param id Current style id
	 * @param parent Parent style
	 */
	Style(String id, Style parent) {
		this(id, null, parent);
	}
	
	/**
	 * Style built on another available style
	 * @param id Current style id
	 * @param name Style name
	 * @param parent Parent style
	 */
	Style(String id, String name, Style parent) {
		this(id, name);
		if (parent != null) {
			this.parent = parent.getId();
			// Since available software won't implement this, the solution we
			//	will take is this:
			// The official documentation defines style inheritance, and makes
			//	clear that all the styles can be redefined in the child style.
			//	Since this is possible, we will simply copy the parent style
			//	attributes to the child, and let it redefine them later if they
			//	fell like it.  This way	we have a correct implementation of the style 
			//	inheritance contract (just by manual means under the hood)
			
			// Copy all the attributes
			if (parent.alignment != null) {
				this.alignment = new Alignment(parent.alignment);
			}
			if (parent.borders != null) {
				this.borders = new Borders(parent.borders);
			}
			if (parent.font != null) {
				this.font = new Font(parent.font); 
			}
			if (parent.interior != null) {
				this.interior = new Interior(parent.interior);
			}
			if (parent.numberFormat != null) {
				this.numberFormat = new NumberFormat(parent.numberFormat);
			}
			if (parent.protection != null) {
				this.protection = new Protection(parent.protection);
			}
		}
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
		
		return XmlHelper.element("ss:Style", 
			new Table<Object>().
				add("ss:ID", id).
				add("ss:Name", name).
				add("ss:Parent", parent),
			// Compose every inner style element XML representation inside 
			//	the Style element
			new StringBuilder()
				.append(alignment != null?alignment.toString():"")
				.append(borders != null?borders.toString():"")
				.append(font != null?font.toString():"")
				.append(interior != null?interior.toString():"")
				.append(numberFormat != null?numberFormat.toString():"")
				.append(protection != null?protection.toString():"").toString()
			);
	}

	/**
	 * @return Style ID to be used as reference when building the XML
	 */
	public String getId() {
		return id;
	}
	
}
