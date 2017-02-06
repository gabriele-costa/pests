package it.unige.parteval;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.SetUtils;

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

	public static NFAutomatonImpl partial(Automaton P, Automaton A, Set<String> Gamma) {
		return partial(P, A, A.getAlphabet(), Gamma);
	}

	public static State prod(State p, State q) {
		return new StateImpl("" + p.getLabel()+q.getLabel() + "");
	}
	
	public static void addGammaLoops(Automaton A, Set<String> G) {
		
		for(State q : A.getStates()) {
			for(String g : G) {
				if(A.trans(q, g).isEmpty()) 
					A.addTransition(new TransitionImpl(q, g, q));
			}
		}
		
	}

	private static void makeSelfLoops(Automaton P, State q, Set<String> Act) {
		for(String a : Act) {
			if(P.trans(q, a).isEmpty())
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
	
	public static void makeController(DFAutomatonImpl A, Set<String> SigmaC) {
		Set<State> toRmv = new HashSet<State>();
		

		do {
			toRmv.clear();
			toRmv.addAll(getPits(A));
			
			boolean found = true;
			while(found) {
				found = false;
				Set<Transition> toCut = new HashSet<>();
				for(Transition t : A.getTransitions()) {
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
	
}
