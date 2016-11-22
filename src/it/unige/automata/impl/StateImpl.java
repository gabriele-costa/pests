package it.unige.automata.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unige.automata.State;

public class StateImpl implements State {

	String label;
	
	
	public StateImpl(String l) {
		label = l;
	}
	
	@Override
	public int compareTo(State o) {
		return label.compareTo(o.getLabel());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateImpl other = (StateImpl) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return "StateImpl [label=" + label + "]";
	}
}
