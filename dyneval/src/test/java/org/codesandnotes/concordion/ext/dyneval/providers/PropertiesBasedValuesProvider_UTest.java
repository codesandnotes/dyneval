package org.codesandnotes.concordion.ext.dyneval.providers;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class PropertiesBasedValuesProvider_UTest {

	@Test
	public void AddValuesToTheListLoadedFromThePropertiesFile() throws IOException {

		Class<PropertiesBasedValuesProvider_UTest> clazz = PropertiesBasedValuesProvider_UTest.class;
		URL thisUrl = clazz.getResource(clazz.getSimpleName() + ".class");
		System.out.println(thisUrl.getPath());

		ValuesProvider valuesProvider = new PropertiesBasedValuesProvider(
				"/org/codesandnotes/concordion/ext/dyneval/providers/PropertiesBasedValuesProvider.properties");

		valuesProvider.put("four", "4");

		assertEquals("unexpected value returned for reference 'two'. ", "2", valuesProvider.get("two"));
		assertEquals("unexpected value returned for reference 'four'", "4", valuesProvider.get("four"));
	}

	@Test
	public void ReadValuesFromThePropertiesFile() throws IOException {

		ValuesProvider valuesProvider = new PropertiesBasedValuesProvider(
				"/org/codesandnotes/concordion/ext/dyneval/providers/PropertiesBasedValuesProvider.properties");

		assertEquals("unexpected value returned for reference 'one'. ", "1", valuesProvider.get("one"));
		assertEquals("unexpected value returned for reference 'two'. ", "2", valuesProvider.get("two"));
		assertEquals("unexpected value returned for reference 'three'. ", "3", valuesProvider.get("three"));
	}

	@Test(expected = FileNotFoundException.class)
	public void ThrowFileNotFoundIfClassPathToPropertiesFileIsIncorrect() throws IOException {

		new PropertiesBasedValuesProvider("wrong/path/file.properties");
	}
}
