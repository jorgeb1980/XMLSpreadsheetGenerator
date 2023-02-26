package xml.spreadsheet;

import xml.spreadsheet.style.*;

import java.util.List;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

/**
 * This class contains the information for a style definition in an XML
 * spreadsheet.  It can only be built attached to a certain <code>XMLSpreadsheetGenerator</code> by calling <code>createStyle</code> 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)?redirectedfrom=MSDN#ssstyle-tag">MSDN Style element reference</a>
 * @see xml.spreadsheet.XMLSpreadsheetGenerator#createStyle()
 */
public record Style(
	/* Unique id */
	String id,
	/* Name.  This will be used by the software to identify named styles and
	 * offer them to the user in some dialogs. */
	String name,
	/* Parent */
	String parent,
	/* Alignment */
	Alignment alignment,
	/* Borders */
	Borders borders,
	/* Font */
	Font font,
	/* Interior */
	Interior interior,
	/* NumberFormat */
	NumberFormat numberFormat,
	/* Protection */
	Protection protection
) {

	public static StyleBuilder builder(List<Style> styles) { return new StyleBuilder(styles); }

	public static class StyleBuilder {
		private String id;
		private String name;
		private Style parent;
		private Alignment alignment;
		private Borders borders;
		private Font font;
		private Interior interior;
		private NumberFormat numberFormat;
		private Protection protection;
		final private List<Style> styles;

		public Style build() {
			Style style = new Style(
				id,
				name,
				parent,
				alignment,
				borders,
				font,
				interior,
				numberFormat,
				protection
			);
			styles.add(style);
			return style;
		}

		StyleBuilder(List<Style> styles) { this.styles = styles; }

		public StyleBuilder withId(String id) {
			this.id = id;
			return this;
		}

		public StyleBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public StyleBuilder withParent(Style parent) {
			this.parent = parent;
			return this;
		}

		public StyleBuilder withAlignment(Alignment a) {
			alignment = a;
			return this;
		}

		public StyleBuilder withBorders(Borders b) {
			borders = b;
			return this;
		}

		public StyleBuilder withFont(Font f) {
			font = f;
			return this;
		}

		public StyleBuilder withInterior(Interior i) {
			interior = i;
			return this;
		}

		public StyleBuilder withNumberFormat(NumberFormat nf) {
			numberFormat = nf;
			return this;

		}

		public StyleBuilder withProtection(Protection p) {
			protection = p;
			return this;
		}
	}

	Style(
		String id,
		String name,
		Style parent,
		Alignment alignment,
		Borders borders,
		Font font,
		Interior interior,
		NumberFormat numberFormat,
		Protection protection
	) {
		// Priority: explicit override > parent values
		this(
			id,
			name,
			parent != null ? parent.id : null,
			alignment != null ? alignment : (parent != null ? Alignment.from(parent.alignment) : null),
			borders != null ? borders : (parent != null ? Borders.from(parent.borders) : null),
			font != null ? font : (parent != null ? Font.from(parent.font) : null),
			interior != null ? interior : (parent != null ? Interior.from(parent.interior) : null),
			numberFormat != null ? numberFormat : (parent != null ? NumberFormat.from(parent.numberFormat) : null),
			protection != null ? protection : (parent != null ? Protection.from(parent.protection) : null)
		);
	}

	@Override
	public String toString() {
		
		return element("ss:Style",
			mapOf(
				"ss:ID", id,
				"ss:Name", name,
				"ss:Parent", parent
			),
			// Compose every inner style element XML representation inside 
			//	the Style element
			new StringBuilder()
				.append(alignment != null ? alignment.toString():"")
				.append(borders != null ? borders.toString():"")
				.append(font != null ? font.toString():"")
				.append(interior != null ? interior.toString():"")
				.append(numberFormat != null ? numberFormat.toString():"")
				.append(protection != null ? protection.toString():"").toString()
			);
	}
	
}
