package unittest.fail;

public class CallsDisabledConstructor2 {
	void foo () {
		new java.io.File("foo") { };
	}
}
