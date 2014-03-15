/**
 * 
 */
package xml.spreadsheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Kind of closure to build maps for the template engine
 */
class MapClosure {

	//---------------------------------------------------------------
	// Class properties
	
	/** Map withe the values contained in the closure */
	private Map<String, String> values;
	
	//---------------------------------------------------------------
	// Class methods
	
	/**
	 * Default constructor
	 */
	public MapClosure() {
		values = new HashMap<String, String>();
	}
	
	/**
	 * Adds a pair key-value to the map, returning the object.
	 * @param key Index to the value
	 * @param v Indexed value
	 * @return Reference to the closure
	 */
	public MapClosure add(String key, String v) {
		values.put(key, v);
		return this;
	}
	
	/**
	 * @return Map with the values added to the closure
	 */
	public Map<String, String> map() {
		return values;
	}
}
