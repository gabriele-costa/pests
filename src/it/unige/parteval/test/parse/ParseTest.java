package it.unige.parteval.test.parse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.Automaton;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.AutomataTextualInterface;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class ParseTest {

	@Test
	public void test() {
		
		DFAutomatonImpl P = getAutomaton();
		
		String s = AutomataTextualInterface.write(P);
		
		System.out.println(s + "\n\n");
		
		DFAutomatonImpl Q = AutomataTextualInterface.read(s);
		
		String t = AutomataTextualInterface.write(Q);
		
		System.out.println(t);
		
	}
	
	
	private DFAutomatonImpl getAutomaton() {
	   	
	   	StateImpl p0 = new StateImpl("p0");
	   	StateImpl p1 = new StateImpl("p1");
	   	//StateImpl ff = new StateImpl("ff");
	   	
	   	DFAutomatonImpl P = new DFAutomatonImpl(p0);
	   	
	   	P.addTransition(new TransitionImpl(p0, "a", p1));
	   	P.addTransition(new TransitionImpl(p1, "b", p0));
	   	P.addTransition(new TransitionImpl(p0, "s", p0));
	   	P.addTransition(new TransitionImpl(p1, "s", p1));
	   	
	   	P.setFinal(p0, true);
	   	
	   	return P;
	}
}
	
