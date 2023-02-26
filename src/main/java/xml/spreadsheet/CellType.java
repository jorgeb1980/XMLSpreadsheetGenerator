package xml.spreadsheet;

/**
 * Possible types for an XMLSpreadsheet cell.
 * https://learn.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10)?redirectedfrom=MSDN#required-attributes-2
 */
enum CellType {
	Number, DateTime, Boolean, String, Error
}
