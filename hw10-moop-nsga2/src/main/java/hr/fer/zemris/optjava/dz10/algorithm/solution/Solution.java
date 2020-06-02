package hr.fer.zemris.optjava.dz10.algorithm.solution;

public class Solution {
    private double[] decisionSpace;
    private double[] objectiveSpace;
    private int frontIndex;
    private double crowdingDistance;

    public Solution(double[] decisionSpace, int n) {
        this.decisionSpace = decisionSpace;
        objectiveSpace = new double[n];
    }

    public double[] getDecisionSpace() {
        return decisionSpace;
    }

    public double[] getObjectiveSpace() {
        return objectiveSpace;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public int getFrontIndex() {
        return frontIndex;
    }

    public void setFrontIndex(int frontIndex) {
        this.frontIndex = frontIndex;
    }

    public String decisionSpacetoString() {
        StringBuilder sb = new StringBuilder();
        for (double d : decisionSpace) {
            sb.append(d + " ");
        }

        return sb.toString();
    }

    public String objectiveSpacetoString() {
        StringBuilder sb = new StringBuilder();
        for (double d : objectiveSpace) {
            sb.append(d + " ");
        }

        return sb.toString().trim();
    }

}
