package hr.fer.zemris.optjava.dz4.part2;

import java.util.List;

public class BoxFunction {
    private int h;
    private List<Stick> sticks;

    public BoxFunction(int h, List<Stick> sticks) {
        this.h = h;
        this.sticks = sticks;
    }

    public List<Stick> getSticks() {
        return sticks;
    }

    public int getWidth(BoxSolution solution) {
        int width = 1;
        int currentHeight = 0;
        for (int index : solution.indexes) {
            int length = sticks.get(index).getLength();
            if (currentHeight + length > h) {
                width++;
                currentHeight = 0;
            }

            currentHeight += length;
        }

        return width;
    }

    public double evaluateSolution(BoxSolution solution) {
        double fitness = 0;
        int currentHeight = 0;
        for (int index : solution.indexes) {
            int length = sticks.get(index).getLength();
            if (length + currentHeight > h) {
                fitness += Math.pow(h - currentHeight, 0.5);
                currentHeight = 0;
            }

            currentHeight += length;
        }
        if (currentHeight > 0) {
            fitness += Math.pow(h - currentHeight, 0.5);
        }

        return -fitness;
    }

}
