package org.jehanson.livedata.experimental;

import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public interface LDElementTransform<T> {

	// NEED TO CONSTRAIN, or DISTINGUISH, read-only behavior (= no change to input)
	// TOTO: better name...cf gang of 4's 'adapter'
	// operator, adapter...
	
	/**
	 * Applies this transform to the given data object.
	 * @param elem the data object input
	 * @return the transform's output
	 */
	public T transform(LDElement elem);
}
