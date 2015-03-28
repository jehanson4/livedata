package org.jehanson.livedata;

public class LDCursor {

	// TODO rename. site? point? loc?
	
	// ===========================
	// Variables
	// ===========================

	private final LDPath path;
	private final LDElement element;

	// ===========================
	// Creation
	// ===========================

	public LDCursor(LDPath path, LDElement element) {
		super();
		if (path == null)
			throw new IllegalArgumentException("Argument \"path\" cannot be null");
		this.path = path;
		this.element = element;
	}

	public LDCursor(LDCursor cursor) {
		if (cursor == null)
			throw new IllegalArgumentException("Argument \"cursor\" cannot be null");
		this.path = cursor.getPath();
		this.element = cursor.getElement();
	}

	// ===========================
	// Operation
	// ===========================

	/**
	 * @return the path giving the putative location of this cursor. Not null.
	 */
	public LDPath getPath() {
		return this.path;
	}

	/**
	 * 
	 * @return the element located at this cursor, or null.
	 */
	public LDElement getElement() {
		return this.element;
	}

	public LDCursor child(Object key) {
		// MAYBE: instead of returning null of child is not found, return cursor
		// w/ null element.
		LDContainer ctnr = LDHelpers.asContainer(this.element);
		if (ctnr != null) {
			LDElement cobj = ctnr.getChild(key);
			if (cobj != null) {
				LDPath cp = this.path.extend(key);
				return new LDCursor(cp, cobj);
			}
		}
		return null;
	}

	public LDCursor parent() {
		// MAYBE: instead of returning null if element or parent is null, return
		// cursor w/ null element.
		if (this.element != null) {
			LDContainer parent = this.element.getParent();
			if (parent instanceof LDElement) {
				LDPath pp = this.path.removeSegments(1);
				return new LDCursor(pp, (LDElement) parent);
			}
		}
		return null;
	}
}