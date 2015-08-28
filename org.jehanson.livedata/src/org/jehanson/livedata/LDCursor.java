package org.jehanson.livedata;

public class LDCursor {

	// ===========================
	// Variables
	// ===========================

	private final LDPath path;
	private final LDElement element;

	// ===========================
	// Creation
	// ===========================

	/**
	 * @param path
	 *            Not null.
	 * @param element
	 *            Not null.
	 */
	public LDCursor(LDPath path, LDElement element) {
		super();
		if (path == null)
			throw new IllegalArgumentException("path cannot be null");
		if (element == null)
			throw new IllegalArgumentException("element cannot be null");
		this.path = path;
		this.element = element;
	}

	public LDCursor(LDCursor cursor) {
		if (cursor == null)
			throw new IllegalArgumentException("cursor cannot be null");
		this.path = cursor.getPath();
		this.element = cursor.getElement();
	}

	// ===========================
	// Operation
	// ===========================

	/**
	 * @return Not null.
	 */
	public LDPath getPath() {
		return this.path;
	}

	/**
	 * 
	 * @return Not null.
	 */
	public LDElement getElement() {
		return this.element;
	}

	/**
	 * Returns a cursor positioned at the parent of this cursor.
	 * <p>
	 * NOTE: if all you care about is the parent <i>element</i>, it is more
	 * efficient to do <code>cursor.getElement().getParent()</code>.
	 * 
	 * @return a cursor positioned at the parent of this one, or null.
	 */
	public LDCursor parent() {
		LDContainer parent = this.element.getParent();
		if (parent instanceof LDElement) {
			LDPath pp = this.path.removeSegments(1);
			return new LDCursor(pp, (LDElement) parent);
		}
		return null;
	}

	/**
	 * Returns a cursor positioned at the given child of this cursor.
	 * <p>
	 * NOTE: if all you care about is the child <i>element</i>, it is more
	 * efficient to do
	 * 
	 * <pre>
	 * LDContainer ctnr = LDHelpers.asContainer(cursor.getElement());
	 * return (ctnr == null) ? null : ctnr.getChild(key);
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	public LDCursor child(Object key) {
		LDContainer ctnr = LDHelpers.optContainer(this.element);
		if (ctnr != null) {
			LDElement cobj = ctnr.getChild(key);
			if (cobj != null) {
				LDPath cp = this.path.addSegment(key);
				return new LDCursor(cp, cobj);
			}
		}
		return null;
	}

	/** Follows the given path from this cursor and returns a cursor positioned at the path's end.
	 * <p>
	 * NOTE: if all you want is the <i>element</i> at the end of the path,
	 * it is more efficient to use {@link #descendantElement(path);
	 * 
	 * @param path the path to follow
	 * @return Returns a cursor positioned at the end of the path, or null.
	 * @see #descendantElement(LDPath)
	 */
	public LDCursor descendant(LDPath path) {
		if (path == null)
			throw new IllegalArgumentException("path cannot be null");
		LDElement tt = this.element;
		for (Object s : path.getSegments()) {
			tt = LDHelpers.optChild(tt, s);
			if (tt == null)
				return null;
		}
		return new LDCursor(this.path.addSegments(path), tt);
	}

	/**
	 * Follows the given path from this cursor and returns the element at the
	 * path's end.
	 * 
	 * @param path
	 * @return the element at the end of the path, or null.
	 */
	public LDElement descendantElement(LDPath path) {
		if (path == null)
			throw new IllegalArgumentException("path cannot be null");
		LDElement tt = this.element;
		for (Object s : path.getSegments()) {
			if (tt == null)
				return null;
			tt = LDHelpers.optChild(tt, s);
		}
		return tt;
	}
	
	@Override
	public String toString() {
		return "LDCursor{ path=" + path + " element=" + element + " }";
	}
}