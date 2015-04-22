package com.boydti.puzzle;

import java.util.Arrays;

public class Node
{
    /**
     * The first element is the index of the '0'
     * after that is the usual byte array
     */
    public byte[] data;
    
    public Node(byte[] data) {
        this.data = data;
    }

//    public static int[][] v;
    
    int code;
    
    int distance;

    @Override
    public boolean equals(Object other)
    {
        if (other == null) { 
            return false;
        }
        return Arrays.equals(data, ((Node)other).data);
    }

    @Override
    public int hashCode()
    {
        if (code == 0) {
            code = Arrays.hashCode(data);
        }
        return code;
    }
}