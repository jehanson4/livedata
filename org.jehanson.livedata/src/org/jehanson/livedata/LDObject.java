package org.jehanson.livedata;

import org.jehanson.livedata.elements.LDMap;

/**
 * 
 * @author jehanson
 */
public class LDObject extends LDMap {

	public LDObject() {}

	/**
	 * @param keyOrder
	 */
	public LDObject(KeyOrder keyOrder) {
		super(keyOrder);
	}

	/**
	 * @param item
	 */
	public LDObject(LDMap item) {
		super(item);
	}

	@Override
	public void parentChanged(LDElement element) {
		for (LDListener listener : getListeners()) {
			listener.parentChanged(element);
		}
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

	@Override
	public void setParent(LDContainer parent) {
		throw new UnsupportedOperationException("LDObject cannot have a parent");
	}

	@Override
	public void replaceParent(LDContainer parent) {
		throw new UnsupportedOperationException("LDObject cannot have a parent");
	}

}
