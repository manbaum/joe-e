package unittest.pass;
import org.joe_e.testlib.ContainsImmutable;
import org.joe_e.Token;

public class CallsEnabledConstructor3 {
	void foo() {
		new ContainsImmutable(new Token(), "comment", -42) {
		    static final long serialVersionUID = 1;
        };
	}
    
    final public boolean equals(Object other) {
        return false;
    }
}
