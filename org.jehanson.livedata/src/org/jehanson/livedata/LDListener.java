package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	public void valueChanged(LiveData data);
	public void structureChanged(LiveData data);
	public void referenceChanged(LiveData data);
}
