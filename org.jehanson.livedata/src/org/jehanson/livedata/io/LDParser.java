package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.InputStream;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDObject;

/**
 * 
 * @author jehanson
 */
public interface LDParser {

	public LDElement parse(InputStream stream) throws IOException, LDFormatException;

	public void parse(LDObject obj, InputStream stream) throws IOException,
			LDFormatException;

}
