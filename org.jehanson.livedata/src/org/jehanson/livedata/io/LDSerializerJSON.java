package org.jehanson.livedata.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jehanson.livedata.LDCursor;
import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDPath;
import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDDouble;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDLong;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.elements.LDReference;
import org.jehanson.livedata.elements.LDString;

/**
 * 
 * @author jehanson
 */
public class LDSerializerJSON implements LDSerializer {

	// ================================
	// Inner classes
	// ================================

	private interface ContentPrinter {
		public abstract void print(LDElement item, PrintWriter writer, int level);
	}

	// ================================
	// Variables
	// ================================

	private final boolean insertLineBreaks;
	private final String indentStr;
	private Map<LDElement.EType, ContentPrinter> contentPrinters;

	// ================================
	// Creation
	// ================================

	public LDSerializerJSON() {
		this(false);
	}

	public LDSerializerJSON(boolean insertLineBreaks) {
		this(insertLineBreaks, 4);
	}

	public LDSerializerJSON(boolean insertLineBreaks, int indentWidth) {
		this.contentPrinters = null;
		this.insertLineBreaks = insertLineBreaks;
		if (this.insertLineBreaks) {
			StringBuilder sbuf = new StringBuilder();
			for (int i = 0; i < indentWidth; i++)
				sbuf.append(LDParserJSON.SPACE);
			this.indentStr = sbuf.toString();
		}
		else
			this.indentStr = ""; // bug evasion
	}

	// ================================
	// Operation
	// ================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jehanson.lodi.resolver.LDEmitter#write(org.jehanson.lodi.item.
	 * LDObject, java.io.Writer)
	 */
	@Override
	public void serialize(LDElement item, Writer w) throws IOException {
		if (w == null)
			throw new IllegalArgumentException("Argument \"w\" cannot be null");

		PrintWriter writer =
				(w instanceof PrintWriter) ? (PrintWriter) w : new PrintWriter(w);
		if (item == null)
			writer.print(LDParserJSON.NULL);
		else {
			loadContentPrinters();
			contentPrinters.get(item.getEType()).print(item, writer, 0);
		}
	}

	// ================================
	// Private
	// ================================

	/**
	 * Adapted, with gratitude, from org.json.JSONWriter.
	 * 
	 * This method is public in order to facilitate unit testing.
	 * 
	 * @param x
	 *            the string to print
	 * @param writer
	 *            the writer to print to
	 */
	private static void printJSONString(Object x, PrintWriter writer) {
		final String s = x.toString();
		String hhhh;
		writer.print(LDParserJSON.QUOTE);
		int ctrlCharIdx = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			ctrlCharIdx = LDParserJSON.CONTROL_CHARS.indexOf(c);
			if (ctrlCharIdx >= 0) {
				writer.print(LDParserJSON.CONTROL_ESCAPES[ctrlCharIdx]);
			}
			else if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
					|| (c >= '\u2000' && c < '\u2100')) {
				// i.e., c requires unicode sequence
				// TODO: validate this against the JSON spec
				writer.print("\\u");
				hhhh = Integer.toHexString(c);
				writer.write("0000", 0, 4 - hhhh.length());
				writer.print(hhhh);
			}
			else {
				writer.write(c);
			}
		}
		writer.print(LDParserJSON.QUOTE);
	}

	private void printLineBreak(boolean afterItem, PrintWriter writer, int level) {
		if (insertLineBreaks) {
			writer.println();
			for (int i = 0; i < level; i++)
				writer.print(indentStr);
		}
		else if (afterItem)
			writer.print(LDParserJSON.SPACE);
	}

	private final void loadContentPrinters() {
		// THREADSAFETY
		if (contentPrinters == null) {
			contentPrinters = new HashMap<LDElement.EType, ContentPrinter>();

			ContentPrinter booleanCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					boolean x = ((LDBoolean) item).getValue();
					writer.print(x ? LDParserJSON.TRUE : LDParserJSON.FALSE);
				}
			};

			ContentPrinter doubleCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					double x = ((LDDouble) item).getValue();
					writer.print(x);
				}
			};

			ContentPrinter listCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					LDList list = (LDList) item;
					writer.print(LDParserJSON.LIST_PREFIX);
					if (!list.isEmpty()) {
						int childLevel = level + 1;
						Iterator<LDCursor> iter = list.childIterator(LDPath.root);
						LDCursor child = iter.next();
						printLineBreak(false, writer, childLevel);
						contentPrinters.get(child.getElement().getEType()).print(
								child.getElement(), writer, childLevel);
						while (iter.hasNext()) {
							child = iter.next();
							writer.print(LDParserJSON.ELEMENT_SEPARATOR);
							printLineBreak(true, writer, childLevel);
							contentPrinters.get(child.getElement().getEType()).print(
									child.getElement(), writer, childLevel);
						}
						printLineBreak(false, writer, level);
					}
					writer.print(LDParserJSON.LIST_SUFFIX);
				}
			};

			ContentPrinter longCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					long x = ((LDLong) item).getValue();
					writer.print(x);
				}
			};

			ContentPrinter mapCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					LDMap map = (LDMap) item;
					writer.print(LDParserJSON.MAP_PREFIX);
					if (!map.isEmpty()) {
						int childLevel = level + 1;
						Iterator<LDCursor> iter = map.childIterator(LDPath.root);
						LDCursor child = iter.next();
						printLineBreak(false, writer, childLevel);
						printJSONString(child.getPath().getLastSegment(), writer);
						writer.print(LDParserJSON.KEY_VALUE_SEP_CHAR);
						writer.print(LDParserJSON.SPACE);
						contentPrinters.get(child.getElement().getEType()).print(
								child.getElement(), writer, childLevel);
						while (iter.hasNext()) {
							child = iter.next();
							writer.print(LDParserJSON.ELEMENT_SEPARATOR);
							printLineBreak(true, writer, childLevel);
							printJSONString(child.getPath().getLastSegment(), writer);
							writer.print(LDParserJSON.KEY_VALUE_SEP_CHAR);
							writer.print(LDParserJSON.SPACE);
							contentPrinters.get(child.getElement().getEType()).print(
									child.getElement(), writer, childLevel);
						}
						printLineBreak(false, writer, level);
					}
					writer.print(LDParserJSON.MAP_SUFFIX);
				}
			};

			ContentPrinter referenceCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					StringBuilder sbuf = new StringBuilder();
					sbuf.append(LDParserJSON.REFERENCE_PREFIX);
					sbuf.append(((LDReference) item).getValue().toString());
					sbuf.append(LDParserJSON.REFERENCE_SUFFIX);
					String x = sbuf.toString();
					printJSONString(x, writer);
				}
			};

			ContentPrinter stringCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					String x = ((LDString) item).getValue();
					printJSONString(x, writer);
				}
			};

			ContentPrinter voidCP = new ContentPrinter() {
				@Override
				public void print(LDElement item, PrintWriter writer, int level) {
					Void x = null;
					writer.print(x);
				}
			};

			contentPrinters.put(LDElement.EType.BOOLEAN, booleanCP);
			contentPrinters.put(LDElement.EType.DOUBLE, doubleCP);
			contentPrinters.put(LDElement.EType.LIST, listCP);
			contentPrinters.put(LDElement.EType.LONG, longCP);
			contentPrinters.put(LDElement.EType.MAP, mapCP);
			contentPrinters.put(LDElement.EType.REFERENCE, referenceCP);
			contentPrinters.put(LDElement.EType.STRING, stringCP);
			contentPrinters.put(LDElement.EType.VOID, voidCP);
		}
	}

}
