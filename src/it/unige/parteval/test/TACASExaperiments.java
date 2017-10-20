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

public class TACASExaperiments {

	private static final int FACTMAX = 4;

	final int BUFFER_MIN  = 100;
	
	final int BOUND = 101;
	final int STEP = 5;
	
	@Test
	public void UAV_SCP() {
		System.out.println("SCP");
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			System.gc();
			long time = System.currentTimeMillis();
			int[] v = testSCP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
	}
	
	//@Test 
	public void UAV_CSP() {
		System.out.println("CSP");
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			System.gc();
			long time = System.currentTimeMillis();
			int[] v = testCSP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
		
	}
	
	//@Test
	public void FLEXFACT_CSP() {
		for(int i = 1; i < FACTMAX; i++) {
			System.gc();
			long time = System.currentTimeMillis();
			int[] v = testFlexFact(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
	}
	
	public int[] testFlexFact(int size) {
		FlexFactPlant plant = new FlexFactPlant(2 + size,3);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 1, true, true, 3);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(size+2, 1, true, true);
		plant.install(SF, 0, 1);
		plant.install(ES, size+1, 1);
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 1, true);
		controls.addAll(FlexFactComponents.getExitSlideControls(size+2, 1));
		
		for(int i = 0; i < size; i++) {
			DFAutomatonImpl RT = FlexFactComponents.RotaryTable(i+1, 1);
			DFAutomatonImpl PM1 = FlexFactComponents.ProcessingMachine(i+1, 0, false);
			DFAutomatonImpl PM2 = FlexFactComponents.ProcessingMachine(i+1, 2, false);
			plant.install(RT, i+1, 1);
			plant.install(PM1, i+1, 0);
			plant.install(PM2, i+1, 2);
			controls.addAll(FlexFactComponents.getRotaryTableControls(i+1, 1));
			controls.addAll(FlexFactComponents.getProcessingMachineControls(i+1, 0));
			controls.addAll(FlexFactComponents.getProcessingMachineControls(i+1, 2));
		}
		
		DFAutomatonImpl PA = plant.getPlantAutomaton();

		DFAutomatonImpl spec = processN(size, controls);
				
		NFAutomatonImpl nPspec = Projection.partialA(spec, PA, new HashSet<String>(), controls);
		
		DFAutomatonImpl Pspec = Projection.unify(nPspec, controls);
		
		return new int[] {Pspec.getStates().size(), Pspec.getTransitions().size()};
	}
	
	DFAutomatonImpl processN(int size, Set<String> controls) {
		
		StateImpl ok = new StateImpl("ok");
		
		DFAutomatonImpl S = new DFAutomatonImpl(ok);
		
		State[] in = new StateImpl[2*size];
		State[] proc = new StateImpl[2*size];
		State[] out = new StateImpl[2*size-1];
		
		
		for(int i = 0; i < 2*size; i++) {
			in[i] = new StateImpl("i"+i);
		}
		
		for(int i = 0; i < 2*size-1; i++) {
			S.addTransition(in[i], FlexFactComponents.move(0, 1, 1, 1), in[i+1]);
		}
		
		if(size > 0) {
			S.addTransition(ok, FlexFactComponents.move(0, 1, 1, 1), in[0]);
		}
		
		for(int i = 0; i < 2*size; i++) {
			proc[i] = new StateImpl("p"+i);
		}
		
		for(int i = 1; i < 2*size; i++) {
			S.addTransition(proc[i-1], FlexFactComponents.process((i/2)+1, (i%2 == 0) ? 0 : 2), proc[i]);
		}
		
		if(size > 0) {
			S.addTransition(in[in.length - 1], FlexFactComponents.process(1, 1), proc[0]);
		}
		
		for(int i = 0; i < 2*size-1; i++) {
			out[i] = new StateImpl("o"+i);
		}
		
		for(int i = 0; i < 2*size-2; i++) {
			S.addTransition(out[i], FlexFactComponents.out(size+2, 1), out[i+1]);
		}
		
		if(size > 0) {
			S.addTransition(proc[proc.length - 1], FlexFactComponents.out(size+2, 1), out[0]);
			S.addTransition(out[out.length - 1], FlexFactComponents.out(size+2, 1), ok);
		}
		
		S.selfLoops(controls);
		
		S.setFinal(ok, true);
		
		return S;
		
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
		
		return new int[] {Pp.getStates().size(), Pp.getTransitions().size()};
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
		
		return new int[] {Pp.getStates().size(), Pp.getTransitions().size()};
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
