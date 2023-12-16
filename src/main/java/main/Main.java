package main;

import animat.Animat;
import animat.AnimatCollection;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import objects.Object;
import objects.ObjectCollection;
import objects.Stone;
import objects.Water;
import rules.Rules;

public class Main extends Application {
	//2d array which hold the values for location of the items
	Object[][] objects = new Object[20][20];

	// holds the objects
	private ObjectCollection ob;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Social learning");

        // visual board
        GridPane board = new GridPane();

        //creating generations
        AnimatCollection generation = new AnimatCollection(1);
        generation.startGeneration(100);

        //
        ob = new ObjectCollection();

        // 100 rectangles grid
        // 5x20 grid
        int count = 0;
        double sides = 40; // length of each box
        for (int y = 0; y < 20; y++) {
        	count++;
        	for (int x = 0; x < 20; x++) {
        		Rectangle r = new Rectangle(sides,sides,sides,sides);
        		// creating a blue line for the water
        		if(y == 2){
                    r.setFill(Color.BLUE);
                    Water water = new Water(x,y);
                    objects[x][y] = water;
                    ob.addObject(water);
                } else if (isStone(x,y) ) {
                	r.setFill(Color.LIGHTGREY);
                	// System.out.println("x"+x+"y"+y);
                } else if(x == 10 & y == 19){
                	r.setFill(Color.BLACK);
                }
                else {
                	r.setFill(Color.GREEN);}
                board.add(r, x, y);
        	}
        }

        // System.out.println(objects[18][2]);
        //ob.createNewStack();
        Rules rules = new Rules(generation, objects, ob);
        board.setAlignment(Pos.CENTER);
        Scene scene = new Scene(board);
        primaryStage.setFullScreen(false);
        primaryStage.setScene(scene);
        //primaryStage.show();

    }

    /**
     * Sets the stones for the map
     * @param x
     * @param y
     * @return
     */
    private Boolean isStone(int x, int y) {
    	Stone st = new Stone(x,y);
    	if (x == 1 && y == 4) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 2 && y == 12) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 5 && y == 9) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 9 && y == 13) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 12 && y == 10) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 14 && y == 6) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}else if (x == 18 && y == 12) {
    		objects[x][y]=st;
    		ob.addObject(st);
    		return true;
    	}

    	return false;
    }
}