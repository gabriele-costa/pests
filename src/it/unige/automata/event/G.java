package it.unige.automata.event;

public class G {
	
	public static Guard tt() {
		return new GTrue();
	}
	
	public static Guard ff() {
		return new GFalse();
	}
	
	public static Guard and(Guard l, Guard r) {
		return new GAnd(l, r);
	}
	
	public static Guard or(Guard l, Guard r) {
		return new GOr(l, r);
	}
	
	public static Guard eq(Expression l, Expression r) {
		return new GEq(l, r);
	}
	
	public static Guard uneq(Expression l, Expression r) {
		return new GUneq(l, r);
	}
	
	public static Expression val(int v) {
		return new Constant(v);
	}
	
	public static Expression var(String x) {
		return new Variable(x);
	}
	
	public static Expression sum(Expression l, Expression r) {
		return new Sum(l, r);
	}
	
	public static Expression diff(Expression l, Expression r) {
		return new Diff(l, r);
	}

}
