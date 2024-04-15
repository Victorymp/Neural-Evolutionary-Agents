package objects;

import animat.Animat;

import java.util.*;

public class ObjectCollection {

	private static final int GRID_SIZE = 20;

	private final Stack<Object> objectStack;
	private final HashSet<Object> objectList;
	private final ArrayList<Neuron> activationMap;
	private final Object[][] objectLocation;
	private final Neuron[][] neuronLocation;

	public ObjectCollection() {
		objectStack = new Stack<>();
		activationMap = new ArrayList<>();
		objectList = new HashSet<>();
		objectLocation = new Object[GRID_SIZE + 1][GRID_SIZE + 1];
		neuronLocation = new Neuron[GRID_SIZE + 1][GRID_SIZE + 1];
	}

	public void createMap() {
		for (int y = 0; y < GRID_SIZE; y++) {
			for (int x = 0; x < GRID_SIZE; x++) {
				Grass grass = new Grass(x, y);
				addObject(grass);
			}
		}
	}

	public List<Neuron> getActivationMap() {
		return activationMap;
	}

	public Stack<Object> getStack() {
		return objectStack;
	}

	public void removeObject(Object ob) {
		objectLocation[ob.getX()][ob.getY()] = null;
	}

	public void addObject(Object ob) {
		if (isOutOfBounds(ob.getX(), ob.getY()) || objectLocation[ob.getX()][ob.getY()] != null){
			return;
		}
		objectStack.push(ob);
		Neuron neuron = new Neuron(0, 0, 1, ob.getX(), ob.getY());
		neuron.setObject(ob);
		activationMap.add(neuron);
		objectLocation[ob.getX()][ob.getY()] = ob;
		neuronLocation[ob.getX()][ob.getY()] = neuron;
		if (ob.getClass() == Stone.class) {
			activationMap.get(activationMap.size() - 1).setValue(1);
			objectLocation[ob.getX()][ob.getY()].setIota(1);
		}
	}

	private boolean isOutOfBounds(int x, int y) {
		return x < 0 || x > GRID_SIZE || y < 0 || y > GRID_SIZE;
	}

	public Object inList(int x, int y) {
		if(neuronLocation[x][y] != null) {
			return neuronLocation[x][y].getObject();
		}
		if (isOutOfBounds(x, y)) {
			return null;
		}
		return createAndAddNewObject(x, y);
	}

	private Object createAndAddNewObject(int x, int y) {
		// If y is not 2, create a grass object, otherwise create a water object
		if(objectLocation[x][y] != null) {
			return objectLocation[x][y];
		}
		Object obj = y != 2 ? new Grass(x, y) : new Water(x, y);
		addObject(obj);
		return obj;
	}



	public ArrayList<Object> getNeighborhood(int x, int y) {
		ArrayList<Object> neighborhood = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// Check if the neighbor is within the grid
				if (x + i >= 0 && x + i < GRID_SIZE && y + j >= 0 && y + j < GRID_SIZE) {
					// neighborhood is a objects touching the object at x, y
					neighborhood.add(objectLocation[x + i][y + j]);
				}
			}
		}
		return neighborhood;
	}

	public List<Neuron> getRecField(int x, int y) {
		List<Neuron> recField = new ArrayList<>();
		for(Neuron neuron : activationMap) {
			if(neuron.getX() >= x - 2 && neuron.getX() <= x + 2 && neuron.getY() >= y - 2 && neuron.getY() <= y + 2) {
				recField.add(neuron);
			}
		}
		return recField;
	}

	public void setIota(Class type, double value, int x, int y) {
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				// Check if the neighbor is within the grid
				if (x + i >= 0 && x + i < GRID_SIZE && y + j >= 0 && y + j < GRID_SIZE) {
					Object obj = objectLocation[x + i][y + j];
					// check if the object is of the same type
					if (obj != null && obj.getClass() == type) {
						obj.setIota(value);
						int index = (x + i) * GRID_SIZE + y + j;
						activationMap.get(index).setIota(value);
						activationMap.get(index).setObject(obj);
						updateMap(obj, activationMap.get(index));
					}
				}
			}
		}
	}

	public void setNeuronValue(int x, int y, double value) {
		for(Neuron neuron : activationMap) {
			if(neuron.getX() == x && neuron.getY() == y) {
				neuron.setValue(value);
			}
		}
	}

	public ObjectCollection printAnimatJourney(Animat animat){
		Stack<Object> stack = animat.getStack();
		ObjectCollection objectCollection = animat.map();
		while (!stack.isEmpty()) {
			Object ob = stack.pop();
			objectCollection.removeObject(ob);
			objectCollection.addObject(new Path(ob.getX(), ob.getY()));
		}
		return objectCollection;
	}

	public Object getClass(Object object) {

		if(object.getClass() == Grass.class) {
			return new Grass(object.getX(), object.getY());
		} else if(object.getClass() == Water.class) {
			return new Water(object.getX(), object.getY());
		} else if(object.getClass() == Stone.class) {
			return new Stone(object.getX(), object.getY());
		} else if(object.getClass() == Path.class) {
			return new Path(object.getX(), object.getY());
		} else if(object.getClass() == Trap.class) {
			return new Trap(object.getX(), object.getY());
		} else if(object.getClass() == Resource.class) {
			return new Resource(object.getX(), object.getY());
		} else {
			return null;
		}

	}
	/*
	 * Generates the receptive field which is locations reachable by the animat
	 */
	public Neuron generateReceptiveField(Neuron neuron) {
		// Get the x and y coordinates of the neuron
		int x = neuron.getX();
		int y = neuron.getY();
		// Loop through the 3x3 grid around the neuron
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// Check if the neighbor is within the grid and not the neuron itself
				if (x + i >= 0 && x + i < GRID_SIZE && y + j >= 0 && y + j < GRID_SIZE && !(i == 0 && j == 0)){
					// Add the neuron to the receptive field
					neuron.addReceptiveField(getNeuron(x + i, y + j));
				}
				if (neuron.getReceptiveField().size() == 8) {
					return neuron;
				}
			}
		}
		return neuron;
	}

	public Neuron getNeuron(int x, int y) {

		return neuronLocation[x][y];
	}

	public void setNeuronWeights(int x, int y, double weights) {
		for(Neuron neuron : activationMap) {
			if(neuron.getX() == x && neuron.getY() == y) {
				neuron.setWeights(weights);
			}
		}
	}

	public void setNeuronBias(int x, int y, double bias) {
		for(Neuron neuron : activationMap) {
			if(neuron.getX() == x && neuron.getY() == y) {
				neuron.setBias(bias);
			}
		}
	}

	public void activateNeuron(int x, int y) {
		for(Neuron neuron : activationMap) {
			if(neuron.getX() == x && neuron.getY() == y) {
				neuron.generateReceptiveField();
				neuron.activate();
			}
		}
	}

	private void updateMap(Object object, Neuron neuron) {
		activationMap.set(neuron.getX() * GRID_SIZE + neuron.getY(), neuron);
		objectLocation[object.getX()][object.getY()] = object;
		neuronLocation[object.getX()][object.getY()] = neuron;
	}

	/*
	 * Euclidean distance between two points (x1 , y1) and (x2, y2)
	 */
	private double euclideanDistance(int x, int y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}


	/*
	* returns
	 */


	// ... rest of the class
}