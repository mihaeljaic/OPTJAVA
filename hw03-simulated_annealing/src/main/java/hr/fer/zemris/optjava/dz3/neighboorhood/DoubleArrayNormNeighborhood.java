package hr.fer.zemris.optjava.dz3.neighboorhood;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

public class DoubleArrayNormNeighborhood implements INeighboorhood<DoubleArraySolution> {
	private double[] deltas;
	Random rand;
	
	public DoubleArrayNormNeighborhood(double[] deltas) {
		super();
		this.deltas = deltas;
		rand = new Random();
	}

	public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
		DoubleArraySolution newSolution = solution.duplicate();
		for (int i = 0; i < deltas.length; i++) {
			newSolution.values[i] = solution.values[i] + rand.nextGaussian() * deltas[i];
		}
		
		return newSolution;
	}

}
