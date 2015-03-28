package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.Reader;

import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public interface LDParser {

	public LDElement parse(Reader r) throws IOException, LDFormatException;
}
