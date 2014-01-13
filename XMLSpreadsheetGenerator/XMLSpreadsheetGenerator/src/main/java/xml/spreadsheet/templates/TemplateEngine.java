/**
 * 
 */
package xml.spreadsheet.templates;

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
	String returnTemplate(String templateId);
	
	/**
	 * Returns the value of the template, after making the proper substitutions
	 * @param templateId Template identifier according to template engine
	 * rules
	 * @param values Object with the values to substitute in the template, being
	 * whatever needed by the engine definition (String[], Map[], etc.)
	 * @return Value of the template after making the proper substitutions
	 */
	String applyTemplate(String templateId, Object values);
	
}
