package de.unibayreuth.bayceer.delta.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

public class BinCsvWriter  {
	
	private static Logger logger = Logger.getLogger(BinCsvWriter.class);	
	
	PrintWriter p = null;
	BinFile bin = null;
	int year;
		
		
	public BinCsvWriter(PrintWriter p, BinFile bin, int year) {
		this.p = p;
		this.bin = bin;
		this.year = year;
	}
	
	@SuppressWarnings("unchecked")
	public boolean write() {	
		
		
		
		CSVWriter w = new CSVWriter(p,';',CSVWriter.NO_QUOTE_CHARACTER);
		try {
			bin.readHeader();
			
			
			
			// Channel 
			Channel[] c = bin.getChannels();							
			String[] chNumbers  = new String[c.length + 1];
			chNumbers[0] = "Channel";
			String[] chLabels  = new String[c.length + 1];
			chLabels[0] = "Label";			
			String[] chSensors = new String[c.length +1];
			chSensors[0] = "Sensor Type";
			String[] chUnits = new String[c.length +1];
			chUnits[0] = "Units";
			
						
			for(int i=0;i<c.length;i++){
				chNumbers[i+1] = String.valueOf(c[i].getChannelNumber()).trim();
				chLabels[i+1] = String.valueOf(c[i].getLabel()).trim();
				chSensors[i+1] = String.valueOf(c[i].getSensorCode()).trim();
				chUnits[i+1] = String.valueOf(c[i].getUnit()).trim();			
			}
			w.writeNext(chNumbers);
			w.writeNext(chLabels);
			w.writeNext(chSensors);
			w.writeNext(chUnits);
			
			
			
			Calendar cal = bin.getFirstTimedData().getCalendar(year);
			int intSecs = bin.getInterval();
			
			
			
			
			int chCount;
			
			while ((chCount = bin.readChannelCount()) > 0){
				
				Vector dr = bin.readBlock(chCount);
				String[] values = new String[dr.size() + 1];				
				for (int i=0;i<dr.size();i++) {					
					Vector element = (Vector) dr.elementAt(i);															
					values[i+1] = element.get(1).toString();
				}				
				values[0] = SimpleDateFormat.getDateTimeInstance().format(cal.getTime());
				w.writeNext(values);
				cal.add(GregorianCalendar.SECOND,intSecs);
			}
			
			
						
																
			w.flush();
			w.close();
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
		
		
		
	}
			
		

}
