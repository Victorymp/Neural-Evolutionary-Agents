package animat;

import java.text.DecimalFormat;
import java.util.*;

import dataFrame.DataFrame;
import neuralNetwork.NeuralNetwork;
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
	private ArrayList<Integer> reached_end;
	private final Animat[] ani_array = new Animat[1000];

	public AnimatCollection(int x, ObjectCollection objectCollection) {
		this.generation = x;
		rdf = new DataFrame();
		ls = new ArrayList<>();
		multiple_days = new ArrayList<>();
		multiple_days_2 = new ArrayList<>();
		death_runs = new ArrayList<String[]>();
		reward_list = new ArrayList<String[]>();
		reached_end = new ArrayList<>();
		this.objectCollection = objectCollection;
		all_animats = new ArrayList<>();
		global_day = 0;
		ani = new ArrayList<>();
	}

	public AnimatCollection(int x, ObjectCollection objectCollection, NeuralNetwork nn) {
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
		reached_end = new ArrayList<>();
		ani = new ArrayList<>();
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
			for(int i = 0; i<x; i++) {
				// if i is less than the percentage of teachers
				int randomX = 10;
				int randomY = 20;
				if( i < teachers_percent) {
					// Starting location is 10,20
					ani.add(new Animat(randomX,randomY,i,true,objectCollection));
				} else {
				 	ani.add(new Animat(randomX,randomY,i,false,objectCollection));
				}
			}
			System.out.println("Created "+ani_array.length+" Animats");
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
			i.setObject_map(objectCollection);
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
				ani.add(new Animat(10,20,i,false,objectCollection));
			} System.out.println("Created "+ani.size()+" Animats");
		} else {
			System.out.println("Too big");
		}
		}


	public int getGen() {
		return generation;
	}
	
	public int getSize() {
		return ani.size();
	}
	
	public void setAnimat(Iterator<Animat> x) {
		while (x.hasNext()) {
			ani.add(x.next());
		}
	}

	public void runDays(int days) {
        for (Animat	 i : ani) {
            all_animats.add(i.getAnimat());
        }
        int count= 0;
		for(int i=0; i< days; i++) {
			day();
			count++;
		}
		for(Animat i: ani) {
			if(i.hasReachedEnd()) {
				reached_end.add(i.getId());
			}
		}
		System.out.print("Generation: "+generation+"\n");
		System.out.print("Days: "+count+"\n");
		System.out.print("Deaths at the end of the generation: "+ death_runs.size()+"\n");
		System.out.print("Reached end at the end of the generation: "+ reached_end.size()+"\n");
		System.out.print("Animats: "+ ani.size()+"\n");
		}

	private void day() {
		ArrayList<String[]> day = new ArrayList<>();
		ArrayList<String[]> day2 = new ArrayList<>();
		ArrayList<String[]> ls = new ArrayList<>();
		ls.add(new String[] {"Day: "+global_day});
		/*
		 * Every Ani is the animat list
		 */
		for(Animat i: ani) {
			// System.out.print("Animat: "+i.getId()+" Moving"+"\n");
			i.move();
			// System.out.print("Animat: "+i.getId()+" Moved"+"\n");
			day.add(i.getDay(Boolean.valueOf(false)));
			day2.add(i.getDay(Boolean.valueOf(true)));
			// System.out.print("Animat2: "+i.getId()+"\n");

		}
		// record dead
		for(Animat i: ani) {
			if(i.getDeath()) {
				ls.add(i.getRuns());
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
	/**
	 * End of generation
	 */
	public void endOfGeneration() {
		if(generation >0){
			naturalSelection();
			for(Animat i: ani) {
				if(i.hasReachedEnd()) {
					reached_end.add(i.getId());
				}
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
	 * Selects the best animat and the second best animat
	 */
	void naturalSelection() {
		if (ani.isEmpty()){
			System.out.println("No animats to select from.");
			return;
		}
		double bestFitness = -1;
		Stack<Animat> stack = new Stack<>();

		for (Animat animat : ani) {
			// If the fitness is better than the current best, update the best and not dead
			if (animat.getFitness() > 0){
				stack.push(animat);
			}
			if (animat.getFitness() > bestFitness ) {
				best_2 = best_1;
				best_1 = animat;
				bestFitness = animat.getFitness();
				lowest_fitness = bestFitness;
			}

		}
		if(!stack.isEmpty()){
			best_2 = stack.pop();
		}
		if(!stack.isEmpty()){
			best_1 = stack.pop();
		}
		if(best_1 != null && best_2 != null){
			return;
		}
		Random rand = new Random();
		if (best_1 == null) {
			best_1 = ani.get(rand.nextInt(ani.size()));
		}

		if (best_2 == null) {
			best_2 = ani.get(rand.nextInt(ani.size()));
			System.out.println("Best 2: "+best_2.getId()+" Fitness: "+best_2.getFitness());
		}

		// this.best_1 = best1;
		// this.best_2 = best2;
	}

	public double getLowestFitness() {
		return lowest_fitness;
	}

	public ObjectCollection getObjectCollection() {
		return objectCollection;
	}

	public NeuralNetwork getGenerationNeuralNetwork(){
        return best_1.getNeuralNetwork();
    }

	public Animat getBest_1(){
        if (best_1 == null) naturalSelection();
		// System.out.println("Best 1: "+best_1.getId()+" Fitness: "+best_1.getFitness());
        return best_1;
    }

	public double getMean() {
		ArrayList<Double> all_fitness = new ArrayList<>();
		for (Animat i: ani) {
			all_fitness.add(Double.valueOf(i.getFitness()));
		}
		return all_fitness.stream().mapToDouble(val -> val).average().orElse(0.0);
	}

	public double getMeanLifespan() {
		ArrayList<Double> all_fitness = new ArrayList<>();
		for (Animat i: ani) {
			all_fitness.add(Double.valueOf(i.getLifeSpan()));
		}
		return all_fitness.stream().mapToDouble(val -> val).average().orElse(0.0);
	}
}
