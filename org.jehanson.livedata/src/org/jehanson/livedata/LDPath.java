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
		return root.addSegment(segment);
	}

	public static LDPath create(String segments) {
		return root.addSegments(segments);
	}

	public static LDPath create(LDPath prefix, int segment) {
		if (prefix == null)
			prefix = root;
		return prefix.addSegment(segment);
	}

	public static LDPath create(LDPath prefix, String segments) {
		if (prefix == null)
			prefix = root;
		return prefix.addSegments(segments);
	}

	// ==============================
	// Operation
	// =============================

	public static boolean isSegment(Object obj) {
		if (obj instanceof Number)
			return isSegment((Number) obj);
		if (obj instanceof String)
			return isSegment((String) obj);
		return false;
	}

	public static boolean isSegment(Number obj) {
		try {
			return ((Number) obj).intValue() >= 0;
		}
		catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean isSegment(String s) {
		try {
			return !s.contains(separator);
		}
		catch (NullPointerException e) {
			return false;
		}
	}

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

	public LDPath addSegments(LDPath path) {
		if (path == null)
			throw new IllegalArgumentException("path cannot be null");
		return this.addSegments(path.getSegments());
	}

	public LDPath addSegments(Iterable<?> segments) {
		if (segments == null)
			throw new IllegalArgumentException("segments cannot be null");
		LDPath p = this;
		for (Object segment : segments) {
			p = p.addSegment(segment);
		}
		return p;
	}

	public <T> LDPath addSegments(T[] segments) {
		if (segments == null)
			throw new IllegalArgumentException("segments cannot be null");
		LDPath p = this;
		for (Object segment : segments) {
			p = p.addSegment(segment);
		}
		return p;
	}

	/**
	 * Splits the string into segments then adds them in order.
	 * 
	 * @param segments
	 * @return
	 */
	public LDPath addSegments(String segments) {
		if (segments == null)
			throw new IllegalArgumentException("segments cannot be null");
		LDPath p = this;
		String[] segArray = segments.split(separatorRegex);
		for (String s : segArray)
			p = new LDPath(p, s);
		return p;
	}

	public LDPath addSegment(Number segment) {
		if (segment == null)
			throw new IllegalArgumentException("segment cannot be null");
		if (!isSegment(segment))
			throw new IllegalArgumentException("Bad value: " + segment);
		return new LDPath(this, segment);
	}

	public LDPath addSegment(String segment) {
		if (segment == null)
			throw new IllegalArgumentException("segment cannot be null");
		if (!isSegment(segment))
			throw new IllegalArgumentException("Bad value: \"" + segment + "\"");
		return new LDPath(this, segment);
	}

	public LDPath addSegment(Object obj) {
		if (obj instanceof Number)
			return addSegment((Number) obj);
		if (obj instanceof String)
			return addSegment((String) obj);
		throw new IllegalArgumentException("Bad value: obj=" + obj
				+ " -- expected String or Number");
	}

	public LDPath addSegment(int segment) {
		return this.addSegment(Integer.valueOf(segment));
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
	 * 
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

	@Override
	public String toString() {
		StringBuilder sbuf = new StringBuilder();
		this.buildPathString(sbuf);
		return sbuf.toString();
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
		// TAGS: INEFFICIENT
		if (isRoot()) {
			sbuf.append(separator);
		}
		else if (prefix.isRoot()) {
			sbuf.append(separator);
			sbuf.append(segment);
		}
		else {
			prefix.buildPathString(sbuf);
			sbuf.append(separator);
			sbuf.append(segment);
		}
	}
}