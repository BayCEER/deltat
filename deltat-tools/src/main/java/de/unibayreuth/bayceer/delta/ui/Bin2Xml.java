package de.unibayreuth.bayceer.delta.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.file.BinFile;
import de.unibayreuth.bayceer.delta.file.BinReader;
import de.unibayreuth.bayceer.delta.file.XmlWriter;



public class Bin2Xml {
	
	private Logger logger = Logger.getRootLogger();	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
        PropertyConfigurator.configure("log4j.properties");        			
		if (args == null || args.length != 3){
			System.out.println("Usage: Bin2Xml <input> <output> <year>");
		}  else {
			Bin2Xml p = new Bin2Xml();
			System.exit(p.convertFile(args[0], args[1], args[2]));	
		}
		
	}
	
	
	public int convertFile(String inFile, String outFile, String year){
		logger.info("Converting File " + inFile + ".");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(inFile));
			out = new BufferedOutputStream(new FileOutputStream(outFile));			
			return convertStream(in, out, year);
			} catch (FileNotFoundException e){
				logger.error(e.getMessage());
				return -1;
			} finally {
				try { 
					if (in != null) in.close();
					if (out != null) out.close();
				} catch (IOException i){
				   logger.error(i.getMessage());
				}
			}
	}
	
	protected int convertStream(InputStream in, OutputStream out, String year){
		OutputStreamWriter writer = null;
		BufferedWriter buffer = null;
		try {	
			writer = new OutputStreamWriter(out,"8859_1");
			buffer = new BufferedWriter(writer);
			BinFile bin = new BinFile(new BinReader(in));
			PrintWriter pw = new PrintWriter(buffer,true);
			XmlWriter w = new XmlWriter(pw,bin,Integer.valueOf(year));
			w.write();						
			return (0);
		} catch (IOException e){
			logger.error(e.getMessage());
			return (-1);
		} finally {
			try { 
				if (writer != null) writer.close();
				if (buffer != null) buffer.close();
			} catch (IOException i){
			   logger.error(i.getMessage());
			}
		}
		
	}
	
	
	
	
	

}
