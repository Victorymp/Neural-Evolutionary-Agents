package animat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import java.lang.Math;

import dataFrame.DataFrame;
import objects.*;

import neuralNetwork.NeuralNetwork;
import objects.Object;

public class Animat {
	private int health;
	private int x_pos;
	private int y_pos;
	private final Integer[] locations;
	private final HashMap<Action, Integer[]> runs;
	private final int id;
	private final Action action;
	private List<String[]> run_list;
	private int lifeSpan;
	private final DataFrame df;
	private final Boolean teacher;
	private ObjectCollection object_map_location;
	private Boolean has_stone;
	@SuppressWarnings("FieldCanBeLocal")
	private Boolean reached_end;

	private Object current_object;

	private Object next_object;
	private Stack<Object> path;
	private NeuralNetwork nn;

	private String[] inputs;

	private double fitness;

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
		current_object = object_map_location.inList(x_pos, y_pos);
		fitness = 0;
		next_object = new Grass(getX(),getY());
		// Initialize the neural network
		nn = new NeuralNetwork(4, 3, 4, 0.1); // Assuming 5 possible states for the animat and 67 possible environmental states
	}

	@Override
	public String toString() {
		return ("Animat: " + getId() + " x pos: " + getX() + " y pos: " + getY());
	}

	/**
	 * Updates location and everything used to move
	 */
	public void updateMoves(int x, int y) {
		// if the move is within the bounds of the map.map
		if(x_pos + x > 0|| x_pos + x < 20){
			if(y_pos + y > 0|| y_pos + y < 20){
				x_pos += x;
				y_pos += y;
			}
		} else {
			System.out.println("x pos: "+x_pos);
		}
		if(x_pos<0) x_pos = 0;
		if(x_pos>20) x_pos = 20;
		if(y_pos<0) y_pos = 0;
		if(y_pos>20) y_pos = 20;

		current_object = object_map_location.inList(x_pos, y_pos);
		updateLocation();
		run();
		this.getTeachers();
		generateInputs();
		path.push(object_map_location.inStack(x_pos, y_pos));
	}

	public void setObject_map_location(ObjectCollection object_map_location) {
		this.object_map_location = object_map_location;
	}


	public int getId() {
		return id;
	}

	public int getX() {
		return x_pos;
	}

	public int getY() {
		return y_pos;
	}

	/*
	 * How it moves within a day
	 * Decision network
	 */
	public void move() {
		// if you're not dead
		if (health > 0) {
			// can I pick up the stone
			pickUpStone();
			// move if you're not on the water
			// Implementation of shunting netwok
			if (y_pos != 2) shuntingNetwork();
				// move if you're on the water and have a stone
			else if (has_stone != null) shuntingNetwork();
				// die if you're on the water and dont have a stone
			else die();
		}
	}


	public String[] getDay(Boolean x) {
		if (!x) {
			return new String[]{"" + id, "" + lifeSpan, "" + x_pos, "" + y_pos, "" + teacher};
		} else {
			return new String[]{"" + id, "" + lifeSpan, "" + x_pos, "" + y_pos, "" + teacher, "" + reached_end};
		}
	}


	public String[] getRuns() {
		String[] t = {"Animat: " + this.id, "Action:" + action.getAc()};
		return t;
	}

	public String[] getAnimat() {
		return new String[]{"" + id, "" + lifeSpan, "" + teacher, "" + reached_end};
	}

	public Boolean getDeath() {
		return health <= 0;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void die() {
		health -= 100;
	}

	public Boolean getTeacher() {
		return teacher;
	}


	/**
	 * Gets the teachers from the memory
	 */
	private void getTeachers() {
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
	 *
	 * @param objectCollection
	 * @return returns array of the location of the closes stone
	 */
	public ArrayList<Double> locateStone(ObjectCollection objectCollection) {
		Stack<Object> temp = objectCollection.getObjects(Stone.class);
		ArrayList<Double> temp_loc = new ArrayList<>();
		for (Object i : temp) {
			//distance formula
			Double distance_to_nearest_stone = Math.sqrt(Math.pow((getX() - i.getX()), 2) + Math.pow((getY() - i.getY()), 2));
			temp_loc.add(distance_to_nearest_stone);
		}
		return temp_loc;
	}

	/**
	 * Distance to the closest object
	 *
	 * @param aClass
	 * @param objectCollection
	 * @return Distances
	 */
	public double distanceToObject(Class aClass, ObjectCollection objectCollection) {
		Stack<Object> temp = objectCollection.getObjects(aClass);
		ArrayList<Double> temp_loc = new ArrayList<>();
		double count = 0.0;
		// closest stone to the start
		double prev = distance(getX(), getY(), 9, 13);
		double min = distance(getX(), getY(), 9, 13);
		for (Object i : temp) {
			//distance formula
			double distance_to_nearest_stone = Math.sqrt(Math.pow((getX() - i.getX()), 2) + Math.pow((getY() - i.getY()), 2));
			if (distance_to_nearest_stone < prev) {
				min = distance_to_nearest_stone;
			}
			temp_loc.add(distance_to_nearest_stone);
			prev = distance_to_nearest_stone;
		}
		return min;
	}

	private Double distance(int x_first, int y_first, int x_second, int y_second) {
		return Math.sqrt(Math.pow((x_second - x_first), 2) + Math.pow((y_second - y_first), 2));
	}

	/**
	 * Generates neural network inputs.
	 * Inputs are current facts and this is the process of getting the inputs
	 */
	public double[] generateInputs() {
		double[] inputs = new double[4];
		// Represents reachable states from current state
		// Getting the neighbouring objects
		double is_grass = 0;
		double has_stone = 0;
		double is_resource = 0;
		double is_water = 0;
		if(current_object.getClass() == Water.class) is_water = 1;
		if(current_object.getClass() == Grass.class) is_grass = 1;
		if (hasStone() != null) has_stone = 1;
		if (current_object.getClass() == Resource.class) is_resource = 1;
		inputs[0] = is_grass;
		inputs[1] = is_resource;
		inputs[2] = has_stone;
		inputs[3] = is_water;
		return inputs;

	}

	public ArrayList<Object> receptiveField(){
		ArrayList<Object> receptiveField = new ArrayList<>();
		// Getting the neighbouring objects
		receptiveField.add(object_map_location.inList(current_object.getX(), (current_object.getY()+1)));
		receptiveField.add(object_map_location.inList((current_object.getX()+1), (current_object.getY()+1)));
		receptiveField.add(object_map_location.inList((current_object.getX()+1), (current_object.getY())));
		receptiveField.add(object_map_location.inList((current_object.getX()+1), (current_object.getY()-1)));
		receptiveField.add(object_map_location.inList((current_object.getX()), (current_object.getY()-1)));
		receptiveField.add(object_map_location.inList((current_object.getX()-1), (current_object.getY()-1)));
		receptiveField.add(object_map_location.inList((current_object.getX()-1), (current_object.getY())));
		receptiveField.add(object_map_location.inList((current_object.getX()-1), (current_object.getY()+1)));
		for (Object i: receptiveField){
			System.out.println(i);
		}
		return receptiveField;
	}


	/**
	 * Picks up the stone
	 */
	private void pickUpStone() {
		for (Double i : locateStone(object_map_location)) {
			if (i == 0) {
				//pick up the stone
				has_stone = true;
				//remove the stone from the map.map
				return;
			}
		}
	}

	private void shuntingNetwork() {

		if (has_stone != null && y_pos <= 2) {
			return;
		}
		Random r = new Random();
		// current object
		current_object = object_map_location.inList(getX(), getY());
		ArrayList<Object> neighbourhood = object_map_location.getNeighborhood(getX(), getY());
		decisionNetwork();
		for(Object i: neighbourhood){
			if(!path.contains(i) && isInBounds(i)){
				next_object = i;
				path.push(next_object);
			}
		}
		lifeSpan += 1;
		// if the next object is not in the path
		if(next_object.getIota() > current_object.getIota()){
			// update move to the next object
			System.out.println("Next object: "+next_object);
			updateMoves(next_object.x() - getX(), next_object.y() - getY());
			return;
		}
		System.out.println("Current object: "+current_object);
		int rand = r.nextInt(3);
		switch (rand) {
			case 0:
				action.setAc("back");
				updateMoves(0, -1);
				break;
			case 1:
				action.setAc("left");
				updateMoves(-1, 0);
				break;
			case 2:
				action.setAc("right");
				updateMoves(1, 0);
				break;
			case 3:
				action.setAc("forward");
				updateMoves(0, 1);
				break;
		}
	}

	/**
	 * Decision network
	 */
	public void decisionNetwork() {
		double iota;
		current_object = object_map_location.inList(getX(), getY());
		// Set the inputs to the neural network

		// Input iota values
		// If the object is moveable, set the Iota value to 1 Environment type is boolean
		if (current_object.ENVIRONMENT_TYPE) {
			iota = 1;
		} else {
			iota = -1;
		}
		// Get the output values
		double[] outputValues = nn.feedForward(generateInputs(),iota);
		setIota(outputValues[1], Grass.class);
		setIota(outputValues[2], Resource.class);
		setIota(outputValues[3], Water.class);
		// Using the neural network output values, set the Iota values and determine the pick-up/put-down actions
		// Based on the output values, set the Iota values and determine the pick-up/put-down actions
//		for (int i = 0; i < outputValues.length; i++) {
//			if(current_object != null && current_object.getClass() != Grass.class){
//				if (outputValues[i] > 0.3) {
//					// Set the Iota value to +15 or pick up the object
//					object_map_location.setIota(current_object, 15, current_object.x(), current_object.y());
//				} else if (outputValues[i] < -0.3) {
//					// Set the Iota value to -15 or put down the object
//					object_map_location.setIota(current_object, -15, current_object.x(), current_object.y());
//				} else {
//					// Set the Iota value of objects of the same type to 0
//					object_map_location.setIota(current_object, 0, current_object.x(), current_object.y());
//				}
//			} else if (current_object != null && current_object.getClass() == Grass.class ) {
//				current_object.setIota(0);
//			}
//		}
	}

	private void setIota(double outputValues, Class object){
		if(current_object != null && current_object.getClass() != Grass.class){
			if (outputValues > 0.3) {
				// Set the Iota value to +15 or pick up the object
				object_map_location.setIota(object, 15, current_object.x(), current_object.y());
			} else if (outputValues < -0.3) {
				// Set the Iota value to -15 or put down the object
				object_map_location.setIota(object, -15, current_object.x(), current_object.y());
			} else {
				// Set the Iota value of objects of the same type to 0
				object_map_location.setIota(object, 0, current_object.x(), current_object.y());
			}
		} else if (current_object != null && current_object.getClass() == Grass.class ) {
			current_object.setIota(0);
		}
	}



	private boolean isInBounds(Object current) {
		// Implement this method to check if the cell (x, y) is within the bounds of the environment.
		if(current.x() < 0 || current.x() > 20 || current.y() < 0 || current.y() > 20){
			return false;
		} return true;
	}
	public Boolean getReached_end() {
		return reached_end;
	}

	public ObjectCollection map() {
		// TODO Auto-generated method stub
		return object_map_location;
	}

	public void addFitness(){
		this.fitness += 1;
	}

	public void fitness(){
		System.out.println(id+": "+fitness);
	}

	public void mutate(){
		nn.mutate();
	}
}
