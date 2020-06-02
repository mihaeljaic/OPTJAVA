package hr.fer.zemris.optjava.dz3.neighboorhood;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

import java.util.Random;

public class BitVectorUnifNeighborhood implements INeighboorhood<BitVectorSolution> {
    private static final double p = 0.2;
    private Random rand;
    private int n;

    public BitVectorUnifNeighborhood(int n) {
        rand = new Random();
        this.n = n;
    }

    @Override
    public BitVectorSolution randomNeighbor(BitVectorSolution solution) {
        BitVectorSolution neighboor = new BitVectorSolution(solution.bits.length, solution.getBitsNumber());
        boolean change = false;
        for (int i = 0; i < solution.bits.length; i++) {
            neighboor.bits[i] = solution.bits[i];
            if (rand.nextDouble() < p) {
                neighboor.bits[i] ^= (1 << rand.nextInt(n));
                change = true;
            }
        }

        if (!change) {
            neighboor.bits[rand.nextInt(solution.bits.length)] ^= (1 << rand.nextInt(n));
        }

        return neighboor;
    }
}
