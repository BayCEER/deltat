package de.unibayreuth.bayceer.delta;

import junit.framework.TestCase;
import de.unibayreuth.bayceer.delta.ui.CsvInterpolator;



public class CsvInterpolatorTest extends TestCase  {

	public void testRun1() {						
		CsvInterpolator.main(new String[] {"tests/files/coul2_A3.csv","tests/files/coul2_A3i.csv"});
	}
	
	
}
