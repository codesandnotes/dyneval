package org.codesandnotes.concordion.ext.dyneval.specs;

import java.io.IOException;

import org.codesandnotes.concordion.ext.dyneval.DynEval;
import org.codesandnotes.concordion.ext.dyneval.providers.PropertiesBasedValuesProvider;

public class Setup extends ConcordionContext {

	public Setup() throws IOException {

		DynEval.setValuesProvider(new PropertiesBasedValuesProvider("/org/codesandnotes/concordion/ext/dyneval/test.properties"));

		DynEval.putValue("one", "1");
		DynEval.putValue("two", "2");
	}

	public String display(String text) {
		return text;
	}
}
