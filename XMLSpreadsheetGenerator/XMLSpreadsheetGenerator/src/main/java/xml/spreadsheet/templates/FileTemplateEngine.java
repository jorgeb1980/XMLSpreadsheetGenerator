/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Map;

/**
 * Trivial implementation of an .xml template engine.  Based on .xml text files
 * and regex pattern replacement
 */
public class FileTemplateEngine extends AbstractTemplateEngine {

	
	//---------------------------------------------------
	// Class methods
	
	// Must be instantiated by reflection
	public FileTemplateEngine() {
	}

	
	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * @see xml.spreadsheet.templates.TemplateEngine#applyTemplate(java.lang.String, java.util.Map)
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
	
	
}
