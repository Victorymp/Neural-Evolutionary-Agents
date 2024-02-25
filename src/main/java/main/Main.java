package main;

import animat.AnimatCollection;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import map.Map;
import objects.*;
import objects.Object;
import rules.Rules;
import dataFrame.DataFrame;

public class Main extends Application {
	//2d array which hold the values for location of the items
	Object[][] objects_list = new Object[20][20];

	// holds the objects
	private ObjectCollection objects_arraylist;

	private DataFrame df;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
		df = new DataFrame();
        primaryStage.setTitle("Social learning");
        // visual board
        createMap();
		//creating generations
		AnimatCollection generation = new AnimatCollection(1, objects_arraylist);
		//starts generation with 100 animats
		generation.startGeneration(100);
        Rules rules = new Rules(generation, objects_list, objects_arraylist);
		rules.Start();
		Map activation_map = Map.createMap(rules.getObjectCollection());
		activation_map.generateMap();
        primaryStage.setScene(activation_map.createScene());
		primaryStage.show();


    }

    /**
     * Sets the stones for the map.map
     * @param x
     * @param y
     * @return
     */
    private Boolean isStone(int x, int y) {
    	Stone st = new Stone(x,y);
    	if (x == 1 && y == 4) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 2 && y == 12) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 5 && y == 9) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 9 && y == 13) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 12 && y == 10) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 14 && y == 6) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}else if (x == 18 && y == 12) {
    		objects_list[x][y]=st;
    		objects_arraylist.addObject(st);
    		return true;
    	}

    	return false;
    }

	public void createMap(){
		GridPane board = new GridPane();
		objects_arraylist = new ObjectCollection();
		// 100 rectangles grid
		// 5x20 grid
		int count = 0;
		double sides = 40; // length of each box
		for (int y = 0; y < 20; y++) {
			count++;
			for (int x = 0; x < 20; x++) {
				Rectangle r = new Rectangle(sides, sides, sides, sides);
				Grass grass = new Grass(x, y);
				Water water = new Water(x, y);
				// creating a blue line for the water
				if (y == 2) {
					r.setFill(Color.BLUE);
					objects_list[x][y] = water;
					objects_arraylist.addObject(water);
				} else if (isStone(x, y)) {
					r.setFill(Color.LIGHTGREY);
					// System.out.println("x"+x+"y"+y);
				} else if (x == 10 & y == 19) {
					r.setFill(Color.BLACK);
					objects_arraylist.addObject(grass);
				} else {
					objects_arraylist.addObject(grass);
					r.setFill(Color.GREEN);
				}
				board.add(r, x, y);
			}
		}
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(objects_arraylist.inList(i,j) == null) {
					Grass grass = new Grass(i, j);
					objects_arraylist.addObject(grass);
				}
			}
		}
	}
}