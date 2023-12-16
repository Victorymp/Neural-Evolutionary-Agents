package animat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import dataFrame.DataFrame;


public class AnimatCollection {
	private ArrayList<Animat> ani;
	private Iterator<Animat> ani1;
	private int generation;
	private int deaths;
	private static Iterator<Animat> instance;
	private List<String[]> runs;
	private List<String[]> ls;
	private DataFrame rdf;
	private ArrayList<ArrayList<String[]>> multiple_days;


	public AnimatCollection(int x) {
		ani = new ArrayList<>(1000);
		ani1 = ani.iterator();
		this.generation = x;
		deaths = 0;
		rdf = new DataFrame();
		ls = new ArrayList<>();
		multiple_days = new ArrayList<>();
		runs = new ArrayList<String[]>();
	}

	public AnimatCollection() {
		// TODO Auto-generated constructor stub
		ani = new ArrayList<>(1000);
		ani1 = ani.iterator();
		this.generation = 1;
		deaths = 0;
	}
	/**
	 * Generation where all are teachers 
	 * @param x
	 */
	public void startGeneration(int x){
		if (x <=1000) {
			for(int i = 0; i<=x; i++) {
				ani.add(new Animat(10,20,i, true));
			}
			System.out.println("Created "+x+" Animats");
		} else {
			System.out.println("Too big");
		}
		}
	
	/**
	 * Generation where you can choose how many are teacher 
	 * 
	 * Percentage between 1 and 0
	 * @param x
	 * @param y
	 */
	public void startGeneration(int x, double y){
		DecimalFormat df = new DecimalFormat("#.##");
		y = Double.valueOf(df.format(y));
		if (x <=1000) {
			for(int i = 0; i<=x; i++) {
				if( i < x*y) {
					ani.add(new Animat(10,20,i,true));
				}
				ani.add(new Animat(10,20,i, false));
			} System.out.println("Created "+x+" Animats");
		} else {
			System.out.println("Too big");
		}
		}


	public ArrayList<Animat> getGeneration(){
		return ani;
	}
	
	private Iterator<Animat> getIt(){
		return ani1;
	}
	
	public static Iterator<Animat> getInstance() {
		if(instance == null) {
			instance = new AnimatCollection().ani1;
		}
		return instance;
	}
	public int getGen() {
		return generation;
	}
	
	public int getSize() {
		return ani.size();
	}
	
	public void setAnimat(Iterator<Animat> x) {
		ArrayList<Animat> tmp = new ArrayList<Animat>();
		while(x.hasNext()) {
			tmp.add(x.next());
		}
		ani = tmp;
	}
	
	public void setAnimat(ArrayList<Animat> x) {
		ani = x;
	}
	
	public void runDays(int days) {
		for(int i=0; i<= days; i++) {
			System.out.print("day "+i +": \n");
			saveDay();
			day();	
		}
		System.out.print("Deaths at the end of the generation: "+runs.size()+"\n");
	}
	
	/**
	 * Prints the journey of a animat if its a teacher 
	 * @param x
	 */
	public void printAnimatJourney(int x) {
		ArrayList<String[]> records = rdf.openMemory("Runners"); 
		for(String[] i: records) {
			// changes from a literal string into a real string and converts it to a integer
			int animat_no = Integer.valueOf(new String(i[0].replace("\"", "")));
			if(animat_no == x ) {
				if(Boolean.valueOf(new String(i[4].replace("\"","")))) {
					System.out.print("Animat: "+animat_no+" day: "+i[1]+"\n");
				}
			}
		}
	}
	
	private void day() {
		Iterator<Animat> iterator = ani.iterator();
		ArrayList<Animat> tmp = new ArrayList<Animat>();
		ArrayList<String[]> day = new ArrayList<>();
		/*
		 * Ani is the animat list 
		 */
		for(Animat i: ani) {
			i.move();
			day.add(i.getDay());
			ls.add(i.getDay());
			// end of life get run 
			if(i.getDeath()) {
				runs.add(i.getRuns());
			}
			// if water animat dies
		}
		// remove dead
		while(iterator.hasNext()) {
			Animat next = iterator.next();
			if(next.getDeath()) {
				iterator.remove();
			}else {
				tmp.add(next);
			}
		}
		multiple_days.add(day);
	}
	
	private void saveDay() {
		rdf.multiDaySave("Runners", multiple_days);
		rdf.saveRun("Dead-Runs",runs);
	}
	
}
