package it.unige.parteval.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.GraphViz;
import it.unige.automata.util.Printer;
import it.unige.lts.LTS;
import it.unige.mu.Assertion;
import it.unige.mu.MuDia;
import it.unige.mu.MuEquation;
import it.unige.mu.MuFF;
import it.unige.mu.MuOr;
import it.unige.mu.MuSystem;
import it.unige.mu.MuVar;
import it.unige.parteval.Main;
import it.unige.parteval.Projection;

public class ETHTest {

	@Test
	public void test() {

		DFAutomatonImpl P = getPolicy();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl A = getA();
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
			
		NFAutomatonImpl Pp = Projection.partialA(P, A, getSigma(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"), "nPA");
		P = Projection.unify(Pp, G);
		//P.collapse();
		//P.minimize();
		//P.renameStates("p");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"), "PA");
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
