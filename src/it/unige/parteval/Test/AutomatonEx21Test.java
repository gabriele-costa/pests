package it.unige.parteval.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;


/*
 * Issues:
 * In the paper they said that the minimal generator of the projection should have 5 states and 15 transitions.
 * But I can't understand why. 
 */
public class AutomatonEx21Test extends PaperAutomatonTest {
	
	@Override
	protected void setUp(){
		State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2") };

		Transition[] trans = { 
				new TransitionImpl(states[0], "a", states[1]),
				new TransitionImpl(states[0], "d", states[1]),
				new TransitionImpl(states[0], "b", states[2]), 
				new TransitionImpl(states[1], "c", states[1]),
				new TransitionImpl(states[1], "a", states[2]),
				new TransitionImpl(states[1], "b", states[0]),
				new TransitionImpl(states[2], "c", states[2]),
				new TransitionImpl(states[2], "a", states[0]),};
		automaton = makeAutomaton(states, trans);
		
		sigma = new HashSet<>();
		sigma.add("a");
		sigma.add("b");
		sigma.add("c");
		cosigma = new HashSet<>();
		cosigma.add("d");
		
		
		expectedResultReachable = new HashSet<>();
		expectedResultReachable.add(states[0]);
		expectedResultReachable.add(states[1]);
		expectedResultReachable.add(states[2]);
		
		expectedRstar = new ArrayList<>();
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[0],states[1])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[2])));
		
		expectedRplus = new ArrayList<>();
		expectedRplus.add(new ArrayList<>(Arrays.asList(states[0], states[1], states[2])));
		
	}
	
}
