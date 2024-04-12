package map;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import main.Main;

public class ScatterController {

    @FXML
    private ScatterChart<Number, Number> scatterChart;
    private Main main;

    public void addPoint(Number x, Number y) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(x, y));
        scatterChart.getData().add(series);
    }
    
    public void setMain(Main main) {
        this.main = main;
        main.scatterChart = scatterChart;
}
}