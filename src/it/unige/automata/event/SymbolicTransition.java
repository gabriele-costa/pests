package it.unige.automata.event;

import it.unige.automata.State;
import it.unige.automata.Transition;

public class SymbolicTransition implements Transition {
	
	private State src, dst;
	private Guard g;
	private String var;
	private String label;
	
	public SymbolicTransition(State src, String label, String var, Guard g, State dst) {
		super();
		this.src = src;
		this.dst = dst;
		this.g = g;
		this.label = label;
		this.var = var;
	}

	@Override
	public State getSource() {
		return src;
	}

	@Override
	public State getDestination() {
		return dst;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	public Guard getGuard() {
		return g;
	}
	
	public String getVariable() {
		return var;
	}

}
