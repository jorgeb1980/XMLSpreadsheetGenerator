/**
 * 
 */
package xml.spreadsheet.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import xml.spreadsheet.templates.TemplateException;

/**
 * This class retrieves files from the application classpath
 */
public class ClasspathFileReader {
	
	//-----------------------------------------------------
	// Class methods
	
	/**
	 * Retrieves (if possible) the content of the resource identified by the
	 * parameter 'path'
	 * @param path Resource path inside the classpath
	 * @return Contents of the resource
	 * @throws IOException In case of any input/output exception
	 * @throws TemplateException If found any error applying the template
	 */
	public static String retrieveFile(String path) 
			throws IOException, TemplateException {
		StringBuilder sb = new StringBuilder();
		
		BufferedReader reader = null;
		
		try {
			InputStream templateStream = ClasspathFileReader.class.getClassLoader().
				getResourceAsStream(path);
			if (templateStream == null) {
				throw new IOException("Unable to find " + path + " template in the classpath");
			}
			reader = new BufferedReader(
				new InputStreamReader(templateStream));
			boolean keepOn = true;
			while (keepOn) {
				String line = reader.readLine();
				keepOn = (line != null);
				if (keepOn) {
					sb.append(line);
				}
			}
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return sb.toString();
	}

}
