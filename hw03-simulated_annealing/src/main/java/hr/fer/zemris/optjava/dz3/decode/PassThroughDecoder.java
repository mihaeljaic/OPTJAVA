package hr.fer.zemris.optjava.dz3.decode;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

	public double[] decode(DoubleArraySolution data) {
		return data.values;
	}

	public void decode(DoubleArraySolution data, double[] point) {
		for (int i = 0; i < point.length; i++) {
			point[i] = data.values[i];
		}
	}
	
}
