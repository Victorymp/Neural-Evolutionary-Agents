package objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Path extends Object{
    /**
     * @param x_pos
     * @param y_pos
     */
    public Path(int x_pos, int y_pos) {
        super(x_pos, y_pos);
    }

    @Override
    public javafx.scene.paint.Paint getColor() {
        return Color.BLACK;
    }

    public Path convertToPath(Object object){
        return new Path(object.getX(), object.getY());
    }
}
