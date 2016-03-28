/**
 * 
 */
package xml.spreadsheet.utils;

import java.util.HashMap;

/**
 * Kind of map builder to build maps for the template engine
 */
public class Table<T> {

	//---------------------------------------------------------------
	// Class properties
	
	/** Map withe the values contained in the map builder */
	private java.util.Map<String, T> values;
	
	//---------------------------------------------------------------
	// Class methods
	
	/**
	 * Default constructor
	 */
	public Table() {
		values = new HashMap<String, T>();
	}
	
	/**
	 * Adds a pair key-value to the map, returning the table object.  If the
	 * value is null, the method does not add it to the collection.
	 * @param key Index to the value
	 * @param v Indexed value
	 * @return Reference to the map builder
	 */
	public Table<T> add(String key, T v) {
		if (v != null) {
			values.put(key, v);
		}
		return this;
	}
	
	/**
	 * @return Map with the values added to the map builder
	 */
	public java.util.Map<String, T> map() {
		return values;
	}
}
