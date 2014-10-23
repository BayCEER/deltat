package de.unibayreuth.bayceer.delta.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.file.BinFile;
import de.unibayreuth.bayceer.delta.file.BinReader;
import de.unibayreuth.bayceer.delta.file.DLDump;

public class CsvDump extends Thread {

	private final static Logger log = Logger.getRootLogger();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private String dev;
	private Integer baudrate;
	private int sleepMin;


	public CsvDump(String dev, Integer baudrate, Integer sleepMin) {
		this.dev = dev;
		this.baudrate = baudrate;
		this.sleepMin = sleepMin;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		System.setProperty("user.timezone", "GMT+1");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		new CsvDump(args[0],Integer.valueOf(args[1]),Integer.valueOf(args[2])).start();
	}

	@Override
	public synchronized void run() {

		log.info("CsvDump thread started.");

		File f = null;
		while (true) {
			log.info("CsvDump dump running.");
			try {

				f = File.createTempFile("delta", ".dmp");								
				DLDump d = new DLDump(f.getAbsolutePath(), false, baudrate,	dev);				
				if (!d.run()){
					log.warn("Failed to dump into file: " + f.getAbsolutePath());					
				} else if (f.length() == 0) {
					log.warn("Empty file.");
				} else {
					log.debug("Writing values out...");
					FileInputStream fin = null;
					try {
						fin = new FileInputStream(f);
						BinFile bin = new BinFile(new BinReader(fin));
						write(bin,false);
					} catch (IOException e) {
						log.error(e.getMessage());
					} finally {
						if (fin != null)
							fin.close();
					}
				}

			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				if (f.exists()) {
					f.delete();
				}
			}

			log.info("CsvDump dump finished.");

			try {
				Thread.sleep(sleepMin*60*1000);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}

	}

	public void write(BinFile bin, boolean status) throws IOException {
		// Identify year span
		Calendar d = new GregorianCalendar();
		int year = d.get(Calendar.YEAR);
		bin.readHeader();
		if (bin.getFirstTimedData().getLong(1970) > bin.getCurrentTime().getLong(1970)) {
			year--;
			log.warn("Identified year span.");
		}
		long ct = bin.getFirstTimedData().getLong(year);
		log.info("Date of first record:" + dateFormat.format(ct));

		int rcount;
		long t = 0;
		java.util.Date logt = null;
		while ((rcount = bin.readChannelCount()) > 0) {
			logt = new java.util.Date(ct + t * bin.getInterval() * 1000);
			System.out.print(dateFormat.format(logt));
			Vector dr = bin.readBlock(rcount);
			for (int i = 0; i < dr.size(); i++) {
				Vector element = (Vector) dr.elementAt(i);
				System.out.print(";");
				System.out.print(bin.getChannels()[i].getChannelNumber()+1);
				System.out.print(":");
				System.out.print(element.get(1));
				if (status) {
					System.out.print(";");
					System.out.print(element.get(0));
				}
			}
			System.out.print("\n");
			t++;
		}
		log.info("Number of records:" + t);
		log.info("Date of last record:" + dateFormat.format(logt));

	}

}
