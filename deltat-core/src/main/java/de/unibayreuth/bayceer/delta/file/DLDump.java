package de.unibayreuth.bayceer.delta.file;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.com.DLConnection;
import de.unibayreuth.bayceer.delta.com.DLException;
import de.unibayreuth.bayceer.delta.com.DLInstruction;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;

public class DLDump {
	private Logger logger = Logger.getRootLogger();
	boolean full;
	int baudrate;
	String filePath;
	DLConnection con;	
	private String device;
	
	public static void main(String[] args) {
		
		System.setProperty("user.timezone","GMT+1");	 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		
		PropertyConfigurator.configure("log4j.properties");        
		if (args.length != 4){
			printUsage();
			return;
		}
		
		String device = args[0];
		int baudrate = Integer.valueOf(args[1]);
		String path = args[2];
		String mode = args[3]; 
		
		boolean full;		
		if (mode.equalsIgnoreCase("full")){
			full = true;
		} else if (mode.equalsIgnoreCase("new")){
			full = false;
		} else {
			printUsage();
			return;
		}
				
		DLDump s = new DLDump(path,full,baudrate,device);
		s.run();		
	}
	
			
	public static void printUsage() {
		System.out.println("usage: DLDump <device> <baudrate> <file> <new|full>");
	}


	public DLDump(String filePath, boolean full, int baudrate, String device){
		this.filePath = filePath;
		this.full = full;
		this.baudrate = baudrate;
		this.device = device;
	}
	
	public boolean run(){
		logger.info("Dump");		
		con = new DLConnection(device,baudrate);
		if (con.open()){
			try {		
				
				int nrec = getRecordsCount();
												
				if (nrec == 0) return true;
				logger.info("Dumping " + nrec + " records.");
				
				// 106
				con.ok();
				con.setBuffer("0000".getBytes());		
				con.exec(106);						
			
				// 84
				if (full){
						con.ok();
						con.exec(84);
				}				
				// 97
				con.ok();			
				StringBuffer b = new StringBuffer("0001");   
				String s = String.format("%1$08d", nrec);  
				con.setBuffer(b.append(s).toString().getBytes());
				con.exec(97);		

				// 98 
				FileOutputStream fout = null;				
				int wb = 0;
				try {
					fout = new FileOutputStream(filePath);
					con.ok();			
					con.exec(98);
					wb = con.readBlock(fout);
					logger.info(wb + " bytes written to file.");																			
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
					return false;
				
				} catch (IOException e) {
					logger.error(e.getMessage());
					return false;
				} finally {
					try {
						fout.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
						return false;
					}
				}
								
			} catch (DLException e) {
				logger.error(e.getMessage());
				return false;
			} finally {				
				con.close();
			}
		} else {
			logger.error("open failed");
			return false;
		}				
		return true;
	}
	
		


	private int getRecordsCount() throws DLException {
		logger.debug("Query status data:");
		con.ok();
		byte[] result = con.query(DLInstruction.StatusData, 128);
		logger.info("Date of 1st-stored record:" + ByteUtils.getDateTime12(result, 51, 62));
		logger.info("Date of next record to-be-output:" + ByteUtils.getDateTime12(result, 63, 74));						 
		if (ByteUtils.getInt(result, 1, 2) != 120) throw new DLException("Byte count error");				
		int recStored = ByteUtils.getInt(result, 3, 10);
		int recIncrement = recStored  - ByteUtils.getInt(result, 27, 34);
	    logger.info("Number of stored records:" + recStored);
		logger.info("Number of records since last retrieved:" + recIncrement);
		
		if (recStored == 0) {
			logger.info("No records available.");
			return 0;
		}
		
		if (!full && recIncrement == 0){
			logger.info("No new records available.");
			return 0;
		}		
		if (full) {
			return recStored;
		} else {
			return recIncrement;
		}
				
	}


	
	
}