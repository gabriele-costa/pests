package it.unige.parteval.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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

public class TACASExaperiments {

	final int BUFFER_MIN  = 5;
	
	final int BOUND = 100;
	final int STEP = 5;
	
	@Test
	public void UAV_SCP() {
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			long time = System.currentTimeMillis();
			int[] v = testCSP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
	}
	
	@Test
	public void UAV_CSP() {
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			long time = System.currentTimeMillis();
			int[] v = testSCP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
		
	}
	
	@Test
	public void FLEXFACT_CSP() {
		
	}
	
	
	
	public int[] testSCP(int size) {

		DFAutomatonImpl P = getPolicy(size);
		
		//Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl A = getA(size - 1);
		// DFAutomatonImpl A = getWA();
		// Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
			
		NFAutomatonImpl Pp = Projection.partialA(P, A, getSigma(), G);
		//Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"), "nPA");
		P = Projection.unify(Pp, G);
		
		return new int[] {P.getStates().size(), P.getTransitions().size()};
		//P.collapse();
		//P.minimize();
		//P.renameStates("p");
		//Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"), "PA");
	}
	
	public int[] testCSP(int size) {

		DFAutomatonImpl P = getPolicy(size);
		makeTotal(P);
		
		// Printer.createDotGraph(Printer.printDotAutomaton(P, "PC"), "PC");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl AB = getAB(size - 1);
		// Printer.createDotGraph(Printer.printDotAutomaton(AB, "AB"), "AB");
			
		NFAutomatonImpl Pp = Projection.partialA(P, AB, new HashSet<String>(), G);
		// Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPCAB"), "nPCAB");
		P = Projection.unify(Pp, G);
		
		return new int[] {P.getStates().size(), P.getTransitions().size()};
		// P.collapse();
		// P.minimize();
		// P.renameStates("p");
		// Printer.createDotGraph(Printer.printDotAutomaton(P, "PCAB"), "PCAB");
		
		// DFAutomatonImpl CAB = DFAutomatonImpl.control(AB, P, G);
		// Printer.createDotGraph(Printer.printDotAutomaton(CAB, "CAB"), "CAB");
		
	}
	
	private void makeTotal(DFAutomatonImpl P) {
		StateImpl fail = new StateImpl("ff");
		Set<TransitionImpl> toAdd = new HashSet<TransitionImpl>();
		
		for(String a : P.getAlphabet()) {
			for(State s : P.getStates()) {
				if(P.trans(s, a).isEmpty()) {
					toAdd.add(new TransitionImpl(s, a, fail));
				}
			}
			
			toAdd.add(new TransitionImpl(fail, a, fail));
		}
		
		for(TransitionImpl t : toAdd) {
			P.addTransition(t);
		}
	}

	private DFAutomatonImpl getPolicy(int size) {
	   	
		StateImpl[] p = new StateImpl[size];
		
		for(int i = 0; i < size; i++) {
			p[i] = new StateImpl("p"+i); 
		}
		
	   	DFAutomatonImpl P = new DFAutomatonImpl(p[0]);
	   	
	   	for(int i = 0; i < size; i++) {
	   		P.addTransition(p[i], "s", p[i]);
	   		P.addTransition(p[i], "t", p[i]);		
	   		P.setFinal(p[i], true);
	   	}
	   	
	   	for(int i = 0; i < size-1; i++) {
	   		P.addTransition(p[i], "a", p[i+1]);
	   		P.addTransition(p[i+1], "b", p[i]);
	   	}
	   	
	   	return P;
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("s");
		G.add("t");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		Sigma.add("b");
		return Sigma;
	}
	
	private DFAutomatonImpl getA(int cap) {
		
	   	StateImpl[] p = new StateImpl[cap];
	   	
	   	for(int i = 0; i < cap; i++) {
	   		p[i] = new StateImpl("q"+i);
	   	}
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p[0]);
	   	
	   	for(int i = 0; i < cap-1; i++) {
	   		P.addTransition(new TransitionImpl(p[i], "a", p[i+1]));
	   		P.setFinal(p[i], true);
	   	}
	   	
	   	StateImpl out = new StateImpl("out");
	   	StateImpl r0 = new StateImpl("r0");
	   	
	   	P.addTransition(new TransitionImpl(p[0], "t", out));
	   	P.addTransition(new TransitionImpl(p[cap-1], "s", r0));
	   	P.addTransition(new TransitionImpl(r0, "s", p[0]));
	   	
	   	P.setFinal(p[cap-1], true);
	   	P.setFinal(out, true);
	   	P.setFinal(r0, true);
	   	
	   	return P;
	}
	
	private DFAutomatonImpl getB(int cap) {
		
	   	StateImpl[] p = new StateImpl[cap];
	   	
	   	for(int i = 0; i < cap; i++) {
	   		p[i] = new StateImpl("p"+i);
	   	}
	   	
	   	StateImpl r0 = new StateImpl("w0");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(r0);
	   	
	   	for(int i = 0; i < cap-1; i++) {
	   		P.addTransition(new TransitionImpl(p[i], "b", p[i+1]));
	   		P.setFinal(p[i], true);
	   	}
	   	
	   	StateImpl out = new StateImpl("out");
	   	
	   	P.addTransition(new TransitionImpl(r0, "t", out));
	   	P.addTransition(new TransitionImpl(p[cap-1], "s", r0));
	   	P.addTransition(new TransitionImpl(r0, "s", p[0]));
	   	
	   	P.setFinal(p[cap-1], true);
	   	P.setFinal(out, true);
	   	P.setFinal(r0, true);
	   	
	   	return P;
	}
	
	private DFAutomatonImpl getAB(int cap) {
	   	
	   	return DFAutomatonImpl.parallel(getA(cap), getB(cap), getGamma());
	   	
	}
}
