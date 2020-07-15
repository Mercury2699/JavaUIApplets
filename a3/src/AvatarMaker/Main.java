package AvatarMaker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Controller c;

    @Override
    public void start(Stage s) {
        Model m = new Model();
        c = new Controller(m, s);
        Scene Main = c.getViewer();
        s.setScene(Main);
        s.setHeight(800);
        s.setWidth(1200);
        s.setMinWidth(800);
        s.setMinHeight(400);
        s.show();
    }
}
