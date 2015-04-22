package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractSolver {

    /**
     * The history map for the nodes
     */
	public HashMap<Node, Node> all_history = new HashMap<>();
	
	public final int WIDTH;
	public final int HEIGHT;
	public final int LENGTH;
	
	/**
	 * Initial byte array
	 */
	public final byte[] INITIAL;
	
	/**
	 * The end goal
	 */
	public final byte[] GOAL;
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param initial
	 * @param goal
	 */
	public AbstractSolver(int width, int height, byte[] initial, byte[] goal) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.LENGTH = this.WIDTH * this.HEIGHT;
		this.INITIAL = initial;
		this.GOAL = goal;
	}
	
	// Unused hash function as it didn't work well
	@Deprecated
	public long getHash(Node state) {
		int length = state.data.length;
		long val = 0;
		for (int i = 0; i < length - 1; i++) {
			val += ((long) state.data[i + 1]) << ((long) (i * 8));
		}
		return val;
	}

	// Unused getState (for my unused hash function above)
	@Deprecated
	public Node getState(long value, int length) {
		Node state = new Node(new byte[length]);
		for (int i = length - 1, j = (length - 2) * 8; i > 0; i--, j-=8) {
			state.data[i] = (byte) (value >> j);
			if (state.data[i] == 0) {
				state.data[0] = (byte) i;
			}
			value -= ((long) state.data[i]) << j;
		}
		return state;
	}
	
	/**
	 * Print the byte array representing the node
	 * @param state
	 */
	public void printState(Node state) {
		System.out.println(Arrays.toString(state.data));
	}
	
	/**
	 * Get a Node from a byte array
	 */
	public Node getState(byte[] simpleGrid) {
		Node state = new Node(new byte[simpleGrid.length + 1]);
		for (int i = 0; i < simpleGrid.length; i++) {
			if (simpleGrid[i] == 0) {
				state.data[0] = (byte) (i + 1);
			}
			state.data[i + 1] = simpleGrid[i];
		}
		return state;
	}
	
	public Node getDown(Node state) {
		byte val = (byte) (state.data[0] + WIDTH);
		if (val > LENGTH) {
			return null;
		}
		return getSwap(state, val);
	}
	
	public Node getUp(Node state) {
		byte val = (byte) (state.data[0] - WIDTH);
		if (val <= 0) {
			return null;
		}
		return getSwap(state, val);
	}
	
	
	public byte[] positions;

	public int abs(int a) {
	    return a < 0 ? 0 - a : a;
	}
	
    public int manhattanDistance(Node node) {
        if (positions == null) {
            positions = new byte[GOAL.length];
            for (byte i = 0; i < GOAL.length; i++) {
                positions[GOAL[i]] = i;
            }
        }
        if (node.distance == 0) {
            for (int i = 1, j = 0; i < node.data.length; i++, j++) {
                byte ideal = positions[node.data[i]];
                node.distance += abs((ideal % WIDTH) - (j % WIDTH));
                node.distance += abs((ideal / WIDTH) - (j / WIDTH));
            }
        }
        return node.distance;
    }
	
	public Node getLeft(Node state) {
		byte val = (byte) (state.data[0] - 1);
		if ((val) % WIDTH == 0) {
			return null;
		}
		return getSwap(state, val);
	}
	
	public Node getRight(Node state) {
		if (state.data[0] % WIDTH == 0) {
			return null;
		}
		return getSwap(state, (byte) (state.data[0] + 1));
	}
	
	public Node getSwap(Node state, byte val) {
		state = new Node(state.data.clone());
		byte i = state.data[0];
		byte tmp = state.data[i];
		state.data[i] = state.data[val];
		state.data[val] = tmp;
		state.data[0] = val;
		return state;
	}
	
	int prunes = 0;
	
	public ArrayDeque<Node> toRemove = new ArrayDeque<>();
	
	public abstract void removeHistory(Node node);
	
	public String getMove(Node first, Node second) {
		if (first.equals(getUp(second))) {
			return "NWOD";
		}
		if (first.equals(getLeft(second))) {
			return "THGIR";
		}
		if (first.equals(getDown(second))) {
			return "PU";
		}
		if (first.equals(getRight(second))) {
			return "TFEL";
		}
		return "MAGIC";
	}
	
	public abstract void solve();
	
	public void displayPath(Node state) {
		Node init = getState(INITIAL);
		StringBuffer history = new StringBuffer();
		String prefix = "";
		int moves = 0;
		while (!state.equals(init)) {
		    moves++;
			Node previous = all_history.get(state);
			history.append(prefix + getMove(previous, state));
			prefix = ";";
			state = previous;
		}
//		System.out.println("[" + history.reverse() +"]");
		System.out.println("TOTOAL MOVES: " + moves);
		System.out.println("NODES EXPLORED: " + (all_history.size() + prunes));
		System.out.println("CACHE SIZE: " + all_history.size());
		System.out.println("PRUNES PERFORMED: " + prunes);
		System.out.println("MANHATTAN DISTANCE: " + manhattanDistance(init));
	}
}
