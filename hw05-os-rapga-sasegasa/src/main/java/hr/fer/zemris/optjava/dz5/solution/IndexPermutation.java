package hr.fer.zemris.optjava.dz5.solution;

import java.util.Random;

public class IndexPermutation extends Solution<Integer[]> {

    public IndexPermutation(int n) {
        value = new Integer[n];
        for (int i = 0; i < n; i++) {
            value[i] = i;
        }
    }

    public void randomize() {
        Random random = new Random();
        for (int i = value.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = value[index];
            value[index] = value[i];
            value[i] = temp;
        }
    }

    @Override
    public Solution duplicate() {
        IndexPermutation duplicate = new IndexPermutation(value.length);
        for (int i = 0; i < value.length; i++) {
            duplicate.value[i] = value[i];
        }

        return duplicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IndexPermutation)) {
            return false;
        }

        IndexPermutation other = (IndexPermutation) obj;
        if (Math.abs(fitness - other.fitness) >= 1e-9) {
            return false;
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] != other.value[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int index : value) {
            sb.append(index + " ");
        }

        return sb.toString();
    }
}
