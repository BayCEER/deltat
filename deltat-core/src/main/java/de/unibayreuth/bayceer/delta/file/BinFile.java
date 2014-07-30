/**
 * 
 */
package de.unibayreuth.bayceer.delta.file;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import de.unibayreuth.bayceer.delta.utils.DateTime;


public class BinFile {
	
	protected final static Logger logger = Logger.getLogger(BinFile.class.getName());
		
	private String version;
	private String experimentName;
	private DateTime startTime;
	private DateTime currentTime;
	private DateTime firstTimedData;
	private String dataType;
	private String dateFormat;
	private int channelCount;
	private int iSecs;
	private Channel[] channels;		
	private BinReader reader;	
	

	public BinFile(BinReader reader){
		this.reader = reader;	
	}
	
	public void readHeader() throws IOException {
		  // Version
		  this.version = reader.readByte() + "." + reader.readByte();
		  // ExperimentName
		  this.experimentName = reader.readString(8);
		  // StartTime
		  setStartTime(reader.readDateTime());		  		  
		  // CurrentTime 		
		  currentTime = reader.readDateTime();		  
		  // FirstTime 		  
		  firstTimedData = reader.readDateTime();
		  // Interval
		  this.iSecs = getIntervallSecs(reader.readByte());
		  // Data Type
		  readDataType();
		  // DateFormat
		  readDateFormat();
		  // Unused Bytes
		  reader.skip(16);
		  this.channelCount = reader.readByte();
		  reader.skip(1);
		  readChannelHeaders();		  
	}
	
    private void readChannelHeaders() throws IOException {		
		channels = new Channel[channelCount];		
		readChannelNumbers();
		
		readSensorCodes();
		testChannelCount();
		
		reader.skip(channelCount * 2); // Type.Flags
		testChannelCount();
		
		readChannelIntervals();
		testChannelCount();
	    
		readChannelFactors();
	    testChannelCount();
	    
	    readChannelOffsets(); // Offset
	    testChannelCount();
	    
	    readChannelMin(); // Minimum Logged Data
	    testChannelCount();
	    readChannelMax(); // Maximum Logged Data	
	    
	}
    
    
    
    @SuppressWarnings("unchecked")
	public Vector readBlock(int ccounts) throws IOException {
    	Vector ret = new Vector(ccounts);
    	for (int i = 0; i < ccounts; i++) {
    		Vector v = new Vector();
    		CompressedFormat cf = new CompressedFormat(reader.readBitArray(2));
    		v.add(cf.getFaultCode());
			double b = (double)(cf.getValue() + this.channels[i].getOffset()) / (double)this.channels[i].getFactor();
			v.add(Double.valueOf(b));
			ret.add(v);
		}
    	return ret;
    }
    
    
    // Liest i byte aus jedem Block 
    // returns 0 falls EOF/EOB 
    public int readChannelCount() throws IOException {
    	int i = reader.readByte();
    	if (i == -1) i = 0;
    	return i;     	
    }
	
	
		
	private void readChannelNumbers() throws IOException {
		for (int i = 0;i < channelCount;i++){
			channels[i] = new Channel();
			channels[i].setChannelNumber(reader.readByte());
		}
	}
	
	private void readSensorCodes() throws IOException {
   		   for (int i = 0;i < channelCount;i++){
				channels[i].setSensorCode(reader.readString(3));
				
				channels[i].setLabel(reader.readString(8).trim());
				channels[i].setUnit(reader.readString(6));
			}	
    }
	
	private void readChannelFactors() throws IOException {
		for (int i = 0;i < channelCount;i++){
			channels[i].setFactor(reader.readShort());
		}
	}
	
	private void readChannelMin() throws IOException {
		for (int i = 0;i < channelCount;i++){
			CompressedFormat cf = new CompressedFormat(reader.readBitArray(2));
			channels[i].setMinLoggedData(cf.getValue());
			
		}
	}
	private void readChannelMax() throws IOException {
		for (int i = 0;i < channelCount;i++){
			CompressedFormat cf = new CompressedFormat(reader.readBitArray(2));
			channels[i].setMaxLogggedData(cf.getValue());
		}
	}
	private void readChannelOffsets() throws IOException {
		for (int i = 0;i < channelCount;i++){
			CompressedFormat cf = new CompressedFormat(reader.readBitArray(2));
			channels[i].setOffset(cf.getValue());
		}
	}
		

	private void readChannelIntervals() throws IOException {
		for (int i = 0;i < channelCount;i++){
			int store = getIntervallSecs(reader.readByte());
			channels[i].setStorageFreq(store);
			int sample = getIntervallSecs(reader.readByte());
			channels[i].setSamplingFreq(sample);
		}
	}
	
	
	private int getIntervallSecs(int i){
		switch (i) {
		case 1:  return 1;
		case 2:  return 5; 
		case 3:  return 10;
		case 4:  return 30;
		case 5:  return 60; 
		case 6:  return 5*60;
		case 7:  return 10*60; 
		case 8:  return 30*60; 
		case 9:  return 60*60; 
		case 10: return 2*60*60;
		case 11: return 4*60*60; 
		case 12: return 12*60*60; 
		case 13: return 24*60*60; 
		default: return 0;
    	}
	}
	
				
	
	private void testChannelCount() throws IOException {
		if (reader.readByte() != channelCount) throw new IOException("Lesefehler.");
	}
	
		
			
	
	private void readDateFormat() throws IOException {
	   int i = reader.readByte();
	   if (i == 0) {
		   this.dateFormat = "European";
	   } else {
		   this.dateFormat = "US";
	   }
	}
	
	
	private void readDataType()  throws IOException {
		int i = reader.readByte();
		if (i == 1){
			dataType = "TIMED";
		} else if (i == 2){
			dataType = "TRIG/61";
		} else if (i == 3){
			dataType = "TRIG/62";
		} else {
			dataType = "UNDEFINIED";
		}
	}
	
	
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
		
	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}
	

	public int getInterval() {
		return iSecs;
	}

	public void setInterval(int secs) {
		iSecs = secs;
	}

	public Channel[] getChannels() {
		return channels;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getCurrentTime() {
		return currentTime;
	}

	protected void setCurrentTime(DateTime currentTime) {
		this.currentTime = currentTime;
	}

	public DateTime getFirstTimedData() {
		return firstTimedData;
	}

	protected void setFirstTimedData(DateTime firstTimedData) {
		this.firstTimedData = firstTimedData;
	}

	
	
	


	
}