package hr.fer.zemris.optjava.dz13;

import javax.swing.*;
import java.awt.*;

public class TileLayout implements LayoutManager2 {
    private int rowsCount;
    private int colsCount;
    private Component[][] components;
    private int rowSize;
    private int columnSize;

    public TileLayout(int rowsCount, int colsCount) {
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;

        components = new JComponent[rowsCount][colsCount];
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        components[rowSize][columnSize] = comp;

        columnSize++;
        if (columnSize == colsCount) {
            columnSize = 0;
            rowSize++;
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(target.getWidth(), target.getHeight());
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(parent.getWidth(), parent.getHeight());
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(parent.getWidth(), parent.getHeight());
    }

    @Override
    public void layoutContainer(Container parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();

        int[] componentWidths = new int[colsCount];
        int[] componentHeights = new int[rowsCount];

        int averageWidth = width / colsCount;
        int averageHeight = height / rowsCount;
        int widthSum = colsCount * averageWidth;
        int heightSum = rowsCount * averageHeight;

        for (int i = 0; i < colsCount; i++) {
            componentWidths[i] = averageWidth;
        }
        for (int i = 0; i < rowsCount; i++) {
            componentHeights[i] = averageHeight;
        }

        int index = 0;
        while (widthSum < width) {
            componentWidths[index++]++;
            index %= colsCount;
            widthSum++;
        }

        index = 0;
        while (heightSum < height) {
            componentHeights[index++]++;
            index %= rowsCount;
            heightSum++;
        }

        int x = 0;
        int y = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                if (components[i][j] == null) continue;
                components[i][j].setBounds(x, y, componentWidths[j], componentHeights[i]);
                x += componentWidths[j];
            }
            y += componentHeights[i];
            x = 0;
        }
    }
}
