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
import it.unige.parteval.test.flexfact.FlexFactComponents;
import it.unige.parteval.test.flexfact.FlexFactPlant;

public class OpenCLExperiment {

	
	@Test
	public void testSCP() {

		DFAutomatonImpl P = getPolicy(8);
		makeTotal(P);
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl A = getA();
		// DFAutomatonImpl A = getWA();
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
			
		NFAutomatonImpl Pp = Projection.partialA(P, A, getSigma(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"), "nPA");
		P = Projection.unify(Pp, G);
		

		Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"), "PA");
		
	}
	
	// @Test
	public void testCSP() {

		DFAutomatonImpl P = getPolicy(8);
		makeTotal(P);
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl AB = getAB();
		Printer.createDotGraph(Printer.printDotAutomaton(AB, "AB"), "AB");
			
		NFAutomatonImpl Pp = Projection.partialA(P, AB, new HashSet<String>(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPCAB"), "nPCAB");
		P = Projection.unify(Pp, G);
		
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PCAB"), "PCAB");
		
		
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
	   		P.addTransition(p[i], "b", p[i]);
	   		P.addTransition(p[i], "b", p[i]);		
	   		P.setFinal(p[i], true);
	   	}
	   	
	   	for(int i = 0; i < size-1; i++) {
	   		P.addTransition(p[i], "e", p[i+1]);
	   		P.addTransition(p[i+1], "d", p[i]);
	   	}
	   	
	   	return P;
	}
	
	private Set<String> getGamma() {
		HashSet<String> G = new HashSet<>();
		G.add("b");
		
		return G;
	}
	
	private Set<String> getSigma() {
		Set<String> Sigma = new HashSet<>();
		Sigma.add("d");
		return Sigma;
	}
	
	private DFAutomatonImpl getA() {
		
	   	StateImpl q0 = new StateImpl("q0");
	   	StateImpl q1 = new StateImpl("q1");
	   		   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(q0);
	   	
   		P.addTransition(new TransitionImpl(q0, "b", q1));
   		P.addTransition(new TransitionImpl(q1, "e", q0));
   		P.setFinal(q0, true);
   		P.setFinal(q1, true);
	   		   	
	   	return P;
	}
	
	private DFAutomatonImpl getB() {
		
	   	StateImpl r0 = new StateImpl("r0");
	   	StateImpl r1 = new StateImpl("r1");
	   		   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(r0);
	   	
   		P.addTransition(new TransitionImpl(r0, "b", r1));
   		P.addTransition(new TransitionImpl(r1, "d", r0));
   		P.setFinal(r0, true);
   		P.setFinal(r1, true);
	   		   	
	   	return P;
	}
	
	private DFAutomatonImpl getAB() {
	   	
	   	return DFAutomatonImpl.parallel(getA(), getB(), getGamma());
	   	
	}
}
