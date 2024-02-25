package objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grass extends Object{
    public Grass(int x_pos, int y_pos) {
        super(x_pos, y_pos);
    }

    @Override
    public Color getColor() {
        if(iota == 0)return Color.GREEN;
        if(iota < 0 )return Color.RED;
        else return Color.BLUE;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void move(int x, int y) {

    }
}
