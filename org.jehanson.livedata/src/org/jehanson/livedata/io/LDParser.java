package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.InputStream;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LiveData;

/**
 * 
 * @author jehanson
 */
public interface LDParser {

	public void parse(LiveData obj, InputStream stream) throws IOException,
			LDFormatException;
	
	public LDElement parse(InputStream stream) throws IOException, LDFormatException;
}
