package unittest.fail;

class Enabler8<T> {
	T t;
	Enabler8(T t) {
		this.t = t;
	}
	
	T getT() {
		return t;
	}
}

public class TypeParameterStringConversion8 {
	Enabler8 e = new Enabler8<Object>(new Object());
	Enabler8<String> e2 = e;
	
	{
		assert 12 > 0 : e2.t;
	}
}