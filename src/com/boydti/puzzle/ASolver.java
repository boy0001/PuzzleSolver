package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ASolver {
	
	// The 
	public HashMap<Node, Node> all_history = new HashMap<>();
	public HashMap<Node, Node> local_history = new HashMap<>();
	
	public ArrayDeque<Node> queue = new ArrayDeque<Node>();
	
	public final int WIDTH;
	public final int HEIGHT;
	public final int LENGTH;
	
	public final byte[] INITIAL;
	public final byte[] GOAL;
	
	public ASolver(int width, int height, byte[] initial, byte[] goal) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.LENGTH = this.WIDTH * this.HEIGHT;
		
		this.INITIAL = initial;
		this.GOAL = goal;
	}
	
	@Deprecated
	public long getHash(Node state) {
		int length = state.data.length;
		long val = 0;
		for (int i = 0; i < length - 1; i++) {
			val += ((long) state.data[i + 1]) << ((long) (i * 8));
		}
		return val;
	}

	/**
	 * Only works on small arrays
	 * @param value
	 * @param length
	 * @return
	 */
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
	
	public void printState(Node state) {
		System.out.println(Arrays.toString(state.data));
	}
	
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
	
	public void removeHistory(Node state) {
		Node previous = all_history.get(state);
		while(local_history.containsKey(previous)) {
			state = previous;
			previous = all_history.get(state);
			all_history.remove(state);
			
		}
	}
	
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
		while (!state.equals(init)) {
			Node previous = all_history.get(state);
			history.append(prefix + getMove(previous, state));
			prefix = ",";
			state = previous;
		}
		System.out.print(history.reverse());
	}
}
