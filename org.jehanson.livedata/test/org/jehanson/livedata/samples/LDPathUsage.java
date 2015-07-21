package org.jehanson.livedata.samples;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jehanson.livedata.LDPath;

/**
 * 
 * @author jehanson
 */
public class LDPathUsage {

	public static Map<String, LDPath> samples() {
		 LDPath p0 = LDPath.root;
		 LDPath p1 = p0.extend("foo");
		 LDPath p2 = p0.extend(1);
		 LDPath p3 = p0.extend("bar");
		 LDPath p4 = p1.extend(2);
		 LDPath p5 = p2.extend("hix");
		 LDPath p6 = p2.extend(3);
		 LDPath p7 = LDPath.root.extend("alpha").extend("beta").extend("gamma").extend("delta").extend("epsilon").extend("phi");
		 LDPath p8 = LDPath.root.extend("many/segments/in/one/string");
		 
		 Map<String, LDPath> map = new LinkedHashMap<String, LDPath>();
		 map.put("p0",  p0);
		 map.put("p1",  p1);
		 map.put("p2",  p2);
		 map.put("p3",  p3);
		 map.put("p4",  p4);
		 map.put("p5",  p5);
		 map.put("p6",  p6);
		 map.put("p7",  p7);
		 map.put("p8",  p8);
		 return map;
	}

	public static void printDetails(String name, LDPath p, PrintWriter w) {
		w.println(name + ":");
		w.println("    toString:     \"" + p.toString() + "\"");
		w.println("    isRoot:       " + p.isRoot());
		w.println("    prefix:       [" + p.getPrefix() + "]");
		w.println("    lastSegment:  [" + p.getLastSegment() + "]");
		w.println("    segmentCount: " + p.getSegmentCount());
		w.println("    segments:     " + p.getSegments());
	}

}
