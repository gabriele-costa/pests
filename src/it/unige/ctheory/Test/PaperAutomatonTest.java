package it.unige.ctheory.Test;

import java.util.*;

import org.junit.Test;


import it.unige.automata.*;
import it.unige.automata.impl.*;
import it.unige.automata.util.Printer;
import it.unige.ctheory.NaturalProjection;
import junit.framework.TestCase;

public abstract class PaperAutomatonTest extends TestCase {
	protected Automaton automaton;
	protected Set<String> sigma;
	protected Set<State> expectedResultReachable;
	protected ArrayList<ArrayList<State>> expectedRstar;
	protected ArrayList<ArrayList<State>> expectedRplus;
	
	
	protected static Automaton makeAutomaton(State[] states, Transition[] trans){
		Automaton A = new NFAutomatonImpl(states[0]);
				
		for(int i=0; i< states.length; ++i){
			A.addState(states[i]);
			A.setFinal(states[i], true);
		}
		
		for(Transition t : trans)
			A.addTransition(t);
			
		return A;
	}
	
	protected static Set<Set<State>> toSetSet(ArrayList<ArrayList<State>> lls){
		Set<Set<State>> result = new HashSet<>();
		
		for(ArrayList<State> l : lls)
			result.add(new HashSet<>(l));		
		
		return result;
	}
	
	@Test
	public void testReachableStates() {
		Set<State> actualResult = new HashSet<>();
		actualResult.addAll(NaturalProjection.reachableStates(automaton, automaton.getInitial(), sigma));
		assertEquals("testReachableStates", expectedResultReachable, actualResult);
		System.out.println(this.getClass().getSimpleName() + "." + "testReachableStates:" + actualResult );
	}


	@Test
	public void testComputeRStar() {
		ArrayList<ArrayList<State>> result = NaturalProjection.computeRStar(automaton, sigma);
		Set<Set<State>> actualRstar = toSetSet(result);
		Set<Set<State>> expectedRstarSet = toSetSet(expectedRstar);
		
		assertEquals("testComputeRstar", expectedRstarSet, actualRstar);
		System.out.println(this.getClass().getSimpleName() + "." + "testComputeRstar:" + expectedRstarSet);
	}

	@Test
	public void testComputeRPlus() {
		ArrayList<ArrayList<State>> result = NaturalProjection.computeRPlus(automaton, expectedRstar, sigma);
		Set<Set<State>> actualRplus = toSetSet(result);
		Set<Set<State>> expectedRplusSet = toSetSet(expectedRplus);
		
		assertEquals("testComputeRplus", expectedRplusSet, actualRplus);
		System.out.println(this.getClass().getSimpleName() + "." + "testComputeRplus:" + expectedRplusSet);
	}

	
	@Test
	public void testProjAutomatonSetOfString() {
		Automaton proj = NaturalProjection.proj(automaton, sigma);
		System.out.println(this.getClass().getSimpleName() + "." + "testProjAutomatonSetOfString:\n" + 
							Printer.printDotAutomaton(proj, this.getClass().getSimpleName()));
	}

	
	@Test
	public void testProjAutomatonAutomatonSetOfString() {
		State s0 = new StateImpl("s0");
		Automaton SigmaStar = new DFAutomatonImpl(s0);
		SigmaStar.setFinal(s0, true);
		
		for(String s : sigma)
			SigmaStar.addTransition(new TransitionImpl(s0, s, s0));
		
		Printer.createDotGraph(Printer.printDotAutomaton(automaton, this.getClass().getSimpleName()), "natural.automaton." + this.getClass().getSimpleName());
		Printer.createDotGraph(Printer.printDotAutomaton(SigmaStar, this.getClass().getSimpleName()), "natural.sigmastar." + this.getClass().getSimpleName());

		Automaton proj = NaturalProjection.proj(automaton, SigmaStar, new HashSet<>());
		
		Printer.createDotGraph(Printer.printDotAutomaton(proj, this.getClass().getSimpleName()), "natural.projection." + this.getClass().getSimpleName());
		// System.out.println(this.getClass().getSimpleName() + "." + "testProjAutomatonAutomatonSetOfString:\n" + 
		//					Printer.printDotAutomaton(proj, this.getClass().getSimpleName()));
	}

}
