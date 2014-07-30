package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.file.DLDump;


public class DLDumpTest extends TestCase {

	
	public void testRun() {				
		String args[] = {"COM1","9600","./dump.bin","full"};		
		DLDump.main(args);				
	}

}
