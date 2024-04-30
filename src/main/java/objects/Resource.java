package objects;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

import javafx.scene.paint.Color;

public class Resource extends Object {
    public Resource(int x_pos, int y_pos) {
        super(x_pos, y_pos);
    }



    @Override
    public javafx.scene.paint.Paint getColor() {
        if(iota == 0)return Color.ORANGE;
        if(iota < 0 )return Color.RED;
        else return Color.YELLOW;
    }

}
