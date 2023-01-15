package UI;

import UI.controllers.RootController;
import UI.tasks.WatchDirTask;
import UI.utils.BundleUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    private FXMLLoader loader;
    private static Stage primaryStage;
    private RootController controller;

    private Thread watcherThread;
    double x, y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        Parent root = loader.load();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
        controller = loader.getController();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));
        Main.primaryStage = primaryStage;

        BundleUtil.getInstance().addObserver(loader.getController());

        watcherThread = new Thread(() -> {
            try {
                WatchDirTask.getInstance().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        watcherThread.start();
        // controller.task();
        //primaryStage.setMaximized(true);
        Main.primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        watcherThread.interrupt();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}