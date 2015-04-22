package com.boydti.puzzle;

public class DFSolver extends ASolver {

	public DFSolver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}

	@Override
	public void solve() {
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		queue.push(state);
		boolean notfound = true; 
		while (notfound) {
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
					queue.push(up);
					local_history.put(up, state);
					all_history.put(up, state);
					if (up.equals(goal)) {
						state = up;
						notfound = false;
						return;
					}
				}
			}
			if (left != null) {
				if (!all_history.containsKey(left)) {
					empty = false;
					queue.push(left);
					local_history.put(left, state);
					all_history.put(left, state);
					if (left.equals(goal)) {
						state = left;
						notfound = false;
						return;
					}
				}
			}
			if (down != null) {
				if (!all_history.containsKey(down)) {
					empty = false;
					queue.push(down);
					local_history.put(down, state);
					all_history.put(down, state);
					if (down.equals(goal)) {
						state = down;
						notfound = false;
						return;
					}
				}
			}
			if (right != null) {
				if (!all_history.containsKey(right)) {
					empty = false;
					queue.push(right);
					local_history.put(right, state);
					all_history.put(right, state);
					if (right.equals(goal)) {
						state = right;
						notfound = false;
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
