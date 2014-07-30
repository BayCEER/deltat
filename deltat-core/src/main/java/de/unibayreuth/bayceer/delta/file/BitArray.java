package de.unibayreuth.bayceer.delta.file;


public class BitArray {
	
	private boolean[] bit;
	
	public BitArray(String value){
		this(value.length());
		for (int i = 0; i < value.length(); i++) {
			char a = value.charAt(value.length() - 1  - i);
			if (a=='1') set(i);
		}
	}
	
	public BitArray(boolean[] value){
		this.bit = value;
	}
	
	public String getString(){
		char[] ret = new char[bit.length]; 
		for (int i = 0; i < bit.length; i++) {			 
			if (bit[i]) {
				ret[i] = '1';
			} else {
				ret[i] = '0';
			}
		}
		return new String(ret);
	}
	
	public BitArray(byte[] value){
		this(value.length * 8);
		for (int i=0; i<value.length*8; i++) {
	          if ((value[value.length-i/8-1]&(1<<(i%8))) > 0) {
	              set(i);
	          }
	    }
	}
	
	public int getInteger(int base){
		int v = 0;
		 for (int i=0;i<bit.length;i++){
			  if (bit[i]) v+=Math.pow(base,i);
		 }
		 return v;
	}
	
	public int getInteger(){
		return getInteger(2);
	}
	
	
	public BitArray(int length){
		if (length < 0)
		    throw new NegativeArraySizeException("length < 0: " + length);
		bit = new boolean[length];
	}
	
	public void set(int index){
		if (index < 0)  throw new IndexOutOfBoundsException("index < 0: " + index);
		bit[index] = true;
	}
	
	public void set(int start, int end){
		if (start < 0)  throw new IndexOutOfBoundsException("start < 0: " + start);
		if (end < 0)  throw new IndexOutOfBoundsException("end < 0: " + end);
		if (start > end) throw new IndexOutOfBoundsException("start > end");		
        if (length() <= start ) return;
        
        for (int i = start; i <= end; i++) {
			set(i);
		}        
	}
	
	public boolean get(int index){
		if (index < 0)  throw new IndexOutOfBoundsException("index < 0: " + index);
		return bit[index];
	}
	
	public BitArray get(int start, int end){
		if (start < 0)  throw new IndexOutOfBoundsException("start < 0: " + start);
		if (end < 0)  throw new IndexOutOfBoundsException("end < 0: " + end);
		if (start > end) throw new IndexOutOfBoundsException("start > end");
		
		 // If no bits in range return empty BitArray
        if (length() <= start || start == end) return new BitArray(0);

		int l = end - start;
		boolean[] ret = new boolean[l];
		System.arraycopy(this.bit, start, ret, 0, l);
		return new BitArray(ret);
	}
	
	public int length(){
		return bit.length;
	}

}
