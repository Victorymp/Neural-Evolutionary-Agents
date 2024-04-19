package rules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import animat.Animat;
import animat.AnimatCollection;

import dataFrame.DataFrame;
import main.Main;
import map.Map;
import objects.Object;
import objects.ObjectCollection;

public class Rules {
	private AnimatCollection ani_list;
	private ArrayList<AnimatCollection> generation;

    private ObjectCollection objectCollection;

	private int current_generation = 0;

	private Main main;

	private double lowest_fitness = 10000;

	List<Point2D.Float> points = new ArrayList<Point2D.Float>();

	List<Point2D.Float> lifespanPoints = new ArrayList<Point2D.Float>();

	private DataFrame dataFrame;


	private Animat best;

	private Map map;

	public Rules(Main main, ObjectCollection ob_list) {
		this.main = main;
		this.ani_list = new AnimatCollection(0, ob_list);
		objectCollection = ob_list;
		generation = new ArrayList<>();
		best = new Animat(0, 0, 0, ob_list);
		dataFrame = new DataFrame();
		objectCollection.setMap(new Map(20, objectCollection));
	}
	/**
	 * Starting point for the simulation
	 */
	public void Start() {
		ani_list.startGeneration(500);
		runGenerations(3000);
	}

	public void runGenerations(int x) {
		for(int i = 0; i < x; i++) {
			System.out.println("Start");
			// Start the timer
			//long startTime = System.currentTimeMillis();
			ani_list.runDays(100);
			ani_list.endOfGeneration();
			double mean = ani_list.getMean();
			double mean_lifespan = ani_list.getMeanLifespan();
			lifespanPoints.add(new Point2D.Float(current_generation, (float) mean_lifespan));
			points.add(new Point2D.Float(current_generation, (float) mean));
			best = ani_list.getBest_1();
			if(ani_list.getLowestFitness() < lowest_fitness) lowest_fitness = ani_list.getLowestFitness();
			generation.add(ani_list);
			ani_list = new AnimatCollection(i + 1, objectCollection, ani_list.getGenerationNeuralNetwork());
			ani_list.startGeneration(500);
			current_generation++;
			if(best != null){
				if(i % (x/3) == 0) main.updateMap(best.map(), points, lifespanPoints);
			}
		}
		System.out.println("LifeSpan: "+best.getLifeSpan());
		main.updateMap(best.map(), points, lifespanPoints);
		System.out.println("End of simulation");
		// Use sparingly as performance is affected
		save();
	}

	public int getGeneration() {
		return current_generation;
	}

	public void save(){
		ArrayList<String[]> data = new ArrayList<>();
		for(AnimatCollection ani : generation){
			data.add(ani.save());
		}
		dataFrame.save("Generations", data, new String[]  {"Generation","Best Fitness","Worst Fitness","Mean Fitness","Standard Deviation","Best Lifespan","Worst Lifespan","Mean Lifespan","Standard Deviation Lifespan"});
	}
}