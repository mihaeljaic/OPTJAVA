package hr.fer.zemris.optjava.dz5.selection;

import hr.fer.zemris.optjava.dz5.solution.BitVectorSolution;

import java.util.List;
import java.util.Random;

public class Task1Selection implements ISelectOperator<BitVectorSolution> {
    private TournamentSelection<BitVectorSolution> selectOperator;
    private int i = 0;
    private Random random = new Random();

    public Task1Selection(TournamentSelection<BitVectorSolution> selectOperator) {
        this.selectOperator = selectOperator;
    }

    @Override
    public BitVectorSolution select(List<BitVectorSolution> population) {
        BitVectorSolution parent;
        if (i % 2 == 0) {
            parent = selectOperator.select(population);
        } else {
            parent = population.get(random.nextInt(population.size()));
        }
        i++;

        return parent;
    }
}
