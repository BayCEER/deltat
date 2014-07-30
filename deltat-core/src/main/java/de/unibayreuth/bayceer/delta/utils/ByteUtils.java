package de.unibayreuth.bayceer.delta.utils;

import de.unibayreuth.bayceer.delta.utils.DateTime;
import de.unibayreuth.bayceer.delta.utils.Time;


public class ByteUtils {
	
	public static byte[] split(byte[] a, int start, int end){		
		int length = end - start + 1;
		byte[] r = new byte[length];
		for (int i=0;i<length;i++){
			r[i] = a[start + i];
		}
		return  r;
	}
	
	
	public static long getCheckSum(byte[] a, int start, int end){
		long r = 0;
		for (int i= start;i <= end;i++){
			r = r + a[i];
		}
		return r;
	}
	
	public static String getString(byte[] b, int start, int end){
		
		return new String(split(b, start, end)); 
	}
	
	public static DateTime getDateTime12(byte[] b, int start, int end){
		return getDateTime12(split(b, start, end)); 
	}
	
	public static Time getTime12(byte[] b, int start, int end){
		return getTime12(split(b, start, end)); 
	}
	
	
	public static int getInt(byte[] b, int start, int end){		
		String i = getString(b, start, end);
		return Integer.parseInt(i, 16);		
	}
	
	
	public static int getIntBCD(byte value){
		
		  int l = (value >> 4);
		  int r = value ^ (l << 4);		 
		  return l * 10 +r;
	}
	
	public static DateTime getDateTime6(byte[] b){
		if (b.length!= 6) {
			throw new IllegalArgumentException("Wrong array length, expected 6 actual " + b.length + ".");
		}
		DateTime d = new DateTime();
		d.setMonth(getIntBCD(b[0]));
		d.setDay(getIntBCD(b[1]));
		d.setHour(getIntBCD(b[3]));
		d.setMinute(getIntBCD(b[4]));
		d.setSecond(getIntBCD(b[5]));		
		return d;		
	}
	
    
		
	public static DateTime getDateTime12(byte[] b){
		
		if (b.length!= 12) {
			throw new IllegalArgumentException("Wrong array length, expected 12 actual " + b.length + ".");
		}
		
		DateTime d = new DateTime();
		
		String month = new String(ByteUtils.split(b, 0, 1));
		d.setMonth(Integer.valueOf(month));			

		String dayOfMonth = new String(ByteUtils.split(b, 2, 3));
		d.setDay(Integer.valueOf(dayOfMonth));
		
		String hour = new String(ByteUtils.split(b, 6, 7));
		d.setHour(Integer.valueOf(hour));
		
		String minute = new String(ByteUtils.split(b, 8, 9));
		d.setMinute(Integer.valueOf(minute));
		
		String sec = new String(ByteUtils.split(b, 10, 11));
		d.setSecond(Integer.valueOf(sec));		
		return d;
	}
	
	
public static Time getTime12(byte[] b){
		
		if (b.length!= 12) {
			throw new IllegalArgumentException("Wrong array length, expected 12 actual " + b.length + ".");
		}
		Time r = new Time();
		
		String hour = new String(ByteUtils.split(b, 6, 7));
		r.setHour(Integer.valueOf(hour));
		
		String minute = new String(ByteUtils.split(b, 8, 9));
		r.setMinute(Integer.valueOf(minute));
		
		String sec = new String(ByteUtils.split(b, 10, 11));
		r.setSecond(Integer.valueOf(sec));
		return r; 	
	}
	
	
	
}
