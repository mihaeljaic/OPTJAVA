package hr.fer.zemris.optjava.dz4.part2;


public class CycleCrossover {

    public static BoxSolution[] crossover(BoxSolution parent1, BoxSolution parent2) {
        int n = parent1.indexes.length;
        BoxSolution[] offspring = new BoxSolution[2];
        offspring[0] = new BoxSolution(n);
        offspring[1] = new BoxSolution(n);

        boolean[] taken = new boolean[n];
        boolean firstParent = true;
        int i = 0;
        while (i < n) {
            int start = 0;
            while (taken[start]) start++;
            int index = start;
            do {
                int par1 = parent1.indexes[index];
                int par2 = parent2.indexes[index];
                taken[index] = true;
                offspring[0].indexes[index] = firstParent ? par1 : par2;
                offspring[1].indexes[index] = firstParent ? par2 : par1;
                i++;
                for (int j = 0; j < n; j++) {
                    if (parent1.indexes[j] == par2) {
                        index = j;
                        break;
                    }
                }
            } while (index != start);

            firstParent = !firstParent;
        }

        return offspring;
    }
}
