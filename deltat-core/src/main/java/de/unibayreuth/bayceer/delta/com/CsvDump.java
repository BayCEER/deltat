package de.unibayreuth.bayceer.delta.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibayreuth.bayceer.delta.file.BinFile;
import de.unibayreuth.bayceer.delta.file.BinReader;
import de.unibayreuth.bayceer.delta.file.DLDump;

public class CsvDump extends Thread {

	private final static Logger log = Logger.getRootLogger();

	private SimpleDateFormat dateFormat = null;

	private String device;

	private Integer baudrate;

	private int interval;

	private Boolean full;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		System.setProperty("user.timezone", "GMT+1");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		new CsvDump().start();
	}

	@Override
	public synchronized void run() {

		log.info("CsvDump thread started.");

		File f = null;
		while (true) {
			log.info("CsvDump dump running.");
			try {
				Properties p = new Properties();
				p.load(new FileReader("CsvDump.properties"));
				device = p.getProperty("device", "COM1");
				baudrate = Integer.valueOf(p.getProperty("baudrate", "9600"));
				interval = Integer.valueOf(p.getProperty("interval", "60")) * 1000 * 60;
				full = Boolean.valueOf(p.getProperty("full", "false"));
				dateFormat = new SimpleDateFormat(p.getProperty("dateFormat","yyyy-MM-dd'T'HH:mm:ss"));
				f = File.createTempFile("delta", ".dmp");				
				
				DLDump d = new DLDump(f.getAbsolutePath(), false, baudrate,	device);
				
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
						write(bin, Boolean.valueOf(p.getProperty("status", "false")));
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
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}

	}

	private void write(BinFile bin, boolean status) throws IOException {

		// Identify year span
		Calendar d = new GregorianCalendar();
		int year = d.get(Calendar.YEAR);
		bin.readHeader();
		if (bin.getFirstTimedData().getLong(1970) > bin.getCurrentTime()
				.getLong(1970)) {
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
