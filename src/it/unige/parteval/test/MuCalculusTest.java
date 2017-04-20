package it.unige.parteval.test;

import static org.junit.Assert.assertEquals;

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

public class MuCalculusTest {

	
	private static int TESTNUM = 0;
    
    private static Assertion makeOrDia(ArrayList<String> Sigma, Assertion f) {
		if(Sigma.isEmpty()) {
			return new MuFF();
		}
		else if (Sigma.size() == 1) {
			return new MuDia(Sigma.get(0), f);
		}
		else {
			String a = Sigma.remove(0);
			Assertion g = makeOrDia(Sigma, f);
			return new MuOr(new MuDia(a, f), g);
		}
	}
	
    @Test
	public void pmcDiningPhilosophers() {
		// x =n <_>.x \/ <eat0>.y;
		// y =n <_>.y \/ <eat1>.x;
		
		HashSet<String> Sigma = new HashSet<String>();
		Sigma.add("get0");
		Sigma.add("get1");
		Sigma.add("put0");
		Sigma.add("put1");
		Sigma.add("eat0");
		Sigma.add("eat1");
		Sigma.add("think0");
		Sigma.add("think1");
		
		ArrayList<String> al0 = new ArrayList<String>();
		ArrayList<String> al1 = new ArrayList<String>();
		al0.addAll(Sigma);
		al1.addAll(Sigma);
		al0.remove("eat0");
		al1.remove("eat1");
		al0.remove("eat1");
		al1.remove("eat0");
		
		
		MuSystem Phi = new MuSystem();
		Assertion f0 = new MuOr(makeOrDia(al0, new MuVar("x")), new MuDia("eat0", new MuVar("y")));
		Assertion f1 = new MuOr(makeOrDia(al1, new MuVar("y")), new MuDia("eat1", new MuVar("x")));
		
		Phi.eq.add(new MuEquation("x", false, f0));
		Phi.eq.add(new MuEquation("y", false, f1));
		
		System.out.println(Phi.toString());
		System.out.println("\nSIZE: "+Phi.size() +"\n");
		System.out.println("=============================\n");
		
		Sigma.remove("think0");
		Sigma.remove("eat0");
		
		LTS A = makePhil();
		
		MuSystem Phip = Main.quotienting(Phi, A, Sigma);
		System.out.println(Phip.toString());
		System.out.println("\nSIZE: "+Phip.size() +"\n");
		
	}
	
	private static LTS makePhil() {
		LTS P = new LTS();
		
		State s0 = new StateImpl("s0");
		State s1 = new StateImpl("s1");
		State s2 = new StateImpl("s2");
		State s3 = new StateImpl("s3");
		State s4 = new StateImpl("s4");
		State s5 = new StateImpl("s5");
		
		P.states.add(s0);
		P.states.add(s1);
		P.states.add(s2);
		P.states.add(s3);
		P.states.add(s4);
		P.states.add(s5);
		
		P.inits = s0;
		
		P.delta.add(new TransitionImpl(s0, "think0", s0));
		P.delta.add(new TransitionImpl(s0, "get0", s1));
		P.delta.add(new TransitionImpl(s0, "get1", s2));
		P.delta.add(new TransitionImpl(s1, "get1", s3));
		P.delta.add(new TransitionImpl(s2, "get0", s3));
		P.delta.add(new TransitionImpl(s3, "eat0", s4));
		P.delta.add(new TransitionImpl(s4, "put0", s5));
		P.delta.add(new TransitionImpl(s5, "put1", s0));
		
		return P;
	}
	
	@Test
	public void testMyProj() {
		DFAutomatonImpl A = makePhilAuto();
		
		System.out.println(Printer.printDotAutomaton(A, "A"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
		
		DFAutomatonImpl P = makeSpecAuto();
		
		System.out.println(Printer.printDotAutomaton(P, "P"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = makeGamma();
		
		NFAutomatonImpl PpA = Projection.partial(P, A, G);
		
		System.out.println(Printer.printDotAutomaton(PpA, "P_A"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpA, "P_A"), "P_A");
		
		DFAutomatonImpl PpADet = PpA.specialDFA(G);
		
		System.out.println(Printer.printDotAutomaton(PpADet, "P_A_det"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"), "P_A_det");
		
		System.out.println("SIZE: " + PpADet.getStates().size() + " states, " + PpADet.getTransitions().size() + " transitions");
		
		
		DFAutomatonImpl PpADetMin = PpADet.minimize();
		
		System.out.println(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"), "P_A_det_min");

		System.out.println("\nFINISHED\n");
	}

	private static Set<String> makeGamma() {
		Set<String> G = new HashSet<String>();
		
		G.add("get0");
		G.add("get1");
		G.add("put0");
		G.add("put1");
		//G.add("-get0");
		//G.add("-get1");
		
		return G;
	}

	private static DFAutomatonImpl makeSpecAuto() {
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		StateImpl p2 = new StateImpl("p2");
		
		DFAutomatonImpl P = new DFAutomatonImpl(p0);
		
		P.setFinal(p0, true);
		P.setFinal(p1, true);
		
		
		makeSelfLoops(P, p0, new String[] {"get0","get1","put0","put1","think0","think1"});
		makeSelfLoops(P, p1, new String[] {"get0","get1","put0","put1","think0","think1"});
		makeSelfLoops(P, p2, new String[] {"get0","get1","put0","put1","think0","think1","eat0","eat1"});
		
		P.addTransition(new TransitionImpl(p0, "eat0", p1));
		P.addTransition(new TransitionImpl(p1, "eat1", p0));
		P.addTransition(new TransitionImpl(p0, "eat1", p2));
		P.addTransition(new TransitionImpl(p1, "eat0", p2));
		
		return P;
	}

	private static void makeSelfLoops(DFAutomatonImpl P, State q, String[] Act) {
		for(String a : Act) {
			P.addTransition(new TransitionImpl(q, a, q));
		}
	}

	private static DFAutomatonImpl makePhilAuto() {
		
		State s0 = new StateImpl("s0");
		State s1 = new StateImpl("s1");
		State s2 = new StateImpl("s2");
		State s3 = new StateImpl("s3");
		State s4 = new StateImpl("s4");
		State s5 = new StateImpl("s5");
		

		DFAutomatonImpl P = new DFAutomatonImpl(s0);
		
		P.addState(s0);
		P.addState(s1);
		P.addState(s2);
		P.addState(s3);
		P.addState(s4);
		P.addState(s5);
		
		P.setFinal(s0, true);
		
		P.addTransition(new TransitionImpl(s0, "think0", s0));
		P.addTransition(new TransitionImpl(s0, "get0", s1));
		P.addTransition(new TransitionImpl(s0, "get1", s2));
		P.addTransition(new TransitionImpl(s1, "get1", s3));
		P.addTransition(new TransitionImpl(s2, "get0", s3));
		P.addTransition(new TransitionImpl(s3, "eat0", s4));
		P.addTransition(new TransitionImpl(s4, "put0", s5));
		P.addTransition(new TransitionImpl(s5, "put1", s0));
		
		return P;
	}

	private static DFAutomatonImpl LTS2FSA(LTS T) {
		DFAutomatonImpl P = new DFAutomatonImpl(T.inits);
		for(State s : T.states) {
			P.addState(s);
			P.setFinal(s, true);
		}
		for(Transition t : T.delta) {
			P.addTransition(t);
		}
		
		return P;
	}

	public static void testOld() {
		DFAutomatonImpl A = TestAuto(TESTNUM);
		
		// assert(new StateImpl("a4").equals(new StateImpl("a4")));
		
		System.out.println(Printer.printDotAutomaton(A, "A"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(A, "A"), "A");
		
		DFAutomatonImpl P = TestPol(TESTNUM);
		
		System.out.println(Printer.printDotAutomaton(P, "P"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "P"), "P");
		
		Set<String> G = TestGamma(TESTNUM);
		
		addGammaLoops(P, G); // necessario perch� le policy devono sempre avere self loop sulle azioni di sincronizzazione
		
		NFAutomatonImpl PpA = Projection.partial(P, A, G);
		
		System.out.println(Printer.printDotAutomaton(PpA, "P_A"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpA, "P_A"), "P_A");
		
		DFAutomatonImpl PpADet = PpA.specialDFA(G);
		
		System.out.println(Printer.printDotAutomaton(PpADet, "P_A_det"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"), "P_A_det");
		
		DFAutomatonImpl PpADetMin = PpADet.minimize();
		
		System.out.println(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"), "P_A_det_min");
		
		System.out.println("\nFINISHED\n");
	}
	
	private static void addGammaLoops(DFAutomatonImpl A, Set<String> g) {
		String[] ga = new String[g.size()];
		int i = 0;
		for(String s : g) {
			ga[i++] = s;
		}
		
		for(State q : A.getStates()) {
			makeSelfLoops(A, q, ga);
		}
		
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
	
	static Set<String> TestGamma0() {
		Set<String> G = new HashSet<String>();
		
		G.add("s");
		//G.add("-s");
		
		return G;
	}
	
	static Set<String> TestGamma1() {
		
		return TestGamma0();
	}
	
	static Set<String> TestGamma2() {
		Set<String> G = new HashSet<String>();
		
		G.add("s");
		G.add("r");
		//G.add("-s");
		//G.add("-r");
		
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
		//G.add("-login");
		//G.add("-logout");
		
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
		//A.addTransition(new TransitionImpl(a2, "-r", a0));
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
		//A.addTransition(new TransitionImpl(a2, "-r", a0));
		A.addTransition(new TransitionImpl(a3, "a", a1));
		A.addTransition(new TransitionImpl(a3, "c", a0));
		//A.addTransition(new TransitionImpl(a2, "-s", a4));
		
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
		
		P.addTransition(new TransitionImpl(p0, "a", p1));
		P.addTransition(new TransitionImpl(p1, "a", p2));
		
		return P;
	}
	
	static DFAutomatonImpl TestPol1() {
		
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		
		DFAutomatonImpl P = new DFAutomatonImpl(p0);
		
		P.setFinal(p0, true);
		//P.setFail(p2, true);
		
		P.addTransition(new TransitionImpl(p0, "a", p1));
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
		
		//P.addTransition(new TransitionImpl(p0, Main.TAU, p0));
		P.addTransition(new TransitionImpl(p0, "alert", p0));
		P.addTransition(new TransitionImpl(p0, "cancel", p0));
		P.addTransition(new TransitionImpl(p0, "pay_req", p0));
		P.addTransition(new TransitionImpl(p0, "micro_req", p0));
		P.addTransition(new TransitionImpl(p0, "pay", p1));
		//P.addTransition(new TransitionImpl(p1, Main.TAU, p1));
		P.addTransition(new TransitionImpl(p1, "alert", p0));
		P.addTransition(new TransitionImpl(p1, "cancel", p1));
		P.addTransition(new TransitionImpl(p1, "pay_req", p1));
		P.addTransition(new TransitionImpl(p1, "micro_req", p1));
		
		P.setFinal(p0, true);
		
		return P;
	}

}
