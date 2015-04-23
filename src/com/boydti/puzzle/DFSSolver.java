/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.HashMap;

public class DFSSolver extends AbstractSolver {

	public DFSSolver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}

	public ArrayDeque<Node> queue = new ArrayDeque<Node>();
	
	@Override
	public void removeHistory(Node node) {
	}
	
	HashMap<Node, Node> local_history = new HashMap<>();
	
	@Override
	public void solve() {
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		queue.push(state);
		while (true) {
			state = queue.remove();
			if (all_history.containsKey(state)) {
				continue;
			}
			all_history.put(state, local_history.get(state));
			local_history.remove(state);
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			boolean empty = true;
            
			if (right != null) {
				local_history.put(right, state);
                empty = false;
                queue.push(right);
                if (right.equals(goal)) {
                	all_history.put(right, state);
                    state = right;
                    return;
                }
            }
			if (down != null) {
				local_history.put(down, state);
				empty = false;
				queue.push(down);
				if (down.equals(goal)) {
					all_history.put(down, state);
					state = down;
					return;
				}
			}
			if (left != null) {
				local_history.put(left, state);
                empty = false;
                queue.push(left);
                if (left.equals(goal)) {
                	all_history.put(left, state);
                    state = left;
                    return;
                }
            }
			if (up != null) {
				local_history.put(up, state);
                empty = false;
                queue.push(up);
                if (up.equals(goal)) {
                	all_history.put(up, state);
                    state = up;
                    return;
                }
            }
			if (empty) {
				removeHistory(state);
			}
		}
	}

}
