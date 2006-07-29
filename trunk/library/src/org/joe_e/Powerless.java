// Copyright 2005-06 Regents of the University of California.  May be used 
// under the terms of the revised BSD license.  See LICENSING for details.
/** 
 * @author Adrian Mettler 
 */
package org.joe_e;

/**
 * Marker interface for annotating classes that transitively do not contain any
 * mutable state or tokens.  Joe-E requires that classes that implement this 
 * interface meet the obligation that they do not extend Token, all fields must
 * be (1) final and (2) of a declared type that implements this interface in
 * the overlay type system.
 *  
 * This interface contains no members.
 */
public interface Powerless extends Immutable {

}
