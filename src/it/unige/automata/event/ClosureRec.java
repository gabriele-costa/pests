package it.unige.automata.event;

import java.util.HashSet;

import it.unige.automata.State;

public class ClosureRec {
	
	public HashSet<State> states;
	public Guard guard;
	
	ClosureRec() {
		states = new HashSet<State>();
		guard = G.tt();
	}
}