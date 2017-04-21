package it.unige.parteval.test.flexfact;

import org.junit.Test;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.util.Printer;

public class FlexFactPlantTest {
	
	@Test
	public void testSimplePlant() {
		
		FlexFactPlant plant = new FlexFactPlant(3,1);
		
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(0, 0, true, true, 1);
		DFAutomatonImpl CB = FlexFactComponents.ConveyorBelt(1, 0, true);
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(2, 0, true, true);
		
		plant.install(SF, 0, 0);
		plant.install(CB, 1, 0);
		plant.install(ES, 2, 0);
		
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		Printer.createDotGraph(Printer.printDotAutomaton(CB, "CB"), "CB");
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
			
		DFAutomatonImpl PA = plant.getPlantAutomaton();
		Printer.createDotGraph(Printer.printDotAutomaton(PA, "PA"), "PA");
		
	}

}
