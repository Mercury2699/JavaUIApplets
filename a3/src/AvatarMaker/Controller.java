package AvatarMaker;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;

public class Controller {
    Model m = new Model();
    Stage s;
    SVGLoader svgLoader = new SVGLoader();

    Group hair = svgLoader.loadSVG("hair/hair_curly.svg");
    Group clothes = svgLoader.loadSVG("clothes.svg");

    FileChooser fileChooser = new FileChooser();
    DropShadow ds = new DropShadow(5, Color.BLUE);
    Node currentlySelected;

    @FXML
    ImageView brows;
    @FXML
    ImageView skin;
    @FXML
    ImageView eyes;
    @FXML
    ImageView mouth;
    @FXML
    Group previewgroup;

    @FXML
    Button savePNGButton;

    // Accordion Selects
    @FXML
    ImageView defaultBrows;
    @FXML
    ImageView sadBrows;
    @FXML
    ImageView angryBrows;

    @FXML
    ImageView longHair;
    @FXML
    ImageView curlyHair;
    @FXML
    ImageView shortHair;
    @FXML
    ImageView wavyHair;

    @FXML
    ImageView defaultMouth;
    @FXML
    ImageView sadMouth;
    @FXML
    ImageView seriousMouth;

    @FXML
    ImageView brownSkin;
    @FXML
    ImageView lightSkin;
    @FXML
    ImageView lighterSkin;

    @FXML
    ImageView closedEyes;
    @FXML
    ImageView defaultEyes;
    @FXML
    ImageView wideEyes;

    @FXML
    ColorPicker colorPicker;
    @FXML
    Spinner spinner;
    @FXML
    Label spinnerLabel;
    @FXML
    Slider slider;
    @FXML
    Label sliderLabel;

    @FXML
    public void initialize() {
        previewgroup.getChildren().add(clothes);
        previewgroup.getChildren().add(hair);
        savePNGButton.setOnMouseClicked(mouseEvent -> savePNG());
        defaultBrows.setOnMouseClicked(mouseEvent -> setBrow(Brows.Default));
        sadBrows.setOnMouseClicked(mouseEvent -> setBrow(Brows.Sad));
        angryBrows.setOnMouseClicked(mouseEvent -> setBrow(Brows.Angry));
        longHair.setOnMouseClicked(mouseEvent -> setHair(Hair.Long));
        curlyHair.setOnMouseClicked(mouseEvent -> setHair(Hair.Curly));
        shortHair.setOnMouseClicked(mouseEvent -> setHair(Hair.Short));
        wavyHair.setOnMouseClicked(mouseEvent -> setHair(Hair.Wavy));
        brownSkin.setOnMouseClicked(mouseEvent -> setSkin(Skin.Brown));
        lightSkin.setOnMouseClicked(mouseEvent -> setSkin(Skin.Light));
        lighterSkin.setOnMouseClicked(mouseEvent -> setSkin(Skin.Lighter));
        defaultMouth.setOnMouseClicked(mouseEvent -> setMouth(Mouth.Default));
        sadMouth.setOnMouseClicked(mouseEvent -> setMouth(Mouth.Sad));
        seriousMouth.setOnMouseClicked(mouseEvent -> setMouth(Mouth.Serious));
        closedEyes.setOnMouseClicked(mouseEvent -> setEyes(Eyes.Closed));
        defaultEyes.setOnMouseClicked(mouseEvent -> setEyes(Eyes.Default));
        wideEyes.setOnMouseClicked(mouseEvent -> setEyes(Eyes.Wide));
        brows.setOnMouseEntered(MouseEvent -> brows.setEffect(ds));
        brows.setOnMouseExited(MouseEvent -> brows.setEffect(null));
        eyes.setOnMouseEntered(MouseEvent -> eyes.setEffect(ds));
        eyes.setOnMouseExited(MouseEvent -> eyes.setEffect(null));
        colorPicker.setOnAction(Event -> ((SVGPath) currentlySelected).setFill(colorPicker.getValue()));
        eyes.setOnMouseClicked(MouseEvent -> {
            currentlySelected = eyes;
            slider.setVisible(true);
            sliderLabel.setVisible(true);
        });
        slider.valueProperty().addListener(ChangeListener -> {
            eyes.setScaleX(slider.getValue());
            eyes.setScaleY(slider.getValue());
        });
        brows.setOnMouseClicked(MouseEvent -> {
            currentlySelected = brows;
            spinner.setVisible(true);
            spinnerLabel.setVisible(true);
            spinner.setValueFactory(m.browOffset);
        });
        spinner.valueProperty().addListener(ChangeListener -> {
            int value = (Integer) spinner.getValueFactory().getValue();
//            Rectangle2D viewportRect;
//            if(value < 0){
//                viewportRect = new Rectangle2D(0, 0, 200, 200+value);
//            } else {
//                viewportRect = new Rectangle2D(0, -value, 200, 200);
//            }
//            brows.setViewport(viewportRect);
//            brows.setY(-value);
            brows.setLayoutY(-value);
//            brows.setTranslateY(-value);
        });
        for (Node path : hair.getChildren()) {
            path.setOnMouseClicked(MouseEvent -> {
                currentlySelected = path;
                SVGPath svg = (SVGPath) path;
                colorPicker.setVisible(true);
                colorPicker.setValue((Color) svg.getFill());
            });
        }
        for (Node path : clothes.getChildren()) {
            path.setOnMouseClicked(MouseEvent -> {
                currentlySelected = path;
                SVGPath svg = (SVGPath) path;
                colorPicker.setVisible(true);
                colorPicker.setValue((Color) svg.getFill());
            });
        }
    }

    public void setEyes(Eyes e) {
        m.setEyes(e);
        Image newEyes;
        switch (e) {
            case Default:
                newEyes = new Image("/eyes/eyes_default.png");
                break;
            case Wide:
                newEyes = new Image("/eyes/eyes_wide.png");
                break;
            case Closed:
                newEyes = new Image("/eyes/eyes_closed.png");
                break;
            default:
                newEyes = null;
                System.exit(1);
        }
        eyes.setImage(newEyes);
    }

    public void setMouth(Mouth mou) {
        m.setMouth(mou);
        Image newMouth;
        switch (mou) {
            case Sad:
                newMouth = new Image("/mouth/mouth_sad.png");
                break;
            case Default:
                newMouth = new Image("/mouth/mouth_default.png");
                break;
            case Serious:
                newMouth = new Image("/mouth/mouth_serious.png");
                break;
            default:
                newMouth = null;
                System.exit(1);
        }
        mouth.setImage(newMouth);
    }

    public void setSkin(Skin s) {
        m.setSkin(s);
        Image newSkin;
        switch (s) {
            case Brown:
                newSkin = new Image("/skin/skin_brown.png");
                break;
            case Light:
                newSkin = new Image("/skin/skin_light.png");
                break;
            case Lighter:
                newSkin = new Image("/skin/skin_lighter.png");
                break;
            default:
                newSkin = null;
                System.exit(1);
        }
        skin.setImage(newSkin);
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
        previewgroup.getChildren().remove(hair);
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
        previewgroup.getChildren().add(hair);
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
