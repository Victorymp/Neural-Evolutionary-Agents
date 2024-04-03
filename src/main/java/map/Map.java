package map;

import objects.Object;
import objects.*;

import javax.swing.*;
import java.awt.*;

public class Map {
    private int size;
    private ObjectCollection locations;

    static Object[][] objects_list = new Object[20][20];
    private static ObjectCollection objects_arraylist;

    public Map(int size, ObjectCollection objects) {
        this.size = size;
        this.locations = objects;
    }

    public JPanel generateMap() {
        JPanel board = new JPanel(new GridLayout(size, size));
        int sides = 40; // length of each box

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                JPanel cell = new JPanel();
                Object obj = locations.inList(x, y);
                cell.setBackground(obj.getColor());
                board.add(cell);
            }
        }

        return board;
    }

    // takes a list of objects and creates a map
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

    public void createMap() {
        JFrame frame = new JFrame("Map");
        JPanel board = new JPanel(new GridLayout(20, 20));
        ObjectCollection objects_arraylist = new ObjectCollection();

        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                JPanel cell = new JPanel();
                Grass grass = new Grass(x, y);
                Water water = new Water(x, y);

                if (y == 2) {
                    cell.setBackground(Color.BLUE);
                    Water[][] objects_list = new Water[0][];
                    objects_list[x][y] = water;
                    objects_arraylist.addObject(water);
                } else if (isStone(x, y)) {
                    cell.setBackground(Color.LIGHT_GRAY);
                } else if (x == 10 && y == 19) {
                    cell.setBackground(Color.BLACK);
                    objects_arraylist.addObject(grass);
                } else {
                    objects_arraylist.addObject(grass);
                    cell.setBackground(Color.GREEN);
                }
                board.add(cell);
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

        frame.add(board);
    }

    public static ObjectCollection startCollection(){
        objects_arraylist = new ObjectCollection();
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                JPanel cell = new JPanel();
                Grass grass = new Grass(x, y);
                Water water = new Water(x, y);
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