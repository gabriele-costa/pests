package it.unige.automata.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.util.SetUtils;

public class NFAutomatonImpl implements Automaton {

	Set<State> states;
	State inits;
	Set<State> finals;
	Set<State> fails;
	Set<Transition> delta;
	
	public NFAutomatonImpl(State init) {
		states = new HashSet<State>();
		inits = init;
		finals = new HashSet<State>();
		fails = new HashSet<State>();
		delta = new HashSet<Transition>();
		
		states.add(init);
	}
	
	@Override
	public boolean addState(State s) {
		return states.add(s);
	}

	@Override
	public State getInitial() {
		return inits;
	}

	@Override
	public Set<State> getFinals() {
		return finals;
	}

	@Override
	public boolean setInitial(State i) {
		inits = i;
		return states.add(i);
	}

	@Override
	public boolean setFinal(State s, boolean f) {
		
		if(f) {
			finals.add(s);
			return states.add(s);
		}
		else
			return fails.remove(s);
	}

	@Override
	public Set<State> getStates() {
		return states;
	}

	@Override
	public Set<Transition> getTransitions() {
		return delta;
	}

	@Override
	public boolean addTransition(Transition t) {
		this.addState(t.getSource());
		this.addState(t.getDestination());
		return delta.add(t);
	}

	@Override
	public Set<String> getAlphabet() {
		Set<String> Sigma = new HashSet<String>();
		
		for(Transition t : delta) {
			Sigma.add(t.getLabel());
		}
		
		return Sigma;
	}

	@Override
	public Set<State> trans(State src, String a) {
		
		Set<State> S = new HashSet<State>();
		
		for(Transition t : delta) 
			if(t.getSource().equals(src) && t.getLabel().compareTo(a) == 0)
				S.add(t.getDestination());
		
		return S;
	}

	@Override
	public String toString() {
		return "DFAutomatonImpl [inits=" + inits + ", finals=" + finals + ", fails=" + fails + ", delta=" + delta + "]";
	}

//	@Override
//	public Set<State> getFails() {
//		return fails;
//	}
//
//	@Override
//	public boolean setFail(State s, boolean f) {
//		if(f) {
//			fails.add(s);
//			return states.add(s);
//		}
//		else
//			return fails.remove(s);
//	}

	@Override
	public boolean removeState(State s) {
		if(states.remove(s)) {
			Set<Transition> R = new HashSet<Transition>();
			for(Transition t : delta) {
				if(t.getSource().equals(s) || t.getDestination().equals(s))
					R.add(t);
			}
			delta.removeAll(R);
			return true;
		}
		else
			return false;
	}
	
	// Set<String> Gamma
	public DFAutomatonImpl toDFA(Set<String> Gamma) {
		/*
		 * From NFA to DFA (standard algorithm)
		 */
		
		MultiStateImpl msi = new MultiStateImpl(Closure(this.inits));
		
		DFAutomatonImpl dfa = new DFAutomatonImpl(msi);
		
		ArrayList<MultiStateImpl> todo = new ArrayList<MultiStateImpl>();
		
		todo.add(msi);
		
		while(!todo.isEmpty()) {
			MultiStateImpl curr = todo.remove(0);
			
			for(State s : curr.states) {
//				if(this.getFails().contains(s))
//					dfa.setFail(curr, true);
//				else 
				if(this.getFinals().contains(s))
					dfa.setFinal(curr, true);
			}
			
			for(String a : this.getAlphabet()) {
				if(a.compareTo(EPSILON) == 0)
					continue;
				
				ArrayList<State> mc;
				/*			
	 			* if(Gamma.contains(a))
					mc = GammaMove(curr.states, a);
				else
				*/
				mc = Closure(Move(curr.states, a));
				
				if(!mc.isEmpty()) {
					MultiStateImpl dest = new MultiStateImpl(mc);
					if(!dfa.getStates().contains(dest))
						todo.add(dest);
					dfa.addTransition(new TransitionImpl(curr, a, dest));
				}
			}
		}
		
		return dfa;
	}
	
	// Set<String> Gamma
	public DFAutomatonImpl specialDFA(Set<String> Gamma) {
		/*
		 * From NFA to DFA (special algorithm)
		 */
		
		MultiStateImpl msi = new MultiStateImpl(Closure(this.inits));
		
		DFAutomatonImpl dfa = new DFAutomatonImpl(msi);
		
		ArrayList<MultiStateImpl> todo = new ArrayList<MultiStateImpl>();
		
		todo.add(msi);
		
		SetUtils<State> util = new SetUtils<State>();
		
		while(!todo.isEmpty()) {
			MultiStateImpl curr = todo.remove(0);
			
			if(!util.intersection(this.getFinals(), curr.states).isEmpty())
				dfa.setFinal(curr, true);
			
			for(String a : this.getAlphabet()) {
				if(a.compareTo(EPSILON) == 0)
					continue;
				
				ArrayList<State> mc;
				if(Gamma.contains(a))
					mc = GammaMove(curr.states, a);
				else
					mc = SpecialMove(curr.states, a);
				
				if(!mc.isEmpty()) {
					MultiStateImpl dest = new MultiStateImpl(mc);
					if(!dfa.getStates().contains(dest))
						todo.add(dest);
					dfa.addTransition(new TransitionImpl(curr, a, dest));
				}
			}
		}
		
		return dfa;
	}
	
	private ArrayList<State> Closure(State s) {
		ArrayList<State> als = new ArrayList<State>();
		als.add(s);
		return Closure(als);
	}
	
	private ArrayList<State> Closure(ArrayList<State> inputStates)
	{
	    ArrayList<State> output = new ArrayList<State>();
	    output.addAll(inputStates);
	 
	    // Keeps states we are going to add later
	    while (true) {
	        ArrayList<State> statesToAdd = new ArrayList<State>();
	        for(State state : output)
	        {
	            for(Transition edge : this.getForwordStar(state))
	            {
	                if (edge.getLabel().compareTo(EPSILON) == 0)
	                {
	                    statesToAdd.add(edge.getDestination());
	                }
	            }
	        }
	        if(output.containsAll(statesToAdd))
	            break; // Exit loop if there are no states to add
	        output.addAll(statesToAdd); // Add all states to output
	    }
	    return output;
	}

	private ArrayList<Transition> getForwordStar(State state) {
		ArrayList<Transition> fs = new ArrayList<Transition>();
		for(Transition t : this.getTransitions()) {
			if(t.getSource().compareTo(state) == 0)
				fs.add(t);
		}
		return fs;
	}
	
	private ArrayList<State> Move(ArrayList<State> inputState, String label) {
		ArrayList<State> output = new ArrayList<State>();
	    for(State state : inputState)
	    {
	        for(Transition edge : this.getForwordStar(state))
	        {
	            if (edge.getLabel().compareTo(label) == 0)
	            {
	                output.add(edge.getDestination());
	            }
	        }
	    }
	    return Closure(output);
	}
	
	private ArrayList<State> SpecialMove(ArrayList<State> inputState, String label) {
		ArrayList<State> output = new ArrayList<State>();
	    for(State state : inputState)
	    {
	    	Set<State> dst = trans(state, label);
	    	if(dst.isEmpty())
	    		return new ArrayList<State>();

	    	output.addAll(dst);
	    }
	    if(output.containsAll(Closure(output)))
	    	return Closure(output);
	    else
	    	return new ArrayList<State>();
	}
	
	private ArrayList<State> GammaMove(ArrayList<State> inputState, String label) {
		ArrayList<State> output = new ArrayList<State>();
	    for(State state : inputState)
	    {
	        for(Transition edge : this.getForwordStar(state))
	        {
	            if (edge.getLabel().compareTo(label) == 0)
	            {
	                output.add(edge.getDestination());
	            }
	        }
	    }
	    return Closure(output);
	}
}
