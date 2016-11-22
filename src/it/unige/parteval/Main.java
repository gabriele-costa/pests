package it.unige.parteval;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.parteval.Test.Test;

public class Main {
	
	public static final String TAU = "[t]";
	public static final String BAR = "-";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test.main(null);
	}
	
	private static State prod(State p, State q) {
		return new StateImpl("" + p.getLabel()+q.getLabel() + "");
	}
	
	private static String neg(String a) {
		if(a.startsWith(BAR)) {
			return a.substring(1);
		}
		else
			return BAR + a;
	}
	
	public static NFAutomatonImpl partial(Automaton P, Automaton A, Set<String> Gamma) {
		
		State i = prod(P.getInitial(), A.getInitial());
		NFAutomatonImpl B = new NFAutomatonImpl(i);
		
		for(State fa : A.getFinals())
			for(State fp : P.getStates())
				if(!P.getFails().contains(fp))
					B.setFinal(prod(fp, fa), true);
		
		for(State qp : P.getStates()) {
			for(State qa : A.getStates()) {
				for(String a : P.getAlphabet()) {
					Set<State> Qpp = P.trans(qp, a);
					for(State qpp : Qpp) {
						B.addTransition(new TransitionImpl(prod(qp,qa), a, prod(qpp, qa)));
						Set<State> Qap = A.trans(qa, a);
							for(State qap : Qap)
								B.addTransition(new TransitionImpl(prod(qp,qa), Automaton.EPSILON, prod(qpp, qap)));
					
						if(a.compareTo(TAU) == 0) {
							for(String g : Gamma) {
								for(State qap : Qap) {
									B.addTransition(new TransitionImpl(prod(qp,qa), neg(g), prod(qpp,qap)));
								}
								Qap = A.trans(qa, neg(g));
								for(State qap : Qap) {
									B.addTransition(new TransitionImpl(prod(qp,qa), g, prod(qpp,qap)));
								}
							}
						}
					}
				}
				if(P.getFails().contains(qp) || A.getFails().contains(qa))
					B.setFail(prod(qp, qa), true);
			}
		}
		
		return B;
	}

}
