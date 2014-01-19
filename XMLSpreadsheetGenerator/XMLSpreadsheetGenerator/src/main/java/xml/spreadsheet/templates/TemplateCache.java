/**
 * 
 */
package xml.spreadsheet.templates;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Static template cache
 */
public class TemplateCache {

	//---------------------------------------------------
	// Class properties
	
	/** Template cache */
	private static Map<String, String> templateCache;
	
	//---------------------------------------------------
	// Class methods
	
	/**
	 * @return Instance of the templates cache
	 */
	private static Map<String, String> cache() {
		if (templateCache == null) {
			templateCache = new Hashtable<String, String>();
		}
		return templateCache;
	}
	
	/** 
	 * This method returns the contents of an file found in the classpath 
	 * @param templateId Template identifier
	 * @return Contents (if available) of the requested template
	 * @throws TemplateException
	 */
	public static String readTemplate(String templateId) throws TemplateException {
		String templateValue = null;
		if (templateId == null || templateId.trim().length() == 0) {
			throw new TemplateException("Template not specified");
		}
		if (!cache().containsKey(templateId)) {
			String path = "templates/" + templateId + ".xml";
			try {
				templateValue = ClasspathFileReader.retrieveFile(path);
				cache().put(templateId, templateValue);
			}
			catch(IOException ioe) {
				throw new TemplateException(ioe);
			}
		}
		else {
			templateValue = cache().get(templateId);
		}
		return templateValue;
	} 
}
