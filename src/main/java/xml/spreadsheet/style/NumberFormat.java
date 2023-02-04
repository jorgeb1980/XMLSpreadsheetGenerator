package xml.spreadsheet.style;

import static xml.spreadsheet.utils.MapBuilder.mapOf;
import static xml.spreadsheet.utils.XmlHelper.element;

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
public record NumberFormat(
	/* A number format code in the Excel number format syntax. */
	String format
) {
	
	/** Predefined number formats */
	public static NumberFormat GENERAL = new NumberFormat("General");
	public static NumberFormat GENERAL_NUMBER = new NumberFormat("General Number");
	public static NumberFormat GENERAL_DATE = new NumberFormat("General Date");
	public static NumberFormat LONG_DATE = new NumberFormat("Long Date");
	public static NumberFormat MEDIUM_DATE = new NumberFormat("Medium Date");
	public static NumberFormat SHORT_DATE = new NumberFormat("Short Date");
	public static NumberFormat LONG_TIME = new NumberFormat("Long Time");
	public static NumberFormat MEDIUM_TIME = new NumberFormat("Medium Time");
	public static NumberFormat SHORT_TIME = new NumberFormat("Short Time");
	public static NumberFormat CURRENCY = new NumberFormat("Currency");
	public static NumberFormat EURO_CURRENCY = new NumberFormat("Euro Currency");
	public static NumberFormat FIXED = new NumberFormat("Fixed");
	public static NumberFormat STANDARD = new NumberFormat("Standard");
	public static NumberFormat PERCENT = new NumberFormat("Percent");
	public static NumberFormat SCIENTIFIC = new NumberFormat("Scientific");
	public static NumberFormat YES_NO = new NumberFormat("Yes/No");
	public static NumberFormat TRUE_FALSE = new NumberFormat("True/False");
	public static NumberFormat ON_OFF = new NumberFormat("On/Off");
	
	/**
	 * Copy constructor.
	 * @param numberFormat Original number format to copy
	 */
	public static NumberFormat from(NumberFormat numberFormat) {
		return numberFormat != null ? new NumberFormat(numberFormat.format()) : null;
	}
	
	@Override
	public String toString() {
		return element("ss:NumberFormat", mapOf("ss:Format", this.format));
	}
}
