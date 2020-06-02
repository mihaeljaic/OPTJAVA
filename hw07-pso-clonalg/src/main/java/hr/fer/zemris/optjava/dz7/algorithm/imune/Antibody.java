package hr.fer.zemris.optjava.dz7.algorithm.imune;

public class Antibody {
    private double[] position;
    private double affinity;
    private boolean evaluated;

    public Antibody(double[] position) {
        this.position = position;
    }

    public Antibody(double[] position, double affinity) {
        this.position = position;
        this.affinity = affinity;
        evaluated = true;
    }

    public Antibody clone() {
        double[] copy = new double[position.length];
        for (int i = 0; i < position.length; i++) {
            copy[i] = position[i];
        }

        return new Antibody(copy, affinity);
    }

    public void setAffinity(double affinity) {
        this.affinity = affinity;
        evaluated = true;
    }

    public double getAffinity() {
        return affinity;
    }

    public double[] getPosition() {
        return position;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }
}
