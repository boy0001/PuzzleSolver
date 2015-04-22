package com.boydti.puzzle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Main {
	

	
	public static void main(String[] args) {
		byte[] initial = new byte[] { 6, 7, 4, 1, 5, 3, 8, 0, 2 };
		byte[] goal = new byte[] { 3, 4, 2, 1, 8, 7, 6, 0, 5, };
		String solver = "BFSolver";
		int width = 3;
		int height = 3;
		
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
		ASolver imp;
		try {
			Class<?> clazz = Class.forName("com.boydti.puzzle." + solver);
			imp = (ASolver) clazz.getConstructor(int.class, int.class, byte[].class, byte[].class).newInstance(width, height, initial, goal);
		} catch (Exception e) {
			e.printStackTrace();
			imp = new BFSolver(width, height, initial, goal);
		}
		System.out.println("Starting...");
		long start = System.currentTimeMillis();
		imp.solve();
		System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Tracing history, please wait...");
		imp.displayPath(imp.getState(goal));
	}
}
