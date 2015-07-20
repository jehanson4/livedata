package org.jehanson.livedata.elements;

import java.io.PrintWriter;

import org.jehanson.livedata.LDException;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDBoolean extends LDElement {

	// ============================
	// Variables
	// ============================

	private boolean value;

	// ============================
	// Creation
	// ============================

	public LDBoolean() {
		this.value = false;
	}

	public LDBoolean(boolean value) {
		this.value = value;
	}

	public LDBoolean(LDBoolean item) {
		if (item == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
		this.value = item.getValue();
	}
	// ============================
	// Operation
	// ============================

	@Override
	public LDBoolean deepCopy() {
		return new LDBoolean(this);
	}
	
	// @Override
	public void copyFrom(LDElement dobj) throws LDException {
		LDBoolean b = LDHelpers.asBoolean(dobj);
		if (b == null)
			throw new LDException("cannot convert to " + this.getEType().getName() + ": " + dobj);
		this.setValue(b.getValue());
	}

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.BOOLEAN;
	}

	public boolean getValue() {
		return this.value;
	}

	public void setValue(boolean value) {
		if (value != this.value) {
			this.value = value;
			fireValueChanged();
		}
	}

	@Override
	public int hashCode() {
		int hc = 23;
		hc = hc * 37 + (value ? 1 : 0);
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDBoolean))
			return false;
		else
			return this.value == ((LDBoolean) obj).getValue();
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print(value);
	}

}
