package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	// TODO support for batch changes

	/** experimental */
	public void valueChanged(LDObject data);

	/** experimental */
	public void structureChanged(LDObject data);
	
	/** experimental */
	public void referenceChanged(LDObject data);
}
