package de.unibayreuth.bayceer.delta.interpolation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;



public class InterpolationParser {
	
	protected final static Logger logger = Logger.getLogger(InterpolationParser.class);
	
	private ArrayList<Interpolation> m = new ArrayList<Interpolation>();
	
	public ArrayList<Interpolation> getInterpolations(){		
		return m;
	}
	
	
	public void parse(InputStream in) {	   
		   try {
		   XMLReader parser = XMLReaderFactory.createXMLReader();
		   parser.setContentHandler(new InterpolationHandler());
		   parser.parse(new InputSource(in));
		   } catch (IOException ioe){
		    logger.error(ioe.getMessage());	   
		   } catch (SAXException se){
			logger.error(se.getMessage());		   
		   }
		   
	   }
	
	
	class InterpolationHandler extends DefaultHandler{
		
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		    if (localName.equalsIgnoreCase("interpolation")) {
		    	Interpolation i = new Interpolation();
		    	i.setCode(atts.getValue("code"));
		    	i.setFunctionId(atts.getValue("function"));
		    	i.setUnitIn(atts.getValue("unitIn"));
		    	i.setUnitOut(atts.getValue("unitOut"));
		    	m.add(i);		    	
			} 
		}
		
	}
}
