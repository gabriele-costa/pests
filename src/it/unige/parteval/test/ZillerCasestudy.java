package it.unige.parteval.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.impl.*;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class ZillerCasestudy {
	
	final String a = "a"; 				
	final String l = "l";
	
	// non cont
	final String b = "b"; 				
	final String c = "c";
	final String e = "e";
	
	
	
	DFAutomatonImpl spec() { 

		State p0 = new StateImpl("s0");
		State p1 = new StateImpl("s1");
		State p2 = new StateImpl("s2");
//		State p3 = new StateImpl("s3");
//		State p4 = new StateImpl("s4");
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl agv1 = new DFAutomatonImpl(p0);
		
		agv1.addTransition(new TransitionImpl(p0, a, p1));
		agv1.addTransition(new TransitionImpl(p1, a, p2));
		agv1.addTransition(new TransitionImpl(p0, l, p0));
		agv1.addTransition(new TransitionImpl(p1, l, p1));
		agv1.addTransition(new TransitionImpl(p2, l, p2));
		agv1.addTransition(new TransitionImpl(p2, a, ff));
		
		agv1.addTransition(new TransitionImpl(p0, b, p0));
		agv1.addTransition(new TransitionImpl(p1, b, p1));
		agv1.addTransition(new TransitionImpl(p0, c, p0));
		agv1.addTransition(new TransitionImpl(p1, c, p1));
		agv1.addTransition(new TransitionImpl(p2, b, p2));
		agv1.addTransition(new TransitionImpl(p2, c, p2));

		agv1.addTransition(new TransitionImpl(ff, a, ff));
		agv1.addTransition(new TransitionImpl(ff, l, ff));
		agv1.addTransition(new TransitionImpl(ff, b, ff));
		agv1.addTransition(new TransitionImpl(ff, c, ff));
		
		agv1.setFinal(p0, true);
		agv1.setFinal(p1, true);
		agv1.setFinal(p2, true);
		
		
		return agv1;
		
	}
	
	DFAutomatonImpl specs() { 

		State p0 = new StateImpl("s0");
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl agv1 = new DFAutomatonImpl(p0);
		
		agv1.addTransition(new TransitionImpl(p0, a, p0));
		agv1.addTransition(new TransitionImpl(p0, l, p0));
		agv1.addTransition(new TransitionImpl(p0, b, p0));
		agv1.addTransition(new TransitionImpl(p0, c, ff));
		
		agv1.addTransition(new TransitionImpl(ff, a, ff));
		agv1.addTransition(new TransitionImpl(ff, l, ff));
		agv1.addTransition(new TransitionImpl(ff, b, ff));
		agv1.addTransition(new TransitionImpl(ff, c, ff));
		
		agv1.setFinal(p0, true);
		
		return agv1;
		
	}
	
	DFAutomatonImpl autos() { 

		State p0 = new StateImpl("p0");
		State p1 = new StateImpl("p1");
		State p2 = new StateImpl("p2");
		State p3 = new StateImpl("p3");
		State p4 = new StateImpl("p4");
		
		DFAutomatonImpl agv1 = new DFAutomatonImpl(p0);
		
		agv1.addTransition(new TransitionImpl(p0, a, p1));
		agv1.addTransition(new TransitionImpl(p0, l, p2));
		agv1.addTransition(new TransitionImpl(p1, b, p3));
		agv1.addTransition(new TransitionImpl(p2, c, p4));
		
		agv1.setFinal(p4, true);
		agv1.setFinal(p3, true);
		
		return agv1;
		
	}
	
	DFAutomatonImpl auto() { 

		State p0 = new StateImpl("p0");
		State p1 = new StateImpl("p1");
		State p2 = new StateImpl("p2");
		State p3 = new StateImpl("p3");
		State p4 = new StateImpl("p4");
		State p5 = new StateImpl("p5");
		
		DFAutomatonImpl agv1 = new DFAutomatonImpl(p0);
		
		agv1.addTransition(new TransitionImpl(p0, a, p1));
		agv1.addTransition(new TransitionImpl(p0, l, p2));
		agv1.addTransition(new TransitionImpl(p1, b, p0));
		agv1.addTransition(new TransitionImpl(p1, c, p3));
		agv1.addTransition(new TransitionImpl(p1, a, p4));
		agv1.addTransition(new TransitionImpl(p2, c, p4));
		agv1.addTransition(new TransitionImpl(p3, a, p1));
		agv1.addTransition(new TransitionImpl(p4, l, p2));
		agv1.addTransition(new TransitionImpl(p4, a, p5));
		agv1.addTransition(new TransitionImpl(p5, c, p4));
		
		agv1.setFinal(p4, true);
		
		return agv1;
		
	}
	
	
	public Set<String> sigmau() {
		HashSet<String> g = new HashSet<>();
		
		g.add(b);
		g.add(c);
		
		return g;
	}
	
	public Set<String> sigmac() {
		HashSet<String> g = new HashSet<>();
		
		g.add(a);
		g.add(l);
		
		return g;
	}
	
	public Set<String> sigma() {
		Set<String> g = sigmac();
		g.addAll(sigmau());
		
		return g;
	}
	
	
	@Test
	public void sopconrefinement() {
		
		DFAutomatonImpl P = spec();
		DFAutomatonImpl A = auto();
		
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
		
		// DFAutomatonImpl PA = DFAutomatonImpl.parallel(P, A, sigma());
		
		// Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		
		NFAutomatonImpl nY = Projection.partial(P, A, sigmac(), sigmac());
		
		Printer.createDotGraph(Printer.printDotAutomaton(nY, "nY"), "nY");
		
		DFAutomatonImpl Y = nY.specialDFA(sigmac());
		Y.minimize();
		Y.collapse();
		
		/*
		 * 
		
		State p0 = new StateImpl("p0");
		State p1 = new StateImpl("p1");
		State p2 = new StateImpl("p2");
		State p3 = new StateImpl("p3");
		State p4 = new StateImpl("p4");
		State p5 = new StateImpl("p5");
		
		Y = new DFAutomatonImpl(p0);
		
		Y.addTransition(new TransitionImpl(p0, l, p1));
		Y.addTransition(new TransitionImpl(p0, a, p2));
		Y.addTransition(new TransitionImpl(p1, l, p1));
		Y.addTransition(new TransitionImpl(p1, a, p3));
		Y.addTransition(new TransitionImpl(p1, a, p4));
		Y.addTransition(new TransitionImpl(p2, a, p5));
		Y.addTransition(new TransitionImpl(p2, l, p4));
		Y.addTransition(new TransitionImpl(p3, l, p4));
		Y.addTransition(new TransitionImpl(p3, a, p5));
		Y.addTransition(new TransitionImpl(p4, l, p4));
		Y.addTransition(new TransitionImpl(p4, a, p5));
		Y.addTransition(new TransitionImpl(p5, l, p5));
		
		Y.setFinal(p1, true);
		Y.setFinal(p3, true);
		Y.setFinal(p4, true);
		Y.setFinal(p5, true);
		*/
		
		Printer.createDotGraph(Printer.printDotAutomaton(Y, "Y"), "Y");
		
		DFAutomatonImpl AY = DFAutomatonImpl.parallel(A, Y, sigmac());
		
		//assertTrue(AY.getStates().contains(new StateImpl(Automaton.FAIL)));
		
		Printer.createDotGraph(Printer.printDotAutomaton(AY, "AY"), "AY");
		
		Projection.makeController(AY, sigmac());
		
		AY.minimize();
		
		Printer.createDotGraph(Printer.printDotAutomaton(AY, "CY"), "CY");
		
    }
    
	
}
