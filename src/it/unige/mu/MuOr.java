package it.unige.mu;

public class MuOr implements Assertion {
	
	public Assertion left, right;

	public MuOr(Assertion left, Assertion right) {
		super();
		this.left = left;
		this.right = right;
	}

}
