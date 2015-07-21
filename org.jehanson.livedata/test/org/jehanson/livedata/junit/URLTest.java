package org.jehanson.livedata.junit;

import java.net.URI;
import java.net.URISyntaxException;

import org.jehanson.livedata.elements.LDReference;
import org.junit.Test;

/**
 * 
 * @author jehanson
 */
public class URLTest {

	@Test
	public void testURIs() {
		int errorCount= 0;
		String[] urls =
				{
					"http://www.example.com",
					"foo:/bar",
					"self://",
					"foo", 
					"foo:bar", 
					"in the fourth year of the war", 
					"", 					
					LDReference.DEFAULT_VALUE_STR

				};
		for (String s : urls) {
			try {
				URI uri = new URI(s);
				print("uri=[" + uri + "]");
			}
			catch (URISyntaxException e) {
				errorCount++;
			}
		}
		assert(errorCount == 0);
	}

	private static void print(Object obj) {
		System.out.println(obj);
	}
}
