/**
 * 
 */
package xml.spreadsheet.templates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class retrieves files from the application classpath
 */
class ClasspathFileReader {
	
	//-----------------------------------------------------
	// Class methods
	
	/**
	 * Retrieves (if possible) the content of the resource identified by the
	 * parameter 'path'
	 * @param path Resource path inside the classpath
	 * @return Contents of the resource
	 * @throws IOException
	 */
	public static String retrieveFile(String path) 
			throws IOException {
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
		finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return sb.toString();
	}

}
