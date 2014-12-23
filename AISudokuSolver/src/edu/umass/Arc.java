package edu.umass;

/**Arc class for AC3
 * @author Wei Hong & Liu Yang
 * @mail weihong@cs.umass.edu & lyang@cs.umass.edu
 */
class Arc{
	int left;
	int right;
	public Arc(int left, int right){		
		this.left = left;
		this.right = right;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Arc){
			Arc other = (Arc) o;
			return((this.right == other.right && this.left == other.left) || (this.right == other.left && this.left == other.right));
		}		
		return false;		
	}
	@Override
	public int hashCode(){		
		return (int)(this.right*this.left*100000);		
	}
}