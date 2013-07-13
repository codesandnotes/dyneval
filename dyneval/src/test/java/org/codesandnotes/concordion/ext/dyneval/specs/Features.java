package org.codesandnotes.concordion.ext.dyneval.specs;

import org.codesandnotes.concordion.ext.dyneval.DynEval;

public class Features extends ConcordionContext {

	public Features() {
		DynEval.putValue("hi", "Hi there!");
		DynEval.putValue("one", "1");
		DynEval.putValue("simpleReference", "Hello, simple reference!");
		DynEval.putValue("three", "3");
		DynEval.putValue("two", "2");
	}
	
	public String display(String text) {
		return text;
	}
}
