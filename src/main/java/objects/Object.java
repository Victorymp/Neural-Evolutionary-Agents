package objects;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public abstract class Object {
	// protected Boolean traversable;
	protected Boolean on_top;
	protected ArrayList<Integer> location;
	protected double moveable = 0;
	/**
	 * Determines if item is lethal
	 */
	protected Integer attack;
	// protected Integer health;
	protected Integer x_pos;
	protected Integer y_pos;
	protected double iota; ;

	public Boolean ENVIRONMENT_TYPE;

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
		this.iota = 0;

	}

	public Integer x(){
		return x_pos;
	}

	public Integer y(){
		return y_pos;
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
        for (int i : new int[]{x_pos, y_pos}) {
            lc.add(Integer.valueOf(i));
        }
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

	/**
	 * Get Iota value
	 * @return
	 */
	public double getIota() {
		return iota;
	}

	/**
	 * Set Iota value
	 * @param value
	 */
	public void setIota(double value) {
        this.iota = value;
	}


	public javafx.scene.paint.Paint getColor() {
		if(iota == 0)return Color.GREEN;
		if(iota < 0 )return Color.RED;
		else return Color.YELLOW;
	}
	public void localConnections() {
	}

	public double distance(Object object) {
		// Euclidean distance from current object to another object
		return Math.max(0,Math.sqrt(Math.pow(this.x_pos - object.x_pos, 2) + Math.pow(this.y_pos - object.y_pos, 2)));
	}

}
