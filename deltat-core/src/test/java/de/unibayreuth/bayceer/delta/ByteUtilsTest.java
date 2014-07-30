package de.unibayreuth.bayceer.delta;


import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.utils.ByteUtils;

/**
 * 
 */

/**
 * @author oliver
 *
 */
public class ByteUtilsTest extends TestCase {

	/**
	 * @param name
	 */
	public ByteUtilsTest(String name) {
		super(name);
	}
	
	public void testCheckSum(){
		String i = "00000000";		
		assertEquals(48*8, ByteUtils.getCheckSum(i.getBytes(),0,7));		
		
	}
	
	
	public void testStringToHex(){
		String i = "0001C000";
		assertEquals(114688, Integer.parseInt(i, 16));
				
	}
	
	

}
