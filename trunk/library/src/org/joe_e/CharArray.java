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
 * An immutable array of char.
 */
public class CharArray extends PowerlessArray<Character> {
    static final long serialVersionUID = 20061222;   
    
    private transient char[] charArr;

    /**
     * Construct an immutable char array with a copy of an existing char array as
     * backing store.
     * 
     * @param charArr the array to make an unmodifiable duplicate of
     */
    public CharArray(char... charArr) {
	// Use back door constructor that sets backing store to null.
        // This lets ConstArray's methods know not to use the backing
        // store for accessing this object.
	super();
        
        this.charArr = charArr.clone();
    }
    
    /*
     * Serialization hacks to prevent the contents from being serialized as
     * a mutable array.  This improves efficiency for projects that serialize
     * Joe-E objects using Java's serialization API to avoid treating immutable
     * state as mutable.  These methods can otherwise be ignored.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeInt(charArr.length);
        for (char x : charArr) {
            out.writeChar(x);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, 
    						      ClassNotFoundException {
        in.defaultReadObject();

        int length = in.readInt();
        charArr = (char[]) 
                  java.lang.reflect.Array.newInstance(Character.class, length);
        for (int i = 0; i < length; ++i) {
            charArr[i] = in.readChar();
        }
    }
        
    /**
     * Return the char located at a specified position
     * 
     * @param pos the position whose char to return
     * 
     * @return the char at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
    public char getChar(int pos) {
	return charArr[pos];
    }

    /**
     * Return a mutable copy of the char array
     * 
     * @return a mutable copy of the array
     */
    public char[] toCharArray() {
	return charArr.clone();
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
        return charArr.length;
    }

    
    /**
     * Return a Character containing the value located at a specified position
     * 
     * @param pos the position whose contents to return
     * 
     * @return a new Character containing the char at the specified position
     * 
     * @throws ArrayIndexOutOfBoundsException if the specified position is
     * out of bounds.
     */
    public Character get(int pos) {
	return charArr[pos];
    }
	
    /**
     * Test for equality with another object
     * 
     * @return true if the other object is a ConstArray with the same
     * contents as this array
     */
    public boolean equals(Object other) {
        if (other instanceof CharArray) {
            // Simple case: just compare charArr fields
            CharArray otherCharArray = (CharArray) other;
            return Arrays.equals(charArr, otherCharArray.charArr);
        } else if (other instanceof ConstArray) {
	    // Other array does not have contents in charArr:
	    // check that length matches, and then compare elements one-by-one
            ConstArray otherArray = (ConstArray) other;
            if (otherArray.length() != charArr.length) {
                return false;
            }
            
            for (int i = 0; i < charArr.length; ++i) {
                Object otherElement = otherArray.get(i);
                if (!(otherElement instanceof Character) ||
                    (Character) otherElement != charArr[i]) {
                    return false;
                }
            }
            
            return true;
        } else {
            // Only ConstArrays can be equal to a CharArray
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
        // their primitive values, a CharArray has the same hashCode as a
        // ConstArray<Character> with the same contents.
        return Arrays.hashCode(charArr);
    }
    
    /**
     * Return a string representation of the array
     * 
     * @return a string representation of this array
     */    
    public String toString() {
        return Arrays.toString(charArr);
    }
    
    /**
     * Return a mutable Character array copy of the char array
     * 
     * @return a mutable Character array copy of the array
     */
    public Character[] toArray() {
	Character[] boxedArray = new Character[charArr.length];
	for (int i = 0; i < charArr.length; ++i) {
	    boxedArray[i] = charArr[i];
	}
	return boxedArray;
    }  
    
    
    /** 
     * Return a new CharArray containing a specified additional char
     * 
     * @return a new CharArray containing a specified additional char
     */
    public CharArray with(char newChar) {
        char[] newArr = new char[charArr.length + 1];
        System.arraycopy(charArr, 0, newArr, 0, charArr.length);
        newArr[charArr.length] = newChar;
        return new CharArray(newArr);
    }
    
    /**
     * Return a new CharArray containing a specified additional Character
     * 
     * @return a new CharArray containing a specified additional Character
     */
    public CharArray with(Character newChar) {
	return with(newChar.charValue());
    }
}
