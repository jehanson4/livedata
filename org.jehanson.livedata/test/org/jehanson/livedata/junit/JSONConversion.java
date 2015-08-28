package org.jehanson.livedata.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;
import org.jehanson.livedata.samples.LDSamples;
import org.jehanson.livedata.serialization.LDFormatException;
import org.jehanson.livedata.serialization.LDParserJSON;
import org.jehanson.livedata.serialization.LDSerializer;
import org.jehanson.livedata.serialization.LDSerializerJSON;
import org.junit.Test;

/**
 * 
 * @author jehanson
 */
public class JSONConversion {

// @Test
// public void writeTest() throws IOException {
// PrintWriter w = new PrintWriter(System.out);
// LDItem item = LDItemAssembly.createDeepMap();
// LDSerializer ser = new LDSerializerJSON(true);
// try {
// ser.serialize(item, w);
// }
// finally {
// w.close();
// }
// }

	@Test
	public void roundTrip() throws IOException, LDFormatException {
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		LDElement dobj1 = LDSamples.deepMap(3);
		LDSerializer ser = new LDSerializerJSON(true);
		try {
			ser.serialize(dobj1, outs);
		}
		finally {
			outs.close();
		}

		String s = outs.toString();
		System.out.println("==================");
		System.out.println(s);
		System.out.println("==================");

		LDParserJSON par = new LDParserJSON();
		InputStream ins = new ByteArrayInputStream(s.getBytes());
		LDElement dobj2 = par.parse(ins);

		if (dobj1.equals(dobj2))
			System.out.println("they're equal");
		else {
			System.out.println("dobj1=" + LDHelpers.prettyString(dobj1));
			System.out.println("dobj2=" + LDHelpers.prettyString(dobj2));
			throw new AssertionError("they're not equal");
		}
	}

	@Test
	public void multilineString() throws IOException, LDFormatException {

		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		LDElement dobj1 = LDSamples.multilineString(3);
		LDSerializer ser = new LDSerializerJSON(true);
		try {
			ser.serialize(dobj1, outs);
		}
		finally {
			outs.close();
		}

		String s = outs.toString();
		System.out.println("==================");
		System.out.println(s);
		System.out.println("==================");
		
		LDParserJSON par = new LDParserJSON();
		InputStream ins = new ByteArrayInputStream(s.getBytes());
		LDElement dobj2 = par.parse(ins);
		System.out.println(dobj2);
		if (dobj1.equals(dobj2))
			System.out.println("they're equal");
		else
			throw new AssertionError("they're not equal");
	}

}
