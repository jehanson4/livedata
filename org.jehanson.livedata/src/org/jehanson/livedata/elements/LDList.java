package org.jehanson.livedata.elements;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jehanson.livedata.LDContainer;
import org.jehanson.livedata.LDCursor;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDPath;

/**
 * 
 * @author jehanson
 */
public class LDList extends LDElement implements LDContainer {

	// ===============================
	// Inner classes
	// ===============================

	private static class LNListIterator implements Iterator<LDCursor> {

		private final LDPath parentPath;
		private final List<LDElement> children;
		private int idx;

		public LNListIterator(LDPath parentPath, LDList parent) {
			this.parentPath = parentPath;
			this.children = parent.children;
			this.idx = 0;
		}

		@Override
		public boolean hasNext() {
			return idx < children.size();
		}

		@Override
		public LDCursor next() {
			if (idx < children.size()) {
				LDCursor cc =
						new LDCursor(parentPath.extend(idx), children.get(idx));
				idx++;
				return cc;
			}
			else
				throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	// ===============================
	// Variables
	// ===============================

	private final List<LDElement> children;

	// ===============================
	// Creation
	// ===============================

	public LDList() {
		this.children = new ArrayList<LDElement>();
	}

	public LDList(LDList item) {
		this();
		if (item == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
		for (int i = 0, n = item.getChildCount(); i < n; i++) {
			LDElement c = item.getChild(i);
			this.addChild(c.deepCopy());
		}

	}

	// ===============================
	// Operation
	// ===============================

	@Override
	public LDList deepCopy() {
		return new LDList(this);
	}

	/**
	 * Indicates whether this list contains all the members of list2.
	 * Does not pay attention to element indices.
	 * @param list2
	 * @return true if this list contains the member of list2, false otherwise.
	 */
	public boolean contains(LDList list2) {
		if (list2 == null)
			throw new IllegalArgumentException("list2 cannot be null");
		for (LDElement elem2: list2.children) {
			if (this.findChild(elem2) == null)
				return false;
		}
		return true;
	}
	
//	@Override
//	public void copyFrom(LDObject dobj) throws LDException {
//		LNList b = LDObject.asList(dobj);
//		if (b == null)
//			throw new LDException("cannot convert to " + this.getEType().getName()
//					+ ": " + dobj);
//		this.removeAllChildren();
//		for (int i = 0; i < b.getChildCount(); i++) {
//			this.addChild(b.getChild(i).deepCopy());
//		}
//	}

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.LIST;
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public LDElement getChild(Object key) {
		try {
			int k;
			if (key instanceof Number)
				k = ((Number) key).intValue();
			else
				k = Integer.parseInt(String.valueOf(key));
			return getChild(k);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public Number findChild(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (int i = 0, n = children.size(); i < n; i++) {
			if (elem == children.get(i))
				return Integer.valueOf(i);
		}
		return null;
	}

	@Override
	public Number findChildEqualTo(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (int i = 0, n = children.size(); i < n; i++) {
			if (elem.equals(children.get(i)))
				return Integer.valueOf(i);
		}
		return null;
	}


	public LDElement getChild(int idx) {
		return children.get(idx);
	}

	public void addChild(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		elem.setParent(this);
		try {
			children.add(elem);
		}
		catch (Exception e) {
			elem.unsetParent();
			throw e;
		}
		notifyStructureChange();
	}

	public void addChild(int idx, LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		elem.setParent(this);
		try {
			children.add(idx, elem);
		}
		catch (Exception e) {
			elem.unsetParent();
			throw e;
		}
		notifyStructureChange();
	}

	public LDElement setChild(int idx, LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		LDElement prevChild = null;
		elem.setParent(this);
		try {
			prevChild = children.set(idx, elem);
			prevChild.unsetParent();
		}
		catch (Exception e) {
			elem.unsetParent();
			throw e;
		}
		notifyStructureChange();
		return prevChild;
	}

	public LDElement removeChild(int idx) {
		LDElement prevChild = children.remove(idx);
		prevChild.unsetParent();
		notifyStructureChange();
		return prevChild;
	}

	public void removeAllChildren() {
		if (children.size() > 0) {
			for (LDElement c : children)
				c.unsetParent();
			children.clear();
			notifyStructureChange();
		}
	}

	@Override
	public Iterator<LDCursor> childIterator(LDPath parentPath) {
		return new LNListIterator(parentPath, this);
	}

	@Override
	public int hashCode() {
		int hc = 23;
		hc = 37 * hc + children.hashCode();
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDList))
			return false;
		else {
			LDList list1 = (LDList) obj;
			if (this.getChildCount() != list1.getChildCount())
				return false;
			else {
				for (int i = 0, n = this.getChildCount(); i < n; i++) {
					if (!this.getChild(i).equals(list1.getChild(i)))
						return false;
				}
				return true;
			}
		}
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print("[");
		Iterator<LDElement> cIter = children.iterator();
		if (cIter.hasNext()) {
			printBreak(writer, level + 1, insertLineBreaks, false);
			cIter.next().print(writer, level + 1, insertLineBreaks);
			while (cIter.hasNext()) {
				writer.print(",");
				printBreak(writer, level + 1, insertLineBreaks, true);
				cIter.next().print(writer, level, insertLineBreaks);
			}
			printBreak(writer, level, insertLineBreaks, false);
		}
		writer.print("]");
	}

	@Override
	public void childReferenceChanged() {
		notifyReferenceChange();
	}

	@Override
	public void childStructureChanged() {
		notifyStructureChange();
	}

	@Override
	public void childValueChanged() {
		notifyValueChange();
	}

}
