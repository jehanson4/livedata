package org.jehanson.livedata;

import java.util.List;

import org.jehanson.livedata.elements.LDMap;

/**
 * A live data object.
 * 
 * @author jehanson
 */
public class LiveData extends LDMap {

	private List<LDListener> listeners;

	public LiveData() {
		this(LDMap.defaultKeyOrder);
	}

	public LiveData(KeyOrder keyOrder) {
		super(keyOrder);
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
	public void childReferenceChanged() {
		for (LDListener listener : listeners)
			listener.referenceChanged(this);
	}

	@Override
	public void childStructureChanged() {
		for (LDListener listener : listeners)
			listener.structureChanged(this);
	}

	@Override
	public void childValueChanged() {
		for (LDListener listener : listeners)
			listener.valueChanged(this);
	}

// /** Syntactic sugar */
// public void load(LDParser parser, InputStream stream) throws
// LDFormatException, IOException {
// parser.parse(this, stream);
// }
//
// /** Syntactic sugar */
// public void store(LDSerializer serializer, OutputStream stream) throws
// IOException {
// serializer.serialize(this, stream);
// }

}
