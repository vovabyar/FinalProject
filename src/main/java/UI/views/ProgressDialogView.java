package UI.views;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import UI.Main;
import UI.controllers.ProgressDialogController;
import UI.utils.BundleUtil;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author vovabyar
 */
public class ProgressDialogView {
    private Stage stage;
    private ProgressDialogController controller;

    public ProgressDialogView() throws IOException {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.initOwner(Main.getPrimaryStage());
        stage.initModality(Modality.NONE);
        stage.setTitle(bundle.getString("operationProgress"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProgressDialogLayout.fxml"));
        loader.setResources(bundle);
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

    public void setTask(Task < Void > task) {
        stage.setOnCloseRequest(event -> task.cancel());
        controller.setTask(task);
    }
}