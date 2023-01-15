package UI.views;

import UI.controllers.SimpleFileEditorController;
import UI.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author vovabyar
 */
public class SimpleFileEditor {
    private Stage stage;
    private SimpleFileEditorController controller;

    public SimpleFileEditor() throws IOException {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.initOwner(Main.getPrimaryStage());
        stage.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SimpleFileEditor.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
    }
    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }
    public void setTask(Path path) {
        File fileToLoad = new File(path.toString());
        if (fileToLoad != null) {
            controller.loadFileToTextArea(fileToLoad);
        }
    }
}