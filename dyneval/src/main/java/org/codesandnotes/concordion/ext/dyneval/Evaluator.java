package org.codesandnotes.concordion.ext.dyneval;

import nu.xom.Node;
import nu.xom.Text;

import org.codesandnotes.concordion.ext.dyneval.providers.ValuesProvider;

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
 * This class is in charge of evaluating the references in a node to replace them with matching values.
 * 
 * @author Diego Pappalardo
 */
public class Evaluator {

	private static final String ESCAPED_MARKUP_CLOSING = "\\}";

	private static final String ESCAPED_MARKUP_OPENING = "\\${";

	private static final String MARKUP_CLOSING = "}";

	private static final String MARKUP_OPENING = "${";

	private ValuesProvider valuesProvider;

	/**
	 * The default constructor for this class.
	 * 
	 * @param valuesProvider
	 *            The ValuesProvider implementation this Evaluator will use to retrieve values while evaluating a Node's
	 *            references.
	 */
	public Evaluator(ValuesProvider valuesProvider) {
		this.valuesProvider = valuesProvider;
	}

	/**
	 * Recursively evaluates the references in the specified node's children.
	 */
	public void evaluateChildrenOf(Node node) {

		for (int position = 0; position < node.getChildCount(); position++) {

			Node child = node.getChild(position);

			if (child instanceof Text) {

				Text text = (Text) child;
				String textValue = text.getValue();

				Indexes indexes = null;
				do {

					indexes = findParseableIndexes(textValue, indexes);

					if (indexes != null) {

						String key = extractKey(textValue, indexes);
						String value = valuesProvider.get(key);

						if (value != null)
							textValue = replaceReferenceWithValue(textValue, indexes, value);
					}

				} while (indexes != null);

				String parsedTextValue = parseEscapedCharacters(textValue);

				text.setValue(parsedTextValue);
			}

			if (child.getChildCount() > 0)
				evaluateChildrenOf(child);
		}
	}

	/**
	 * Adds a reference to the list of references used evaluate a Node.
	 */
	public void putValue(String key, String value) {
		valuesProvider.put(key, value);
	}

	private String extractKey(String text, Indexes indexes) {
		return text.substring(indexes.beginIndex + MARKUP_OPENING.length(), indexes.endIndex);
	}

	private Indexes findParseableIndexes(String text, Indexes previousIndexes) {

		int searchPosition = 0;
		if (previousIndexes != null)
			searchPosition = previousIndexes.beginIndex + 1;

		int markupOpeningIndex = text.indexOf(MARKUP_OPENING, searchPosition);

		Indexes indexes = null;

		if (markupOpeningIndex > -1) {

			if (markupOpeningIndex >= 0) {

				// Might that actually be an escaped opening markup?
				int openingLengthDifference = ESCAPED_MARKUP_OPENING.length() - MARKUP_OPENING.length();
				int escapedMarkupOpeningPosition = text.indexOf(ESCAPED_MARKUP_OPENING, markupOpeningIndex
						- openingLengthDifference);

				if (escapedMarkupOpeningPosition < 0
						|| escapedMarkupOpeningPosition >= (markupOpeningIndex + MARKUP_OPENING.length())) {

					int markupClosingIndex = text.indexOf(MARKUP_CLOSING, markupOpeningIndex);

					if (markupClosingIndex >= 0) {

						// Might that actually be an escaped closing markup?
						int closingLengthDifference = ESCAPED_MARKUP_CLOSING.length() - MARKUP_CLOSING.length();
						int escapedMarkupClosingPosition = text.indexOf(ESCAPED_MARKUP_CLOSING, markupClosingIndex
								- closingLengthDifference);

						if (escapedMarkupClosingPosition < 0
								|| escapedMarkupClosingPosition >= (markupClosingIndex + MARKUP_CLOSING.length())) {

							indexes = new Indexes(markupOpeningIndex, markupClosingIndex);
						}
					}
				}
			}
		}

		return indexes;
	}

	private String parseEscapedCharacters(String text) {
		text = text.replace(ESCAPED_MARKUP_OPENING, MARKUP_OPENING);
		text = text.replace(ESCAPED_MARKUP_CLOSING, MARKUP_CLOSING);
		return text;
	}

	private String replaceReferenceWithValue(String text, Indexes indexes, String value) {
		return new StringBuilder().append(text.substring(0, indexes.beginIndex)).append(value)
				.append(text.substring(indexes.endIndex + 1)).toString();
	}

	private class Indexes {

		public int beginIndex;

		public int endIndex;

		public Indexes(int beginIndex, int endIndex) {
			this.endIndex = endIndex;
			this.beginIndex = beginIndex;
		}
	}
}
