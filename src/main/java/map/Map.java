package map;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import objects.*;
import objects.Object;

public class Map {
    private int size;
    private ObjectCollection locations;
    private Object[][] objectsList;
    private ObjectCollection objectsArrayList;

    public Map(int size, ObjectCollection objects) {
        this.size = size;
        this.locations = objects;
        this.objectsList = new Object[20][20];
        this.objectsArrayList = new ObjectCollection();
    }

    public GridPane generateMap() {
        GridPane board = new GridPane();
        double sides = 40; // length of each box
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                Rectangle r = createRectangle(sides);
                Object obj = locations.inList(x, y);
                r.setFill(obj.getColor());
                board.add(r, x, y);
            }
        }
        return board;
    }

    private Rectangle createRectangle(double sides) {
        return new Rectangle(sides, sides, sides, sides);
    }

    public void createMap(ObjectCollection objects) {
        this.locations = objects;
        Stage stage = new Stage();
        stage.setTitle("Map");
        GridPane board = generateMap();
        Scene scene = new Scene(board);
        stage.setScene(scene);
        stage.show();
    }

    public ObjectCollection startCollection(){
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                addObjectToCollection(x, y);
            }
        }
        fillRemainingWithGrass();
        return objectsArrayList;
    }

    private void addObjectToCollection(int x, int y) {
        if(y == 2) {
            addWater(x, y);
        } else if (isStone(x, y)) {
            addStone(x, y);
        } else if (x == 5 && y == 0) {
            addResource(x, y);
        } else {
            addGrass(x, y);
        }
    }

    private void addWater(int x, int y) {
        Water water = new Water(x, y);
        objectsArrayList.addObject(water);
    }

    private void addStone(int x, int y) {
        Stone stone = new Stone(x, y);
        objectsArrayList.addObject(stone);
        objectsList[x][y] = stone;
    }

    private void addResource(int x, int y) {
        Resource resource = new Resource(x, y);
        objectsArrayList.addObject(resource);
    }

    private void addGrass(int x, int y) {
        Grass grass = new Grass(x, y);
        objectsArrayList.addObject(grass);
    }

    private void fillRemainingWithGrass() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (objectsArrayList.inList(i, j) == null) {
                    addGrass(i, j);
                }
            }
        }
    }

    private boolean isStone(int x, int y) {
        return (x == 1 && y == 4) || (x == 2 && y == 12) || (x == 5 && y == 9) || (x == 9 && y == 13) ||
               (x == 12 && y == 10) || (x == 14 && y == 6) || (x == 18 && y == 12);
    }


}