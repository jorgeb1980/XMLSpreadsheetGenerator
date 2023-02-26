package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;


/**
 * Defines the protection properties that should be used in cells referencing this style. 
 * This element exists as a short-hand way to apply protection to an entire table, 
 * row, or column, by simply adding it to a style. 
 * @see <a href="https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)#ssprotection-tag">MSDN Protection element reference</a>
 */
public record Protection(
	/*
	 * This attribute indicates whether or not this cell is protected.
	 * When the worksheet is unprotected, cell-level protection has no effect.
	 * When a cell is protected, it will not allow the user to enter information into it.
	 */
	Boolean protectedCell,
	/*
	 * This attribute indicates whether or not this cell's formula
	 * should be hidden when worksheet protection is enabled.
	 */
	Boolean hideFormula
) {
	
	/**
	 * Copy constructor.
	 * @param protection Original protection
	 */
	public static Protection from(Protection protection) {
		return protection != null ?
			new Protection(
				protection.hideFormula,
				protection.protectedCell
			): null;
	}

	public static ProtectionBuilder builder() { return new ProtectionBuilder(); }

	public static class ProtectionBuilder{
		private Boolean protectedCell;
		private Boolean hideFormula;

		ProtectionBuilder() { }

		/**
		 * @param protectedCell the protectedCell to set
		 */
		public ProtectionBuilder withProtectedCell(boolean protectedCell) {
			this.protectedCell = protectedCell;
			return this;
		}

		/**
		 * @param hideFormula the hideFormula to with
		 */
		public ProtectionBuilder withHideFormula(boolean hideFormula) {
			this.hideFormula = hideFormula;
			return this;
		}

		public Protection build() {
			return new Protection(
				protectedCell,
				hideFormula
			);
		}

	}

	@Override
	public String toString() {
		return element("ss:Protection",
			mapOf(
				"ss:Protected", protectedCell,
				"x:HideFormula", hideFormula
			)
		);
	}
	
	
}
