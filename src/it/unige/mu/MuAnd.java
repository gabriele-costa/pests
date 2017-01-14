package it.unige.mu;

public class MuAnd implements Assertion {
	
	public Assertion left, right;

	public MuAnd(Assertion left, Assertion right) {
		super();
		this.left = left;
		this.right = right;
	}

}
