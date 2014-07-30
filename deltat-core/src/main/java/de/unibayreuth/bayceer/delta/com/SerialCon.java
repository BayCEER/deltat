package de.unibayreuth.bayceer.delta.com;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class SerialCon {

	private static final Logger log = Logger.getLogger(SerialCon.class);

	private String device;

	private int baudrate;

	public SerialCon(String device, int baudrate) {
		this.device = device;
		this.baudrate = baudrate;
	}

	InputStream in;

	OutputStream out;
	
	CommPortIdentifier portIdentifier;
	
	SerialPort serialPort;

	public boolean connect()  {

		
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(device);
		

		if (portIdentifier.isCurrentlyOwned()) {
			log.error("ComPort is currently in use");
			return false;
			
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 10000);

			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();
			return true;
		}
				
		
		} catch (NoSuchPortException e) {			
			log.error(e.getMessage());
			return false;
		} catch (PortInUseException e) {
			log.error(e.getMessage());
			return false;
		} catch (UnsupportedCommOperationException e) {
			log.error(e.getMessage());
			return false;
		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		}

	}

	public void disconnect() {
		
		if (serialPort!=null){
			serialPort.close();
		}
		
		
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}

	}

	public InputStream getInputStream() {
		return in;
	}

	public OutputStream getOutputStream() {
		return out;
	}

}
