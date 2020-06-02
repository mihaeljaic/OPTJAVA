package hr.fer.zemris.optjava.algorithm.selection;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements ISelection {
    private int contendersCount;

    public TournamentSelection(int contendersCount) {
        this.contendersCount = contendersCount;
    }

    @Override
    public AntSolution select(List<AntSolution> population) {
        int populationSize = population.size();
        boolean[] taken = new boolean[populationSize];
        Random random = new Random();

        List<AntSolution> contenders = new ArrayList<>(contendersCount);
        for (int i = 0; i < contendersCount; i++) {
            int index = random.nextInt(populationSize);
            while (taken[index]) {
                index = random.nextInt(populationSize);
            }

            taken[index] = true;
            contenders.add(population.get(index));
        }

        Collections.sort(contenders);
        return contenders.get(contendersCount - 1);
    }
}
