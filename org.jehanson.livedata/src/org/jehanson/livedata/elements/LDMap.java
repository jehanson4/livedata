package org.jehanson.livedata.elements;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jehanson.livedata.LDContainer;
import org.jehanson.livedata.LDCursor;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDPath;

/**
 * 
 * @author jehanson
 */
public class LDMap extends LDElement implements LDContainer {

	// ==============================
	// Inner classes
	// ==============================

	public enum KeyOrder {
		ALPHABETICAL, ANY, INSERTION
	}

	private static class LNMapIterator implements Iterator<LDCursor> {

		private final LDPath parentPath;
		private final Iterator<Map.Entry<String, LDElement>> base;

		public LNMapIterator(LDPath parentPath, LDMap parent) {
			this.parentPath = parentPath;
			base = parent.children.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return base.hasNext();
		}

		@Override
		public LDCursor next() {
			Map.Entry<String, LDElement> e = base.next();
			return new LDCursor(parentPath.extend(e.getKey()), e.getValue());
		}

		/**
		 * Throws UnsupportedOperationException
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	// ==============================
	// Variables
	// ==============================

	public static KeyOrder defaultKeyOrder = KeyOrder.ANY;

	private final KeyOrder keyOrder;
	private final Map<String, LDElement> children;

	// private static final Pattern whitespacePattern = Pattern.compile("\\s");

	// ==============================
	// Creation
	// ==============================

	public LDMap() {
		this(defaultKeyOrder);
	}

	public LDMap(KeyOrder keyOrder) {
		this.keyOrder = keyOrder;
		this.children = createInnerMap(keyOrder);
	}

	public LDMap(LDMap item) {
		if (item == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
		this.keyOrder = KeyOrder.ANY;
		this.children = createInnerMap(keyOrder);
		for (String k : item.getChildKeys()) {
			LDElement v = item.getChild(k);
			this.putChild(k, v.deepCopy());
		}
	}

	// ==============================
	// Operation
	// ==============================

	public static KeyOrder getDefaultKeyOrder() {
		return defaultKeyOrder;
	}

	public static void setDefaultKeyOrder(KeyOrder k) {
		if (k == null)
			throw new IllegalArgumentException("k cannot be null");
		defaultKeyOrder = k;
	}

	/**
	 * Indicates whether the given string is a valid key in an LNMap.
	 * <p>
	 * A key is valid if it is non-empty, contains no whitespace, and is not
	 * equal to the string "null".
	 * 
	 * @param s
	 *            the string in question
	 * @return true if s is a valid key, false if invalid or null.
	 */
	public static boolean isKey(String s) {
		if (s == null || s.isEmpty() || s.equals("null"))
			return false;

		// SOMEDAY: compare performance w/ regex approach
		// Matcher matcher = whitespacePattern.matcher(s);
		// if (matcher.find())
		// return false;

		for (int i = 0, n = s.length(); i < n; i++) {
			if (Character.isWhitespace(s.charAt(i)))
				return false;
		}

		return true;
	}

	public static String fixKey(String s) {
		if (s == null || s.isEmpty() || s.equals("null"))
			return "key";

		// SOMEDAY: compare performance w/ regex approach
		// Matcher matcher = whitespacePattern.matcher(s);
		// return matcher.replaceAll("_");

		StringBuilder sbuf = new StringBuilder();
		char c;
		for (int i = 0, n = s.length(); i < n; i++) {
			c = s.charAt(i);
			sbuf.append(Character.isWhitespace(c) ? '_' : c);
		}
		return sbuf.toString();
	}

	public String findUnusedKey(String hint) {
		String base = fixKey(hint);
		int count = 0;
		String test = base;
		while (children.containsKey(test)) {
			test = base + "-" + count++;
		}
		return test;
	}

	@Override
	public LDMap deepCopy() {
		return new LDMap(this);
	}

	/**
	 * Indicates whether this map contains all key/value pairs of map2. Values
	 * are matched by equality, not identity.
	 * 
	 * @param map2
	 * @return true if this map contains the members of map2, false otherwise.
	 */
	public boolean contains(LDMap map2) {
		if (map2 == null)
			throw new IllegalArgumentException("map2 cannot be null");
		for (Entry<String, LDElement> e : map2.children.entrySet()) {
			LDElement elem1 = this.children.get(e.getKey());
			if (!e.getValue().equals(elem1))
				return false;
		}
		return true;
	}

// @Override
// public void copyFrom(LDObject dobj) throws LDException {
// throw new UnsupportedOperationException("not implemented");
// }

	@Override
	public LDElement.VType getEType() {
		return LDElement.VType.MAP;
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

// /**
// * Convenience method to check whether this map does NOT already have a
// * child w/ given key and content type different from the desired type.
// *
// * @param key
// * The key
// * @param type
// * The desired type
// * @return true if there is no child with the given key and a type different
// * from the desired type.
// */
// public boolean isChildPermitted(String key, EType type) {
// LDObject c = children.get(key);
// return (c == null || c.getIType() == type);
// }

	public Collection<String> getChildKeys() {
		return Collections.unmodifiableCollection(children.keySet());
	}

	@Override
	public LDElement getChild(Object key) {
		String skey = (key == null) ? null : key.toString();
		return getChild(skey);
	}

	public LDElement getChild(String key) {
		return children.get(key);
	}

	public @Override int getChildCount() {
		return children.size();
	}

	@Override
	public String findChild(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (Entry<String, LDElement> e : children.entrySet()) {
			if (elem == e.getValue())
				return e.getKey();
		}
		return null;
	}

	@Override
	public String findChildEqualTo(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (Entry<String, LDElement> e : children.entrySet()) {
			if (elem.equals(e.getValue()))
				return e.getKey();
		}
		return null;
	}

	public LDElement putChild(String key, LDElement elem) {
		if (!isKey(key))
			throw new IllegalArgumentException("Bad key: " + key);
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		elem.setParent(this);
		LDElement prevChild = children.put(key, elem);
		if (prevChild != null)
			prevChild.unsetParent();
		notifyStructureChange();
		return prevChild;
	}

	public LDElement removeChild(String key) {
		LDElement prevChild = children.remove(key);
		if (prevChild != null) {
			prevChild.unsetParent();
			notifyStructureChange();
		}
		return prevChild;
	}

	public void removeAllChildren() {
		if (children.isEmpty())
			return;

		List<String> keys = new ArrayList<String>();
		keys.addAll(children.keySet());
		for (String key : keys) {
			LDElement prevChild = children.remove(key);
			if (prevChild != null)
				prevChild.unsetParent();
		}
		notifyStructureChange();
	}

	@Override
	public Iterator<LDCursor> childIterator(LDPath parentPath) {
		return new LNMapIterator(parentPath, this);
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
		else if (!(obj instanceof LDMap))
			return false;
		else {
			LDMap map1 = (LDMap) obj;
			if (this.getChildCount() != map1.getChildCount())
				return false;
			else {
				for (Map.Entry<String, LDElement> entry0 : this.children.entrySet()) {
					if (!entry0.getValue().equals(map1.getChild(entry0.getKey())))
						return false;
				}
				return true;
			}
		}
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print("{");
		Iterator<Map.Entry<String, LDElement>> cIter = children.entrySet().iterator();
		Map.Entry<String, LDElement> c;
		if (cIter.hasNext()) {
			printBreak(writer, level + 1, insertLineBreaks, false);
			c = cIter.next();
			writer.print(String.valueOf(c.getKey()));
			writer.print(" = ");
			c.getValue().print(writer, level + 1, insertLineBreaks);
			while (cIter.hasNext()) {
				writer.print(",");
				printBreak(writer, level + 1, insertLineBreaks, true);
				c = cIter.next();
				writer.print(String.valueOf(c.getKey()));
				writer.print(" = ");
				c.getValue().print(writer, level + 1, insertLineBreaks);
			}
			printBreak(writer, level, insertLineBreaks, false);
		}
		writer.print("}");
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

	// ===================================
	// Private
	// ===================================

	private static Map<String, LDElement> createInnerMap(KeyOrder keyOrder) {
		switch (keyOrder) {
		case ALPHABETICAL:
			return new TreeMap<String, LDElement>();
		case INSERTION:
			return new LinkedHashMap<String, LDElement>();
		default:
			return new HashMap<String, LDElement>();
		}
	}

}
