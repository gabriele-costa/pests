package it.unige.parteval.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.State;
import it.unige.automata.impl.*;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class CasestudyTest {

	final int N_DRONES = 1;
	final int N_NODES = 2;
	
	final String LOCK = "lock";
	final String FLY = "fly";
	final String UNLK = "unlock";
	final String CHG = "charge";
	
	private String lock(int d, int n) {
		return LOCK + d + "_" + n;
	}
	
	private String fly(int d) {
		// return FLY + d;
		return FLY;
	}
	
	private String unlock(int d, int n) {
		return UNLK + d + "_" + n;
	}
	
	private String charge(int d) {
		// return CHG + d;
		return CHG;
	}
	
    //@Test
    public void droneAvailability() {
    	runTest(getAvailabilityPolicy(), makeAvailabilitySigmaB(), makeAvailabilityGamma());
    }
    
    private Set<String> makeAvailabilitySigmaB() {
		HashSet<String> SigmaB = new HashSet<>();
		
		for(int d = 1; d <= N_DRONES; d++) {
			for(int n = 1; n <= N_NODES; n++) {
				SigmaB.add(lock(d, n));
				SigmaB.add(unlock(d, n));
			}
		}
		
		return SigmaB;
	}

	@Test
    public void droneRefueling() {
    	runTest(getRefuelingPolicy(), makeRefuelingSigmaB(), makeRefuelingGamma());
    }
    
    
    private Set<String> makeRefuelingSigmaB() {
    	HashSet<String> SigmaB = new HashSet<>();
		
		for(int d = 1; d <= N_DRONES; d++) {
			for(int n = 1; n <= N_NODES; n++) {
				SigmaB.add(lock(d, n));
				SigmaB.add(unlock(d, n));
			}
		}
		
		SigmaB.add(FLY);
		SigmaB.add(CHG);
		
		return SigmaB;
	}

	public void runTest(DFAutomatonImpl P, Set<String> SigmaB, Set<String> G) {
    	
		// System.out.println(Printer.printDotAutomaton(P, "Policy"));
		System.out.println("=============================");
		Printer.createDotGraph(Printer.printDotAutomaton(P, "Policy"), "Policy");
		
		System.out.println("POLICY SIZE: " + P.getStates().size() + " states, " + P.getTransitions().size() + " transitions");
    	
//    	DFAutomatonImpl T = getTower();
//		
//		System.out.println(Printer.printDotAutomaton(T, "Tower"));
//		System.out.println("=============================");
//		Printer.createDotGraph(Printer.printDotAutomaton(T, "Tower"), "Tower");
		
		DFAutomatonImpl[] D = new DFAutomatonImpl[N_DRONES]; 
		
		
		for(int i = 0; i < N_DRONES; i++) {
			// D[i] = getGeneralDrone(i+1); // too big
			D[i] = getSimpleDrone(i+1);
			
			
			// System.out.println(Printer.printDotAutomaton(D[i], "Drone"+(i+1)));
			// System.out.println("=============================");
			Printer.createDotGraph(Printer.printDotAutomaton(D[i], "Drone"+(i+1)), "Drone"+(i+1));
			
			System.out.println("MODEL "+i+" SIZE: " + D[i].getStates().size() + " states, " + D[i].getTransitions().size() + " transitions");
		}
		
		System.out.println("=============================");
		
		DFAutomatonImpl PpADetMin = P;
		DFAutomatonImpl PpADet = null;
		NFAutomatonImpl PpA = null;
		
		for(int i = 0; i < N_DRONES; i++) {
			//  addActions(D[i],SigmaB)
			PpA = Projection.partial(PpADetMin, D[i], SigmaB, G);
		
			// System.out.println(Printer.printDotAutomaton(PpA, "P_A"+i));
			// System.out.println("=============================");
			Printer.createDotGraph(Printer.printDotAutomaton(PpA, "P_A"+i), "P_A"+i);
			
			System.out.println(i+": NFA PARTIAL SIZE: " + PpA.getStates().size() + " states, " + PpA.getTransitions().size() + " transitions");
			
			PpADet = PpA.specialDFA(G);
		
			// System.out.println(Printer.printDotAutomaton(PpADet, "P_A_det"+i));
			// System.out.println("=============================");
			Printer.createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"+i), "P_A_det"+i);
			
			System.out.println(i+": DFA PARTIAL SIZE: " + PpADet.getStates().size() + " states, " + PpADet.getTransitions().size() + " transitions");
			
			PpADetMin = PpADet.minimize();
			
			PpADetMin.renameStates("q");
		
			// System.out.println(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"+i));
			// Printer.createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"+i), "P_A_det_min"+i);
		
			System.out.println(i+": MINIMIZED SIZE: " + PpADetMin.getStates().size() + " states, " + PpADetMin.getTransitions().size() + " transitions");
			
			System.out.println("=============================");
			
		}
		
		System.out.println("Writing on file 1/3");		
		Printer.createDotGraph(Printer.printDotAutomaton(PpA, "P_A"), "P_A");
		System.out.println("Writing on file 2/3");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADet, "P_A_det"), "P_A_det");
		System.out.println("Writing on file 3/3");
		Printer.createDotGraph(Printer.printDotAutomaton(PpADetMin, "P_A_det_min"), "P_A_det_min");

		System.out.println("\nFINISHED\n");

    }
    
    private Set<String> addActions(DFAutomatonImpl A, Set<String> SigmaB) {
		HashSet<String> Sigma = new HashSet<>();
		
		Sigma.addAll(SigmaB);
		Sigma.addAll(A.getAlphabet());

		return SigmaB;
	}

	private Set<String> makeAvailabilityGamma() {
		HashSet<String> Gamma = new HashSet<String>();
		
		return Gamma;
	}
    
    private Set<String> makeRefuelingGamma() {
		HashSet<String> Gamma = new HashSet<String>();
		for(int d = 0; d < N_DRONES; d++) {
			for(int n = 0; n < N_NODES; n++) {
				Gamma.add(lock(d+1, n+1));
				Gamma.add(unlock(d+1, n+1));
			}
		}
		return Gamma;
	}
    
    private DFAutomatonImpl getAvailabilityPolicy() {
    	 // At most N_NODES - N_DRONES lock
    	StateImpl C[] = new StateImpl[N_NODES - N_DRONES + 1];
    	
    	for(int i = 0; i < N_NODES - N_DRONES + 1; i++) {
    		C[i] = new StateImpl("C" + i);
    	}
    	
    	DFAutomatonImpl P = new DFAutomatonImpl(C[0]);
    	
    	for(int d = 0; d < N_DRONES; d++) {
    		for(int n = 0; n < N_NODES; n++) {
    			P.addTransition(new TransitionImpl(C[0], unlock(d, n), C[0]));
    	    	for(int i = 0; i < N_NODES - N_DRONES; i++) {	
    	    		P.addTransition(new TransitionImpl(C[i], lock(d, n), C[i + 1]));
	    			P.addTransition(new TransitionImpl(C[i + 1], unlock(d, n), C[i]));
	    		}
	    	}
    	}
    	
    	for(int i = 0; i < C.length; i++) {
    		P.setFinal(C[i], true);
    	}
    	
    	return P;
    }
    
    private DFAutomatonImpl getRefuelingPolicy() {
    	
    	StateImpl C[] = new StateImpl[N_NODES + 1];
    	StateImpl F = new StateImpl("fail");
    	
    	for(int i = 0; i < N_NODES + 1; i++) {
    		C[i] = new StateImpl("C" + i);
    	}
    	
    	DFAutomatonImpl P = new DFAutomatonImpl(C[0]);
    	
    	for(int d = 0; d < N_NODES; d++) {
			P.addTransition(new TransitionImpl(C[d], FLY, C[d + 1]));
			if(d > 0) { 
				P.addTransition(new TransitionImpl(C[d + 1], CHG, C[d - 1]));		
			}
    	}		
    	P.addTransition(new TransitionImpl(C[1], CHG, C[0]));
    	P.addTransition(new TransitionImpl(C[0], CHG, C[0]));
    	P.addTransition(new TransitionImpl(C[N_NODES], FLY, F));
    	P.addTransition(new TransitionImpl(F, FLY, F));
    	P.addTransition(new TransitionImpl(F, CHG, F));

    	for(int i = 0; i < N_NODES + 1; i++)
    		P.setFinal(C[i], true);
    	
    	
    	
    	return P;
    }
    
    private DFAutomatonImpl getTrivialPolicy() {
    	
    	StateImpl p0 = new StateImpl("p0");
    	StateImpl p1 = new StateImpl("p1");
    	StateImpl p2 = new StateImpl("p2");
    	
    	
    	DFAutomatonImpl P = new DFAutomatonImpl(p0);
    		
    	P.addTransition(new TransitionImpl(p0, CHG, p0));
    	P.addTransition(new TransitionImpl(p0, FLY, p1));
    	P.addTransition(new TransitionImpl(p1, FLY, p2));
    	P.addTransition(new TransitionImpl(p1, CHG, p0));
    	P.addTransition(new TransitionImpl(p2, CHG, p0));

    	P.setFinal(p0, true);
    	P.setFinal(p1, true);
    	P.setFinal(p2, true);
    	
    	return P;
    }
    
    private DFAutomatonImpl getSimpleDrone(int i) {
    	
    	assertTrue(i <= N_DRONES);
    	assertTrue(N_DRONES < N_NODES);
    	
    	int ip1 = 1+(i % (N_NODES+1));
    	
    	StateImpl H1 = new StateImpl("H" + i);
    	StateImpl H2 = new StateImpl("H" + ip1);
    	StateImpl T1 = new StateImpl("T" + i);
    	StateImpl T2 = new StateImpl("T" + ip1);
    	StateImpl L1 = new StateImpl("L" + i);
    	StateImpl L2 = new StateImpl("L" + ip1);
    	StateImpl R = new StateImpl("R");
    	
    	DFAutomatonImpl drone = new DFAutomatonImpl(H1);
    	// if drone joins from outside
    	// 		drone.addTransition(new TransitionImpl(init, lock(i,i) ,H[i]));
    	
			
		drone.addTransition(new TransitionImpl(R, charge(i), H1));
		drone.addTransition(new TransitionImpl(H1, lock(i,ip1), T1));
		drone.addTransition(new TransitionImpl(H2, lock(i,i), T2));
		drone.addTransition(new TransitionImpl(T1, fly(i), L1));
		drone.addTransition(new TransitionImpl(T2, fly(i), L2));
		drone.addTransition(new TransitionImpl(L1, unlock(i,i), H2));
		drone.addTransition(new TransitionImpl(L2, unlock(i,ip1), R));
		
		drone.setFinal(H1, true);
		drone.setFinal(H2, true);
    	
    	return drone;
    }

	private DFAutomatonImpl getGeneralDrone(int i) {
    	
    	assertTrue(i <= N_DRONES);
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

    	// if drone joins from outside
//    	StateImpl init = new StateImpl("i"+i);

    	// if drone already in
    	StateImpl init = H[i-1];

    	
    	DFAutomatonImpl drone = new DFAutomatonImpl(init);
    	// if drone joins from outside
// 		drone.addTransition(new TransitionImpl(init, lock(i,i) ,H[i]));
    	
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
	
	private void swap(int[] v, int i, int j) {
		int tmp = v[i];
		v[i] = v[j];
		v[j] = tmp;
	}
	
	private List<int[]> permute(int[] arr, int k){
		ArrayList<int[]> list = new ArrayList<int[]>();
		if(k == arr.length) {
        	list.add(arr.clone());
        	return list;
        }
        
		for(int i = k; i < arr.length; i++){
            swap(arr, i, k);
            list.addAll(permute(arr, k+1));
            swap(arr, k, i);
        }
		return list;
    }
    
    private DFAutomatonImpl getTower() {
    	
    	assertTrue(N_DRONES > 0);
    	assertTrue(N_NODES > N_DRONES);
    	
    	int iv[] = new int[N_NODES];
    	for(int i = 0; i < N_NODES; i++) {
    		if(i < N_DRONES) {
    			iv[i] = i + 1;
    		}
    		else {
    			iv[i] = 0;
    		}
    	}
    	
    	List<int[]> stateEnc = permute(iv, 0);
    	
    	State init = makeState(iv);
    	
    	DFAutomatonImpl tower = new DFAutomatonImpl(init);
    	
    	for(int[] v : stateEnc) {
    		State s = makeState(v);
    		tower.addState(s);
    		tower.setFinal(s, true);
    	}
    	
    	List<int[]> next, previous;
    	previous = stateEnc;
    	
    	do {
    		
    		next = generateNextLayer(previous);
    		
    		for(int[] nx : next) 
    			for(int[] pv : previous)
    				if(related(pv, nx)) {
    					State t = makeState(pv);
    					State d = makeState(nx);
    					tower.addTransition(new TransitionImpl(t, makeLock(pv, nx), d));
    					tower.addTransition(new TransitionImpl(t, makeUnlock(nx, pv), d));    					
    				}
    		
    		
    		previous = next;
    		
    		
    	} while(!last(next));
    	
    	return tower;
    }

	private String makeUnlock(int[] nx, int[] pv) {
		int h = -1;
		for(int i = 0; i < nx.length; i++) {
			if(pv[i] != nx[i]) {
				h = i;
				break;
			}
		}
		
		assertTrue(h >= 0);
		
		return unlock(nx[h], h);
	}

	private String makeLock(int[] pv, int[] nx) {
		int h = -1;
		for(int i = 0; i < nx.length; i++) {
			if(pv[i] != nx[i]) {
				h = i;
				break;
			}
		}
		
		assertTrue(h >= 0);
		
		return lock(nx[h], h);
	}

	private boolean related(int[] pv, int[] nx) {
		boolean f = false;
		for(int i = 0; i < pv.length; i++) {
			if(pv[i] != nx[i]) {
				if(f)
					return false;
				else 
					f = true;
			}
		}
		
		return f;
	}

	private List<int[]> generateNextLayer(List<int[]> previous) {
		int[] gv = previous.get(0).clone();
		int a = -1;
		for(int i = 0; i < gv.length; i++)
			if(gv[i] == 0)
				a = i;
		
		assertTrue(a >= 0);
		
		List<int[]> layer = new ArrayList<int[]>();
		
		for(int d = 0; d < N_DRONES; d++) {
			if(!replicated(d+1, gv)) {
				gv[a] = d + 1;
				layer.addAll(permute(gv, 0));
			}
		}
		
		return layer;
	}

	private boolean replicated(int d, int[] gv) {
		boolean f = false;
		for(int i = 0; i < gv.length; i++) {
			if(gv[i] == d)
				if(f)
					return true;
				else
					f = true;
		}
		return false;
	}

	private boolean last(List<int[]> layer) {
		
		if(layer.size() <= 0)
			return true;
		
		int[] v = layer.get(0);
		for(int i = 0; i < v.length; i++)
			if(v[i] == 0)
				return false;
		return true;
	}

	private State makeState(int b[]) {
    	String l = "H";
    	for(int i = 0; i < b.length; i++) {
    		l += b[i];
    	}
    	
    	return new StateImpl(l);
    }

}
