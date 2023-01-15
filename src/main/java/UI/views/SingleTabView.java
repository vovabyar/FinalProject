package UI.views;

import UI.utils.BundleUtil;
import UI.controllers.SingleTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

/**
 * @author vovabyar
 */
public class SingleTabView {
    private AnchorPane layout;
    private SingleTabController controller;
    private FXMLLoader loader;

    double x, y = 0;

    public SingleTabView() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/TabEdittor.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        layout = loader.load();

        layout.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        controller = loader.getController();
        BundleUtil.getInstance().addObserver(controller);
    }

    public Pane getLayout() {
        return layout;
    }

    public SingleTabController getController() {
        return controller;
    }
}