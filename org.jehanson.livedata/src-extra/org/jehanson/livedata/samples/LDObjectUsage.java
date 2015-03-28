package org.jehanson.livedata.samples;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jehanson.livedata.LDCursor;
import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public class LDObjectUsage {

	public static void prettyPrintSamples() {
		PrintWriter w = new PrintWriter(System.out);
		for (Entry<String, LDElement> e : LDSamples.simpleItems().entrySet()) {
			w.println(e.getKey() + " = " + e.getValue());
			e.getValue().print(w, 0, true);
			w.println();
			w.println("=====================");
		}

		LDElement item = LDSamples.deepMap(3);
		w.println("deep-map-3 = " + item);
		item.print(w, 0, true);
		w.println();

		w.close();		
	}
	
	public static void printSampleTrees() {
		
	}
	
	public static void printDescendants(String name, LDElement item) {
		String indent = "    ";
		System.out.println(name);
		System.out.println(item);
		Iterator<LDCursor> iter = item.treeIterator();
		while (iter.hasNext()) {
			LDCursor p = iter.next();
			System.out.println(indent + p.getPath());
			System.out.println(indent + indent + p.getElement());
		}
	}

}
