package animat;

import java.util.*;

import java.lang.Math;

import dataFrame.DataFrame;
import objects.*;

import neuralNetwork.NeuralNetwork;
import objects.Object;

import objects.Neuron;

public class Animat {
	private int health;
	private int x_pos;
	private int y_pos;
	private final int id;
	private final Action action;
	private List<String[]> run_list;
	private int lifeSpan;
	private final DataFrame df;
	private boolean teacher;
	private ObjectCollection object_map;
	private Boolean has_stone;
	//@SuppressWarnings("FieldCanBeLocal")
	private boolean reached_end;

	private Object current_object;

	private Neuron current_neuron;

	private Object next_object;
	private Stack<Object> path;
	private NeuralNetwork nn;

	private double fitness;

	private Boolean has_resource;

	private List<Neuron> receptiveField;

	public Animat(int x_pos, int y_pos, int id,  ObjectCollection object_map) {
		this.object_map = object_map;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		health = 100;
		this.id = id;
		action = new Action();
		lifeSpan = 0;
		df = new DataFrame();
		reached_end = false;
		path = new Stack<>();
		current_object = object_map.inList(x_pos, y_pos);
		current_neuron = object_map.getNeuron(x_pos, y_pos);
		fitness = 0;
		next_object = new Grass(getX(),getY());
		// Initialize the neural network
		nn = new NeuralNetwork(5, 3, 5, 0.1); // Assuming 5 possible states for the animat and 67 possible environmental states
	}

    public Animat(int id, Action action, DataFrame df, Boolean teacher) {
        this.id = id;
        this.action = action;
        this.df = df;
    }

	public void setTeacher(Boolean teacher) {
		this.teacher = teacher;
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
		// System.out.println("updating moves");
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
		current_object = object_map.inList(x_pos, y_pos);
		assert current_object != null;
		current_neuron = object_map.getNeuron(x_pos, y_pos);
		path.push(current_object);
		// System.out.println("Updated moves");

	}

	public void setObject_map(ObjectCollection object_map) {
		this.object_map = object_map;
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
	 * How an animat moves within a day
	 * Subsumption architecture
	 */
	public void move() {
		// if you're not dead
		if (health > 0 && !reached_end) {
			current_object = object_map.inList(x_pos, y_pos);
			current_neuron = object_map.getNeuron(x_pos, y_pos);
			// Fitness function
			evaluateFitness();
			lifeSpan += 1;
			// Pick up the stone
			pickUp();
			// Implement the decision network
			decisionNetwork();
			// move if you're not on the water
			// move if you're on the water and have a stone
			// Implementation of shunting network
			shuntingNetwork();
			// die if you're on the water and don't have a stone
			if(current_object.getClass()== Trap.class) die();
			if(current_object.getClass() == Water.class && has_stone == null)die();
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
		health = 0;
		//fitness = 100;
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
		Object currentObject = object_map.inList(x_pos, y_pos);
		// removes the need to loop over all the objects
		// if the object is of the same class as the current object
		// System.out.println("Current object: "+currentObject.getClass());
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
		double[] inputs = new double[5];
		// Represents reachable states from current state
		// Getting the neighbouring objects
		double is_grass = 0;
		double carrying = 0;
		double is_resource = 0;
		double is_water = 0;
		double is_trap = 0;
		if(current_object.getClass() == Water.class) is_water = 1;
		if(current_object.getClass() == Grass.class) is_grass = 1;
		if (hasStone() != null || has_resource != null) carrying = 1;
		if (current_object.getClass() == Resource.class) is_resource = 1;
		if (current_object.getClass() == Trap.class) is_trap = 1;
		inputs[0] = is_grass;
		inputs[1] = is_resource;
		inputs[2] = carrying;
		inputs[3] = is_water;
		inputs[4] = is_trap;
        return inputs;

    }

	 List<Neuron> receptiveField(Object current_object){
		List<Neuron> receptiveField = new ArrayList<>();
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				// Check if the neighbor is within the grid
				if (current_object.x() + i >= 0 && current_object.x() + i < 20 && current_object.y() + j >= 0 && current_object.y() + j < 20) {
					// if the neighbor is within the grid then add the object to the receptive field
					Object obj = object_map.inList(current_object.x() + i, current_object.y() + j);
					// check if the object is of the same type

				}
			}
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
		//System.out.println("Pick up complete: Has stone " + has_stone);
	}

	void shuntingNetwork() {
		// Identify the current state
		if(current_neuron == null) System.out.println("Neuron is null");
		// Set the receptive field of the neuron
		if(current_neuron.getReceptiveField().isEmpty()) current_neuron = object_map.generateReceptiveField(current_neuron);
		// System.out.println("Receptive field "+ current_neuron.getReceptiveField().size());
		// Get the neighbouring objects of the current object
		for(Neuron neuron : current_neuron.getReceptiveField()) {
            assert neuron != null;
            neuron.activate();
		}

		plan(current_neuron.getReceptiveField());
		// System.out.println("Next object: "+next_object);
		// Get the neighbouring objects of the current object
		// Set the receptive field of the neuron
		// Activate the neuron
		if (next_object != null) {
			updateMoves(next_object.x() - getX(), next_object.y() - getY());
			return;
		}
		// if the next_object is null, then the animat should wonder
		wonder();

	}

	/**
	 * Decision network
	 */
	 void decisionNetwork() {
		// Set the inputs to the neural network

		// Input iota values
		// If the object is moveable, set the Iota value to 1 Environment type is boolean
		// Get the output values
		double[] outputValues = nn.feedForward(generateInputs());
		// The output values for all objects in the environment
		 setIota(outputValues[1], Resource.class);
		 setIota(outputValues[2], Stone.class);
		 setIota(outputValues[3], Water.class);
		 setIota(outputValues[4], Trap.class);

		// System.out.println("Decision network complete");
	}

	void setIota(double outputValues, Class object){
		if(current_object != null && current_object.getClass() != Grass.class){
			if (outputValues > 0.3) {
				// Set the Iota value to +15 or pick up the object
				object_map.setIota(object, 15, current_object.x(), current_object.y());
			} else if (outputValues < -0.3) {
				// Set the Iota value to -15 or put down the object
				object_map.setIota(object, -15, current_object.x(), current_object.y());
			} else {
				// Set the Iota value of objects of the same type to 0
				object_map.setIota(object, 0, current_object.x(), current_object.y());
			}
		} else if (current_object != null && current_object.getClass() == Grass.class ) {
			current_object.setIota(0);
		}
	}

	private void wonder(){
		 Random rand = new Random();
		 int x = rand.nextInt(3) - 1;
		 int y = rand.nextInt(3) - 1;
		 updateMoves(x, y);
	}

	boolean isInBounds(Object current) {
		// Implement this method to check if the cell (x, y) is within the bounds of the environment.
        return current.x() >= 0 && current.x() <= 20 && current.y() >= 0 && current.y() <= 20;
    }
	void plan(List<Neuron> neighbourhood){
		// System.out.println("Planning");
		double highest_value = -Double.MAX_VALUE;
		Object highest_object = null;
		for(Neuron neuron: neighbourhood){
			// if(neuron.getObject() == null) neuron.setObject(object_map.inList(neuron.getX(), neuron.getY()));
			if(neuron.getObject().getClass() == Resource.class) {
				next_object = neuron.getObject();
				return;
			}
			// if the neuron value is greater than the highest value and the object is not in the path
			if(neuron.getCurrentValue() > highest_value && path.search(neuron.getObject()) == -1){
				highest_value = neuron.getCurrentValue();
				highest_object = neuron.getObject();
			}
		}
		if(highest_object == null) {
			next_object = null;
			return;
		}
		next_object = highest_object;
		// System.out.println("Plan completed Next object: "+next_object.x()+", "+next_object.y());
	}
	public Boolean getReached_end() {
		 // System.out.println("Reached end: "+reached_end);
		return reached_end;
	}

	public ObjectCollection map() {
		return object_map;
	}

	/**
	 * Evaluate the fitness function of the animat
	 */
	private void evaluateFitness(){
		//if(reached_end) addFitness();
		// if the animat has reached the end
		if(hasReachedEnd()) {
			reachedEnd();
		}
		// if the animat should die
		if(shouldDie()) {
			die();
		}


	}

	public void fitness(){
		System.out.println(id+": "+fitness);
	}

	public double getFitness(){
		return this.fitness;
	}

	public void mutate(){
		nn.setMutationRate(0.5);
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

	private boolean shouldDie() {
		return reached_end && current_object.getClass() == Resource.class;
	}

	/**
	 * Check if the animat has reached the end
	 * @return boolean
	 *
	 **/
	public boolean hasReachedEnd() {
		if(has_resource != null) {
			if (has_resource) {
				if (current_object.getClass() == Resource.class) {
					fitness = 1;
					return true;
				}
			}
		}
		return has_resource != null;
	}

	private void reachedEnd() {
		reached_end = true;
	}

	public Stack<Object> getStack() { return path;
	}

}
