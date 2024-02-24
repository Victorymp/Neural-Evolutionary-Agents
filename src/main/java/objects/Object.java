package objects;

import java.util.ArrayList;

import shape.Drawable;

public abstract class Object implements Drawable{
	// protected Boolean traversable;
	protected Boolean on_top;
	protected ArrayList<Integer> location;
	protected Boolean lethal;
	/**
	 * Determines if item is lethal
	 */
	protected Integer attack;
	// protected Integer health;
	protected Integer x_pos;
	protected Integer y_pos;
	protected String colour;

/**
 * 
 * @param x_pos
 * @param y_pos
 */
	public Object(int x_pos, int y_pos) {
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.location = setLocation(x_pos,y_pos);
		this.attack = 100;
		this.on_top = false;
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

	/**
	 *
	 * @param x_pos
	 *
	 */
	public void setX(int x_pos) {
		this.x_pos = x_pos;
	}


	/**
	 *
	 * @param y_pos
	 */
	public void setY(int y_pos) {
		this.y_pos = y_pos;
	}
	/**
	 *
	 * @return new location doesn't update the current location
	 */
	private void setLocation(){
		location.add(x_pos);
		location.add(y_pos);
	}
	/**
	 *
	 * @param x_pos
	 * @param y_pos
	 * @return new list and updates the old one
	 */
	public ArrayList<Integer> setLocation(int x_pos,int y_pos){
		ArrayList<Integer> lc = new ArrayList<>();
		lc.add(x_pos);
		lc.add(y_pos);
		return lc;
	}

	/**
	 * May need to change to a java matrix
	 * @author mpoko
	 * @return current location as a list
	 */
	public ArrayList<Integer> getLocation(){
		if (location.isEmpty()){
			setLocation();
		}
		return location;
	}

	/**
	 * Keeping track of stacked object
	 */
	public void setOnTopTrue() {
		on_top = true;
	}

	/**
	 * Removing a object
	 */
	public void setOnTopFalse() {
		on_top = false;
	}

}
