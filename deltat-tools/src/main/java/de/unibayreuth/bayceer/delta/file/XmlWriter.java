/**
 * 
 */
package de.unibayreuth.bayceer.delta.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import de.unibayreuth.bayceer.delta.utils.DateTime;

public class XmlWriter{
	PrintWriter p = null;
	BinFile bin = null;
	int year;
		
		
	public XmlWriter(PrintWriter p, BinFile bin, int year) {
		this.p = p;
		this.bin = bin;
		this.year = year;
	}
	
	@SuppressWarnings("unchecked")
	public boolean write() {
			
		try {
			bin.readHeader();
	        writeXMLHeader();
			writeVersion(bin.getVersion());
			writeExperimentName(bin.getExperimentName());
			writeDateTime("start-time",bin.getStartTime());
			writeDateTime("current-time",bin.getCurrentTime());
			writeDateTime("first-timed-date",bin.getFirstTimedData());
			writeDataType(bin.getDataType());
			writeDateFormat(bin.getDateFormat());
			writeChannelCount(bin.getChannelCount());
			writeInterval(bin.getInterval());
			writeCloseTag();
			
			Channel[] c = bin.getChannels();
			p.print("<channels>");
			for (int i = 0;i<c.length;i++){
				StringBuffer b = new StringBuffer();
				b.append("<channel");
				
				b.append(" number=\"");
				b.append(c[i].getChannelNumber());
				b.append("\"");
				
				b.append(" label=\"");
				b.append(c[i].getLabel());
				b.append("\"");
				
				b.append(" sensorcode=\"");
				b.append(c[i].getSensorCode());
				b.append("\"");
				
				b.append(" unit=\"");
				b.append(c[i].getUnit());
				b.append("\"");
				
				b.append(" storefreq=\"");
				b.append(c[i].getStorageFreq());
				b.append("\"");
				
				b.append(" samplefreq=\"");
				b.append(c[i].getSamplingFreq());
				b.append("\"");
				
				b.append(" factor=\"");
				b.append(c[i].getFactor());
				b.append("\"");
				
				b.append(" offset=\"");
				b.append(c[i].getOffset());
				b.append("\"");
				
				b.append(" minLoggedData=\"");
				b.append(c[i].getMinLoggedData());
				b.append("\"");
				
				b.append(" maxMaxLogggedData=\"");
				b.append(c[i].getMaxLogggedData());
				b.append("\"");				
				
				b.append("/>");
				p.println(b.toString());
								
			}
												
			p.println("</channels>");
			
			int rcount;
			long t = 0;
			while ((rcount = bin.readChannelCount()) > 0){				
				p.print("<data row=\"");
				p.print(t);
				p.println("\">");

				Vector dr = bin.readBlock(rcount);
				
				for (int i=0;i < dr.size();i++) {
					Vector element = (Vector) dr.elementAt(i);
					p.print("<record channelNumber=\"");
					p.print(c[i].getChannelNumber());
					p.print("\" faultCode=\"");
					p.print(element.get(0));
					p.print("\"/>");
					p.print(element.get(1));
					p.println("</record>");
				}
			    p.println("</data>");
				t++;
			}

			writeXMLEnd();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	private void writeXMLHeader(){
		p.println("<?xml version=\"1.0\"?>");
		p.print("<logger ");
	}
	
	private void writeXMLEnd(){
		p.println("</logger>");
	}
	
	private void writeVersion(String Version) throws IOException {
		p.print("version=\"" + Version + "\" ");
	}
	
	private void writeDataType(String value) throws IOException {
		p.print("datatype=\"" + value + "\" ");
	}
	
	private void writeDateFormat(String value) throws IOException {
		p.print("date-format=\"" + value + "\" ");
	}
	
	private void writeChannelCount(int value) throws IOException {
		p.print("channels=\"" + value + "\" ");
	}
	
	private void writeInterval(int value) throws IOException {
		p.print("interval=\"" + value + "\" ");
	}
	
	private void writeCloseTag() throws IOException {
		p.append(">");
		p.println();
	}
	
	private void writeExperimentName(String name) throws IOException {
		p.write("name=\"" + name + "\" " );			
	}
	
	private void writeDateTime(String name, DateTime date) throws IOException {
		p.write(name + "=\"" + date.getDate(year) + "\" ");
	}
	
	
}