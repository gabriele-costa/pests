package it.unige.parteval;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert.*;
import org.junit.Test;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.event.EventAutomaton;
import it.unige.automata.event.G;
import it.unige.automata.event.Guard;
import it.unige.automata.event.NEventAutomaton;
import it.unige.automata.event.ClosureRec;
import it.unige.automata.event.SymbolicTransition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.MultiStateImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.SetUtils;

public class Projection {

public static NFAutomatonImpl partial(Automaton<TransitionImpl> P, Automaton<TransitionImpl> A, Set<String> SigmaB, Set<String> Gamma) {
		
		
		addGammaLoops(P, Gamma);
		
		State i = prod(P.getInitial(), A.getInitial());
		NFAutomatonImpl B = new NFAutomatonImpl(i);
		
		State ffstate = new StateImpl("ff");

		for(State fa : A.getFinals())
			for(State fp : P.getFinals())
				//if(!P.getFails().contains(fp))
					B.setFinal(prod(fp, fa), true);
		
		Set<String> Sigma = new HashSet<>();
		Sigma.addAll(A.getAlphabet());
		Sigma.addAll(SigmaB);
		
		for(State qp : P.getStates()) {
			for(State qa : A.getStates()) {
				for(String a : Sigma ) {
					Set<State> Qpp = P.trans(qp, a);
					
					// if Qpp is empty == false
					if(Qpp.isEmpty())
						B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
										
					for(State qpp : Qpp) {
						
						if(!Gamma.contains(a)) {
							
							if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
								for(State qap : A.trans(qa, a))
									B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
							}
							
							if(SigmaB.contains(a)) { 		// a in SigmaB \ Gamma
								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
							}								
						}
						else { 									// a in Gamma
							for(State qap : A.trans(qa, a)) {
								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp,qap)));
							}
						}
					}
				}
//				if(P.getFails().contains(qp) || A.getFails().contains(qa))
//					B.setFail(prod(qp, qa), true);
			}
		}
		
		// removeGammaLoops(P, Gamma);
		// removeGammaLoops(B, Gamma);
		
		makeSelfLoops(B, ffstate, B.getAlphabet());
		
		return B;
	}

	public static NFAutomatonImpl partial(Automaton<TransitionImpl> P, Automaton<TransitionImpl> A, Set<String> Gamma) {
		return partial(P, A, A.getAlphabet(), Gamma);
	}

	public static State prod(State p, State q) {
		return new StateImpl("" + p.getLabel()+q.getLabel() + "");
	}
	
	public static void addGammaLoops(Automaton<TransitionImpl> A, Set<String> G) {
		
		for(State q : A.getStates()) {
			for(String g : G) {
				if(A.trans(q, g).isEmpty()) 
					A.addTransition(new TransitionImpl(q, g, q));
			}
		}
		
	}

	private static void makeSelfLoops(Automaton<TransitionImpl> P, State q, Set<String> Act) {
		for(String a : Act) {
			if(P.trans(q, a).isEmpty())
				P.addTransition(new TransitionImpl(q, a, q));
		}
	}
	
	private static void removeGammaLoops(Automaton<Transition> A, Set<String> g) {
		
		HashSet<Transition> toRmv = new HashSet<Transition>();
		
		for(Transition t : A.getTransitions()) {
			if(t.getSource().equals(t.getDestination()) && g.contains(t.getLabel()))
				toRmv.add(t);
		}
		
		for(Transition t : toRmv) {
			A.removeTransition(t);
		}
		
	}
	
	public static void makeController(DFAutomatonImpl A, Set<String> SigmaC) {
		Set<State> toRmv = new HashSet<State>();
		

		do {
			toRmv.clear();
			toRmv.addAll(getPits(A));
			
			boolean found = true;
			while(found) {
				found = false;
				Set<TransitionImpl> toCut = new HashSet<>();
				for(TransitionImpl t : A.getTransitions()) {
					if(toRmv.contains(t.getDestination())) {
						toCut.add(t);
						if(!SigmaC.contains(t.getLabel())) {
							toRmv.add(t.getSource());
							found = true;
						}
					}
				}
				
				A.removeTransitions(toCut);
				A.removeStates(toRmv);
			}
		
			A.minimize();
		} while(!toRmv.isEmpty());
	}

	private static Set<State> getPits(DFAutomatonImpl A) {
		Set<State> pits = new HashSet<>();
		for(Transition t : A.getTransitions()) {
			if(!A.getFinals().contains(t.getDestination()))
				pits.add(t.getDestination());
		}
		
		for(Transition t : A.getTransitions()) {
			if(!(t.getSource().equals(t.getDestination())))
				pits.remove(t.getSource());
		}
		return pits;
	}
	
	/**
	 * Official algorithm for EventAutomata
	 */
	public static NEventAutomaton partialS(EventAutomaton P, EventAutomaton A, Set<String> SigmaB, Set<String> Gamma) {
		
		
		State i = prod(P.getInitial(), A.getInitial());
		NEventAutomaton B = new NEventAutomaton(i);
		
//		State ffstate = new StateImpl(Automaton.FAIL);

		for(State fa : A.getFinals())
			for(State fp : P.getFinals())
				B.setFinal(prod(fp, fa), true);
		
		Set<String> Sigma = new HashSet<>();
		Sigma.addAll(A.getAlphabet());
		Sigma.addAll(SigmaB);
		
		Set<State> violating = new HashSet<State>();
		
		Set<State> Qp = P.getStates();
		
		for(State qp : Qp) {
			for(State qa : A.getStates()) {
				for(String a : Sigma ) {
					Set<SymbolicTransition> Tpp = P.forwardStar(qp, a);
					Set<SymbolicTransition> Tap = A.forwardStar(qa, a);
					
					// if Qpp is empty == false
					if(Tpp.isEmpty()) {
						if(!Tap.isEmpty() && !Gamma.contains(a))
							violating.add(prod(qp,qa));
						continue;
					}
//						B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
										
					for(SymbolicTransition tpp : Tpp) {
						
						if(!Gamma.contains(a)) {
							if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
								for(SymbolicTransition tap : Tap)
									B.addTransition(
											new SymbolicTransition(
													prod(qp,qa), 
													Automaton.EPSILON, 
													tpp.getVariable(),
													G.and(G.and(tpp.getGuard(), tap.getGuard()), G.eq(G.var(tpp.getVariable()), G.var(tap.getVariable()))), 
													prod(tpp.getDestination(), tap.getDestination())));
							}
							// else?
							if(SigmaB.contains(a)) { 			// a in SigmaB \ Gamma
								B.addTransition(new SymbolicTransition(
										prod(qp,qa), 
										a,
										tpp.getVariable(),
										tpp.getGuard(), 
										prod(tpp.getDestination(), qa)));
							}								
						}
						else { 									// a in Gamma
							for(SymbolicTransition tap : Tap) {
								B.addTransition(new SymbolicTransition(
										prod(qp,qa), 
										a,
										tpp.getVariable(),
										G.and(G.and(tpp.getGuard(), tap.getGuard()), G.eq(G.var(tpp.getVariable()), G.var(tap.getVariable()))), 
										prod(tpp.getDestination(),tap.getDestination())));
							}
						}
					}
				}
			}
		}
		
		for(State u : B.getStates()) {
			ClosureRec C = B.Closure(u);
			C.states.retainAll(violating);
			if(!C.states.isEmpty()) {
				violating.add(u);	
			}
		}
		
		for(State v : violating) {
			B.removeState(v);
		}
		
		return B;
	}
	
	/**
	 * Official algorithm for finite state automata
	 */
	public static NFAutomatonImpl partialA(DFAutomatonImpl P, DFAutomatonImpl A, Set<String> SigmaB, Set<String> Gamma) {
		// TODO
		
		State i = prod(P.getInitial(), A.getInitial());
		NFAutomatonImpl B = new NFAutomatonImpl(i);
		
//		State ffstate = new StateImpl(Automaton.FAIL);

		for(State fa : A.getFinals())
			for(State fp : P.getFinals())
				B.setFinal(prod(fp, fa), true);
		
		Set<String> Sigma = new HashSet<>();
		Sigma.addAll(A.getAlphabet());
		Sigma.addAll(SigmaB);
		
		Set<State> violating = new HashSet<State>();
		
		for(State qp : P.getStates()) {
			for(State qa : A.getStates()) {
				for(String a : Sigma ) {
					Set<State> Qpp = P.trans(qp, a);
					Set<State> Qap = A.trans(qa, a);
					
					// if Qpp is empty == false
					if(Qpp.isEmpty()) {
						if(!Qap.isEmpty() && !Gamma.contains(a))
							violating.add(prod(qp,qa));
						continue;
					}
//						B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
										
					for(State qpp : Qpp) {
						
						if(!Gamma.contains(a)) {
							if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
								for(State qap : Qap)
									B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
							}
							// else?
							if(SigmaB.contains(a)) { 			// a in SigmaB \ Gamma
								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
							}								
						}
						else { 									// a in Gamma
							for(State qap : Qap) {
								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp,qap)));
							}
						}
					}
				}
			}
		}
		
		for(State u : B.getStates()) {
			Set<State> C = B.Closure(u);
			C.retainAll(violating);
			if(!C.isEmpty()) {
				violating.add(u);	
			}
		}
		
		for(State v : violating) {
			B.removeState(v);
		}
		
		return B;
	}

	/**
	 * From NFA to DFA (special algorithm)
	 */
	public static DFAutomatonImpl unify(NFAutomatonImpl B, Set<String> G) {
		
		Set<State> I = new HashSet<State>();
		I.addAll(B.Closure(B.getInitial()));
		MultiStateImpl msi = new MultiStateImpl(I);
		
		DFAutomatonImpl dfa = new DFAutomatonImpl(msi);
		
		ArrayList<MultiStateImpl> todo = new ArrayList<MultiStateImpl>();
		
		todo.add(msi);
		
		while(!todo.isEmpty()) {
			MultiStateImpl curr = todo.remove(0);
			
			Set<State> tmp, ff;
			ff = new HashSet<>();
			
			ff.addAll(curr.states);
			ff.removeAll(B.getFinals());
			
			if(ff.isEmpty())
				dfa.setFinal(curr, true);
			
			for(String a : B.getAlphabet()) {
				if(a.compareTo(Automaton.EPSILON) == 0)
					continue;
				
				HashSet<State> mc;
				if(!G.contains(a))
					mc = AndMove(B, curr.states, a);
				else
					mc = OrMove(B, curr.states, a);
				
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
	

	/**
	 * From eNEA to DEA (special algorithm)
	 */
	public static EventAutomaton unifyS(NEventAutomaton B, Set<String> Gamma) {
		
		Set<State> I = new HashSet<State>();
		I.addAll(B.Closure(B.getInitial()).states);
		MultiStateImpl msi = new MultiStateImpl(I);
		
		EventAutomaton dfa = new EventAutomaton(msi);
		
		ArrayList<MultiStateImpl> todo = new ArrayList<MultiStateImpl>();
		
		todo.add(msi);
		
		while(!todo.isEmpty()) {
			MultiStateImpl curr = todo.remove(0);
			
			Set<State> tmp, ff;
			ff = new HashSet<>();
			
			ff.addAll(curr.states);
			ff.removeAll(B.getFinals());
			
			if(ff.isEmpty())
				dfa.setFinal(curr, true);
			
			for(String a : B.getAlphabet()) {
				if(a.compareTo(Automaton.EPSILON) == 0)
					continue;
				
				Set<SymbolicTransition> mc;
				if(!Gamma.contains(a))
					mc = AndMoveS(B, curr.states, a);
				else
					mc = OrMoveS(B, curr.states, a);
				
				if(!mc.isEmpty()) {
					
					Set<State> ds = new HashSet<State>();
					
					String var = null;
					Guard g = null;
					
					for(SymbolicTransition st : mc) {
						ds.add(st.getDestination());
						
						if(g == null) {
							g = st.getGuard();
						}
						else {
							g = G.and(g, st.getGuard());
						}
						
						if(st.getLabel().compareTo(Automaton.EPSILON) == 0) 
							continue;
						
						if(var == null) {
							var = st.getVariable();
						}
						else {
							g = G.and(g, G.eq(G.var(var), G.var(st.getVariable())));
						}
					}
					
					MultiStateImpl dest = new MultiStateImpl(ds);
					if(!dfa.getStates().contains(dest))
						todo.add(dest);
					dfa.addTransition(
							new SymbolicTransition(
									curr, 
									a, 
									var,
									g,
									dest));
				}
			}
		}
		
		return dfa;
	}

	private static HashSet<SymbolicTransition> OrMoveS(NEventAutomaton B, HashSet<State> states, String a) {
		HashSet<SymbolicTransition> output = new HashSet<SymbolicTransition>();
	    for(State state : states)
	    {
	        for(SymbolicTransition edge : B.getForwardStar(state, a))
	        {
	            if (edge.getLabel().compareTo(a) == 0)
	            {
	                output.add(edge);
	            }
	        }
	    }
	    
	    return B.Closure(output);
	}
	
	private static HashSet<SymbolicTransition> AndMoveS(NEventAutomaton B, HashSet<State> states, String a) {
		HashSet<SymbolicTransition> output = new HashSet<SymbolicTransition>();
	    boolean first = true;
	    
		for(State state : states)
	    {
			ArrayList<SymbolicTransition> dst = B.getForwardStar(state, a);
	    	if(first) {
	    		first = false;
	    		output.addAll(dst);
	    	}
	    	else {
	    		output = conjunction(output, dst);
	    	}
	    }
		return B.Closure(output);
	}
	
	private static HashSet<SymbolicTransition> conjunction(HashSet<SymbolicTransition> T, ArrayList<SymbolicTransition> R) {
		
		HashSet<SymbolicTransition> output = new HashSet<>();
		
		for(SymbolicTransition t : T) {
			for(SymbolicTransition r : R) {
				if(t.getLabel().compareTo(r.getLabel()) == 0) {
					if(t.getDestination().equals(r.getDestination())) {
						output.add(t);
						output.add(r);
					}
				}
			}
		}
		
		return output;
	}

	private static HashSet<State> OrMove(NFAutomatonImpl B, HashSet<State> states, String a) {
		HashSet<State> output = new HashSet<State>();
	    for(State state : states)
	    {
	        for(Transition edge : B.getForwardStar(state))
	        {
	            if (edge.getLabel().compareTo(a) == 0)
	            {
	                output.add(edge.getDestination());
	            }
	        }
	    }
	    return B.Closure(output);
	}

	private static HashSet<State> AndMove(NFAutomatonImpl B, HashSet<State> states, String a) {
		HashSet<State> output = new HashSet<State>();
	    boolean first = true;
		for(State state : states)
	    {
			Set<State> dst = B.trans(state, a);
	    	if(first) {
	    		first = false;
	    		output.addAll(dst);
	    	}
	    	else {
	    		output.retainAll(dst);
	    	}
	    }
		return B.Closure(output);
	}
}
