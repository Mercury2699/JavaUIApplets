package AvatarMaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    Controller c;

    @Override
    public void start(Stage s) {
        Model m = new Model();
        c = new Controller(m, s);
        Pane rootpane = null;
        try {
            rootpane = FXMLLoader.load(getClass().getResource("/AvatarMaker/Viewer.fxml"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        Scene Main = new Scene(rootpane);
        s.setScene(Main);
        s.setHeight(800);
        s.setWidth(1200);
        s.setMinWidth(800);
        s.setMinHeight(400);
        s.show();
    }
}
