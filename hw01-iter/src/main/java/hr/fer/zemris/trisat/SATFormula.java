package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;

public class SATFormula {
    private int numberOfVariables;
    private Clause[] clauses;
    private List<Integer>[] posLitClause;
    private List<Integer>[] negLitClause;

    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
        initClauses();
    }

    private void initClauses() {
        posLitClause = new List[numberOfVariables];
        negLitClause = new List[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            posLitClause[i] = new ArrayList<>();
            negLitClause[i] = new ArrayList<>();
        }

        for (int i = 0; i < clauses.length; i++) {
            Clause clause = clauses[i];
            for (int j = 0, len = clause.getSize(); j < len; j++) {
                int literal = clause.getLiteral(j);
                if (literal > 0) {
                    posLitClause[literal-1].add(i);
                } else {
                    literal = -literal;
                    negLitClause[literal-1].add(i);
                }
            }
        }
    }

    public List<Integer>[] getPosLitClause() {
        return posLitClause;
    }

    public List<Integer>[] getNegLitClause() {
        return negLitClause;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return clauses.length;
    }

    public Clause getClause(int index) {
        return clauses[index];
    }

    public boolean isSatisfied(BitVector assignment) {
        for (Clause clause : clauses) {
            if (clause.numberOfSatisfied(assignment) == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("f(");
        for (int i = 0; i < numberOfVariables; i++) {
            sb.append("x" + Integer.toString(i + 1));
            if (i < numberOfVariables - 1) {
                sb.append(", ");
            }
        }

        sb.append(")=");
        for (int i = 0; i < clauses.length; i++) {
            sb.append("(" + clauses[i].toString() + ")");
        }

        return sb.toString();
    }
}
