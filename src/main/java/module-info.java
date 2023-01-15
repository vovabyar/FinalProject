module UI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.io;
    requires org.testng;
    requires json.simple;
    requires junit;
    requires java.logging;

    opens UI to javafx.fxml;
    opens UI.controllers to javafx.fxml;
    opens UI.comparators to javafx.fxml;
    opens UI.fileOperations to javafx.fxml;
    opens UI.models to javafx.base;
    opens UI.tasks to javafx.fxml;
    opens UI.utils to javafx.fxml;
    opens UI.views to javafx.fxml;

    exports UI;
}