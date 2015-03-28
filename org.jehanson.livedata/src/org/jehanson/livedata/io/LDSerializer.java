package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.Writer;

import org.jehanson.livedata.LDElement;

/**
 * 
 * @author jehanson
 */
public interface LDSerializer {

	public void serialize(LDElement item, Writer w) throws IOException;
}
