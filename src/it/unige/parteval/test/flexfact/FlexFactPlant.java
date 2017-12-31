package it.unige.parteval.test.flexfact;

import java.util.HashSet;
import java.util.Set;

import it.unige.automata.impl.DFAutomatonImpl;

public class FlexFactPlant {
	
	DFAutomatonImpl[][] plant;
	
	public FlexFactPlant(int width, int height) {
		plant = new DFAutomatonImpl[width][height];
	}
	
	public Set<String> getGamma(int x, int y) {
		
		Set<String> Gamma = new HashSet<String>();
		
		if(x > 0) {
			Gamma.add(FlexFactComponents.move(x-1, y, x, y));
			Gamma.add(FlexFactComponents.move(x, y, x-1, y));
		}
		if(y > 0) {
			Gamma.add(FlexFactComponents.move(x, y-1, x, y));
			Gamma.add(FlexFactComponents.move(x, y, x, y-1));
		}
		
		return Gamma;
	}
	
	public void install(DFAutomatonImpl C, int x, int y) {
		plant[x][y] = C;
	}
	
	public DFAutomatonImpl getPlantAutomaton() {
		DFAutomatonImpl P = null;
		
		for(int i = 0; i < plant.length; i++)
			for(int j = 0; j < plant[i].length; j++) {
				if(plant[i][j] != null) {
					
					System.gc();
					
					System.out.println("Adding component ("+i+","+j+")");
					
					P = (P == null) ? plant[i][j] : DFAutomatonImpl.parallel(P, plant[i][j], getGamma(i,j));
					
					System.out.println("Size: " + P.getStates().size() + " states, " + P.getTransitions().size() + " transitions");
				}
			}
		
		return P;
	}

}
