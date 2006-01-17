package test;

import org.joe_e.Incapable;

public enum BadEnumeration implements Incapable {
	foo(0, 1), bar(0, 2), baz(1, 3);
	
	final int q;
	int z;
	
	BadEnumeration(int q, int z) {
		this.q = q;
		this.z = z;
	}
	
	// Each enumeration value represents a fibonacci sequence. Yay!
	int fib(int n) {
		if (n == 0)
			return q;
		int i = q, j = z;
		while (n > 1) {
			j = i + j;
			i = j - i;
		}
		
		return j;
	}
	
	// Unless we ensure they are incapable, we can 
	// modify the enumeration's "constants"!  Fields of enumeration members
	// (or their contents) can convey authority.  Ouch!
	void muy_eviloso(int q, int z) {
		this.q = q;
		this.z = z;
	}
}	
