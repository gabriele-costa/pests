package it.unige.parteval.test.parse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.Automaton;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.AutomataTextualInterface;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class ParseTest {

	@Test
	public void test() {
		
		DFAutomatonImpl P = getPolicy();
		
		String s = Printer.printDotAutomaton(P, "P");
		
		// TODO
		// Automaton<TransitionImpl> Q = AutomataTextualInterface.parseDotAutomaton(s);
	}
	
	
	private DFAutomatonImpl getPolicy() {
	   	
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	//StateImpl ff = new StateImpl("ff");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p0);
	   	
	   	P.addTransition(new TransitionImpl(p0, "a", p1));
	   	P.addTransition(new TransitionImpl(p1, "b", p0));
	   	P.addTransition(new TransitionImpl(p0, "s", p0));
	   	P.addTransition(new TransitionImpl(p1, "s", p1));
	   	
	   	P.setFinal(p0, true);
//	   	
//	   	P.addTransition(new TransitionImpl(p0, "l", ff));
//	   	P.addTransition(new TransitionImpl(p1, "u", ff));
//	   	P.addTransition(new TransitionImpl(ff, "u", ff));
//	   	P.addTransition(new TransitionImpl(ff, "l", ff));
//	   		   	
	   	return P;
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("s");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		Sigma.add("b");
		return Sigma;
	}
	
	private DFAutomatonImpl getA() {
		
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	StateImpl p2 = new StateImpl("p2"); // ADD
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p0);
	   	
	   	P.addTransition(new TransitionImpl(p0, "a", p1));
	   	// P.addTransition(new TransitionImpl(p1, "s", p0)); // REMOVE
	   	P.addTransition(new TransitionImpl(p1, "s", p2)); // ADD
	   	P.addTransition(new TransitionImpl(p2, "s", p0)); // ADD
	   		
	   	P.setFinal(p2, true);
	   	
	   	return P;
	}
}
	
