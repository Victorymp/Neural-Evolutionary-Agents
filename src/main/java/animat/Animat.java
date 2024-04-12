package animat;

import java.util.*;

import java.lang.Math;

import dataFrame.DataFrame;
import objects.*;

import neuralNetwork.NeuralNetwork;
import objects.Object;

public class Animat {
	private int health;
	private int x_pos;
	private int y_pos;
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

	private Boolean has_resource;

	public Animat(int x_pos, int y_pos, int id, boolean teachers, ObjectCollection object_map_location) {
		this.object_map_location = object_map_location;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		health = 100;
		this.id = id;
		action = new Action();
		lifeSpan = 0;
		df = new DataFrame();
		this.teacher = (Boolean) teachers;
		reached_end = (Boolean) false;
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
		path.push(current_object);
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
		if (health > 0 && !reached_end) {
			evaluateFitness();
			lifeSpan += 1;
			pickUp();
			//putDown();
			// move if you're not on the water
			// Implementation of shunting netwok
			if (y_pos != 2) shuntingNetwork();
			// move if you're on the water and have a stone
			else if (has_stone != null) shuntingNetwork();
			// die if you're on the water and dont have a stone
			else die();
		}
	}

	private void putDown() {
		if (has_stone != null && current_object.getClass() == Water.class) {
			has_stone = null;
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
		return (health <= 0);
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void die() {
		health =0;
	}

	private Boolean hasStone() {
		return has_stone;
	}

	//loacte stone

	/**
	 * Get the class of the object at the current location
	 * @param aClass
	 * @return
	 */
	private Object getClass(Class aClass) {
		Object currentObject = object_map_location.inList(x_pos, y_pos);
		// removes the need to loop over all the objects
		// if the object is of the same class as the current object
		if (aClass.isInstance(currentObject)) {
			return currentObject;
		}
		return null;
	}


	private Double distance(int x_first, int y_first, int x_second, int y_second) {
		return Math.sqrt(Math.pow((x_second - x_first), 2) + Math.pow((y_second - y_first), 2));
	}

	/**
	 * Generates neural network inputs.
	 * Inputs are current facts that are present in the same location as it and this is the process of getting the inputs
	 * With inputs being 1 or 0
	 */
	private double[] generateInputs() {
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

	 ArrayList<Object> receptiveField(){
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
	void pickUp() {
		// if the animat is on the stone
		// if the animat is on the resource
		if(getClass(Stone.class) != null) has_stone = true;
		if(getClass(Resource.class) != null) has_resource =true;
	}

	void shuntingNetwork() {
		// if the animat is on the water and does not have a stone
		if (shouldDie()) {
			die();
			return;
		}

		if (hasReachedEnd()) {
			reachedEnd();
			return;
		}
		Random r = new Random();
		// current object
		current_object = object_map_location.inList(getX(), getY());
		ArrayList<Object> neighbourhood = object_map_location.getNeighborhood(current_object.getX(),current_object.getY());
		decisionNetwork();
		plan(neighbourhood);

		int rand = r.nextInt(4);
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
	 void decisionNetwork() {
		current_object = object_map_location.inList(getX(), getY());
		// Set the inputs to the neural network

		// Input iota values
		// If the object is moveable, set the Iota value to 1 Environment type is boolean
		// Get the output values
		double[] outputValues = nn.feedForward(generateInputs());
		setIota(outputValues[1], Grass.class);
		setIota(outputValues[2], Resource.class);
		setIota(outputValues[3], Water.class);
	}

	void setIota(double outputValues, Class object){
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

	boolean isInBounds(Object current) {
		// Implement this method to check if the cell (x, y) is within the bounds of the environment.
		if(current.x() < 0 || current.x() > 20 || current.y() < 0 || current.y() > 20){
			return false;
		} return true;
	}
	void plan(ArrayList<Object> neighbourhood){
		double start_route = distance(getX(), getY(), next_object.x(), next_object.y());
		for(Object i: neighbourhood){
			if(isInBounds(i)){
				// if the object is a resource goto the object
				if(i.getClass() == Resource.class){
					next_object = i;
					return;
				}
				double route = distance(getX(), getY(), i.x(), i.y());
				// if the route is less than the start route and not going back
				if(route < start_route && !path.contains(i) ){
					// if has stone and the object is a stone it cant be next object
					if(has_stone != null && i.getClass() == Stone.class) {
						continue;
					}else {
						next_object = i;
					}
				}
			}
		}
	}
	public Boolean getReached_end() {
		return reached_end;
	}

	public ObjectCollection map() {
		return object_map_location;
	}
	private void evaluateFitness(){
		if(has_stone == null) addFitness();
		if(!reached_end) addFitness();

	}

	public void addFitness(){
		this.fitness += 1;
	}

	public void fitness(){
		System.out.println(id+": "+fitness);
	}

	public double getFitness(){
		return this.fitness;
	}

	public void mutate(){
		nn.setMutationRate(5);
		nn.mutate();
	}

	public NeuralNetwork getNeuralNetwork(){
		return nn;
	}

	public void setNeuralNetwork(NeuralNetwork nn){
		this.nn = nn;
	}

	public void crossOver(Animat a, Animat b){
		nn.crossOver(a.getNeuralNetwork(), b.getNeuralNetwork());
	}

	public NeuralNetwork getNeuralNetwork(Animat a){
		return a.getNeuralNetwork();
	}

	private boolean shouldDie() {
		return has_stone == null && y_pos <= 2;
	}

	/**
	 * Check if the animat has reached the end
	 * @return boolean
	 *
	 **/
	private boolean hasReachedEnd() {
		return has_resource != null;
	}

	private void reachedEnd() {
		reached_end = true;
		die();
	}

	public Stack<Object> getStack() { return path;
	}
}
