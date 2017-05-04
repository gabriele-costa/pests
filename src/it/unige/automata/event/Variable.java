package it.unige.automata.event;

public class Variable implements Expression {
	
	public String var;
	
	public Variable(String v) {
		var = v;
	}
	
	@Override
	public String toString() {
		return var;
	}

}
