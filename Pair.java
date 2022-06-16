/**
 * Pair class used to store rankings 
 * @author Zain Salman - 7790429
 */

public class Pair implements Comparable<Pair>{
	private int key;
	private int value;
	
	 /**
    * Returns the key stored in this Pair.
    * @return the Pair's key
    */
	public int getKey() {
		return key;
	}

	/**
    * Returns the value stored in this Pair.
    * @return the Pair's value
    */
	public int getValue() {
		return value;
	}
	
	/**
    * Creates a pair to store integer key-values
    */
	public Pair(int k,int v) {
		key = k;
		value = v;
	}

	@Override
	/**
    * Override method for priority que, comparing integer values. 
    */
	public int compareTo(Pair other) {
		return Integer.compare(this.getKey(), other.getKey());
	}
	

}
