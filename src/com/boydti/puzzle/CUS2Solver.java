/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This solver uses simulated annealing initially then switches to A star when it gets close to the solutin
 * @author Jesse Boyd
 *
 */
public class CUS2Solver extends AbstractSolver {

	public CUS2Solver(int width, int height, byte[] initial, byte[] goal) {
		super(width, height, initial, goal);
	}
	
	public PriorityQueue<Node> queue = new PriorityQueue<Node>(1, new Comparator<Node>() {
        @Override
        public int compare(Node a, Node b) {
            return customDistance(a) - customDistance(b);
        }
    });
	
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
	 * A much faster random class I made which isn't secure at all, but fit for this purpose
	 */
	public PseudoRandom random = new PseudoRandom();
	
	/**
	 * The distance function
	 */
	public int customDistance(Node node) {
        if (positions == null) {
            positions = new byte[GOAL.length];
            for (byte i = 0; i < GOAL.length; i++) {
                positions[GOAL[i]] = i;
            }
        }
        if (node.distance == 0) {
            // Normal manhattan distance
            for (int i = 1, j = 0; i < node.data.length; i++, j++) {
                if (i != node.data[0]) {
                    byte ideal = positions[node.data[i]];
                    node.distance += abs((ideal % WIDTH) - (j % WIDTH));
                    node.distance += abs((ideal / WIDTH) - (j / WIDTH));
                }
            }
            
            // If MD is > 8, use simulated annealing
            if (node.distance > 8) {
                // Decisions have a random element which decreases the closer it gets to the solution
            	node.distance = random.random(node.distance - 4) + node.distance * 8 + node.moves;
            }
            else {
                // If it's really close, use A*
            	node.distance += node.moves;
            }
        }
        return node.distance;
    }
	
	@Override
	public void solve() {
		Node goal = getState(GOAL);
		Node state = getState(INITIAL);
		queue.add(state);
		while (true) {
			state = queue.remove();
			Integer moves = state.moves;
			if (moves == null) {
			    moves = 0;
			}
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			boolean empty = true;
			if (up != null) {
				if (!all_history.containsKey(up)) {
					empty = false;
					up.moves = state.moves + 1;
					queue.add(up);
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
					left.moves = state.moves + 1;
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
					down.moves = state.moves + 1;
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
					right.moves = state.moves + 1;
					queue.add(right);
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
