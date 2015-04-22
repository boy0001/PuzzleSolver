/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.ArrayDeque;

public class DFSSolver extends AbstractSolver {

	public DFSSolver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}

	public ArrayDeque<Node> queue = new ArrayDeque<Node>();
	
	@Override
	public void removeHistory(Node node) {
	}
	
	@Override
	public void solve() {
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		queue.push(state);
		all_history.put(state, state);
		while (true) {
			state = queue.remove();
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			boolean empty = true;
            
			if (right != null) {
                if (!all_history.containsKey(right)) {
                    empty = false;
                    queue.push(right);
                    all_history.put(right, state);
                    if (right.equals(goal)) {
                        state = right;
                        return;
                    }
                }
            }
			if (down != null) {
				if (!all_history.containsKey(down)) {
					empty = false;
					queue.push(down);
					all_history.put(down, state);
					if (down.equals(goal)) {
						state = down;
						return;
					}
				}
			}
			if (left != null) {
                if (!all_history.containsKey(left)) {
                    empty = false;
                    queue.push(left);
                    all_history.put(left, state);
                    if (left.equals(goal)) {
                        state = left;
                        return;
                    }
                }
            }
			if (up != null) {
                if (!all_history.containsKey(up)) {
                    empty = false;
                    queue.push(up);
                    all_history.put(up, state);
                    if (up.equals(goal)) {
                        state = up;
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
