package hr.fer.zemris.optjava.algorithm.creators;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;
import hr.fer.zemris.optjava.dz13.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grow implements IPopulationCreator {
    private int maxNodes;
    private int maxDepth;
    private int minNodes;

    private int nodes;
    private Random random = new Random();

    public Grow(int maxNodes, int maxDepth, int minNodes) {
        this.maxNodes = maxNodes;
        this.maxDepth = maxDepth;
        this.minNodes = minNodes;
    }

    @Override
    public List<AntSolution> create(int populationSize) {
        List<AntSolution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(create(maxNodes, maxDepth, minNodes));
        }

        return population;
    }

    @Override
    public AntSolution create(int maxNodes, int maxDepth, int minNodes) {
        nodes = minNodes + random.nextInt(maxNodes - minNodes);

        Node root = new Node(random.nextInt(3), 0, new ArrayList<>());
        root = buildTree(root);

        return new AntSolution(root);
    }

    private Node buildTree(Node root) {
        int program = root.getProgram();
        if (root.getProgram() >= 3) {
            return root;
        }

        List<Node> children = root.getChildren();
        for (int i = 0; i < 2; i++) {
            nodes--;
            if (nodes < 3) {
                children.add(buildTree(new Node(3 + random.nextInt(3), 0, new ArrayList<>())));
            } else {
                children.add(buildTree(new Node(random.nextInt(6),0, new ArrayList<>())));
            }
        }
        if (program == Constants.PROG_3) {
            nodes--;
            if (nodes < 3) {
                children.add(buildTree(new Node(3 + random.nextInt(3), 0, new ArrayList<>())));
            } else {
                children.add(buildTree(new Node(random.nextInt(6),0, new ArrayList<>())));
            }
        }

        return root;
    }
}
