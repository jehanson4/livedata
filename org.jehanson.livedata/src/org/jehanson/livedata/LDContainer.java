package org.jehanson.livedata;

import java.util.Iterator;

import org.jehanson.livedata.elements.LDList;

/**
 * Data item that holds other data items.
 * 
 * @author jehanson
 */
public interface LDContainer extends LDListener {

	// MAYBE removeChild(Object key)
	// MAYBE clear()
	// MAYBE addChildren(Iterable<KVPair>> elems);

	public boolean isEmpty();

	/**
	 * Indicates whether the given key is valid for this type of container.
	 * <p>
	 * The key's validity deos not depend on the current set of children, but
	 * only on the key itself.
	 * 
	 * @return true if the key is valid, false if not.
	 */
	public boolean isValidKey(Object key);

	public int getChildCount();

	/**
	 * Returns the key currently associated with the given child. Returns null
	 * if the child is not found.
	 * 
	 * NOTE 1: Uses identity comparison, not equality. For equality, use
	 * {@link #locateChildEqualTo(LDElement)}
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
	public Object locateChild(LDElement elem);

	/**
	 * Returns the key currently associated with a child equal to the given
	 * element. Returns null if no match is found.
	 * 
	 * NOTE 1: If there are multiple such children, returns the first one
	 * encountered.
	 * 
	 * NOTE 2: for list-like containers, the key associated with a given child
	 * may change over time.
	 * 
	 * @param elem
	 *            Not null.
	 * @return The key under which the given element is stored, or null.
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public Object locateChildEqualTo(LDElement elem);

	/**
	 * Returns the child currently associated with the given key. Returns null
	 * if no such child is found.
	 * 
	 * NOTE: for list-like containers, the key associated with a given child may
	 * change over time.
	 * 
	 * @param key
	 *            The key in question
	 * @return the element associated with the key, or null.
	 * @throws IllegalArgumentException
	 *             if the key is not valid
	 * @see {@link #isValidKey(Object)}
	 */
	public LDElement getChild(Object key);

	/**
	 * Puts the given elem into this container such that a subsequent call to
	 * <code>this.getChild(key)</code> would return it.
	 * 
	 * NOTE: for list-like containers, this may have additional side effects;
	 * see in particular {@link LDList#putChild(Object)}.
	 * 
	 * @param key
	 *            The key in question.
	 * @param elem
	 *            The child element to be put into the container.
	 * @return The element that was previously accessible under the given key,
	 *         or null.
	 * @throws IllegalArgumentException
	 *             if the key is not valid
	 */
	public LDElement putChild(Object key, LDElement elem);

	/**
	 * Returns an iterator over this item's immediate children.
	 * 
	 * @param basePath
	 *            Notional path to assign to this iterator. May be any non-null
	 *            value (e.g., {@link LDPath#root}). The paths in the cursors
	 *            returned by this iterator will be extensions of this path.
	 * @return iterator over this elements's immediate children. Not null.
	 * @throws UnsupportedOperationException
	 *             if this optional method is not supported.
	 */
	public Iterator<LDCursor> childIterator(LDPath basePath);

}
