// Copyright 2006 Regents of the University of California.  May be used 
// under the terms of the revised BSD license.  See LICENSING for details.
/** 
 * @author Adrian Mettler 
 */
package org.joe_e;

import java.util.Arrays;

/**
 * An immutable array of float.
 */
public class FloatArray extends PowerlessArray<Float> {
    static final long serialVersionUID = 5430882612581040334L;   
    
	private final float[] floatArr;

	/**
	 * Construct an immutable float array with a copy of an existing float array as
	 * backing store.
	 * 
	 * @param floatArr the array to make an unmodifiable duplicate of
	 */
	public FloatArray(float... floatArr) {
		// Use back door constructor that sets backing store to null.
        // This lets ConstArray's methods know not to use the backing
        // store for accessing this object.
	    super();
        
        this.floatArr = floatArr.clone();
   	}
    
    /**
     * Return the float located at a specified position
     * 
     * @param pos the position whose float to return
     * 
     * @return the float at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
	public float getFloat(int pos) {
		return floatArr[pos];
	}

    /**
     * Return a mutable copy of the float array
     * 
     * @return a mutable copy of the array
     */
	public float[] toFloatArray() {
		return floatArr.clone();
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
        return floatArr.length;
    }

    
    /**
     * Return a Float containing the value located at a specified position
     * 
     * @param pos the position whose contents to return
     * 
     * @return a new Float containing the float at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
	public Float get(int pos) {
		return floatArr[pos];
	}
	
    /**
     * Test for equality with another object
     * 
     * @return true if the other object is a ConstArray with the same
     * contents as this array
     */
    public boolean equals(Object other) {
        if (other instanceof FloatArray) {
            FloatArray otherFloatArray = (FloatArray) other;
            return Arrays.equals(floatArr, otherFloatArray.floatArr);
        } else if (other instanceof ConstArray) {
            ConstArray otherArray = (ConstArray) other;
            if (otherArray.length() != floatArr.length) {
                return false;
            }
            for (int i = 0; i < floatArr.length; ++i) {
                Object otherElement = otherArray.get(i);
                if (!(otherElement instanceof Float) ||
                    (Float) otherElement != floatArr[i]) {
                    return false;
                }
            }
            return true;
        } else {
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
        // their primitive values, a FloatArray has the same hashCode as a
        // ConstArray<Float>.
        return Arrays.hashCode(floatArr);
    }
    
    /**
     * Return a string representation of the array
     * 
     * @return a string representation of this array
     */    
    public String toString() {
        return Arrays.toString(floatArr);
    }
    
    /**
     * Return a mutable Float array copy of the float array
     * 
     * @return a mutable Float array copy of the array
     */
	public Float[] toArray() {
		Float[] boxedArray = new Float[floatArr.length];
		for (int i = 0; i < floatArr.length; ++i) {
			boxedArray[i] = floatArr[i];
		}
		return boxedArray;
	}  
    
    
    /** 
     * Return a new FloatArray containing a specified additional float
     * 
     * @return a new FloatArray containing a specified additional float
     */
    public FloatArray with(float newFloat) {
        float[] newArr = new float[floatArr.length + 1];
        System.arraycopy(floatArr, 0, newArr, 0, floatArr.length);
        newArr[floatArr.length] = newFloat;
        return new FloatArray(newArr);
    }
    
    /**
     * Return a new FloatArray containing a specified additional Float
     * 
     * @return a new FloatArray containing a specified additional Float
     */
	public FloatArray with(Float newFloat) {
		return with(newFloat.floatValue());
	}
}