/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Defines the font alignment attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:alignment
 */
public class Alignment {
	
	//-----------------------------------------------------------
	// Subtypes and constants
	
	// Reading order of a cell
	public enum ReadingOrder {
		
		// Constant values from
		// http://www.smarterdatacollection.com/Blog/?p=374
		
		RightToLeft ("-5004"), 
		LeftToRight ("-5003"), 
		Context ("-5002");
		
		private String order;
		
		private ReadingOrder(String order) {
			this.order = order;
		}
		
		public String toString() {
			return order;
		}
	}
	
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
	private HorizontalAlignment horizontal = HorizontalAlignment.Automatic;
	
	/** Number of indents. */
	private int indent = 0;
	
	/** Reading order of the cell. */
	private ReadingOrder readingOrder;
	
	/** Rotation of the cell.  
	 * 90 is straight up, 0 is horizontal, and -90 is straight down
	 */
	private double rotate = 0.0;
	
	/**
	 * True means that the text size should be shrunk so that all of the text 
	 * fits within the cell. False means that the font within the cell 
	 * should behave normally.  
	 */
	private boolean shrinkToFit = false;
	
	/** Vertical alignment of the cell. */
	private VerticalAlignment vertical = VerticalAlignment.Automatic;
	
	/** Specifies whether the text is drawn "downwards", 
	 * whereby each letter is drawn horizontally, one above the other. */
	private boolean verticalText = false;
	
	/** Specifies whether the text in this cell should wrap at the cell boundary. 
	 * False means that text either spills or gets truncated at the cell boundary 
	 * (depending on whether the adjacent cell(s) have content).*/
	private boolean wrapText = false;
	
	
	//-----------------------------------------------------------
	// Class methods
	
	Alignment() {}
	
	/**
	 * @return the horizontal
	 */
	public HorizontalAlignment getHorizontal() {
		return horizontal;
	}
	/**
	 * @param horizontal the horizontal to set
	 */
	public void setHorizontal(HorizontalAlignment horizontal) {
		this.horizontal = horizontal;
	}
	/**
	 * @return the indent
	 */
	public int getIndent() {
		return indent;
	}
	/**
	 * @param indent the indent to set
	 */
	public void setIndent(int indent) {
		this.indent = indent;
	}
	/**
	 * @return the readingOrder
	 */
	public ReadingOrder getReadingOrder() {
		return readingOrder;
	}
	/**
	 * @param readingOrder the readingOrder to set
	 */
	public void setReadingOrder(ReadingOrder readingOrder) {
		this.readingOrder = readingOrder;
	}
	/**
	 * @return the rotate
	 */
	public double getRotate() {
		return rotate;
	}
	/**
	 * @param rotate the rotate to set
	 */
	public void setRotate(double rotate) {
		this.rotate = rotate;
	}
	/**
	 * @return the shrinkToFit
	 */
	public boolean isShrinkToFit() {
		return shrinkToFit;
	}
	/**
	 * @param shrinkToFit the shrinkToFit to set
	 */
	public void setShrinkToFit(boolean shrinkToFit) {
		this.shrinkToFit = shrinkToFit;
	}
	/**
	 * @return the vertical
	 */
	public VerticalAlignment getVertical() {
		return vertical;
	}
	/**
	 * @param vertical the vertical to set
	 */
	public void setVertical(VerticalAlignment vertical) {
		this.vertical = vertical;
	}
	/**
	 * @return the verticalText
	 */
	public boolean isVerticalText() {
		return verticalText;
	}
	/**
	 * @param verticalText the verticalText to set
	 */
	public void setVerticalText(boolean verticalText) {
		this.verticalText = verticalText;
	}
	/**
	 * @return the wrapText
	 */
	public boolean isWrapText() {
		return wrapText;
	}
	/**
	 * @param wrapText the wrapText to set
	 */
	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
	}
	
	@Override
	public String toString() {
		// TODO: XML rendering of the alignment
		return "";
	}
}
