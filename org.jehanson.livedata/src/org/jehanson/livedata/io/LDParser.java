package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.InputStream;

import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public interface LDParser {

	public LDElement parse(InputStream stream) throws IOException, LDFormatException;
}
