package hr.fer.zemris.optjava.dz4.part2;

class CycleCrossoverTest {
    @org.junit.jupiter.api.Test
    void crossover() {
        CycleCrossover cycleCrossover = new CycleCrossover();
        BoxSolution p1 = new BoxSolution(10);
        BoxSolution p2 = new BoxSolution(10);
        p1.indexes[0] = 8;
        p1.indexes[1] = 4;
        p1.indexes[2] = 7;
        p1.indexes[3] = 3;
        p1.indexes[4] = 6;
        p1.indexes[5] = 2;
        p1.indexes[6] = 5;
        p1.indexes[7] = 1;
        p1.indexes[8] = 9;
        p1.indexes[9] = 0;

        p2.indexes[0] = 0;
        p2.indexes[1] = 1;
        p2.indexes[2] = 2;
        p2.indexes[3] = 3;
        p2.indexes[4] = 4;
        p2.indexes[5] = 5;
        p2.indexes[6] = 6;
        p2.indexes[7] = 7;
        p2.indexes[8] = 8;
        p2.indexes[9] = 9;

        BoxSolution[] res = cycleCrossover.crossover(p1, p2);
        System.out.println("Offspring 1: " + res[0]);
        System.out.println("Offspring 2: " + res[1]);
    }

}