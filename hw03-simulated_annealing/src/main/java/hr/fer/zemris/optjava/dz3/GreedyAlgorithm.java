package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.decode.IDecoder;
import hr.fer.zemris.optjava.dz3.neighboorhood.INeighboorhood;

public class GreedyAlgorithm<T> implements IOptAlgorithm<T> {
	private IDecoder<T> decoder;
	private INeighboorhood<T> neighboorhood;
	private T startsWith;
	private IFunction function;
	private boolean minimize;
	private static final int MAX_ITERATIONS = 100000;
	
	public GreedyAlgorithm(IDecoder<T> decoder, INeighboorhood<T> neighboorhood, T startsWith, IFunction function,
			boolean minimize) {
		super();
		this.decoder = decoder;
		this.neighboorhood = neighboorhood;
		this.startsWith = startsWith;
		this.function = function;
		this.minimize = minimize;
	}

	public T run() {
		T omega = startsWith;
		double value = function.valueAt(decoder.decode(omega));
		double fitness = minimize ? -value : value;

		for (int i = 0; i < MAX_ITERATIONS; i++) {
			T neighboor = neighboorhood.randomNeighbor(omega);
			double neighbValue = function.valueAt(decoder.decode(neighboor));
			double neighbFitness = minimize ? -neighbValue : neighbValue;
			
			if (neighbFitness > fitness) {
				fitness = neighbFitness;
				omega = neighboor;
			}
			
			i++;
		}
		
		return omega;
	}
	
}
