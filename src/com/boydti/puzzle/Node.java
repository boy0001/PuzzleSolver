/*
 * Copyright (c) Jesse Boyd 2015 ALL RIGHTS RESERVED
 */

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

    /**
     * The hashcode (cached) (may be unset)
     */
    int code;
    
    /**
     * The Manhattan Distance (cached) (may be unset)
     */
    int distance;
    
    /**
     * The number of moves
     */
    int moves;

    @Override
    public boolean equals(Object other)
    {
        if (other == null) { 
            return false;
        }
        if (other.hashCode() != this.hashCode()) {
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