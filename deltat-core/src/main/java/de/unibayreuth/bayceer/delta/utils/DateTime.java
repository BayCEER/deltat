package de.unibayreuth.bayceer.delta.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime  {
	
   private int month;
   private int day;
   private int hour;
   private int minute;
   private int second;
   
   
	public DateTime(){
		super();		
		
	}
	
	public DateTime(int month, int day, int hour, int minute, int second){
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	
	public java.util.Date getDate(int year){
		return getCalendar(year).getTime();	
	}
	
	public long getLong(int year){
		return getCalendar(year).getTimeInMillis();
	}
	
	public Calendar getCalendar(int year){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,Integer.valueOf(month)-1);
		cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
		cal.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hour));
		cal.set(Calendar.MINUTE,Integer.valueOf(minute));
		cal.set(Calendar.SECOND,Integer.valueOf(second));
		return cal;
	}
	
	
	
	
	public String toString(){
		return String.format(String.format("%02d.%02d %02d:%02d:%02d",day,month,hour,minute,second));		
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

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
		


}



