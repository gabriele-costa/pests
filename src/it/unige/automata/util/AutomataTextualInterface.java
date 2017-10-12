package it.unige.automata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class AutomataTextualInterface {
	
	private static final String INIT = "initial:";
	private static final String FINS = "finals:";
	private static final String ALPH = "actions:";
	private static final String ARFR = "--";
	private static final String ARTO = "->";
	private static final String LEND = ";";
	private static final String LSEP = ",";
	
	public static Automaton<TransitionImpl> read(String s) throws IllegalArgumentException {
		
		// TODO test me!
		
		String[] lines = s.split("\n");
		
		String sinit = lines[0].substring(lines[0].indexOf(':')+1, lines[0].indexOf(LEND));
		State init = new StateImpl(sinit.trim());
		
		Automaton<TransitionImpl> A = new DFAutomatonImpl(init);
		
		String[] sf = lines[1].substring(lines[1].indexOf(':')+1).split(LSEP);
		for(int i = 0; i < sf.length; i++) {
			State f = new StateImpl(sf[i].trim());
			A.setFinal(f, true);
		}
		
//		String[] sa = lines[2].substring(lines[2].indexOf(':')+1).split(LSEP);
//		for(int i = 0; i < sa.length; i++) {
//			A.setFinal(f, true);
//		}
		
		for(int l = 3; l < lines.length; l++) {
			A.addTransition(readLine(lines[l]));
		}
		
		return A;
	}
	
	private static TransitionImpl readLine(String line) {
		String[] v = line.split(ARFR);
		State s = new StateImpl(v[0].trim());
		v = v[1].split(ARTO);
		String label = v[0].trim();
		State d = new StateImpl(v[1].substring(0, v[1].indexOf(LEND)).trim());
		
		return new TransitionImpl(s, label, d);
	}

	public static String write(DFAutomatonImpl A) {
		
		String output = INIT + A.getInitial() + LEND + "\n";
		output += FINS;
		for(State s : A.getFinals()) {
			output += s + LSEP;
		}
		
		output = output.substring(0, output.length() - 1);
		output += LEND + "\n";
		
		output += ALPH;
		for(String a : A.getAlphabet()) {
			output += a + LSEP;
		}
		
		output = output.substring(0, output.length() - 1);
		output += LEND + "\n";
		
		for(Transition t : A.getTransitions()) {
			output += t.getSource() + ARFR + t.getLabel() + ARTO + t.getDestination() + LEND + "\n";
		}
		
		return output;
	}

}
