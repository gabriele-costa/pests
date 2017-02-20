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

public class ICALPExample {

	final int BUFFER_MIN  = 5;
	final int UAV_CAP_MIN = 4;
	
	final int BUFFER_MAX  = 21;
	final int UAV_CAP_MAX = 20;
	
	final int STEP = 5;
	
	@Test
	public void test() {

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
	
	private DFAutomatonImpl getWA() {
		
	   	StateImpl p0 = new StateImpl("q0");
	   	StateImpl p1 = new StateImpl("q1");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p0);
	   	
   		
	   	
	   	StateImpl out = new StateImpl("q3");
	   	StateImpl p2 = new StateImpl("q2");
	   	
	   	P.addTransition(new TransitionImpl(p0, "a", p1));
	   	P.addTransition(new TransitionImpl(p1, "a", p2));
	   	P.addTransition(new TransitionImpl(p2, "s", p0));
	   	P.addTransition(new TransitionImpl(p0, "t", out));
	   	
	   	P.setFinal(p0, true);
	   	P.setFinal(p1, true);
	   	P.setFinal(p2, true);
	   	P.setFinal(out, true);
	   	
	   	return P;
	}
}
