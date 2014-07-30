package de.unibayreuth.bayceer.delta.nagios;

public class PluginCheck {
	private String message;
	private int status;
	
	
	public PluginCheck(String message, int status){
		this.message = message;
		this.status = status;
	}
	
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}

}
