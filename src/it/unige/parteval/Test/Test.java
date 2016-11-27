package it.unige.parteval.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.GraphViz;
import it.unige.automata.util.Printer;
import it.unige.parteval.Main;

public abstract class Test {
	
	static int TESTNUM = 5;

	public static void main(String[] args) {
		
		
		DFAutomatonImpl A = TestAuto(TESTNUM);
		
		// assert(new StateImpl("a4").equals(new StateImpl("a4")));
		
		System.out.println(Printer.printDotAutomaton(A, "A"));
		System.out.println("=============================");
		createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
		
		DFAutomatonImpl P = TestPol(TESTNUM);
		
		System.out.println(Printer.printDotAutomaton(P, "P"));
		System.out.println("=============================");
		createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = TestGamma(TESTNUM);
		
		NFAutomatonImpl PpA = Main.partial(P, A, G);
		
		System.out.println(Printer.printDotAutomaton(PpA, "P_A"));
		System.out.println("=============================");
		createDotGraph(Printer.printDotAutomaton(PpA, "P_A"), "P_A");
		
		DFAutomatonImpl PpADet = PpA.specialDFA(G);
		
		System.out.println(Printer.printDotAutomaton(PpADet, "P_A_det"));
		System.out.println("=============================");
		createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"), "P_A_det");
		
		DFAutomatonImpl PpADetMin = PpADet.minimize();
		
		System.out.println(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"));
		System.out.println("=============================");
		createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"), "P_A_det_min");
		
		System.out.println("\nFINISHED\n");
	}
	
	static Set<String> TestGamma(int num) {
		switch(num) {
		case 0: return TestGamma0();
		case 1: return TestGamma1();
		case 2: return TestGamma2();
		case 3: return TestGamma3();
		case 4: return TestGamma4();
		case 5: return TestGamma5();		
		default: return null;
		}
	}
	
	static DFAutomatonImpl TestAuto(int num) {
		switch(num) {
		case 0: return TestAuto0();
		case 1: return TestAuto1();
		case 2: return TestAuto2();
		case 3: return TestAuto3();
		case 4: return TestAuto4();
		case 5: return TestAuto5();
		default: return null;
		}
	}
	
	static DFAutomatonImpl TestPol(int num) {
		switch(num) {
		case 0: return TestPol0();
		case 1: return TestPol1();
		case 2: return TestPol2();
		case 3: return TestPol3();
		case 4: return TestPol4();
		case 5: return TestPol5();
		default: return null;
		}
	}

	public static void createDotGraph(String dotFormat, String fileName) {
	    GraphViz gv = new GraphViz();
	    gv.add(dotFormat);
	    String type = "pdf";
	    // gv.increaseDpi();
	    // gv.decreaseDpi();
	    File out = new File(fileName+"."+ type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	
	static Set<String> TestGamma0() {
		Set<String> G = new HashSet<String>();
		
		G.add("s");
		G.add("-s");
		
		return G;
	}
	
	static Set<String> TestGamma1() {
		
		return TestGamma0();
	}
	
	static Set<String> TestGamma2() {
		Set<String> G = new HashSet<String>();
		
		G.add("s");
		G.add("r");
		G.add("-s");
		G.add("-r");
		
		return G;
	}
	
	static Set<String> TestGamma3() {
		
		return TestGamma2();
	}
	
	static Set<String> TestGamma4() {
		
		return TestGamma2();
	}
	
	static Set<String> TestGamma5() {
		Set<String> G = new HashSet<String>();
		
		G.add("login");
		G.add("logout");
		G.add("-login");
		G.add("-logout");
		
		return G;
	}
	
	static DFAutomatonImpl TestAuto0() {
		
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "a", a1));
		// A.addTransition(new TransitionImpl(a1, "s", a2));
		// A.addTransition(new TransitionImpl(new StateImpl("a1"), "s", new StateImpl("a2")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-s", new StateImpl("a3")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-r", new StateImpl("a4")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "r", a0));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "r", new StateImpl("a1")));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "b", new StateImpl("a4")));
		
		A.setFinal(a1, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestAuto1() {
		
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		State a2 = new StateImpl("a2");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "a", a1));
		A.addTransition(new TransitionImpl(a1, "s", a2));
		// A.addTransition(new TransitionImpl(new StateImpl("a1"), "s", new StateImpl("a2")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-s", new StateImpl("a3")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-r", new StateImpl("a4")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "r", a0));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "r", new StateImpl("a1")));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "b", new StateImpl("a4")));
		
		A.setFinal(a2, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestAuto2() {
		
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		State a2 = new StateImpl("a2");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "a", a1));
		A.addTransition(new TransitionImpl(a1, "c", a1));
		A.addTransition(new TransitionImpl(a1, "s", a2));
		A.addTransition(new TransitionImpl(a1, "r", a0));
		A.setFinal(a2, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestAuto3() {
		
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		State a2 = new StateImpl("a2");
		State a3 = new StateImpl("a3");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "a", a1));
		A.addTransition(new TransitionImpl(a1, "s", a2));
		A.addTransition(new TransitionImpl(a2, "b", a3));
		A.addTransition(new TransitionImpl(a1, "r", a3));
		A.addTransition(new TransitionImpl(a2, "-r", a0));
		A.addTransition(new TransitionImpl(a3, "a", a1));
		A.addTransition(new TransitionImpl(a3, "c", a0));
		
		// A.addTransition(new TransitionImpl(new StateImpl("a1"), "s", new StateImpl("a2")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-s", new StateImpl("a3")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "-r", new StateImpl("a4")));
		// A.addTransition(new TransitionImpl(new StateImpl("a2"), "r", a0));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "r", new StateImpl("a1")));
		// A.addTransition(new TransitionImpl(new StateImpl("a3"), "b", new StateImpl("a4")));
		
		A.setFinal(a3, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestAuto4() {
		
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		State a2 = new StateImpl("a2");
		State a3 = new StateImpl("a3");
		State a4 = new StateImpl("a4");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "a", a1));
		A.addTransition(new TransitionImpl(a1, "s", a2));
		A.addTransition(new TransitionImpl(a2, "b", a3));
		A.addTransition(new TransitionImpl(a1, "r", a3));
		A.addTransition(new TransitionImpl(a2, "-r", a0));
		A.addTransition(new TransitionImpl(a3, "a", a1));
		A.addTransition(new TransitionImpl(a3, "c", a0));
		A.addTransition(new TransitionImpl(a2, "-s", a4));
		
		A.setFinal(a4, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestAuto5() {
		State a0 = new StateImpl("a0");
		State a1 = new StateImpl("a1");
		State a2 = new StateImpl("a2");
		State a3 = new StateImpl("a3");
		State a4 = new StateImpl("a4");
		State a5 = new StateImpl("a5");
		State a6 = new StateImpl("a6");
		
		DFAutomatonImpl A = new DFAutomatonImpl(a0);
		
		A.addTransition(new TransitionImpl(a0, "login", a1));
		A.addTransition(new TransitionImpl(a0, "micro_req", a5));
		A.addTransition(new TransitionImpl(a5, "alert", a6));
		A.addTransition(new TransitionImpl(a6, "cancel", a0));
		A.addTransition(new TransitionImpl(a6, "pay", a0));
		A.addTransition(new TransitionImpl(a1, "logout", a0));
		A.addTransition(new TransitionImpl(a1, "pay_req", a2));
		A.addTransition(new TransitionImpl(a2, "alert", a3));
		A.addTransition(new TransitionImpl(a3, "cancel", a1));
		A.addTransition(new TransitionImpl(a3, "pay", a1));
		A.addTransition(new TransitionImpl(a1, "micro_req", a4));
		A.addTransition(new TransitionImpl(a4, "pay", a1));
		A.setFinal(a0, true);
		
		return A;
	}
	
	static DFAutomatonImpl TestPol0() {
		
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		StateImpl p2 = new StateImpl("p2");
		//StateImpl p3 = new StateImpl("F");
		
		DFAutomatonImpl P = new DFAutomatonImpl(p0);
		
		P.setFinal(p0, true);
		P.setFinal(p1, true);
		P.setFinal(p2, true);
		//P.setFail(p3, true);
		
		P.addTransition(new TransitionImpl(p0, Main.TAU, p0));
		P.addTransition(new TransitionImpl(p0, "a", p1));
		P.addTransition(new TransitionImpl(p1, Main.TAU, p1));
		P.addTransition(new TransitionImpl(p1, "a", p2));
		P.addTransition(new TransitionImpl(p2, Main.TAU, p2));
		//P.addTransition(new TransitionImpl(p2, "a", p3));
		//P.addTransition(new TransitionImpl(p3, Main.TAU, p3));
		//P.addTransition(new TransitionImpl(p3, "a", p3));
		
		return P;
	}
	
	static DFAutomatonImpl TestPol1() {
		
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		
		DFAutomatonImpl P = new DFAutomatonImpl(p0);
		
		P.setFinal(p0, true);
		//P.setFail(p2, true);
		
		P.addTransition(new TransitionImpl(p0, Main.TAU, p0));
		P.addTransition(new TransitionImpl(p0, "a", p1));
		P.addTransition(new TransitionImpl(p1, Main.TAU, p1));
		P.addTransition(new TransitionImpl(p1, "c", p1));
		P.addTransition(new TransitionImpl(p1, "b", p0));
		
		return P;
	}
	
	static DFAutomatonImpl TestPol2() {
		return TestPol1();
	}
	
	static DFAutomatonImpl TestPol3() {
		return TestPol1();
	}
	
	static DFAutomatonImpl TestPol4() {
		return TestPol1();
	}
	
	static DFAutomatonImpl TestPol5() {
		
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		
		DFAutomatonImpl P = new DFAutomatonImpl(p0);
		
		P.addTransition(new TransitionImpl(p0, Main.TAU, p0));
		P.addTransition(new TransitionImpl(p0, "alert", p0));
		P.addTransition(new TransitionImpl(p0, "cancel", p0));
		P.addTransition(new TransitionImpl(p0, "pay_req", p0));
		P.addTransition(new TransitionImpl(p0, "micro_req", p0));
		P.addTransition(new TransitionImpl(p0, "pay", p1));
		P.addTransition(new TransitionImpl(p1, Main.TAU, p1));
		P.addTransition(new TransitionImpl(p1, "alert", p0));
		P.addTransition(new TransitionImpl(p1, "cancel", p1));
		P.addTransition(new TransitionImpl(p1, "pay_req", p1));
		P.addTransition(new TransitionImpl(p1, "micro_req", p1));
		
		P.setFinal(p0, true);
		
		return P;
	}

}
