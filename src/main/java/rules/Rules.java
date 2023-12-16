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
;		Start();
	}

	public void Start() {
		ani_list.runDays(50);
		ani_list.printAnimatJourney(5);
		//System.out.println(ani.size());
	}

	public void day() {
		
	}
}