package it.unige.mu;

public class MuEquation {
	
	public String x;
	public boolean mu;
	public Assertion f;
	
	public MuEquation(String x, boolean mu, Assertion f) {
		super();
		this.x = x;
		this.mu = mu;
		this.f = f;
	}
	
	@Override
	public String toString() {
		return x + " =" + ((mu) ? "m" : "n") + " " + f.toString();
	}

}
