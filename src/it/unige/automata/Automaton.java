package it.unige.automata;

import java.util.Set;

public interface Automaton {
	
	public static final String EPSILON = "[e]";
	
	public State getInitial();
	
	public boolean setInitial(State i);
	
	public Set<State> getFinals();
	
	// public Set<State> getFails();
	
	public boolean setFinal(State s, boolean f);
	
	// public boolean setFail(State s, boolean f);
	
	public Set<State> getStates();
	
	public boolean addState(State s);
	
	public Set<Transition> getTransitions();
	
	public boolean addTransition(Transition t);

	public Set<String> getAlphabet();
	
	public Set<State> trans(State src, String a);
	
	public boolean removeState(State s);
	
}
