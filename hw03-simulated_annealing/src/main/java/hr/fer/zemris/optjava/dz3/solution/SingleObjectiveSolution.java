package hr.fer.zemris.optjava.dz3.solution;

public class SingleObjectiveSolution {
	public double fitness;
	public double value;

	public int compareTo(SingleObjectiveSolution other) {
		if (Math.abs(fitness - other.fitness) < 1e-6) {
			return 0;
		}
		
		return fitness < other.fitness ? -1 : 1;
	}
}
