/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Map;

/**
 * 
 */
public interface TemplateEngine {

	/**
	 * Returns a template identified by the parameter
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @return Value of the template without any tampering
	 */
	String returnTemplate(String templateId) throws TemplateException;
	
	/**
	 * Returns the value of the template, after making the proper substitutions
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @param values Substitutions to apply (key -> value)
	 * @return Value of the template after making the proper substitutions
	 */
	String applyTemplate(String templateId, Map<String, String> values) throws TemplateException;
	
}
