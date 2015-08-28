package org.jehanson.livedata.experimental;

import java.util.List;
import java.util.Map;

import org.jehanson.livedata.LDContainer;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;
import org.jehanson.livedata.LDPath;
import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDDouble;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDLong;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.elements.LDReference;
import org.jehanson.livedata.elements.LDString;
import org.jehanson.livedata.elements.LDVoid;

/**
 * see LNDataTransform
 * @author jehanson
 */
public class LDHelpers2 extends LDHelpers {

	// TODO refactor as LNList.transformChildren
	public static <T> List<T> transformChildren(LDList dobj, LDElementTransform<T> xform, boolean retainNullValues) {
		return null;
	}
	
	// TODO refactor as LNMap.transformChildren
	public static <T> Map<String, T> transformChildren(LDMap dobj, LDElementTransform<T> xform, boolean retainNullValues) {
		return null;
	}

	// TODO refactor as LNList.filterChildren
	public static List<LDElement> filterChildren(LDList dobj, LDElementPredicate f) {
		return null;
	}
	
	// TODO refactor as LNMap.filterChildren
	public static Map<String, LDElement> filterChildren(LDMap dobj, LDElementPredicate f) {
		return null;
	}

	/**
	 * Follows the given path from the start obj. Returns the element at its
	 * end. If no such element exists, returns null.
	 * 
	 * @param start_obj
	 * @param path_to_follow
	 * @return the element at the end of the path, or null.
	 */
	public static LDElement findElement(LDElement start_obj, LDPath path_to_follow) {
		// dobj's type is LDObject, not LDContainer, to handle the case
		// where path is empty.
		if (path_to_follow == null)
			throw new IllegalArgumentException(
					"Argument \"path_to_follow\" cannot be null");
		LDElement tt = start_obj;
		for (Object s : path_to_follow.getSegments()) {
			if (tt == null)
				return null;
			tt = LDHelpers.optChild(tt, s);
		}
		return tt;
	}

	public static LDBoolean findBoolean(LDElement startObj, LDPath path) {
		return optBoolean(findElement(startObj, path));
	}

	public static LDContainer findContainer(LDElement startObj, LDPath path) {
		return optContainer(findElement(startObj, path));
	}

	public static LDDouble findDouble(LDElement startObj, LDPath path) {
		return optDouble(findElement(startObj, path));
	}

	public static LDList findList(LDElement startObj, LDPath path) {
		return optList(findElement(startObj, path));
	}

	public static LDLong findLong(LDElement startObj, LDPath path) {
		return optLong(findElement(startObj, path));
	}

	public static LDReference findReference(LDElement startObj, LDPath path) {
		return optReference(findElement(startObj, path));
	}

	public static LDString findString(LDElement startObj, LDPath path) {
		return optString(findElement(startObj, path));
	}

	public static LDVoid findVoid(LDElement startObj, LDPath path) {
		return optVoid(findElement(startObj, path));
	}

}
