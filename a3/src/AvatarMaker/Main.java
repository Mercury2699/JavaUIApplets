package AvatarMaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    Controller c;

    @Override
    public void start(Stage s) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AvatarMaker/Viewer.fxml"));
        HBox roothbox = null;
        try {
            roothbox = loader.load();
            c = loader.getController();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        Scene Main = new Scene(roothbox);
        s.setScene(Main);
        s.setHeight(800);
        s.setWidth(1200);
        s.setMinWidth(800);
        s.setMinHeight(400);
        s.show();
    }
}
