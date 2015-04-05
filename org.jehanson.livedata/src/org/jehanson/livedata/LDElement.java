package org.jehanson.livedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A piece of information, or a collection of such.
 * 
 * @author jehanson
 */
public abstract class LDElement {
	
	// ===========================================
	// Inner classes
	// ===========================================

	private static final class TreeIterator implements Iterator<LDCursor> {

		private final LinkedList<LDCursor> unvisited;

		public TreeIterator(LDElement root) {
			unvisited = new LinkedList<LDCursor>();
			unvisited.addFirst(new LDCursor(LDPath.root, root));
		}

		@Override
		public boolean hasNext() {
			return !unvisited.isEmpty();
		}

		@Override
		public LDCursor next() {
			LDCursor nn = unvisited.removeFirst();
			LDContainer nc = LDHelpers.asContainer(nn.getElement());
			if (nc != null) {
				Iterator<LDCursor> ni = nc.childIterator(nn.getPath());
				// A little inefficient but easy to code: transfer all the
				// children to unvisited queue. More efficient would be to put
// nn's
				// childIter itself on a queue...
				while (ni.hasNext())
					unvisited.add(ni.next());
			}
			return nn;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	// ===========================================
	// Variables
	// ===========================================

	public static enum EType {
		BOOLEAN {
			@Override
			public String getName() {
				return "boolean";
			}
	
			@Override
			public char getSymbol() {
				return 'b';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return true;
			}
		},
		DOUBLE {
			@Override
			public String getName() {
				return "double";
			}
	
			@Override
			public char getSymbol() {
				return 'd';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return true;
			}
		},
		LIST {
			@Override
			public String getName() {
				return "list";
			}
	
			@Override
			public char getSymbol() {
				return 'L';
			}
	
			@Override
			public boolean isContainer() {
				return true;
			}
	
			@Override
			public boolean isValue() {
				return false;
			}
		},
		LONG {
			@Override
			public String getName() {
				return "long";
			}
	
			@Override
			public char getSymbol() {
				return 'l';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return true;
			}
		},
		MAP {
			@Override
			public String getName() {
				return "map";
			}
	
			@Override
			public char getSymbol() {
				return 'M';
			}
	
			@Override
			public boolean isContainer() {
				return true;
			}
	
			@Override
			public boolean isValue() {
				return false;
			}
		},
		REFERENCE {
			@Override
			public String getName() {
				return "reference";
			}
	
			@Override
			public char getSymbol() {
				return 'r';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return true;
			}
		},
		STRING {
			@Override
			public String getName() {
				return "string";
			}
	
			@Override
			public char getSymbol() {
				return 's';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return true;
			}
		},
		VOID {
			@Override
			public String getName() {
				return "void";
			}
	
			@Override
			public char getSymbol() {
				return 'v';
			}
	
			@Override
			public boolean isContainer() {
				return false;
			}
	
			@Override
			public boolean isValue() {
				return false;
			}
		};
	
		/**
		 * Returns a name for the item that's easier on the human eye than the
		 * bare enum value.
		 * 
		 * @returns the name
		 */
		public abstract String getName();
	
		/**
		 * Returns a 1-character abbreviation. @ the symbol
		 */
		public abstract char getSymbol();
	
		/**
		 * Indicates whether this IType represents a container, as opposed to a
		 * value or null.
		 * 
		 * @return true if this IType represents a container, false if not.
		 */
		public abstract boolean isContainer();
	
		/**
		 * Indicates whether this IType represents a value, as opposed to a
		 * container or null.
		 * 
		 * @return true if this IType represents a value, false if not.
		 */
		public abstract boolean isValue();
	}

	private LDContainer parent;

	// ===========================================
	// Creation
	// ===========================================

	protected LDElement() {
		this.parent = null;
	}

	// ===========================================
	// Operation
	// ===========================================

	public abstract EType getEType();

	public abstract void print(PrintWriter writer, int level, boolean insertLineBreaks);

	public abstract LDElement deepCopy();
	
	// This is problematic b/c it has to delete extant children in container types
	// public abstract void copyFrom(LDObject dobj) throws LDException;
	
	public LDContainer getParent() {
		return parent;
	}

	public void setParent(LDContainer parent) {
		if (this.parent != null)
			throw new IllegalStateException("Parent is already set");
		this.parent = parent;
	}

	public void unsetParent() {
		this.parent = null;
	}
	
	@Override
	public String toString() {
		StringWriter w1 = new StringWriter();
		PrintWriter w2 = new PrintWriter(w1);
		print(w2, 0, false);
		w2.close();
		try {
			w1.close();
		}
		catch (IOException e) {
			// ignore
		}
		return w1.toString();
	}

	// MAYBE move this into LDRoot; or leave it here but add it to ctnr?
	// MAYBE let it apply to subtrees by adding LDPath arg?
	public Iterator<LDCursor> treeIterator() {
		return new TreeIterator(this);
	}

	protected void notifyReferenceChange() {
		if (parent != null)
			parent.childReferenceChanged();
	}

	protected void notifyStructureChange() {
		if (parent != null)
			parent.childStructureChanged();
	}

	protected void notifyValueChange() {
		if (parent != null)
			parent.childValueChanged();
	}

	/**
	 * Convenience method for printing appropriate whitespace before, between,
	 * or after elements of a list or map.
	 * 
	 * @param writer
	 * @param level
	 * @param insertLineBreaks
	 * @param betweenElements
	 */
	protected static void printBreak(PrintWriter writer, int level,
			boolean insertLineBreaks, boolean betweenElements) {
		if (insertLineBreaks) {
			writer.println();
			for (int i = 0; i < level; i++)
				writer.print("    ");
		}
		else if (betweenElements)
			writer.print(" ");
	}
}
