package it.unige.parteval;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class Projection {

public static NFAutomatonImpl partial(Automaton P, Automaton A, Set<String> SigmaB, Set<String> Gamma) {
		
		
		addGammaLoops(P, Gamma);
		
		State i = prod(P.getInitial(), A.getInitial());
		NFAutomatonImpl B = new NFAutomatonImpl(i);
		
		State ffstate = new StateImpl("ff");

		for(State fa : A.getFinals())
			for(State fp : P.getFinals())
				//if(!P.getFails().contains(fp))
					B.setFinal(prod(fp, fa), true);
		
		for(State qp : P.getStates()) {
			for(State qa : A.getStates()) {
				for(String a : P.getAlphabet() ) {
					Set<State> Qpp = P.trans(qp, a);
					
					// if Qpp is empty == false
					if(Qpp.isEmpty())
						B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
										
					for(State qpp : Qpp) {
						
						Set<State> Qap = A.trans(qa, a);
						
						if(!Gamma.contains(a)) {
							if(SigmaB.contains(a)) { 			// a in SigmaB \ Gamma
								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
							}						
							
							if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
								for(State qap : Qap)
									B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
							}
						}
						else { 									// a in Gamma
							for(State qap : Qap) {
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

	public static NFAutomatonImpl partial(Automaton P, Automaton A, Set<String> Gamma) {
		return partial(P, A, A.getAlphabet(), Gamma);
	}
	
	

	private static State prod(State p, State q) {
		return new StateImpl("" + p.getLabel()+q.getLabel() + "");
	}
	
	private static void addGammaLoops(Automaton A, Set<String> G) {
		
		for(State q : A.getStates()) {
			for(String g : G) {
				if(A.trans(q, g).isEmpty()) 
					A.addTransition(new TransitionImpl(q, g, q));
			}
		}
		
	}

	private static void makeSelfLoops(Automaton P, State q, Set<String> Act) {
		for(String a : Act) {
			P.addTransition(new TransitionImpl(q, a, q));
		}
	}
	
	private static void removeGammaLoops(Automaton A, Set<String> g) {
		
		HashSet<Transition> toRmv = new HashSet<Transition>();
		
		for(Transition t : A.getTransitions()) {
			if(t.getSource().equals(t.getDestination()) && g.contains(t.getLabel()))
				toRmv.add(t);
		}
		
		for(Transition t : toRmv) {
			A.removeTransition(t);
		}
		
	}
	
}
