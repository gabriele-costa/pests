package it.unige.automata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class Parser {
	
	public static Automaton<TransitionImpl> parseDotAutomaton(String s) throws IllegalArgumentException {
		
		// TODO test me!
		
		Pattern initP = Pattern.compile("fakeinit -> \\w+;");
		Pattern tranP = Pattern.compile("\\w+ -> \\w+ "+Pattern.quote("[")+" label = \"\\w+\""+Pattern.quote("]"));
		
		Matcher initM = initP.matcher(s);
	
		State init = null; 
		
		if(initM.find()) {
			String match = initM.group(1);
			init = new StateImpl(match.split("->")[1].trim());
		}
		else {
			throw new IllegalArgumentException("Missing initial state");
		}
		
		Automaton<TransitionImpl> A = new DFAutomatonImpl(init);
		
		Matcher transM = tranP.matcher(s);
		transM.find();
		for(int i = 1; i <= transM.groupCount(); i++) {
			
			String[] token = transM.group(i).split("->[]"); 
			
			A.addTransition(new StateImpl(token[0].trim()), 
					token[2].substring(9).trim(), 
					new StateImpl(token[1].trim()));
		}
		
		return A;
	}

}
