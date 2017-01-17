package it.unige.mu;

public class MuVar implements Assertion {
	
	public String x;

	public MuVar(String x) {
		super();
		this.x = x;
	}
	
	@Override
	public String toString() {
		return x;
	}
	
	@Override
	public int size() {
		return 1;
	}

}
