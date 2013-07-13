package org.codesandnotes.concordion.ext.dyneval.providers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapBasedValuesProvider_UTest {

	@Test
	public void PutAndRetrieveValuesOneByOne() {
		
		ValuesProvider valuesProvider = new MapBasedValuesProvider();
		valuesProvider.put("one", "1");
		valuesProvider.put("two", "2");
		valuesProvider.put("three", "3");
		
		String result = valuesProvider.get("one") + valuesProvider.get("two") + valuesProvider.get("three");
		assertEquals("unexpected values returned. ", "123", result);
	}
}
