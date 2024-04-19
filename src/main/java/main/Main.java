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
import javafx.scene.layout.VBox;
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
    private static ObjectCollection objects_arraylist;
    private Stage primaryStage;
    private HBox hbox;
    private final int chartWidth = 300;
    private final int chartHeight = 300;

    private static Map map;


    @FXML
    public ScatterChart scatterChart;

    @FXML
    public ScatterChart scatterChartLifespan;

    public static void main(String[] args) {
        map = new Map(20, new ObjectCollection());
        objects_arraylist = map.startCollection();
        launch(args);
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

        // Set the first scatter chart
        scatterChart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

        // Set labels for the first scatter chart
        scatterChart.getXAxis().setLabel("Generation");
        scatterChart.getYAxis().setLabel("Average Fitness");

        // Set the second scatter chart for average lifespan
        scatterChartLifespan = new ScatterChart<>(new NumberAxis(), new NumberAxis());

        // Set labels for the second scatter chart
        scatterChartLifespan.getXAxis().setLabel("Generation");
        scatterChartLifespan.getYAxis().setLabel("Average Lifespan");

        // Set the scatter charts to fill the entire width
        scatterChart.setPrefSize(chartWidth, chartHeight);
        scatterChartLifespan.setPrefSize(chartWidth, chartHeight);

        // Create a VBox and add the scatter charts to it
        VBox vbox = new VBox();
        vbox.getChildren().add(scatterChart);
        vbox.getChildren().add(scatterChartLifespan);

        // Create a HBox and add the board and VBox to it
        this.hbox = new HBox();
        hbox.getChildren().add(start_board);
        hbox.getChildren().add(vbox);

        // set the Hbox to fill the entire width
        hbox.prefWidthProperty().bind(primaryStage.widthProperty());
        hbox.prefHeightProperty().bind(primaryStage.heightProperty());

        Scene mapScene = new Scene(hbox); // Use HBox here
        primaryStage.setTitle("Social learning");
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setScene(mapScene);
        primaryStage.setResizable(true);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        // primaryStage.setFullScreen(true);
        primaryStage.show();

        // Then initialize the Rules class and start the generation process
        Rules rules = new Rules(this, objects_arraylist);
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
    public synchronized void updateMap(ObjectCollection ob, List<Point2D.Float> points, List<Point2D.Float> lifespanPoints){
        Platform.runLater(() -> {
            GridPane updatedBoard = new Map(20, ob).generateMap();
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            List<Point2D.Float> pointsCopy = new ArrayList<>(points);
            for (Point2D.Float point : pointsCopy) {
                series.getData().add(new XYChart.Data<>(point.x, point.y));
            }
            // Clear the scatter chart data and add the new data
            scatterChart.getData().clear();
            scatterChart.getData().add(series);

            // Create a new series for the average lifespan data
            XYChart.Series<Number, Number> lifespanSeries = new XYChart.Series<>();
            List<Point2D.Float> lifespanPointsCopy = new ArrayList<>(lifespanPoints);
            for (Point2D.Float point : lifespanPointsCopy) {
                lifespanSeries.getData().add(new XYChart.Data<>(point.x, point.y));
            }
            // Clear the second scatter chart data and add the new data
            scatterChartLifespan.getData().clear();
            scatterChartLifespan.getData().add(lifespanSeries);

            // Set the scatter charts to fill the entire width
            scatterChart.setPrefSize(chartWidth, chartHeight);
            scatterChartLifespan.setPrefSize(chartWidth, chartHeight);

            // set the size of the points in the scatter chart

            VBox vbox = new VBox();
            vbox.getChildren().add(scatterChart);
            vbox.getChildren().add(scatterChartLifespan);


            hbox.getChildren().clear();
            hbox.getChildren().add(updatedBoard);
            hbox.getChildren().add(vbox);
            hbox.prefWidthProperty().bind(primaryStage.widthProperty());
            hbox.prefHeightProperty().bind(primaryStage.heightProperty());
            //hbox.autosize();
        });
    }
}