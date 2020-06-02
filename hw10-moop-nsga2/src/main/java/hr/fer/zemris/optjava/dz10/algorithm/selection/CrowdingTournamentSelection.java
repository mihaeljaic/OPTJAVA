package hr.fer.zemris.optjava.dz10.algorithm.selection;

import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CrowdingTournamentSelection implements ISelection {
    private int contendersCount;
    private static Random random = new Random();
    private Comparator<Solution> comparator = (s1, s2) -> {
        if (s1.getFrontIndex() == s2.getFrontIndex()) {
            return Double.compare(s2.getCrowdingDistance(), s1.getCrowdingDistance());
        }

        return s1.getFrontIndex() - s2.getFrontIndex();
    };

    public CrowdingTournamentSelection(int contendersCount) {
        this.contendersCount = contendersCount;
    }

    public void setContendersCount(int contendersCount) {
        this.contendersCount = contendersCount;
    }

    public int getContendersCount() {
        return contendersCount;
    }

    @Override
    public Solution select(List<Solution> population) {
        List<Solution> contenders = new ArrayList<>(contendersCount);
        if (population.size() <= contendersCount) {
            contenders.addAll(population);
        } else {
            boolean[] contends = new boolean[population.size()];
            for (int i = 0; i < contendersCount; i++) {
                int index = random.nextInt(population.size());
                while (contends[index]) {
                    index = random.nextInt(population.size());
                }

                contenders.add(population.get(index));
                contends[index] = true;
            }
        }

        contenders.sort(comparator);

        return contenders.get(0);
    }

}
