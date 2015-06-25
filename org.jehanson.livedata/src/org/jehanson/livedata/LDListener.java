package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	// TODO support for batch changes
	// 1. LDChangeEvent w/ flags for which kinds of change were made.
	// 2. LDChangeAction to permit 1 event for bulk change.
	// 3. rename LDChangeListener

	/**
	 * Indicates that a value has changed somewhere in the tree of the given
	 * data object.
	 * 
	 * @param data The data object in question
	 * @param value The element that changed
	 */
	public void valueChanged(LDObject data, LDElement value);

	/**
	 * Indicates that the tree structure of the given data object has changed in
	 * some way.
	 * 
	 * @param data The data object in question
	 * @param container The container that changed
	 */
	public void structureChanged(LDObject data, LDContainer container);

}
