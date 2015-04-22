package com.boydti.puzzle;

import java.util.Arrays;

public class AdvancedNode extends Node
{

    
    
    public AdvancedNode(byte[] data) {
        super(data);
    }

    int code;
    
    int distance;

    @Override
    public boolean equals(Object other)
    {
        if (other == null) { 
            return false;
        }
        return Arrays.equals(data, ((AdvancedNode)other).data);
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