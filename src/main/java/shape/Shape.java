package shape;

public abstract class Shape implements Drawable{

	private int x_pos;
	private int y_pos;


	public Shape(int x_pos, int y_pos) {
		this.x_pos = x_pos;
		this.y_pos = y_pos;
	}


	/**
	 *
	 * @return current x position
	 */
	public Integer getX() {
		return this.x_pos;
	}

	/**
	 *
	 * @return current y position
	 */
	public Integer getY() {
		return this.y_pos;
	}

	public Integer getW() {
		return Drawable.W;
	}

	public Integer getH() {
		return Drawable.H;
	}
}
