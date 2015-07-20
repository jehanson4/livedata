package org.jehanson.livedata;

/**
 * 
 * @author jehanson
 */
public interface LDListener {

	public void parentChanged(LDElement element);

	public void valueChanged(LDElement element);

	public void structureChanged(LDElement element);

}
