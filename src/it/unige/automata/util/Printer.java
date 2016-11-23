package it.unige.automata.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.parteval.Main;

public class Printer {

	public static String printAutomaton(Automaton A) {
		StringBuilder builder = new StringBuilder();
		
		for(Transition t : A.getTransitions()) {
			if(A.getInitial().equals(t.getSource()))
				builder.append("(i) ");
			builder.append(t.getSource().getLabel() + " -- " + t.getLabel() + " -> " +t.getDestination().getLabel());
			if(A.getFinals().contains(t.getDestination()))
				builder.append(" (f)");
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	public static String printDotAutomaton(Automaton A, String name) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("digraph "+name+" {\n"
				+ "\trankdir=LR;\n"
				+ "\tsize=\"8,5\"\n");
		
		builder.append("\tnode [shape = point]; fakeinit ;\n");
		
		if(!A.getFinals().isEmpty()) {
				
			builder.append("\tnode [shape = doublecircle]; ");
			
			for(State s : A.getFinals()) {
				builder.append(s.getLabel() + " ");
			}
			
			builder.append(";\n");
		}
		
//		if(!A.getFails().isEmpty()) {
//			builder.append("\tnode [shape = circle, style=filled, fillcolor=red]; ");
//			
//			for(State s : A.getFails()) {
//				builder.append(s.getLabel() + " ");
//			}
//			
//			builder.append(";\n");
//		}
		
		builder.append("\tnode [shape = circle, fillcolor=white];\n");
		
		builder.append("\tfakeinit -> " + A.getInitial().getLabel() + ";\n");
		
		for(Transition t : A.getTransitions()) {
			builder.append("\t" + t.getSource().getLabel() + " -> " + t.getDestination().getLabel() + " [ label = \"" + t.getLabel() + "\"] ;\n");
		}
		
		builder.append("}\n");
		
		
		return builder.toString();	
		
	}
	
}
