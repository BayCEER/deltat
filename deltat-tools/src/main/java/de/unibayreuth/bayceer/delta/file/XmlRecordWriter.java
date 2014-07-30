/**
 * 
 */
package de.unibayreuth.bayceer.delta.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;



public class XmlRecordWriter{
	PrintWriter p = null;
	BinFile bin = null;
	private int year;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			
	public XmlRecordWriter(PrintWriter p, BinFile bin, int year) {
		this.p = p;
		this.bin = bin;
		this.year = year;
	}
	
	@SuppressWarnings("unchecked")
	public boolean write() {
			
		try {
			bin.readHeader();
			writeXMLHeader();														
			Channel[] c = bin.getChannels();														
			Calendar firstTime  = bin.getFirstTimedData().getCalendar(1900);
			firstTime.set(Calendar.YEAR,year);			
			
			int rcount;						
			Calendar logt = firstTime;			
			while ((rcount = bin.readChannelCount()) > 0){				
				p.print("<records time=\"");
				p.print(sdf.format(logt.getTime()));				
				p.println("\">");
				Vector dr = bin.readBlock(rcount);				
				for (int i=0;i < dr.size();i++) {	
					Vector element = (Vector) dr.elementAt(i);
					p.print("<record number=\"");
					p.print(c[i].getChannelNumber());					
					p.print("\" label=\"");
					p.print(c[i].getLabel().trim());
					p.print("\" unit=\"");
					p.print(c[i].getUnit().trim());					
					p.print("\" faultcode=\"");
					p.print(element.get(0));
					p.print("\">");
					p.print(element.get(1));
					p.println("</record>");																																			
				}
			    p.println("</records>");				
				logt.add(Calendar.SECOND,bin.getInterval());
			}
			writeXMLEnd();
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	private void writeXMLHeader(){
		p.println("<?xml version=\"1.0\"?>");
		p.println("<data>");
	}
	
	private void writeXMLEnd(){
		p.println("</data>");
	}
	
		
}