package animat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataFrame.DataFrame;
import objects.ObjectCollection;

import static javafx.application.Platform.exit;


public class AnimatCollection {
	private ArrayList<Animat> ani;
	private final int generation;
	private List<String[]> death_runs;
	private List<String[]> ls;
	private DataFrame rdf;

	private List<String[]> all_animats;
	private ArrayList<ArrayList<String[]>> multiple_days;
	private ObjectCollection objectCollection;
	private ArrayList<String[]> inputs;
	private ArrayList<String[]> reward_list;
	private int global_day;
	private ArrayList<ArrayList<String[]>> multiple_days_2;

	public AnimatCollection(int x, ObjectCollection objectCollection) {
		ani = new ArrayList<>(1000);
		this.generation = x;
		rdf = new DataFrame();
		ls = new ArrayList<>();
		multiple_days = new ArrayList<>();
		multiple_days_2 = new ArrayList<>();
		death_runs = new ArrayList<String[]>();
		reward_list = new ArrayList<String[]>();
		this.objectCollection = objectCollection;
		inputs = new ArrayList<>();
		all_animats = new ArrayList<>();
		global_day = 0;
	}

	public AnimatCollection() {
		// TODO Auto-generated constructor stub
		ani = new ArrayList<>(1000);
		this.generation = 1;
	}
	/**
	 * Generation where all are teachers
	 * x is the number of animats
	 * @param x
	 */
	public void startGeneration(int x){
		// 20% of the animats are teachers
		int teachers_percent= (int) Math.round(x*0.2);
		if (x <=1000) {
			for(int i = 0; i<=x; i++) {
				// if i is less than the percentage of teachers
				if( i < teachers_percent) {
					ani.add(new Animat(10,20,i,true,objectCollection));
				} else {
					ani.add(new Animat(10,20,i, false,objectCollection));
				}
			}
			System.out.println("Created "+x+" Animats");
		} else {
			System.out.println("Too big");
		}
		// Gives the animats the object map.map
		for (Animat i: ani) {
			i.setObject_map_location(objectCollection);
		}
		}
	
	/**
	 * Generation where you can choose how many are teacher
	 * Percentage between 1 and 0
	 * @param x
	 * @param y
	 */
	public void startGeneration(int x, double y){
		DecimalFormat df = new DecimalFormat("#.##");
		y = Double.parseDouble(df.format(y));
		if (x <=1000) {
			for(int i = 0; i<=x; i++) {
				if( i < x*y) {
					ani.add(new Animat(10,20,i,true,objectCollection));
				}
				ani.add(new Animat(10,20,i, false,objectCollection));
			} System.out.println("Created "+x+" Animats");
		} else {
			System.out.println("Too big");
		}
		}


	public ArrayList<Animat> getGeneration(){
		return ani;
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
		if(!ani.isEmpty()) {
			for(Animat i: ani) {
				all_animats.add(i.getAnimat());
			}
		}
		for(int i=0; i<= days; i++) {
			day();	
		}
		saveDay();

		System.out.print("Deaths at the end of the generation: "+ death_runs.size()+"\n");
		System.out.print("Reached end at the end of the generation: "+ reward_list.size()+"\n");
	}
	


	
	private void day() {
		Iterator<Animat> iterator = ani.iterator();
		ArrayList<Animat> tmp = new ArrayList<Animat>();
		ArrayList<String[]> day = new ArrayList<>();
		ArrayList<String[]> day2 = new ArrayList<>();
		ArrayList<String[]> ls = new ArrayList<>();
		ls.add(new String[] {"Day: "+global_day});
		/*
		 * Every Ani is the animat list
		 */
		for(Animat i: ani) {
			i.move();
			// if the animat is a teacher
			day.add(i.getDay(false));
			day2.add(i.getDay(true));
			// day.add(i.getDay());
			// ls.add(i.getDay());
			inputs.add(i.generateInputs());
			// end of life get run 
			if(i.getDeath()) death_runs.add(i.getRuns());
			// If the animat reaches the end
			if(i.getReached_end()) reward_list.add(new String[]{Integer.toString(i.getId())});
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
		death_runs.add(ls.get(0));
		reward_list.add(ls.get(0));
		multiple_days.add(ls);
		multiple_days.add(day);
		multiple_days_2.add(ls);
		multiple_days_2.add(day2);
		global_day++;
	}
	
	private void saveDay() {
		rdf.multiDaySave("Runners", multiple_days);
		rdf.saveRun("Dead-Runs", death_runs);
		rdf.saveRun("Reward-Runs", reward_list);
		rdf.saveInputs("Inputs", inputs);
		rdf.saveRun("MultiDay", rdf.multiDayConversion(multiple_days_2), new String[] {"Animat","Lifespan","Location x","Location y","Teacher","Reached end"});
		rdf.saveRun("AllAnimats", all_animats, new String[] {"Animat","Lifespan","Teacher","Reached end"});
	}
	/**
	 * Sets the object collection for the animats to use
	 * @param ob
	 */
	public void setObjectCollection(ObjectCollection ob) {
		objectCollection = ob;
	}
	/**
	 * Prints the journey of a animat if its a teacher
	 * @param x
	 */
	public void printAnimatJourney(int x,String name) {
		ArrayList<String[]> records = rdf.openMemory(name);
		int count = 0;
		for(String[] i: records) {
			// changes from a literal string into a real string and converts it to a integer
			int animat_no = Integer.parseInt(i[0].replace("\"", ""));
			if(animat_no == x ) {
				if(Boolean.parseBoolean(i[4].replace("\"", ""))) {
					System.out.print("Animat: "+animat_no+" day: "+i[1]+"\n");
				} else {
					System.out.print("Day"+count+"\n");
					System.out.print("Animat: "+animat_no+" Nearest stone: "+i[1]+" Distance to water: "+i[2]+" Distance from start: "+i[3]+" Distance to end: "+i[4]+" Has stone: "+i[5]+"\n");
					count++;
				}
			}
		}
	}
}
