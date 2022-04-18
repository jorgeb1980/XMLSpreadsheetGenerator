/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.utils.MapBuilder;
import xml.spreadsheet.utils.XmlHelper;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines the font alignment attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:alignment">MSDN Alignment element reference</a>
 */
public class Alignment {
	
	//-----------------------------------------------------------
	// Subtypes and constants
		
	// Horizontal alignment of a cell
	public enum HorizontalAlignment {
		Automatic, 
		Left, 
		Center, 
		Right, 
		Fill, 
		Justify, 
		CenterAcrossSelection, 
		Distributed, 
		JustifyDistributed;
		
		// No need to override toString, it is enough as is
	}
	
	// Vertical alignment of a cell
	public enum VerticalAlignment {
		Automatic, 
		Top, 
		Bottom, 
		Center, 
		Justify, 
		Distributed, 
		JustifyDistributed;
		
		// No need to override toString, it is enough as is
	}
	
	//-----------------------------------------------------------
	// Class properties
	
	/** Horizontal alignment of the cell. */
	private HorizontalAlignment horizontal = null;
	
	/** Rotation of the cell.  
	 * 90 is straight up, 0 is horizontal, and -90 is straight down
	 */
	private Double rotate = null;
	
	/**
	 * True means that the text size should be shrunk so that all of the text 
	 * fits within the cell. False means that the font within the cell 
	 * should behave normally.  
	 */
	private Boolean shrinkToFit = null;
	
	/** Vertical alignment of the cell. */
	private VerticalAlignment vertical = null;
	
	/** Specifies whether the text is drawn "downwards", 
	 * whereby each letter is drawn horizontally, one above the other. */
	private Boolean verticalText = null;
	
	/** Specifies whether the text in this cell should wrap at the cell boundary. 
	 * False means that text either spills or gets truncated at the cell boundary 
	 * (depending on whether the adjacent cell(s) have content).*/
	private Boolean wrapText = null;
	
	
	//-----------------------------------------------------------
	// Class methods
	
	@Override
	public String toString() {
		return element(
			"ss:Alignment",
			mapOf(
					"ss:Horizontal", horizontal != null ? horizontal.toString() : null,
					"ss:Rotate", rotate,
					"ss:ShrinkToFit", shrinkToFit,
					"ss:Vertical", vertical != null ? vertical.toString() : null,
					"ss:VerticalText", verticalText,
					"ss:WrapText", wrapText
			)
		);
	}
	
	/** Default constructor. */
	public Alignment() {}
	
	/**
	 * Copy constructor.
	 * @param alignment Original alignment
	 */
	public Alignment(Alignment alignment) {
		this.horizontal = alignment.horizontal;
		this.rotate = alignment.rotate;
		this.shrinkToFit = alignment.shrinkToFit;
		this.vertical = alignment.vertical;
		this.verticalText = alignment.verticalText;
		this.wrapText = alignment.wrapText;
	}
	
	/**
	 * @param horizontal Specifies the left-to-right alignment of text within a cell
	 */
	public void setHorizontal(HorizontalAlignment horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * @param rotate Specifies the rotation of the text within the cell. 
	 * 90 is straight up, 0 is horizontal, and -90 is straight down
	 */
	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	/**
	 * @param shrinkToFit True means that the text size should be 
	 * shrunk so that all of the text fits within the cell.
	 * False means that the font within the cell should behave normally
	 */
	public void setShrinkToFit(boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}

	/**
	 * @param vertical Specifies the top-to-bottom alignment of text within a 
	 * cell. Distributed and JustifyDistributed are only legitimate values 
	 * when ss:VerticalText is True
	 */
	public void setVertical(VerticalAlignment vertical) {
		this.vertical = vertical;
	}

	/**
	 * @param verticalText Specifies whether the text is drawn "downwards", 
	 * whereby each letter is drawn horizontally, one above the other
	 */
	public void setVerticalText(boolean verticalText) {
		this.verticalText = verticalText;
	}

	/**
	 * @param wrapText Specifies whether the text in this cell should wrap at the cell boundary. 
	 * False means that text either spills or gets truncated at the cell boundary 
	 * (depending on whether the adjacent cell(s) have content)
	 */
	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
	}
	
	
}
