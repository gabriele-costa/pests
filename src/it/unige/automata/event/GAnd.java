package it.unige.automata.event;

public class GAnd implements Guard {
	
	public Guard left, right;
	
	public GAnd(Guard l, Guard r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left.toString() + " and " + right.toString();
	}
	
}
