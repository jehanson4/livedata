package org.jehanson.livedata;

import java.util.ArrayList;
import java.util.List;

import org.jehanson.livedata.elements.LDMap;

/**
 * A live data object.
 * 
 * @author jehanson
 */
public class LDObject extends LDMap {

	private final List<LDListener> listeners;

	public LDObject() {
		this(LDMap.defaultKeyOrder);
	}

	public LDObject(KeyOrder keyOrder) {
		super(keyOrder);
		listeners = new ArrayList<LDListener>();
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
	public void childStructureChanged(LDContainer container) {
		for (LDListener listener : listeners)
			listener.structureChanged(this, container);
	}

	@Override
	public void childValueChanged(LDElement value) {
		for (LDListener listener : listeners)
			listener.valueChanged(this, value);
	}
}
