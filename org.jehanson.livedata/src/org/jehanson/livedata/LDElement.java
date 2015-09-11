package org.jehanson.livedata;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
			LDContainer nc = LDHelpers.optContainer(nn.getElement());
			if (nc != null) {
				Iterator<LDCursor> ni = nc.childIterator(nn.getPath());
				// TAGS: INEFFICIENT
				// A little inefficient but easy to code: transfer all the
				// children to unvisited queue. More efficient would be to put
				// nn's childIter itself on a queue...
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
		 * bare enum value. E.g., "list" or "int".
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
	private List<LDListener> listeners;

	// ===========================================
	// Creation
	// ===========================================

	protected LDElement() {
		this.parent = null;
		this.listeners = null;
	}

	// ===========================================
	// Operation
	// ===========================================

	public abstract EType getEType();

	public abstract void print(PrintWriter writer, int level, boolean insertLineBreaks);

	public abstract LDElement deepCopy();

	// This is problematic b/c it has to delete extant children in container
	// types
	// public abstract void copyFrom(LDObject dobj) throws LDException;

	public LDContainer getParent() {
		return parent;
	}

	/**
	 * Sets this element's parent, fires change event, adds new parent (if not
	 * null) as change listener.
	 * 
	 * @param parent new parent. Not null.
	 * @throws IllegalStateException if elem's parent is already set.
	 */
	public void setParent(LDContainer parent) {
		if (parent == null)
			throw new IllegalArgumentException("parent cannot be null");
		if (this.parent != null)
			throw new IllegalStateException("Parent is already set");

		// set & fire, then add listener
		this.parent = parent;
		fireParentChanged();

		addListener(this.parent);
	}

	/**
	 * Removes current parent (if not null) as change listener, sets parent to
	 * null, fires change event.
	 * 
	 */
	public void unsetParent() {
		if (this.parent == null)
			return;

		// remove listener, then set & fire
		if (this.parent != null)
			removeListener(this.parent);

		this.parent = null;
		fireParentChanged();
	}

	/**
	 * Unset and set in a single operation.
	 * 
	 * @param parent new parent. Not null.
	 */
	public void replaceParent(LDContainer parent) {
		if (parent == null)
			throw new IllegalArgumentException("parent cannot be null");
		if (this.parent == parent)
			return;

		// remove, set & fire, add
		if (this.parent != null)
			removeListener(this.parent);

		this.parent = parent;
		fireParentChanged();

		if (this.parent != null)
			addListener(this.parent);
	}

	public void addListener(LDListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener cannot be null");
		if (listeners == null)
			listeners = new ArrayList<LDListener>();
		listeners.add(listener);
	}

	public void removeListener(LDListener listener) {
		if (listeners == null)
			return;
		listeners.remove(listener);
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

	protected void fireParentChanged() {
		if (listeners == null)
			return;
		for (LDListener listener : listeners)
			listener.parentChanged(this);
	}

	protected void fireStructureChanged() {
		if (listeners == null)
			return;
		for (LDListener listener : listeners)
			listener.structureChanged(this);
	}

	protected void fireValueChanged() {
		if (listeners == null)
			return;
		for (LDListener listener : listeners)
			listener.valueChanged(this);
	}
	
	protected List<LDListener> getListeners() {
		if (this.listeners == null)
			return Collections.emptyList();
		else
			return this.listeners;
	}

	protected void propagateStructureChange(LDElement element) {
		if (parent != null)
			parent.structureChanged(element);
	}

	protected void propagateValueChange(LDElement element) {
		if (parent != null)
			parent.valueChanged(element);
	}

	
}
