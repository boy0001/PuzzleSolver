package com.boydti.puzzletest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
    	
    	// If you are ONLY generating files
    	boolean generate = false;
    	
    	if (generate) // Generate random files
    	{
	    	int WIDTH = 4;
	    	int HEIGHT = 3;
	    	int num_files = 1000;
	    	boolean rand = true;
	    	Generator generator = new Generator();
	    	generator.generate(WIDTH, HEIGHT, num_files, rand);
    	}
    	else // Testing your solver (This is compatible with any program using standard output)
    	{
    		int WIDTH = 3;
    		int HEIGHT = 3;
    		String[] METHODS = {"GBFS", "CUS2", "AS", "BFS", "CUS1", "DFS"};
    		int num_tests = 100;
    		String classpath = "com.boydti.puzzle.Main";
    		boolean random = false;
    		Generator generator = new Generator();
            List<File> files = generator.generate(WIDTH, HEIGHT, num_tests, random);
    		try {
    			for (String method : METHODS) {
    				System.out.println("--- " + method + " ---");
    				new Tester(classpath, method, files);
    				System.out.println("-----------");
    			}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
    }
}
