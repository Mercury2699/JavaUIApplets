package AvatarMaker;

import javafx.scene.control.SpinnerValueFactory;

enum Hair {Curly, Long, Short, Wavy};

enum Brows {Angry, Default, Sad};

enum Skin {Brown, Light, Lighter}

enum Eyes {Closed, Default, Wide};

enum Mouth {Default, Sad, Serious};

public class Model {
    Hair hair = Hair.Curly;
    Brows brows = Brows.Default;
    Skin skin = Skin.Light;
    Eyes eyes = Eyes.Default;
    Mouth mouth = Mouth.Default;

    public SpinnerValueFactory.IntegerSpinnerValueFactory browOffset = new SpinnerValueFactory.IntegerSpinnerValueFactory(-8, 8, 0);

    public void setHair(Hair h) {
        hair = h;
    }

    public void setBrows(Brows b) {
        brows = b;
    }

    public void setSkin(Skin s) {
        skin = s;
    }

    public void setEyes(Eyes e) {
        eyes = e;
    }

    public void setMouth(Mouth m) {
        mouth = m;
    }


}