/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Map;

/**
 * Trivial implementation of an .xml template engine.  Based on .xml text files
 * and regex pattern replacement
 */
public class FileTemplateEngine implements TemplateEngine {

	
	//---------------------------------------------------
	// Class methods
	
	// Must be instantiated by reflection
	public FileTemplateEngine() {
	}

	
	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * The values is expected to be an array of Objects in order for MessageFormat
	 * to use it
	 * @see xml.spreadsheet.templates.TemplateEngine#applyTemplate(java.lang.String, java.lang.Object)
	 */
	public String applyTemplate(String templateId, Map<String, String> values) 
			throws TemplateException {
		String templateValue = returnTemplate(templateId);
		// 'Naive' replacement of variables
		if (values != null) {
			for (String key: values.keySet()) {
				templateValue = templateValue.replaceAll("\\$" + key, values.get(key));
			}		
		}
		return templateValue;
	}
	
	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * @see xml.spreadsheet.templates.TemplateEngine#returnTemplate(java.lang.String)
	 */
	public String returnTemplate(String templateId) throws TemplateException {
		return TemplateCache.readTemplate(templateId);
	}	
}
