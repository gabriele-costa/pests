package it.unige.automata;

public interface Transition {
	
	public State getSource();
	
	public State getDestination();
	
	public String getLabel();

}
