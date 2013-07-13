package org.codesandnotes.concordion.ext.dyneval.providers;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple, map-based implementation of a values' provider.
 */
public class MapBasedValuesProvider implements ValuesProvider {

	private Map<String, String> values = new HashMap<String, String>();

	public String get(String key) {
		return values.get(key);
	}

	public void put(String key, String value) {
		values.put(key, value);
	}
}
