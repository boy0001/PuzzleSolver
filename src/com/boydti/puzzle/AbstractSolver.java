/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

public abstract class AbstractSolver {

    /**
     * The history map for the nodes
     */
	public HashMap<Node, Node> all_history = new HashMap<>();
	
	/**
	 * Dimensions
	 */
	public final int WIDTH;
	public final int HEIGHT;
	public final int LENGTH;
	
	/**
	 * History related...
	 */
	int prunes = 0;
    public ArrayDeque<Node> toRemove = new ArrayDeque<>();
	
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
	
	/**
	 * Get the state if move down (or null)
	 *  - From the perspective of the empty slot
	 */
	public Node getDown(Node state) {
		byte val = (byte) (state.data[0] + WIDTH);
		if (val > LENGTH) {
			return null;
		}
		return getSwap(state, val);
	}
	
	/**
     * Get the state if move up (or null)
     *  - From the perspective of the empty slot
     */
	public Node getUp(Node state) {
		byte val = (byte) (state.data[0] - WIDTH);
		if (val <= 0) {
			return null;
		}
		return getSwap(state, val);
	}
	
	/**
     * An array for the positions of the puzzle
     */
	public byte[] positions;

	public int abs(int a) {
	    return a < 0 ? 0 - a : a;
	}

	/**
	 * Calculate the Manhattan Distance to the goal
	 */
    public int manhattanDistance(Node node) {
        if (positions == null) {
            positions = new byte[GOAL.length];
            for (byte i = 0; i < GOAL.length; i++) {
                positions[GOAL[i]] = i;
            }
        }
        if (node.distance == 0) {
            for (int i = 1, j = 0; i < node.data.length; i++, j++) {
                if (i != node.data[0]) {
                    byte ideal = positions[node.data[i]];
                    node.distance += abs((ideal % WIDTH) - (j % WIDTH));
                    node.distance += abs((ideal / WIDTH) - (j / WIDTH));
                }
            }
        }
        return node.distance;
    }
	
    /**
     * Get the state if move left (or null)
     *  - From the perspective of the empty slot
     */
	public Node getLeft(Node state) {
		byte val = (byte) (state.data[0] - 1);
		if ((val) % WIDTH == 0) {
			return null;
		}
		return getSwap(state, val);
	}
	
	/**
     * Get the state if move right (or null)
     *  - From the perspective of the empty slot
     */
	public Node getRight(Node state) {
		if (state.data[0] % WIDTH == 0) {
			return null;
		}
		return getSwap(state, (byte) (state.data[0] + 1));
	}
	
	/**
     * Get the state if an index is swapped with the empty slot
     */
	public Node getSwap(Node state, byte val) {
		state = new Node(state.data.clone());
		byte i = state.data[0];
		byte tmp = state.data[i];
		state.data[i] = state.data[val];
		state.data[val] = tmp;
		state.data[0] = val;
		return state;
	}
	
	/**
	 * Remove a node from the map (basic garbage collection)
	 */
	public abstract void removeHistory(Node node);
	
	
	/**
	 * Get the String name for a move
	 */
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
	
	/**
	 * Solve the puzzle
	 */
	public abstract void solve();
	
	/**
	 * Print the steps to a state to console
	 */
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
		System.out.println(history.reverse());
	}
}
