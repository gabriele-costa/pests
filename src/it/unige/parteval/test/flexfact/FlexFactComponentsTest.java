package it.unige.parteval.test.flexfact;

import java.util.Set;

import org.junit.Test;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.util.Printer;
import it.unige.parteval.Projection;

public class FlexFactComponentsTest {
	
	@Test
	public void testConvoyBelt() {
		
		DFAutomatonImpl CB = FlexFactComponents.ConveyorBelt(1, 1, true);
		Printer.createDotGraph(Printer.printDotAutomaton(CB, "CB"), "CB");
		
	}
	
	@Test
	public void testProcessingMachine() {
			
		DFAutomatonImpl PM = FlexFactComponents.ProcessingMachine(1, 1, true);
		Printer.createDotGraph(Printer.printDotAutomaton(PM, "PM"), "PM");
		
	}
	
	@Test
	public void testRotaryTable() {
			
		DFAutomatonImpl RT = FlexFactComponents.RotaryTable(1, 1);
		Printer.createDotGraph(Printer.printDotAutomaton(RT, "RT"), "RT");
		
	}
	
	@Test
	public void testStackFeeder() {
			
		DFAutomatonImpl SF = FlexFactComponents.StackFeeder(1, 1, true, true, 3);
		Printer.createDotGraph(Printer.printDotAutomaton(SF, "SF"), "SF");
		
	}
	
	@Test
	public void testExitSlide() {
			
		DFAutomatonImpl ES = FlexFactComponents.ExitSlide(1, 1, true, true);
		Printer.createDotGraph(Printer.printDotAutomaton(ES, "ES"), "ES");
		
	}

}
