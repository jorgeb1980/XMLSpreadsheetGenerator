/**
 * 
 */
package xml.spreadsheet.templates;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Velocity implementation of an .xml template engine.  Based on .xml text files
 * and Velocity
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {

	
	//---------------------------------------------------
	// Class methods
	
	// Must be instantiated by reflection
	public VelocityTemplateEngine() {
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
		VelocityContext context = new VelocityContext();
		if (values != null) {
			for (String key: values.keySet()) {
				context.put(key, values.get(key));
			}
		}
		StringWriter writer = new StringWriter();
		try {
			Velocity.evaluate(context, writer, templateId, templateValue);
		}
		catch(IOException ioe) {
			throw new TemplateException(ioe);
		}
		return writer.toString();
	}
	
	
}
