package it.unige.automata;

import java.util.Set;


/**
 * Base interface for automata
 * 
 * @author Gabriele CostaS
 *
 * @param <T> A type extending the Transition interface
 */
public interface Automaton<T extends Transition> {
	
	public static final String EPSILON 	= "[e]";
	public static final String FAIL 	= "ff";
	
	public State getInitial();
	
	public boolean setInitial(State i);
	
	public Set<State> getFinals();
	
	// public Set<State> getFails();
	
	public boolean setFinal(State s, boolean f);
	
	// public boolean setFail(State s, boolean f);
	
	public Set<State> getStates();
	
	public boolean addState(State s);
	
	public Set<T> getTransitions();
	
	public boolean addTransition(T t);
	
	public boolean addTransition(State s, String l, State d);

	public Set<String> getAlphabet();
	
	public Set<State> trans(State src, String a);
	
	public boolean removeState(State s);
	
	public void removeTransition(T t);
	
}
