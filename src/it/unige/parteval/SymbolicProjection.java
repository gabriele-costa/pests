package it.unige.parteval;

import java.util.HashSet;
import java.util.Set;

import automata.sfa.SFA;
import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.TransitionImpl;

public class SymbolicProjection<Pred, Alph> {
	
	public static SFA partial(SFA P, SFA A, Set<String> SigmaB, Set<String> Gamma) {
			
		
			
	//	State i = prod(P.getInitial(), A.getInitial());
	//	NFAutomatonImpl B = new NFAutomatonImpl(i);
	//	
	////	State ffstate = new StateImpl(Automaton.FAIL);
	//
	//	for(State fa : A.getFinals())
	//		for(State fp : P.getFinals())
	//			B.setFinal(prod(fp, fa), true);
	//	
	//	Set<String> Sigma = new HashSet<>();
	//	Sigma.addAll(A.getAlphabet());
	//	Sigma.addAll(SigmaB);
	//	
	//	Set<State> violating = new HashSet<State>();
	//	
	//	for(State qp : P.getStates()) {
	//		for(State qa : A.getStates()) {
	//			for(String a : Sigma ) {
	//				Set<State> Qpp = P.trans(qp, a);
	//				Set<State> Qap = A.trans(qa, a);
	//				
	//				// if Qpp is empty == false
	//				if(Qpp.isEmpty()) {
	//					if(!Qap.isEmpty() && !Gamma.contains(a))
	//						violating.add(prod(qp,qa));
	//					continue;
	//				}
	////					B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
	//									
	//				for(State qpp : Qpp) {
	//					
	//					if(!Gamma.contains(a)) {
	//						if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
	//							for(State qap : Qap)
	//								B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
	//						}
	//						// else?
	//						if(SigmaB.contains(a)) { 			// a in SigmaB \ Gamma
	//							B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
	//						}								
	//					}
	//					else { 									// a in Gamma
	//						for(State qap : Qap) {
	//							B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp,qap)));
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}
	//	
	//	for(State u : B.getStates()) {
	//		Set<State> C = B.Closure(u);
	//		C.retainAll(violating);
	//		if(!C.isEmpty()) {
	//			violating.add(u);	
	//		}
	//	}
	//	
	//	for(State v : violating) {
	//		B.removeState(v);
	//	}
	//	
	//	return B;
		return null;
	}

}
