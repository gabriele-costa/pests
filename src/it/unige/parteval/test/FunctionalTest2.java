package it.unige.parteval.test;

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
	
	int N = 4; 

	@Test
	public void iterative() {
		
		DFAutomatonImpl Sa = getA();
		Printer.createDotGraph(Printer.printDotAutomaton(Sa, "A"), "A");
		DFAutomatonImpl Sb = getB();

		Printer.createDotGraph(Printer.printDotAutomaton(Sb, "B"), "B");
	
		DFAutomatonImpl P = getPolicy();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		
		for(int i = 0; i < N; i++) {
			
			
			NFAutomatonImpl nSb = Projection.partial(P, Sa, Sb.getAlphabet(), G);
			
			Printer.createDotGraph(Printer.printDotAutomaton(nSb, "nSb"+i), "nSb"+i);
			
			NFAutomatonImpl nSa = Projection.partial(P, Sb, Sa.getAlphabet(), G);
			
			Printer.createDotGraph(Printer.printDotAutomaton(nSa, "nSa"+i), "nSa"+i);
			
			Sb = nSb.specialDFA(G);
			Sb.minimize();
			Sb.collapse();
			Sb.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(Sb, "Sa"+i), "Sa"+i);	
			
			Sa = nSa.specialDFA(G);
			Sa.minimize();
			Sa.collapse();
			Sa.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(Sa, "Sa"+i), "Sa"+i);	
		}
	}
	
	private DFAutomatonImpl getPolicy() {
	   	
		
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	
	   	
	   	DFAutomatonImpl nP = new DFAutomatonImpl(p0);
	   	
	   	nP.addTransition(new TransitionImpl(p0, "c", p0));
	   	nP.addTransition(new TransitionImpl(p0, "a", p1));
	   	nP.addTransition(new TransitionImpl(p1, "b", p0));
	   	
	   	nP.setFinal(p0, true);
	   	nP.setFinal(p1, true);
	   		   	
	   	return nP;
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("c");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		//Sigma.add("a");
		//Sigma.add("b");
		//Sigma.add("d");
		return Sigma;
	}
	
	private DFAutomatonImpl getA() {
		
		StateImpl c0 = new StateImpl("c0");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(c0);
	   	
	   	P.addTransition(new TransitionImpl(c0, "a", c0));
	   	P.addTransition(new TransitionImpl(c0, "c", c0));
	   	
	   		
	   	P.setFinal(c0, true);
	   	
	   	return P;
	}
	
	private DFAutomatonImpl getB() {

		StateImpl c0 = new StateImpl("c0");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(c0);
	   	
	   	P.addTransition(new TransitionImpl(c0, "b", c0));
	   	P.addTransition(new TransitionImpl(c0, "c", c0));
	   		
	   	P.setFinal(c0, true);
	   	
	   	return P;
	}
}
