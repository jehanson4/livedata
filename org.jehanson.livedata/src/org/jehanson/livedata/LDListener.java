package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	public void dirtyStateChanged(LDRoot root, boolean valueDirty, boolean structureDirty);
}
