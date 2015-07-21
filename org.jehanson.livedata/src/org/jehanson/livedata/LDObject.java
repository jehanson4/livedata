package org.jehanson.livedata;

import org.jehanson.livedata.elements.LDMap;

/**
 * This is an {@link LDMap} with two special properties:
 * <ol>
 * <li>No parent allowed.</li>
 * <li>LDListeners are notified of all structure and value changes anywhere in
 * the tree rooted at this object.</li>
 * </ol>
 * 
 * @author jehanson
 */
public class LDObject extends LDMap {

	public LDObject() {}

	public LDObject(KeyOrder keyOrder) {
		super(keyOrder);
	}

	public LDObject(LDMap item) {
		super(item);
	}

	@Override
	public void setParent(LDContainer parent) {
		throw new UnsupportedOperationException("LDObject cannot have a parent");
	}

	@Override
	public void replaceParent(LDContainer parent) {
		throw new UnsupportedOperationException("LDObject cannot have a parent");
	}

	@Override
	public void parentChanged(LDElement element) {
		// NOP
	}

	@Override
	public void valueChanged(LDElement element) {
		for (LDListener listener : getListeners()) {
			listener.valueChanged(element);
		}
	}

	@Override
	public void structureChanged(LDElement element) {
		for (LDListener listener : getListeners()) {
			listener.structureChanged(element);
		}
	}

}
