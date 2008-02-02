// Copyright 2006 Regents of the University of California.  May be used 
// under the terms of the revised BSD license.  See LICENSING for details.
/** 
 * @author Adrian Mettler 
 */
package org.joe_e.array;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.lang.reflect.Array;

import org.joe_e.array.ByteArray.Builder;


/**
 * An immutable array of <code>double</code>.
 */
public final class DoubleArray extends PowerlessArray<Double> {
    static private final long serialVersionUID = 1L;   
    
    private /* final */ transient double[] doubles;

    private DoubleArray(double... doubles) {
        // Use back door constructor that sets backing store to null.
        // This lets ConstArray's methods know not to use the backing
        // store for accessing this object.
        super(null);
        this.doubles = doubles;
    }
    
    /**
     * Constructs a {@link DoubleArray}.
     * @param doubles each <code>double</code>
     */
    static public DoubleArray array(final double... doubles) {
        return new DoubleArray(doubles.clone());
    }
    
    // java.io.Serializable interface
    
    /*
     * Serialization hacks to prevent the contents from being serialized as a
     * mutable array.  This improves efficiency for projects that serialize
     * Joe-E objects using Java's serialization API by avoiding treatment of
     * immutable state as mutable.  These methods can otherwise be ignored.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeInt(doubles.length);
        for (double c : doubles) {
            out.writeDouble(c);
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, 
    						                      ClassNotFoundException {
        in.defaultReadObject();

        final int length = in.readInt();
        doubles = new double[length];
        for (int i = 0; i < length; ++i) {
            doubles[i] = in.readDouble();
        }
    }
    
    /*
     *  Methods that must be overriden, as the implementation in ConstArray
     *  would try to use arr, which is null.
     */
    
    // java.lang.Object interface
    
    /**
     * Test for equality with another object
     * @return true if the other object is a {@link ConstArray} with the same
     *         contents as this array
     */
    public boolean equals(final Object other) {
        if (other instanceof DoubleArray) {
            // Simple case: just compare doubleArr fields
            return Arrays.equals(doubles, ((DoubleArray)other).doubles);
        } else if (other instanceof ConstArray) {
            // Other array does not have contents in doubleArr:
            // check that length matches, and then compare elements one-by-one
            final ConstArray otherArray = (ConstArray)other;
            if (otherArray.length() != doubles.length) {
                return false;
            }            
            for (int i = 0; i < doubles.length; ++i) {
                final Object otherElement = otherArray.get(i);
                if (!(otherElement instanceof Double) ||
                    ((Double)otherElement).doubleValue() != doubles[i]) {
                    return false;
                }
            }            
            return true;
        } else {
            // Only a ConstArray can be equal to a DoubleArray
            return false;
        }
    }

    /**
     * Computes a digest of the array for hashing.  The hash code is the same
     * as <code>Arrays.hashCode()</code> called on a Java array containing the
     * same elements.
     * @return a hash code based on the contents of this array
     */
    public int hashCode() {
        // Because wrappers for primitive types return the same hashCode as 
        // their primitive values, a DoubleArray has the same hashCode as a
        // ConstArray<Double> with the same contents.
        return Arrays.hashCode(doubles);
    }
    
    /**
     * Return a string representation of the array
     */    
    public String toString() { 
        return Arrays.toString(doubles);
    }
    
    // org.joe_e.ConstArray interface

    /**
     * Gets the length of the array.
     */
    public int length() { 
        return doubles.length;
    }
    
    /**
     * Creates a {@link Double} for a specified <code>double</code>.
     * @param i position of the <code>double</code> to return
     * @throws ArrayIndexOutOfBoundsException <code>i</code> is out of bounds
     */
    public Double get(int i) { 
        return doubles[i]; 
    }
    
    /**
     * Return a mutable copy of the array
     * @param prototype prototype of the array to copy into
     * @return an array containing the contents of this <code>ConstArray</code>
     *     of the same type as <code>prototype</code>
     * @throws ArrayStoreException if an element cannot be stored in the array
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] prototype) {
        final int len = length();
        if (prototype.length < len) {
            final Class t = prototype.getClass().getComponentType(); 
            prototype = (T[])Array.newInstance(t, len);
        }
        
        for (int i = 0; i < len; ++i) {
            prototype[i] = (T) (Double) doubles[i];
        }
        return prototype;
    }
    
    /**
     * Creates a {@link DoubleArray} with an appended {@link Double}.
     * @param newDouble   the {@link Double} to append
     * @throws NullPointerException <code>newDouble</code> is null
     */
    public DoubleArray with(final Double newDouble) {
        return with(newDouble.doubleValue());
    }
           
    /*
     * Convenience (more efficient) methods with double
     */
        
    /**
     * Gets the <code>double</code> at a specified position.
     * @param i position of the <code>double</code> to return
     * @throws ArrayIndexOutOfBoundsException <code>i</code> is out of bounds
     */
    public double getDouble(final int i) { 
        return doubles[i]; 
    }

    /**
     * Creates a mutable copy of the <code>double</code> array
     */
    public double[] toDoubleArray() {
        return doubles.clone(); 
    }
    
    /** 
     * Creates a {@link DoubleArray} with an appended <code>double</code>.
     * @param newDouble   the <code>double</code> to append
     */
    public DoubleArray with(final double newDouble) {
        final double[] newDoubles = new double[doubles.length + 1];
        System.arraycopy(doubles, 0, newDoubles, 0, doubles.length);
        newDoubles[doubles.length] = newDouble;
        return new DoubleArray(newDoubles);
    }

    /**
     * Return a new <code>DoubleArray</code> that contains the same elements
     * as this one excluding the element at a specified index
     * @param i the index of the element to exclude
     * @return  the new array
     */
    public DoubleArray without(final int i) {
        final double[] newArr = new double[doubles.length - 1];
        System.arraycopy(doubles, 0, newArr, 0, i);
        System.arraycopy(doubles, i + 1, newArr, i, newArr.length - i);
        return new DoubleArray(newArr);
    }
    
    /**
     * A {@link DoubleArray} factory.
     */
    static public final class Builder extends PowerlessArray.Builder<Double> {
        private double[] buffer;
        private int size;

        /**
         * Construct an instance with the default internal array length.
         */
        public Builder() {
            this(0);
        }
        
        /**
         * Construct an instance.
         * @param estimate  estimated array length
         */
        public Builder(int estimate) {
            buffer = new double[estimate > 0 ? estimate : 32];
            size = 0;
        }

        // ArrayBuilder<Double> interface
        public void append(Double newDouble) {
            append ((double) newDouble);
        }
        
        public void append(final Double[] newDoubles) {
            append(newDoubles, 0, newDoubles.length);
        }      
        
        public void append(final Double[] newDoubles, 
                          final int off, final int len) {
            int newSize = size + len;
            if (len < 0 || newSize < 0 || off + len > newDoubles.length) {
                throw new IndexOutOfBoundsException();
            }
            if (newSize > buffer.length) {
                int newLength = Math.max(newSize, 2 * buffer.length);
                System.arraycopy(buffer, 0, buffer = new double[newLength], 0,
                                 size);
            }
            
            for (int i = 0; i < len; ++i) {
                buffer[size + i] = newDoubles[off + i];
            }           
            size = newSize;
        }
        
        /**
         * Create a snapshot of the current content.
         */
        public DoubleArray snapshot() {
            final double[] arr;
            if (size == buffer.length) {
                arr = buffer;
            } else {
                arr = new double[size];
                System.arraycopy(buffer, 0, arr, 0, size);
            }
            return new DoubleArray(arr);
        }
        
        /*
         * Convenience (more efficient) methods with double
         */
        public void append(final double newDouble) {
            if (size == buffer.length) {
                System.arraycopy(buffer, 0, buffer = new double[2 * size], 0,
                                 size);
            }
            buffer[size++] = newDouble;
        }

        public void append(final double[] newDoubles) {
            append(newDoubles, 0, newDoubles.length);
        }      
        
        public void append(final double[] newDoubles, final int off, final int len) {
            int newSize = size + len;
            if (len < 0 || newSize < 0 || off + len > newDoubles.length) {
                throw new IndexOutOfBoundsException();
            }
            if (newSize > buffer.length) {
                int newLength = Math.max(newSize, 2 * buffer.length);
                System.arraycopy(buffer, 0, buffer = new double[newLength], 0,
                                 size);
            }
            System.arraycopy(newDoubles, off, buffer, size, len);
            size = newSize;
        }
    }
    
    /* If one only invokes static methods statically, this is sound, since
     * DoubleArray extends PowerlessArray<Double> and thus this method is
     * only required to return something of a type covariant with
     * PowerlessArray.Builder<Double>.  Unfortunately, this is not completely
     * sound because it is possible to invoke static methods on instances, e.g.
     * ConstArray.Builder<String> = (ConstArray (DoubleArray.array())).builder(),
     * allowing for heap pollution without an unchecked cast warning.
     * 
     * The only solution to this would be to completely de-genericize these
     * methods.
     */
    @SuppressWarnings("unchecked")
    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public static Builder builder(final int estimate) {
        return new Builder(estimate);
    }
}