package it.unige.ctheory;

import java.util.*;
import it.unige.automata.*;
import it.unige.lts.*;

public class NaturalProjection {
	private static ArrayList<ArrayList<State>> AlgC1(ArrayList<ArrayList<State>> Chi) {

		int i = 1;
		int k = Chi.size();
		while(i <= k-1) {
			boolean flag = false;
			Set<State> Y = new HashSet<State>();
			ArrayList<Collection<State>> Chip = new ArrayList<Collection<State>>();
			int j = i + 1;
			while(j <= k) {
				Set<State> tmp = new HashSet<State>();
				tmp.addAll(Chi.get(i));
				tmp.retainAll(Chi.get(j));
				if(!tmp.isEmpty()) {
					flag = true;
					Y.addAll(Chi.get(j));
					Chi.remove(j);
				}
				else {
					Chip.add(Chi.get(j));
				}
				j++;
			}
			if(!flag) {
				i++;
			}
			else {
				Chi.get(i).addAll(Y);
				k = Chi.size();
			}
		}

		return Chi;
	}

	private static ArrayList<ArrayList<State>> AlgC2(ArrayList<ArrayList<ArrayList<State>>> P) {
		ArrayList<ArrayList<State>> Z = new ArrayList<ArrayList<State>>();
		for(ArrayList<ArrayList<State>> Pi : P) {
			Z.addAll(Pi);
		}

		return AlgC1(Z);
	}

	private static ArrayList<ArrayList<State>> AlgC3(LTS A, Set<String> Sigma_B) {
		ArrayList<State> Q = new ArrayList<State>();
		Q.addAll(A.states);
		State q0 = Q.get(0); // Assunzione sbagliata:  0 non corrisponde sempre allo stato iniziale. 
		ArrayList<String> Sigma_AmB = new ArrayList<String>();
		Sigma_AmB.addAll(A.Sigma());
		Sigma_AmB.removeAll(Sigma_B);
		Set<Transition> Tq0 = A.getTransitions(q0, Sigma_AmB); // Va presa la chiususa non solo il primo livello
		ArrayList<State> Xq0 = new ArrayList<State>();
		for(Transition t : Tq0) {
			Xq0.add(t.getDestination());
		}
		ArrayList<ArrayList<State>> Chi = new ArrayList<ArrayList<State>>(); // Meglio set di set
		Chi.add(Xq0);

		ArrayList<State> Y = new ArrayList<State>();   // SortedSet
		Y.addAll(Q);
		Y.removeAll(Xq0);
		while(!Y.isEmpty()) {
			State q = Y.get(0);
			Set<Transition> Tq = A.getTransitions(q, Sigma_AmB);
			ArrayList<State> Xq = new ArrayList<State>(); // Dobrebbe essere la chiusura
			for(Transition t : Tq) {
				Xq.add(t.getDestination());
			}
			Chi.add(Xq);
			Y.removeAll(Xq);
		}

		return AlgC1(Chi);
	}

	private static ArrayList<ArrayList<State>> AlgC4(LTS A, ArrayList<ArrayList<State>> Chi, ArrayList<String> Sigma_B) {
		ArrayList<ArrayList<ArrayList<State>>> PY = new ArrayList<ArrayList<ArrayList<State>>>();
		for(ArrayList<State> X_i : Chi){
			ArrayList<ArrayList<State>> Y_ij = new ArrayList<ArrayList<State>>();
			for(String sigma_j : Sigma_B){
				HashSet<State> X_ij = new HashSet<State>();
				for(State q : X_i){
					for(Transition Tq : A.getTransitions(q, sigma_j))
						X_ij.add(Tq.getDestination());
				}

				HashSet<State> Y = new HashSet<State>();
				Y_ij.addAll(Chi);

				for(ArrayList<State> X_k : Chi){
					HashSet<State> tmp = new HashSet<State>();
					tmp.addAll(X_ij);
					tmp.retainAll(X_k);
					if(!tmp.isEmpty()){
						Y.addAll(X_k);
						Y_ij.remove(X_k);
					}
				}
				ArrayList<State> tmp = new ArrayList<State>();
				tmp.addAll(Y);
				Y_ij.add(tmp);
			}
			PY.add(Y_ij);
		}
		return AlgC2(PY);
	}

	public static ArrayList<ArrayList<State>> computeRStar(LTS A, Set<String> Sigma_B){
		return AlgC3(A, Sigma_B);
	}
	
	/* Implementation schema: 
	 * 1 - Compute R* through Alg3
	 * 2 - Compute R+ => fixed point of \omega(R*) 
	 * 3 - Compute canonical projection h : Q -> Y (see paper pag. 17)
	 * 4 - Compute transitions (see paper pag. 17)
	 */
	public static LTS proj(LTS A, Set<String> Sigma_B) {
		return null;
	}
	
	public static LTS proj(LTS spec, LTS A, Set<String> Sigma_B) {
		return null;
	}
}
