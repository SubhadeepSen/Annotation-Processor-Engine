package sdp.processor;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The AnnotatedClassLoader loads the available classes from a particular
 * package.
 * 
 * @author Subhadeep Sen
 */
@SuppressWarnings("rawtypes")
public class AnnotatedClassLoader {

	@SuppressWarnings("deprecation")
	public List<Class> getClasses(ClassLoader classLoader, String packageName) throws Exception {
		// converting the dotted package to URL path [sdp.controller --> sdp/controller]
		String urlPath = packageName.replaceAll("[.]", "/");
		List<Class> classes = new ArrayList<Class>();
		// get the complete path of that package
		URL upackage = classLoader.getResource(urlPath);

		// contains all the available file names
		DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
		String line = null;
		// reading file name one by one and checking if the file contains .class
		while ((line = dis.readLine()) != null) {
			if (line.endsWith(".class")) {
				// if it contains .class then adding it to classes list

				classes.add(Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.'))));
			}
		}
		// returning list of available classes in the mentioned package
		return classes;
	}
}
