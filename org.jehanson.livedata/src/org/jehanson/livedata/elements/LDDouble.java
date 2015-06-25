package org.jehanson.livedata.elements;

import java.io.PrintWriter;

import org.jehanson.livedata.LDException;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDDouble extends LDElement {

	// =======================
	// Variables
	// =======================

	private double value;

	// =======================
	// Creation
	// =======================

	public LDDouble() {
		this.value = 0;
	}

	public LDDouble(double value) {
		this.value = value;
	}

	public LDDouble(LDDouble item) {
		if (item == null)
			throw new IllegalArgumentException("Argument \"item\" cannot be null");
		this.value = item.getValue();
	}

	// =========================
	// Operation
	// =========================

	@Override
	public LDDouble deepCopy() {
		return new LDDouble(this);
	}

	// @Override
	public void copyFrom(LDElement dobj) throws LDException {
		LDDouble b = LDHelpers.asDouble(dobj);
		if (b == null)
			throw new LDException("cannot convert to " + this.getEType().getName() + ": " + dobj);
		this.setValue(b.getValue());
	}

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.DOUBLE;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		if (value != this.value) {
			this.value = value;
			notifyValueChange(this);
		}
	}

	@Override
	public int hashCode() {
		int hc = 23;
		hc = hc * 37 + (int) value;
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDDouble))
			return false;
		else
			return this.value == ((LDDouble) obj).getValue();
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print(value);
	}

}
