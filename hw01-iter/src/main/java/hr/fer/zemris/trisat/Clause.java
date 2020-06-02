package hr.fer.zemris.trisat;

public class Clause {
    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = indexes;
    }

    public int getSize() {
        return indexes.length;
    }

    public int getLiteral(int index) {
        return indexes[index];
    }

    // Modified because of 5.algorithm
    public int numberOfSatisfied(BitVector assignment) {
        int trueLiterals = 0;
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] > 0 && assignment.get(indexes[i] - 1) || indexes[i] < 0 && !assignment.get(-indexes[i] - 1)) {
                trueLiterals++;
            }
        }

        return trueLiterals;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexes.length; i++) {
            sb.append(String.format("%sx%d%s", indexes[i] < 0 ? "!" : "", Math.abs(indexes[i]), i == indexes.length - 1 ? "" : "+"));
        }

        return sb.toString();
    }
}
