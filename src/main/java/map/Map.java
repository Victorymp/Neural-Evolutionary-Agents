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
    private int currentMap;

    public Map(int size, ObjectCollection objects) {
        this.size = size;
        this.locations = objects;
        this.objectsList = new Object[20][20];
        this.objectsArrayList = new ObjectCollection();
        currentMap =3;
    }

    public Map(int size, int currentMap) {
        this.size = size;
        this.currentMap = currentMap;
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
    }

    public GridPane generateMap() {
        GridPane board = new GridPane();
        double sides = 10; // length of each box
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
        switch (currentMap) {
            case 1:
                if(y == 2) {
                    addWater(x, y);
                } else if (isStoneMap1(x, y)) {
                    addStone(x, y);
                } else if (x == 5 && y == 0) {
                    addResource(x, y);
                } else {
                    addGrass(x, y);
                }
                break;
            case 2:
                if(y == 2 || y == 3) {
                    addWater(x, y);
                } else if (isStoneMap2(x, y)) {
                    addStone(x, y);
                } else if (x == 12 && y == 1) {
                    addResource(x, y);
                } else if (x == 7 && y == 10 || x == 11 && y == 15) {
                    addTrap(x, y);
                } else {
                    addGrass(x, y);
                }
                break;
            case 3:
                if (isStoneMap3(x, y)) {
                    addStone(x, y);
                } else if (x == 5 && y == 0) {
                    addResource(x, y);
                } else if (x == 7 && y == 10) {
                    addTrap(x, y);
                } else {
                    addGrass(x, y);
                }
                break;
        }
    }

    private void addTrap(int x, int y) {
        Trap trap = new Trap(x, y);
        objectsArrayList.addObject(trap);
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

    public Object map1(Object ob) {
        if (ob.getY() != 2 && ob.getClass() == Water.class) {
            ob = new Grass(ob.getX(), ob.getY());
        } if(isStoneMap1(ob.getX(), ob.getY())) {
            ob = new Stone(ob.getX(), ob.getY());
        } if (ob.getX() == 7 && ob.getY() == 10) {
            ob = new Trap(ob.getX(), ob.getY());
        } if (ob.getX() == 5 && ob.getY() == 0) {
            ob = new Resource(ob.getX(), ob.getY());
        }
        return ob;
    }

    /**
     * Sets the stones for the map.map
     * @param x
     * @param y
     * @return
     */
    private static boolean isStoneMap1(int x, int y) {
        return (x == 1 && y == 4) || (x == 2 && y == 12) || (x == 5 && y == 9) || (x == 9 && y == 13) ||
               (x == 12 && y == 10) || (x == 14 && y == 6) || (x == 18 && y == 12);
    }

    public Object map2(Object ob) {
        if ((ob.getY() != 2 && ob.getClass() == Water.class) || (ob.getY() !=3 && ob.getClass() == Water.class) ) {
            ob = new Grass(ob.getX(), ob.getY());
        } if(isStoneMap2(ob.getX(), ob.getY())) {
            ob = new Stone(ob.getX(), ob.getY());
        } if (ob.getX() == 7 && ob.getY() == 10) {
            ob = new Trap(ob.getX(), ob.getY());
        } if (ob.getX() == 5 && ob.getY() == 0) {
            ob = new Resource(ob.getX(), ob.getY());
        }
        return ob;
    }

    /**
     * Sets the stones for the map.map
     * @param x
     * @param y
     * @return true if the object is a stone
     */
    private static boolean isStoneMap2(int x, int y) {
        return (x == 1 && y == 7) || (x == 3 && y == 10) || (x == 5 && y == 16) || (x == 6 && y == 14) ||
               (x == 7 && y == 16) || (x == 9 && y == 6) || (x == 12 && y == 13);
    }

    public Object map3(Object ob) {
        // ... code to create the map
        if(isStoneMap3(ob.getX(), ob.getY())) {
            ob = new Stone(ob.getX(), ob.getY());
        } if (ob.getX() == 7 && ob.getY() == 10) {
            ob = new Trap(ob.getX(), ob.getY());
        } if (ob.getX() == 5 && ob.getY() == 0) {
            ob = new Resource(ob.getX(), ob.getY());
        }
        return ob;
    }
    /**
     * Sets the stones for the map.map
     * @param x
     * @param y
     * @return
     */
    private static boolean isStoneMap3(int x, int y) {
        return (x == 1 && y == 4) || (x == 2 && y == 12) || (x == 5 && y == 9) || (x == 9 && y == 13) ||
                       (x == 12 && y == 10) || (x == 14 && y == 6) || (x == 18 && y == 12);
    }
}