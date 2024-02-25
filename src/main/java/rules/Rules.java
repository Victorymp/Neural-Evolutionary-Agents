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

	public Rules(AnimatCollection aniList, Object[][] ob, ObjectCollection ob_list) {
		this.ani_list = aniList;
		this.ob = ob;
		this.ob_list = ob_list;

		ani = ani_list.getGeneration();
	}
	/**
	 * Starting point for the simulation
	 */
	public void Start() {
		ani_list.setObjectCollection(ob_list);
		// ani_list.printAnimatJourney(87,"Inputs-read");
		ani_list.runDays(50);
		ob_list.objectSize();
		ob_list.displayActivationMap();
		//exit();
		//ani_list.locateStone();

		//System.out.println(ani.size());
	}

	public ObjectCollection getObjectCollection() {
		return ob_list;
	}
}