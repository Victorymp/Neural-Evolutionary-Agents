package animat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import java.lang.Math;

import dataFrame.DataFrame;
import objects.Object;
import objects.ObjectCollection;
import objects.Stone;

public class Animat {
	private int health;
	private int x_pos;
	private int y_pos;
	private final Integer[] locations;
	private final HashMap<Action, Integer[]> runs;
	private final int id;
	private final Action action;
	private List<String[]> run_list;
	private DataFrame rdf;
	private int lifeSpan;
	private final DataFrame df;
	private final Boolean teacher;
	private ObjectCollection object_map_location;
	private Boolean has_stone;
	@SuppressWarnings("FieldCanBeLocal")
	private Boolean reached_end;

	private Stack<Object> path;

	public Animat(int x_pos, int y_pos, int id, Boolean teachers, ObjectCollection object_map_location) {
		this.object_map_location = object_map_location;
		locations = new Integer[2];
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		locations[0] = x_pos;
		locations[1] = y_pos;
		health = 100;
		this.id = id;
		action = new Action();
		runs = new HashMap<>();
		lifeSpan = 0;
		df = new DataFrame();
		this.teacher = teachers;
		reached_end = false;
		path = new Stack<>();
	}

	@Override
	public String toString() {
		return ("Animat: "+getId()+" x pos: "+getX()+" y pos: "+getY());
	}

	/**
	 * Updates location and everything used to move
	 */
	public void updateMoves(int x, int y) {
		x_pos += x;
		y_pos += y;
		updateLocation();
		run();
		this.getTeachers();
		generateInputs();
		path.push(object_map_location.inStack(x_pos,y_pos));
	}

	public void setObject_map_location(ObjectCollection object_map_location) {
		this.object_map_location = object_map_location;
	}


	public int getId() { return id; }

	public int getX() { return x_pos;}

	public int getY() { return y_pos;}
	/*
	 * How it moves within a day
	 * Decision network
	 */
	public void move() {
		// if you're not dead
		if(health > 0) {
			// can I pick up the stone
			pickUpStone();
			// move if you're not on the water
			// Implementation of shunting netwok
			if (y_pos != 2) moveBlock();
			// move if you're on the water and have a stone
			else if (has_stone != null) moveBlock();
			// die if you're on the water and dont have a stone
			else die();
		}
	}


	public String[] getDay(Boolean x) {
		if (!x) {
            return new String[]{""+id,""+lifeSpan,""+x_pos,""+y_pos,""+teacher};
		} else {
			return new String[]{""+id,""+lifeSpan,""+x_pos,""+y_pos,""+teacher,""+reached_end};
		}
	}


	public String[] getRuns(){
		String[] t = {"Animat: "+this.id,"Action:"+ action.getAc()};
		return t;
	}

	public String[] getAnimat() {return new String[]{""+id,""+lifeSpan,""+teacher,""+reached_end};}

	public Boolean getDeath() {
        return health <= 0;
    }

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void die() {
		health -= 100;}

	public Boolean getTeacher() {
		return teacher;
	}

	public void saveRuns() {
		rdf.saveRun(run_list);
	}

	/**
	 * Gets the teachers from the memory
	 */
	private void getTeachers() {
		//System.out.println(df.openMemory("Runners"));
		//ArrayList<Object> teachers = df.openMemory("Runners");
		df.openMemory("Runners");
	}

	public void run() {
		// mapping action and location together
		runs.put(action.getAc(lifeSpan), locations);
	}

	private void updateLocation() {
		locations[0] = getX();
		locations[1] = getY();
	}

	private Boolean hasStone() {
		return has_stone;
	}

	//loacte stone
	/**
	 * Locates the closes stone
	 * @param objectCollection
	 * @return returns array of the location of the closes stone
	 */
	public ArrayList<Double> locateStone(ObjectCollection objectCollection) {
		Stack<Object> temp = objectCollection.getObjects(Stone.class);
		ArrayList<Double> temp_loc = new ArrayList<>();
		for(Object i: temp) {
			//distance formula
			Double distance_to_nearest_stone = Math.sqrt(Math.pow((getX() - i.getX()),2) + Math.pow((getY() - i.getY()),2));
			temp_loc.add(distance_to_nearest_stone);
		}
		return temp_loc;
	}

	/**
	 * Distance to the closest object
	 * @param aClass
	 * @param objectCollection
	 * @return Distances
	 */
	public double distanceToObject(Class aClass, ObjectCollection objectCollection){
		Stack<Object> temp = objectCollection.getObjects(aClass);
		ArrayList<Double> temp_loc = new ArrayList<>();
		double count = 0.0;
		// closest stone to the start
		double prev = distance(getX(),getY(),9,13);
		double min = distance(getX(),getY(),9,13);
		for(Object i: temp) {
			//distance formula
			double distance_to_nearest_stone = Math.sqrt(Math.pow((getX() - i.getX()),2) + Math.pow((getY() - i.getY()),2));
			if (distance_to_nearest_stone < prev){
				min = distance_to_nearest_stone;
			}
			temp_loc.add(distance_to_nearest_stone);
			prev = distance_to_nearest_stone;
		}
		return min;
	}

	private Double distance(int x_first, int y_first, int x_second, int y_second) {
		return Math.sqrt(Math.pow((x_second - x_first),2) + Math.pow((y_second - y_first),2));
	}

	/**
	 * Generates neural network inputs
	 */
	public String[] generateInputs() {
		//distance to the closest stone
		Double distance_to_nearest_stone = distanceToObject(Stone.class, object_map_location);
		//distance to the closest water
		Double distance_to_water = distance(getX(),getY(),getX(),2);
		//distance to the start
		Double distance_from_start = distance(getX(),getY(),10,19);
		//distance to the end
		Double distance_to_end = distance(getX(),getY(),10,1);
		//has stone
		int has_stone = 0;
		if(hasStone() != null) {
			has_stone = 1;}
		ArrayList<Double> inputs = new ArrayList<>();
		inputs.add(distance_to_nearest_stone);
		inputs.add(distance_to_water);
		inputs.add(distance_from_start);
		inputs.add(distance_to_end);
		inputs.add((double) has_stone);
		return new String[]{"" + id, "" + inputs.get(0), "" + inputs.get(1), "" + inputs.get(2), "" + inputs.get(3), "" + inputs.get(4)};

	}

	/**
	 * Picks up the stone
	 */
	private void pickUpStone() {
		for(Double i: locateStone(object_map_location)){
            if (i == 0) {
                //pick up the stone
                has_stone = true;
                //remove the stone from the map
                return;
            }
		}
	}

	private void moveBlock() {
		if(has_stone != null && y_pos <= 2 ){
			this.reached_end = true;
		}
		Random r = new Random();
		int rand = r.nextInt(3);
		lifeSpan +=1;
		switch (rand) {
			case 0:
				action.setAc("back");
				updateMoves(0,-1);
				break;
			case 1:
				action.setAc("left");
				updateMoves(-1,0);
				break;
			case 2:
				action.setAc("right");
				updateMoves(1,0);
				break;
			case 3:
				action.setAc("forward");
				updateMoves(0,1);
				break;
		}
	}

	public Boolean getReached_end() {
		return reached_end;
	}
}

class StackException extends Exception{
	public StackException(String message) {
		super(message);
	}
}
