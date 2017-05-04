package it.unige.automata.event;

public class Diff implements Expression {
	
	public Expression left, right;
	
	public Diff(Expression l, Expression r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left + " - " + right;
	}

}
