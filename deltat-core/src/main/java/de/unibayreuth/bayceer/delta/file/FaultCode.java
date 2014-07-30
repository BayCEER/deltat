package de.unibayreuth.bayceer.delta.file;

public enum FaultCode
 {
   OVERRUN(0),
   NOISY(3),
   OUTSIDELIMITS(6),
   OVERRANGE(5),
   OK(0);
 
   private final int status;
    
   FaultCode(int status)
   {
    this.status = status;
   }

public int getStatus() {
	return status;
}
   
   
   

   
 }
