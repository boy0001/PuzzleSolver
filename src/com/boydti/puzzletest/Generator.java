package com.boydti.puzzletest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generator {
    public List<File> generate(int WIDTH, int HEIGHT, int num_files, boolean rand) {
    	ArrayList<File> files = new ArrayList<>();
        int moves;
        moves = 1000;
        Random r = new Random();
        if (rand) {
	        for (int i = 0; i < num_files; i++) {
	        	int width = r.nextInt(HEIGHT - 2) + 2;
	        	int height = r.nextInt(HEIGHT - 2) + 2;
	            Node goal = getBase(width, height);
	            Node initial = getPuzzle(goal, width, width * height, moves);
	            files.add(saveState(initial, goal, width, height, width + "x" + height + "-" + i + ".txt"));
	        }
        }
        else {
        	int width = WIDTH;
        	int height = HEIGHT;
	        for (int i = 0; i < num_files; i++) {
	            Node goal = getBase(width, height);
	            Node initial = getPuzzle(goal, width, width * height, moves);
	            files.add(saveState(initial, goal, width, height, width + "x" + height + "-" + i + ".txt"));
	        }
        }
        return files;
    }
    
    public void printState(Node state) {
        System.out.println(Arrays.toString(state.data));
    }
    
    public File saveState(Node initial, Node goal, int width, int height, String name) {
        File file = new File("files" + File.separator + name);
        if (!file.getParentFile().exists()) {
        	file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        String lines = width + "x" + height + "\n";
        String prefix = "";
        
        for (int i = 1; i < initial.data.length; i++) {
            lines += prefix + initial.data[i];
            prefix = " ";
        }
        
        prefix = "\n";
        
        for (int i = 1; i < goal.data.length; i++) {
            lines += prefix + goal.data[i];
            prefix = " ";
        }
        
        try {
            Files.write(file.toPath(), lines.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    public Node getBase(int height, int width) {
        int length = width * height;
        byte[] state = new byte[length];
        for (byte i = 0; i < length - 1; i++) {
            state[i] = (byte) (i + 1);
        }
        state[length - 1] = 0;
        return getState(state);
    }
    
    public Node getPuzzle(Node initial, int width, int length, int moves) {
        Node state = new Node(initial.data.clone());
        Random r = new Random();
        for (int i = 0; i < moves; i++) {
            Node val;
            switch (r.nextInt(4)) {
                case 0:
                    val = getUp(state, width, length);
                    if (val != null) {
                        state = val;
                    }
                    break;
                case 1: 
                    val = getLeft(state, width, length);
                    if (val != null) {
                        state = val;
                    }
                    break;
                case 2:
                    val = getDown(state, width, length);
                    if (val != null) {
                        state = val;
                    }
                    break;
                case 3:
                    val = getRight(state, width, length);
                    if (val != null) {
                        state = val;
                    }
            }
        }
        return state;
    }

    /**
     * Get the state if move down (or null)
     *  - From the perspective of the empty slot
     */
    public Node getDown(Node state, int WIDTH, int LENGTH) {
        byte val = (byte) (state.data[0] + WIDTH);
        if (val > LENGTH) {
            return null;
        }
        return getSwap(state, val);
    }
    
    public Node getState(byte[] simpleGrid) {
        Node state = new Node(new byte[simpleGrid.length + 1]);
        for (int i = 0; i < simpleGrid.length; i++) {
            if (simpleGrid[i] == 0) {
                state.data[0] = (byte) (i + 1);
            }
            state.data[i + 1] = simpleGrid[i];
        }
        return state;
    }
    
    /**
     * Get the state if move up (or null)
     *  - From the perspective of the empty slot
     */
    public Node getUp(Node state, int WIDTH, int LENGTH) {
        byte val = (byte) (state.data[0] - WIDTH);
        if (val <= 0) {
            return null;
        }
        return getSwap(state, val);
    }
    
    /**
     * Get the state if move left (or null)
     *  - From the perspective of the empty slot
     */
    public Node getLeft(Node state, int WIDTH, int LENGTH) {
        byte val = (byte) (state.data[0] - 1);
        if ((val) % WIDTH == 0) {
            return null;
        }
        return getSwap(state, val);
    }
    
    /**
     * Get the state if move right (or null)
     *  - From the perspective of the empty slot
     */
    public Node getRight(Node state, int WIDTH, int LENGTH) {
        if (state.data[0] % WIDTH == 0) {
            return null;
        }
        return getSwap(state, (byte) (state.data[0] + 1));
    }
    
    /**
     * Get the state if an index is swapped with the empty slot
     */
    public Node getSwap(Node state, byte val) {
        state = new Node(state.data.clone());
        byte i = state.data[0];
        byte tmp = state.data[i];
        state.data[i] = state.data[val];
        state.data[val] = tmp;
        state.data[0] = val;
        return state;
    }
}
