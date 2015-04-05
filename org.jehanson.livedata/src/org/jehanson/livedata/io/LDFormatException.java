package org.jehanson.livedata.io;

/**
 * Exception thrown when the creating an LDElement from serialized input data,
 * indicating that the input data has the wrong format.
 * 
 * @author jehanson
 */
public class LDFormatException extends RuntimeException {

	private static final long serialVersionUID = -5691237944833502448L;

	public LDFormatException() {
		super();
	}

	public LDFormatException(String arg0) {
		super(arg0);
	}

	public LDFormatException(Throwable arg0) {
		super(arg0);
	}

	public LDFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
