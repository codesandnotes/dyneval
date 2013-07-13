package org.codesandnotes.concordion.ext.dyneval;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.codesandnotes.concordion.ext.dyneval.providers.ValuesProvider;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class Evaluator_UTest {

	private Evaluator evaluator;

	private IMocksControl mocksControl = createStrictControl();

	private ValuesProvider valuesProvider;

	@Before
	public void _init() {

		valuesProvider = mocksControl.createMock(ValuesProvider.class);

		evaluator = new Evaluator(valuesProvider);
	}

	@Test
	public void EvaluateAReference() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test 1, ${two}, 3</p></body></html>");

		expect(valuesProvider.get("two")).andReturn("2");
		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test 1, 2, 3");
	}

	@Test
	public void EvaluateAReferenceAtTheBeginningOfAText() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>${one}, 2, 3, test!</p></body></html>");

		expect(valuesProvider.get("one")).andReturn("1");
		evaluateChildrenOf(document);

		assertDocumentContains(document, "1, 2, 3, test!");
	}

	@Test
	public void EvaluateATextNodeWithAnOpeningMarkupButAnEscapedClosingMarkupByRemovingEscapeCharacters() throws ValidityException, ParsingException,
			IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test ${test\\}</p></body></html>");

		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test ${test}");
	}

	@Test
	public void EvaluateMultipleReferencesWithingTheSameText() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>${one}, ${two}, ${one}, ${two}, check it out!</p></body></html>");

		expect(valuesProvider.get("one")).andReturn("1");
		expect(valuesProvider.get("two")).andReturn("2");
		expect(valuesProvider.get("one")).andReturn("1");
		expect(valuesProvider.get("two")).andReturn("2");
		evaluateChildrenOf(document);

		assertDocumentContains(document, "1, 2, 1, 2, check it out!");
	}

	@Test
	public void LeaveABlankReferenceAsItIs() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test ${}</p></body></html>");

		expect(valuesProvider.get("")).andReturn(null);
		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test ${}");
	}

	@Test
	public void LeaveAnUnknownReferenceAsItIs() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test ${unknownReference}</p></body></html>");

		expect(valuesProvider.get("unknownReference")).andReturn(null);
		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test ${unknownReference}");
	}

	@Test
	public void LeaveATextWithAClosingMarkupButNoOpeningMarkupAsItIs() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test test}</p></body></html>");

		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test test}");
	}

	@Test
	public void LeaveATextWithAnOpeningMarkupButNoClosingMarkupAsItIs() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test ${test</p></body></html>");

		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test ${test");
	}

	@Test
	public void RemoveEscapeCharactersWhenEvaluatingATextThatBeginsWithAnEscapedMarkup() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>\\${escapedMarkup\\} test!</p></body></html>");

		evaluateChildrenOf(document);

		assertDocumentContains(document, "${escapedMarkup} test!");
	}

	@Test
	public void RemoveEscapeCharactersWhenEvaluatingATextThatContainsAnEscapedReference() throws ValidityException, ParsingException, IOException {

		Document document = buildDocumentFrom("<html><body><h1>Header</h1><p>Test \\${escapedMarkup\\}</p></body></html>");

		evaluateChildrenOf(document);

		assertDocumentContains(document, "Test ${escapedMarkup}");
	}

	private void assertDocumentContains(Document document, String text) {
		String xmlText = document.toXML();
		assertTrue("the document did not contain the expected text '" + text + "': [" + xmlText + "]. ", xmlText.contains(text));
	}

	private Document buildDocumentFrom(String textDocument) throws ParsingException, ValidityException, IOException {
		Builder builder = new Builder(false);
		Document document = builder.build(textDocument, null);
		return document;
	}

	private void evaluateChildrenOf(Document document) {
		mocksControl.replay();
		evaluator.evaluateChildrenOf(document);
		mocksControl.verify();
	}
}
