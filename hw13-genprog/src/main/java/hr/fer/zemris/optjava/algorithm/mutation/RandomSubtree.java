package hr.fer.zemris.optjava.algorithm.mutation;

import hr.fer.zemris.optjava.algorithm.creators.IPopulationCreator;
import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;

public class RandomSubtree implements IMutation {
    private int maxNodes;
    private IPopulationCreator creator;

    public RandomSubtree(int maxNodes, IPopulationCreator creator) {
        this.maxNodes = maxNodes;
        this.creator = creator;
    }

    @Override
    public AntSolution mutate(AntSolution solution) {
        Node root = solution.getRoot();

        Node.Result randomSubtree = root.pickRandomNode();
        if (randomSubtree.getParent() == null) {
            return creator.create(maxNodes, 0, maxNodes / 2);
        }

        int max = maxNodes - Node.treeSize(root);
        if (max <= 0 || max / 2 == 0 || max / 2 == max / 3) {
            return solution;
        }

        randomSubtree.getParent().getChildren().set(randomSubtree.getChildIndex(), null);

        Node subtree = creator.create(max / 2, 0, max / 3).getRoot();
        randomSubtree.getParent().getChildren().set(randomSubtree.getChildIndex(), subtree);

        return solution;
    }
}
