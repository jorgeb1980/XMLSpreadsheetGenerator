/**
 * 
 */
package xml.spreadsheet.templates;

import java.util.Map;

/**
 * Interface for all the possible template engine implementations that the
 * library could support.
 */
public interface TemplateEngine {

	
	/**
	 * Returns the value of the template, after making the proper substitutions
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @param values Substitutions to apply (key -> value)
	 * @return Value of the template after making the proper substitutions
	 */
	String applyTemplate(String templateId, Map<String, String> values) throws TemplateException;
	
	/**
	 * Returns the value of the template
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @return Value of the template 
	 */
	String applyTemplate(String templateId) throws TemplateException;
	
}
