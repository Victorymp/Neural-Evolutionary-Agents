package objects;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

import javafx.scene.paint.Color;

public class Stone extends Object {
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

	@Override
	public javafx.scene.paint.Paint getColor() {
		if(iota == 0)return Color.GRAY;
		if(iota < 0 )return Color.RED;
		else return Color.YELLOW;
	}



}
