package org.codesandnotes.concordion.ext.dyneval;

import nu.xom.Document;

import org.codesandnotes.concordion.ext.dyneval.providers.MapBasedValuesProvider;
import org.codesandnotes.concordion.ext.dyneval.providers.PropertiesBasedValuesProvider;
import org.codesandnotes.concordion.ext.dyneval.providers.ValuesProvider;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.DocumentParsingListener;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
/**
 * A Concordion extension that evaluates references marked with ${ } found in specification documents.
 * 
 * @author Diego Pappalardo
 */
public class DynEval implements ConcordionExtension {

	private static Evaluator evaluator = null;

	public void addTo(ConcordionExtender concordionExtender) {

		concordionExtender.withDocumentParsingListener(new DocumentParsingListener() {

			public void beforeParsing(Document document) {

				initEvaluator();

				evaluator.evaluateChildrenOf(document);
			}
		});
	}

	/**
	 * Adds a reference to the list of references used by DynEval for parsing specification documents.
	 * 
	 * @param key
	 *            The key that identifies the reference to put.
	 * @param value
	 *            The value associated to the put reference.
	 */
	public static void putValue(String key, String value) {
		initEvaluator();
		evaluator.putValue(key, value);
	}

	/**
	 * Sets the values' provider to be used by DynEval to retrieve references' values.
	 * 
	 * @param valuesProvider
	 *            The ValuesProvider implementation to be used by DynEval to retrieve refreences' values.
	 * 
	 * @see MapBasedValuesProviderF
	 * @see PropertiesBasedValuesProvider
	 */
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
}
