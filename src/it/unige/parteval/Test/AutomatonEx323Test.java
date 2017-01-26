package it.unige.parteval.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class AutomatonEx323Test extends PaperAutomatonTest {
	@Override
	protected void setUp(){
		State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3"),
				new StateImpl("q4"), new StateImpl("q5") };

		Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
				new TransitionImpl(states[0], "b", states[5]), new TransitionImpl(states[0], "d", states[3]),
				new TransitionImpl(states[1], "a", states[1]), new TransitionImpl(states[1], "d", states[2]),
				new TransitionImpl(states[1], "b", states[4]), new TransitionImpl(states[2], "b", states[1]),
				new TransitionImpl(states[3], "b", states[0]), new TransitionImpl(states[3], "c", states[2]),
				new TransitionImpl(states[4], "c", states[5]) };
		
		automaton = makeAutomaton(states, trans);
		
		sigma = new HashSet<>();
		sigma.add("a");
		sigma.add("b");
		sigma.add("d");
		cosigma = new HashSet<>();
		cosigma.add("c");
		gamma = new HashSet<>();
		
		
		// Here to change
		
		expectedResultReachable = new HashSet<>();
		expectedResultReachable.add(states[0]);
		expectedResultReachable.add(states[1]);
		expectedResultReachable.add(states[2]);
		expectedResultReachable.add(states[3]);
		expectedResultReachable.add(states[4]);
		expectedResultReachable.add(states[5]);
		
		expectedRstar = new ArrayList<>();
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[0])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[1])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[2],states[3])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[4],states[5])));
		
		expectedRplus = new ArrayList<>();
		expectedRplus.add(new ArrayList<>(Arrays.asList(states[0],states[1])));
		expectedRplus.add(new ArrayList<>(Arrays.asList(states[2],states[3])));
		expectedRplus.add(new ArrayList<>(Arrays.asList(states[4],states[5])));
		
	}
}
