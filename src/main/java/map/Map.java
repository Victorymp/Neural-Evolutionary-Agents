package map;


import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import objects.Object;
import objects.*;

public class Map {
    private int size;
    private ObjectCollection locations;

    public Map(int size, ObjectCollection objects) {
        this.size = size;
        this.locations = objects;
    }

    public GridPane generateMap() {
        GridPane board = new GridPane();
        double sides = 40; // length of each box

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Rectangle r = new Rectangle(sides, sides, sides, sides);
                Object obj = locations.inList(x, y);
                r.setFill(obj.getColor());
                board.add(r, x, y);
            }
        }

        return board;
    }

    // takes an list of objects and creates a map
    public static Map createMap(ObjectCollection objects) {
        int size = 20;
        Object[][] locations = new Object[size][size];
        ObjectCollection tmp = new ObjectCollection();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Object obj = objects.inList(x, y);
                tmp.addObject(obj);
            }
        }

        return new Map(size, tmp);
    }

    // creates a scene with the map
    public Scene createScene() {
        GridPane board = generateMap();
        return new Scene(board, 800, 800);
    }
/*
    public ObjectCollection getActivationsMap(ObjectCollection objects){

        for(ObjectCollection ob: objects){
            if(ob.getClass() == Stone.class){
                ob.setIota(0);
            }
        }
        return objects;
    }*/
}
