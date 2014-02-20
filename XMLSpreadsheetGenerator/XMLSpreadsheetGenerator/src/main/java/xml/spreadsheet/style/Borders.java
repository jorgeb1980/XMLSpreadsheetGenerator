/**
 * 
 */
package xml.spreadsheet.style;

import java.util.HashMap;
import java.util.Map;

import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Borders element inside style.
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:borders
 */
public class Borders {

	//-----------------------------------------------------------
	// Class properties

	private Map<Border.BorderPosition, Border> borders;
	
	//-----------------------------------------------------------
	// Class methods
	
	Borders() {
		borders = new HashMap<Border.BorderPosition, Border>();
	}
	
	public Border createBorder(Border.BorderPosition position) throws XMLSpreadsheetException {
		if (borders.containsKey(position)) {
			throw new XMLSpreadsheetException("The " + position + " border is already defined");
		}
		Border border = new Border(position);
		borders.put(position, border);
		return border;
	}
}
