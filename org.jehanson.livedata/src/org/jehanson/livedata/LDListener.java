package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	// TODO support for batch changes

	/** experimental */
	public void valueChanged(LiveData data);

	/** experimental */
	public void structureChanged(LiveData data);
	
	/** experimental */
	public void referenceChanged(LiveData data);
}
