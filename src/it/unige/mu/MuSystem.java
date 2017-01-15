package it.unige.mu;

import java.util.ArrayList;

public class MuSystem {

	public ArrayList<MuEquation> eq;
	
	public MuSystem() {
		eq = new ArrayList<MuEquation>();
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < eq.size(); i++)
			s += eq.get(i).toString() + "\n";

		return s;
	}
}
