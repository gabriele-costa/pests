package it.unige.parteval.Test;

import java.util.*;

import org.junit.Test;


import it.unige.automata.*;
import it.unige.automata.impl.*;
import it.unige.automata.util.Printer;
import it.unige.ctheory.NaturalProjection;
import it.unige.parteval.Projection;
import junit.framework.TestCase;

public abstract class PaperAutomatonTest extends TestCase {
	protected Automaton automaton;
	protected Set<String> sigma;
	protected Set<String> cosigma;
	protected Set<String> gamma;
	protected Set<State> expectedResultReachable;
	protected ArrayList<ArrayList<State>> expectedRstar;
	protected ArrayList<ArrayList<State>> expectedRplus;
	
	
	protected static Automaton makeAutomaton(State[] states, Transition[] trans){
		Automaton A = new NFAutomatonImpl(states[0]);
				
		for(int i=1; i< states.length; ++i){
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
	public void testProjAutomatonAutomatonSetOfString() {
		Automaton coSigmaAutomaton = makeComplementAutomato(cosigma);
		
		Printer.createDotGraph(Printer.printDotAutomaton(automaton, this.getClass().getSimpleName()), "partial.automaton." + this.getClass().getSimpleName());
		Printer.createDotGraph(Printer.printDotAutomaton(coSigmaAutomaton, this.getClass().getSimpleName()), "partial.cosigmastar." + this.getClass().getSimpleName());

		Set<String> SigmaB = new HashSet<String>();
		SigmaB.addAll(automaton.getAlphabet());
		SigmaB.removeAll(coSigmaAutomaton.getAlphabet());
		NFAutomatonImpl nfaproj = Projection.partial(automaton, coSigmaAutomaton, SigmaB, gamma);
		Printer.createDotGraph(Printer.printDotAutomaton(nfaproj, this.getClass().getSimpleName()), "partial.nfaprojection." + this.getClass().getSimpleName());
		
		DFAutomatonImpl proj = nfaproj.specialDFA(gamma);
		proj = proj.minimize();
		
		Printer.createDotGraph(Printer.printDotAutomaton(proj, this.getClass().getSimpleName()), "partial.projection." + this.getClass().getSimpleName());
	}

	private Automaton makeComplementAutomato(Set<String> cosigma2) {
		State s0 = new StateImpl("s0");
		Automaton SigmaStar = new DFAutomatonImpl(s0);
		SigmaStar.setFinal(s0, true);
		
		for(String s : cosigma2)
			SigmaStar.addTransition(new TransitionImpl(s0, s, s0));
		
		return SigmaStar;
	}

}
