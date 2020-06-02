package hr.fer.zemris.optjava.dz5.function;

import hr.fer.zemris.optjava.dz5.solution.BitVectorSolution;

public class MaxOneFitnessFunction implements IFitnessFunction<BitVectorSolution> {
    private int bitNumber;

    public MaxOneFitnessFunction(int bitNumber) {
        this.bitNumber = bitNumber;
    }

    @Override
    public int getVariableCount() {
        return bitNumber;
    }

    public double calculateFitness(BitVectorSolution solution) {
        int bitCount = 0;
        for (boolean b : solution.value) {
            if (b) bitCount++;
        }

        if (bitCount <= 0.8 * bitNumber) {
            return (double) bitCount / bitNumber;
        } else if (bitCount > 0.8 * bitNumber && bitCount <= 0.9 * bitNumber) {
            return 0.8;
        } else {
            return 2 * bitCount / bitNumber - 1;
        }
    }
}
