package de.unibayreuth.bayceer.delta.nagios;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.com.DLConnection;
import de.unibayreuth.bayceer.delta.com.DLException;
import de.unibayreuth.bayceer.delta.com.DLInstruction;
import de.unibayreuth.bayceer.delta.com.DLLogger;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;


public class DLNagiosNSCA {
	private static Logger logger = Logger.getRootLogger();
	private String device;
	private int baudrate;
	private String name;
		
	public DLNagiosNSCA(String device, int baudrate, String name) {
		this.device = device;
		this.baudrate = baudrate;
		this.name = name;
	}



	public static void main(String args[]) throws Exception {
		if (args.length != 3) printusage();
		String device = args[0];
		int baudrate = Integer.valueOf(args[1]);
		String name = args[2];		
		System.setProperty("user.timezone","GMT+1");	 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		PropertyConfigurator.configure("log4j.properties");		
		logger.info("Query Status of Logger " + device);		
		DLNagiosNSCA p = new DLNagiosNSCA(device,baudrate,name);
		p.checkLogger();
		
		logger.info("DLStatus stopped.");		
	}
	
	
	
	
	
	
	private static void printusage() {
		System.out.println("usage: DLtoNagios <device> <baudrate> <name>");
		System.exit(-1);
		
	}



	private void sendMsg(String svc_description, int return_code, String plugin_out)  {		
		String cmd = "sh sendmsg.sh " + svc_description + " " + return_code + " " + plugin_out;
		logger.info(cmd);
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
					
	}


	public void checkLogger()  {
		logger.info("Retrieving status.");		
		DLConnection con = new DLConnection(this.device,this.baudrate);
		if (con.open()){
			try {								
				con.ok();
				byte[] result = con.query(DLInstruction.StatusGeneral, 168);
				DLLogger l= new DLLogger();
				l.setLogging(ByteUtils.getString(result,19,22).equals("A1B2"));
				l.setCurrentDate(ByteUtils.getDateTime12(result,147,158));
				//l.setBatteryVoltage(Math.round(ByteUtils.getInt(result, 15, 18)/409.6));				
				sendMsg(name,l.getStatusCode(),l.getStatusMessage());					
			} catch (DLException e) {
				logger.error(e.getMessage());
				sendMsg(name,3,"unknown");								
			} finally {				
				con.close();
			}
		} else {
			logger.error("open failed");
			sendMsg(name,3,"unknown");
		}						
		
	}
}
