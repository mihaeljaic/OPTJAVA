package hr.fer.zemris.optjava.dz13;

import javax.swing.*;
import java.awt.*;

public class Tile extends JLabel {

    public Tile(char tyleType) {
        this(tyleType, null);
    }

    public Tile(char tyleType, ImageIcon icon) {
        Color background;
        setOpaque(true);
        if (tyleType == Constants.GRASS) {
            background = Color.GREEN;
        } else if (tyleType == Constants.PATH) {
            background = Color.ORANGE;
        } else if (tyleType == Constants.FOOD){
            background = Color.MAGENTA;
        } else {
            throw new IllegalArgumentException("Unknown tile type.");
        }

        setBackground(background);
        if (icon != null) setIcon(icon);
    }

}
