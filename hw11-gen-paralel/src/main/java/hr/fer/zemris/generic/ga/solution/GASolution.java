package hr.fer.zemris.generic.ga.solution;

public abstract class GASolution<T> {
    protected T data;
    public double fitness;

    public GASolution() {

    }

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();
}
