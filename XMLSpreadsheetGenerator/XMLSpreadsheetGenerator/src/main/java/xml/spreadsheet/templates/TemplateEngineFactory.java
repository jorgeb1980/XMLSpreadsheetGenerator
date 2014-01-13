/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 */
public class TemplateEngineFactory {

	//---------------------------------------------------
	// Class properties
	
	/** Array of registered implementations. */
	private static Map<String, TemplateEngine> engines = 
			new Hashtable<String, TemplateEngine>();
	
	//---------------------------------------------------
	// Class methods
	
	// Hidden in order to avoid instantiation
	private TemplateEngineFactory() {}
	
	/**
	 * Returns the default template engine
	 * @return Instance of a template engine
	 */
	public static TemplateEngine engine() {
		validateEngines();
		// Return the first engine registered
		return engines.values().iterator().next();
	}
	
	/** 
	 * Entry point for all template engines to register into the factory
	 * @param id Engine identifier
	 * @param te Concrete template engine
	 */
	static void registerEngine(String id, TemplateEngine te) {
		engines.put(id, te);
	}
	
	/**
	 * Returns the engine identified by the parameter.
	 * @param engineId Template identifier
	 * @return 
	 */
	public static TemplateEngine engine(String id) {
		validateEngines();
		if (!engines.containsKey(id)) {
			throw new NullPointerException("No template engine found for id " + id);
		}
		return engines.get(id);
	}

	// Make sure there is some engine registered
	private static void validateEngines() {
		if (engines.isEmpty()) {
			throw new NullPointerException("No template engine initialized");
		}
	}
}
