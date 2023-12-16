package shape;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
	final int W = 100;
	final int H = 100;

	public abstract void draw (GraphicsContext gc);

	public abstract void move (int x, int y);
}
