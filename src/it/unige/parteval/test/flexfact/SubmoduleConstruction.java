package it.unige.parteval.test.flexfact;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class SubmoduleConstruction {
	
	@Test
	public void testPlantConstruction() {
		
		// TODO
			
	}
	
	
	DFAutomatonImpl specification() {
		
		StateImpl p0 = new StateImpl("p0");
		StateImpl p1 = new StateImpl("p1");
		StateImpl p2 = new StateImpl("p2");
		StateImpl p3 = new StateImpl("p3");
		
		DFAutomatonImpl S = new DFAutomatonImpl(p0);
		
		S.addTransition(p0, FlexFactComponents.move(0, 0, 1, 0), p1);
		
		S.addTransition(p1, FlexFactComponents.process(1, 0), p2);
		S.addTransition(p1, FlexFactComponents.process(0, 0), p2);
		S.addTransition(p1, FlexFactComponents.process(0, 1), p2);
		S.addTransition(p1, FlexFactComponents.process(0, 2), p2);
		
		S.addTransition(p2, FlexFactComponents.out(2, 0), p3);
		
		S.addTransition(p3, FlexFactComponents.reload(0, 0), p0);
		
		S.addTransition(p1, FlexFactComponents.move(1, 0, 1, 1), p1);
		S.addTransition(p1, FlexFactComponents.move(1, 1, 1, 0), p1);
		S.addTransition(p1, FlexFactComponents.move(1, 0, 2, 0), p1);
		
		S.addTransition(p1, FlexFactComponents.move(0, 1, 1, 1), p1);
		S.addTransition(p1, FlexFactComponents.move(1, 1, 0, 1), p1);
		S.addTransition(p1, FlexFactComponents.move(1, 1, 2, 1), p1);
		S.addTransition(p1, FlexFactComponents.move(2, 1, 1, 1), p1);
		
		S.addTransition(p2, FlexFactComponents.move(1, 0, 1, 1), p2);
		S.addTransition(p2, FlexFactComponents.move(1, 1, 1, 0), p2);
		S.addTransition(p2, FlexFactComponents.move(1, 0, 2, 0), p2);
		
		S.addTransition(p2, FlexFactComponents.move(0, 1, 1, 1), p2);
		S.addTransition(p2, FlexFactComponents.move(1, 1, 0, 1), p2);
		S.addTransition(p2, FlexFactComponents.move(1, 1, 2, 1), p2);
		S.addTransition(p2, FlexFactComponents.move(2, 1, 1, 1), p2);
		
		S.setFinal(p3, true);
		
		return S;
		
	}


	Set<String> getControls() {
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getRotaryTableControls(1, 0));
		controls.addAll(FlexFactComponents.getExitSlideControls(2, 0));
		
		return controls;
	}

}
