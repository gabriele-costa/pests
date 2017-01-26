package it.unige.parteval.Test;

import it.unige.automata.*;
import it.unige.automata.impl.*;
import java.util.*;



public class AutomatonEx322Test extends PaperAutomatonTest {
	
	@Override
	protected void setUp(){
		State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3") };

		Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
				new TransitionImpl(states[0], "b", states[3]), new TransitionImpl(states[1], "a", states[1]),
				new TransitionImpl(states[1], "b", states[2]), new TransitionImpl(states[2], "c", states[3]) };
		automaton = makeAutomaton(states, trans);
		
		sigma = new HashSet<>();
		sigma.add("a");
		sigma.add("b");
		cosigma = new HashSet<>();
		cosigma.add("c");
		
		
		expectedResultReachable = new HashSet<>();
		expectedResultReachable.add(states[0]);
		expectedResultReachable.add(states[1]);
		expectedResultReachable.add(states[2]);
		expectedResultReachable.add(states[3]);
		
		expectedRstar = new ArrayList<>();
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[0])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[1])));
		expectedRstar.add(new ArrayList<>(Arrays.asList(states[2],states[3])));
		
		expectedRplus = new ArrayList<>();
		expectedRplus.addAll(expectedRstar);
		
	}

}
