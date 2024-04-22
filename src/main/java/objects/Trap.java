package objects;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

public class Trap extends Object{
    /**
     * @param x_pos
     * @param y_pos
     */
    public Trap(int x_pos, int y_pos) {
        super(x_pos, y_pos);
    }

    /**
     * @param gc
     */
    @Override
    public void draw(GraphicsContext gc) {

    }

    /**
     * @param x
     * @param y
     */
    @Override
    public void move(int x, int y) {
    }

    @Override
    public javafx.scene.paint.Paint getColor() {
        return Color.BLACK;
    }
}
