package org.jehanson.livedata.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jehanson.livedata.LDElement;
import org.jehanson.livedata.LDObject;
import org.jehanson.livedata.elements.LDBoolean;
import org.jehanson.livedata.elements.LDDouble;
import org.jehanson.livedata.elements.LDList;
import org.jehanson.livedata.elements.LDLong;
import org.jehanson.livedata.elements.LDMap;
import org.jehanson.livedata.elements.LDReference;
import org.jehanson.livedata.elements.LDString;
import org.jehanson.livedata.elements.LDVoid;

/**
 * 
 * @author jehanson
 */
public class LDParserJSON implements LDParser {

	private static final String clsName = LDParserJSON.class.getName();
	private static final Logger logger = Logger.getLogger(clsName);

	// =======================================
	// Inner classes
	// =======================================

	public static class Tokenizer extends FilterReader {

		private int prevCharRead;
		private int pushbackChar;
		private int lineNumber;
		private int charPosition;

		public Tokenizer(Reader arg0) {
			super(arg0);
			prevCharRead = -1;
			pushbackChar = -1;
			lineNumber = 1;
			charPosition = 1;
		}

		@Override
		public int read() throws IOException {
			int c;
			if (pushbackChar >= 0) {
				c = pushbackChar;
				pushbackChar = -1;
			}
			else {
				c = super.read();
				// For counting lines, we support three terminators (two common
				// and one uncommon):
				// 1. "\n"
				// 2. "\r\n" FIXME get the order right!!!!
				// 3. "\r"
				if (prevCharRead == '\r') {
					// case 2 or 3
					this.lineNumber++;
					this.charPosition = (c == '\n') ? 1 : 0; // FIXME WHY?
				}
				else if (c == '\n') {
					// case 1
					this.lineNumber++;
					this.charPosition = 0;
				}
				else {
					this.charPosition++;
				}
				prevCharRead = c;
			}
			return c;
		}

		/**
		 * Reads exactly count chars, returns them as a string. Throws exception
		 * if EOF is encountered early.
		 * 
		 * @param count
		 *            The number of chars to read.
		 * @return The chars that were read.
		 * @throws LDFormatException
		 * @throws IOException
		 */
		public String read(int count) throws LDFormatException, IOException {
			StringBuilder sbuf = new StringBuilder();
			int c;
			for (int i = 0; i < count; i++) {
				c = read();
				if (c < 0)
					throw new LDFormatException("Premature end of file");
				else
					sbuf.append((char) c);
			}
			return sbuf.toString();
		}

		public void unread(int c) throws IOException {
			if (pushbackChar >= 0)
				throw new IOException("pushback buffer is full");
			else
				pushbackChar = c;
		}

		/**
		 * Reads and discards whitespace chars until it encounters a
		 * non-whitespace char or end of file.
		 * 
		 * @return The first non-whitespace char.
		 * @throws IOException
		 * @throws LDFormatException
		 *             if it encountered an EOF
		 */
		public char skipWhitespace() throws IOException, LDFormatException {
			int c = this.read();
			while (Character.isWhitespace(c))
				c = this.read();
			if (c < 0)
				throw new LDFormatException("Premature end of file");
			else
				return (char) c;
		}

		/**
		 * Reads chars and appends them to sbuf until it encounters EOR or one
		 * of the chars in stopChars. If encouters EOF, throws an exception. If
		 * not, returns the stop char. Does not append the stop char to sbuf
		 * orunread it.
		 * 
		 * @param stopChars
		 * @param sbuf
		 * @return the stop char
		 * @throws IOException
		 *             if a read error occurs
		 * @throws LDFormatException
		 *             if it encounters EOF before a stop char.
		 */
		public char readPast(String stopChars, StringBuilder sbuf) throws IOException,
				LDFormatException {
			int c;
			for (;;) {
				c = this.read();
				if (c < 0)
					throw new LDFormatException(errorMessage("Expected one of ["
							+ stopChars + "]"));
				else if (stopChars.indexOf(c) >= 0)
					return (char) c;
				else
					sbuf.append((char) c);
			}
		}

		public LDMap readMap() throws IOException, LDFormatException {
			LDMap map = new LDMap();
			readMapContents(map);
			return map;
		}

		public LDList readList() throws LDFormatException, IOException {
			char c = skipWhitespace();
			if (c != LDParserJSON.LIST_PREFIX)
				throw new LDFormatException(errorMessage("Expected \'"
						+ LDParserJSON.LIST_PREFIX + "\', got \'" + (char) c + "\'"));
			LDList list = new LDList();
			c = skipWhitespace(); // this checks for empty lists
			if (c != LDParserJSON.LIST_SUFFIX) {
				unread(c);
				for (;;) {
					LDElement dobj = readDataObject();
					list.addChild(dobj);
					c = skipWhitespace();
					if (c == LDParserJSON.LIST_SUFFIX)
						break;
					else if (c != LDParserJSON.ELEMENT_SEPARATOR)
						throw new LDFormatException(errorMessage("Expected \'"
								+ LDParserJSON.ELEMENT_SEPARATOR + "\' or \'"
								+ LDParserJSON.LIST_SUFFIX + "\', got \'" + (char) c
								+ "\'"));
				}
			}
			return list;
		}

		protected LDElement readDataObject() throws IOException, LDFormatException {
			char c = skipWhitespace();
			unread(c);
			switch (c) {
			case LDParserJSON.LIST_PREFIX:
				return readList();
			case LDParserJSON.MAP_PREFIX:
				return readMap();
			case LDParserJSON.QUOTE:
				return readStringBasedObj();
			default:
				return readSimpleObj();
			}
		}

		public void readMapContents(LDMap map) throws IOException, LDFormatException {
			char c = skipWhitespace();
			if (c != LDParserJSON.MAP_PREFIX)
				throw new LDFormatException(errorMessage("Expected \'"
						+ LDParserJSON.MAP_PREFIX + "\', got \'" + (char) c + "\'"));
			c = skipWhitespace(); // this checks for empty maps
			if (c != LDParserJSON.MAP_SUFFIX) {
				unread(c);
				for (;;) {
					String key = readJsonString();
					if (!LDMap.isKey(key))
						throw new LDFormatException(errorMessage("Invalid key: " + key));
					c = skipWhitespace();
					if (c != LDParserJSON.KEY_VALUE_SEP_CHAR)
						throw new LDFormatException(errorMessage("Expected \'"
								+ LDParserJSON.KEY_VALUE_SEP_CHAR + "\', got \'"
								+ (char) c + "\'"));
					LDElement dobj = readDataObject();
					map.putChild(key, dobj);
					c = skipWhitespace();
					if (c == LDParserJSON.MAP_SUFFIX)
						break;
					else if (c != LDParserJSON.ELEMENT_SEPARATOR)
						throw new LDFormatException("Expected \'"
								+ LDParserJSON.ELEMENT_SEPARATOR + "\' or \'"
								+ LDParserJSON.MAP_SUFFIX + "\', got \'" + (char) c
								+ "\'");
				}
			}
		}

		public LDElement readStringBasedObj() throws LDFormatException, IOException {
			String s = readJsonString();
			URI refValue = null;
			if (s.length() >= 2 && s.charAt(0) == REFERENCE_PREFIX
					&& s.charAt(s.length() - 1) == REFERENCE_SUFFIX) {
				String s2 = s.substring(1, s.length() - 1);
				try {
					refValue = new URI(s2);
				}
				catch (URISyntaxException e) {
					System.out.println("ERROR in uri str: >[" + s2 + "]<");
				}
			}
			return (refValue == null) ? new LDString(s) : new LDReference(refValue);
		}

		/**
		 * Applies these heuristics:
		 * 
		 * <pre>
		 * "true" or "false"            => LDBoolean
		 * "null"                       => LDVoid
		 * string w/ '.' or 'e' or 'E'  => LDDouble
		 * otherwise                    => LDLong
		 * </pre>
		 * 
		 * @param s
		 * @return
		 */
		public LDElement readSimpleObj() throws LDFormatException, IOException {
			final String mtdName = "readSimpleObj";

			// FIXME do this differently: more like readJsonString. readPast is
			// used ONLY by this guy.

			StringBuilder sbuf = new StringBuilder();
			char c = this.readPast(LDParserJSON.STOP_CHARS, sbuf);
			unread(c);

			String s = sbuf.toString().trim();
			if (logger.isLoggable(Level.FINER))
				logger.logp(Level.FINER, clsName, mtdName, "s=\"" + s + "\"");

			if (s.equalsIgnoreCase(LDParserJSON.TRUE)) {
				return new LDBoolean(true);
			}
			else if (s.equalsIgnoreCase(LDParserJSON.FALSE)) {
				return new LDBoolean(false);
			}
			else if (s.equalsIgnoreCase(LDParserJSON.NULL)) {
				return new LDVoid();
			}
			else if (containsAny(s, LDParserJSON.FLOAT_HINT_CHARS)) {
				try {
					double x = Double.parseDouble(s);
					return new LDDouble(x);
				}
				catch (NumberFormatException err) {
					throw new LDFormatException(
							errorMessage("Error attempting to parse floating-point value from \""
									+ s + "\""), err);
				}
			}
			else {
				try {
					long x = Long.parseLong(s);
					return new LDLong(x);
				}
				catch (NumberFormatException err) {
					throw new LDFormatException(
							errorMessage("Error attempting to parse integer value from \""
									+ s + "\""), err);
				}
			}
		}

		public String readJsonString() throws LDFormatException, IOException {
			// start: read the open quote
			char ch = skipWhitespace();
			if (ch != LDParserJSON.QUOTE) {
				throw new LDFormatException(errorMessage("Expected \'"
						+ LDParserJSON.QUOTE + "\', got \'" + ch + "\'"));
			}
			else {
				StringBuffer sbuf = new StringBuffer();
				boolean inescape = false;
				int c;
				for (;;) {
					c = this.read();
					ch = (char) c;
					if (c < 0)
						throw new LDFormatException(
								errorMessage("Unterminated JSON string"));
					else if (LDParserJSON.CONTROL_CHARS.indexOf(ch) >= 0)
						throw new LDFormatException(errorMessage("Control char (" + ch
								+ ") in JSON string"));
					else if (inescape) {
						inescape = false;
						switch (ch) {
						case 'b':
							sbuf.append('\b');
							break;
						case 'f':
							sbuf.append('\f');
							break;
						case 'n':
							sbuf.append('\n');
							break;
						case 'r':
							sbuf.append('\r');
							break;
						case 't':
							sbuf.append('\t');
							break;
						case 'u':
							String sss = read(4);
							int nnn;
							try {
								nnn = Integer.parseInt(sss, 16);
								ch = (char) nnn;
							}
							catch (Exception e) {
								throw new LDFormatException(
										errorMessage("Expected 4 hex digits, got \""
												+ sss + "\""), e);

							}
							sbuf.append(ch);
							break;
						default:
							throw new LDFormatException(errorMessage("Illegal char \'"
									+ ch + "\' in escape sequence"));
						}
					}
					else {
						switch (ch) {
						case LDParserJSON.ESC:
							inescape = true;
							break;
						case LDParserJSON.QUOTE:
							return sbuf.toString();
						default:
							sbuf.append(ch);
							break;
						}
					}
				}
			}
		}

		public static boolean containsAny(String s, String testChars) {
			for (int i = 0; i < testChars.length(); i++) {
				if (s.indexOf(testChars.charAt(i)) >= 0)
					return true;
			}
			return false;
		}

		private String errorMessage(String msg) {
			if (msg == null)
				return "Near line " + lineNumber + " position " + charPosition;
			else
				return msg + ": near line " + lineNumber + " position " + charPosition;
		}

	} // end class Tokenizer

	// =================================
	// Variables
	// =================================

	static final String STOP_CHARS = ",]}";
	static final String FLOAT_HINT_CHARS = ".eE";
	static final char LIST_PREFIX = '[';
	static final char LIST_SUFFIX = ']';
	static final char MAP_PREFIX = '{';
	static final char MAP_SUFFIX = '}';
	static final char REFERENCE_PREFIX = '<';
	static final char REFERENCE_SUFFIX = '>';
	static final char ELEMENT_SEPARATOR = ',';
	static final char KEY_VALUE_SEP_CHAR = ':';
	static final char QUOTE = '"';
	static final String TRUE = "true";
	static final String FALSE = "false";
	static final String NULL = "null";
	static final char SPACE = ' ';
	static final char ESC = '\\';
	static final String CONTROL_CHARS = "\b\f\n\r\t";

	// must match order of CONTROL_CHARS
	static final String[] CONTROL_ESCAPES = { "\\b", "\\f", "\\n", "\\r", "\\t" };

	// =================================
	// Creation
	// =================================

	public LDParserJSON() {
		super();
	}

	// =================================
	// Operation
	// =================================

	@Override
	public void parse(LDObject obj, InputStream inputStream) throws IOException,
			LDFormatException {
		if (inputStream == null)
			throw new IllegalArgumentException("inputStream cannot be null");
		Reader r = new InputStreamReader(inputStream);
		Tokenizer r2 = new Tokenizer(r);
		try {
			r2.readMapContents(obj);
		}
		finally {
			// Does closing r2 close r as well? Yes it does.
			// Does closing r close inputStream? I don't know!
			r2.close();
		}
	}

	@Override
	public LDElement parse(InputStream inputStream) throws IOException, LDFormatException {
		if (inputStream == null)
			throw new IllegalArgumentException("stream cannot be null");
		Reader r = new InputStreamReader(inputStream);
		Tokenizer r2 = new Tokenizer(r);
		try {
			return r2.readDataObject();
		}
		finally {
			// Does closing r2 close r as well? Yes it does.
			// Does closing r close inputStream? I don't know!
			r2.close();
		}
	}
	
	
}
