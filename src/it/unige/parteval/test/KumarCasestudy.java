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

public class KumarCasestudy {
	
	final String m02 = "m02"; 				
	final String m21 = "m21";
	final String m10 = "m10";
	final String m04 = "m04"; 				
	final String m43 = "m43";
	final String m30 = "m30";

	final String c20 = "c20"; 				
	final String c01 = "c01";
	final String c03 = "c03";
	final String c12 = "c12"; 				
	final String c40 = "c40";
	final String c34 = "c34";
	
	// non controllable
	final String c13 = "c13";
	final String c31 = "c31";
	
	DFAutomatonImpl spec0() { 

		State p0 = new StateImpl("p0");
		State pc = new StateImpl("pc");
		State pm = new StateImpl("pm");
		
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl spec = new DFAutomatonImpl(p0);
		
		// Cat in
		spec.addTransition(new TransitionImpl(p0, c20, pc));
		spec.addTransition(new TransitionImpl(p0, c40, pc));
		// Mouse in
		spec.addTransition(new TransitionImpl(p0, m10, pm));
		spec.addTransition(new TransitionImpl(p0, m30, pm));
		// Cat out
		spec.addTransition(new TransitionImpl(pc, c01, p0));
		spec.addTransition(new TransitionImpl(pc, c03, p0));
		// Mouse out
		spec.addTransition(new TransitionImpl(pm, m02, p0));
		spec.addTransition(new TransitionImpl(pm, m04, p0));
		// Failure
		spec.addTransition(new TransitionImpl(pc, m10, ff));
		spec.addTransition(new TransitionImpl(pc, m30, ff));
		spec.addTransition(new TransitionImpl(pm, c20, ff));
		spec.addTransition(new TransitionImpl(pm, c40, ff));
		
		// Projection.addGammaLoops(spec, sigmac());
		
		spec.setFinal(p0, true);
		spec.setFinal(pc, true);
		spec.setFinal(pm, true);
		
		
		return spec;
		
	}
	
	DFAutomatonImpl spec1() { 

		State p0 = new StateImpl("p0");
		State pc = new StateImpl("pc");
		State pm = new StateImpl("pm");
		
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl spec = new DFAutomatonImpl(p0);
		
		// Cat in
		spec.addTransition(new TransitionImpl(p0, c01, pc));
		spec.addTransition(new TransitionImpl(pc, c31, p0));
		// Mouse in
		spec.addTransition(new TransitionImpl(p0, m21, pm));
		// Cat out
		spec.addTransition(new TransitionImpl(pc, c12, p0));
		spec.addTransition(new TransitionImpl(pc, c13, p0));
		// Mouse out
		spec.addTransition(new TransitionImpl(pm, m10, p0));
		// Failure
		spec.addTransition(new TransitionImpl(pc, m21, ff));
		spec.addTransition(new TransitionImpl(pm, c01, ff));
		spec.addTransition(new TransitionImpl(pm, c13, ff));
		
		// Projection.addGammaLoops(spec, sigmac());
		
		spec.setFinal(p0, true);
		spec.setFinal(pc, true);
		spec.setFinal(pm, true);
		
		return spec;
		
	}
		
	DFAutomatonImpl spec2() { 

		State p0 = new StateImpl("p0");
		State pc = new StateImpl("pc");
		State pm = new StateImpl("pm");
		
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl spec = new DFAutomatonImpl(p0);
		
		// Cat in
		spec.addTransition(new TransitionImpl(p0, c12, pc));
		// Mouse in
		spec.addTransition(new TransitionImpl(p0, m02, pm));
		// Cat out
		spec.addTransition(new TransitionImpl(pc, c20, p0));
		// Mouse out
		spec.addTransition(new TransitionImpl(pm, m21, ff));
		// Failure
		spec.addTransition(new TransitionImpl(pc, m02, ff));
		spec.addTransition(new TransitionImpl(pm, c12, ff));
		
		// Projection.addGammaLoops(spec, sigmac());
		
		spec.setFinal(p0, true);
		spec.setFinal(pc, true);
		spec.setFinal(pm, true);
		
		
		return spec;
		
	}
	
	DFAutomatonImpl spec3() { 

		State p0 = new StateImpl("p0");
		State pc = new StateImpl("pc");
		State pm = new StateImpl("pm");
		
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl spec = new DFAutomatonImpl(p0);
		
		// Cat in
		spec.addTransition(new TransitionImpl(p0, c03, pc));
		spec.addTransition(new TransitionImpl(p0, c13, pc));
		// Mouse in
		spec.addTransition(new TransitionImpl(p0, m43, pm));
		// Cat out
		spec.addTransition(new TransitionImpl(pc, c31, p0));
		spec.addTransition(new TransitionImpl(pc, c34, p0));
		// Mouse out
		spec.addTransition(new TransitionImpl(pm, m30, p0));
		// Failure
		spec.addTransition(new TransitionImpl(pc, m43, ff));
		spec.addTransition(new TransitionImpl(pm, c03, ff));
		spec.addTransition(new TransitionImpl(pm, c13, ff));
		
		// Projection.addGammaLoops(spec, sigmac());
		
		spec.setFinal(p0, true);
		spec.setFinal(pc, true);
		spec.setFinal(pm, true);
		
		
		return spec;
		
	}
	
	DFAutomatonImpl spec4() { 

		State p0 = new StateImpl("p0");
		State pc = new StateImpl("pc");
		State pm = new StateImpl("pm");
		
		State ff = new StateImpl("ff");
		
		DFAutomatonImpl spec = new DFAutomatonImpl(p0);
		
		// Cat in
		spec.addTransition(new TransitionImpl(p0, c34, pc));
		// Mouse in
		spec.addTransition(new TransitionImpl(p0, m04, pm));
		// Cat out
		spec.addTransition(new TransitionImpl(pc, c40, p0));
		// Mouse out
		spec.addTransition(new TransitionImpl(pm, m43, p0));
		// Failure
		spec.addTransition(new TransitionImpl(pc, m04, ff));
		spec.addTransition(new TransitionImpl(pm, c34, ff));
		
		// Projection.addGammaLoops(spec, sigmac());
		
		spec.setFinal(p0, true);
		spec.setFinal(pc, true);
		spec.setFinal(pm, true);
		
		
		return spec;
		
	}
	
	
	
	DFAutomatonImpl cat() { 

		State p0 = new StateImpl("c0");
		State p1 = new StateImpl("c1");
		State p2 = new StateImpl("c2");
		State p3 = new StateImpl("c3");
		State p4 = new StateImpl("c4");
		
		DFAutomatonImpl cat = new DFAutomatonImpl(p2);
		
		cat.addTransition(new TransitionImpl(p0, c01, p1));
		cat.addTransition(new TransitionImpl(p0, c03, p3));
		cat.addTransition(new TransitionImpl(p1, c12, p2));
		cat.addTransition(new TransitionImpl(p2, c20, p0));
		cat.addTransition(new TransitionImpl(p1, c13, p3));
		cat.addTransition(new TransitionImpl(p3, c13, p1));
		cat.addTransition(new TransitionImpl(p3, c34, p4));
		cat.addTransition(new TransitionImpl(p4, c40, p0));
		
		cat.setFinal(p2, true);
		
		return cat;
		
	}
	
	DFAutomatonImpl mouse() { 

		State p0 = new StateImpl("m0");
		State p1 = new StateImpl("m1");
		State p2 = new StateImpl("m2");
		State p3 = new StateImpl("m3");
		State p4 = new StateImpl("m4");
		
		DFAutomatonImpl mouse = new DFAutomatonImpl(p4);
		
		mouse.addTransition(new TransitionImpl(p0, m02, p2));
		mouse.addTransition(new TransitionImpl(p0, m04, p4));
		mouse.addTransition(new TransitionImpl(p1, m10, p0));
		mouse.addTransition(new TransitionImpl(p3, m30, p0));
		mouse.addTransition(new TransitionImpl(p2, m21, p1));
		mouse.addTransition(new TransitionImpl(p4, m43, p3));
		
		mouse.setFinal(p4, true);
		
		return mouse;
		
	}
	
	
	public Set<String> sigmau() {
		HashSet<String> g = new HashSet<>();
		
		g.add(c13);
		
		return g;
	}
	
	public Set<String> sigmac() {
		HashSet<String> g = new HashSet<>();

		g.add(m02);
		g.add(m21);
		g.add(m10);
		g.add(m04);
		g.add(m43);
		g.add(m30);
		g.add(c20);
		g.add(c01);
		g.add(c03);
		g.add(c12);
		g.add(c40);
		g.add(c34);
		
		return g;
	}
	
	public Set<String> sigma() {
		Set<String> g = sigmac();
		g.addAll(sigmau());
		
		return g;
	}
	

	DFAutomatonImpl spec(int i) { 
		switch (i) {
		case 0 : return spec0();
		case 1 : return spec1();
		case 2 : return spec2();
		case 3 : return spec3();
		case 4 : return spec4();
		default : return null;
		}
	}
	
	@Test
	public void mazecontroller() {
		
		
		DFAutomatonImpl	S = DFAutomatonImpl.parallel(cat(), mouse(), new HashSet<String>()).minimize();
		
		Printer.createDotGraph(Printer.printDotAutomaton(S, "S"), "S");
		
		int i = 0;
		
		do {
		
			DFAutomatonImpl P = spec(i);
			
			Printer.createDotGraph(Printer.printDotAutomaton(P, "P"+i), "P"+i);
			
			NFAutomatonImpl nY = Projection.partial(S, P, sigmac(), sigmac());
			
			Printer.createDotGraph(Printer.printDotAutomaton(nY, "nY"+i), "nY"+i);
			
			DFAutomatonImpl Y = nY.specialDFA(sigmac());
			Y.minimize();
			Y.collapse();
			
			Printer.createDotGraph(Printer.printDotAutomaton(Y, "Y"+i), "Y"+i);
		
			S = Y;
			
//			DFAutomatonImpl SY = DFAutomatonImpl.parallel(S, Y, sigmac());
//			
//			Printer.createDotGraph(Printer.printDotAutomaton(SY, "SY"), "SY");
//			
//			Projection.makeController(SY, sigmac());
//			
//			SY.minimize();
//			
//			Printer.createDotGraph(Printer.printDotAutomaton(SY, "CY"), "CY");
			
			i++;
		} while(i < 5);
		
    }
    
	
}
