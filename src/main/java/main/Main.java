package main;

import animat.AnimatCollection;
import map.Map;
import objects.*;
import objects.Object;
import rules.Rules;
import dataFrame.DataFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main extends JFrame {
    Object[][] objects_list = new Object[20][20];
    private static ObjectCollection objects_arraylist;
    private DataFrame df;

    public static void main(String[] args) {
		objects_arraylist = Map.startCollection();
		Map start_map = new Map(20, objects_arraylist);
		JPanel start_board = start_map.generateMap();
		SwingUtilities.invokeLater(() -> {
			Main frame = new Main();
			frame.setTitle("Social learning");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.add(start_board);
			frame.setSize(800, 800); // Adjust size as needed
			frame.setLocationRelativeTo(null); // Center window
			frame.setVisible(true);
		});
	}

    public Main() {
		df = new DataFrame();
		createMap();
		Rules rules = new Rules(this,objects_list, objects_arraylist);
		new SwingWorker<Void,Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				rules.Start();
				return null;
			}
		}.execute();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		// Draw the objects
	}

    public void createMap() {
        JPanel board = new JPanel(new GridLayout(20, 20));
        objects_arraylist = new ObjectCollection();

        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                JPanel cell = new JPanel();
                Grass grass = new Grass(x, y);
                Water water = new Water(x, y);

                if (y == 2) {
                    cell.setBackground(Color.BLUE);
                    objects_list[x][y] = water;
                    objects_arraylist.addObject(water);
                } else if (isStone(x, y)) {
                    cell.setBackground(Color.LIGHT_GRAY);
                } else if (x == 10 & y == 19) {
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

        add(board);
    }

    // Rest of your methods...
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

	// method that updates the map at the end of each day
	public void updateMap(ObjectCollection ob){
		this.getContentPane().removeAll();
		Map updatedBoard = new Map(20, ob);
		JPanel board = updatedBoard.generateMap();
		this.add(board);
		//this.pack();
		this.revalidate();
		this.repaint();
	}

	private void formWindowOpened(java.awt.event.WindowEvent e) {
		// TODO add your code here

	}
}

// Path: src/main/java/animat/AnimatCollection.java