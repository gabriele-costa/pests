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
				
		for(int i=1; i< states.length; ++i)
			A.addState(states[i]);
		
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
		
		assertEquals("testComputeRstar", expectedRplusSet, actualRplus);
		System.out.println(this.getClass().getSimpleName() + "." + "testComputeRstar:" + expectedRplusSet);
	}

	
	@Test
	public void testProjAutomatonSetOfString() {
		Automaton proj = NaturalProjection.proj(automaton, sigma);
		System.out.println(this.getClass().getSimpleName() + "." + "testProjAutomatonSetOfString:\n" + Printer.printDotAutomaton(proj, this.getClass().getSimpleName()));
	}

	/*@Ignore
	@Test
	public void testProjLTSLTSSetOfString() {
		fail("Not yet implemented");
	}*/

}
