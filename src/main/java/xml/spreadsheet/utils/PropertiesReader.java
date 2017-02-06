/**
 * 
 */
package xml.spreadsheet.utils;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Static properties container for the library
 */
public class PropertiesReader {

	//----------------------------------------------------------
	// Class constants
	
	/** Name of the master properties file */
	private static final String PROPERTIES_FILE = "spreadsheet.properties";
	
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
	 * @throws XMLSpreadsheetException If had any problem finding the
	 * properties file
	 */
	private static Properties properties() 
			throws XMLSpreadsheetException {
		if (properties == null) {
			properties = new Properties();
			try {
				InputStream is = null;
				try {
					is = PropertiesReader.class.getClassLoader().
							getResourceAsStream(PROPERTIES_FILE);
					properties.load(is);
				}
				finally {
					is.close();
				}
			}
			catch(Throwable t) {
				throw new XMLSpreadsheetException("Could not find " + PROPERTIES_FILE + " in the classpath");
			}			
		}
		return properties;
	}
	
	/**
	 * Returns the requested property value
	 * @param property Name of the property
	 * @return Value of the property
	 * @throws XMLSpreadsheetException If had any problem finding the
	 * properties file
	 */
	public static String property(String property)
			throws XMLSpreadsheetException {
		return properties().getProperty(property);
	}
	
	/**
	 * Returns an ordered list of properties by an specified index.  It respects
	 * the order the user wrote the properties in at the .properties file. 
	 * @param prefix Property prefix
	 * @return List of properties with that exact prefix, ordered by their index
	 * @throws XMLSpreadsheetException If had any problem finding the
	 * properties file
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
		Collections.sort(strings);
		return strings;
	}
}
