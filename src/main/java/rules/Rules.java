package rules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import animat.Animat;
import animat.AnimatCollection;

import main.Main;
import objects.Object;
import objects.ObjectCollection;

public class Rules {
	private AnimatCollection ani_list;
	private Object[][] ob;
	private ArrayList<AnimatCollection> generation;
	private ObjectCollection ob_list;

    private ObjectCollection objectCollection;

	private int current_generation = 0;

	private Main main;

	private double lowest_fitness = 10000;

	List<Point2D.Float> points = new ArrayList<Point2D.Float>();

	List<Point2D.Float> lifespanPoints = new ArrayList<Point2D.Float>();


	private Animat best;

	public Rules(Main main, Object[][] ob, ObjectCollection ob_list) {
		this.main = main;
		this.ani_list = new AnimatCollection(0, ob_list);
		this.ob = ob;
		this.ob_list = ob_list;
		objectCollection = ob_list;
		generation = new ArrayList<>();
		best = new Animat(0, 0, 0, false, ob_list);
	}
	/**
	 * Starting point for the simulation
	 */
	public void Start() {
		ani_list.startGeneration(100);
		runGenerations(100);
	}

	public void runGenerations(int x) {
		for(int i = 0; i < x; i++) {
			System.out.println("Start");
			// Start the timer
			//long startTime = System.currentTimeMillis();
			System.out.println("Start of generation successfully");
			ani_list.runDays(500);
			System.out.println("Days ran successfully");
			ani_list.endOfGeneration();
			System.out.println("End of generation successfully");
			double mean = ani_list.getMean();
			double mean_lifespan = ani_list.getMeanLifespan();
			lifespanPoints.add(new Point2D.Float(current_generation, (float) mean_lifespan));
			points.add(new Point2D.Float(current_generation, (float) mean));
			best = ani_list.getBest_1();
			if(ani_list.getLowestFitness() < lowest_fitness) lowest_fitness = ani_list.getLowestFitness();
			System.out.println("Lowest fitness: "+lowest_fitness);
			generation.add(ani_list);
			ani_list = new AnimatCollection(i + 1, objectCollection, ani_list.getGenerationNeuralNetwork());
			ani_list.startGeneration(100);
			current_generation++;
			System.out.println("LifeSpan: "+best.getLifeSpan()+" ID: "+best.getId());
			//if(i % (x/3) == 0) main.updateMap(best.map().printAnimatJourney(best), points);
		}
		System.out.println("LifeSpan: "+best.getLifeSpan());
		main.updateMap(best.map(), points, lifespanPoints);
		System.out.println("End of simulation");
	}

	public int getGeneration() {
		return current_generation;
	}

	public void scatterPlot() {

	}
}