package de.unibayreuth.bayceer.delta.nagios;

import java.util.TimeZone;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.unibayreuth.bayceer.delta.com.DLConnection;
import de.unibayreuth.bayceer.delta.com.DLException;
import de.unibayreuth.bayceer.delta.com.DLInstruction;
import de.unibayreuth.bayceer.delta.com.DLLogger;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;
import de.unibayreuth.bayceer.delta.utils.DateTime;


public class DLNagiosCheck {
	
	
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.OFF);
		
		System.setProperty("user.timezone","GMT+1");	 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		
		if (args.length != 2){			
			exit(new PluginCheck("Wrong number of arguments.",NagiosStatus.UNKNOWN.code()));
		}		
		DLNagiosCheck c = new DLNagiosCheck();
		exit(c.check(args[0],Integer.valueOf(args[1])));		
	}

	private static void exit(PluginCheck p) {
		System.out.println(p.getMessage());		
		System.exit(p.getStatus());				
	}


	private PluginCheck check(String device, int baudrate) {
		
		DLConnection con = new DLConnection(device,baudrate);
		if (con.open()){
			try {								
				con.ok();
				byte[] result = con.query(DLInstruction.StatusGeneral, 168);				
				DLLogger l= new DLLogger();
				l.setLogging(ByteUtils.getString(result,19,22).equals("A1B2"));
				DateTime d = ByteUtils.getDateTime12(result,147,158);				
				l.setCurrentDate(d);				
				return new PluginCheck(l.getStatusMessage(), l.getStatusCode());									
			} catch (DLException e) {
				return new PluginCheck("Device " + device + " error:" + e.getMessage(), NagiosStatus.UNKNOWN.code());												
			} finally {				
				con.close();
			}
		} else {
			return new PluginCheck("Couldn't open " + device, NagiosStatus.UNKNOWN.code());			
		}			
	}

}
