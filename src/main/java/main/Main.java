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
		//createMap();
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
		// Draw the objects
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
}

// Path: src/main/java/animat/AnimatCollection.java