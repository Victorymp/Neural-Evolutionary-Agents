package rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import animat.Animat;
import animat.AnimatCollection;

import objects.Object;
import objects.ObjectCollection;
import objects.Water;

import dataFrame.DataFrame;

import static javafx.application.Platform.exit;

public class Rules {
	private AnimatCollection ani_list;
	private Object[][] ob;
	private ArrayList<AnimatCollection> generation;
	private ObjectCollection ob_list;

	private ArrayList<Animat> ani;

	private ObjectCollection objectCollection;

	public Rules( Object[][] ob, ObjectCollection ob_list) {
		this.ani_list = new AnimatCollection(0, ob_list);
		this.ob = ob;
		this.ob_list = ob_list;
		ani_list.startGeneration(100);
		ani = ani_list.getGeneration();
		objectCollection = ob_list;
		generation = new ArrayList<>();
	}
	/**
	 * Starting point for the simulation
	 */
	public void Start() {
		//ani_list.runDays(50);
		//ob_list.objectSize();
		//ob_list.displayActivationMap();
		//fitness();
		//exit();
		//ani_list.locateStone();

		//System.out.println(ani.size());
		runGenerations(3);
	}

	public ObjectCollection getObjectCollection() {
		return ob_list;
	}

	public void fitness() {
		ArrayList<Double> map =new ArrayList<Double>();
		ArrayList<Object> temp = objectCollection.getLists();
		// Checks the current object list
		for (Object i: temp){
			map.add(i.getIota());
		}
		// Check animat list
		for(Animat a: ani){
			for (Object o: a.map().getLists()){
				for(Object i: temp){
					if(o == i){
						a.addFitness();
					}
				}
			}
			a.fitness();
		}

	}

	public void runGenerations(int x) {
		for(int i = 0; i < x; i++) {
			ani_list.runDays(50);
			ob_list.objectSize();
			//ob_list.displayActivationMap();
			fitness();
			generation.add(ani_list);
			int size = ani_list.getSize();
			ani_list = new AnimatCollection(i+1, ob_list);
			ani_list.startGeneration(size);

		}
	}
}