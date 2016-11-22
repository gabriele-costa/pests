package it.unige.automata.impl;

import it.unige.automata.State;
import it.unige.automata.Transition;

public class TransitionImpl implements Transition {

	State src, dst;
	String label;
	
	public TransitionImpl(State s, String l, State d) {
		src = s;
		label = l;
		dst = d;
	}
	
	@Override
	public State getSource() {
		return src;
	}

	@Override
	public State getDestination() {
		return dst;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((src == null) ? 0 : src.hashCode());
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
		TransitionImpl other = (TransitionImpl) obj;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransitionImpl [src=" + src + ", label=" + label + ", dst=" + dst + "]";
	}

}
