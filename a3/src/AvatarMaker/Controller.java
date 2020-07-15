package AvatarMaker;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;

public class Controller {
    Model m;
    Stage s;
    SVGLoader svgLoader = new SVGLoader();
    Group hair = svgLoader.loadSVG("hair/hair_curly.svg");
    Group clothes = svgLoader.loadSVG("clothes.svg");
    FileChooser fileChooser = new FileChooser();
    @FXML
    ImageView brows;
    @FXML
    Group previewgroup;
    @FXML
    ImageView defaultBrows;
    @FXML
    Button savePNGButton;
    @FXML
    ImageView sadBrows;

    @FXML
    public void initialize() {
        previewgroup.getChildren().add(clothes);
        previewgroup.getChildren().add(hair);
        savePNGButton.setOnMouseClicked(mouseEvent -> savePNG());
        defaultBrows.setOnMouseClicked(mouseEvent -> setBrow(Brows.Default));
        sadBrows.setOnMouseClicked(mouseEvent -> setBrow(Brows.Sad));
//        ImageView angryBrowsImgView = (ImageView) Viewer.lookup("#angryBrows");
//        angryBrowsImgView.setOnMouseClicked(mouseEvent -> setBrow(Brows.Angry));
//        ImageView longHairImgView = (ImageView) Viewer.lookup("#longHair");
//        longHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Long));
//        ImageView curlyHairImgView = (ImageView) Viewer.lookup("#curlyHair");
//        curlyHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Curly));
//        ImageView shortHairImgView = (ImageView) Viewer.lookup("#shortHair");
//        shortHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Short));
//        ImageView wavyHairImgView = (ImageView) Viewer.lookup("#wavyHair");
//        wavyHairImgView.setOnMouseClicked(mouseEvent -> setHair(Hair.Wavy));
    }

    public Controller(Model model, Stage stage) {
        m = model;
        s = stage;
    }

    public void setBrow(Brows b) {
        m.setBrows(b);
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
        brows.setImage(newBrow);
    }

    public void setHair(Hair h) {
        m.setHair(h);
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
    }

    public void savePNG() {
        WritableImage save = previewgroup.snapshot(new SnapshotParameters(), null);
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
}
