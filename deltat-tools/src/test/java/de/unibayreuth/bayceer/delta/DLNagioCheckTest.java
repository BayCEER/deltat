package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.nagios.DLNagiosCheck;


public class DLNagioCheckTest extends TestCase {

	public void testMain() {
		String args[] = {"COM1","9600"};		
		DLNagiosCheck.main(args);
		
				
		
	}

}
