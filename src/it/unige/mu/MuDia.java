package it.unige.mu;

public class MuDia implements Assertion {
	
	public String a;
	public Assertion f;
	
	public MuDia(String a, Assertion f) {
		super();
		this.a = a;
		this.f = f;
	}
	
	@Override
	public String toString() {
		return "<" + a + "> " + f.toString();
	}

}
