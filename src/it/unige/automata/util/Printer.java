package it.unige.automata.util;


import java.io.File;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.event.SymbolicTransition;
import it.unige.automata.impl.TransitionImpl;

public class Printer {
	
	public static String type = "png";
	public static String outdir = "out/";

	public static String printAutomaton(Automaton<TransitionImpl> A) {
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

	public static String printDotAutomaton(Automaton<TransitionImpl> A, String name) {
		
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
	
	public static String printDotEAutomaton(Automaton<SymbolicTransition> A, String name) {
		
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
		
		for(SymbolicTransition t : A.getTransitions()) {
			builder.append("\t" + t.getSource().getLabel() + " -> " + t.getDestination().getLabel() + " [ label = \"" + t.getLabel() + "("+t.getVariable()+") : "+ t.getGuard().toString() + "\"] ;\n");
		}
		
		builder.append("}\n");
		
		
		return builder.toString();	
		
	}
	
	public static void createDotGraph(String dotFormat, String fileName) {
	    GraphViz gv = new GraphViz();
	    gv.add(dotFormat);
	    //String type = "png";
	    //gv.increaseDpi();
	    //gv.decreaseDpi();
	    File out = new File(outdir + fileName+"."+ type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	
}
