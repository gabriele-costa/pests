package it.unige.parteval.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.State;
import it.unige.automata.impl.*;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class OfficialTest {

	final int N_DRONES = 1;
	final int N_NODES = 3;
	
	final String LOCK = "lock";
	final String FLY = "fly";
	final String UNLK = "unlock";
	final String CHG = "charge";
	
	private String lock(int d, int n) {
		return LOCK + d + "_" + n;
	}
	
	private String fly(int d) {
		return FLY + d;
	}
	
	private String unlock(int d, int n) {
		return UNLK + d + "_" + n;
	}
	
	private String charge(int d) {
		return CHG + d;
	}
	
    @Test
    public void dronePartial() {
    	
    	DFAutomatonImpl T = getTower();
		
		System.out.println(Printer.printDotAutomaton(T, "Tower"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(T, "Tower"), "Tower");
		
		for(int i = 0; i < N_DRONES; i++) {
			DFAutomatonImpl Di = getDrone(i);
			
			System.out.println(Printer.printDotAutomaton(Di, "Drone"+i));
			System.out.println("=============================");
			Printer.createDotGraph(Printer.printDotAutomaton(Di, "Drone"+i), "Drone"+i);
		}
		
		Set<String> G = makeGamma();
		
//		NFAutomatonImpl PpA = Projection.partial(P, A, G);
//		
//		System.out.println(Printer.printDotAutomaton(PpA, "P_A"));
//		System.out.println("=============================");
//		Printer.createDotGraph(Printer.printDotAutomaton(PpA, "P_A"), "P_A");
//		
//		DFAutomatonImpl PpADet = PpA.specialDFA(G);
//		
//		System.out.println(Printer.printDotAutomaton(PpADet, "P_A_det"));
//		System.out.println("=============================");
//		Printer.createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"), "P_A_det");
//		
//		System.out.println("SIZE: " + PpADet.getStates().size() + " states, " + PpADet.getTransitions().size() + " transitions");
//		
//		
//		DFAutomatonImpl PpADetMin = PpADet.minimize();
//		
//		System.out.println(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"));
//		System.out.println("=============================");
//		Printer.createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"), "P_A_det_min");
//
//		System.out.println("\nFINISHED\n");

    }
    
    private Set<String> makeGamma() {
		HashSet<String> Gamma = new HashSet<String>();
		for(int d = 0; d < N_DRONES; d++)
			for(int n = 0; n < N_NODES; n++) {
				Gamma.add(lock(d, n));
				Gamma.add(unlock(d, n));
			}
		return Gamma;
	}

	private DFAutomatonImpl getDrone(int i) {
    	
    	assertTrue(i < N_DRONES);
    	assertTrue(N_DRONES < N_NODES);
    	
    	StateImpl H[] = new StateImpl[N_NODES];
    	StateImpl T[] = new StateImpl[2*N_NODES];
    	StateImpl L[] = new StateImpl[2*N_NODES];
    	
    	for(int j = 0; j < N_NODES; j++) {
    		H[j] = new StateImpl("H" + j);
    		T[j] = new StateImpl("T" + j + "_" + (j+1));
    		T[N_NODES + j] = new StateImpl("T" + (j+1) + "_" + j);
    		L[j] = new StateImpl("L" + j + "_" + (j+1));
    		L[N_NODES + j] = new StateImpl("L" + (j+1) + "_" + j);
    	}
    	
    	StateImpl init = new StateImpl("i"+i);
    	
    	DFAutomatonImpl drone = new DFAutomatonImpl(init);
    	drone.addTransition(new TransitionImpl(init, lock(i,i) ,H[i]));
    	
    	for(int j = 0; j < N_NODES; j++) {
    		
    		int jp1 = (j + 1) % N_NODES;
    		int jm1 = (N_NODES + (j - 1)) % N_NODES;
    		
    		drone.addTransition(new TransitionImpl(H[j], charge(i), H[j]));
    		drone.addTransition(new TransitionImpl(H[j], lock(i,jp1), T[j]));
    		drone.addTransition(new TransitionImpl(H[j], lock(i,jm1), T[N_NODES+j]));
    		drone.addTransition(new TransitionImpl(T[j], fly(i), L[j]));
    		drone.addTransition(new TransitionImpl(T[N_NODES+j], fly(i), L[N_NODES+j]));
    		drone.addTransition(new TransitionImpl(L[j], unlock(i,j), H[jp1]));
    		drone.addTransition(new TransitionImpl(L[N_NODES+j], unlock(i,j), H[jm1]));
    		
    		drone.setFinal(H[j], true);
    	}
    	
    	return drone;
    }
    
    private DFAutomatonImpl getTower() {
    	
    	int iv[] = new int[N_NODES];
    	for(int i = 0; i < N_NODES; i++)
    		iv[i] = -1;
    	
    	ArrayList<int[]> todo = new ArrayList<int[]>();
    	HashSet<int[]> done = new HashSet<int[]>();
    	todo.add(iv);
    	
    	State init = makeState(iv);
    	
    	DFAutomatonImpl tower = new DFAutomatonImpl(init);
    	
    	while(!todo.isEmpty()) {
    		int v[] = todo.remove(0);
    		
    		if(isDone(done, v)) {
    			System.out.println("Done: " + v);
    			continue;
    		}
    		
    		System.out.println("Doing: " + v);
    		
    		done.add(v);
    		State s = makeState(v);
    		
    		for(int i = 0; i < N_NODES; i++) {
    			if(v[i] >= 0) {
    				int w[] = v.clone();
    				int d = v[i];
    				w[i] = -1;
    				todo.add(w);
    				State dst = makeState(w);
    				tower.addTransition(new TransitionImpl(s, unlock(d, i), dst));
    			}
    			else {
    				for(int d = 0; d < N_DRONES; d++) {
    					int w[] = v.clone();
        				w[i] = d;
        				todo.add(w);
        				State dst = makeState(w);
        				tower.addTransition(new TransitionImpl(s, lock(d, i), dst));
        			}
    			}
    		}
    	}
    	
    	for(State s : tower.getStates()) {
    		tower.setFinal(s, true);
    	}	
    	
    	return tower;
    }
    
    private boolean isDone(HashSet<int[]> done, int[] v) {
		for(int[] u : done) {
			assertEquals("Vectors must have same length", v.length, u.length);
			boolean eq = true;
			for(int i = 0; i<v.length; i++) {
				eq &= (v[i] == u[i]);
			}
			if(eq)
				return true;
		}
		return false;
	}

	private State makeState(int b[]) {
    	String l = "H";
    	for(int i = 0; i < b.length; i++) {
    		l += (b[i] >= 0) ? b[i] : "e";
    	}
    	
    	return new StateImpl(l);
    }

}
