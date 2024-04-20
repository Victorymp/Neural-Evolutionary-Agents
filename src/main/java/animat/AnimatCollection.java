package animat;

import java.text.DecimalFormat;
import java.util.*;

import dataFrame.DataFrame;
import neuralNetwork.NeuralNetwork;
import objects.ObjectCollection;


public class AnimatCollection {
	private final ArrayList<Animat> ani;
	private final int generation;
	private DataFrame rdf;
	private ObjectCollection objectCollection;
	private ArrayList<String[]> reward_list;
	private ArrayList<Integer> death_runs;
	private int global_day;
	private double lowest_fitness = 100;
	private Animat best_1;
	private Animat best_2;
	private NeuralNetwork nn;
	private final ArrayList<Integer> reached_end;
	private Map<String, Double> distanceCache = new HashMap<>();

	public AnimatCollection(int x, ObjectCollection objectCollection) {
		this.generation = x;
		rdf = new DataFrame();
		reward_list = new ArrayList<String[]>();
		reached_end = new ArrayList<>();
		this.objectCollection = objectCollection;
		global_day = 0;
		ani = new ArrayList<>();
		death_runs = new ArrayList<>();
	}

	public AnimatCollection(int x, ObjectCollection objectCollection, NeuralNetwork nn) {
		this.generation = x;
		rdf = new DataFrame();
		reward_list = new ArrayList<String[]>();
		this.objectCollection = objectCollection;
		global_day = 0;
		this.nn = nn;
		reached_end = new ArrayList<>();
		ani = new ArrayList<>();
		death_runs = new ArrayList<>();
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
				int randomY = 19;
				Animat animat = new Animat(randomX,randomY,i,objectCollection);
				if (generation > 0) animat.setNeuralNetwork(nn);
				if( i < teachers_percent) {
					// Starting location is 10,20
					animat.setTeacher(true);
					ani.add(animat);
				} else {
					animat.setTeacher(false);
				 	ani.add(animat);
				}
			}
			System.out.println("Created "+ani.size()+" Animats");
		} else {
			System.out.println("Too big");
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
				Animat animat = new Animat(10,20,i,objectCollection);
				if (generation > 0) animat.setNeuralNetwork(nn);
				if( i < x*y) {
					animat.setTeacher(true);
					ani.add(animat);
				}
				animat.setTeacher(false);
				ani.add(animat);
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
		for(int i=0; i< days; i++) {
			day();
		}
		System.out.print("Generation: "+generation+"\n");
//		System.out.print("Days: "+count+"\n");
//		System.out.print("Deaths at the end of the generation: "+ death_runs.size()+"\n");
//		System.out.print("Reached end at the end of the generation: "+ reached_end.size()+"\n");
//		System.out.print("Animats: "+ ani.size()+"\n");
		}

	private void day() {
		/*
		 * Every Ani is the animat list
		 */
		for(Animat i: ani) {
			i.move();
		}
		// record dead
		global_day++;
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

	/**
	 * End of generation
	 */
	public void endOfGeneration() {
		if(generation >0){
			Animat[] animats = naturalSelection();
			best_1 = animats[0];
			best_2 = animats[1];
			for(Animat i: ani) {
				if(i.hasReachedEnd()) {
					reached_end.add(i.getId());
				}
				if (i.getDeath()){
                    death_runs.add(i.getId());
				}
				// cross over with the best
				if(best_1 != null && best_2 != null) {
					i.crossOver(best_1, best_2);
					i.mutate();
				}
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
	Animat[] naturalSelection() {
		if (ani.isEmpty()) {
			System.out.println("No animats to select from.");
			return null;
		}
		double bestFitness = -1;
		Stack<Animat> stack = new Stack<>();
		for (Animat animat : ani) {
			// If the fitness is better than the current best, update the best and not dead
			if (animat.getFitness() > 0) {
				stack.push(animat);
			}
			if (animat.getFitness() > bestFitness && animat.getFitness() > 0) {
				best_2 = best_1;
				best_1 = animat;
				bestFitness = animat.getFitness();
				lowest_fitness = animat.getFitness();
			}
		}
		if (!stack.isEmpty()) {
			best_2 = stack.pop();
		}
		if (!stack.isEmpty()) {
			best_1 = stack.pop();
		}
		if (best_1 != null && best_2 != null) {
			return new Animat[]{best_1, best_2};
		}
		Animat[] animats = closetAnimat();
		best_1 = animats[0];
		best_2 = animats[1];
		return animats;

	}

	/**
	 * Euclidean distance
	 * @param animat
	 * @return the euclidean distance
	 */
	private double euclideanDistance(Animat animat) {
		String key = animat.getX() + "," + animat.getY();
		if (distanceCache.containsKey(key)) {
			return distanceCache.get(key);
		}
		double distance = Math.sqrt(Math.pow(10 - animat.getX(), 2) + Math.pow(20 - animat.getY(), 2));
		distanceCache.put(key, distance);
		return distance;
	}

	/**
	 * Get the best animat
	 * @return the best animat
	 */
	public double getLowestFitness() {
		return lowest_fitness;
	}

	/**
	 * Closet animat
	 * @return the closet animat to the end
	 */
	public Animat[] closetAnimat() {
		Animat closestAnimat1 = null;
		Animat closestAnimat2 = null;
		double minDistance1 = Double.MAX_VALUE;
		double minDistance2 = Double.MAX_VALUE;
		for (Animat animat : ani) {
			double distance = euclideanDistance(animat);
			if (distance < minDistance1) {
				minDistance2 = minDistance1;
				closestAnimat2 = closestAnimat1;
				minDistance1 = distance;
				closestAnimat1 = animat;
			} else if (distance < minDistance2) {
				minDistance2 = distance;
				closestAnimat2 = animat;
			}
		}

		return new Animat[]{closestAnimat1, closestAnimat2};
	}

	public ObjectCollection getObjectCollection() {
		return objectCollection;
	}

	/**
	 * Get the best animat
	 * @return the best animat
	 */
	public NeuralNetwork getGenerationNeuralNetwork(){
        return best_1.getNeuralNetwork();
    }

	/**
	 * Get the best animat
	 * @return the best animat
	 */
	public Animat getBest_1(){
        if (best_1 == null) naturalSelection();
		// System.out.println("Best 1: "+best_1.getId()+" Fitness: "+best_1.getFitness());
        return best_1;
    }

	/**
	 * Mean fitness
	 * @return the mean fitness of the animats
	 */
	public double getMean() {
		return ani.stream()
				.mapToDouble(Animat::getFitness)
				.average()
				.orElse(0.0);}

	/**
	 * Mean lifespan
	 * @return the mean lifespan of the animats
	 */
	public double getMeanLifespan() {
		return ani.stream()
				.mapToDouble(Animat::getLifeSpan)
				.average()
				.orElse(0.0);}

	/*
	 * Best fitness
	 * Fitness is if the agent has reached the end so this is 1
	 * @return the best fitness is the agent that has the highest fitness
	 */
	public double bestFitness() {
		return ani.stream()
				.mapToDouble(Animat::getFitness)
				.max()
				.orElse(0.0);}

	/*
	 * Worst fitness
	 * @return the worst fitness is the agent that has the lowest fitness however fitness is set to has reach end so this should always be 0
	 */
	public double worstFitness() {
		return ani.stream()
				.mapToDouble(Animat::getFitness)
				.min()
				.orElse(0.0);
	}

	/*
	 * Best lifespan
	 * @return the best lifespan is the agent that has lived the shortest time with finding the resource
	 */
	public double bestLifespan() {
		return ani.stream()
				.mapToDouble(Animat::getLifeSpan)
				.max()
				.orElse(0.0);
	}

	/*
	 * Worst lifespan
	 * @return the worst lifespan is the agent that has lived the longest without finding the resource
	 */
	public double worstLifeSpan() {
		return ani.stream()
				.mapToDouble(Animat::getLifeSpan)
				.min()
				.orElse(0.0);
	}

	/**
	 * Standard deviation of the fitness
	 * @return the standard deviation of the fitness
	 **/
	public double stdDevFitness() {
		double mean = ani.stream().mapToDouble(Animat::getFitness).average().orElse(0.0);
		double sum = ani.stream().mapToDouble(a -> Math.pow(a.getFitness() - mean, 2)).sum();
		return Math.sqrt(sum / ani.size());}

	/**
	 * Standard deviation of the lifespan
	 * @return the standard deviation of the lifespan
	 **/
	public double stdDevLifespan() {
		ArrayList<Double> all_fitness = new ArrayList<>();
		for (Animat i: ani) {
			all_fitness.add((double) i.getLifeSpan());
		}
		double mean = all_fitness.stream().mapToDouble(val -> val).average().orElse(0.0);
		double sum = 0;
		for (double i: all_fitness) {
			sum += Math.pow(i - mean, 2);
		}
		return Math.sqrt(sum / all_fitness.size());
	}

	/**
	 * Mutates the animats
	 */
	public String[] save(){
		double bestFitness = bestFitness();
		double worstFitness = worstFitness();
		double meanMean = getMean();
		double stdDev = stdDevFitness();
		double bestLifespan = bestLifespan();
		double worstLifespan = worstLifeSpan();
		double meanLifespan = getMeanLifespan();
		double stdDevLifespan = stdDevLifespan();
		return new String[] {generation+"",bestFitness+"",worstFitness+"",meanMean+"",stdDev+"",bestLifespan+"",worstLifespan+"",meanLifespan+"",stdDevLifespan+""};
	}
}
