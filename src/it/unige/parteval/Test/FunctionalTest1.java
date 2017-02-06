package it.unige.parteval.Test;

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

public class FunctionalTest1 {
	
	int N = 4; 

	@Test
	public void edit() {

		DFAutomatonImpl P = getPolicy();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		for(int i = 1; i <= N; i++) {
			
			System.out.println("Loop "+i);
			
			DFAutomatonImpl Ai = getA(i);
			Printer.createDotGraph(Printer.printDotAutomaton(Ai, "A"+i), "A"+i);
			
			NFAutomatonImpl Pp = Projection.partial(P, Ai, getSigma(), G);
			Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"+i), "nPA"+i);
			P = Pp.specialDFA(G);
			P.minimize();
			P.collapse();
			P.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"+i), "PA"+i);
			
			DFAutomatonImpl Bi = getB(++i);
			Printer.createDotGraph(Printer.printDotAutomaton(Bi, "B"+i), "B"+i);
			
			Pp = Projection.partial(P, Bi, getSigma(), G);
			Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPB"+i), "nPB"+i);
			P = Pp.specialDFA(G);
			P.minimize();
			P.collapse();
			P.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(P, "PB"+i), "PB"+i);
			
		}
	}
	
	@Test
	public void control() {

		DFAutomatonImpl P = getPolicy();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "Pc"), "Pc");
		
		Set<String> G = getGamma();
		
		for(int i = 1; i <= N; i++) {
			
			System.out.println("Loop "+i);
			
			DFAutomatonImpl Ai = getA(i);
			Printer.createDotGraph(Printer.printDotAutomaton(Ai, "Ac"+i), "Ac"+i);
			
			NFAutomatonImpl Pp = Projection.partial(P, Ai, G, G);
			Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPAc"+i), "nPAc"+i);
			P = Pp.specialDFA(G);
			P.minimize();
			P.collapse();
			P.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(P, "PAc"+i), "PAc"+i);
			
			i++;
			
			DFAutomatonImpl Bi = getB(i);
			Printer.createDotGraph(Printer.printDotAutomaton(Bi, "Bc"+i), "Bc"+i);
			
			Pp = Projection.partial(P, Bi, getSigma(), G);
			Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPBc"+i), "nPBc"+i);
			P = Pp.specialDFA(G);
			P.minimize();
			P.collapse();
			P.complete(G);
			//P.renameStates("p");
			Printer.createDotGraph(Printer.printDotAutomaton(P, "PBc"+i), "PBc"+i);
			
		}
		
		
	}
	
	private DFAutomatonImpl getPolicy() {
	   	
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	StateImpl ff = new StateImpl("ff");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p0);
	   	
	   	P.addTransition(new TransitionImpl(p0, "a", p1));
	   	P.addTransition(new TransitionImpl(p1, "b", p0));
	   	
	   	P.setFinal(p0, true);
	   	
	   	P.addTransition(new TransitionImpl(p0, "b", ff));
	   	P.addTransition(new TransitionImpl(p1, "a", ff));
	   	P.addTransition(new TransitionImpl(ff, "a", ff));
	   	P.addTransition(new TransitionImpl(ff, "b", ff));
	   		   	
	   	return P;
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("c");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		Sigma.add("a");
		Sigma.add("b");
		return Sigma;
	}
	
	private DFAutomatonImpl getA(int i) {

		assertTrue(i > 0);
		
	   	StateImpl[] c = new StateImpl[i+2];
	   	
	   	for(int j = 0; j < c.length; j++) {
	   		c[j] = new StateImpl("q"+j);
	   	}
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(c[0]);
	   	
	   	for(int j = 0; j < c.length-3; j++) {
	   		P.addTransition(new TransitionImpl(c[j], "c", c[j+1]));
	   	}
	   	
	   	P.addTransition(new TransitionImpl(c[c.length-3], "a", c[c.length-2]));
	   	P.addTransition(new TransitionImpl(c[c.length-2], "c", c[c.length-1]));
   		
	   		
	   	P.setFinal(c[c.length-1], true);
	   	
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
