package it.unige.automata.impl;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.util.SetUtils;

public class DFAutomatonImpl implements Automaton {

	Set<State> states;
	State inits;
	Set<State> finals;
	Set<State> fails;
	Set<Transition> delta;
	
	public DFAutomatonImpl(State init) {
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
		assert(t.getLabel().compareTo(Automaton.EPSILON) != 0);
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
				if(t.getSource().equals(s) || t.getDestination().equals(s))
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
		
		while(!W.isEmpty()) {
			Set<State> A = Util.pick(W);
			for(String c : this.getAlphabet()) {
				HashSet<State> X = new HashSet<State>();
				for(State s : this.getStates()) {
					if(A.contains(this.trans(s, c))) {
						X.add(s);
					}
				}
				
				for(Set<State> Y : P) {
					Set<State> CAP = util.intersection(X, Y);
					Set<State> MIN = util.minus(Y, X);
					
					if(CAP.isEmpty())
						continue;
					if(MIN.isEmpty())
						continue;
					
					P.remove(Y);
					P.add(CAP);
					P.add(MIN);
					
					if(W.contains(Y)) {
						W.remove(Y);
						W.add(CAP);
						W.add(MIN);
					}
					else {
						if(CAP.size() <= MIN.size())
							W.add(CAP);
						else
							W.add(MIN);
					}
				}
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
	
}
