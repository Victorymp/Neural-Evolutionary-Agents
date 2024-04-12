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

    static Object[][] objects_list = new Object[20][20];
    private static ObjectCollection objects_arraylist;

    public Map(int size, ObjectCollection objects) {
        this.size = size;
        this.locations = objects;
    }

    public GridPane generateMap() {
        GridPane board = new GridPane();
        // 100 rectangles grid
        // 5x20 grid
        int count = 0;
        double sides = 40; // length of each box
        for (int y = 0; y < 20; y++) {
            count++;
            for (int x = 0; x < 20; x++) {
                Rectangle r = new Rectangle(sides, sides, sides, sides);
                Object obj = locations.inList(x, y);
                // creating a blue line for the water
                if (y == 2) {
                    r.setFill(obj.getColor());
                    Water water = new Water(x, y);
                } else if (isStone(x, y)) {
                    r.setFill(obj.getColor());
                    // System.out.println("x"+x+"y"+y);
                } else if (x == 10 & y == 19) {
                    r.setFill(obj.getColor());
                } else {
                    r.setFill(obj.getColor());
                }
                board.add(r, x, y);
            }
        }
        return board;

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

    public static ObjectCollection startCollection(){
        objects_arraylist = new ObjectCollection();
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                Grass grass = new Grass(x, y);
                Water water = new Water(x, y);
                Resource resource = new Resource(x, y);
                if(objects_arraylist == null) {
                    objects_arraylist = new ObjectCollection();
                }
                if(y == 2) {
                    objects_arraylist.addObject(water);
                } else if (isStone(x, y)) {
                    Stone stone = new Stone(x, y);
                    objects_arraylist.addObject(stone);
                } else if (x == 10 && y == 19) {
                    objects_arraylist.addObject(grass);
                } else if (x == 5 && y == 0) {
                    objects_arraylist.addObject(resource);
                } else {
                    objects_arraylist.addObject(grass);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (objects_arraylist.inList(i, j) == null) {
                    Grass grass = new Grass(i, j);
                    objects_arraylist.addObject(grass);
                }
            }
        }
        return objects_arraylist;
    }

    /**
     * Sets the stones for the map.map
     * @param x
     * @param y
     * @return
     */
    private static boolean isStone(int x, int y) {
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


}