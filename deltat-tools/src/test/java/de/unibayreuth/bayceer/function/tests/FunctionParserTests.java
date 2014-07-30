package de.unibayreuth.bayceer.function.tests;

import java.io.InputStream;

import org.apache.commons.math.FunctionEvaluationException;

import de.unibayreuth.bayceer.function.FunctionParser;

import junit.framework.TestCase;

public class FunctionParserTests extends TestCase {
  
	public void testParser(){
		String inFile = "/de/unibayreuth/bayceer/interpolate/tests/spline.xml";
		InputStream fin = getClass().getResourceAsStream(inFile);
		FunctionParser p = new FunctionParser();
		p.parse(fin);
		assertNotNull(p.getFunction());
		try {
			assertEquals(4.0, p.getFunction().value(8.006));
		} catch (FunctionEvaluationException e) {
				fail(e.getMessage());
		}
	}
	
}
