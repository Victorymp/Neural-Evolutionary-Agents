package animat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Random;

import dataFrame.DataFrame;
import neuralNetwork.NeuralNetwork;
import objects.Object;
import objects.ObjectCollection;


public class AnimatCollection {
	private ArrayList<Animat> ani;
	private int generation;
	private List<String[]> death_runs;
	private List<String[]> ls;
	private DataFrame rdf;

	private List<String[]> all_animats;
	private ArrayList<ArrayList<String[]>> multiple_days;
	private ObjectCollection objectCollection;
	private ArrayList<String[]> reward_list;
	private int global_day;
	private ArrayList<ArrayList<String[]>> multiple_days_2;
	private double lowest_fitness = 10000;
	private Animat best_1;
	private Animat best_2;
	private NeuralNetwork nn;

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
		all_animats = new ArrayList<>();
		global_day = 0;
	}

	public AnimatCollection(int x, ObjectCollection objectCollection, NeuralNetwork nn) {
		ani = new ArrayList<>(1000);
		this.generation = x;
		rdf = new DataFrame();
		ls = new ArrayList<>();
		multiple_days = new ArrayList<>();
		multiple_days_2 = new ArrayList<>();
		death_runs = new ArrayList<String[]>();
		reward_list = new ArrayList<String[]>();
		this.objectCollection = objectCollection;
		all_animats = new ArrayList<>();
		global_day = 0;
		this.nn = nn;
	}
	/**
	 * Generation where all are teachers
	 * x is the number of animats
	 * @param x
	 */
	public void startGeneration(int x){
		// 20% of the animats are teachers
		int teachers_percent= (int) Math.round(x*0.2);
		Random rand = new Random();

		if (x <=1000) {
			for(int i = 0; i<x; i++) {
				// if i is less than the percentage of teachers
				int randomX = rand.nextInt(20);
				int randomY = rand.nextInt(20);
				if( i < teachers_percent) {
					// Starting location is 10,20
					ani.add(new Animat(randomX,randomY,i,true,objectCollection));
				} else {
					ani.add(new Animat(randomX,randomY,i, false,objectCollection));
				}
			}
			System.out.println("Created "+ani.size()+" Animats");
		} else {
			System.out.println("Too big");
		}
		if(generation > 0) {
			// if the generation is greater than 0
			// then it will give the animats the neural network
			for (Animat i: ani) {
				i.setNeuralNetwork(nn);
			}
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
			} System.out.println("Created "+ani.size()+" Animats");
		} else {
			System.out.println("Too big");
		}
		}
	public void nextGeneration() {
		ArrayList<Animat> tmp = new ArrayList<>();
		for(Animat i: ani) {
			tmp.add(i);
		}
		ani = tmp;
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
		int count= 0;
		for(int i=0; i< days; i++) {
			day();
			count++;
		}
		System.out.print("Generation: "+generation+"\n");
		System.out.print("Days: "+count+"\n");
		System.out.print("Deaths at the end of the generation: "+ death_runs.size()+"\n");
		System.out.print("Reached end at the end of the generation: "+ reward_list.size()+"\n");
		System.out.print("Animats: "+ ani.size()+"\n");
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
			day.add(i.getDay(Boolean.valueOf(false)));
			day2.add(i.getDay(Boolean.valueOf(true)));
			if(i.getDeath()) death_runs.add(i.getRuns());
			if(i.getReached_end()) reward_list.add(new String[]{Integer.toString(i.getId())});
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

	public ArrayList<ObjectCollection> fitness() {
		ArrayList<ObjectCollection> ob = new ArrayList<>();
		for(Animat i: ani) {
			ob.add(i.map());
		}
		return ob;
	}

	public void endOfGeneration() {
		if(generation >0){
			naturalSelection();
			for(Animat i: ani) {
				// cross over with the best
				i.crossOver(best_1, best_2);
				i.mutate();
			}
		} else {
			System.out.println("Skipped natural selection");
		}
	}

	/**
	 * Natural selection
	 * @return the best animat
	 */
	private void naturalSelection() {
		if (ani.isEmpty()) {
			System.out.println("No animats to select from.");
			return;
		}

		double bestFitness = Double.MAX_VALUE;
		Animat best1 = null;
		Animat best2 = null;

		for (Animat animat : ani) {
			// If the fitness is better than the current best, update the best
			if (animat.getFitness() < bestFitness && !animat.getDeath()) {
				best2 = best1;
				best1 = animat;
				bestFitness = animat.getFitness();
				lowest_fitness = bestFitness;
			} else if (animat.getFitness() == bestFitness && !animat.getDeath() ){
				// Handle equal fitness values here
				// This is just an example, adjust as needed
				if (best2 == null || animat.getId() > best2.getId()) {
					best2 = animat;
				}
			}
		}

		if (best1 == null) {
			System.out.println("Best animat is null");
			return;
		}

		if (best2 == null) {
			best2 = best1;
		}

		this.best_1 = best1;
		this.best_2 = best2;
	}

	public double getLowestFitness() {
		return lowest_fitness;
	}

	public ObjectCollection getObjectCollection() {
		return objectCollection;
	}

	public NeuralNetwork getGenerationNeuralNetwork(){
		if(best_1 != null){
			return best_1.getNeuralNetwork();
		} else {
			naturalSelection();
			return best_1.getNeuralNetwork();
		}
	}

	public Animat getBest_1(){
        if (best_1 == null)  naturalSelection();
		System.out.println("Best 1: "+best_1.getId()+" Fitness: "+best_1.getFitness());
        return best_1;
    }

	public double getMean() {
		ArrayList<Double> all_fitness = new ArrayList<>();
		for (Animat i: ani) {
			all_fitness.add(Double.valueOf(i.getFitness()));
		}
		return all_fitness.stream().mapToDouble(val -> val).average().orElse(0.0);
	}
}
