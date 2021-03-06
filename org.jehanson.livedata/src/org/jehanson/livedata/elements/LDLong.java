package org.jehanson.livedata.elements;

import java.io.PrintWriter;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDTypeException;
import org.jehanson.livedata.LDHelpers;

/**
 * 
 * @author jehanson
 */
public class LDLong extends LDElement {

	// ========================
	// Variables
	// ========================

	private long value;

	// ========================
	// Creation
	// ========================

	public LDLong() {
		this.value = 0;
	}

	public LDLong(long value) {
		this.value = value;
	}

	public LDLong(LDLong item) {
		this.value = item.getValue();
	}

	// ========================
	// Operation
	// ========================

	@Override
	public LDLong deepCopy() {
		return new LDLong(this);
	}
	
	// @Override
	public void copyFrom(LDElement dobj) throws LDTypeException {
		LDLong b = LDHelpers.optLong(dobj);
		if (b == null)
			throw new LDTypeException("cannot convert to " + this.getEType().getName() + ": " + dobj);
		this.setValue(b.getValue());
	}

	@Override
	public LDElement.EType getEType() {
		return LDElement.EType.LONG;
	}

	public long getValue() {
		return this.value;
	}

	public void setValue(long value) {
		if (value != this.value) {
			this.value = value;
			fireValueChanged();
		}
	}

	public void setValue(Number value) {
		setValue((value == null) ? 0 : value.longValue());
	}
	
	@Override
	public int hashCode() {
		int hc = 23;
		hc = 37 * hc + (int) value;
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (!(obj instanceof LDLong))
			return false;
		else
			return this.value == ((LDLong) obj).getValue();
	}

	@Override
	public void print(PrintWriter writer, int level, boolean insertLineBreaks) {
		writer.print(getEType());
		writer.print('{');
		writer.print(value);
		writer.print('}');
	}

}
