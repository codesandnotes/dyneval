package org.codesandnotes.concordion.ext.dyneval.providers;

/**
 * Classes implementing this interface are able to store and retrieve values.
 */
public interface ValuesProvider {

	/**
	 * Returns the value matching the specified key, or <i>null</i> if no value matching that key was found.
	 */
	String get(String key);

	/**
	 * Stores the specified value with the specified key.
	 */
	void put(String key, String value);
}
