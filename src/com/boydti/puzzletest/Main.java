package com.boydti.puzzletest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
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
    		int WIDTH = 2;
    		int HEIGHT = 15;
    		String[] METHODS = {"CUS2"};
    		int num_tests = 100;
    		String classpath = "com.boydti.puzzle.Main";
    		boolean random = false;
    		try {
    			for (String method : METHODS) {
    				System.out.println("--- " + method + " ---");
    				new Tester(classpath, WIDTH, HEIGHT, method, num_tests, random);
    				System.out.println("-----------");
    			}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
    }
}
