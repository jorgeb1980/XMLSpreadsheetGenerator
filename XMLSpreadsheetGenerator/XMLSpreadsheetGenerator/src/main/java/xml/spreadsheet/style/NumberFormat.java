/**
 * 
 */
package xml.spreadsheet.style;

/**
 * Defines the number format that should be in cells referencing this style. 
 * The default value is General, and all other number formats require a custom format code. 
 * http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:numberformat
 */
public class NumberFormat {

	//-------------------------------------------------------------------
	// Subtypes and constants
	
	/** Predefined number formats */
	public enum Format {
		General ("General"), 
		GeneralNumber ("General Number"), 
		GeneralDate ("General Date"), 
		LongDate ("Long Date"), 
		MediumDate ("Medium Date"), 
		ShortDate ("Short Date"), 
		LongTime ("Long Time"), 
		MediumTime ("Medium Time"), 
		ShortTime ("Short Time"), 
		Currency ("Currency"), 
		EuroCurrency ("Euro Currency"), 
		Fixed ("Fixed"), 
		Standard ("Standard"), 
		Percent ("Percent"), 
		Scientific ("Scientific"), 
		YesNo ("Yes/No"), 
		TrueFalse ("True/False"), 
		OnOff ("On/Off");
		
		private String format;
		
		private Format(String format) {
			this.format = format;
		}
		
		@Override
		public String toString() {
			return format;
		}
	}
	
	//-------------------------------------------------------------------
	// Class properties
	
	/**
	 * A number format code in the Excel number format syntax.
	 */
	private String format = Format.General.toString();
	
	
	//-------------------------------------------------------------------
	// Class methods
	
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format.toString();
	}
}
