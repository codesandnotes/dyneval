package org.codesandnotes.concordion.ext.dyneval;

import nu.xom.Document;

import org.codesandnotes.concordion.ext.dyneval.providers.MapBasedValuesProvider;
import org.codesandnotes.concordion.ext.dyneval.providers.ValuesProvider;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.DocumentParsingListener;

/**
 * A Concordion extension that evaluates references marked with ${ } found in specification documents.
 */
public class DynEval implements ConcordionExtension {

	private static Evaluator evaluator = null;

	public static void putValue(String key, String value) {
		initEvaluator();
		evaluator.putValue(key, value);
	}

	public static void setValuesProvider(ValuesProvider valuesProvider) {
		evaluator = new Evaluator(valuesProvider);
	}

	private static void initEvaluator() {
		if (evaluator == null) {
			synchronized (DynEval.class) {
				if (evaluator == null)
					evaluator = new Evaluator(new MapBasedValuesProvider());
			}
		}
	}

	public void addTo(ConcordionExtender concordionExtender) {

		concordionExtender.withDocumentParsingListener(new DocumentParsingListener() {

			public void beforeParsing(Document document) {

				initEvaluator();

				evaluator.evaluateChildrenOf(document);
			}
		});
	}
}
