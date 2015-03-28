package org.jehanson.livedata;

import java.util.List;

import org.jehanson.livedata.elements.LDMap;

/**
 * 
 * @author jehanson
 */
public class LDRoot extends LDMap {

	private boolean valueDirty;
	private boolean structureDirty;
	private List<LDListener> listeners; 
	
	public LDRoot() {
		this(LDMap.defaultKeyOrder);
	}

	public LDRoot(KeyOrder keyOrder) {
		super(keyOrder);
		valueDirty = false;
		structureDirty = false;
	}

	public void resetDirtyState() {
		valueDirty = false;
		structureDirty = false;
		
	}
	
	@Override
	public void setParent(LDContainer parent) {
		throw new UnsupportedOperationException("Cannot set parent of root");
	}

	public void addLDListener(LDListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener cannot be null");
		listeners.add(listener);
	}
	
	public void removeLDListener(LDListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void childValueChanged() {
		if (valueDirty)
			return;
		
		valueDirty = true;
		notifyListeners(valueDirty, structureDirty);
	}
	
	@Override
	public void childStructureChanged() {
		if (structureDirty)
			return;
		
		structureDirty = true;
		notifyListeners(valueDirty, structureDirty);
	}

	protected void notifyListeners(boolean v, boolean s) {
		for (LDListener listener : listeners) {
			listener.dirtyStateChanged(this, v, s);
		}
	}

	
}
