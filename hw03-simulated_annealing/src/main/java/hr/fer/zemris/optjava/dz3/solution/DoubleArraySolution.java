package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution {
	public double[] values;
	
	public DoubleArraySolution(int n) {
		values = new double[n];
	}
	
	public DoubleArraySolution duplicate() {
		DoubleArraySolution duplicate = new DoubleArraySolution(values.length);
		for (int i = 0; i < values.length; i++) {
			duplicate.values[i] = values[i];
		}
		
		return duplicate;
	}
	
	public void randomize(Random rand, double[] lower, double[] upper) {
		for (int i = 0; i < values.length; i++) {
			values[i] = rand.nextDouble() * (upper[i] - lower[i]) + lower[i];
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		
		for (int i = 0; i < values.length; i++) {
			sb.append(" " + values[i]);
			if (i != values.length-1) {
				sb.append(",");
			}
		}
		
		return sb.append("}").toString();
	}
	
}
