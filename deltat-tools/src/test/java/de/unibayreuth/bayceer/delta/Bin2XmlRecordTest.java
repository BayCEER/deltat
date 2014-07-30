package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.ui.Bin2XmlRecord;


public class Bin2XmlRecordTest extends TestCase {

	
	public void testRun() {				
		String args[] = {"C://Users//oliver//Documents//bins//A4100330.BIN","C://Users//oliver//Documents//bins//A4100330.XML","2010"};		
		Bin2XmlRecord.main(args);					
	}
	
	public void testRun2() {				
		String args[] = {"C://Users//oliver//Documents//bins//A4100414.BIN","C://Users//oliver//Documents//bins//A4100414.XML","2009"};		
		Bin2XmlRecord.main(args);					
	}

}
