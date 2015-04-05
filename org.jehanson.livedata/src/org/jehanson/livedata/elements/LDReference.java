package org.jehanson.livedata.elements;

import java.io.PrintWriter;
import java.net.URI;

import org.jehanson.livedata.LDException;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDReference extends LDElement {

	/*
	 * This does not contain the dereferenced target object. I've gone back and
	 * forth on whether it should. The upside is much faster operation. The
	 * downside is that I would have to define, in this lib, the class type for a
	 * dereferenced-obj: i.e., introduce MMObject or something. That would open
	 * the door to bringing in resolvers, caches, and all that kind of stuff,
	 * which I feel doesn't really belong here.
	 */

	// =======================================
	// Variables
	// =======================================

	public static final String DEFAULT_VALUE_STR = "";
	private URI value;

	// =======================================
	// Creation
	// =======================================

	public LDReference() {
		this.value = URI.create(DEFAULT_VALUE_STR);
	}

	public LDReference(URI value) {
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");
		this.value = value;
	}

	public LDReference(LDReference element) {
		if (element == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
		this.value = element.value;
	}

	// =======================================
	// Operation
	// =======================================

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.REFERENCE;
	}

	public URI getValue() {
		return value;
	}

	public void setValue(URI value) {
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");
		if (!this.value.equals(value)) {
			this.value = value;
			notifyReferenceChange();
		}
	}

	@Override
	public LDReference deepCopy() {
		return new LDReference(this);
	}

	// @Override
	public void copyFrom(LDElement dobj) throws LDException {
		LDReference b = LDHelpers.asReference(dobj);
		if (b == null)
			throw new LDException("cannot convert to " + this.getEType().getName() + ": "
					+ dobj);
		this.setValue(b.getValue());
	}

	@Override
	public int hashCode() {
		int hc = 23; // FIXME hashcode
		hc = 37 * hc + value.hashCode();
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDReference))
			return false;
		else
			return this.value.equals(((LDReference) obj).getValue());
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print("<");
		writer.print(value);
		writer.print(">");
	}

}
