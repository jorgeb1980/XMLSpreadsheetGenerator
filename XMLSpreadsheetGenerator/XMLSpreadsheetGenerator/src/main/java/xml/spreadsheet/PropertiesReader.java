/**
 * 
 */
package xml.spreadsheet;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Static properties container for the application
 */
public class PropertiesReader {

	//----------------------------------------------------------
	// Class properties
	
	// Static singleton instance
	private static Properties properties;
	
	//----------------------------------------------------------
	// Class methods
	
	// Prevents instatiation
	private PropertiesReader() {}
	
	
	/** 
	 * Singleton instance of the .properties file
	 * @return Properties object
	 */
	private static Properties properties() 
			throws XMLSpreadsheetException {
		if (properties == null) {
			properties = new Properties();
			try {
				InputStream is = null;
				try {
					is = PropertiesReader.class.getClassLoader().
							getResourceAsStream("spreadsheet.properties");
					properties.load(is);
				}
				finally {
					is.close();
				}
			}
			catch(Throwable t) {
				throw new XMLSpreadsheetException(t);
			}			
		}
		return properties;
	}
	
	/**
	 * Returns the requested property value
	 * @param property Name of the property
	 * @return Value of the property
	 */
	public static String property(String property) 
			throws XMLSpreadsheetException {
		return properties().getProperty(property);
	}
	
	/**
	 * Returns an ordered list of properties by an specified index.  It respects
	 * the order the user wrote the properties in at the .properties file. 
	 * @param prefix Property prefix
	 * @return List of properties with that exact prefix, in the same
	 * order they were written in the .properties file
	 * @throws XMLSpreadsheetException
	 */
	public static List<String> propertiesByPrefix(String prefix)
			throws XMLSpreadsheetException {
		Properties prop = properties();
		List<String> strings = new LinkedList<String>();
		for (Object key: prop.keySet()) {
			String sKey = (String) key;
			if ((sKey).startsWith(prefix)) {
				strings.add(sKey);
			}
		}
		return strings;
	}
}
