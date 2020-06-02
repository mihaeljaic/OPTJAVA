package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution {
	public int[] bits;
	private int bitsNumber;
	
	public BitVectorSolution(int n, int bitsNumber) {
		bits = new int[n];
		this.bitsNumber = bitsNumber;
	}
	
	public SingleObjectiveSolution duplicate() {
		BitVectorSolution duplicate = new BitVectorSolution(bits.length, bitsNumber);
		for (int i = 0; i < bits.length; i++) {
			duplicate.bits[i] = bits[i];
		}
		
		return duplicate;
	}
	
	public void randomize(Random rand) {
		for (int i = 0; i < bits.length; i++) {
			bits[i] = rand.nextInt();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{ ");
		for (int i = 0; i < bits.length; i++) {
			for (int j = bitsNumber - 1; j >= 0; j--) {
				sb.append((bits[i] & j) > 0 ? "1" : "0");
			}
			sb.append(" ");
		}
		sb.append(" }");
		return sb.toString();
	}

	public int getBitsNumber() {
		return bitsNumber;
	}
}
