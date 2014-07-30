package de.unibayreuth.bayceer.function;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class FunctionParser {
   protected final static Logger logger = Logger.getLogger("FunctionParser.class");      	
   private String id;
   private String name;
   private ArrayDoubleList xl = new ArrayDoubleList();
   private ArrayDoubleList yl = new ArrayDoubleList();
   
   public String getId(){
	 return id;
   }
   
   public UnivariateRealFunction getFunction() {
	   UnivariateRealInterpolator interpolator = new SplineInterpolator();
	   try {
		return interpolator.interpolate(xl.toArray(),yl.toArray());
	} catch (MathException e) {
		logger.error(e.getMessage());
		return null;
	}
   }
   
   public void parse(InputStream in) {	   
	   try {
	   XMLReader parser = XMLReaderFactory.createXMLReader();
	   parser.setContentHandler(new SplineHandler());
	   parser.parse(new InputSource(in));
	   } catch (IOException ioe){
	    logger.error(ioe.getMessage());	   
	   } catch (SAXException se){
		logger.error(se.getMessage());		   
	   }
	   
   }
   
   class SplineHandler extends DefaultHandler  {
	   
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	    if (localName.equalsIgnoreCase("spline")) {
			id = atts.getValue("id");
			name = atts.getValue("name");
		} else if (localName.equals("data")){
			yl.add(Double.valueOf(atts.getValue("y")));
			xl.add(Double.valueOf(atts.getValue("x")));
		}
	}

   }

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}
   
   
}
