package it.unige.parteval.test;

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

public class AGVCasestudy {
	
	
	/*
Event Interpretation
// 1



// 2





















	 */

	final String z11 = "z11"; 				// 11 Unparks & enters Zone 1
	final String ips1 = "ips1"; 			// 10 Exits Zone 1 & loads from IPS1
	final String rz11 = "rz11";				// 13 Re-enters Zone 1
	final String ws11 = "ws12";				// 12 Exits Zone 1, unloads to WS2, & parks
	
	final String z23 = "z23"; 				// 21 Unparks & enters Zone 3
	final String z22 = "z22"; 				// 18 Exits Zone 3 & enters Zone 2
	final String z21 = "z21";				// 20 Exits Zone 2 & enters Zone 1
	final String ips2 = "ips2";				// 22 Exits Zone 1 & loads from IPS2
	final String rz21 = "rz21";				// 23 Re-enters Zone 1
	final String rz23 = "rz23";				// 26 Exits Zone 2 & re-enters Zone 3
	final String ws23 = "ws23";				// 28 Exits Zone 3, unloads to WS3, & parks
	
	final String z32 = "z32"; 				// 33 Unparks & enters Zone 2
	final String ws32 = "ws32"; 			// 34 Exits Zone 2 & loads from WS2
	final String rz32 = "rz32";				// 31 Re-enters Zone 2
	final String ws31 = "ws31";				// 32 Exits Zone 2, unloads to WS1, & parks
	
	final String z43 = "z43"; 				// 41 Unparks & enters Zone 3
	final String z44 = "z44"; 				// 40 Exits Zone 3 & enters Zone 4
	final String ws43 = "ws43";				// 42 Exits Zone 4 & loads from WS3
	final String rz44 = "rz44";				// 43 Re-enters Zone 4
	final String rz43 = "rz43";				// 44 Exits Zone 4 & re-enters Zone 3
	final String ws41 = "ws41";				// 46 Exits Zone 3, unloads at WS1, & parks
	
	final String z54 = "z54"; 				// 51 Unparks & enters Zone 4
	final String ws51 = "ws51"; 			// 50 Exits Zone 4 & loads from WS1
	final String rz54 = "rz54";				// 53 Re-enters Zone 4
	final String cps5 = "cps5";				// 52 Exits Zone 4, unloads to CPS, & parks
	
	
	
	DFAutomatonImpl z1spec() { return null; }
	
	DFAutomatonImpl z2spec() { return null; }
	
	DFAutomatonImpl z3spec() { return null; }
	
	DFAutomatonImpl z4spec() { return null; }
	
	DFAutomatonImpl ws1spec() { return null; }
	
	DFAutomatonImpl ws2spec() { return null; }
	
	DFAutomatonImpl ws3spec() { return null; }
	
	DFAutomatonImpl ipsspec() { return null; }
	
	DFAutomatonImpl agv1() { 

		State p0 = new StateImpl("p0");
		State p1 = new StateImpl("p1");
		State p2 = new StateImpl("p2");
		State p3 = new StateImpl("p3");
		
		DFAutomatonImpl agv1 = new DFAutomatonImpl(p0);
		
		//agv1.addTransition(new TransitionImpl(s, l, d))
		return null;
		
	}
	
	DFAutomatonImpl agv2() { return null; }
	
	DFAutomatonImpl agv3() { return null; }
	
	DFAutomatonImpl agv4() { return null; }
	
	DFAutomatonImpl agv5() { return null; }
	
	
	
	
	@Test
	/*
	 * Iteratively refines the most permissive controller
	 */
    public void sopconrefinement() {
    	
    }
    
	
}
