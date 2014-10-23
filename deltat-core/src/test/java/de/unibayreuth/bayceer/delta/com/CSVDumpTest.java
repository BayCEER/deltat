package de.unibayreuth.bayceer.delta.com;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.junit.Test;

import de.unibayreuth.bayceer.delta.file.BinFile;
import de.unibayreuth.bayceer.delta.file.BinReader;

public class CSVDumpTest {
	
	
	@Test
	public void testCSVOutPut() throws IOException{		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		String f = getClass().getResource("/2SAINT.BIN").getFile();				
		BinFile d = new BinFile(new BinReader(new FileInputStream(f)));		
		CsvDump csv = new CsvDump("COM1",9600, 10);		
		csv.write(d, false);
		// d.readHeader();
	}

}
