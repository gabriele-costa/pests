package it.unige.automata.event;

public class GUneq implements Guard {

public Expression left, right;
	
	public GUneq(Expression l, Expression r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left.toString() + " != " + right.toString();
	}
	
}
