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
	
	public int size() {
		int s = 0;
		for(MuEquation e : eq) {
			s += e.size();
		}
		return s;
	}
}
