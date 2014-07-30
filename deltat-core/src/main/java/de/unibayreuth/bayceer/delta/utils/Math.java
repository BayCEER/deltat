package de.unibayreuth.bayceer.delta.utils;

public class Math {
	/** returns the pow of a int basis and int exponent
	 * @param basis
	 * @param exp
	 * @return int 
	 */
	public static int pow(int basis, int exp) {
	 int ret = 1;
	 for (int i=1; i<=exp; i++) ret *= basis;
	 return ret;
	}

}
