package it.unige.automata.event;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.util.SetUtils;
import it.unige.parteval.Projection;

public class EventAutomaton implements Automaton<SymbolicTransition> {

	Set<State> states;
	State inits;
	Set<State> finals;
	Set<SymbolicTransition> delta;
	
	public EventAutomaton(State init) {
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
	public boolean addTransition(State s, String l, State d) {
		return this.addTransition(new SymbolicTransition(s, l, "_", null, d)); 
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
		throw new UnsupportedOperationException("No standard transition for Event Automata");
	}
	
	public Set<SymbolicTransition> forwardStar(State src, String a) {

		Set<SymbolicTransition> fs = new HashSet<>();
		
		for(SymbolicTransition t : delta) 
			if(t.getSource().equals(src) && t.getLabel().compareTo(a) == 0)
				fs.add(t);
		
		return fs;
	}

	@Override
	public String toString() {
		return "EventAutomaton [inits=" + inits + ", finals=" + finals + ", delta=" + delta + "]";
	}

	@Override
	public boolean removeState(State s) {
		if(states.remove(s)) {
			Set<Transition> R = new HashSet<Transition>();
			for(Transition t : delta) {
				if(t.getSource().compareTo(s) == 0 || t.getDestination().compareTo(s) == 0)
					R.add(t);
			}
			delta.removeAll(R);
			return true;
		}
		else
			return false;
	}
	
	public void renameStates(String base) {
		int i = 0;
		for(State s : this.states) {
			s.setLabel(base + (i++));
		}
	}
	

	public static EventAutomaton parallel(EventAutomaton A, EventAutomaton B, Set<String> Gamma) {
		
		State init = Projection.prod(A.inits, B.inits);
		
		Set<String> Sigma = new HashSet<>();
		Sigma.addAll(Gamma);
		Sigma.addAll(A.getAlphabet());
		Sigma.addAll(B.getAlphabet());
		
		EventAutomaton AB = new EventAutomaton(init);
		
		for(State p : A.getStates()) {
			for(State q : B.getStates()) {
				for(String a : Sigma) {
					if(Gamma.contains(a)) {								
						
						for(SymbolicTransition tA : A.forwardStar(p, a)) {
							for(SymbolicTransition tB : B.forwardStar(q, a)) {
								AB.addTransition(
										new SymbolicTransition(
												Projection.prod(p, q), 
												a, 
												tA.getVariable(),
												G.and(G.and(tA.getGuard(), tB.getGuard()), G.eq(G.var(tA.getVariable()), G.var(tB.getVariable()))),
												Projection.prod(tA.getDestination(), tB.getDestination())
															)
												);
							}
						}
					}
					else {
						if(A.getAlphabet().contains(a)) {
							for(SymbolicTransition tA : A.forwardStar(p, a)) {
								AB.addTransition(
									new SymbolicTransition(
											Projection.prod(p, q), 
											a, 
											tA.getVariable(),
											tA.getGuard(),
											Projection.prod(tA.getDestination(), q)
														)
											);
							}
						}
						// else?
						if(B.getAlphabet().contains(a)) {
							for(SymbolicTransition tB : B.forwardStar(q, a)) {
								AB.addTransition(
										new SymbolicTransition(
												Projection.prod(p, q), 
												a, 
												tB.getVariable(),
												tB.getGuard(),
												Projection.prod(p, tB.getDestination())
															)
												);
							}
						}
					}
				}
			}
		}
		
		for(State p : A.getFinals())
			for(State q : B.getFinals())
				AB.setFinal(Projection.prod(p, q), true);
		
		
		return AB;
		
	}

	public void removeTransitions(Set<SymbolicTransition> toCut) {
		for(SymbolicTransition t : toCut) {
			removeTransition(t);
		}
	}

	public void removeStates(Set<State> toRmv) {
		for(State s : toRmv) {
			removeState(s);
		}
	}
	
	public static EventAutomaton control(EventAutomaton A, EventAutomaton C, Set<String> controls) {
		
		EventAutomaton CA = parallel(A, C, controls);
		
		Set<String> uncontrollable = new HashSet<String>();
		uncontrollable.addAll(A.getAlphabet());
		uncontrollable.removeAll(controls);
		
		
		Set<State> pits = getPits(CA, uncontrollable);
		
		while(!pits.isEmpty()) {
			CA.removeStates(pits);
			pits = getPits(CA, uncontrollable);
		}
		
		return CA;		
	}

	private static Set<State> getPits(EventAutomaton CA, Set<String> U) {

		Set<State> pits = new HashSet<State>();
		
		for(State s : CA.getStates()) {
			if(CA.getFinals().contains(s))
				continue;
			
			boolean pit = true;
			
			for(Transition t : CA.getTransitions()) {
				if(t.getDestination().compareTo(s) != 0)
					continue;
				
				if(t.getSource().compareTo(s) == 0)
					continue;
				
				if(U.contains(t.getLabel()))
					continue;
				
				pit = false;
				break;
			}
			
			if(pit) {
				pits.add(s);
			}
		}
		
		return pits;

	}

	@Override
	public boolean addTransition(SymbolicTransition t) {
		
		states.add(t.getDestination());
		states.add(t.getSource());
		return delta.add(t);
	}

	@Override
	public void removeTransition(SymbolicTransition t) {
		delta.remove(t);
	}

	@Override
	public Set<SymbolicTransition> getTransitions() {
		return delta;
	}

	
}
