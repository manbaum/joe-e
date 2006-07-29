package org.joe_e;

import java.util.Map;
import java.util.HashMap;

/*
 * NOT AN ENDORSED STABLE INTERFACE!
 * 
 * Comments?
 */

public class Honoraries {
    static final int IMPL_IMMUTABLE = 0x0001;
    static final int IMPL_POWERLESS = 0x0002; 
    static final int IMPL_RECORD    = 0x0004;
    static final int IMPL_DATA      = 0x0008;
    static final int IMPL_EQUATABLE = 0x0010;
    
    private static final Map<Class<?>, Integer> entries;

    static {
        entries = new HashMap<Class<?>, Integer>();

        // The following entries will be automatically generated by the taming tool.
        entries.put(String.class, IMPL_IMMUTABLE | IMPL_POWERLESS | IMPL_RECORD | IMPL_DATA);
        entries.put(Integer.class, IMPL_IMMUTABLE | IMPL_POWERLESS | IMPL_RECORD | IMPL_DATA);
        entries.put(Character.class, IMPL_IMMUTABLE | IMPL_POWERLESS | IMPL_RECORD | IMPL_DATA);
        entries.put(Enum.class, IMPL_EQUATABLE);
    }
    
    /**
     * Test whether a class implements an interface in the overlay type system
     * 
     * @argument implementor the class to test for implementation of the interface
     * @argument mi the marker interface
     * 
     * @return true if the specified class implements the specified marker
     *  interface
     */
    public static boolean honorarilyImplements(Class<?> implementor, Class<?> mi) {
        Integer result = entries.get(implementor);
        if (result == null) {
            return false;
        } else {
            if (mi == Immutable.class) {
                return ((result & IMPL_IMMUTABLE) != 0);                
            } else if (mi == Powerless.class) {
                return ((result & IMPL_POWERLESS) != 0);
            } else if (mi == Record.class) {
                return ((result & IMPL_RECORD) != 0);
            } else if (mi == Data.class) {
                return ((result & IMPL_DATA) != 0);
            } else if (mi == Equatable.class) {
                return ((result & IMPL_EQUATABLE) != 0);
            } else {
                throw new IllegalArgumentException("mi not a marker interface");
            }
        }
    }
}
