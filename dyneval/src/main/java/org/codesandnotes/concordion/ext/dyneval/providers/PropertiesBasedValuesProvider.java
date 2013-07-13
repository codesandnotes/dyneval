package org.codesandnotes.concordion.ext.dyneval.providers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesBasedValuesProvider implements ValuesProvider {

	private Properties properties = new Properties();

	public PropertiesBasedValuesProvider(String classPathToFile) throws IOException {

		URL resource = getResourceAsURL(classPathToFile);

		loadProperties(resource);
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public void put(String key, String value) {
		properties.setProperty(key, value);
	}

	private URL getResourceAsURL(String classPathToFile) throws FileNotFoundException {

		URL resource = PropertiesBasedValuesProvider.class.getResource(classPathToFile);

		if (resource == null)
			throw new FileNotFoundException("Could not find '" + classPathToFile + "' ");

		return resource;
	}

	private void loadProperties(URL resource) throws IOException {
		InputStream inputStream = resource.openStream();
		try {
			properties.load(inputStream);
		} finally {
			inputStream.close();
		}
	}

}
