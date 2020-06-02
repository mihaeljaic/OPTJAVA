package hr.fer.zemris.optjava.dz13;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapModel {
    private List<List<Tile>> tiles;
    private int width;
    private int height;
    private int antOrientation;
    private int antX;
    private int antY;
    private Image antRight;
    private Image antLeft;
    private Image antDown;
    private Image antUp;

    private boolean[][] map;

    public MapModel(List<List<Tile>> tiles, int antOrientation, int antX, int antY, Image antRight, Image antLeft, Image antDown, Image antUp, boolean[][] map) {
        this.tiles = tiles;
        this.antOrientation = antOrientation;
        this.antX = antX;
        this.antY = antY;
        this.antRight = antRight;
        this.antLeft = antLeft;
        this.antDown = antDown;
        this.antUp = antUp;

        height = tiles.size();
        width = tiles.get(0).size();
        this.map = map;
    }

    public void moveAnt() {
        tiles.get(antY).get(antX).setIcon(null);
        tiles.get(antY).get(antX).revalidate();

        Image ant;
        if (antOrientation == Constants.ORIENTATION_LEFT) {
            antX = antX == 0 ? width - 1 : antX - 1;
            ant = antLeft;
        } else if (antOrientation == Constants.ORIENTATION_UP) {
            antY = antY == 0 ? height - 1 : antY - 1;
            ant = antUp;
        } else if (antOrientation == Constants.ORIENTATION_RIGHT) {
            antX = (antX + 1) % width;
            ant = antRight;
        } else {
            antY = (antY + 1) % height;
            ant = antDown;
        }

        map[antY][antX] = false;
        JLabel antTile = tiles.get(antY).get(antX);
        antTile.setIcon(Util.getScaledImage(ant, antTile.getWidth(), antTile.getHeight()));
        antTile.revalidate();
    }

    public void rotateAntLeft() {
        rotate(Constants.ORIENTATION_LEFT);
    }

    public void rotateAntRight() {
        rotate(Constants.ORIENTATION_RIGHT);
    }

    private void rotate(int rotation) {
        Image ant;
        if (rotation == Constants.ORIENTATION_LEFT && antOrientation == Constants.ORIENTATION_LEFT || rotation == Constants.ORIENTATION_RIGHT && antOrientation == Constants.ORIENTATION_RIGHT) {
            antOrientation = Constants.ORIENTATION_DOWN;
            ant = antDown;
        } else if (rotation == Constants.ORIENTATION_LEFT && antOrientation == Constants.ORIENTATION_RIGHT || rotation == Constants.ORIENTATION_RIGHT && antOrientation == Constants.ORIENTATION_LEFT) {
            antOrientation = Constants.ORIENTATION_UP;
            ant = antUp;
        } else if (rotation == Constants.ORIENTATION_LEFT && antOrientation == Constants.ORIENTATION_DOWN || rotation == Constants.ORIENTATION_RIGHT && antOrientation == Constants.ORIENTATION_UP) {
            antOrientation = Constants.ORIENTATION_RIGHT;
            ant = antRight;
        } else {
            antOrientation = Constants.ORIENTATION_LEFT;
            ant = antLeft;
        }

        JLabel antTile = tiles.get(antY).get(antX);
        antTile.setIcon(Util.getScaledImage(ant, antTile.getWidth(), antTile.getHeight()));
        antTile.revalidate();
    }

    public boolean isFoodAhead() {
        int tempX = antX;
        int tempY = antY;
        if (antOrientation == Constants.ORIENTATION_RIGHT) {
            tempX = (tempX + 1) % width;
        } else if (antOrientation == Constants.ORIENTATION_LEFT) {
            tempX = tempX - 1 < 0 ? width - 1 : tempX - 1;
        } else if (antOrientation == Constants.ORIENTATION_DOWN) {
            tempY = (tempY + 1) % height;
        } else {
            tempY = tempY - 1 < 0 ? height - 1 : tempY - 1;
        }

        return map[tempY][tempX];
    }
}
