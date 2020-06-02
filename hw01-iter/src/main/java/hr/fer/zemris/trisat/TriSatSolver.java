package hr.fer.zemris.trisat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TriSatSolver {
    private static final int NUMBER_OF_ALGORITHMS = 6;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Očekujem 2 argumenta");
            return;
        }

        int algoritmNumber;
        try {
            algoritmNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            System.out.println("Prvi argument je broj algoritma.");
            return;
        }

        if (algoritmNumber < 1 || algoritmNumber > NUMBER_OF_ALGORITHMS) {
            System.out.println("Broj algoritma [1, " + NUMBER_OF_ALGORITHMS + "]");
            return;
        }

        Path path = Paths.get(args[1]);
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Neispravan put do datoteke.");
            return;
        }

        SATFormula formula = parseFormula(lines);
        if (formula == null) {
            System.out.println("Neispravan format datoteke.");
            return;
        }

        SATFormulaStats formulaStats = new SATFormulaStats(formula);
        if (algoritmNumber == 1) {
            System.out.println(algorithm1(formulaStats));
        } else if (algoritmNumber == 2) {
            System.out.println(algorithm2(formulaStats));
        } else if (algoritmNumber == 3) {
            System.out.println(algorithm3(formulaStats));
        } else if (algoritmNumber == 4) {
            System.out.println(algorithm4(formulaStats));
        } else if (algoritmNumber == 5) {
            System.out.println(algorithm5(formulaStats));
        } else {
            System.out.println(algorithm6(formulaStats));
        }
    }

    private static SATFormula parseFormula(List<String> lines) {
        List<Clause> clauses = new ArrayList<>();
        int numberOfVariables = 0;
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("%")) {
                break;
            }

            if (line.toLowerCase().startsWith("c")) {
                continue;
            }

            if (line.toLowerCase().startsWith("p")) {
                String[] temp = line.split("\\s+");
                if (temp.length != 4) {
                    return null;
                }

                try {
                    numberOfVariables = Integer.parseInt(temp[2]);
                } catch (NumberFormatException ex) {
                    return null;
                }
                continue;
            }

            String[] temp = line.split("\\s+");

            List<Integer> literals = new ArrayList<>();
            boolean terminated = false;
            for (String literal : temp) {
                if (terminated) {
                    terminated = false;
                    literals = new ArrayList<>();
                }

                if (literal.equals("0")) {
                    int[] literalsArray = new int[literals.size()];
                    for (int i = 0; i < literalsArray.length; i++) {
                        literalsArray[i] = literals.get(i);
                    }

                    clauses.add(new Clause(literalsArray));
                    terminated = true;
                    continue;
                }
                try {
                    literals.add(Integer.parseInt(literal));
                } catch (NumberFormatException ex) {
                    return null;
                }
            }

        }

        Clause[] clausesArray = new Clause[clauses.size()];
        for (int i = 0; i < clausesArray.length; i++) {
            clausesArray[i] = clauses.get(i);
        }

        return new SATFormula(numberOfVariables, clausesArray);
    }

    private static String algorithm1(SATFormulaStats formulaStats) {
        MutableBitVector assignment = new MutableBitVector(formulaStats.getFormula().getNumberOfVariables());
        int numberOfCombinations = 1 << formulaStats.getFormula().getNumberOfVariables();
        StringBuilder solutions = new StringBuilder();

        formulaStats.setAssignment(assignment, false);
        if (formulaStats.isSatisfied()) {
            solutions.append(assignment.toString());
        }
        for (int i = 1; i <= numberOfCombinations; i++) {
            newCombination(assignment, i);
            formulaStats.setAssignment(assignment, false);
            if (formulaStats.isSatisfied()) {
                solutions.append(assignment.toString() + "\n");
            }
        }

        return solutions.toString();
    }

    private static void newCombination(MutableBitVector assignment, int i) {
        for (int j = 0, n = assignment.getSize(); j < n; j++) {
            if ((i & ((1 << j) - 1)) == 0) {
                assignment.set(j, !assignment.get(j));
            }
        }
    }

    private static final int MAX_ITERATIONS = 100000;

    private static String algorithm2(SATFormulaStats formulaStats) {
        Random random = new Random();
        BitVector assignment = new BitVector(random, formulaStats.getFormula().getNumberOfVariables());
        int iterations = 0;
        BitVectorNGenerator generator = new BitVectorNGenerator(assignment);
        BitVector cycleStopper = assignment;
        while (iterations < MAX_ITERATIONS) {
            formulaStats.setAssignment(assignment, false);
            if (formulaStats.isSatisfied()) {
                return "Zadovoljeno: " + assignment.toString();
            }

            int currentFitness = formulaStats.getNumberOfSatisfied();
            List<BitVector> fittest = new ArrayList<>();
            int maxFitness = 0;
            for (BitVector neighboor : generator) {
            	if (neighboor.equals(cycleStopper)) {
                	continue;
                }
                formulaStats.setAssignment(neighboor, false);
                if (formulaStats.isSatisfied()) {
                    return "Zadovoljeno: " + neighboor.toString();
                }
                
                int fitness = formulaStats.getNumberOfSatisfied();
                if (fitness == maxFitness) {
                    fittest.add(neighboor);
                } else if (fitness > maxFitness) {
                    fittest.clear();
                    fittest.add(neighboor);
                    maxFitness = fitness;
                }
            }
            
            if (currentFitness > maxFitness) {
            	return "Dokazivanje nije uspjelo: dosegnut lokalni optimum";
            }
            
            cycleStopper = assignment;
            assignment = fittest.get(random.nextInt(fittest.size()));
            generator.setAssignment(assignment);
            iterations++;
        }

        return "Dokazivanje nije uspjelo: postignut makismalan broj iteracija";
    }

    private static final int NUMBER_OF_BEST = 6;

    private static String algorithm3(SATFormulaStats formulaStats) {
        Random random = new Random();
        BitVector assignment = new BitVector(random, formulaStats.getFormula().getNumberOfVariables());
        int iterations = 0;
        BitVectorNGenerator generator = new BitVectorNGenerator(assignment);
        while (iterations < MAX_ITERATIONS) {
            formulaStats.setAssignment(assignment, true);
            if (formulaStats.isSatisfied()) {
                return "Zadovoljeno: " + assignment.toString();
            }

            List<Result> neighboors = new ArrayList<>();
            for (BitVector neighboor : generator) {
                formulaStats.setAssignment(neighboor, true);
                if (formulaStats.isSatisfied()) {
                    return "Zadovoljeno: " + neighboor.toString();
                }

                neighboors.add(new Result(neighboor, formulaStats.getNumberOfSatisfied() + formulaStats.getPercentageBonus()));
            }

            neighboors.sort((a, b) -> {
                if (Math.abs(a.fitness - b.fitness) < 1e-9) {
                    return 0;
                }

                return a.fitness < b.fitness ? 1 : -1;
            });
            assignment = neighboors.get(random.nextInt(NUMBER_OF_BEST)).assignment;
            generator.setAssignment(assignment);

            iterations++;
        }

        return "Dokazivanje nije uspjelo: postignut maksimalan broj iteracija";
    }

    private static class Result {
        private BitVector assignment;
        private double fitness;

        private Result(BitVector assignment, double fitness) {
            this.assignment = assignment;
            this.fitness = fitness;
        }
    }

    private static final int MAX_FLIPS = 1000;
    private static final int MAX_TRIES = 100;

    private static String algorithm4(SATFormulaStats formulaStats) {
        Random random = new Random();
        int[] variables = new int[formulaStats.getFormula().getNumberOfVariables()];
        for (int i = 0; i < variables.length; i++) {
        	variables[i] = i;
        }
        for (int i = 0; i < MAX_TRIES; i++) {
            BitVector assignment = new BitVector(random, formulaStats.getFormula().getNumberOfVariables());
            for (int j = 0; j < MAX_FLIPS; j++) {
                formulaStats.setAssignment(assignment, false);
                if (formulaStats.isSatisfied()) {
                    return "Zadovoljeno: " + assignment.toString();
                }

                List<Integer> minBreakVariables = formulaStats.minBreakVariables(variables, assignment);
                int mutationIndex = minBreakVariables.get(random.nextInt(minBreakVariables.size()));
                MutableBitVector temp = assignment.copy();
                temp.set(mutationIndex, !assignment.get(mutationIndex));
                assignment = temp;
            }
        }

        return "Dokazivanje nije uspjelo: postignut maksimalan broj iteracija";
    }

    private static final double P = 0.4;

    private static String algorithm5(SATFormulaStats formulaStats) {
        Random random = new Random();
        for (int i = 0; i < MAX_TRIES; i++) {
            BitVector assignment = new BitVector(random, formulaStats.getFormula().getNumberOfVariables());
            for (int j = 0; j < MAX_FLIPS; j++) {
                formulaStats.setAssignment(assignment, false);
                if (formulaStats.isSatisfied()) {
                    return "Zadovoljeno: " + assignment.toString();
                }

                List<Integer> unsatisfiedClauses = formulaStats.getUnsatisfiedClauses();
                int randUnsatisfied = unsatisfiedClauses.get(random.nextInt(unsatisfiedClauses.size()));
                Clause clause = formulaStats.getFormula().getClause(randUnsatisfied);

                if (Math.random() < P) {
                    int randVariable = Math.abs(clause.getLiteral(random.nextInt(clause.getSize()))) - 1;
                    MutableBitVector temp = assignment.copy();
                    temp.set(randVariable, !assignment.get(randVariable));
                    assignment = temp;
                }

                if (Math.random() < (1 - P)) {
                	int[] variables = new int[clause.getSize()];
                	for (int k = 0; k < variables.length; k++) {
                		variables[k] = Math.abs(clause.getLiteral(k)) - 1;
                	}
                	
                	List<Integer> minBreakVariables = formulaStats.minBreakVariables(variables, assignment);
                    int variable = minBreakVariables.get(random.nextInt(minBreakVariables.size()));
                    MutableBitVector temp = assignment.copy();
                    temp.set(variable, !assignment.get(variable));
                    assignment = temp;
                }
            }
        }

        return "Dokazivanje nije uspjelo: postignut maksimalan broj iteracija";
    }

    private static final double MUTATION_PERCENTAGE = 0.1;

    private static String algorithm6(SATFormulaStats formulaStats) {
        Random random = new Random();
        for (int i = 0; i < MAX_TRIES; i++) {
            BitVector assignment = new BitVector(random, formulaStats.getFormula().getNumberOfVariables());
            BitVector cycleStopper = assignment;
            for (int j = 0; j < MAX_FLIPS; j++) {
                formulaStats.setAssignment(assignment, false);
                if (formulaStats.isSatisfied()) {
                    return "Zadovoljeno: " + assignment.toString();
                }

                int currentFitness = formulaStats.getNumberOfSatisfied();
                List<BitVector> fittest = new ArrayList<>();
                int maxFitness = 0;
                for (BitVector neighboor : new BitVectorNGenerator(assignment)) {
                	if (neighboor.equals(cycleStopper)) {
                    	continue;
                    }
                    formulaStats.setAssignment(neighboor, false);
                    if (formulaStats.isSatisfied()) {
                        return "Zadovoljeno: " + neighboor.toString();
                    }
                    
                    int fitness = formulaStats.getNumberOfSatisfied();
                    if (fitness == maxFitness) {
                        fittest.add(neighboor);
                    } else if (fitness > maxFitness) {
                        fittest.clear();
                        fittest.add(neighboor);
                        maxFitness = fitness;
                    }
                }
                
                cycleStopper = assignment;
                if (currentFitness > maxFitness) {
                    // Stuck in local optimum
                    assignment = change(assignment, random);
                } else {
                    assignment = fittest.get(random.nextInt(fittest.size()));
                }
                
            }
        }

        return "Dokazivanje nije uspjelo: postignut maksimalan broj iteracija";
    }

    private static BitVector change(BitVector assignment, Random random) {
        int numberOfVariables = assignment.getSize();
        int variablesToChange = (int) Math.round(numberOfVariables * MUTATION_PERCENTAGE);
        boolean[] toChange = new boolean[numberOfVariables];
        while (variablesToChange > 0) {
            int varNumber = random.nextInt(numberOfVariables);
            if (!toChange[varNumber]) {
                toChange[varNumber] = true;
                variablesToChange--;
            }
        }

        MutableBitVector newAssignment = new MutableBitVector(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            newAssignment.set(i, toChange[i] ? !assignment.get(i) : assignment.get(i));
        }

        return newAssignment;
    }

}
