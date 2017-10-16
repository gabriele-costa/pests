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

	final int BUFFER_MIN  = 5;
	
	final int BOUND = 101;
	final int STEP = 5;
	
	//@Test
	public void UAV_SCP() {
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			System.gc();
			long time = System.currentTimeMillis();
			int[] v = testSCP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
	}
	
	@Test
	public void UAV_CSP() {
		for(int i = BUFFER_MIN; i < BOUND; i+=STEP) {
			System.gc();
			long time = System.currentTimeMillis();
			int[] v = testCSP(i);
			time = System.currentTimeMillis() - time;
			System.out.println("Test "+i+") " + "Time=" + time +" ms, States="+v[0]+", Transitions=" + v[1]); 
		}
		
	}
	
	@Test
	public void FLEXFACT_CSP() {
		FlexFactPlant plant = new FlexFactPlant(3,2);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 0, true, true, 1);
		DFAutomatonImpl RT = FlexFactComponents.RotaryTable(1, 0);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(2, 0, true, true);
		DFAutomatonImpl PM = FlexFactComponents.ProcessingMachine(1, 1, false);
		
		plant.install(SF, 0, 0);
		plant.install(RT, 1, 0);
		plant.install(ES, 2, 0);
		plant.install(PM, 1, 1);
		
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		Printer.createDotGraph(Printer.printDotAutomaton(RT, "RT"), "RT");
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
		Printer.createDotGraph(Printer.printDotAutomaton(PM, "PM"), "PM");
			
		DFAutomatonImpl PA = plant.getPlantAutomaton();
		
		System.out.println("Plant ready. States: " + PA.getStates().size() + " Transitions: " + PA.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getRotaryTableControls(1, 0));
		controls.addAll(FlexFactComponents.getExitSlideControls(2, 0));
		controls.addAll(FlexFactComponents.getProcessingMachineControls(1, 1));
		
		DFAutomatonImpl spec = neverLeavePlant(3, 2, controls);
		
		System.out.println("Spec ready.");
		
		Printer.createDotGraph(Printer.printDotAutomaton(spec, "spec"), "spec");
		
		NFAutomatonImpl nPspec = Projection.partialA(spec, PA, new HashSet<String>(), controls);
		
		System.out.println("ND partial spec ready. States: " + nPspec.getStates().size() + " Transitions: " + nPspec.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(nPspec, "nPspec"), "nPspec");
		
		DFAutomatonImpl Pspec = Projection.unify(nPspec, getControls());
		
		System.out.println("Partial spec ready. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
		
//		Pspec.minimize();
//		
//		System.out.println("Partial spec minimized. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
//		
//		Pspec.collapse();
//		
//		System.out.println("Partial spec collapsed. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(Pspec, "Pspec"), "Pspec");
	}
	
	DFAutomatonImpl neverLeavePlant(int w, int h, Set<String> controls) {
		
		StateImpl ok = new StateImpl("ok");
		StateImpl fail = new StateImpl("fail");
		
		DFAutomatonImpl S = new DFAutomatonImpl(ok);
		
		for(String c : controls) {
			S.addTransition(ok, c, ok);
			S.addTransition(fail, c, fail);
		}
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				if(x < w-1) {
					S.addTransition(ok, FlexFactComponents.move(x, y, x+1, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y, x+1, y), fail);
					S.addTransition(ok, FlexFactComponents.move(x+1, y, x, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x+1, y, x, y), fail);
				}
				
				if(y < h-1) {
					S.addTransition(ok, FlexFactComponents.move(x, y, x, y+1), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y, x, y+1), fail);
					S.addTransition(ok, FlexFactComponents.move(x, y+1, x, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y+1, x, y), fail);
				}
			}
		}
		
		for(int x = 0; x < w; x++) {
			S.addTransition(ok, FlexFactComponents.move(x, 0, x, -1), fail);
			S.addTransition(ok, FlexFactComponents.move(x, h-1, x, h), fail);
		}
		for(int y = 0; y < h; y++) {
			S.addTransition(ok, FlexFactComponents.move(0, y, -1, y), fail);
			S.addTransition(ok, FlexFactComponents.move(w-1, y, w, y), fail);
		}
		
		S.setFinal(ok, true);
		
		return S;
		
	}
	
	Set<String> getControls() {
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getRotaryTableControls(1, 0));
		controls.addAll(FlexFactComponents.getExitSlideControls(2, 0));
		
		return controls;
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
