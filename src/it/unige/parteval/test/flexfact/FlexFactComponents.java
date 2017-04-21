package it.unige.parteval.test.flexfact;

import java.util.HashSet;
import java.util.Set;

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
	private static final String ItemProcessing = "ip";
	private static final String ReadyFull = "rd";
	private static final String EmptyStack = "es";
	private static final String FullStack = "fs";
	private static final String StackDelivering = "sd";
	
	public static final String CB = "CB";
	public static final String PM = "PM";
	public static final String RT = "RT";
	public static final String SF = "SF";
	public static final String ES = "ES";
	
	public static final String rotated 	= "rot";	
	
	public static final String start 	= "start";
	public static final String stop 	= "stop";
	public static final String move 	= "move";
	public static final String process 	= "process";
	public static final String skip 	= "skip";
	public static final String rotate 	= "rotate";
	public static final String out 		= "out";
	public static final String reload	= "reload";
	
	
	public static String move(int x, int y, int x1, int y1) {
		return move +"("+ x +","+ y + ","+ x1 + ","+ y1 + ")";
	}
	
	public static String start(int x, int y, boolean plus) {
		return start + "(" + x + "," + y + "," + ((plus) ? "+" : "-") + ")";
	}
	
	public static String stop(int x, int y) {
		return stop + "(" + x + "," + y + ")";
	}
	
	public static String process(int x, int y) {
		return process + "(" + x + "," + y + ")";
	}
	
	public static String skip(int x, int y) {
		return skip + "(" + x + "," + y + ")";
	}
	
	public static String out(int x, int y) {
		return out + "(" + x + "," + y + ")";
	}
	
	public static String rotate(int x, int y) {
		return rotate + "(" + x + "," + y + ")";
	}
	
	public static String reload(int x, int y) {
		return reload + "(" + x + "," + y + ")";
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
		StateImpl se = new StateImpl(PM + StoppedEmpty);
		
		StateImpl re = new StateImpl(PM + RightEmpty);
		StateImpl rf = new StateImpl(PM + RightFull);
		
		StateImpl le = new StateImpl(PM + LeftEmpty);
		StateImpl lf = new StateImpl(PM + LeftFull);
		
		StateImpl mf = new StateImpl(PM + MovingFull);
		StateImpl sf = new StateImpl(PM + StoppedFull);
		
		StateImpl ip = new StateImpl(PM + ItemProcessing);
	   	
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
	   	
	   	C.addTransition(sf, process(x,y), ip);
	   	C.addTransition(sf, skip(x,y), ip);
	   	
	   	C.addTransition(ip, start(x, y, true), rf);
	   	C.addTransition(ip, start(x, y, false), lf);
	   	
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
	
	public static DFAutomatonImpl RotaryTable(int x, int y) {
		
		StateImpl se = new StateImpl(PM + StoppedEmpty);
		
		StateImpl re = new StateImpl(PM + RightEmpty);
		StateImpl rf = new StateImpl(PM + RightFull);
		
		StateImpl le = new StateImpl(PM + LeftEmpty);
		StateImpl lf = new StateImpl(PM + LeftFull);
		
		StateImpl mf = new StateImpl(PM + MovingFull);
		StateImpl sf = new StateImpl(PM + StoppedFull);
		
		StateImpl rd = new StateImpl(PM + ReadyFull);
		
		// rotated state
		StateImpl ser = new StateImpl(PM + StoppedEmpty + rotated);
		
		StateImpl rer = new StateImpl(PM + RightEmpty + rotated);
		StateImpl rfr = new StateImpl(PM + RightFull + rotated);
		
		StateImpl ler = new StateImpl(PM + LeftEmpty + rotated);
		StateImpl lfr = new StateImpl(PM + LeftFull + rotated);
		
		StateImpl mfr = new StateImpl(PM + MovingFull + rotated);
		StateImpl sfr = new StateImpl(PM + StoppedFull + rotated);
		
		StateImpl rdr = new StateImpl(PM + ReadyFull + rotated);
	   	
	   	DFAutomatonImpl C = new DFAutomatonImpl(se);
	   	
	   	// straight
	   	C.addTransition(se, start(x, y, true), re);
	   	C.addTransition(se, start(x, y, false), le);
	   	
	   	C.addTransition(re, move(x-1,y,x,y), mf);
		C.addTransition(le, move(x+1,y,x,y), mf);
	   	
	   	C.addTransition(mf, stop(x,y), sf);
	   	
	   	C.addTransition(sf, skip(x,y), rd);
	   	
	   	C.addTransition(rd, start(x, y, true), rf);
	   	C.addTransition(rd, start(x, y, false), lf);
	   	
	   	C.addTransition(rf, move(x,y,x+1,y), se);
	   	C.addTransition(lf, move(x,y,x-1,y), se);
	   	
	   	// rotated
	   	C.addTransition(ser, start(x, y, true), rer);
	   	C.addTransition(ser, start(x, y, false), ler);
	   	
	   	C.addTransition(rer, move(x,y-1,x,y), mfr);
		C.addTransition(ler, move(x,y+1,x,y), mfr);
	   	
	   	C.addTransition(mfr, stop(x,y), sfr);
	   	
	   	C.addTransition(sfr, skip(x,y), rdr);
	   	
	   	C.addTransition(rdr, start(x, y, true), rfr);
	   	C.addTransition(rdr, start(x, y, false), lfr);
	   	
	   	C.addTransition(rfr, move(x,y,x,y+1), ser);
	   	C.addTransition(lfr, move(x,y,x,y-1), ser);
	   	
	   	// rotations
	   	C.addTransition(se, rotate(x, y), ser);
	   	C.addTransition(ser, rotate(x, y), se);
	   	
	   	C.addTransition(sf, rotate(x, y), sfr);
	   	C.addTransition(sfr, rotate(x, y), sf);
	   	
   		C.setFinal(se, true);
   		C.setFinal(ser, true);
	   		
	   	return C;
	}
	
	public static DFAutomatonImpl StackFeeder(int x, int y, boolean horizontal, boolean straight, int size) {
		
		StateImpl[] ss = new StateImpl[size*2];
		
		for(int i = 0; i < size; i++) {
			ss[i * 2] = new StateImpl(SF + FullStack + (size - i));
			ss[i * 2 + 1] = new StateImpl(SF + StackDelivering + (size - i));
		}
		
		StateImpl es = new StateImpl(SF + EmptyStack);
		
		DFAutomatonImpl C = new DFAutomatonImpl(ss[0]);
		
		for(int i = 0; i < size; i++) {
			C.addTransition(ss[i*2], start(x, y, straight), ss[i*2+1]);
			C.addTransition(ss[i*2], reload(x, y), ss[0]);
			
			if(i < size-1) {
				if(horizontal)
					C.addTransition(ss[i*2+1], move(x, y, (straight) ? x+1 : x-1, y), ss[(i+1)*2]);
				else
					C.addTransition(ss[i*2+1], move(x, y, x, (straight) ? y+1 : y-1), ss[(i+1)*2]);
			} else {
				if(horizontal)
					C.addTransition(ss[i*2+1], move(x, y, (straight) ? x+1 : x-1, y), es);
				else
					C.addTransition(ss[i*2+1], move(x, y, x, (straight) ? y+1 : y-1), es);
				
				C.addTransition(es, reload(x, y), ss[0]);
			}
		}
		
		C.setFinal(es, true);
		
		return C;
	}
	
	public static DFAutomatonImpl ExitSlide(int x, int y, boolean horizontal, boolean straight) {
		
		StateImpl es0 = new StateImpl(ES + 0);
		StateImpl es1 = new StateImpl(ES + 1);
		
		DFAutomatonImpl C = new DFAutomatonImpl(es0);
		
		if(horizontal) {
			if(straight) {
				C.addTransition(es0, move(x-1, y, x, y), es1);
			}
			else {
				C.addTransition(es0, move(x+1, y, x, y), es1);
			}
		}
		else {
			if(straight) {
				C.addTransition(es0, move(x, y-1, x, y), es1);
			}
			else {
				C.addTransition(es0, move(x, y+1, x, y), es1);
			}
		}
		C.addTransition(es1, out(x, y), es0);
		
		C.setFinal(es0, true);
		
		return C;
	}
	
	public static Set<String> getConveyBeltControls(int x, int y) {
		Set<String> G = new HashSet<String>();
		
		G.add(start(x, y, true));
		G.add(start(x, y, false));
		G.add(stop(x, y));
		
		return G;
	}
	
	public static Set<String> getProcessingMachineControls(int x, int y) {
		Set<String> G = new HashSet<String>();
		
		G.add(start(x, y, true));
		G.add(start(x, y, false));
		G.add(stop(x, y));
		G.add(skip(x, y));
		G.add(process(x, y));
		
		return G;
	}
	
	public static Set<String> getRotaryTableControls(int x, int y) {
		Set<String> G = new HashSet<String>();
		
		G.add(start(x, y, true));
		G.add(start(x, y, false));
		G.add(stop(x, y));
		G.add(rotate(x, y));
		
		return G;
	}
	
	public static Set<String> getStackFeederControls(int x, int y, boolean straight) {
		Set<String> G = new HashSet<String>();
		
		G.add(start(x, y, straight));
		G.add(reload(x, y));
		
		return G;
	}
	
	public static Set<String> getExitSlideControls(int x, int y) {
		Set<String> G = new HashSet<String>();
		
		G.add(out(x, y));
		
		return G;
	}

}
