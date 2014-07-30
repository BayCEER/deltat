package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.ui.Bin2Csv;


public class Bin2CsvTest extends TestCase {

	
//	// A standard file 
	public void testRun1() {				
		String args[] = {"tests/files/coul2_A3.bin","tests/files/coul2_A3.csv","2010"};
		Bin2Csv.main(args);
		assertTrue(true);
	}
	
	// A file with is spanning two years 
	public void testRun2() {				
//		String args[] = {"tests/files/T070102R.BIN","tests/files/T070102R.CSV","1999"};		
//		Bin2Csv.main(args);	
//		assertTrue(true);
	}
}
