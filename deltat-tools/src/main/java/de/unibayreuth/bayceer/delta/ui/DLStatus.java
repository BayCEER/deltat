package de.unibayreuth.bayceer.delta.ui;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.com.DLConnection;
import de.unibayreuth.bayceer.delta.com.DLException;
import de.unibayreuth.bayceer.delta.com.DLInstruction;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;


public class DLStatus {
	private String device;
	private int baudrate;
	private static Logger logger = Logger.getRootLogger();
	
	
	public DLStatus(String device, int baudrate){
		this.device = device;
		this.baudrate = baudrate;
		
	}
	
	/**
	 * @param args
	 * @return 
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties"); 
		if (args.length != 2) printusage();		
		DLStatus s = new DLStatus(args[0],Integer.valueOf(args[1]));
		s.retrieve();		
	}
	
		
	private static void printusage() {
		System.out.println("usage: DLStatusCheck <device> <baudrate>");
		System.exit(-1);
	}

	public boolean retrieve(){
		DLConnection con = new DLConnection(device,baudrate);
		byte[] result;
		if (con.open()){
			try {				
				con.ok();				
				result = con.query(DLInstruction.StatusGeneral, 168);
				printStatusGen(result);

				con.ok();
				result = con.query(DLInstruction.StatusData, 128);
				printStatusData(result);

												
			} catch (DLException e) {
				System.out.println(e.getMessage());
				return false;
			} finally {				
				con.close();
			}
		} else {
			System.out.println("failed to open connection");
			return false;
		}				
		return true;
	}
	
	private void printStatusGen(byte[] result){
	   System.out.println("length of array (bytes): " + result.length);		
//         1-2    integer; byte count (A0)
		          System.out.println("byte count: " + ByteUtils.getInt(result, 1, 2));
//		   3-6    integer; 0000		          
//		   7-10   integer; PROM version number
		          System.out.println("PROM version: " + ByteUtils.getInt(result, 7, 10));
//		  11-14   integer; PROM revision number
		          System.out.println("PROM revision: " + ByteUtils.getInt(result, 11, 14));		  		
//		  15-18   integer; battery voltage
//		          bits 0-11 = battery voltage x 409.6
//		          bit 12, 1 => battery voltage > 10 volts
//                0 => battery voltage < 10 volts, calculate as above		

		          float d  = Math.round(ByteUtils.getInt(result, 15, 18)/409.6);		          		          		          
		          if (d > 9.0) {
		        	  System.out.println("battery voltage: >9.0 V");
		          } else {
		        	  System.out.println("battery voltage: " + d + "V");
		          }
//		          		          		 
//		  19-22   integer; logging status
//		          A1B2 => logging
//		          0000 => not logging
		  		  System.out.println("logging status: " + (ByteUtils.getString(result,19,22).equals("A1B2")?"logging":"not logging"));
//		  23-34   3 x integer, compressed format;
//		          RAM installed and allocated to TIMED, TRIG/61, TRIG/62 data
//		  35-46   3 x integer, compressed format; 
//		          number of stored TIMED, TRIG/61, TRIG/62 data
//		  47-50   integer; minimum sampling interval (interval between 
//		          lines of data) for TIMED data:
		  		  
		  		  String res = ByteUtils.getString(result, 47, 50);
		  		  
		  		  String msg;
		  		  switch (Integer.parseInt(res, 16)) {
		  		  case 1: msg =  "1s" ; break;
		  		  case 2: msg =  "5s" ; break;
		  		  case 3: msg =  "10s" ; break;
		  		  case 4: msg =  "30s" ; break;
		  		  case 5: msg =  "1m" ; break;
		  		  case 6: msg =  "5m" ; break;
		  		  case 7: msg =  "10m" ; break;
		  		  case 8: msg =  "30m"; break;
		  		  case 9: msg =  "1h" ; break;
		  		  case 10: msg =  "2h" ; break;
		  		  case 11: msg =  "4h" ; break;
		  		  case 12: msg =  "12h" ; break;
		  		  case 13: msg =  "24h" ; break;
		  		  default:	
					msg = "undefined"; break;
		  		  }
		  		  System.out.println("minimum sampling interval: " + msg);
//		  51-58   2 x integer; 
//		          number of scanned channels for TRIG/61, TRIG/62 data 
//		  59-60   integer; battery failed flag
//		          00 => battery OK
//		          01 => battery failed
		  		  System.out.println( "battery failed flag: " + (ByteUtils.getString(result,59,60).equals("00")?"ok":"failed"));
//		  61-62   integer; memory full flags
//		          bits 0,1,2 => TIMED, TRIG/61, TRIG/62 
//		          0 => memory OK
//		          1 => memory filled		  		  
		  		  System.out.println( "memory full flag: " + (ByteUtils.getString(result, 61, 62).equals("00")?"ok":"false") );
//		  63-70   text; experiment name
		  		  System.out.println("experiment name: " + ByteUtils.getString(result,63,70));		  		  
//		  71-78   text; password
		  		  System.out.println("password: " + ByteUtils.getString(result,71,78));		  		  
//		  79-90   date-time; started logging (if applicable)
		  		  System.out.println("started logging: " + ByteUtils.getDateTime12(result, 79, 90));		  		  
//		  91-102  date-time; stopped logging (if applicable)
		  		  System.out.println("stopped logging: " + ByteUtils.getDateTime12(result, 91, 102));		  		  
//		 103-114  date-time; first stored timed data stored in RAM (if 
//		          applicable)
		  		  System.out.println("first stored timed data: " + ByteUtils.getDateTime12(result, 103, 114));		  		  
//		 115-126  date-time; next timed data to be output (if applicable)
		  		  System.out.println("next timed data to be output: " + ByteUtils.getDateTime12(result, 115, 126));		  		  
//		 127-128  integer; logger's date-time format
//		          00 => European 
//		          01 => US
		  		  System.out.println("date-time format: " +  (ByteUtils.getString(result, 127,128).equals("00")?"European":"US"));
//		 129-130  integer; overwrite mode
//		          00 => disabled
//		          01 => enabled
		  		  System.out.println("overwrite mode: " + (ByteUtils.getString(result, 129, 130).equals("00")?"disabled":"enabled"));
//		 131-142  time: time of next timed data to be logged (date digits are unused)
		  		  System.out.println("next timed data: " + ByteUtils.getTime12(result, 131, 142));
//		 143-146  unused
//		 147-158  date-time; current time
		  		  System.out.println("current time: " + ByteUtils.getDateTime12(result, 147, 158));		  		  
//		 159-162  unused
//		 163-166  integer; checksum		  		  
		          System.out.println("checksum: " + ByteUtils.getInt(result, 163, 166));		          
		          System.out.println("calculated checksum: " + ByteUtils.getCheckSum(result, 1, 162));
		          
		          
	}
	
	
		
	
	
	
	
	
	private void printStatusData(byte[] result){						
//		INSTRUCTION 69 (45h, "E", .DATA.STATUS)
//		 - send data status information.
//		OUTPUT, 126 bytes:
//		   1-2    integer; byte count (78)
		   System.out.println("byte count:" + ByteUtils.getInt(result, 1, 2));
//		   3-26   3 x integer; numbers of stored TIMED, TRIG/61, TRIG/62                     
//		          data
		   
		   int storedRecs = ByteUtils.getInt(result, 3, 10);
		   System.out.println("Number of stored records: " + storedRecs);
		   
		   System.out.println("Date of 1st-stored record: " + ByteUtils.getDateTime12(result, 51, 62));
			 
//		  27-50   3 x integer; numbers of previously output TIMED,                     
//		          TRIG/61, TRIG/62 data
		   System.out.println("Number of records since last retrieved: " + (storedRecs  - ByteUtils.getInt(result, 27, 34)));
//		  51-122  3 x (2 x date-time); date-times of 1st-stored-data and	   
//		          next-data-to-be-output, for TIMED, TRIG/61, TRIG/62 data
//		          (for TRIG/61 and TRIG/62 data, the date-time of next-data-to-
//		          be-output is in fact the date-time of the last data already 
//		          output)

		  System.out.println("Date of next record to-be-output: " + ByteUtils.getDateTime12(result, 63, 74));
//		 123-126  integer; checksum
				
		 
		
	}


	
}