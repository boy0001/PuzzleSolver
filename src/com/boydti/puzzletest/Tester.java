package com.boydti.puzzletest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Tester {

	public Tester(String classpath, int WIDTH, int HEIGHT, String METHOD, int num_tests, boolean rand) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Generator gen = new Generator();
		List<File> files = gen.generate(WIDTH, HEIGHT, num_tests, rand);
		
		Class<?> clazz = Class.forName(classpath);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(os);
	    PrintStream old = System.out;
	    System.setOut(ps);

	    int[] explores = new int[files.size()];
	    int[] moves = new int[files.size()];
	    int[] times = new int[files.size()];

	    Method meth = clazz.getMethod("main", String[].class);
	    
	    int j = 0;
		for (File file : files) {
		    String[] params = new String[] {file.getPath(), METHOD}; // init params accordingly
		    long start = System.currentTimeMillis();
		    meth.invoke(null, (Object) params); // static method doesn't have an instance
		    times[j] = (int) (System.currentTimeMillis() - start); 
    		j++;
		}
		
	    System.out.flush();
	    System.setOut(old);
	    
	    String[] split = os.toString().split("\\r?\\n");
	    for (int i = 0; i < split.length; i++) {
	    	if (i % 2 == 0) {
	    		explores[i/2] = Integer.parseInt(split[i].split(" ")[2]);
	    		//first
	    	}
	    	else {
	    		moves[i/2] = split[i].split(";").length + 1;
	    		//second
	    	}
	    }
	    
	    System.out.println("VISITS");
	    System.out.println(" - mean: " + getMean(explores));
	    System.out.println(" - median: " + getMedian(explores));
	    System.out.println(" - sd: " + getSD(explores));
	    System.out.println("MOVES");
	    System.out.println(" - mean: " + getMean(moves));
	    System.out.println(" - median: " + getMedian(moves));
	    System.out.println(" - sd: " + getSD(moves));
	    System.out.println("TIMES");
	    System.out.println(" - mean: " + getMean(times));
	    System.out.println(" - median: " + getMedian(times));
	    System.out.println(" - sd: " + getSD(times));
	}
	
	public double getMean(int[] array) {
		double count = 0;
		for (int i : array) {
			count += i;
		}
		return count / array.length; 
	}
	
	public double getMedian(int[] array) {
		Arrays.sort(array);
		return array.length % 2 == 0 ? array[array.length / 2] : ((double) (array[(array.length -1) / 2] + array[(1 + array.length) / 2])) / 2.0;
	}
	
	public double getSD(int[] array) {
		double av = getMean(array);
		double sd = 0;
		for (int i=0; i<array.length;i++)
		{
		    sd += Math.pow(Math.abs(array[i] - av), 2);
		}
		return Math.sqrt(sd/array.length);
	}

}
