package org.jehanson.livedata.junit;

import java.util.Map.Entry;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.samples.LDObjectUsage;
import org.jehanson.livedata.samples.LDSamples;
import org.junit.Test;

/**
 * 
 * @author jehanson
 */
public class LDObjectTests {

	@Test
	public void createDefaultItems() {
		LDSamples.defaultItems();
	}

	@Test
	public void createShallowList() {
		LDSamples.shallowList();
	}

	@Test
	public void createShallowMap() {
		LDSamples.shallowMap();
	}

	@Test
	public void createDeepMap() {
		LDSamples.deepMap(3);
	}
	
	@Test
	public void equalityOfBooleans() {
		LDBoolean t1 = new LDBoolean(true);
		LDBoolean f1 = new LDBoolean(false);
		LDBoolean t2 = new LDBoolean(true);
		LDBoolean f2 = new LDBoolean(false);
		assert (t1.equals(t2) && !t1.equals(f1) && !f1.equals(t1) && f1.equals(f2));
	}

	@Test
	public void equalityOfDeepMaps() {
		LDMap map1 = LDSamples.deepMap(3);
		LDMap map2 = LDSamples.deepMap(3);
		assert map1.equals(map2);
	}

	@Test
	public void prettyPrint() {
		LDObjectUsage.prettyPrintSamples();
	}

	@Test
	public void testDescendants() {
		for (Entry<String, LDElement> e : LDSamples.all().entrySet()) {
			System.out.println("========================");
			LDObjectUsage.printDescendants(e.getKey(), e.getValue());
		}
	}

}
