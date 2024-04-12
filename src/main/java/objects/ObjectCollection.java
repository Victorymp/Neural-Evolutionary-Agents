package objects;

import animat.Animat;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class ObjectCollection {

	private static final int GRID_SIZE = 20;

	private Stack<Object> objectStack;
	private ArrayList<Object> objectList;
	private double[][] activationMap;
	private Object[][] nullLocation;

	public ObjectCollection() {
		objectStack = new Stack<>();
		activationMap = new double[GRID_SIZE + 1][GRID_SIZE + 1];
		objectList = new ArrayList<>();
		nullLocation = new Object[GRID_SIZE + 1][GRID_SIZE + 1];
	}

	public void createMap() {
		for (int y = 0; y < GRID_SIZE; y++) {
			for (int x = 0; x < GRID_SIZE; x++) {
				Grass grass = new Grass(x, y);
				addObject(grass);
			}
		}
	}

	public double[][] getActivationMap() {
		return activationMap;
	}

	public Stack<Object> getStack() {
		return objectStack;
	}

	public void removeObject(Object ob) {
		objectList.remove(ob);
	}

	public void createNewStack() {
		objectStack = new Stack<>();
	}

	public void addObject(Object ob) {
		if (objectList.contains(ob) || isOutOfBounds(ob.getX(), ob.getY())) {
			throw new IllegalArgumentException("Object already exists or is out of bounds");
		}
		objectStack.push(ob);
		objectList.add(ob);
		activationMap[ob.getX()][ob.getY()] = 0;
		nullLocation[ob.getX()][ob.getY()] = ob;
		if (ob.getClass() == Stone.class) {
			activationMap[ob.getX()][ob.getY()] = 0;
		}
	}

	private boolean isOutOfBounds(int x, int y) {
		return x < 0 || x > GRID_SIZE || y < 0 || y > GRID_SIZE;
	}

	public Object inList(int x, int y) {
		for (Object i : objectList) {
			if (i.getX() == x && i.getY() == y) {
				return i;
			}
		}
		if (isOutOfBounds(x, y)) {
			return null;
		}
		return createAndAddNewObject(x, y);
	}

	private Object createAndAddNewObject(int x, int y) {
		// If y is not 2, create a grass object, otherwise create a water object
		Object obj = y != 2 ? new Grass(x, y) : new Water(x, y);
		addObject(obj);
		return obj;
	}

	public ArrayList<Object> getLists() {
		return objectList;
	}

	public ArrayList<Object> getNeighborhood(int x, int y) {
		ArrayList<Object> neighborhood = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// Check if the neighbor is within the grid
				if (x + i >= 0 && x + i < GRID_SIZE && y + j >= 0 && y + j < GRID_SIZE) {
					// neighborhood is a objects touching the object at x, y
					neighborhood.add(nullLocation[x + i][y + j]);
				}
			}
		}
		return neighborhood;
	}

	public void setIota(Class type, double value, int x, int y) {
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				// Check if the neighbor is within the grid
				if (x + i >= 0 && x + i < GRID_SIZE && y + j >= 0 && y + j < GRID_SIZE) {
					Object obj = nullLocation[x + i][y + j];
					// check if the object is of the same type
					if (obj != null && obj.getClass() == type) {
						obj.setIota(value);
						activationMap[obj.getX()][obj.getY()] = value;
					}
				}
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


	// ... rest of the class
}