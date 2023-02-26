/**
 * 
 */
package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * Defines the font alignment attributes to use in this style. 
 * Each attribute that is specified is considered an override from the default. 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)#ssalignment-tag">MSDN Alignment element reference</a>
 */
public record Alignment(
	/* Horizontal alignment of the cell. */
	HorizontalAlignment horizontal,
	/*
	 * Rotation of the cell.
	 * 90 is straight up, 0 is horizontal, and -90 is straight down
	 */
	Double rotate,
	/*
	 * True means that the text size should be shrunk so that all of the text
	 * fits within the cell. False means that the font within the cell
	 * should behave normally.
	 */
	Boolean shrinkToFit,
	/* Vertical alignment of the cell. */
	VerticalAlignment vertical,
	/*
	 * Specifies whether the text is drawn "downwards",
	 * whereby each letter is drawn horizontally, one above the other.
	 */
	Boolean verticalText,
	/*
	 * Specifies whether the text in this cell should wrap at the cell boundary.
	 * False means that text either spills or gets truncated at the cell boundary
	 * (depending on whether the adjacent cell(s) have content).
	 */
	Boolean wrapText
) {
		
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
	}
	
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

	/**
	 * Copy constructor.
	 * @param alignment Original alignment
	 */
	public static Alignment from(Alignment alignment) {
		return alignment != null ?
			new Alignment(
				alignment.horizontal,
				alignment.rotate,
				alignment.shrinkToFit,
				alignment.vertical,
				alignment.verticalText,
				alignment.wrapText
			): null;
	}

	public static AlignmentBuilder builder() { return new AlignmentBuilder(); }
		
	public static class AlignmentBuilder {
		
		private HorizontalAlignment horizontal;
		private Double rotate;
		private Boolean shrinkToFit;
		private VerticalAlignment vertical;
		private Boolean verticalText;
		private Boolean wrapText;
		
		private AlignmentBuilder() {}

		/**
		 * @param horizontal Specifies the left-to-right alignment of text within a cell
		 */
		public AlignmentBuilder withHorizontal(HorizontalAlignment horizontal) {
			this.horizontal = horizontal;
			return this;
		}

		/**
		 * @param rotate Specifies the rotation of the text within the cell. 
		 * 90 is straight up, 0 is horizontal, and -90 is straight down
		 */
		public AlignmentBuilder withRotate(double rotate) {
			this.rotate = rotate;
			return this;
		}

		/**
		 * @param shrinkToFit True means that the text size should be 
		 * shrunk so that all of the text fits within the cell.
		 * False means that the font within the cell should behave normally
		 */
		public AlignmentBuilder withShrinkToFit(boolean shrinkToFit) {
			this.shrinkToFit = shrinkToFit;
			return this;
		}

		/**
		 * @param vertical Specifies the top-to-bottom alignment of text within a 
		 * cell. Distributed and JustifyDistributed are only legitimate values 
		 * when ss:VerticalText is True
		 */
		public AlignmentBuilder withVertical(VerticalAlignment vertical) {
			this.vertical = vertical;
			return this;
		}

		/**
		 * @param verticalText Specifies whether the text is drawn "downwards", 
		 * whereby each letter is drawn horizontally, one above the other
		 */
		public AlignmentBuilder withVerticalText(boolean verticalText) {
			this.verticalText = verticalText;
			return this;
		}

		/**
		 * @param wrapText Specifies whether the text in this cell should wrap at the cell boundary. 
		 * False means that text either spills or gets truncated at the cell boundary 
		 * (depending on whether the adjacent cell(s) have content)
		 */
		public AlignmentBuilder withWrapText(boolean wrapText) {
			this.wrapText = wrapText;
			return this;
		}
		
		public Alignment build() {
			return new Alignment(
				horizontal,
				rotate,
				shrinkToFit,
				vertical,
				verticalText,
				wrapText
			);
		}
	}
}
