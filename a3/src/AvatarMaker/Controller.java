package AvatarMaker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Controller {
    Model m;
    HBox Viewer;
    SVGLoader svgLoader = new SVGLoader();
    @FXML
    private StackPane midPane;

    public Controller(Model model) {
        m = model;
        try {
            Viewer = FXMLLoader.load(getClass().getResource("/AvatarMaker/Viewer.fxml"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
//        midPane.getChildren().add(svgLoader.loadSVG("resources/clothes.svg "));
//        midPane.getChildren().add(svgLoader.loadSVG("resources/hair/hair_curly.svg "));
    }

    public HBox getViewer() {
        return Viewer;
    }
}
