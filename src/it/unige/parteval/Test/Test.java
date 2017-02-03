package it.unige.parteval.Test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public abstract class Test {

	public static void main(String[] args) {
		
	    Result result = JUnitCore.runClasses(DroneCasestudyTest.class);
	    for (Failure failure : result.getFailures()) {
	      System.out.println(failure.toString());
	    }
	
		// testProj();
//		testPMC();
//		
//		System.out.println("==============================");
//		System.out.println("==============================");
		
		// testMyProj();
		
		//testOld();
		
	}
	
	public static void testProj() {
		
	}

}
