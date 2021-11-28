package xml.spreadsheet.style;

import xml.spreadsheet.utils.MapBuilder;
import xml.spreadsheet.utils.XmlHelper;


/**
 * Defines the protection properties that should be used in cells referencing this style. 
 * This element exists as a short-hand way to apply protection to an entire table, 
 * row, or column, by simply adding it to a style. 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:protection">MSDN Protection element reference</a>
 */
public class Protection {

	//-------------------------------------------------------------------
	// Class properties
	
	/**
	 * This attribute indicates whether or not this cell is protected. 
	 * When the worksheet is unprotected, cell-level protection has no effect. 
	 * When a cell is protected, it will not allow the user to enter information into it. 
	 */
	private Boolean protectedCell = null;
	
	/**
	 * This attribute indicates whether or not this cell's formula 
	 * should be hidden when worksheet protection is enabled. 
	 */
	private Boolean hideFormula = null;
	
	//-------------------------------------------------------------------
	// Class methods
	
	/** Default constructor. */
	public Protection() {}
	
	/**
	 * Copy constructor.
	 * @param protection Original protection
	 */
	public Protection(Protection protection) {
		this.hideFormula = protection.hideFormula;
		this.protectedCell = protection.protectedCell;
	}

	/**
	 * @param protectedCell the protectedCell to set
	 */
	public void setProtectedCell(boolean protectedCell) {
		this.protectedCell = protectedCell;
	}

	/**
	 * @param hideFormula the hideFormula to set
	 */
	public void setHideFormula(boolean hideFormula) {
		this.hideFormula = hideFormula;
	}
	
	@Override
	public String toString() {
		return XmlHelper.element("ss:Protection",
			MapBuilder.of(
				"ss:Protected", protectedCell,
				"x:HideFormula", hideFormula
			)
		);
	}
	
	
}
