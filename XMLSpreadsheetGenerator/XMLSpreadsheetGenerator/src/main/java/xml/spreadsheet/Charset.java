/**
 * 
 */
package xml.spreadsheet;

/**
 * Recognized character sets.
    00H = 0 = 	ANSI Latin 
    A2H = 162 = ANSI Turkish
	01H = 1 = 	System default 
	A3H = 163 = ANSI Vietnamese
	02H = 2 = 	Symbol 
	B1H = 177 = ANSI Hebrew
	4DH = 77 = 	Apple Roman 
	B2H = 178 = ANSI Arabic
	80H = 128 = ANSI Japanese Shift-JIS 
	BAH = 186 = ANSI Baltic
	81H = 129 = ANSI Korean (Hangul) 
	CCH = 204 = ANSI Cyrillic
	82H = 130 = ANSI Korean (Johab) 
	DEH = 222 = ANSI Thai
	86H = 134 = ANSI Chinese Simplified GBK 
	EEH = 238 = ANSI Latin II (Central European)
	88H = 136 = ANSI Chinese Traditional BIG5 
	FFH = 255 = OEM Latin I
	A1H = 161 = ANSI Greek
	
	Source: http://www.openoffice.org/sc/excelfileformat.pdf
 */
public enum Charset {
	ANSI_Latin (0),
	ANSI_Turkish (162),
	Default (1),
	ANSI_Vietnamese (163),
	Symbol (2),
	ANSI_Hebrew (177),
	Apple_Roman (77),
	ANSI_Arabic (178),
	ANSI_Japanese (128),
	ANSI_Baltic (186),
	ANSI_Korean (129),
	ANSI_Thai (222),
	ANSI_Chinese_Simplified (134),
	ANSI_Latin_Central_European (238),
	ANSI_Chinese_Traditional (136),
	OEM_Latin (255),
	ANSI_Greek (161);
	
	private int value;
	
	Charset(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
