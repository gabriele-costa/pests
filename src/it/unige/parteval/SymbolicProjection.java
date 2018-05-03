package it.unige.parteval;

import java.util.Set;

import automata.sfa.SFA;

public class SymbolicProjection<Pred, Alph> {
	
public static SFA partial(SFA P, SFA A, Set<String> SigmaB, Set<String> Gamma) {
		
		
//		addGammaLoops(P, Gamma);
//		
//		State i = prod(P.getInitial(), A.getInitial());
//		NFAutomatonImpl B = new NFAutomatonImpl(i);
//		
//		State ffstate = new StateImpl("ff");
//
//		for(State fa : A.getFinals())
//			for(State fp : P.getFinals())
//				//if(!P.getFails().contains(fp))
//					B.setFinal(prod(fp, fa), true);
//		
//		Set<String> Sigma = new HashSet<>();
//		Sigma.addAll(A.getAlphabet());
//		Sigma.addAll(SigmaB);
//		
//		for(State qp : P.getStates()) {
//			for(State qa : A.getStates()) {
//				for(String a : Sigma ) {
//					Set<State> Qpp = P.trans(qp, a);
//					
//					// if Qpp is empty == false
//					if(Qpp.isEmpty())
//						B.addTransition(new TransitionImpl(prod(qp,qa), a, ffstate));
//										
//					for(State qpp : Qpp) {
//						
//						if(!Gamma.contains(a)) {
//							
//							if(A.getAlphabet().contains(a)) { 	// a in SigmaA \ Gamma
//								for(State qap : A.trans(qa, a))
//									B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
//							}
//							
//							if(SigmaB.contains(a)) { 		// a in SigmaB \ Gamma
//								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
//							}								
//						}
//						else { 									// a in Gamma
//							for(State qap : A.trans(qa, a)) {
//								B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp,qap)));
//							}
//						}
//					}
//				}
////				if(P.getFails().contains(qp) || A.getFails().contains(qa))
////					B.setFail(prod(qp, qa), true);
//			}
//		}
//		
//		// removeGammaLoops(P, Gamma);
//		// removeGammaLoops(B, Gamma);
//		
//		makeSelfLoops(B, ffstate, B.getAlphabet());
//		
//		return B;
		return null;
	}

}
