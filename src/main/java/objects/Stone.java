package objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Stone extends Object {
	public double moveable = 1;
	public final int carry = 1;
	public Stone(int x_pos, int y_pos) {
		super(x_pos, y_pos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub

	}

	public Color getColor() {
		if(iota == 0)return Color.GREY;
		if(iota < 0 )return Color.RED;
		else return Color.YELLOW;
	}


}
