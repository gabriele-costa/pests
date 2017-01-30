package it.unige.parteval.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.Automaton;
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

public class FunctionalTest2 {
	
	int N = 20; 

	@Test
	public void iterative() {

		DFAutomatonImpl P = getPolicy();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
			
		DFAutomatonImpl Ai = getA();
		Printer.createDotGraph(Printer.printDotAutomaton(Ai, "A"), "A");
		
		NFAutomatonImpl Pp = Projection.partial(P, Ai, getSigma(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"), "nPA");
		P = Pp.specialDFA(G);
		P.minimize();
		P.collapse();
		P.complete(G);
		//P.renameStates("p");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"), "PA");	
		
	}
	
	private DFAutomatonImpl getPolicy() {
	   	
		
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	
	   	
	   	NFAutomatonImpl nP = new NFAutomatonImpl(p0);
	   	
	   	nP.addTransition(new TransitionImpl(p0, "a", p0));
	   	nP.addTransition(new TransitionImpl(p0, "d", p1));
	   	nP.addTransition(new TransitionImpl(p1, "a", p1));
	   	nP.addTransition(new TransitionImpl(p1, "b", p1));
	   	nP.addTransition(new TransitionImpl(p1, Automaton.EPSILON, p0));
	   	
	   	nP.setFinal(p0, true);
	   		   	
	   	return nP.toDFA();
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("c");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		//Sigma.add("a");
		Sigma.add("b");
		//Sigma.add("d");
		return Sigma;
	}
	
	private DFAutomatonImpl getA() {
		
	   	StateImpl c0 = new StateImpl("c0");
	   	StateImpl c1 = new StateImpl("c1");
	   	StateImpl c2 = new StateImpl("c2");
	   	
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(c0);
	   	
	   	P.addTransition(new TransitionImpl(c0, "a", c0));
	   	P.addTransition(new TransitionImpl(c0, "c", c1));
	   	P.addTransition(new TransitionImpl(c1, "a", c1));
	   	P.addTransition(new TransitionImpl(c1, "d", c2));
	   	P.addTransition(new TransitionImpl(c2, "a", c2));
	   	P.addTransition(new TransitionImpl(c2, "c", c0));
	   		
	   	P.setFinal(c0, true);
	   	
	   	return P;
	}
	
	private DFAutomatonImpl getB(int i) {

		assertTrue(i > 0);
		
	   	StateImpl[] c = new StateImpl[i+2];
	   	
	   	for(int j = 0; j < c.length; j++) {
	   		c[j] = new StateImpl("q"+j);
	   	}
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(c[0]);
	   	
	   	for(int j = 0; j < c.length-3; j++) {
	   		P.addTransition(new TransitionImpl(c[j], "c", c[j+1]));
	   	}
	   	
	   	P.addTransition(new TransitionImpl(c[c.length-3], "b", c[c.length-2]));
	   	P.addTransition(new TransitionImpl(c[c.length-2], "c", c[c.length-1]));
   		
	   		
	   	P.setFinal(c[c.length-1], true);
	   	
	   	return P;
	}
}
