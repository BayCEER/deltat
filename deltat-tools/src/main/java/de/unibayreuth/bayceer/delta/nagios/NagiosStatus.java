package de.unibayreuth.bayceer.delta.nagios;

public enum NagiosStatus {
	OK(0), WARN(1), CRITICAL(2), UNKNOWN(3);
	
	private int code; 
	
	public int code() {
		return code;
	}
	
	private NagiosStatus(int code) {
		this.code = code;
	}
		
	
}
