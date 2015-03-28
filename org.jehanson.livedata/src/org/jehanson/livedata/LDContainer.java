package org.jehanson.livedata;

import java.util.Iterator;

/**
 * Data item that holds other data items.
 * 
 * @author jehanson
 */
public interface LDContainer {

	// maybe...
	// addChild(Object key, LDObject child
	// removeChild(Object key)
	// removeAllChildren()

	public boolean isEmpty();

	public int getChildCount();

	/**
	 * Returns the key currently associated with the given child. Returns null
	 * if the child is not found.
	 * 
	 * NOTE 1: Uses identity comparison, not equality. For equality, use
	 * {@link #findChildEqualTo(LDElement)}
	 * 
	 * NOTE 2: for some container types (notably LNList), the key associated
	 * with a given child may change over time.
	 * 
	 * OPTIONAL METHOD. Implementations that do not support this method will
	 * throw an exception.
	 * 
	 * @param elem
	 *            Not null.
	 * @return The key under which the given element is stored, or null.
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public Object findChild(LDElement elem);

	/**
	 * Returns the key currently associated with a child equal to the given
	 * element. Returns null if no match is found.
	 * 
	 * NOTE: for some container types (notably LNList), the key associated with
	 * a given child may change over time.
	 * 
	 * OPTIONAL METHOD. Implementations that do not support this method will
	 * throw an exception.
	 * 
	 * @param elem
	 *            Not null.
	 * @return The key under which the given element is stored, or null.
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public Object findChildEqualTo(LDElement elem);

	/**
	 * Returns the child currently associated with the given key. Returns null
	 * if no such child is found.
	 * 
	 * NOTE: for some container types (notably LNList), the key associated with
	 * a given child may change over time.
	 * 
	 * OPTIONAL METHOD. Implementations that do not support this method will
	 * throw an exception.
	 * 
	 * @param key
	 * @return
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public LDElement getChild(Object key);

	/**
	 * Returns an iterator over this item's immediate children.
	 * 
	 * OPTIONAL METHOD. Implementations that do not support this method will
	 * throw an exception.
	 * 
	 * @param basePath
	 *            Notional path to assign to this iterator. May be any non-null
	 *            value (e.g., {@link LDPath#root}). The paths in the
	 *            cursors returned by this iterator will be extensions of this
	 *            path.
	 * 
	 * @return iterator over this elements's immediate children. Not null.
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public Iterator<LDCursor> childIterator(LDPath basePath);

	/**
	 * Informs this container that the value of one of its children has changed.
	 * <p>
	 * This method is called by a container's children when they are modified.
	 * Users should not bother to call it.
	 */
	public void childValueChanged();

	/**
	 * Informs this container that the structure of one of its children has changed.
	 * <p>
	 * This method is called by a container's children when they are modified.
	 * Users should not bother to call it.
	 */
	public void childStructureChanged();
	
}
