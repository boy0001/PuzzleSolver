package com.boydti.puzzle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class CUS2Solver extends AbstractSolver {

    
    /**
     * Like A* except it also uses the Manhattan distance from the start as well as the end
     * 
     * Searches 2189 nodes instead of 47828 for the 3x3
     * Searches 381 nodes instead of 30780 for the 3x5
     * 
     */
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
	
	HashMap<Node, Integer> local_history = new HashMap<>();
	
	public byte[] positions2;
	
	public int customDistance(Node node) {
        if (positions == null) {
            positions = new byte[GOAL.length];
            positions2 = new byte[INITIAL.length];
            for (byte i = 0; i < GOAL.length; i++) {
                positions[GOAL[i]] = i;
                positions2[INITIAL[i]] = i;
            }
        }
        if (node.distance == 0) {
            for (int i = 1, j = 0; i < node.data.length; i++, j++) {
                byte ideal = positions[node.data[i]];
                node.distance += abs((ideal % WIDTH) - (j % WIDTH)) * Main.PRECISION;
                node.distance += abs((ideal / WIDTH) - (j / WIDTH)) * Main.PRECISION;
                
                byte ideal2 = positions2[node.data[i]];
                node.distance -= abs((ideal2 % WIDTH) - (j % WIDTH)) * Main.PRECISION;
                node.distance -= abs((ideal2 / WIDTH) - (j / WIDTH)) * Main.PRECISION;

                Integer moves = local_history.get(node);
                if (moves != null) {
                    node.distance += moves;
                }
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
			Integer moves = local_history.get(state);
			if (moves == null) {
			    moves = 0;
			}
			local_history.remove(state);
			Node up = getUp(state);
			Node left = getLeft(state);
			Node down = getDown(state);
			Node right = getRight(state);
			boolean empty = true;
			if (up != null) {
				if (!all_history.containsKey(up)) {
					empty = false;
					local_history.put(up, moves + 1);
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
					local_history.put(left, moves + 1);
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
					local_history.put(down, moves + 1);
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
					local_history.put(right, moves + 1);
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
