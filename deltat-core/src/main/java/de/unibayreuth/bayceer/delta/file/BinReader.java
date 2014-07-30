package de.unibayreuth.bayceer.delta.file;

import java.io.IOException;
import java.io.InputStream;

import de.unibayreuth.bayceer.delta.utils.ByteUtils;
import de.unibayreuth.bayceer.delta.utils.DateTime;

public class BinReader {
    InputStream in = null;
    	
	public BinReader(InputStream in){
		this.in = in;
	}
	
	public String readString(int bytes) throws IOException{
			byte[] b = new byte[bytes];
			in.read(b, 0, bytes);
			return new String(b);
		
	}
	
	public void skip(long i) throws IOException {	
			in.skip(i);

	}
	
	public String readString() throws IOException{
		return readString(1);
	}
	
	
	public BitArray readBitArray(int bytes) throws IOException{
		byte[] b = new byte[bytes];
		in.read(b, 0, bytes);
		return new BitArray(b);
	}
	
	public int readShort() throws IOException {
		byte[] b = new byte[2];
		in.read(b,0,2);
		BitArray ret = new BitArray(b);
		return ret.getInteger();
	}
	
	
	
	public int readByte() throws IOException {
		  return in.read();
	}
	
	public DateTime readDateTime() throws IOException{
		byte[] b = new byte[6];
		in.read(b, 0, 6);
		return ByteUtils.getDateTime6(b);	
	}
	
	
	
		
	
}
