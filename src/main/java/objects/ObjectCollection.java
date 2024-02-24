package objects;


import java.util.Stack;

public class ObjectCollection {

	private Stack<Object> object_stack;
	public ObjectCollection() {
		object_stack = new Stack<>();
	}

	public Stack<Object> getStack(){
		return object_stack;
	}

	public void createNewStack() {
		object_stack = new Stack<>();
	}

	public void addObject(Object ob) {
		object_stack.push(ob);
		//System.out.println("add "+ob.getClass()+ " x: "+ob.x_pos+" y: "+ob.y_pos);
	}

	public Object inStack(int x, int y) {
		for(Object i: object_stack) {
			if(i.getX() == x && i.getY() == y) {
				return i;
			}
		}
		return null;
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

	public int objectSize() {
		return object_stack.capacity();
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


}
