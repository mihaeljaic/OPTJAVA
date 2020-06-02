package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;

public class SATFormulaStats {
    private SATFormula formula;
    private double[] percentages;
    private int numberOfClausesSatisfied;
    private boolean isSatisfied;
    private double percentageBonus;
    private List<Integer> unsatisfiedClauses;
    private int[] trueLitCount;

    private static final double PERCENTAGE_CONSTANT_UP = 0.01;
    private static final double PERCENTAGE_CONSTANT_DOWN = 0.1;
    private static final double PERCENTAGE_UNIT_AMOUNT = 500;

    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        percentages = new double[formula.getNumberOfClauses()];
        unsatisfiedClauses = new ArrayList<>();
        trueLitCount = new int[formula.getNumberOfClauses()];
    }

    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        numberOfClausesSatisfied = 0;
        percentageBonus = 0.0;
        unsatisfiedClauses.clear();
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            trueLitCount[i] = formula.getClause(i).numberOfSatisfied(assignment);
            if (trueLitCount[i] > 0) {
                if (updatePercentages) {
                    percentages[i] = (1 - percentages[i]) * PERCENTAGE_CONSTANT_UP;
                }

                percentageBonus += PERCENTAGE_UNIT_AMOUNT * (1 - percentages[i]);
                numberOfClausesSatisfied++;
            } else {
                if (updatePercentages) {
                    percentages[i] += (0 - percentages[i]) * PERCENTAGE_CONSTANT_DOWN;
                }

                percentageBonus -= PERCENTAGE_UNIT_AMOUNT * (1 - percentages[i]);
                unsatisfiedClauses.add(i);
            }
        }

        isSatisfied = numberOfClausesSatisfied == formula.getNumberOfClauses();
    }

    public SATFormula getFormula() {
        return formula;
    }

    public int getNumberOfSatisfied() {
        return numberOfClausesSatisfied;
    }

    public boolean isSatisfied() {
        return isSatisfied;
    }

    public double getPercentageBonus() {
        return percentageBonus;
    }

    public double getPercentage(int index) {
        return percentages[index];
    }

    public List<Integer> getUnsatisfiedClauses() {
        return unsatisfiedClauses;
    }

    // Finds variables from clause clauseIndex that when changed have least
    // unsatisfied clauses.  
    public List<Integer> minBreakVariables(int[] variables, BitVector assignment) {
    	List<Integer> minBreakVariables = new ArrayList<>();
    	int minBreak = Integer.MAX_VALUE;
    	for (int i = 0; i < variables.length; i++) {
    		int breakX = breakX(variables[i], assignment.get(variables[i]), minBreak);
    		if (breakX < minBreak) {
                minBreakVariables.clear();
                minBreakVariables.add(variables[i]);
                minBreak = breakX;
            } else if (breakX == minBreak) {
                minBreakVariables.add(variables[i]);
            }
    	}
    	
    	return minBreakVariables;
    }
    
    private int breakX(int var, boolean value, int breakMin) {
        int breakX = 0;
        if (value) {
            for (int clauseIndex : formula.getPosLitClause()[var]) {
                if (trueLitCount[clauseIndex] == 1) breakX++;
                if (breakX > breakMin) return breakX;
            }
        } else {
            for (int clauseIndex : formula.getNegLitClause()[var]) {
                if (trueLitCount[clauseIndex] == 1) breakX++;
                if (breakX > breakMin) return breakX;
            }
        }

        return breakX;
    }

}
