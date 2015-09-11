package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	// Much as I would like to pass LDCursor, I can't w/o
	// having each element know or calculate its own
	// location in the tree

	public void parentChanged(LDElement cursor);

	public void valueChanged(LDElement element);

	public void structureChanged(LDElement element);

}
