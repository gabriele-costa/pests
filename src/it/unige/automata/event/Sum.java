package it.unige.automata.event;

public class Sum implements Expression {
	
	public Expression left, right;
	
	public Sum(Expression l, Expression r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left + " + " + right;
	}

}
