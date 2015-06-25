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

	// TODO decide whether this kind of incomplete change info is better or
	// worse than nothing.

	// TODO consider whether to replace this with detailed info incl path.
	// downside is a whole lotta notifications nobody asked for.

	// MAYBE include a flag in LDElement to enable detailed notifications
	// and have it percolate down from above. Problem is, again, with the
	// lists.

	/**
	 * Indicates that a value has changed somewhere in the tree of the given
	 * data object.
	 * 
	 * @param data The data object in question
	 * 
	 * MAYBE @param elem The element that changed
	 */
	public void valueChanged(LDObject data);

	/**
	 * Indicates that the tree structure of the given data object has changed in
	 * some way.
	 * 
	 * @param data The data object in question
	 * 
	 * MAYBE @param elem The element that changed
	 */
	public void structureChanged(LDObject data);

	/**
	 * Indicates that a reference has changed somewhere in the tree of the given
	 * data object.
	 * 
	 * @param data The data object in question
	 * 
	 * MAYBE @param elem The element that changed
	 */
	public void referenceChanged(LDObject data);
}
