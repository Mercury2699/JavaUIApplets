package AvatarMaker;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {
    Model m;
    Scene Viewer;
    Stage s;
    HBox roothbox;
    SVGLoader svgLoader = new SVGLoader();
    Group hair = svgLoader.loadSVG("hair/hair_curly.svg");
    Group clothes = svgLoader.loadSVG("clothes.svg");
    FileChooser fileChooser = new FileChooser();
    Group preview;

    public Controller(Model model, Stage stage) {
        m = model;
        s = stage;
        try {
            roothbox = FXMLLoader.load(getClass().getResource("/AvatarMaker/Viewer.fxml"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        Viewer = new Scene(roothbox);
        preview = (Group) Viewer.lookup("#previewgroup");
        preview.getChildren().add(clothes);
        preview.getChildren().add(hair);
        Button saveAsPNGBtn = (Button) Viewer.lookup("#savePNGButton");
        saveAsPNGBtn.setOnMouseClicked(mouseEvent -> savePNG());
        ImageView defBrowsImgView = (ImageView) Viewer.lookup("#defaultBrows");
        defBrowsImgView.setOnMouseClicked(mouseEvent -> setBrow(Brows.Default));
        ImageView sadBrowsImgView = (ImageView) Viewer.lookup("#sadBrows");
        sadBrowsImgView.setOnMouseClicked(mouseEvent -> setBrow(Brows.Sad));
        ImageView angryBrowsImgView = (ImageView) Viewer.lookup("#angryBrows");
        angryBrowsImgView.setOnMouseClicked(mouseEvent -> setBrow(Brows.Angry));
        ImageView longHairImgView = (ImageView) Viewer.lookup("#longHair");
        longHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Long));
        ImageView curlyHairImgView = (ImageView) Viewer.lookup("#curlyHair");
        curlyHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Curly));
        ImageView shortHairImgView = (ImageView) Viewer.lookup("#shortHair");
        shortHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Short));
        ImageView wavyHairImgView = (ImageView) Viewer.lookup("#wavyHair");
        wavyHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Wavy));
    }

    public void setBrow(Brows b) {
        m.setBrows(b);
        ImageView brow = (ImageView) Viewer.lookup("#brows");
        Image newBrow;
        switch (b) {
            case Default:
                newBrow = new Image("/brows/brows_default.png");
                break;
            case Sad:
                newBrow = new Image("/brows/brows_sad.png");
                break;
            case Angry:
                newBrow = new Image("/brows/brows_angry.png");
                break;
            default:
                newBrow = null;
                System.exit(1);
        }
        brow.setImage(newBrow);
    }

    public void setHair(Hair h) {
        m.setHair(h);
        preview.getChildren().remove(hair);
        switch (h) {
            case Long:
                hair = svgLoader.loadSVG("hair/hair_long.svg");
                break;
            case Curly:
                hair = svgLoader.loadSVG("hair/hair_curly.svg");
                break;
            case Wavy:
                hair = svgLoader.loadSVG("hair/hair_wavy.svg");
                break;
            case Short:
                hair = svgLoader.loadSVG("hair/hair_short.svg");
                break;
            default:
                System.exit(1);
        }
        preview.getChildren().add(hair);
    }

    public void savePNG() {
        WritableImage save = preview.snapshot(new SnapshotParameters(), null);
        FileChooser.ExtensionFilter extFltr = new FileChooser.ExtensionFilter("image files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFltr);
        fileChooser.setInitialFileName("Avatar.png");
        File file = fileChooser.showSaveDialog(s);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(save, null), "png", file);
            } catch (Exception s) {
                System.out.println(s.toString());
            }
        }
    }

    public Scene getViewer() {
        return Viewer;
    }
}
