package it.unige.automata.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetUtils<T> {
	
	
	public HashSet<T> intersection(Collection<T> A, Collection<T> B) {
		
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
	
	public boolean contains(Collection<T> S, T elm) {
		for(T t : S)
			if(t.equals(elm))
				return true;
		return false;
	}
	
	public boolean containsAll(Collection<T> S, Collection<T> s) {
		for(T t : s)
			if(!contains(S, t))
				return false;
		return true;
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
