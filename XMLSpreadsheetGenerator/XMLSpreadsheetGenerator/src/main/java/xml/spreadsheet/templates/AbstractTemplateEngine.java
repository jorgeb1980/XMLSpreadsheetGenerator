/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Map;

/**
 * Abstract implementation of the TemplateEngine interface
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

		
	// This method returns the contents of an .xml file
	public String returnTemplate(String templateId) throws TemplateException {
		return TemplateCache.readTemplate(templateId);
	}	

	/* (non-Javadoc)
	 * @see xml.spreadsheet.templates.TemplateEngine#applyTemplate(java.lang.String, java.util.Map)
	 */
	@Override
	public abstract String applyTemplate(String templateId, Map<String, String> values)
			throws TemplateException;

	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * @see xml.spreadsheet.templates.TemplateEngine#applyTemplate(java.lang.String)
	 */
	public String applyTemplate(String templateId) 
			throws TemplateException {
		return applyTemplate(templateId, null);
	}

}
