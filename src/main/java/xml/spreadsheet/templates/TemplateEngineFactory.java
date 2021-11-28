/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import xml.spreadsheet.utils.PropertiesReader;
import xml.spreadsheet.XMLSpreadsheetException;

/**
 * Singleton factory for template engines
 */
public class TemplateEngineFactory {

	
	//---------------------------------------------------
	// Class constants
	
	/** Prefix for template engine implementation class properties. */
	private static final String PROPERTY_ENGINE_PREFIX = "template.engine.";
	/** Property with the id of the default template engine. */
	private static final String PROPERTY_DEFAULT_ENGINE = "template.engine.default";
	
	//---------------------------------------------------
	// Class properties
	
	/** Singleton instance of the factory. */
	private static TemplateEngineFactory factory = null;
	/** Array of registered implementations. */
	private Map<String, TemplateEngine> engines = null;
	
	//---------------------------------------------------
	// Class methods
	
	// Hidden in order to avoid instantiation
	private TemplateEngineFactory() throws XMLSpreadsheetException {
		engines = 
			new Hashtable<String, TemplateEngine>();
		// Reads and tries to instantiate every possible template engine
		//	found in spreadsheet.properties
		List<String> templateEngines = 
			PropertiesReader.READER.propertiesByPrefix(PROPERTY_ENGINE_PREFIX);
		for (String id: templateEngines) {
			try {
				String className = PropertiesReader.READER.property(id);
				if (className != null && className.trim().length() > 0 
						&& !id.equals(PROPERTY_DEFAULT_ENGINE)) {
					@SuppressWarnings("rawtypes")
					Class clazz = Class.forName(className.trim());  
					TemplateEngine t = (TemplateEngine) clazz.newInstance();
					engines.put(id, t);
				}
			}
			catch(Throwable t) {
				throw new XMLSpreadsheetException(t);
			}
		}
	}
	
	/**
	 * Public entry point to get a reference to the factory singleton
	 * @return Singleton of the template engines factory
	 * @throws XMLSpreadsheetException If found any error instantiating the factory
	 */
	public static TemplateEngineFactory factory() throws XMLSpreadsheetException {
		if (factory == null) {
			factory = new TemplateEngineFactory();
		}
		return factory;
	}
	
	/**
	 * Returns the default template engine
	 * @return Instance of a template engine
	 * @throws XMLSpreadsheetException If found any error instantiating the engine
	 */
	public TemplateEngine engine() throws XMLSpreadsheetException {
		if (engines == null) {
			throw new TemplateException(
					"No template engines registered - " +
					"check spreadsheet.properties for any template.engine.* property");
		}
		else {
			// Return the default engine (property template.engine.default)
			String defaultEngine = PropertiesReader.READER.property(PROPERTY_DEFAULT_ENGINE);
			return engine(defaultEngine);
		}
	}
	
	/**
	 * Returns the engine identified by the parameter.
	 * @param id Template identifier
	 * @return Requested implementation of the template engine
	 * @throws TemplateException If found any error instantiating the engine
	 */
	public TemplateEngine engine(String id) throws TemplateException {
		String engineId = PROPERTY_ENGINE_PREFIX + id;
		if (!engines.containsKey(engineId)) {
			// Try to instantiate it
			throw new TemplateException("No template engine registered for id " + id);
		}		
		return engines.get(engineId);
	}
}
