package hr.fer.zemris.optjava.dz3;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.decode.IDecoder;
import hr.fer.zemris.optjava.dz3.neighboorhood.INeighboorhood;
import hr.fer.zemris.optjava.dz3.tempSchedule.ITempSchedule;

public class SimulatedAnnealing<T> implements IOptAlgorithm<T> {
	private IDecoder<T> decoder;
	private INeighboorhood<T> neighboorhood;
	private T startsWith;
	private IFunction function;
	private boolean minimize;
	private Random rand;
	private ITempSchedule tempSchedule;
	
	public SimulatedAnnealing(IDecoder<T> decoder, INeighboorhood<T> neighboorhood, T startsWith, IFunction function,
			boolean minimize, ITempSchedule tempSchedule) {
		super();
		this.decoder = decoder;
		this.neighboorhood = neighboorhood;
		this.startsWith = startsWith;
		this.function = function;
		this.minimize = minimize;
		this.tempSchedule = tempSchedule;
		rand = new Random();
	}

	public T run() {
		T omega = startsWith;
		double value = function.valueAt(decoder.decode(omega));
		double fitness = minimize ? -value : value; 
		
		for (int k = 0, tempChanges = tempSchedule.getOuterLoopCounter(); k < tempChanges; k++) {
			double currentTemp = tempSchedule.getNextTemperature();
			for (int i = 0, innerLoop = tempSchedule.getInnerLoopCounter(); i < innerLoop; i++) {
				T neighboor = neighboorhood.randomNeighbor(omega);
				double nValue = function.valueAt(decoder.decode(neighboor));
				double nFitness = minimize ? -nValue : nValue;
				double delta = fitness - nFitness;
				
				if (delta <= 0) {
					omega = neighboor;
					fitness = nFitness;
				} else {
					double p = Math.exp(-delta / currentTemp);
					if (rand.nextDouble() < p) {
						omega = neighboor;
					}
				}

			}
		}
		
		return omega;
	}

}
