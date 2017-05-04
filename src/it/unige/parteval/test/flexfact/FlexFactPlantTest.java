package it.unige.parteval.test.flexfact;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class FlexFactPlantTest {
	
	@Test
	public void testProcessingPlant() {
		
		FlexFactPlant plant = new FlexFactPlant(3,2);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 0, true, true, 1);
		DFAutomatonImpl RT = FlexFactComponents.RotaryTable(1, 0);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(2, 0, true, true);
		DFAutomatonImpl PM = FlexFactComponents.ProcessingMachine(1, 1, false);
		
		plant.install(SF, 0, 0);
		plant.install(RT, 1, 0);
		plant.install(ES, 2, 0);
		plant.install(PM, 1, 1);
		
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		Printer.createDotGraph(Printer.printDotAutomaton(RT, "RT"), "RT");
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
		Printer.createDotGraph(Printer.printDotAutomaton(PM, "PM"), "PM");
			
		DFAutomatonImpl PA = plant.getPlantAutomaton();
		
		System.out.println("Plant ready. States: " + PA.getStates().size() + " Transitions: " + PA.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getRotaryTableControls(1, 0));
		controls.addAll(FlexFactComponents.getExitSlideControls(2, 0));
		controls.addAll(FlexFactComponents.getProcessingMachineControls(1, 1));
		
		DFAutomatonImpl spec = neverLeavePlant(3, 2, controls);
		
		System.out.println("Spec ready.");
		
		Printer.createDotGraph(Printer.printDotAutomaton(spec, "spec"), "spec");
		
		NFAutomatonImpl nPspec = Projection.partialA(spec, PA, new HashSet<String>(), controls);
		
		System.out.println("ND partial spec ready. States: " + nPspec.getStates().size() + " Transitions: " + nPspec.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(nPspec, "nPspec"), "nPspec");
		
		DFAutomatonImpl Pspec = Projection.unify(nPspec, getControls());
		
		System.out.println("Partial spec ready. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
		
//		Pspec.minimize();
//		
//		System.out.println("Partial spec minimized. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
//		
//		Pspec.collapse();
//		
//		System.out.println("Partial spec collapsed. States: " + Pspec.getStates().size() + " Transitions: " + Pspec.getTransitions().size());
		
		Printer.createDotGraph(Printer.printDotAutomaton(Pspec, "Pspec"), "Pspec");
		
	}
	
	//@Test
	public void testSimplePlant() {
		
		FlexFactPlant plant = new FlexFactPlant(3,1);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 0, true, true, 1);
		DFAutomatonImpl RT = FlexFactComponents.RotaryTable(1, 0);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(2, 0, true, true);
		
		plant.install(SF, 0, 0);
		plant.install(RT, 1, 0);
		plant.install(ES, 2, 0);
		
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		Printer.createDotGraph(Printer.printDotAutomaton(RT, "RT"), "RT");
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
			
		DFAutomatonImpl PA = plant.getPlantAutomaton();
		Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		
		Set<String> controls = getControls();
		
		DFAutomatonImpl spec = neverLeavePlant(3, 1, controls);
		
		Printer.createDotGraph(Printer.printDotAutomaton(spec, "spec"), "spec");
		
		NFAutomatonImpl nPspec = Projection.partialA(spec, PA, new HashSet<String>(), controls);
		
		Printer.createDotGraph(Printer.printDotAutomaton(nPspec, "nPspec"), "nPspec");
		
		DFAutomatonImpl Pspec = Projection.unify(nPspec, getControls());
		
		Pspec.minimize();
		Pspec.collapse();
		
		Printer.createDotGraph(Printer.printDotAutomaton(Pspec, "Pspec"), "Pspec");
		
	}
	
	//@Test
	public void testTrivialPlant() {
		
		FlexFactPlant plant = new FlexFactPlant(2,1);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 0, true, true, 1);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(1, 0, true, true);
		
		plant.install(SF, 0, 0);
		plant.install(ES, 1, 0);
		
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
		
		DFAutomatonImpl PA = plant.getPlantAutomaton();
		Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		

		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getExitSlideControls(1, 0));
		
		DFAutomatonImpl spec = neverLeavePlant(2, 1, controls);
		
		Printer.createDotGraph(Printer.printDotAutomaton(spec, "spec"), "spec");
		
		NFAutomatonImpl nPspec = Projection.partialA(spec, PA, new HashSet<String>(), controls);
		
		Printer.createDotGraph(Printer.printDotAutomaton(nPspec, "nPspec"), "nPspec");
		
		DFAutomatonImpl Pspec = Projection.unify(nPspec, getControls());
		
		//Pspec.collapse();
		
		Printer.createDotGraph(Printer.printDotAutomaton(Pspec, "Pspec"), "Pspec");
		
	}
	
	DFAutomatonImpl neverLeavePlant(int w, int h, Set<String> controls) {
		
		StateImpl ok = new StateImpl("ok");
		StateImpl fail = new StateImpl("fail");
		
		DFAutomatonImpl S = new DFAutomatonImpl(ok);
		
		for(String c : controls) {
			S.addTransition(ok, c, ok);
			S.addTransition(fail, c, fail);
		}
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				if(x < w-1) {
					S.addTransition(ok, FlexFactComponents.move(x, y, x+1, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y, x+1, y), fail);
					S.addTransition(ok, FlexFactComponents.move(x+1, y, x, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x+1, y, x, y), fail);
				}
				
				if(y < h-1) {
					S.addTransition(ok, FlexFactComponents.move(x, y, x, y+1), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y, x, y+1), fail);
					S.addTransition(ok, FlexFactComponents.move(x, y+1, x, y), ok);
					S.addTransition(fail, FlexFactComponents.move(x, y+1, x, y), fail);
				}
			}
		}
		
		for(int x = 0; x < w; x++) {
			S.addTransition(ok, FlexFactComponents.move(x, 0, x, -1), fail);
			S.addTransition(ok, FlexFactComponents.move(x, h-1, x, h), fail);
		}
		for(int y = 0; y < h; y++) {
			S.addTransition(ok, FlexFactComponents.move(0, y, -1, y), fail);
			S.addTransition(ok, FlexFactComponents.move(w-1, y, w, y), fail);
		}
		
		S.setFinal(ok, true);
		
		return S;
		
	}
	
	Set<String> getControls() {
		
		Set<String> controls = FlexFactComponents.getStackFeederControls(0, 0, true);
		controls.addAll(FlexFactComponents.getRotaryTableControls(1, 0));
		controls.addAll(FlexFactComponents.getExitSlideControls(2, 0));
		
		return controls;
	}

}
