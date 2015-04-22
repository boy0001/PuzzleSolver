package com.boydti.puzzle;

import java.util.Arrays;

public final class Node
{
    public byte[] data;

    public Node(byte[] data)
    {
        this.data = data;
    }

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
        int value = 0;
        for (int i = 1; i < data.length; i++) {
        	value += data[i] * (i - 1) * 64;
        }
        return value;
    }
}