package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GBFSolver extends ASolver {

	public GBFSolver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}
	
	public PriorityQueue<Node> queue = new PriorityQueue<Node>(0, new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return 0;
        }
    });
	
	@Override
	public void solve() {
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		queue.add(state);
		while (true) {
			state = queue.remove();
			local_history.remove(state);
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			boolean empty = true;
			if (up != null) {
				if (!all_history.containsKey(up)) {
					empty = false;
					queue.add(up);
					local_history.put(up, state);
					all_history.put(up, state);
					if (up.equals(goal)) {
						state = up;
						return;
					}
				}
			}
			if (left != null) {
				if (!all_history.containsKey(left)) {
					empty = false;
					queue.add(left);
					local_history.put(left, state);
					all_history.put(left, state);
					if (left.equals(goal)) {
						state = left;
						return;
					}
				}
			}
			if (down != null) {
				if (!all_history.containsKey(down)) {
					empty = false;
					queue.add(down);
					local_history.put(down, state);
					all_history.put(down, state);
					if (down.equals(goal)) {
						state = down;
						return;
					}
				}
			}
			if (right != null) {
				if (!all_history.containsKey(right)) {
					empty = false;
					queue.add(right);
					local_history.put(right, state);
					all_history.put(right, state);
					if (right.equals(goal)) {
						state = right;
						return;
					}
				}
			}
			if (empty) {
				removeHistory(state);
			}
		}
	}

}
