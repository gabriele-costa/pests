package it.unige.automata.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.MultiStateImpl;
import it.unige.automata.impl.TransitionImpl;

public class NEventAutomaton implements Automaton<SymbolicTransition> {

	private static int cnt = 0;
	
	Set<State> states;
	State inits;
	Set<State> finals;
	Set<SymbolicTransition> delta;
	
	public NEventAutomaton(State init) {
		states = new HashSet<State>();
		inits = init;
		finals = new HashSet<State>();
		delta = new HashSet<SymbolicTransition>();
		
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
			return finals.remove(s);
	}

	@Override
	public Set<State> getStates() {
		return states;
	}

	@Override
	public Set<SymbolicTransition> getTransitions() {
		return delta;
	}

	@Override
	public boolean addTransition(SymbolicTransition t) {
		this.addState(t.getSource());
		this.addState(t.getDestination());
		return delta.add(t);
	}
	
	@Override
	public boolean addTransition(State s, String l, State d) {
		throw new UnsupportedOperationException("Only symbolic transitions in Event Automata");
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
		return "DFAutomatonImpl [inits=" + inits + ", finals=" + finals + ", delta=" + delta + "]";
	}

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
	/**
	 * From eNEA to DEA (standard algorithm)
	 */
	public EventAutomaton toEA() {
		
		ClosureRec initclos = Closure(this.inits);
		
		MultiStateImpl msi = new MultiStateImpl(initclos.states);
		
		EventAutomaton ea = new EventAutomaton(msi);
		
		ArrayList<ClosureRec> todo = new ArrayList<ClosureRec>();
		
		todo.add(initclos);
		
		while(!todo.isEmpty()) {
			ClosureRec curr = todo.remove(0);
			
			State src = new MultiStateImpl(curr.states);
			
			for(State s : curr.states) {
				if(this.getFinals().contains(s))
					ea.setFinal(src, true);
			}
			
			for(String a : this.getAlphabet()) {
				if(a.compareTo(EPSILON) == 0)
					continue;
				
				ClosureRec mc = Closure(Move(curr.states, a).states);
				
				if(!mc.states.isEmpty()) {
					MultiStateImpl dest = new MultiStateImpl(mc.states);
					if(!ea.getStates().contains(dest))
						todo.add(mc);
					
					String x = fresh();
					Guard g = G.and(mc.guard, makeGuard(a, x, curr.states));
					ea.addTransition(new SymbolicTransition(src, a, x, g, dest));
				}
			}
		}
		
		return ea;
	}
	
	private static String fresh() {
		return "fs" + (cnt++);
	}

	private Guard makeGuard(String a, String x, HashSet<State> states) {
		
		ArrayList<SymbolicTransition> fs = new ArrayList<>();
		for(State s : states)
			fs.addAll(getForwardStar(s, a));
		
		Guard g;
		if(fs.isEmpty())
			g = G.tt();
		else {
			g = fs.get(0).getGuard();
			for(int i = 1; i < fs.size(); i++) {
				g = G.and(g, fs.get(i).getGuard());
			}
		}
		
		return g;
	}

	public ClosureRec Closure(State s) {
		HashSet<State> als = new HashSet<State>();
		als.add(s);
		return Closure(als);
	}
	
	public HashSet<SymbolicTransition> Closure(HashSet<SymbolicTransition> input) {
		HashSet<SymbolicTransition> closure = new HashSet<>();
		
		closure.addAll(input);
		
		// Keeps states we are going to add later
	    while (true) {
	    	HashSet<SymbolicTransition> TtoAdd = new HashSet<>();
			for(SymbolicTransition trans : closure)
	        {
	            for(SymbolicTransition edge : this.getForwardStar(trans.getDestination()))
	            {
	                if (edge.getLabel().compareTo(EPSILON) == 0)
	                {
	                    TtoAdd.add(edge);
	                }
	            }
	        }
	        if(closure.containsAll(TtoAdd))
	            break; // Exit loop if there are no states to add
	        closure.addAll(TtoAdd); // Add all states to output
	    }
	    return closure;
	}
	
	public ClosureRec Closure(Set<State> inputStates)
	{
		ClosureRec output = new ClosureRec();
	    output.states.addAll(inputStates);
	 
	    // Keeps states we are going to add later
	    while (true) {
	        ArrayList<State> statesToAdd = new ArrayList<State>();
	        for(State state : output.states)
	        {
	            for(Transition edge : this.getForwardStar(state))
	            {
	                if (edge.getLabel().compareTo(EPSILON) == 0)
	                {
	                    statesToAdd.add(edge.getDestination());
	                }
	            }
	        }
	        if(output.states.containsAll(statesToAdd))
	            break; // Exit loop if there are no states to add
	        output.states.addAll(statesToAdd); // Add all states to output
	    }
	    return output;
	}

	public ArrayList<SymbolicTransition> getForwardStar(State state) {
		ArrayList<SymbolicTransition> fs = new ArrayList<>();
		for(SymbolicTransition t : this.getTransitions()) {
			if(t.getSource().compareTo(state) == 0)
				fs.add(t);
		}
		return fs;
	}
	
	public ArrayList<SymbolicTransition> getForwardStar(State state, String label) {
		ArrayList<SymbolicTransition> fs = new ArrayList<>();
		for(SymbolicTransition t : this.getTransitions()) {
			if(t.getSource().compareTo(state) == 0 && t.getLabel().compareTo(label) == 0)
				fs.add(t);
		}
		return fs;
	}
	
	private ClosureRec Move(HashSet<State> inputState, String label) {
		HashSet<State> output = new HashSet<State>();
	    for(State state : inputState)
	    {
	        for(Transition edge : this.getForwardStar(state))
	        {
	            if (edge.getLabel().compareTo(label) == 0)
	            {
	                output.add(edge.getDestination());
	            }
	        }
	    }
	    return Closure(output);
	}
	
	private ClosureRec SpecialMove(HashSet<State> inputState, String label) {
		ClosureRec output = new ClosureRec();
	    for(State state : inputState)
	    {
	    	Set<State> dst = trans(state, label);
	    	if(dst.isEmpty())
	    		return new ClosureRec();

	    	output.states.addAll(dst);
	    }
	    if(output.states.containsAll(Closure(output.states).states))
	    	return output;
	    else
	    	return new ClosureRec();
	}
	
	private ClosureRec GammaMove(HashSet<State> inputState, String label) {
		HashSet<State> output = new HashSet<State>();
	    for(State state : inputState)
	    {
	        for(Transition edge : this.getForwardStar(state))
	        {
	            if (edge.getLabel().compareTo(label) == 0)
	            {
	                output.add(edge.getDestination());
	            }
	        }
	    }
	    return Closure(output);
	}

	@Override
	public void removeTransition(SymbolicTransition t) {
		delta.remove(t);
	}
}
