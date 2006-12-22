// Copyright 2006 Regents of the University of California.  May be used 
// under the terms of the revised BSD license.  See LICENSING for details.
/** 
 * @author Adrian Mettler 
 */
package org.joe_e;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * An immutable array of long.
 */
public class LongArray extends PowerlessArray<Long> {
    static final long serialVersionUID = 20061222;   
    
    private transient long[] longArr;

    /**
     * Construct an immutable long array with a copy of an existing long array as
     * backing store.
     * 
     * @param longArr the array to make an unmodifiable duplicate of
     */
    public LongArray(long... longArr) {
	// Use back door constructor that sets backing store to null.
        // This lets ConstArray's methods know not to use the backing
        // store for accessing this object.
	super();
        
        this.longArr = longArr.clone();
    }
    
    /*
     * Serialization hacks to prevent the contents from being serialized as
     * a mutable array.  This improves efficiency for projects that serialize
     * Joe-E objects using Java's serialization API to avoid treating immutable
     * state as mutable.  These methods can otherwise be ignored.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeInt(longArr.length);
        for (long x : longArr) {
            out.writeLong(x);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, 
    						      ClassNotFoundException {
        in.defaultReadObject();

        int length = in.readInt();
        longArr = (long[]) 
                  java.lang.reflect.Array.newInstance(Long.class, length);
        for (int i = 0; i < length; ++i) {
            longArr[i] = in.readLong();
        }
    }
        
    /**
     * Return the long located at a specified position
     * 
     * @param pos the position whose long to return
     * 
     * @return the long at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
    public long getLong(int pos) {
	return longArr[pos];
    }

    /**
     * Return a mutable copy of the long array
     * 
     * @return a mutable copy of the array
     */
    public long[] toLongArray() {
	return longArr.clone();
    }

    /*
     *  Methods that must be overriden, as the implementation in ConstArray
     *  would try to use arr, which is null.
     */
	
    /**
     * Return the length of the array
     * 
     * @return the length of the array
     */
    public int length() {
        return longArr.length;
    }

    
    /**
     * Return a Long containing the value located at a specified position
     * 
     * @param pos the position whose contents to return
     * 
     * @return a new Long containing the long at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
    public Long get(int pos) {
	return longArr[pos];
    }
	
    /**
     * Test for equality with another object
     * 
     * @return true if the other object is a ConstArray with the same
     * contents as this array
     */
    public boolean equals(Object other) {
        if (other instanceof LongArray) {
            // Simple case: just compare longArr fields
            LongArray otherLongArray = (LongArray) other;
            return Arrays.equals(longArr, otherLongArray.longArr);
        } else if (other instanceof ConstArray) {
	    // Other array does not have contents in longArr:
	    // check that length matches, and then compare elements one-by-one
            ConstArray otherArray = (ConstArray) other;
            if (otherArray.length() != longArr.length) {
                return false;
            }
            
            for (int i = 0; i < longArr.length; ++i) {
                Object otherElement = otherArray.get(i);
                if (!(otherElement instanceof Long) ||
                    (Long) otherElement != longArr[i]) {
                    return false;
                }
            }
            
            return true;
        } else {
            // Only ConstArrays can be equal to a LongArray
            return false;
        }
    }

    /**
     * Computes a digest of the array for hashing
     * 
     * @return a hash code based on the contents of this array
     */
    public int hashCode() {
        // Because wrappers for primitive types return the same hashCode as 
        // their primitive values, a LongArray has the same hashCode as a
        // ConstArray<Long> with the same contents.
        return Arrays.hashCode(longArr);
    }
    
    /**
     * Return a string representation of the array
     * 
     * @return a string representation of this array
     */    
    public String toString() {
        return Arrays.toString(longArr);
    }
    
    /**
     * Return a mutable Long array copy of the long array
     * 
     * @return a mutable Long array copy of the array
     */
    public Long[] toArray() {
	Long[] boxedArray = new Long[longArr.length];
	for (int i = 0; i < longArr.length; ++i) {
	    boxedArray[i] = longArr[i];
	}
	return boxedArray;
    }  
    
    
    /** 
     * Return a new LongArray containing a specified additional long
     * 
     * @return a new LongArray containing a specified additional long
     */
    public LongArray with(long newLong) {
        long[] newArr = new long[longArr.length + 1];
        System.arraycopy(longArr, 0, newArr, 0, longArr.length);
        newArr[longArr.length] = newLong;
        return new LongArray(newArr);
    }
    
    /**
     * Return a new LongArray containing a specified additional Long
     * 
     * @return a new LongArray containing a specified additional Long
     */
    public LongArray with(Long newLong) {
	return with(newLong.longValue());
    }
}
