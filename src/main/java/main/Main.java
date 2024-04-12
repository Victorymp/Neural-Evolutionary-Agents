package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import map.Map;
import map.ScatterController;
import objects.*;
import objects.Object;
import rules.Rules;
import dataFrame.DataFrame;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;


public class Main extends  Application {
    Object[][] objects_list = new Object[20][20];
    private static ObjectCollection objects_arraylist;
    private DataFrame df;
    private Stage primaryStage;
    private Scene scene;
    private Pane mapPane;
    private HBox hbox;


    @FXML
    public ScatterChart scatterChart;

    public static void main(String[] args) {
        objects_arraylist = Map.startCollection();
        launch(args);
    }

    public Main() throws IOException {
        df = new DataFrame();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    // Load the Map.fxml
    FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/com/Map.fxml"));
    Parent mapRoot = mapLoader.load();

    // Generate the map
    Map start_map = new Map(20, objects_arraylist);
    GridPane start_board = start_map.generateMap();

    // Set the scatter chart
    scatterChart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

    // Set labels for the scatter chart
    scatterChart.getXAxis().setLabel("Generation");
    scatterChart.getYAxis().setLabel("Mean Fitness");

    // Set the scatter chart to fill the entire width
    scatterChart.setPrefSize(700, 700);

    // Create a HBox and add the board and scatter chart to it
    this.hbox = new HBox();
    hbox.getChildren().add(start_board);
    hbox.getChildren().add(scatterChart);

    // set the Hbox to fill the entire width
    hbox.prefWidthProperty().bind(primaryStage.widthProperty());
    hbox.prefHeightProperty().bind(primaryStage.heightProperty());



    Scene mapScene = new Scene(hbox); // Use HBox here
    primaryStage.setTitle("Social learning");
    primaryStage.setScene(mapScene);
    primaryStage.setResizable(true);
    primaryStage.sizeToScene();
    primaryStage.centerOnScreen();
    // primaryStage.setFullScreen(true);
    primaryStage.show();

    // Then initialize the Rules class and start the generation process
    Rules rules = new Rules(this, objects_list, objects_arraylist);

    new SwingWorker<Void,Void>(){
        @Override
        protected Void doInBackground() throws Exception {
            rules.Start();
            return null;
        }
    }.execute();
    }

    // method that updates the map at the end of each generation
    /**
     * Update the map with the new object collection and the points
     * @param ob
     * @param points
     */
    public synchronized void updateMap(ObjectCollection ob, List<Point2D.Float> points){
    Platform.runLater(() -> {
        GridPane updatedBoard = new Map(20, ob).generateMap();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (Point2D.Float point : points) {
            series.getData().add(new XYChart.Data<>(point.x, point.y));
        }
        // Clear the scatter chart data and add the new data
        scatterChart.getData().clear();
        scatterChart.getData().add(series);

        // Set labels for the scatter chart
        scatterChart.getXAxis().setLabel("Generation");
        scatterChart.getYAxis().setLabel("Mean Fitness");
        // scatterChart.setPrefSize(1000, 1000);
        hbox.getChildren().clear();


        hbox.getChildren().add(updatedBoard);
        hbox.getChildren().add(scatterChart);
        hbox.prefWidthProperty().bind(primaryStage.widthProperty());
        hbox.prefHeightProperty().bind(primaryStage.heightProperty());
        hbox.autosize();
        hbox.setAlignment(Pos.CENTER);
        // set the Hbox to fill the entire width
        scatterChart.setPrefSize(700, 700);
    });
}


}