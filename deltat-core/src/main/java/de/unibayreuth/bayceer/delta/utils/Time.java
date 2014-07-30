package de.unibayreuth.bayceer.delta.utils;

public class Time {
	   int hour;
	   int minute;
	   int second;
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	
	public String toString(){
		return String.format(String.format("%02d:%02d:%02d",hour,minute,second));		
	}
}
