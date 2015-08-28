package org.jehanson.livedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jehanson.livedata.LDElement.EType;
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

	public static LDElement createElement(LDElement.EType type) {
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

	public static EType bestContainerType(Object childKey) {
		return (childKey instanceof Number) ? EType.LIST : EType.MAP;
	}

	public static LDBoolean asBoolean(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.BOOLEAN)
			return (LDBoolean) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.BOOLEAN.getName());
	}

	public static LDContainer asContainer(LDElement item) throws LDTypeException {
		if (item != null && item.getEType().isContainer())
			return (LDContainer) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item)
				+ " to container: " + item);
	}

	public static LDDouble asDouble(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.DOUBLE)
			return (LDDouble) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.DOUBLE.getName());
	}

	public static LDList asList(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.LIST)
			return (LDList) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.LIST.getName());
	}

	public static LDLong asLong(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.LONG)
			return (LDLong) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.LONG.getName());
	}

	public static LDMap asMap(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.MAP)
			return (LDMap) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.MAP.getName());
	}

	public static LDReference asReference(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.REFERENCE)
			return (LDReference) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.REFERENCE.getName());
	}

	public static LDString asString(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.STRING)
			return (LDString) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.STRING.getName());
	}

	public static LDVoid asVoid(LDElement item) throws LDTypeException {
		if (item != null && item.getEType() == LDElement.EType.VOID)
			return (LDVoid) item;
		throw new LDTypeException("cannot cast " + getETypeNameOrNull(item) + " to "
				+ EType.VOID.getName());
	}

	public static String getETypeNameOrNull(LDElement element) {
		return (element == null) ? String.valueOf(element) : element.getEType().getName();
	}

	public static LDBoolean optBoolean(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.BOOLEAN) ? (LDBoolean) item
				: null;
	}

	public static LDContainer optContainer(LDElement item) {
		return (item != null && item.getEType().isContainer()) ? (LDContainer) item
				: null;
	}

	public static LDDouble optDouble(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.DOUBLE) ? (LDDouble) item
				: null;
	}

	public static LDList optList(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.LIST) ? (LDList) item
				: null;
	}

	public static LDLong optLong(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.LONG) ? (LDLong) item
				: null;
	}

	public static LDMap optMap(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.MAP) ? (LDMap) item
				: null;
	}

	public static LDReference optReference(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.REFERENCE) ? (LDReference) item
				: null;
	}

	public static LDString optString(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.STRING) ? (LDString) item
				: null;
	}

	public static LDVoid optVoid(LDElement item) {
		return (item != null && item.getEType() == LDElement.EType.VOID) ? (LDVoid) item
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
	public static LDElement optChild(LDElement dobj, Object key) {
		LDContainer cc = LDHelpers.optContainer(dobj);
		return (cc == null) ? null : cc.getChild(key);
	}

	public static String keyString(Object key) {
		if (key == null)
			return String.valueOf(null);
		if (key instanceof String)
			return "\"" + key + "\"";
		if (key instanceof Number)
			return key.toString();
		throw new IllegalArgumentException("Bad class type " + key.getClass().getName()
				+ " for key " + key);
	}

	public static String prettyString(LDElement dobj) {
		StringWriter w1 = new StringWriter();
		PrintWriter w2 = new PrintWriter(w1);
		if (dobj == null) {
			w2.print((String) null);
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
