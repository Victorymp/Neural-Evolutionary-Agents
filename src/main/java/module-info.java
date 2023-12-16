module com.sociallearning.sociallearning{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.opencsv;

    opens com.sociallearning.sociallearning to javafx.fxml;
    exports main;
}