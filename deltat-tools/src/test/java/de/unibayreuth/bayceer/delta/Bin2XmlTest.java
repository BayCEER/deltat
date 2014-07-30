package de.unibayreuth.bayceer.delta;



import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.ui.Bin2Xml;


public class Bin2XmlTest extends TestCase {

	
//	public void testRun1() {				
//		String args[] = {"tests/files/coul2_A3.bin","tests/files/coul2_A3.xml"};		
//		Bin2Xml.main(args);				
//	}
	
	// A file with is spanning two years 
	public void testRun2() {				
		String args[] = {"tests/files/78041.bin","tests/files/78041.xml"};		
		Bin2Xml.main(args);				
	}
}
