package it.unige.automata.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils<T> {
	
	
	public HashSet<T> intersection(Set<T> A, Set<T> B) {
		
		HashSet<T> S = new HashSet<T>();
		
		S.addAll(A);
		S.retainAll(B);
		
		return S;
	}
	
	public HashSet<T> minus(Set<T> A, Set<T> B) {
		
		HashSet<T> S = new HashSet<T>();
		
		S.addAll(A);
		S.removeAll(B);
		
		return S;
	}
	
	
	public T pick(Set<T> A) {
		
		T res = this.sample(A);
		
		if(res != null)
			A.remove(res);
		
		return res;
	}
	
	public T sample(Set<T> A) {
		
		T res = null;
		
		for(T t : A) {
			res = t;
			break;
		}
		
		return res;
	}

}
