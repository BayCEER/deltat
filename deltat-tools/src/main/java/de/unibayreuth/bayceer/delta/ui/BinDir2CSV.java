package de.unibayreuth.bayceer.delta.ui;

import java.io.File;
import java.io.FilenameFilter;

public class BinDir2CSV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File d = new File(args[0]);
		for(File f: d.listFiles(new FilenameFilter() {			
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".bin");				
			    }
				})
				)
		{
						
			String a[] = {f.getAbsolutePath(),f.getAbsolutePath().substring(0,f.getAbsolutePath().length() -4) + ".csv" ,f.getName().substring(0, 4)};		
			Bin2Csv.main(a);	
		}
		

	}

}
