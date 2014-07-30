package de.unibayreuth.bayceer.delta.file;

import java.util.regex.Pattern;

public class Channel {
 private int ChannelNumber;
 private String Label;
 private String Unit;
 private String SensorCode;
 private int TypeFlag;
 private int StorageFreq;
 private int SamplingFreq;
 private int Factor;
 private int Offset;
 private int MinLoggedData;
 private int MaxLogggedData;
 
 Pattern patternFunction = Pattern.compile("[a-zA-Z]");
 Pattern patternId = Pattern.compile("\\d+");
 
 public int getChannelNumber() {
	return ChannelNumber;
}
public void setChannelNumber(int channelNumber) {
	ChannelNumber = channelNumber;
}
public String getLabel() {
	return Label;
}
public void setLabel(String label) {
	Label = label;
}
public String getSensorCode() {
	return SensorCode;
}
public void setSensorCode(String sensorCode) {
	SensorCode = sensorCode;
}
public String getUnit() {
	return Unit;
}
public void setUnit(String unit) {
	Unit = unit;
}
public int getFactor() {
	return Factor;
}
public void setFactor(int factor) {
	Factor = factor;
}

public int getMaxLogggedData() {
	return MaxLogggedData;
}
public void setMaxLogggedData(int maxLogggedData) {
	MaxLogggedData = maxLogggedData;
}
public int getMinLoggedData() {
	return MinLoggedData;
}
public void setMinLoggedData(int minLoggedData) {
	MinLoggedData = minLoggedData;
}
public int getOffset() {
	return Offset;
}
public void setOffset(int offset) {
	Offset = offset;
}
public int getTypeFlag() {
	return TypeFlag;
}
public void setTypeFlag(int typeFlag) {
	TypeFlag = typeFlag;
}
public int getStorageFreq() {
	return StorageFreq;
}
public void setStorageFreq(int storageFreq) {
	StorageFreq = storageFreq;
}
public int getSamplingFreq() {
	return SamplingFreq;
}
public void setSamplingFreq(int samplingFreq) {
	SamplingFreq = samplingFreq;
}
public boolean isSkipped() {
	if (!isDBLabel()) {
		return true;
	} else {
		return (Label.charAt(0) == 'Y') || (Label.charAt(0) == 'y');	
	}
	
}

public boolean interpolate() {
	if (!isDBLabel()) {
		return false;
	} else {
		return !((Label.charAt(0) == 'X') || (Label.charAt(0) == 'x'));	
	}
	
	
}

public String getFunctionKey(){
	return String.valueOf(Label.charAt(0));
}

public boolean isDBLabel(){
	if (Label == null || Label.isEmpty()) return false;	
	String f = String.valueOf(Label.charAt(0));		
	if (!patternFunction.matcher(f).matches()) return false;
	String id = String.valueOf(Label.substring(1));
	if (!patternId.matcher(id).matches()) return false;
	return true;	
}

public String getMesId(){
	return Label.substring(1);
}
  
}
