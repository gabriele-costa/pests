package it.unige.automata.event;

public class Constant implements Expression {
	
	public int val;
	
	public Constant(int v) {
		val = v;
	}
	
	@Override
	public String toString() {
		return "" + val;
	}

}
