package de.unibayreuth.bayceer.delta.file;



public class CompressedFormat {

    BitArray bitArray  =null;
	
    public CompressedFormat(BitArray bitArray){
		this.bitArray = bitArray;
	}
	
	public int getValue(){
		  int m	= de.unibayreuth.bayceer.delta.utils.Math.pow(8,bitArray.get(12, 14).getInteger()) ;  // octal range
		  int si = bitArray.get(14)?+1:-1; // sign
		  int v = bitArray.get(0,12).getInteger(); // int
		  return si * v * m;
	}
	
	/**
	 * get faultcode of compressed format
	 * @param value
	 * @return FaultCode
	 */
	public FaultCode getFaultCode(){
		  if (bitArray.get(15)) {
			  int i = bitArray.get(0, 2).getInteger();  
			  switch (i) {
			    case 0:return FaultCode.OVERRUN;
			    case 1:return FaultCode.NOISY;
			    case 2:return FaultCode.OUTSIDELIMITS;
			    case 3:return FaultCode.OVERRANGE;
			    default: return null;
			}
		  } else {
			  return FaultCode.OK;
		  }
	  }
	
}
