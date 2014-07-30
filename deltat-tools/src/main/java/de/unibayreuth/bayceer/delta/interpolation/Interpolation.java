package de.unibayreuth.bayceer.delta.interpolation;

import org.apache.commons.math.analysis.UnivariateRealFunction;


public class Interpolation {
	
		
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUnitIn() {
		return unitIn;
	}
	public void setUnitIn(String unitIn) {
		this.unitIn = unitIn;
	}
	String code;
	String unitIn;
	String unitOut;	
	String functionId;
	
	UnivariateRealFunction function;

	public String getUnitOut() {
		return unitOut;
	}
	public void setUnitOut(String unitOut) {
		this.unitOut = unitOut;
	}
	public UnivariateRealFunction getFunction() {
		return function;
	}
	public void setFunction(UnivariateRealFunction function) {
		this.function = function;
	}
	

}
