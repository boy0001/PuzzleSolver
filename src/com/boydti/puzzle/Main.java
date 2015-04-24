/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

package com.boydti.puzzle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Main {
    
    /**
     * Value of 1 is 100% accurate for A* and CUS2
     * Higher values are faster but less accurate
     */
    public static int PRECISION = 2;
	
	public static void main(String[] args) {
	    // The initial and goal states
	    byte[] initial = null;
	    byte[] goal = null;
	    int width;
	    int height;
	    String solver;
	    String filename = "";
	    
		/*
		 * Parsing command line arguments (so you can specify the solver etc.)
		 */
		if (args.length == 2) {
		    filename = args[0];
		    File file = new File(filename);
		    List<String> lines;
		    try {
                lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
		    String[] sizeSplit = lines.get(0).split("x");
		    width = Integer.parseInt(sizeSplit[0]);
		    height = Integer.parseInt(sizeSplit[1]);
		    
		    String[] startStr = lines.get(2).split(" ");
            initial = new byte[startStr.length];
            for (int i = 0; i < startStr.length; i++) {
                initial[i] = Byte.parseByte(startStr[i]);
            }
            
            String[] endStr = lines.get(1).split(" ");
            goal = new byte[startStr.length];
            for (int i = 0; i < endStr.length; i++) {
                goal[i] = Byte.parseByte(endStr[i]);
            }
            solver = args[1];
            
		}
		else { // Some initial values
		    
//	        initial = new byte[] { 1, 2, 3, 5, 11, 6, 4, 13, 9, 8, 0, 12, 7, 10, 14 }; // 3x5 -> 14 moves
	        
	        initial = new byte[] { 6, 7, 4, 1, 5, 3, 8, 0, 2 }; // 3x3 -> 20 moves
	        goal = new byte[] {3, 4, 2, 1, 8, 7, 6, 0, 5};
	        
//	      initial = new byte[] { 6,4,7,8,5,0,3,2,1 }; // 3x3 -> 31 moves
	        
//	      initial = new byte[] {0,9,3,7,1,5,4,8,2,6}; // 5x2 -> 55 moves
	        
	        solver = "BFS";
	        width = 3;
	        height = 3;
	        
	        if (goal == null) {
	            goal = getGoal(initial);
	        }
	        
	        filename = "null.txt";
		}
		
		// Creating a new instance of the specified AbstractSolver class
		AbstractSolver imp;
		try {
			Class<?> clazz = Class.forName("com.boydti.puzzle." + solver + "Solver");
			imp = (AbstractSolver) clazz.getConstructor(int.class, int.class, byte[].class, byte[].class).newInstance(width, height, initial, goal);
		} catch (Exception e) {
			e.printStackTrace();
			imp = new BFSSolver(width, height, initial, goal);
		}
		
		// Solving the puzzle 
		try {
		    imp.solve();
		}
		catch (Exception e) {
			System.out.println(filename + " " + solver + " " + (imp.all_history.size() + imp.prunes));
			System.out.println("No solution found");
		    return;
		}
		System.out.println(filename + " " + solver + " " + (imp.all_history.size() + imp.prunes));
		imp.displayPath(imp.getState(goal));
	}
	
	/**
	 * Get the default goal if the goal is not set 
	 */
	public static byte[] getGoal(byte[] initial) {
	    byte[] goal = initial.clone();
	    Arrays.sort(goal);
	    for (int i = 1; i < goal.length; i++) {                
	        goal[i-1] = goal[i];
	    }
	    goal[goal.length - 1] = 0;
	    return goal;
	}
}
