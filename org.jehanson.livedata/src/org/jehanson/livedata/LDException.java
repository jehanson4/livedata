package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public class LDException extends Exception {

	private static final long serialVersionUID = 8029599387726086390L;

	public LDException() {
		super();
	}

	public LDException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LDException(String arg0) {
		super(arg0);
	}

	public LDException(Throwable arg0) {
		super(arg0);
	}

}
