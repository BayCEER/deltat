package de.unibayreuth.bayceer.delta.com;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

import de.unibayreuth.bayceer.delta.utils.ByteUtils;


public class DLConnection {
	
	private SerialCon con;
	private static final Logger logger = Logger.getLogger(DLConnection.class);
	private String device;
	
	private int baudrate;
	
	private final static int WAIT_BETWEEN_REQUESTS = 500;
	
	public DLConnection(String device, int baudrate){
		this.device = device;
		this.baudrate = baudrate;		
	}
	
	public boolean open(){
		logger.debug("open:" + device);
		con = new SerialCon(device,baudrate);
		return con.connect();						
	}

	public void close(){
		sleep();
		con.disconnect();		
	}
	
	
	private byte[] read(int length) throws IOException, DLException {
		logger.debug("read " +  length  + " bytes");
		byte[] ret = new byte[length];
		for (int i=0;i<length;i++){			
			ret[i] = (byte)con.getInputStream().read();
			if (ret[i] == DLProtocolCode.BSY) {
				throw new DLException("Logger is busy");
			}
			logger.debug("read[" +i + "] = " + ret[i]);
		}
		return ret;	
	}
	
	
	public void setBuffer(byte[] in) throws DLException {
		logger.debug("setBuffer");									
		exec(DLInstruction.BufferIn);	
		ok();
		try {									
			write(in);
			write(DLProtocolCode.OK);			
			byte[] ret = read(in.length); 				
			if (read() != DLProtocolCode.OK){
				throw new DLException("Response is invalid. A");
			}			
			if (Arrays.equals(ret,in)){
				ok();
			} else {
				nok();
				throw new DLException("Response is invalid. B");
			}						

		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new DLException("Failed to set buffer");		
		}
		
					
	}
		

	public byte[] query(int command, int bytes) throws DLException {		
		exec(command);		
		byte[] result = read(DLProtocolCode.OK, bytes);
		if (ByteUtils.getInt(result, 1, 2) !=  bytes - 8 ){
			throw new DLException("Wrong response length.");			
		}
		return result;
	}
	
		
	
	public void exec(int command) throws DLException {
		exec(command,command);
	}
		
	public void exec(int expected, int command) throws DLException {
		logger.debug("Write:" + command + " Expected:" + expected);
		write(command);
		int actual = read();		
		if (actual != expected) throw new DLException("Wrong response, expected:" + expected + " was: " + actual);			
	}
	
	
	public void ok() throws DLException {
		logger.debug("ok()");
		try {
		exec(DLProtocolCode.RDY,DLProtocolCode.OK);
		} catch (DLException e){
			logger.warn("Second try of ok");			
			exec(DLProtocolCode.RDY,DLProtocolCode.OK);
		}
	}
	
	public void nok() throws DLException {
		logger.debug("nok()");
		exec(DLProtocolCode.RDY,DLProtocolCode.NOK);				
	}
	
	
	private void write(byte[] in) throws DLException{
		logger.debug("write:" + in.length + " bytes");
		try {			
			for (int i=0;i<in.length;i++){
				logger.debug("write[" + i + "] = " + in[i]);				
				con.getOutputStream().write(in[i]);	
				Thread.sleep(WAIT_BETWEEN_REQUESTS);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DLException("Can't write.");			
		}
	}
	
				
	private void write(int command) throws DLException{
		logger.debug("write:" + command);
		try {
			con.getOutputStream().write(command);
			Thread.sleep(WAIT_BETWEEN_REQUESTS);			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DLException("Can't write command:" + command);			
		}
	}
	
	private int read() throws DLException {		
		int i = 0;		
		int b = 0;
		try {			
			
			while (con.getInputStream().available() > 0){
				i = con.getInputStream().read();
				logger.debug("read(" + b + ") = " + i);
				b++;
			}			
			return i;
			
		} catch (IOException e) {			
			throw new DLException("Can't read from input due:" + e.getMessage());					
		}		
	}
	
	public int readBlock(OutputStream stream) throws DLException, IOException  {
		logger.debug("readBlock()");
		write(DLProtocolCode.OK);
		int r = con.getInputStream().read();
		if (r != DLProtocolCode.RDY){
			throw new DLException("Wrong response expected " + DLProtocolCode.RDY + " was " + r);
		}
		int b = 0;
		int eof = 0;
		int i = 0;
		long chk = 0;
		while ((b = con.getInputStream().read()) != -1){
			i++;
			if (b == 0) { 
				eof++; 
			} else {
				eof = 0;
			}		
			chk = chk + b;
			stream.write(b);
			if (eof == 64) break; 
		}
		logger.debug("writeBlock checksum:" + chk);
		return i;
		
	}
			 
	public byte[] read(int command,int bytes) throws DLException {
		logger.debug("read bytes:" + bytes);
		byte[] ret = new byte[bytes];		
		try {
			write(command);			
			for (int i=0;i<bytes;i++){				
				ret[i] = (byte)con.getInputStream().read();
				logger.debug("response[" + i + "] = " + ret[i]);
			}						
			return ret;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}
				
	}

	public void sleep() {
		try {
			ok();
			exec(DLInstruction.Sleep);
			ok(); // Last Instruction
		} catch (DLException e) {
			logger.error(e.getMessage());
		}
		
		
	}


		
	
	
}
