/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.utils.AttributeHelper;

/**
 * Defines the protection properties that should be used in cells referencing this style. 
 * This element exists as a short-hand way to apply protection to an entire table, 
 * row, or column, by simply adding it to a style. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:protection
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
	
	public Protection() {}

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
		StringBuilder sb = new StringBuilder();
		
		sb.append("<ss:Protection");
		
		AttributeHelper.att(sb, "ss:Protected", protectedCell);
		AttributeHelper.att(sb, "x:HideFormula", hideFormula);
		
		sb.append("/>");
		
		return sb.toString();
	}
	
	
}
