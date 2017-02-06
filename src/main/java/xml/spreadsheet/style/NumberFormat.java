/**
 * 
 */
package xml.spreadsheet.style;

import xml.spreadsheet.utils.Table;
import xml.spreadsheet.utils.XmlHelper;

/**
 * Defines the number format that should be in cells referencing this style. 
 * The default value is General, and all other number formats require a custom format code. 
 * Though it is not intuitive, xml spreadsheet date formats are treated as number formats.  See
 * the appropiate documentation.  Note that every date format for the xml spreadsheet format
 * must have any non-format specific character preceded by a <code>\</code>
 * <br/>  This way, a dd-mm-yyyy format would require this Java code:<br/><br/>
 * <code>
 * Output documentOutput = // wherever it goes  <br/>
 * XMLSpreadsheetGenerator generator = new XMLSpreadsheetGenerator(documentOutput) ;<br/>
 * Style dateStyle = generator.createStyle();<br/>
 * dateStyle.numberFormat().setFormat("dd\\-mm\\-yyyy");<br/>
 * </code><br/>
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/aa140066%28v=office.10%29.aspx#odc_xmlss_ss:numberformat">MSDN NumberFormat element reference</a>
 * @see <a href="http://office.microsoft.com/en-us/excel-help/create-a-custom-number-format-HP010342372.aspx">Technical article on number format creation</a>
 * @see <a href="http://office.microsoft.com/en-us/excel-help/format-a-date-the-way-you-want-HA102809474.aspx">Custom date formatting</a>
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
	private String format = null;
	
	
	//-------------------------------------------------------------------
	// Class methods
	
	/** Default constructor. */
	public NumberFormat() {}
	
	/**
	 * Copy constructor.
	 * @param numberFormat Original number format to copy
	 */
	public NumberFormat(NumberFormat numberFormat) {
		this.format = numberFormat.format;
	}
	
	@Override
	public String toString() {
		return XmlHelper.element("ss:NumberFormat",
			new Table<Object>().
				add("ss:Format", format));
	}
	
	/**
	 * @param format A number format code in the Excel number format syntax.
	 * See http://office.microsoft.com/en-us/excel-help/create-a-custom-number-format-HP010342372.aspx
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * @param format A predefined XML Spreadsheet format 
	 */
	public void setFormat(Format format) {
		this.format = format.toString();
	}
}
