package de.unibayreuth.bayceer.delta.com;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.unibayreuth.bayceer.delta.utils.DateTime;






public class DLLogger {
	private String name;
	private int port;
	boolean enabled;
	int baudrate;
	DateTime currentDate;
	boolean logging;
		
	private final static int ALLOWED_DRIFT = 300;
	
	
	boolean isCriticalTimeshift(){
		return getTimeshift() > ALLOWED_DRIFT;
	}
	
	
	private long getTimeshift(){
		Calendar cal = new GregorianCalendar();	
		// Fix for NGW100 Date error under JamVM
		cal.add(Calendar.HOUR, 15 * 24);
		int year = cal.get(Calendar.YEAR);
		return Math.abs(currentDate.getDate(year).getTime() - cal.getTimeInMillis())/1000;		
	}
	
	
	
	public int getBaudrate() {
		return baudrate;
	}
	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public DateTime getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(DateTime currentDate) {
		this.currentDate = currentDate;
	}
	public boolean isLogging() {
		return logging;
	}
	public void setLogging(boolean logging) {
		this.logging = logging;
	}
	
	public String getStatusMessage(){
		StringBuffer b = new StringBuffer();
		b.append("Logging:");
		b.append(isLogging()?"started":"stopped");
		b.append("; timeshift[sec]:" + getTimeshift());							
		return b.toString();
	}


	public int getStatusCode() {		
		if (!isLogging() || isCriticalTimeshift()) {
			return 2;
		} else {
			return 0;
		}		
	}
}
