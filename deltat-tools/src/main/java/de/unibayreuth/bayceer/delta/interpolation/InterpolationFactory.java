package de.unibayreuth.bayceer.delta.interpolation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.log4j.Logger;

import de.unibayreuth.bayceer.function.FunctionFactory;

public class InterpolationFactory {
	
	
	 private static Map<String,Interpolation> map = new HashMap<String, Interpolation>(10);
	  
	 protected final static Logger logger = Logger.getLogger(InterpolationFactory.class);
	   
	 private InterpolationFactory(){
		 
	 }
	 
	 
	 public static void configure() throws FileNotFoundException {
		 
		 InterpolationParser p = new InterpolationParser();
		 p.parse(new FileInputStream("./interpolations.xml"));		 		 
		 List<Interpolation>interpolations = p.getInterpolations();
		 
		 FunctionFactory.configure("functions");
		 Hashtable<String, UnivariateRealFunction> hash = FunctionFactory.getFunctions();
		 
		 for (Interpolation interpolation : interpolations) {
			UnivariateRealFunction f = hash.get(interpolation.functionId);			
			if (f == null) {
				logger.warn("Function with id " + interpolation.getFunctionId() + " not found.");
			} else {				
				interpolation.setFunction(f);
				map.put(interpolation.code + ":" + interpolation.getUnitIn(), interpolation);
			}
		 }	 
		 
	 }
	 
	 
	 public static Map<String,Interpolation> getMap(){		 		 
	     return map;
     }

}
