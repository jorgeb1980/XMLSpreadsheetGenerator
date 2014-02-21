/**
 * 
 */
package xml.spreadsheet.style;

import java.util.HashMap;
import java.util.Map;

import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Defines the border properties for cells referencing this style. The Borders 
 * element contains no attributes; it is purely a container for individual Border elements. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:borders
 */
public class Borders {

	//-----------------------------------------------------------
	// Class properties

	/** Defined borders, indexed by position. */
	private Map<Border.BorderPosition, Border> borders;
	
	//-----------------------------------------------------------
	// Class methods
	
	Borders() {
		borders = new HashMap<Border.BorderPosition, Border>();
	}
	
	/**
	 * Creates a Border inside the Borders container.  It admits a Border instance
	 * for each of the six possible positions.
	 * @param position Border position
	 * @return Border object just created
	 * @throws XMLSpreadsheetException If tried to insert a border in an 
	 * already used position
	 */
	public Border createBorder(Border.BorderPosition position) throws XMLSpreadsheetException {
		if (borders.containsKey(position)) {
			throw new XMLSpreadsheetException("The " + position + " border is already defined");
		}
		Border border = new Border(position);
		borders.put(position, border);
		return border;
	}
	
	@Override
	public String toString() {
		// TODO: XML rendering of the borders
		return "";
	}
}
