package org.jehanson.livedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDDouble;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDLong;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.elements.LDReference;
import org.jehanson.livedata.elements.LDString;
import org.jehanson.livedata.elements.LDVoid;

/**
 * Enums and helper methods.
 * 
 * @author jehanson
 */
public class LDHelpers {

	public static LDElement createElement(LDElement.VType type) {
		// MAYBE put this functionality inside IType without introducing
		// circular import.
		switch (type) {
		case BOOLEAN:
			return new LDBoolean();
		case DOUBLE:
			return new LDDouble();
		case LIST:
			return new LDList();
		case LONG:
			return new LDLong();
		case MAP:
			return new LDMap();
		case REFERENCE:
			return new LDReference();
		case STRING:
			return new LDString();
		case VOID:
			return new LDVoid();
		default:
			throw new UnsupportedOperationException("No case found for type " + type);
		}
	}

	public static LDBoolean asBoolean(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.BOOLEAN) ? (LDBoolean) item
				: null;
	}

	public static LDContainer asContainer(LDElement item) {
		return (item != null && item.getEType().isContainer()) ? (LDContainer) item
				: null;
	}

	public static LDDouble asDouble(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.DOUBLE) ? (LDDouble) item
				: null;
	}

	public static LDList asList(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.LIST) ? (LDList) item
				: null;
	}

	public static LDLong asLong(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.LONG) ? (LDLong) item
				: null;
	}

	public static LDMap asMap(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.MAP) ? (LDMap) item
				: null;
	}

	public static LDReference asReference(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.REFERENCE) ? (LDReference) item
				: null;
	}

	public static LDString asString(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.STRING) ? (LDString) item
				: null;
	}

	public static LDVoid asVoid(LDElement item) {
		return (item != null && item.getEType() == LDElement.VType.VOID) ? (LDVoid) item
				: null;
	}

	/**
	 * If dobj is a container that has a child with the given key, returns that
	 * child. Otherwise returns null.
	 * 
	 * @param dobj
	 *            the parent
	 * @param key
	 *            the key
	 * @return the child, or null
	 */
	public static LDElement getChild(LDElement dobj, Object key) {
		LDContainer cc = LDHelpers.asContainer(dobj);
		return (cc == null) ? null : cc.getChild(key);
	}

	/**
	 * Follows the given path from the start obj. Returns the element at its
	 * end. If no such element exists, returns null.
	 * 
	 * @param start_obj
	 * @param path_to_follow
	 * @return the element at the end of the path, or null.
	 */
	public static LDElement findElement(LDElement start_obj,
			LDPath path_to_follow) {
		// dobj's type is LDObject, not LDContainer, to handle the case
		// where path is empty.
		if (path_to_follow == null)
			throw new IllegalArgumentException(
					"Argument \"path_to_follow\" cannot be null");
		LDElement tt = start_obj;
		for (Object s : path_to_follow.getSegments()) {
			if (tt == null)
				return null;
			tt = LDHelpers.getChild(tt, s);
		}
		return tt;
	}

	public static LDBoolean findBoolean(LDElement startObj, LDPath path) {
		return asBoolean(findElement(startObj, path));
	}
		
	public static LDContainer findContainer(LDElement startObj, LDPath path) {
		return asContainer(findElement(startObj, path));
	}
		
	public static LDDouble findDouble(LDElement startObj, LDPath path) {
		return asDouble(findElement(startObj, path));
	}
		
	public static LDList findList(LDElement startObj, LDPath path) {
		return asList(findElement(startObj, path));
	}

	public static LDLong findLong(LDElement startObj, LDPath path) {
		return asLong(findElement(startObj, path));
	}
		
	public static LDReference findReference(LDElement startObj, LDPath path) {
		return asReference(findElement(startObj, path));
	}
		
	public static LDString findString(LDElement startObj, LDPath path) {
		return asString(findElement(startObj, path));
	}
	
	public static LDVoid findVoid(LDElement startObj, LDPath path) {
		return asVoid(findElement(startObj, path));
	}
		
	public static String prettyString(LDElement dobj) {
		StringWriter w1 = new StringWriter();
		PrintWriter w2 = new PrintWriter(w1);
		if (dobj == null) {
			w2.print((String)null);
		}
		else {
			dobj.print(w2, 0, true);
		}
		w2.close();
		try {
			w1.close();
		}
		catch (IOException e) {
			// ignore
		}
		return w1.toString();
	}

}
