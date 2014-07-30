/**
 * 
 */
package de.unibayreuth.bayceer.delta.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.unibayreuth.bayceer.delta.interpolation.Interpolation;
import de.unibayreuth.bayceer.delta.interpolation.InterpolationFactory;

/**
 * @author oliver
 *
 */
public class CsvInterpolator {
	
	private static final int LABEL_ROW = 1;
	private static final int CODE_ROW = 2;
	private static final int UNIT_ROW = 3;
	
	
	Logger logger = Logger.getRootLogger();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 	System.setProperty("user.timezone","GMT+1");
	        TimeZone.setDefault(TimeZone.getTimeZone("GMT+1")); 
	        
	        PropertyConfigurator.configure("log4j.properties");        			
			if (args == null || args.length != 2){
				System.out.println("Usage: CsvInterpolator <input> <output>");
				System.exit(-1);
			}  else {
				CsvInterpolator ci = new CsvInterpolator();
				System.exit(ci.interpolateFile(args[0],args[1]));
			}
	}
	
	
	public CsvInterpolator() {
		super();
		
	}


	public int interpolateFile(String inFile, String outFile){
		logger.info("Interpolation on file " + inFile + ".");
		
		Map<String,Interpolation> map = new HashMap<String,Interpolation>(10);
		
		
		
		
		CSVReader reader = null;
		CSVWriter writer = null;
			try {
				
				InterpolationFactory.configure();
				map = InterpolationFactory.getMap();
				
				reader = new CSVReader(new FileReader(inFile),';',CSVWriter.NO_QUOTE_CHARACTER );
				writer = new CSVWriter(new PrintWriter(outFile),';',CSVWriter.NO_QUOTE_CHARACTER);
				
				long r = 0;
				String[] row = null;
				
				List<String> code = new ArrayList<String>();
				List<String> keys = new ArrayList<String>();
				List<String> labels = new ArrayList<String>();
								
				while ( (row = reader.readNext()) != null){					
					if (r==0) {
					  writer.writeNext(row);
					} else if (r==LABEL_ROW){						
					   labels = new ArrayList<String>(Arrays.asList(row));
					   writer.writeNext(row);
					} else if (r==CODE_ROW){
					   code =  new ArrayList<String>(Arrays.asList(row));
					   writer.writeNext(row);
					} else if (r==UNIT_ROW){
						for(int i=0;i<row.length;i++){											
							StringBuffer key = new StringBuffer();
							key.append(code.get(i));key.append(":");key.append(row[i]);							
							keys.add(key.toString());																
							if (map.containsKey(key.toString())){
								Interpolation f = map.get(key.toString());							
								logger.info("Interpolate column " + labels.get(i).trim() + " using function " + f.getFunctionId());								
								row[i] = f.getUnitOut();
								
							}
						}		  
						writer.writeNext(row);
					} else {
						String[] rw = new String[row.length];						 
						for(int i=0;i<row.length;i++){																					
							if (map.containsKey(keys.get(i)) && !row[i].isEmpty()){
								Double v = Double.valueOf(row[i]);
								Interpolation iP = map.get(keys.get(i));
																
								try {
									rw[i] = String.valueOf(iP.getFunction().value(v));	
								} catch (FunctionEvaluationException m){
									logger.warn("Invalid value in Cell ["+r+","+i+"]:" + v );
									rw[i] = "";									
								}
							} else {
								rw[i] = row[i];
							}														
						}		  
						writer.writeNext(rw);
					}
					r++;
				}				
				
				
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;			
			} finally {
				try {
					reader.close();
					writer.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}	
				logger.info("Interpolation finished.");
				
			}
			
			
		    return 0;
			
		
	}


	
	

}
