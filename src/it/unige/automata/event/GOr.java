package it.unige.automata.event;

public class GOr implements Guard {
	
	public Guard left, right;
	
	public GOr(Guard l, Guard r) {
		left = l;
		right = r;
	}
	
	@Override
	public String toString() {
		return left.toString() + " or " + right.toString();
	}

}
