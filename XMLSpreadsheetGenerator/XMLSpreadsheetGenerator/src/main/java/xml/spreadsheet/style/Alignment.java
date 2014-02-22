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
	private HorizontalAlignment horizontal = null;
	
	/** Number of indents. */
	private Integer indent = null;
	
	/** Reading order of the cell. */
	private ReadingOrder readingOrder = null;
	
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
		StringBuilder sb = new StringBuilder();
		
		sb.append("<ss:Alignment");
		
		AttributeHelper.att(sb, "ss:Horizontal", horizontal);
		
		if (indent != null) {
			AttributeHelper.att(sb, "ss:Indent", indent.intValue());
		}
	
		// Ignore reading order - not sure if supported
		
		if (rotate != null) {
			AttributeHelper.att(sb, "ss:Rotate", rotate.doubleValue());
		}
		
		if (shrinkToFit != null) {
			AttributeHelper.att(sb, "ss:ShrinkToFit", shrinkToFit.booleanValue());
		}
		
		AttributeHelper.att(sb, "ss:Vertical", vertical);
		
		if (verticalText != null) {
			AttributeHelper.att(sb, "ss:VerticalText", verticalText.booleanValue());
		}
		
		if (wrapText != null) {
			AttributeHelper.att(sb, "ss:WrapText", wrapText.booleanValue());
		}
		
		sb.append("/>");		
		
		return sb.toString();
	}
	
	public Alignment() {}
	
	/**
	 * @param horizontal Specifies the left-to-right alignment of text within a cell
	 */
	public void setHorizontal(HorizontalAlignment horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * @param indent Number of indents
	 */
	public void setIndent(int indent) {
		this.indent = indent;
	}

	/**
	 * @param readingOrder Specifies the default right-to-left text entry mode for a cell
	 */
	public void setReadingOrder(ReadingOrder readingOrder) {
		this.readingOrder = readingOrder;
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
