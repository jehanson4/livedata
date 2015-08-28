package org.jehanson.livedata.serialization;

import java.io.IOException;
import java.io.OutputStream;

import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public interface LDSerializer {

	public void serialize(LDElement obj, OutputStream stream) throws IOException;
}
