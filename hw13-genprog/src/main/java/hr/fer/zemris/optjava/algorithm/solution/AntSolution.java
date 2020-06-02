package hr.fer.zemris.optjava.algorithm.solution;

import java.util.ArrayList;
import java.util.List;

public class AntSolution implements Comparable<AntSolution> {
    private Node root;
    private int foodEaten;
    private double fitness;

    public AntSolution(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public AntSolution copy() {
        AntSolution copy = new AntSolution(root.copy());
        copy.setFitness(fitness);
        copy.setFoodEaten(foodEaten);

        return copy;
    }

    public void setFoodEaten(int foodEaten) {
        this.foodEaten = foodEaten;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    @Override
    public int compareTo(AntSolution o) {
        return Double.compare(fitness, o.fitness);
    }
}
