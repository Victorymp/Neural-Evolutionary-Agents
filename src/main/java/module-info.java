module com.sociallearning.sociallearning {
    requires javafx.controls;
    requires javafx.fxml;

    opens main to javafx.fxml;
    opens map to javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.opencsv;
    requires java.desktop;

    exports main;
    exports neuralNetwork;
    exports map;
    exports rules;
    exports objects;
}