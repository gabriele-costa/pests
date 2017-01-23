package it.unige.ctheory.Test;

import java.util.*;

import org.junit.Test;

import it.unige.automata.*;
import it.unige.automata.impl.*;
import it.unige.ctheory.NaturalProjection;
import it.unige.lts.*;

public class BasicTest {
	private static LTS makeLTS(State[] states, Transition[] trans){
		LTS A = new LTS();
				
		A.inits = states[0];
		A.states.addAll(Arrays.asList(states));
		A.delta.addAll(Arrays.asList(trans));
			
		return A;
	}
	
	private static LTS ltsEx322 = null;
	private static LTS getEx322(){
		if(ltsEx322 == null){
			State[] states = { new StateImpl("q0"), new StateImpl("q1"), new StateImpl("q2"), new StateImpl("q3") };

			Transition[] trans = { new TransitionImpl(states[0], "a", states[1]),
					new TransitionImpl(states[0], "b", states[3]), new TransitionImpl(states[1], "a", states[1]),
					new TransitionImpl(states[1], "b", states[2]), new TransitionImpl(states[2], "c", states[3]), };
			ltsEx322 = makeLTS(states, trans);
		}
		return ltsEx322;
	}
	
		
	@Test
	public void computeRStar(){
		HashSet<String> sigma = new HashSet<String>();
		sigma.add("a");
		sigma.add("b");
		ArrayList<ArrayList<State>> Rstar = NaturalProjection.computeRStar(getEx322(), sigma);
		for(ArrayList<State> l : Rstar){
			System.out.print("{");
			for(State s : l){
				System.out.print(s.toString() + " ");
			}
			System.out.print("} ");
		}
	}
	
	public static void main(String[] args){
		BasicTest basic = new BasicTest();
		basic.computeRStar();
	}
}
