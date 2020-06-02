package hr.fer.zemris.trisat;

import java.util.Random;

public class BitVector {
    protected boolean[] variables;

    public BitVector(Random rand, int numberOfBits) {
        if (numberOfBits < 1) {
            throw new IllegalArgumentException("Broj bitova treba biti pozitivan broj.");
        }

        variables = new boolean[numberOfBits];
        for (int i = 0; i < numberOfBits; i++) {
            variables[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean ... bits) {
        variables = bits;
    }

    public BitVector(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Broj bitova treba biti pozitivan broj.");
        }

        variables = new boolean[n];
    }

    public boolean get(int index) {
        return variables[index];
    }

    public int getSize() {
        return variables.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(variables.length);
        for (int i = 0; i < variables.length; i++) {
            sb.append(variables[i] ? "1" : "0");
        }

        return sb.toString();
    }

    public MutableBitVector copy() {
        boolean[] copy = new boolean[variables.length];
        for (int i = 0; i < variables.length; i++) {
            copy[i] = variables[i];
        }

        return new MutableBitVector(copy);
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof BitVector)) {
    		return false;
    	}
    	
    	BitVector other = (BitVector) obj;
    	if (other.getSize() != this.getSize()) {
    		return false;
    	}
    	
    	for (int i = 0; i < getSize(); i++) {
    		if (variables[i] != other.get(i)) {
    			return false;
    		}
    	}
    	
    	return true;
    } 

}
