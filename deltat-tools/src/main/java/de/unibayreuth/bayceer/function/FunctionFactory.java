package de.unibayreuth.bayceer.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.log4j.Logger;

public final class FunctionFactory {
   protected final static Logger logger = Logger.getLogger("FunctionFactory.class");
   
   
   private static Hashtable<String,UnivariateRealFunction> hash = null;
      
   private FunctionFactory(){
	   
   }
   
   public static void configure(String path) {
	   hash = new Hashtable<String, UnivariateRealFunction>(5);
	   File f = new File(path);
	   if (!f.exists()) throw new IllegalArgumentException("Directory:" + path + " doesn't exist.");
	   if (!f.isDirectory()) throw new IllegalArgumentException(path + " must be a directory!");
	   
	   File[] files = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				Pattern p = Pattern.compile("\\.xml$");
				return p.matcher(name).find();
			}});
	   
	   
		for (int i=0;i<files.length;i++){	
			FileInputStream in = null;
		   	try {
				in = new FileInputStream(files[i]);
				FunctionParser p = new FunctionParser();
				p.parse(in);
				hash.put(String.valueOf(p.getId()), p.getFunction());
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (in != null){
					try {
						in.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}	
				}
				
			}
			
		}
	
	   
   }
         
   public static Hashtable<String,UnivariateRealFunction> getFunctions(){
	       return hash;
   }
   
   
}
