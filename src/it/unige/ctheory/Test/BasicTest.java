package it.unige.ctheory.Test;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import it.unige.automata.*;
import it.unige.automata.impl.*;
import it.unige.ctheory.*;


public class BasicTest {
	private static Automaton makeAutomaton(State[] states, Transition[] trans){
		Automaton A = new NFAutomatonImpl(states[0]);
				
		for(int i=1; i< states.length; ++i)
			A.addState(states[i]);
		
		for(Transition t : trans)
			A.addTransition(t);
			
		return A;
	}
	
	private static Automaton ltsEx322 = null;
	private static Automaton getEx322(){
		if(ltsEx322 == null){
			State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3") };

			Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
					new TransitionImpl(states[0], "b", states[3]), new TransitionImpl(states[1], "a", states[1]),
					new TransitionImpl(states[1], "b", states[2]), new TransitionImpl(states[2], "c", states[3]) };
			ltsEx322 = makeAutomaton(states, trans);
		}
		return ltsEx322;
	}
	
	private static Automaton ltsEx323 = null;

	private static Automaton getEx323() {
		if (ltsEx323 == null) {
			State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3"),
					new StateImpl("q4"), new StateImpl("q5") };

			Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
					new TransitionImpl(states[0], "b", states[5]), new TransitionImpl(states[0], "d", states[3]),
					new TransitionImpl(states[1], "a", states[1]), new TransitionImpl(states[1], "d", states[2]),
					new TransitionImpl(states[1], "b", states[4]), new TransitionImpl(states[2], "b", states[1]),
					new TransitionImpl(states[3], "b", states[0]), new TransitionImpl(states[3], "c", states[2]),
					new TransitionImpl(states[4], "c", states[5]), };
			ltsEx323 = makeAutomaton(states, trans);
		}
		return ltsEx323;
	}
	
	
	@Test
	public void testRStarComputationEx322(){
		HashSet<String> sigma = new HashSet<String>();
		sigma.add("a");
		sigma.add("b");
		sigma.add("d");
		ArrayList<ArrayList<State>> Rstar = NaturalProjection.computeRStar(getEx322(), sigma);
		System.out.println(Rstar);
	}
	
	@Test
	public void testRStarComputationEx323(){
		HashSet<String> sigma = new HashSet<String>();
		sigma.add("a");
		sigma.add("b");
		sigma.add("d");
		ArrayList<ArrayList<State>> Rstar = NaturalProjection.computeRStar(getEx323(), sigma);
		System.out.println(Rstar);
	}
	
	@Test
	public void testReachableStatesEx322(){
		HashSet<String> sigma = new HashSet<String>();
		sigma.add("a");
		sigma.add("b");
		ArrayList<State> reachable = NaturalProjection.reachableStates(getEx322(), getEx322().getInitial(), sigma);
		System.out.println(reachable);
	}
	
	@Test
	public void testReachableStatesEx323(){
		HashSet<String> sigma = new HashSet<String>();
		sigma.add("a");
		sigma.add("b");
		ArrayList<State> reachable = NaturalProjection.reachableStates(getEx323(), getEx323().getInitial(), sigma);
		System.out.println(reachable);
	}
	
	public static void main(String[] args){
		Result result = JUnitCore.runClasses(BasicTest.class);
		
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      System.out.println(result.wasSuccessful());
	}
}
