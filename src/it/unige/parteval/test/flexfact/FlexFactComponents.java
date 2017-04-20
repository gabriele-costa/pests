package it.unige.parteval.test.flexfact;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;

public class FlexFactComponents {
	
	private static final String StoppedFull = "sf";
	private static final String LeftFull = "lf";
	private static final String LeftEmpty = "le";
	private static final String RightFull = "rf";
	private static final String RightEmpty = "re";
	private static final String StoppedEmpty = "se";
	private static final String MovingFull = "mf";
	public static String CB = "CB";
	public static String PM = "PM";
	public static String RT = "RT";
	public static String SF = "SF";
	public static String ES = "ES";
	
	public static String start = "start";
	public static String stop = "stop";
	public static String move = "move";
	
	public static String move(int x, int y, int x1, int y1) {
		return move +"("+ x +","+ y + " -> "+ x1 + ","+ y1 + ")";
	}
	
	public static String start(int x, int y, boolean plus) {
		return start + "(" + x + "," + y + "," + ((plus) ? "+" : "-") + ")";
	}
	
	public static String stop(int x, int y) {
		return stop + "(" + x + "," + y + ")";
	}

	public static DFAutomatonImpl ConveyorBelt(int x, int y, boolean horizontal) {
		
		StateImpl se = new StateImpl(CB + StoppedEmpty);
		
		StateImpl re = new StateImpl(CB + RightEmpty);
		StateImpl rf = new StateImpl(CB + RightFull);
		
		StateImpl le = new StateImpl(CB + LeftEmpty);
		StateImpl lf = new StateImpl(CB + LeftFull);
		
		StateImpl mf = new StateImpl(CB + MovingFull);
		StateImpl sf = new StateImpl(CB + StoppedFull);
	   	
	   	DFAutomatonImpl C = new DFAutomatonImpl(se);
	   	
	   	C.addTransition(se, start(x, y, true), re);
	   	C.addTransition(se, start(x, y, false), le);
	   	

	   	if(horizontal) {
		   	C.addTransition(re, move(x-1,y,x,y), mf);
		   	C.addTransition(le, move(x+1,y,x,y), mf);
	   	}
	   	else {
		   	C.addTransition(re, move(x,y-1,x,y), mf);
		   	C.addTransition(le, move(x,y+1,x,y), mf);	   		
	   	}
	   	
	   	C.addTransition(mf, stop(x,y), sf);
	   	
	   	C.addTransition(sf, start(x, y, true), rf);
	   	C.addTransition(sf, start(x, y, false), lf);
	   	
	   	if(horizontal) {
	   		C.addTransition(rf, move(x,y,x+1,y), se);
	   		C.addTransition(lf, move(x,y,x-1,y), se);
	   	}
	   	else {
	   		C.addTransition(rf, move(x,y,x,y+1), se);
	   		C.addTransition(lf, move(x,y,x,y-1), se);
	   	}
   		
   		C.setFinal(se, true);
	   		
	   	return C;
	}
	
	public static DFAutomatonImpl ProcessingMachine(int x, int y, boolean horizontal) {
		return null;
	}
	
	public static DFAutomatonImpl RotaryTable(int x, int y) {
		return null;
	}
	
	public static DFAutomatonImpl StackFeeder(int x, int y, boolean horizontal, boolean reverse, int size) {
		return null;
	}
	
	public static DFAutomatonImpl ExitSlide(int x, int y, boolean horizontal) {
		return null;
	}

}
