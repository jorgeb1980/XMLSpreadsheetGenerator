/**
 * 
 */
package xml.spreadsheet.templates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * Trivial implementation of an .xml template engine.  Based on .xml text files
 * and MessageFormat
 */
/**
 * @author Jorge
 *
 */
/**
 * @author Jorge
 *
 */
public class FileTemplateEngine implements TemplateEngine {

	
	//---------------------------------------------------
	// Class properties
	
	/** Template cache */
	private Map<String, String> templateCache;
	
	//---------------------------------------------------
	// Class methods
	
	// To prevent instantiation
	private FileTemplateEngine() {
		templateCache = new Hashtable<String, String>();
	}

	
	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * The values is expected to be an array of Objects in order for MessageFormat
	 * to use it
	 * @see xml.spreadsheet.templates.TemplateEngine#applyTemplate(java.lang.String, java.lang.Object)
	 */
	public String applyTemplate(String templateId, Object values) {
		if (values != null && !(values instanceof Object[])) {
			throw new IllegalArgumentException(
				"Values should be an Object[] for this template engine implementation");
		}
		String templateValue = returnTemplate(templateId);
		return MessageFormat.format(templateValue, (Object[]) values);
	}
	
	/* 
	 * In this case, we map templateId to a full path in the classpath.  The way
	 * to do so is:
	 * Classpath route -> templates/{templateId}.xml
	 * @see xml.spreadsheet.templates.TemplateEngine#returnTemplate(java.lang.String)
	 */
	public String returnTemplate(String templateId) {
		String templateValue = null;
		if (!templateCache.containsKey(templateId)) {
			templateValue = readTemplate(templateId);	
		}
		else {
			templateValue = templateCache.get(templateId);
		}
		return templateValue;		
	}
	
	// This method returns the contents of an .xml file
	private String readTemplate(String templateId) {
		String path = "templates/" + templateId + ".xml";
		StringBuilder sb = new StringBuilder();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(
				new InputStreamReader(FileTemplateEngine.class.getClassLoader().
					getResourceAsStream(path)));
			boolean keepOn = true;
			while (keepOn) {
				String line = reader.readLine();
				keepOn = (line != null);
				if (keepOn) {
					sb.append(line);
				}
			}
		}
		catch (IOException ioe) {
			throw new IllegalArgumentException(ioe);
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		return sb.toString();
	}

	static {
		TemplateEngineFactory.registerEngine("FileTemplateEngine", 
				new FileTemplateEngine());
	}
}
