package org.jehanson.livedata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LDPath {

	// =============================
	// Variables
	// =============================

	public static final LDPath root = new LDPath(null, null);
	public static final String separator = "/";

	private static final String separatorRegex = "/";

	private final LDPath prefix;
	private final Object segment;

	// ==============================
	// Creation
	// =============================

	private LDPath(LDPath prefix, Object segment) {
		this.prefix = prefix;
		this.segment = segment;
	}

	public static LDPath create(int segment) {
		return root.extend(segment);
	}

	public static LDPath create(String segments) {
		return root.extend(segments);
	}

	public static LDPath create(LDPath prefix, int segment) {
		if (prefix == null)
			prefix = root;
		return prefix.extend(segment);
	}

	public static LDPath create(LDPath prefix, String segments) {
		if (prefix == null)
			prefix = root;
		return prefix.extend(segments);
	}

	// ==============================
	// Operation
	// =============================

	public LDPath removeSegments(int count) {
		LDPath p = this;
		for (int i = 0; i < count; i++) {
			p = p.getPrefix();
			if (p == null)
				throw new IllegalArgumentException("count=" + count
						+ " -- must be <= the total number of segments (="
						+ this.getSegmentCount() + ")");
		}
		return p;
	}

	public LDPath extend(Object obj) {
		if (obj instanceof Number)
			return extend((Number) obj);
		else if (obj instanceof String)
			return extend((String) obj);
		else
			throw new IllegalArgumentException("Bad value for obj: " + obj
					+ " -- exected String or Number");
	}

	public LDPath extend(String segments) {
		if (segments == null)
			throw new IllegalArgumentException("segments cannot be null");
		LDPath p = this;
		String[] segArray = String.valueOf(segments).split(separatorRegex);
		for (String s : segArray)
			p = new LDPath(p, s);
		return p;
	}

	public LDPath extend(Number segment) {
		if (segment == null)
			throw new IllegalArgumentException("segment cannot be null");
		if (segment.intValue() < 0)
			throw new IllegalArgumentException("Bad value: segment=" + segment.intValue()
					+ " -- must be >= 0");
		return new LDPath(this, segment);
	}
	
	public LDPath extend(int segment) {
		return this.extend(Integer.valueOf(segment));
	}

	public boolean isRoot() {
		return (prefix == null);
	}

	public LDPath getPrefix() {
		return prefix;
	}

	public Object getLastSegment() {
		return segment;
	}

	public int getSegmentCount() {
		return (isRoot()) ? 0 : prefix.getSegmentCount() + 1;
	}

	/**
	 * Returns the segments of this path.
	 * @return
	 */
	public List<Object> getSegments() {
		if (isRoot())
			return Collections.emptyList();
		else {
			List<Object> list = new ArrayList<Object>();
			this.buildSegmentList_nonRoot(list);
			return list;
		}
	}

// /**
// * Indicates whether this path is a prefix of, or equal to, the given path.
// *
// * @param path
// * The given path.
// * @return true if the given path is a prefix of this path.
// */
// public boolean isPrefixOf(LDPath path2) {
// }

	@Override
	public String toString() {
		if (isRoot()) {
			return separator;
		}
		else {
			StringBuilder sbuf = new StringBuilder();
			this.buildPathString(sbuf);
			return sbuf.toString();
		}
	}

	@Override
	public int hashCode() {
		int hc = 53;
		if (!isRoot()) {
			hc = 37 * hc + prefix.hashCode();
			hc = 37 * hc + segment.hashCode();
		}
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDPath))
			// MAYBE construct a path from obj and then compare.
			return false;
		else {
			LDPath p1 = (LDPath) obj;
			if (this.isRoot()) // (Not really necessary, but safer.)
				return p1.isRoot();
			else {
				return this.segment.equals(p1.getLastSegment())
						&& this.prefix.equals(p1.getPrefix());
			}
		}
	}

	// =============================
	// Private
	// =============================

	/**
	 * Assumes this path is NOT root.
	 * 
	 * @param list
	 *            The list to append to. Not null.
	 */
	private void buildSegmentList_nonRoot(List<Object> list) {
		// TODO get rid of this test
		if (!prefix.isRoot())
			prefix.buildSegmentList_nonRoot(list);
		list.add(segment);
	}

	/**
	 * Assumes this path is NOT root.
	 * 
	 * @param sbuf
	 *            The buffer to append to. Not null.
	 */
	private void buildPathString(StringBuilder sbuf) {
		if (!prefix.isRoot()) {
			prefix.buildPathString(sbuf);
			sbuf.append(separator);
		}
		sbuf.append(segment);
	}
}