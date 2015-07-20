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
import java.util.regex.Pattern;

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

	/** NOT final: see {@LDMap#setDefaultKeyOrder}. */
	public static KeyOrder defaultKeyOrder = KeyOrder.ANY;

	private static final Pattern whitespaceRegex = Pattern.compile("\\s");

	private final KeyOrder keyOrder;
	private final Map<String, LDElement> children;

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
			LDElement c = v.deepCopy();
			this.putChild(k, c);
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
	@Override
	public boolean isValidKey(Object key) {
		try {
			asMapKey(key);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Converts the given object into a map key if possible.
	 * <p>
	 * A map key is a non-empty string, not equal to "null", and containing no
	 * whitespace chars.
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String asMapKey(Object key) throws IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null");
		String k = key.toString();
		if (k.isEmpty() || k.equals("null") || whitespaceRegex.matcher(k).find())
			throw new IllegalArgumentException("Bad key: \"" + key + "\"");
		return k;
	}

	/**
	 * Modifies or replaces the given string as necessary in order to make it a
	 * map key.
	 * 
	 * @param s
	 *            The input string
	 * @return The map key.
	 */
	public static String fixKey(String s) {
		if (s == null || s.isEmpty() || s.equals("null"))
			return "key";
		return whitespaceRegex.matcher(s).replaceAll("_");
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

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.MAP;
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	public Collection<String> getChildKeys() {
		return Collections.unmodifiableCollection(children.keySet());
	}

	@Override
	public LDElement getChild(Object key) {
		if (!isValidKey(key))
			throw new IllegalArgumentException("Bad key: " + key);
		return children.get(String.valueOf(key));
	}

	public LDElement getChild(String key) {
		if (!isValidKey(key))
			throw new IllegalArgumentException("Bad key: " + key);
		return children.get(key);
	}

	public @Override int getChildCount() {
		return children.size();
	}

	@Override
	public String locateChild(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (Entry<String, LDElement> e : children.entrySet()) {
			if (elem == e.getValue())
				return e.getKey();
		}
		return null;
	}

	@Override
	public String locateChildEqualTo(LDElement elem) {
		if (elem == null)
			throw new IllegalArgumentException("elem cannot be null");
		for (Entry<String, LDElement> e : children.entrySet()) {
			if (elem.equals(e.getValue()))
				return e.getKey();
		}
		return null;
	}

	@Override
	public LDElement putChild(Object key, LDElement elem) {
		String k = asMapKey(key);
		elem.setParent(this);
		LDElement prevChild = children.put(k, elem);
		if (prevChild != null) {
			prevChild.unsetParent();
		}
		fireStructureChanged();
		return prevChild;
	}

	public LDElement removeChild(String key) {
		LDElement prevChild = children.remove(key);
		if (prevChild != null) {
			prevChild.unsetParent();
			fireStructureChanged();
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
		fireStructureChanged();
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

	@Override
	public void parentChanged(LDElement element) {
		// NOP		
	}

	@Override
	public void valueChanged(LDElement element) {
		this.fireValueChanged();
	}

	@Override
	public void structureChanged(LDElement element) {
		this.fireStructureChanged();
	}

}
