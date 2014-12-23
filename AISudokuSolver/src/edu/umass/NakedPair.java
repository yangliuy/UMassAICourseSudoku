package edu.umass;

/**NakedPair class
 * @author Wei Hong & Liu Yang
 * @mail weihong@cs.umass.edu & lyang@cs.umass.edu
 */

class NakedPair{
	int p1;
	int p2;
	public NakedPair(int p1, int p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof NakedPair){
			NakedPair other = (NakedPair) o;
			return((this.p1 == other.p2 && this.p2 == other.p1) || (this.p1 == other.p1 && this.p2 == other.p2));
		}		
		return false;		
	}
	@Override
	public int hashCode(){		
		return (int)(this.p1*this.p2*Math.random()*100000);		
	}
}