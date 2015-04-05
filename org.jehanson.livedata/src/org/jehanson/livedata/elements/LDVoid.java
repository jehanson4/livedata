package org.jehanson.livedata.elements;

import java.io.PrintWriter;

import org.jehanson.livedata.LDException;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDVoid extends LDElement {

	public LDVoid() {}

	public LDVoid(LDVoid item) {
		if (item == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
	}

	@Override
	public LDVoid deepCopy() {
		return new LDVoid();
	}

	// @Override
	public void copyFrom(LDElement dobj) throws LDException {
		LDVoid b = LDHelpers.asVoid(dobj);
		if (b == null)
			throw new LDException("cannot convert to " + this.getEType().getName()
					+ ": " + dobj);
	}

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.VOID;
	}

	@Override
	public int hashCode() {
		return 23;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this || (obj instanceof LDVoid));
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.write("null");
	}

}
