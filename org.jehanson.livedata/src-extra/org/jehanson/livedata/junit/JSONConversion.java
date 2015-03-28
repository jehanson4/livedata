package org.jehanson.livedata.junit;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;
import org.jehanson.livedata.io.LDFormatException;
import org.jehanson.livedata.io.LDParser;
import org.jehanson.livedata.io.LDParserJSON;
import org.jehanson.livedata.io.LDSerializer;
import org.jehanson.livedata.io.LDSerializerJSON;
import org.jehanson.livedata.samples.LDSamples;
import org.junit.Test;

/**
 * 
 * @author jehanson
 */
public class JSONConversion {

//	@Test
//	public void writeTest() throws IOException {
//		PrintWriter w = new PrintWriter(System.out);
//		LDItem item = LDItemAssembly.createDeepMap();
//		LDSerializer ser = new LDSerializerJSON(true);
//		try {
//			ser.serialize(item, w);
//		}
//		finally {
//			w.close();
//		}
//	}
	
	@Test
	public void roundTrip() throws IOException, LDFormatException {
		StringWriter w = new StringWriter();
		LDElement dobj1 = LDSamples.deepMap(3);
		LDSerializer ser = new LDSerializerJSON(true);
		try {
			ser.serialize(dobj1, w);
		}
		finally {
			w.close();
		}

		String s = w.toString();
		System.out.println("==================");
		System.out.println(s);
		System.out.println("==================");
		StringReader r = new StringReader(s);
		LDParser par = new LDParserJSON();
		LDElement dobj2 = par.parse(r);
		
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
		
		
		StringWriter w = new StringWriter();
		LDElement dobj1 = LDSamples.multilineString(3);
		LDSerializer ser = new LDSerializerJSON(true);
		try {
			ser.serialize(dobj1, w);
		}
		finally {
			w.close();
		}

		String s = w.toString();
		System.out.println("==================");
		System.out.println(s);
		System.out.println("==================");
		StringReader r = new StringReader(s);
		LDParser par = new LDParserJSON();
		LDElement dobj2 = par.parse(r);
		System.out.println(dobj2);
		if (dobj1.equals(dobj2))
			System.out.println("they're equal");
		else
			throw new AssertionError("they're not equal");
	}

}
