package it.unige.automata.impl;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.util.SetUtils;
import it.unige.parteval.Projection;

public class DFAutomatonImpl implements Automaton<TransitionImpl> {

	Set<State> states;
	State inits;
	Set<State> finals;
	Set<State> fails;
	Set<TransitionImpl> delta;
	
	public DFAutomatonImpl(State init) {
		states = new HashSet<State>();
		inits = init;
		finals = new HashSet<State>();
		fails = new HashSet<State>();
		delta = new HashSet<TransitionImpl>();
		
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
	public Set<TransitionImpl> getTransitions() {
		return delta;
	}
	
	@Override
	public void removeTransition(TransitionImpl t) {
		delta.remove(t);
	}

	@Override
	public boolean addTransition(TransitionImpl t) {
		assert(t.getLabel().compareTo(Automaton.EPSILON) != 0);
		this.addState(t.getSource());
		this.addState(t.getDestination());
		return delta.add(t);
	}
	
	@Override
	public boolean addTransition(State s, String l, State d) {
		return this.addTransition(new TransitionImpl(s, l, d)); 
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
		
		assert(S.size() <= 1);
		
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
				if(t.getSource().compareTo(s) == 0 || t.getDestination().compareTo(s) == 0)
					R.add(t);
			}
			delta.removeAll(R);
			return true;
		}
		else
			return false;
	}
	
	// Hopcroft's algorithm
	public DFAutomatonImpl minimize() {
		
		Set<Set<State>> P = new HashSet<Set<State>>();
		Set<Set<State>> W = new HashSet<Set<State>>();
		
		P.add(this.getFinals());
		HashSet<State> tmp = new HashSet<State>();
		tmp.addAll(this.getStates());
		tmp.removeAll(this.getFinals());
		P.add(tmp);
		SetUtils<Set<State>> Util = new SetUtils<Set<State>>();
		SetUtils<State> util = new SetUtils<State>();
		
		W.add(this.getFinals());
		W.add(tmp);
		
		while(!W.isEmpty()) {
			
			// System.out.println(W.size());
			
			Set<State> S = Util.pick(W);
			for(String c : this.getAlphabet()) {
			
				HashSet<State> Ia = new HashSet<State>();
				for(State s : this.getStates()) {
					if(util.containsAll(S,this.trans(s, c))) {
						Ia.add(s);
					}
				}
				
				Set<Set<State>> newP = new HashSet<Set<State>>();
				for(Set<State> R : P) {
					Set<State> R1 = util.intersection(Ia, R);
					Set<State> R2 = util.minus(R, R1);
					
					if(R1.isEmpty()) {
						newP.add(R);
						continue;
					}
					if(R2.isEmpty()) {
						newP.add(R);
						continue;
					}
					
					newP.remove(R);
					newP.add(R1);
					newP.add(R2);
					
					if(W.contains(R)) {
						W.remove(R);
						W.add(R1);
						W.add(R2);
					}
					else {
						if(R1.size() <= R2.size())
							W.add(R1);
						else
							W.add(R2);
					}
				}
				
				P = newP;
			}
		}
		
		MultiStateImpl i = null;
		for(Set<State> S : P) {
			if(S.contains(this.getInitial())) {
				i = new MultiStateImpl(S);
			}
		}
		
		DFAutomatonImpl M = new DFAutomatonImpl(i);
		
		for(Set<State> S : P) {
			State s = util.sample(S);
			for(String a : this.getAlphabet()) {
				State dst = util.sample(this.trans(s, a));
				if(dst != null) {
					for(Set<State> R : P) {
						if(R.contains(dst))
							M.addTransition(new TransitionImpl(new MultiStateImpl(S), a, new MultiStateImpl(R)));
					}
				}
			}
		}
		
		for(Set<State> S : P) {
			if(!util.intersection(S, this.getFinals()).isEmpty())
				M.setFinal(new MultiStateImpl(S), true);
		}
		
//		for(Set<State> S : P) {
//			if(!util.intersection(S, this.getFails()).isEmpty())
//				M.setFail(new MultiStateImpl(S), true);
//		}
		
		return M;
			
	}

	public void renameStates(String base) {
		int i = 0;
		for(State s : this.states) {
			s.setLabel(base + (i++));
		}
	}
	
	public void complete(Set<String> Gamma) {
		
		State pit = new StateImpl(FAIL);
		this.addState(pit);
		
		for(State s : this.getStates()) {
			for(String a : this.getAlphabet()) {
				if(trans(s, a).isEmpty() && !Gamma.contains(a))
					this.addTransition(new TransitionImpl(s, a, pit));
			}
		}
	}
	
	/*
	 * Collapses all the pits in a single state
	 */
	public void collapse() {
		Set<State> toKeep = new HashSet<State>();
		toKeep.addAll(this.getFinals());
		
		boolean mod;
		do {
			mod = false;
			for(State s : getStates()) {
				if(!toKeep.contains(s)) {
					for(String a : getAlphabet()) {
						Set<State> reach = trans(s,a);
						reach.retainAll(toKeep);
						if(!reach.isEmpty()) {
							mod = true;
							toKeep.add(s);
						}
					}
				}
			}
		} while(mod);
		
		Set<State> toCollapse = new HashSet<State>();
		toCollapse.addAll(getStates());
		toCollapse.removeAll(toKeep);
		
		if(toCollapse.size() < 2)
			return;
		
		State ff = new StateImpl(FAIL);
		
		Set<Transition> toRemove = new HashSet<Transition>();
		Set<TransitionImpl> toAdd = new HashSet<TransitionImpl>();
		
		for(Transition t: getTransitions()) {
			if(toCollapse.contains(t.getDestination())) {
				toRemove.add(t);
				if(toCollapse.contains(t.getSource())) {
					toAdd.add(new TransitionImpl(ff, t.getLabel(), ff));
				}
				else {
					toAdd.add(new TransitionImpl(t.getSource(), t.getLabel(), ff));
				}
			}
		}
		
		this.states.removeAll(toCollapse);
		this.delta.removeAll(toRemove);	
		this.delta.addAll(toAdd);
	}
	
	public static DFAutomatonImpl parallel(DFAutomatonImpl A, DFAutomatonImpl B, Set<String> Gamma) {
		
		State init = Projection.prod(A.inits, B.inits);
		
		Set<String> Sigma = new HashSet<>();
		Sigma.addAll(Gamma);
		Sigma.addAll(A.getAlphabet());
		Sigma.addAll(B.getAlphabet());
		
		NFAutomatonImpl nfa = new NFAutomatonImpl(init);
		
		for(State p : A.getStates()) {
			for(State q : B.getStates()) {
				for(String a : Sigma) {
					if(Gamma.contains(a)) {								
						for(State pp : A.trans(p, a)) {
							for(State qq : B.trans(q, a)) {
								nfa.addTransition(Projection.prod(p, q), a, Projection.prod(pp, qq));
							}
						}
					}
					else {
						if(A.getAlphabet().contains(a)) {
							for(State pp : A.trans(p, a)) {
									nfa.addTransition(Projection.prod(p, q), a, Projection.prod(pp, q));
							}
						}
						if(B.getAlphabet().contains(a)) {
							for(State qq : B.trans(q, a)) {
								nfa.addTransition(Projection.prod(p, q), a, Projection.prod(p, qq));
							}
						}
					}
				}
			}
		}
		
		for(State p : A.getFinals())
			for(State q : B.getFinals())
				nfa.setFinal(Projection.prod(p, q), true);
		
		DFAutomatonImpl AB = nfa.toDFA();
				//.minimize(); // too costly
		// AB.collapse(); // perhaps unnecessary
		return AB;
		
	}

	public void removeTransitions(Set<TransitionImpl> toCut) {
		for(TransitionImpl t : toCut) {
			removeTransition(t);
		}
	}

	public void removeStates(Set<State> toRmv) {
		for(State s : toRmv) {
			removeState(s);
		}
	}
	
	public static DFAutomatonImpl control(DFAutomatonImpl A, DFAutomatonImpl C, Set<String> controls) {
		
		DFAutomatonImpl CA = parallel(A, C, controls);
		
		NFAutomatonImpl nfa = new NFAutomatonImpl(CA.inits);
		
		for(TransitionImpl t : CA.getTransitions()) {
			if(!controls.contains(t.getLabel())) {
				t.label = EPSILON;
			}
			nfa.addTransition(t);
		}
		
		for(State s : CA.getFinals()) {
			nfa.setFinal(s, true);
		}
		
		CA = nfa.toDFA();
		
		Set<String> uncontrollable = new HashSet<String>();
		uncontrollable.addAll(A.getAlphabet());
		uncontrollable.removeAll(controls);
		
		Set<State> pits = getPits(CA, uncontrollable);
		
		while(!pits.isEmpty()) {
			CA.removeStates(pits);
			pits = getPits(CA, uncontrollable);
		}
		
		return CA.minimize();
		
	}

	private static Set<State> getPits(DFAutomatonImpl CA, Set<String> U) {

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
}
