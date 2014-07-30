package de.unibayreuth.bayceer.function.tests;

import java.util.Hashtable;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import de.unibayreuth.bayceer.function.FunctionFactory;

import junit.framework.TestCase;

public class FunctionFactoryTest extends TestCase {
	
	public void testHashTable(){
		
		FunctionFactory.configure("functions");
		Hashtable<String, UnivariateRealFunction> hash = FunctionFactory.getFunctions();
		assertEquals(2, hash.size());
		assertTrue(hash.containsKey("B"));
		assertTrue(hash.containsKey("b"));
		try {
			assertEquals(28.0, hash.get("B").value(2.633));
		} catch (FunctionEvaluationException e) {
			fail(e.getMessage());
		}
	}

}
