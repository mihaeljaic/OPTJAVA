package hr.fer.zemris.optjava.dz4.part2;

import java.util.Random;

public class BoxMutation {
    private int n;
    private Random random = new Random();

    public BoxMutation(int n) {
        this.n = n;
    }

    public void mutation(BoxSolution solution) {
        int varCount = solution.indexes.length;
        for (int i = 0; i < n; i++) {
            int rand1 = random.nextInt(varCount);
            int rand2 = random.nextInt(varCount);

            int temp = solution.indexes[rand1];
            solution.indexes[rand1] = solution.indexes[rand2];
            solution.indexes[rand2] = temp;
        }
    }
}
