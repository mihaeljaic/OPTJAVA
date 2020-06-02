package hr.fer.zemris.optjava.dz3.decode;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public class NaturalBinaryDecoder extends BitVectorDecoder {

	public NaturalBinaryDecoder(double[] mins, double[] maxs, int n, int totalBits) {
		super(mins, maxs, n, totalBits);
	}

	public NaturalBinaryDecoder(double mins, double maxs, int n, int totalBits) {
	    super(mins, maxs, n, totalBits);
    }

	@Override
	public double[] decode(BitVectorSolution data) {
		double[] solution = new double[n];
		bitsToDouble(data, solution);

		return solution;
	}

	@Override
	public void decode(BitVectorSolution data, double[] point) {

	}

	private void bitsToDouble(BitVectorSolution data, double[] solution) {
        int maxNumber = (1 << totalBits) - 1;
        for (int i = 0; i < n; i++) {
            solution[i] = mins[i] + (double) k(data.bits[i]) / maxNumber * (maxs[i] - mins[i]);
        }
    }

	private int k(int bits) {
	    int value = 0;
	    for (int i = 0; i < totalBits; i++) {
	        value += ((1 << i) & bits);
        }

        return value;
    }

}
