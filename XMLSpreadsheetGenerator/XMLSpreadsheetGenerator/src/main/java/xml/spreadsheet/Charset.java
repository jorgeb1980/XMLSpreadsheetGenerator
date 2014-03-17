/**
 * 
 */
package xml.spreadsheet;

/**
 * Recognized character sets:<br/>
    00H = 0 = 	ANSI Latin <br/>
    A2H = 162 = ANSI Turkish<br/>
	01H = 1 = 	System default <br/>
	A3H = 163 = ANSI Vietnamese<br/>
	02H = 2 = 	Symbol <br/>
	B1H = 177 = ANSI Hebrew<br/>
	4DH = 77 = 	Apple Roman <br/>
	B2H = 178 = ANSI Arabic<br/>
	80H = 128 = ANSI Japanese Shift-JIS <br/>
	BAH = 186 = ANSI Baltic<br/>
	81H = 129 = ANSI Korean (Hangul) <br/>
	CCH = 204 = ANSI Cyrillic<br/>
	82H = 130 = ANSI Korean (Johab) <br/>
	DEH = 222 = ANSI Thai<br/>
	86H = 134 = ANSI Chinese Simplified GBK <br/>
	EEH = 238 = ANSI Latin II (Central European)<br/>
	88H = 136 = ANSI Chinese Traditional BIG5 <br/>
	FFH = 255 = OEM Latin I<br/>
	A1H = 161 = ANSI Greek<br/>
	
	Source: <a href="http://www.openoffice.org/sc/excelfileformat.pdf">Open Office file format documentation</a>
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
