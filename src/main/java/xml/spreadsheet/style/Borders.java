/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.XMLSpreadsheetException;
import xml.spreadsheet.style.Border.BorderPosition;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Defines the border properties for cells referencing this style. The Borders 
 * element contains no attributes; it is purely a container for individual Border elements. 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)#ssborders-tag">MSDN Borders element reference</a>
 */
public record Borders(
	/* Defined borders, indexed by position. Unmodifiable version of the map. */
	Map<BorderPosition, Border> borders
) {

	public Borders(Map<BorderPosition, Border> borders) {
		this.borders = borders != null ? unmodifiableMap(borders) : null;
	}

	/**
	 * Copy constructor.
	 * @param originalBorders Original borders
	 */
	public static Borders from (Borders originalBorders) {
		if (originalBorders == null) return null;
		else {
			Map<BorderPosition, Border> borders = new HashMap<>();
			for (var originalPosition : originalBorders.borders.keySet()) {
				var originalBorder = originalBorders.borders.get(originalPosition);
				// Create new instances of Border
				borders.put(originalPosition, Border.from(originalBorder));
			}
			return new Borders(borders);
		}
	}

	public static BordersBuilder builder() { return new BordersBuilder(); }

	public static class BordersBuilder {

		private Map<BorderPosition, Border> borders;

		private BordersBuilder() {
			borders = new HashMap<>();
		}

		public Borders build() {
			return new Borders(borders);
		}

		/**
		 * Creates a Border inside the Borders container.  It admits a Border instance
		 * for each of the six possible positions.
		 *
		 * @return Border object just created
		 * @throws XMLSpreadsheetException If tried to insert a border in an
		 *                                 already used position
		 */
		public BordersBuilder withBorder(Border border) throws XMLSpreadsheetException {
			if (borders.containsKey(border.position())) {
				throw new XMLSpreadsheetException("The " + border.position() + " border is already defined");
			}
			borders.put(border.position(), border);
			return this;
		}

	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		if (borders != null && !borders.isEmpty()) {
			sb.append("<ss:Borders>");
			for (Border border: borders.values()) {
				sb.append(border.toString());
			}
			sb.append("</ss:Borders>");
		}
		
		return sb.toString();
	}
}
