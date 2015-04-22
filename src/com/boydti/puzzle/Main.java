package com.boydti.puzzle;

import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
	    // The initial and goal states
	    byte[] initial;
	    byte[] goal = null;
	    
	    /*
	     * Some options to choose from
	     */
	    
	    // 3x5 -> 14 moves
//	    initial = new byte[] { 1, 2, 3, 5, 11, 6, 4, 13, 9, 8, 0, 12, 7, 10, 14 };
	    
	    // 3x3 -> 20 moves
	    initial = new byte[] { 6, 7, 4, 1, 5, 3, 8, 0, 2 };
	    goal = new byte[] {3, 4, 2, 1, 8, 7, 6, 0, 5};
	    
	    // 3x3 -> 31 moves
//	    initial = new byte[] { 6,4,7,8,5,0,3,2,1 };
	    
	    // 5x2 -> 55 moves
//	    initial = new byte[] {0,9,3,7,1,5,4,8,2,6};
	    
	    /*
	     * Algorithm BFSolver, DFSolver, GBFSolver
	     */
		String solver = "BFSolver";
		int width = 3;
		int height = 3;
		
		/*
		 * Parsing command line arguments (so you can specify the solver etc)
		 */
		for (String arg : args) {
			String[] split = arg.split("=");
			if (split.length == 2) {
				switch (split[0]) {
					case "start": {
						String[] array = split[1].split(",");
						initial = new byte[array.length];
						for (int i = 0; i < array.length; i++) {
							initial[i] = Byte.parseByte(array[i]);
						}
					}
					case "end": {
						String[] array = split[1].split(",");
						goal = new byte[array.length];
						for (int i = 0; i < array.length; i++) {
							goal[i] = Byte.parseByte(array[i]);
						}
					}
					case "solver": {
						solver = split[1];
					}
					case "width": {
						width = Integer.parseInt(split[1]);
					}
					case "height": {
						height = Integer.parseInt(split[1]);
					}
				}
			}
		}
		// Setting the goal to the sorted byte array (if it wasn't specified)
		if (goal == null) {
		    goal = initial.clone();
		    Arrays.sort(goal);
		    for (int i = 1; i < goal.length; i++) {                
		        goal[i-1] = goal[i];
            }
		    goal[goal.length - 1] = 0;
        }
		
		// Creating a new instance of the specified ASolver class
		ASolver imp;
		try {
			Class<?> clazz = Class.forName("com.boydti.puzzle." + solver);
			imp = (ASolver) clazz.getConstructor(int.class, int.class, byte[].class, byte[].class).newInstance(width, height, initial, goal);
		} catch (Exception e) {
			e.printStackTrace();
			imp = new BFSolver(width, height, initial, goal);
		}
		
		imp.printState(imp.getState(initial));
		imp.printState(imp.getState(goal));
		
		// General stats
		System.out.println("Manhattan Distance: " + imp.manhattanDistance(imp.getState(initial)));
		
		// Actual code
		System.out.println("Solving...");
		long start = System.currentTimeMillis();

		// Solving the puzzle
		try {
		    imp.solve();
		}
		catch (Exception e) {
		    System.out.println(System.currentTimeMillis() - start);
		    System.out.println("No solutions found!");
		    return;
		}
		
		System.out.println(System.currentTimeMillis() - start);
		System.out.println("Tracing history, please wait...");
		
		// Display the stats
		imp.displayPath(imp.getState(goal));
	}
}
