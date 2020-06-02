package hr.fer.zemris.optjava.dz13;

import hr.fer.zemris.optjava.algorithm.FitnessFunction;
import hr.fer.zemris.optjava.algorithm.GeneticProgramming;
import hr.fer.zemris.optjava.algorithm.creators.Grow;
import hr.fer.zemris.optjava.algorithm.creators.IPopulationCreator;
import hr.fer.zemris.optjava.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.algorithm.crossover.ReplaceSubTree;
import hr.fer.zemris.optjava.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.algorithm.mutation.RandomSubtree;
import hr.fer.zemris.optjava.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.algorithm.selection.TournamentSelection;
import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AntTrailGA extends JFrame {
    private static String mapPath;
    private static int maxGenerations;
    private static int populationSize;
    private static int targetFitness;
    private static String solutionPath;
    private static int actionsCount = 600;
    public static final int minNodes = 150;
    public static final int maxNodes = 400;

    private Node solution;
    private MapModel mapModel;
    private Image antRight;
    private Image antDown;
    private Image antLeft;
    private Image antUp;
    private Image cake;

    private Stack<StackElement> stack;
    private static boolean[][] map;

    public AntTrailGA(Node solution) {
        this.solution = solution;
        stack = new Stack<>();
        stack.push(new StackElement(solution, 0));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Santa Fe Ant Trail");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initializeGUI();

        setVisible(true);
    }

    private void initializeGUI() {
        loadIcons();
        loadMap();

        JButton next = new JButton("Next");
        next.addActionListener(l -> {
            nextTerminal();

            actionsCount--;
            if (actionsCount == 0 || stack.isEmpty()) {
                next.setEnabled(false);
            }
        });

        add(next, BorderLayout.NORTH);
    }

    private void nextTerminal() {
        while (!stack.isEmpty()) {
            StackElement node = stack.peek();
            int program = node.node.getProgram();
            if (program >= 3) {
                if (program == Constants.MOVE) {
                    mapModel.moveAnt();
                } else if (program == Constants.LEFT) {
                    mapModel.rotateAntLeft();
                } else {
                    mapModel.rotateAntRight();
                }

                stack.pop();
                while (!stack.isEmpty()) {
                    StackElement el = stack.peek();
                    if (el.node.getProgram() == Constants.PROG_3 && el.childIndex < 2 || el.node.getProgram() != Constants.PROG_3 && el.childIndex < 1) {
                        el.childIndex++;
                        return;
                    }

                    stack.pop();
                }

                return;
            }

            if (program == Constants.IF_FOOD_AHEAD) {
                stack.pop();
                stack.push(new StackElement(node.node.getChildren().get(mapModel.isFoodAhead() ? 0 : 1), 0));
            } else {
                stack.push(new StackElement(node.node.getChildren().get(node.childIndex), 0));
            }
        }

    }

    private static class StackElement {
        private Node node;
        private int childIndex;

        public StackElement(Node node, int childIndex) {
            this.node = node;
            this.childIndex = childIndex;
        }
    }

    private void loadIcons() {
        try {
            antRight = ImageIO.read(new File(Constants.antRightIcon));
            antDown = ImageIO.read(new File(Constants.antDownIcon));
            antLeft = ImageIO.read(new File(Constants.antLeftIcon));
            antUp = ImageIO.read(new File(Constants.antUpIcon));
            cake = ImageIO.read(new File(Constants.cakeIcon));
        } catch (IOException ex) {
            System.out.println("Unable to load icons.");
            System.exit(1);
        }
    }

    private void loadMap() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(mapPath), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Unable to load map.");
            System.exit(1);
        }

        String[] tmp = lines.get(0).split("x|X");
        int width = Integer.parseInt(tmp[0]);
        int height = Integer.parseInt(tmp[1]);

        JPanel mapPanel = new JPanel(new TileLayout(height, width));
        List<List<Tile>> map = new ArrayList<>(height);
        final int tileWidth = getWidth() / width;
        final int tileHeight = getHeight() / height;
        for (int i = 1; i < lines.size(); i++) {
            List<Tile> row = new ArrayList<>(width);
            for (char c : lines.get(i).toCharArray()) {
                Tile tile = new Tile(c);
                if (c == Constants.FOOD) tile.setIcon(Util.getScaledImage(cake, tileWidth, tileHeight));
                mapPanel.add(tile);
                row.add(tile);
            }
            if (row.size() != width) {
                throw new RuntimeException("Invalid number of cols.");
            }
            map.add(row);
        }

        if (map.size() != height) {
            throw new RuntimeException("Invalid number of rows.");
        }

        JLabel antPosition = map.get(0).get(0);
        antPosition.setIcon(Util.getScaledImage(antRight, tileWidth, tileHeight));
        antPosition.revalidate();
        mapModel = new MapModel(map, Constants.ORIENTATION_RIGHT, 0, 0, antRight, antLeft, antDown, antUp, this.map);

        add(mapPanel, BorderLayout.CENTER);
    }

    public static void main(final String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Expected 5 arguments.");
            return;
        }

        mapPath = args[0];
        maxGenerations = Integer.parseInt(args[1]);
        populationSize = Integer.parseInt(args[2]);
        targetFitness = Integer.parseInt(args[3]);
        solutionPath = args[4];

        FitnessFunction function = parseMap(Files.readAllLines(Paths.get(mapPath), StandardCharsets.UTF_8));
        IPopulationCreator creator = new Grow(maxNodes, 6, minNodes);
        ICrossover crossover = new ReplaceSubTree();
        IMutation mutation = new RandomSubtree(maxNodes, creator);
        ISelection selection = new TournamentSelection(7);

        GeneticProgramming gp = new GeneticProgramming(function, populationSize, creator, crossover, mutation, selection,
                targetFitness, maxGenerations, 0.14, 0.85, 0.01,
                0.9, maxNodes, 6);
        AntSolution best = gp.run();

        SwingUtilities.invokeLater(() -> new AntTrailGA(best.getRoot()));

        Files.write(Paths.get(solutionPath), best.getRoot().toString().getBytes());
    }

    private static FitnessFunction parseMap(List<String> lines) {
        String first = lines.get(0);
        String[] temp = first.split("x|X");
        int rows = Integer.parseInt(temp[0]);
        int cols = Integer.parseInt(temp[1]);

        map = new boolean[rows][cols];
        for (int i = 1; i < lines.size(); i++) {
            char[] line = lines.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                map[i - 1][j] = line[j] == '1';
            }
        }

        return new FitnessFunction(actionsCount, rows, cols, map);
    }
}
