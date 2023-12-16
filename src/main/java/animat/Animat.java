package animat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import com.opencsv.CSVWriter;

import dataFrame.DataFrame;
import javafx.scene.canvas.GraphicsContext;
import objects.Stone;
import shape.Drawable;

public class Animat {
	private int health;
	private int x_pos;
	private int y_pos;
	private Integer[] locations;
	private Stack<Stone> stones;
	private HashMap<Action, Integer[]> runs;
	private int id;
	private Action ac;
	private List<String[]> run_list;
	private DataFrame rdf;
	private int lifeSpan;
	private DataFrame df;
	private Boolean teacher;

	public Animat(int x_pos, int y_pos, int id, Boolean teachers) {
		locations = new Integer[2];
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		locations[0] = x_pos;
		locations[1] = y_pos;
		health += 100;
		this.id = id;
		ac = new Action();
		runs = new HashMap<>();
		lifeSpan = 0;
		df = new DataFrame();
		this.teacher = teachers;
	}
	
	@Override
	public String toString() {
		return ("Animat: "+getId()+" x pos: "+getX()+" y pos: "+getY());
	}
	
	/*
	 * Updates location and everything used to move
	 */
	public void updateMoves(int x, int y) {
		x_pos += x;
		y_pos += y;
		updateLocation();
		run();
		this.getTeachers();
	}
	

	public int getId() { return id; }


	public int getX() { return x_pos;}

	public int getY() { return y_pos;}
	/*
	 * How it moves within a day
	 */
	public void move() {
		if(y_pos != 2 ) {
			Random r = new Random();
			int rand = r.nextInt(3);
			lifeSpan +=1;
			switch (rand) {
				case 0:
					ac.setAc("back");
					updateMoves(0,-1);
					break;
				case 1:
					ac.setAc("left");
					updateMoves(-1,0);
					break;
				case 2:
					ac.setAc("right");
					updateMoves(1,0);
					break;
				case 3:
					ac.setAc("forward");
					updateMoves(0,1);
					break;
					}
			}else if(y_pos == 2 & !hasStone()){
				die();
			} else if(y_pos == 2) {return;}
	}
	
	public String[] getDay() {
		if(teacher) {
			String[] day = {""+id,""+lifeSpan,""+x_pos,""+y_pos,""+teacher};
			return day;
				
		}else {
			String[] day = {""+id,""+lifeSpan,""+x_pos,""+y_pos,};
			return day;
		}
		
	}
	
	public String[] getRuns(){
		String[] t = {"Animat: "+this.id+"","Action:"+ac.getAc()};
		return t;

	}
	
	public Boolean getDeath() {
		if(health <= 0) {
			return true;
		}
		return false;
	}
	
	public int getLifeSpan() {
		return lifeSpan;
	}

	public void forward() {
		y_pos += 1;
	}

	public void right() {
		x_pos += 1;
	}

	public void back() {
		y_pos -= 1;
	}

	public void left() {
		x_pos -= 1;
	}

	public void pickUp(Stone st) {
		stones.push(st);
	}
	
	public Stone drop()throws StackException{

		if (!stones.isEmpty()) {
			return stones.pop();
		} else {
			System.out.println("No stones");
			throw new StackException("No stones");
		}
	}
	// has to be in bounds of the board
	public Boolean nextIsValid(int x_move, int y_move) {
		// check if the location is taken 
		return true;
	}

	public void die(String x) {
		health -= 100;}

	public void die() {
		health -= 100;}

	public void saveRuns() {
		rdf.saveRun(run_list);
	}

	private void saveMemory(ArrayList record) {

	}
	
	private ArrayList getTeachers() {
		//System.out.println(df.openMemory("Runners"));
		//ArrayList<Object> teachers = df.openMemory("Runners");
		return df.openMemory("Runners");
	}

	public void run() {
		// mapping action and location together 
		runs.put(ac.getAc(lifeSpan), locations);
	}
	
	private void updateLocation() {
		locations[0] = getX();
		locations[1] = getY();
	}
	
	private Boolean hasStone() {
		if(stones != null) {
			return true;
		}
		return false;
	}




}

class StackException extends Exception{
	public StackException(String message) {
		super(message);
	}
}
