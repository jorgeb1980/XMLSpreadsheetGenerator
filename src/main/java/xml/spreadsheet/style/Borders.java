/**
 * 
 */
package xml.spreadsheet.style;

import java.util.HashMap;
import java.util.Map;

import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.style.Border.BorderPosition;

/**
 * Defines the border properties for cells referencing this style. The Borders 
 * element contains no attributes; it is purely a container for individual Border elements. 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:borders">MSDN Borders element reference</a>
 */
public class Borders {

	//-----------------------------------------------------------
	// Class properties

	/** Defined borders, indexed by position. */
	private Map<Border.BorderPosition, Border> borders;
	
	//-----------------------------------------------------------
	// Class methods
	
	/** Default constructor. */
	public Borders() {
		borders = new HashMap<Border.BorderPosition, Border>();
	}
	
	/**
	 * Copy constructor.
	 * @param originalBorders Original borders
	 */
	public Borders(Borders originalBorders) {
		this();
		for (BorderPosition originalPosition: originalBorders.borders.keySet()) {
			Border originalBorder = originalBorders.borders.get(originalPosition);
			Border border = new Border(originalBorder);
			this.borders.put(originalPosition, border);
		}		
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
		StringBuilder sb = new StringBuilder();
		
		if (borders != null && borders.size() > 0) {
			sb.append("<ss:Borders>");
			for (Border border: borders.values()) {
				sb.append(border.toString());
			}
			sb.append("</ss:Borders>");
		}
		
		return sb.toString();
	}
}
