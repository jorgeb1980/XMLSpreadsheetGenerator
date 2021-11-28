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
	 * @param values Substitutions to apply (key -&gt; value)
	 * @return Value of the template after making the proper substitutions
	 * @throws TemplateException If found any error (no template found, etc.)
	 */
	String applyTemplate(String templateId, Map<String, Object> values) throws TemplateException;
	
	/**
	 * Returns the value of the template
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @return Value of the template 
	 * @throws TemplateException If found any error (no template found, etc.)
	 */
	String applyTemplate(String templateId) throws TemplateException;
	
}
