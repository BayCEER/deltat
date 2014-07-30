package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.com.DLConnection;


public abstract class AbstractConnectionTest extends TestCase {
	
	

	protected DLConnection con;	
	
	private static final String device = "COM1";
	//private static final String device = "/dev/ttyS0";
	
	
	private static final int baudrate = 4800;

	protected void setUp() throws Exception {
		BasicConfigurator.configure();
						  		
		con = new DLConnection(device,baudrate);
		con.open();				
			
	}
	
	

	protected void tearDown() throws Exception {					
		con.close();
	}
	
	

}
