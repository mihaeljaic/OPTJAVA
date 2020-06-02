package hr.fer.zemris.optjava.dz3.decode;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
	protected double[] mins;
	protected double[] maxs;
	protected int n;
	protected int totalBits;
	
	public BitVectorDecoder(double[] mins, double[] maxs, int n, int totalBits) {
		super();
		if (totalBits < 5 || totalBits > 30) {
		    throw new IllegalArgumentException("Supported number of bits are [5, 30]");
        }

		this.mins = mins;
		this.maxs = maxs;
		this.n = n;
		this.totalBits = totalBits;
	}
	
	public BitVectorDecoder(double min, double max, int n, int totalBits) {
		super();
        if (totalBits < 5 || totalBits > 30) {
            throw new IllegalArgumentException("Supported number of bits are [5, 30]");
        }

		this.n = n;
		mins = new double[n];
		maxs = new double[n];
		for (int i = 0; i < n; i++) {
		    mins[i] = min;
		    maxs[i] = max;
        }

		this.totalBits = totalBits;
	}


	public int getTotalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
	
}
