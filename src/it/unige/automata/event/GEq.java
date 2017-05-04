package it.unige.automata.event;

public class GEq implements Guard {
	
	public Expression left, right;
	
	public GEq(Expression l, Expression r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left.toString() + " = " + right.toString();
	}

}
