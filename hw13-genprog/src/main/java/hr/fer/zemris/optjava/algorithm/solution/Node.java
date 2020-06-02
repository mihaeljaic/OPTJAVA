package hr.fer.zemris.optjava.algorithm.solution;

import hr.fer.zemris.optjava.dz13.Constants;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int program;
    private int amountOfChildren;
    private List<Node> children;

    private static double currentProbability;
    private static double random;
    private static double nodeProbability;

    public Node(int program, int amountOfChildren, List<Node> children) {
        this.program = program;
        this.amountOfChildren = amountOfChildren;
        this.children = children;
    }

    public int getProgram() {
        return program;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public int getAmountOfChildren() {
        return amountOfChildren;
    }

    public Node copy() {
        List<Node> children = new ArrayList<>();
        for (Node child : this.children) {
            if (child == null) {
                children.add(null);
            } else {
                children.add(child.copy());
            }
        }

        return new Node(program, children.size(), children);
    }

    public static int treeSize(Node root) {
        if (root == null) {
            return 1;
        }

        int size = 1;
        for (Node child : root.getChildren()) {
            size += treeSize(child);
        }

        return size;
    }

    public static int maxDepth(Node root) {
        int deepest = 0;
        for (Node child : root.getChildren()) {
            deepest = Math.max(deepest, maxDepth(child));
        }

        return deepest + 1;
    }

    public Result pickRandomNode() {
        nodeProbability = 1.0 / treeSize(this);
        random = Math.random();
        currentProbability = 0.0;

        return getRandom(this, null, 0);
    }

    private Result getRandom(Node root, Node parent, int childIndex) {
        currentProbability += nodeProbability;
        if (random <= currentProbability) {
            return new Result(root, parent, childIndex);
        }

        int i = 0;
        for (Node child : root.getChildren()) {
            Result result = getRandom(child, root, i);
            if (result != null) {
                return result;
            }
            i++;
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        visit(this, sb);

        return sb.toString();
    }

    private void visit(Node root, StringBuilder sb) {
        String nodeName;
        switch (root.program) {
            case Constants.IF_FOOD_AHEAD: nodeName = "IfFoodAhead"; break;
            case Constants.PROG_2: nodeName = "Program2"; break;
            case Constants.PROG_3: nodeName = "Program3"; break;
            case Constants.MOVE: nodeName = "Move"; break;
            case Constants.LEFT: nodeName = "Left"; break;
            default: nodeName = "Right";
        }
        sb.append(nodeName);
        if (root.getChildren().size() == 0) {
            return;
        }
        sb.append("(");

        for (Node child : root.getChildren()) {
            visit(child, sb);
        }

        sb.append(")");
    }

    public static class Result {
        private Node node;
        private Node parent;
        private int childIndex;

        public Result(Node node, Node parent, int childIndex) {
            this.node = node;
            this.parent = parent;
            this.childIndex = childIndex;
        }

        public Node getNode() {
            return node;
        }

        public Node getParent() {
            return parent;
        }

        public int getChildIndex() {
            return childIndex;
        }
    }
}
