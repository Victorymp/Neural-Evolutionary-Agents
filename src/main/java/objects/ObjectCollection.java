package objects;


import java.util.ArrayList;
import java.util.Stack;

public class ObjectCollection {

	private Stack<Object> object_stack;

	private ArrayList<Object> object_list;

	private double[][] activation_map;

	private Object[][] null_location;

    public ObjectCollection() {
		object_stack = new Stack<>();
		// Values held in the object map.map are Iota values
        activation_map = new double[21][21];

		object_list = new ArrayList<>();
		null_location = new Object[21][21];
	}

	public void createMap(){
		// 100 rectangles grid
		// 20x20 grid
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				Grass grass = new Grass(x, y);
				addObject(grass);
			}
		}
	}

	public double[][] getActivationMap() {
		return activation_map;
	}
	public Stack<Object> getStack(){
		return object_stack;
	}

	public void createNewStack() {
		object_stack = new Stack<>();
	}



	public void addObject(Object ob) {
		// check if object already exists
		if(object_list.contains(ob) || ob.getX() < 0 || ob.getX() > 20 || ob.getY() < 0 || ob.getY() > 20) {
			System.out.println("Object already exists");
			return;
		}
		object_stack.push(ob);
		object_list.add(ob);
		activation_map[ob.getX()][ob.getY()] = 0;
		if(ob.getClass() == Stone.class) {
			activation_map[ob.getX()][ob.getY()] = 0;
		}
		//sortList();
		//System.out.println("add "+ob.getClass()+ " x: "+ob.x_pos+" y: "+ob.y_pos);
	}

	public Object inStack(int x, int y) {
		for(Object i: object_stack) {
			if(i.getX().equals(x) && i.getY().equals(y)) {
				return i;
			}
		}
		return null;
	}

	public Object inList(int x, int y) {
		for(Object i: object_list) {
			if(i.getX() == x && i.getY() == y) {
				return i;
			}
		}
		if(x < 0 || x > 20 || y < 0 || y > 20) {
			Grass grass = new Grass(x, y);
			addObject(grass);
			return grass;
		}
		null_location[x][y] = new Grass(x, y);
		if(y !=2){
		Grass grass = new Grass(x, y);
		addObject(grass);
		return grass;}
		Water water = new Water(x, y);
		addObject(water);
		return water;
	}

	public void setIota(int x, int y, double value) {
		inStack(x,y).setIota(value);
	}

	public int inStackIndex(int x, int y) {
		int count = 0;
		for(Object i: object_stack) {
			if(i.getX() == x && i.getY() == y) {
				return count;
			}
			count++;
		}
		return -1;
	}

	public void objectSize() {
		//sortList();
		System.out.println(object_list.size());
	}

	public ArrayList<Object> getLists(){
		return object_list;
	}

	public void displayObjects() {
		for(Object i: object_stack) {
			System.out.println("x: "+i.getX()+" y: "+i.getY()+" class: "+i.getClass());
		}
	}

	/**
	 * Displays all the objects of a certain class
	 * @param ob
	 */
	public void displayObjects(Class ob) {
		for(Object i: object_stack) {
			if(i.getClass() == ob) {
				System.out.println("x: "+i.getX()+" y: "+i.getY()+" class: "+i.getClass());
			}
		}
	}

	/**
	 * returns a stack with all the objects of a certain class
	 * @param ob
	 */
	public Stack<Object> getObjects(Class ob) {
		Stack<Object> temp = new Stack<>();
		for(Object i: object_stack) {
			if(i.getClass() == ob) {
				temp.push(i);
			}
		}
		return temp;
	}

	// removes a object in the stack
	public void removeObject(Object ob) {
		object_stack.remove(ob);
	}

	/**
	 * Sort the objects in the list
	 */
	private void sortList() {
		ArrayList<Object> temp = new ArrayList<>();
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				for(Object k: object_list) {
					if(k.getX() == i && k.getY() == j) {
						temp.add(k);
					}
				}
			}
		}
		object_list = temp;
	}

	// displays null locations
	public void displayNullLocations() {
		for(Object[] i: null_location) {
			for(Object j: i) {
				System.out.println("x: "+j.getX()+" y: "+j.getY());
			}
		}
	}

	// displays the activation map.map
	public void displayActivationMap() {
		// sortList();
		for(Object i:object_list){
			System.out.println("x: "+i.getX()+" y: "+i.getY()+" Iota: "+i.getIota());
			}

	}


}
