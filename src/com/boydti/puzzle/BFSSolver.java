/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.ArrayDeque;

public class BFSSolver extends AbstractSolver {

	public BFSSolver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}
	
	/**
	 * The queue object (ArrayDeque is more performant than a linked list)
	 */
	public ArrayDeque<Node> queue = new ArrayDeque<Node>();

	/**
	 * This shouldn't really be called pruning, it's just basic garbage collection
	 */
	@Override
	public void removeHistory(Node node) {
	    toRemove.add(node);
        if (toRemove.size() > queue.size()) {
            all_history.remove(toRemove.remove());
            all_history.remove(toRemove.remove());
            prunes++;
        }
	}
	
	/**
	 * Things to note:
	 * 
	 *  global cache (all_history): Is a hash containing the complete history of anything in the local cache
	 *  
	 */
	@Override
	public void solve() {
	    
	    // Set the goal and initial vars
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		
		// Initialize the queue with the starting state
		queue.add(state);
		
		// Main loop
		while (true) {
		    
		    // Set the state to the first in the queue
		    // Remove it from local history (as it no longer needs to be cached)
			state = queue.remove();
			
			// Calculate the up, left, down and right options
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			
			// If there are no unexplored connected nodes (see logic below)
			boolean empty = true;
			
			if (up != null) {
			    
			    // Check if the node is explored already
				if (!all_history.containsKey(up)) {
				    
				    // Set emtpy to false as an unexplored node was found
					empty = false;
					
					// Add this node to the queue
					queue.add(up);
					
					// Add this node to the global cache
					all_history.put(up, state);
					
					// Check if up is a solution
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
					all_history.put(right, state);
					if (right.equals(goal)) {
						state = right;
						return;
					}
				}
			}
			
			// Prune the history hashmap of useless nodes
			if (empty) {
				removeHistory(state);
			}
		}
	}

}
