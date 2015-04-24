/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * This is a custom iterative depth first solver. 
 * It uses depth first to a depth and slowly increases the max depth until it solves it
 * (Not very pracical or efficient) 
 * @author Jesse Boyd
 *
 */
public class CUS1Solver extends AbstractSolver {

	public CUS1Solver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}

	// ArrayDeque is what we use as we don't need any sorting
	public ArrayDeque<Node> queue = new ArrayDeque<Node>();
	
	@Override
	public void removeHistory(Node node) {
	}
	
	HashMap<Node, Node> local_history = new HashMap<>();
	
	@Override
	public void solve() {
		int max_depth = 1;
		Node goal = getState(GOAL);
		while (true) {
			Node state = getState(INITIAL);
			queue.push(state);
			while (true) {
				state = queue.poll();
				if (state == null) {
					break;
				}
				if (state.moves >= max_depth) {
					continue;
				}
				Node other = all_history.get(state);
				if (other != null) {
					if (other.moves <= state.moves) {
						continue;
					}
				}
				all_history.put(state, local_history.get(state));
				local_history.remove(state);
				Node up = getUp(state);
				Node left = getLeft(state);
				Node down = getDown(state);
				Node right = getRight(state);
				boolean empty = true;
	            
				if (right != null) {
					right.moves = state.moves + 1;
					Node check = local_history.get(right);
					if (check == null || check.moves >= right.moves) {
						local_history.put(right, state);
						empty = false;
		                queue.push(right);
		                if (right.equals(goal)) {
		                	all_history.put(right, state);
		                    state = right;
		                    return;
		                }
					}
	            }
				if (down != null) {
					down.moves = state.moves + 1;
					Node check = local_history.get(down);
					if (check == null || check.moves >= down.moves) {
						local_history.put(down, state);
						empty = false;
						queue.push(down);
						if (down.equals(goal)) {
							all_history.put(down, state);
							state = down;
							return;
						}
					}
				}
				if (left != null) {
					left.moves = state.moves + 1;
					Node check = local_history.get(left);
					if (check == null || check.moves >= left.moves) {
						local_history.put(left, state);
						empty = false;
		                queue.push(left);
		                if (left.equals(goal)) {
		                	all_history.put(left, state);
		                    state = left;
		                    return;
		                }
					}
	            }
				if (up != null) {
					up.moves = state.moves + 1;
					Node check = local_history.get(up);
					if (check == null || check.moves >= up.moves) {
						local_history.put(up, state);
						empty = false;
		                queue.push(up);
		                if (up.equals(goal)) {
		                	all_history.put(up, state);
		                    state = up;
		                    return;
		                }
					}
	            }
				if (empty) {
					removeHistory(state);
				}
			}
			queue.clear();
			all_history.clear();
			local_history.clear();
			max_depth += Main.PRECISION;
		}
	}

}
