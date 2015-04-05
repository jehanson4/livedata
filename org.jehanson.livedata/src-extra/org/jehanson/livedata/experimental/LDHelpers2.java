package org.jehanson.livedata.experimental;

import java.util.List;
import java.util.Map;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDMap;

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

}
