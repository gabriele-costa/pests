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

public class PaperExample {

	final int BUFFER_MIN  = 5;
	final int UAV_CAP_MIN = 4;
	
	final int BUFFER_MAX  = 21;
	final int UAV_CAP_MAX = 20;
	
	final int STEP = 5;
	
	@Test
	public void testSCP() {

		DFAutomatonImpl P = getPolicy(4);
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl A = getA(3);
		//DFAutomatonImpl A = getWA();
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
			
		NFAutomatonImpl Pp = Projection.partialA(P, A, getSigma(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPA"), "nPA");
		P = Projection.unify(Pp, G);
		//P.collapse();
		//P.minimize();
		//P.renameStates("p");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PA"), "PA");
	}
	
	@Test
	public void testCSP() {

		DFAutomatonImpl P = getPolicy(3);
		makeTotal(P);
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PC"), "PC");
		
		Set<String> G = getGamma();
		
		DFAutomatonImpl AB = getAB();
		Printer.createDotGraph(Printer.printDotAutomaton(AB, "AB"), "AB");
			
		NFAutomatonImpl Pp = Projection.partialA(P, AB, new HashSet<String>(), G);
		Printer.createDotGraph(Printer.printDotAutomaton(Pp, "nPCAB"), "nPCAB");
		P = Projection.unify(Pp, G);
		P.collapse();
		P.minimize();
		//P.renameStates("p");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "PCAB"), "PCAB");
		
		DFAutomatonImpl CAB = DFAutomatonImpl.control(AB, P, G);
		Printer.createDotGraph(Printer.printDotAutomaton(CAB, "CAB"), "CAB");
		
	}
	
	private void makeTotal(DFAutomatonImpl P) {
		StateImpl fail = new StateImpl("ff");
		Set<Transition> toAdd = new HashSet<Transition>();
		
		for(String a : P.getAlphabet()) {
			for(State s : P.getStates()) {
				if(P.trans(s, a).isEmpty()) {
					toAdd.add(new TransitionImpl(s, a, fail));
				}
			}
			
			toAdd.add(new TransitionImpl(fail, a, fail));
		}
		
		for(Transition t : toAdd) {
			P.addTransition(t);
		}
	}

	//@Test
	public void testLoop() {

		for(int b = BUFFER_MIN; b < BUFFER_MAX; b+=STEP) {
			DFAutomatonImpl P = getPolicy(b);
			Set<String> G = getGamma();
			Set<String> S = getSigma();
			
			System.out.println();
			System.out.print("|\tP("+b+"): states = " + P.getStates().size()  + "\t\t|\n");
			System.out.print("=========================================\n");
			System.out.print("| #\t| A\t| Q\t| U\t| T\t|\n");
			System.out.print("+---------------------------------------+\n");
			//Printer.createDotGraph(Printer.printDotAutomaton(P, "P"+b), "P"+b);
			
			for(int c = UAV_CAP_MIN; c < UAV_CAP_MAX; c+=STEP) {
				
				if(c > b)
					break;
				
				System.out.print("|"+c+"\t|");
				
				DFAutomatonImpl A = getA(c);
				//Printer.createDotGraph(Printer.printDotAutomaton(A, "A"+c), "A"+c);
				
				System.out.print(A.getStates().size() + "\t|");
				
				long s = System.currentTimeMillis();
				NFAutomatonImpl Pp = Projection.partialA(P, A, S, G);
				DFAutomatonImpl PA = Projection.unify(Pp, G);
				s = System.currentTimeMillis() - s;
				
				//Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"+b+""+c), "PA"+b+"-"+c);
				System.out.print(Pp.getStates().size() + "\t|");
				System.out.print(PA.getStates().size() + "\t|");
//				
//				PA.collapse();
//				PA.minimize();
//				
//				System.out.print("Minim: states = " + PA.getStates().size() + "\t| ");
				
				System.out.print(s + "\t|\n");
			}
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
	
	private DFAutomatonImpl getAB() {
		
	   	StateImpl p0 = new StateImpl("q0");
	   	
	   	DFAutomatonImpl A = new DFAutomatonImpl(p0);
	   	
	   	StateImpl p1 = new StateImpl("q1");
	   	StateImpl p2 = new StateImpl("q2");
	   	StateImpl p3 = new StateImpl("q3");
	   	StateImpl p4 = new StateImpl("q4");
	   	
	   	A.addTransition(new TransitionImpl(p0, "a", p1));
	   	A.addTransition(new TransitionImpl(p1, "a", p2));
	   	A.addTransition(new TransitionImpl(p2, "s", p3));
	   	A.addTransition(new TransitionImpl(p3, "s", p0));
	   	A.addTransition(new TransitionImpl(p0, "t", p4));
	   	
	   	A.setFinal(p0, true);
	   	A.setFinal(p1, true);
	   	A.setFinal(p2, true);
	   	A.setFinal(p3, true);
	   	A.setFinal(p4, true);
	   	
	   	StateImpl w0 = new StateImpl("w0");
	   	
	   	
	   	DFAutomatonImpl B = new DFAutomatonImpl(w0);
	   	
	   	StateImpl w1 = new StateImpl("w1");
	   	StateImpl w2 = new StateImpl("w2");
	   	StateImpl w3 = new StateImpl("w3");
	   	StateImpl w4 = new StateImpl("w4");
	   	StateImpl w5 = new StateImpl("w5");
	   	StateImpl w6 = new StateImpl("w6");
	   	StateImpl w7 = new StateImpl("w7");
	   	
	   	B.addTransition(new TransitionImpl(w0, "t", w7));
	   	B.addTransition(new TransitionImpl(w0, "s", w1));
	   	B.addTransition(new TransitionImpl(w1, "b", w2));
	   	B.addTransition(new TransitionImpl(w2, "b", w4));
	   	B.addTransition(new TransitionImpl(w2, "s", w3));
	   	B.addTransition(new TransitionImpl(w3, "t", w5));
	   	B.addTransition(new TransitionImpl(w3, "s", w6));
	   	B.addTransition(new TransitionImpl(w4, "s", w0));
	   	B.addTransition(new TransitionImpl(w5, "b", w7));
	   	B.addTransition(new TransitionImpl(w6, "b", w1));
	   	
	   	B.setFinal(w0, true);
	   	B.setFinal(w1, true);
	   	B.setFinal(w2, true);
	   	B.setFinal(w3, true);
	   	B.setFinal(w4, true);
	   	B.setFinal(w5, true);
	   	B.setFinal(w6, true);
	   	B.setFinal(w7, true);
	   	
	   	return DFAutomatonImpl.parallel(A, B, getGamma());
	   	
	}
}
