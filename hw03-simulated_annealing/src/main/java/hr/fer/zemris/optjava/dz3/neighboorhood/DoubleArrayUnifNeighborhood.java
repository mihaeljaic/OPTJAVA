package hr.fer.zemris.optjava.dz3.neighboorhood;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

public class DoubleArrayUnifNeighborhood implements INeighboorhood<DoubleArraySolution> {
	private double[] deltas;
	Random rand;
	private double p;

	public DoubleArrayUnifNeighborhood(double[] deltas, double p) {
		super();
		this.deltas = deltas;
		rand = new Random();
		this.p = p;
	}

	public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
		DoubleArraySolution newSolution = solution.duplicate();
		boolean change = false;
		for (int i = 0, len = solution.values.length; i < len; i++) {
			newSolution.values[i] = solution.values[i];
			if (rand.nextDouble() < p) {
				newSolution.values[i] += 2 * rand.nextDouble() * deltas[i] - deltas[i];
				change = true;
			}
		}
		
		if (!change) {
			int randInt = rand.nextInt(deltas.length);
			newSolution.values[randInt] += rand.nextDouble() * 2 * deltas[randInt] - deltas[randInt]; 
		}
		
		return newSolution;
	}	
	
}
