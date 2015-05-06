package com.boydti.puzzle;

import java.util.Arrays;

public class SolverUtil {
    public static boolean isValidPuzzle(byte[] initial, byte[] goal) {
        int a = getGroups(initial);
        int b = getGroups(goal);
        if (a == -1 || b == -1) {
            return false;
        }
        return a % 2 == b % 2;
    }
    
    public static int getGroups(byte[] state) {
        int goalSum = state.length * (state.length - 1) / 2;
        int sum = state[state.length - 1];
        int count = 0;
        for (int i = 0; i < state.length - 1; i++) {
            sum += state[i];
            for (int j = i + 1; j < state.length; j++) {
                if (state[j] == state[i]) {
                    return -1;
                }
                if (state[j] == 0) {
                    continue;
                }
                if (state[j] < state[i]) {
                    count++;
                }
            }
        }
        if (sum != goalSum) {
            return -1;
        }
        return count;
    }
    
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
