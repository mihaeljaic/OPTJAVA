package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;
import hr.fer.zemris.optjava.dz13.Constants;

public class FitnessFunction {
    private int actions;
    private int rows;
    private int cols;
    private boolean[][] map;

    private int actionsLeft;
    private int foodEaten;
    private int x;
    private int y;
    private int orientation;
    private boolean[][] tempMap;

    public FitnessFunction(int actions, int rows, int cols, boolean[][] map) {
        this.actions = actions;
        this.rows = rows;
        this.cols = cols;
        this.map = map;
        tempMap = new boolean[rows][cols];
    }

    public int evaluateSolution(AntSolution solution) {
        foodEaten = 0;
        x = 0;
        y = 0;
        orientation = Constants.ORIENTATION_RIGHT;
        actionsLeft = actions;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tempMap[i][j] = map[i][j];
            }
        }

        visit(solution.getRoot());

        return foodEaten;
    }

    private boolean isTerminalNode(int program) {
        return program == Constants.MOVE || program == Constants.LEFT || program == Constants.RIGHT;
    }

    private void visit(Node root) {
        if (actionsLeft == 0) return;
        int program = root.getProgram();
        if (isTerminalNode(program)) {
            if (program == Constants.MOVE) {
                handleMove();
            } else if (program == Constants.LEFT) {
                rotateLeft();
            } else {
                rotateRight();
            }

            actionsLeft--;
            return;
        }

        if (program == Constants.IF_FOOD_AHEAD) {
            if (tempMap[getAheadY()][getAheadX()]) {
                visit(root.getChildren().get(0));
            } else {
                visit(root.getChildren().get(1));
            }
        } else {
            for (Node node : root.getChildren()) {
                visit(node);
            }
        }
    }

    private void rotateLeft() {
        if (orientation == Constants.ORIENTATION_RIGHT) {
            orientation = Constants.ORIENTATION_UP;
        } else if (orientation == Constants.ORIENTATION_UP) {
            orientation = Constants.ORIENTATION_LEFT;
        } else if (orientation == Constants.ORIENTATION_LEFT) {
            orientation = Constants.ORIENTATION_DOWN;
        } else {
            orientation = Constants.ORIENTATION_RIGHT;
        }
    }

    private void rotateRight() {
        if (orientation == Constants.ORIENTATION_RIGHT) {
            orientation = Constants.ORIENTATION_DOWN;
        } else if (orientation == Constants.ORIENTATION_UP) {
            orientation = Constants.ORIENTATION_RIGHT;
        } else if (orientation == Constants.ORIENTATION_LEFT) {
            orientation = Constants.ORIENTATION_UP;
        } else {
            orientation = Constants.ORIENTATION_LEFT;
        }
    }

    private int getAheadX() {
        int aheadX = x;
        if (orientation == Constants.ORIENTATION_RIGHT) {
            aheadX++;
            aheadX %= cols;
        } else if (orientation == Constants.ORIENTATION_LEFT) {
            aheadX--;
            aheadX = aheadX < 0 ? cols - 1 : aheadX;
        }

        return aheadX;
    }

    private int getAheadY() {
        int aheadY = y;
        if (orientation == Constants.ORIENTATION_DOWN) {
            aheadY++;
            aheadY %= rows;
        } else if (orientation == Constants.ORIENTATION_UP) {
            aheadY--;
            aheadY = aheadY < 0 ? rows - 1 : aheadY;
        }

        return aheadY;
    }

    private void handleMove() {
        x = getAheadX();
        y = getAheadY();

        if (tempMap[y][x]) {
            foodEaten++;
            tempMap[y][x] = false;
        }
    }
}
