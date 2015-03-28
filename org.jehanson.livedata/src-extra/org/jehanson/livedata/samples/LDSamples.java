package org.jehanson.livedata.samples;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDDouble;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDLong;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.elements.LDReference;
import org.jehanson.livedata.elements.LDString;
import org.jehanson.livedata.elements.LDVoid;

/**
 * 
 * @author jehanson
 */
public class LDSamples {

	public static Map<String, LDElement> all() {
		Map<String, LDElement> map = new LinkedHashMap<String, LDElement>();
		map.putAll(simpleItems());
		map.put("multilineString2", multilineString(2));
		map.put("multilineString10", multilineString(10));
		map.put("shallowlist", LDSamples.shallowList());
		map.put("shallowmap", shallowMap());
		map.put("deepmap1", deepMap(1));
		map.put("deepmap2", deepMap(2));
		map.put("deepmap3", deepMap(3)); 
		return map;
	}

	public static Map<String, LDElement> defaultItems() {
		Map<String, LDElement> items = new LinkedHashMap<String, LDElement>();
		items.put(LDElement.VType.BOOLEAN.getName(), new LDBoolean());
		items.put(LDElement.VType.DOUBLE.getName(), new LDDouble());
		items.put(LDElement.VType.LONG.getName(), new LDLong());
		items.put(LDElement.VType.LIST.getName(), new LDList());
		items.put(LDElement.VType.MAP.getName(), new LDMap());
		items.put(LDElement.VType.REFERENCE.getName(), new LDReference());
		items.put(LDElement.VType.STRING.getName(), new LDString());
		items.put(LDElement.VType.VOID.getName(), new LDVoid());
		return items;
	}

	public static Map<String, LDElement> simpleItems() {
		Map<String, LDElement> items = new LinkedHashMap<String, LDElement>();
		items.put(LDElement.VType.BOOLEAN.getName(), new LDBoolean(true));
		items.put(LDElement.VType.DOUBLE.getName(), new LDDouble(-99.99));
		items.put(LDElement.VType.LONG.getName(), new LDLong(1001));
		items.put(LDElement.VType.LIST.getName(), new LDList());
		items.put(LDElement.VType.MAP.getName(), new LDMap());
		items.put(LDElement.VType.REFERENCE.getName(),
				new LDReference(URI.create("http://www.example.com")));
		items.put(LDElement.VType.STRING.getName(), new LDString("fee fie foe fum"));
		items.put(LDElement.VType.VOID.getName(), new LDVoid());
		return items;
	}

	public static LDString multilineString(int lineCount) {
		StringWriter w1 = new StringWriter();
		PrintWriter w2 = new PrintWriter(w1);
		for (int i = 0; i<lineCount; i++) {
			w2.println("line " + i);
		}
		w2.close();
		String v = w1.toString();
		return new LDString(v);
	}
	
	public static LDMap shallowMap() {
		LDMap map = new LDMap();
		for (Entry<String, LDElement> e : simpleItems().entrySet()) {
			map.putChild(e.getKey(), e.getValue());
		}
		return map;
	}

	public static LDMap deepMap(int depth) {
		if (!(depth >= 0))
			throw new IllegalArgumentException("depth=" + depth + " -- must be >= 0");
		LDMap map = new LDMap();
		if (depth > 0) {
			for (Entry<String, LDElement> e : simpleItems().entrySet()) {
				map.putChild(e.getKey(), e.getValue());
			}
		}
		if (depth > 1) {
			map.putChild("submap" + (depth - 1), deepMap(depth - 1));
			map.putChild("sublist" + (depth - 1), LDSamples.shallowList());
		}
		return map;
	}

	public static LDList shallowList() {
		LDList list = new LDList();
		for (LDElement item : simpleItems().values()) {
			list.addChild(item);
		}
		return list;
	}

}
