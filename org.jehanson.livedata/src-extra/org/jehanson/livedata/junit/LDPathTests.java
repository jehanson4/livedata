package org.jehanson.livedata.junit;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.jehanson.livedata.LDPath;
import org.jehanson.livedata.samples.LDPathUsage;
import org.junit.Test;

/**
 * 
 * @author jehanson
 */
public class LDPathTests {

	@Test
	public void printSamples() {
		PrintWriter w = new PrintWriter(System.out);
		Map<String, LDPath> parray = LDPathUsage.samples();
		for (Entry<String, LDPath> e : parray.entrySet()) {
			LDPathUsage.printDetails(e.getKey(), e.getValue(), w);
		}
		w.close();
	}
}
