package org.codesandnotes.concordion.ext.dyneval.specs;

import org.codesandnotes.concordion.ext.dyneval.DynEval;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@Extensions(DynEval.class)
@RunWith(ConcordionRunner.class)
public class ConcordionContext {

	public ConcordionContext() {

		System.setProperty("concordion.output.dir", "specs");
	}
}
