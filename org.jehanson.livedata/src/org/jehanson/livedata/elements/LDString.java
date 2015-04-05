package org.jehanson.livedata.elements;

import java.io.PrintWriter;

import org.jehanson.livedata.LDException;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDString extends LDElement {

	// ===========================
	// Variables
	// ===========================

	private static final String EMPTY_STRING = "";

	private String value;

	// ===========================
	// Creation
	// ===========================

	public LDString() {
		this.value = EMPTY_STRING;
	}

	public LDString(String v) {
		if (v == null)
			throw new IllegalArgumentException("v cannot be null");
		this.value = v;
	}

	public LDString(LDString item) {
		if (item == null)
			throw new IllegalArgumentException("item cannot be null");
		this.value = item.getValue();
	}

	// ============================
	// Operation
	// ============================

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.STRING;
	}

	@Override
	public LDString deepCopy() {
		return new LDString(this);
	}

	// @Override
	public void copyFrom(LDElement dobj) throws LDException {
		LDString b = LDHelpers.asString(dobj);
		if (b == null)
			throw new LDException("cannot convert to " + this.getEType().getName() + ": " + dobj);
		this.setValue(b.getValue());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");
		if (!value.equals(this.value)) {
			this.value = value;
			notifyValueChange();
		}
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
		else if (!(obj instanceof LDString))
			return false;
		else
			return this.value.equals(((LDString) obj).getValue());
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print('"');
		writer.print(value);
		writer.print('"');
	}

}
