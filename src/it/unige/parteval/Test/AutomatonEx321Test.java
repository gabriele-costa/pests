package it.unige.parteval.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class AutomatonEx321Test extends PaperAutomatonTest {
	@Override
	protected void setUp(){
		State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3"), new StateImpl("q4") };

		Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
				new TransitionImpl(states[0], "b", states[2]), new TransitionImpl(states[1], "c", states[3]),
				new TransitionImpl(states[2], "d", states[3]), new TransitionImpl(states[3], "e", states[4]) };
		automaton = makeAutomaton(states, trans);
		
		sigma = new HashSet<>();
		sigma.add("a");
		sigma.add("b");
		sigma.add("e");
		cosigma = new HashSet<>();
		cosigma.add("c");
		cosigma.add("d");
		
		
		expectedResultReachable = new HashSet<>();
		expectedResultReachable.add(states[0]);
		expectedResultReachable.add(states[1]);
		expectedResultReachable.add(states[2]);
		
		expectedRstar = new ArrayList<>();
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[0])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[1],states[2],states[3])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[4])));
		
		expectedRplus = new ArrayList<>();
		expectedRplus.addAll(expectedRstar);
		
	}
}
