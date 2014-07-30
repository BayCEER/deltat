package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.file.Channel;


public class ChannelTest extends TestCase {
	
	public void testChannel(){
		
		Channel c = new Channel();
		assertFalse(c.isDBLabel());
		assertTrue(c.isSkipped());
		assertFalse(c.interpolate());
		
		c.setLabel("x122221");
		assertTrue(c.isDBLabel());
		assertFalse(c.isSkipped());
		assertFalse(c.interpolate());
		
		
		c.setLabel("x01200002221");
		assertTrue(c.isDBLabel());
		assertFalse(c.isSkipped());
		assertFalse(c.interpolate());
		assertEquals("01200002221",c.getMesId());
		
		
		c.setLabel("002221");
		assertFalse(c.isDBLabel());
				
		
		c.setLabel("y122221");
		assertTrue(c.isDBLabel());
		assertEquals("122221",c.getMesId());
		assertTrue(c.isSkipped());
		
		
		c.setLabel("b122221");
		assertTrue(c.isDBLabel());
		assertFalse(c.isSkipped());
		assertTrue(c.interpolate());
		
		
	}

}
