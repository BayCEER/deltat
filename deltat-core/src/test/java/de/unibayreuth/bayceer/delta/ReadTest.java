package de.unibayreuth.bayceer.delta;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.unibayreuth.bayceer.delta.com.DLException;
import de.unibayreuth.bayceer.delta.com.DLInstruction;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;


public class ReadTest extends AbstractConnectionTest {
	
	public void testRead(){
		try {
			
			boolean full = false;
			con.ok();
		
			byte[] result = con.query(DLInstruction.StatusData,128);			
			int nRecs = ByteUtils.getInt(result, 3, 10);
			System.out.println("Number of stored records:" + nRecs);
			System.out.println("Number of records since last retrieved:" + (nRecs  - ByteUtils.getInt(result, 27, 34)));
			
			// 106 
			con.ok();
			con.setBuffer("0000".getBytes());
			con.exec(106);
			
			// 84			
			if (full){
				con.ok();
				con.exec(84);
			}
												
			// 97
			con.ok();			
			StringBuffer b = new StringBuffer("0001");   
			String s = String.format("%1$08d", nRecs);  
			con.setBuffer(b.append(s).toString().getBytes());
			con.exec(97);		
			
			// 98 s. h flow
			FileOutputStream fout;
			
			int wb = 0;
			try {
				fout = new FileOutputStream("/tmp/test.bin");
				con.ok();			
				con.exec(98);
				wb = con.readBlock(fout);
				System.out.println(wb + " bytes written to file.");
				fout.close();				
			} catch (FileNotFoundException e) {
				fail(e.getMessage());
			} catch (IOException e) {
				fail(e.getMessage());
			}
			
			// 99
			con.ok();
			con.exec(99);
			byte[] r = con.read(13,20);
			if (ByteUtils.getInt(r, 7, 14) != wb){
				fail("Diff in file length and sent bytes.");
			}
			
			
		} catch (DLException e) {
			fail(e.getLocalizedMessage());
		} 				
		
	}

}
