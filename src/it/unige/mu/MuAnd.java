package it.unige.mu;

public class MuAnd implements Assertion {
	
	public Assertion left, right;

	public MuAnd(Assertion left, Assertion right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return left.toString() + " /\\ " + right.toString();
	}

	@Override
	public int size() {
		return left.size() + right.size();
	}
}
