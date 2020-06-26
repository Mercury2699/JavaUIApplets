import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static javafx.scene.paint.Color.*;


public class MiniGame extends Application {

    static final int DEFAULT_VELOCITY = 150;
    static final int W_WIDTH = 1280;
    static final int W_HEIGHT = 640;
    static final int BASELINE = 300;

    protected Stage mainStage;
    protected Pane canvas;
    protected int remainingEnemies = 10;
    protected int playerLives = 3;
    protected int scor = 0;
    protected int lev = 0;
    protected boolean halted = false;

    Image idle = new Image("./resources/img/idle.png");
    Image fire = new Image("./resources/img/fire.png");

    ImageView background = new ImageView(new Image("./resources/img/background2.png"));
    ImageView player = new ImageView(idle);
    Label live = new Label();
    Label score = new Label();
    Label remain = new Label();
    ArrayList<AnimationTimer> enemyTimers;
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> genEnemy()),
            new KeyFrame(Duration.seconds(1), e -> genEnemy()),
            new KeyFrame(Duration.seconds(2), e -> genEnemy()),
            new KeyFrame(Duration.seconds(3), e -> genEnemy()),
            new KeyFrame(Duration.seconds(4), e -> genEnemy()),
            new KeyFrame(Duration.seconds(5), e -> genEnemy()),
            new KeyFrame(Duration.seconds(6), e -> genEnemy()),
            new KeyFrame(Duration.seconds(7), e -> genEnemy()),
            new KeyFrame(Duration.seconds(8), e -> genEnemy()),
            new KeyFrame(Duration.seconds(9), e -> genEnemy()),
            new KeyFrame(Duration.seconds(10), e -> genEnemy())
    );

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        canvas = FXMLLoader.load(getClass().getResource("MiniGame.fxml"));
        Scene opening = new Scene(canvas);
        opening.setOnKeyPressed(keyEvent -> {
            printEvent(keyEvent);
            if (keyEvent.getCode() == KeyCode.Q){
                System.exit(0);
            } else if (keyEvent.getCode() == KeyCode.ENTER){
                mainStage.setScene(GenLevel(1));
                mainStage.show();
            } else if (keyEvent.getCode() == KeyCode.getKeyCode("1")){
                mainStage.setScene(GenLevel(1));
                mainStage.show();
            } else if (keyEvent.getCode() == KeyCode.getKeyCode("2")){
                mainStage.setScene(GenLevel(2));
                mainStage.show();
            } else if (keyEvent.getCode() == KeyCode.getKeyCode("3")){
                mainStage.setScene(GenLevel(3));
                mainStage.show();
            }
        });
        stage.setTitle("MiniGame");
        stage.setScene(opening);
        stage.show();
    }

    protected Pane initiate(){
        enemyTimers = new ArrayList<AnimationTimer>();
        background.setFitHeight(W_HEIGHT);
        background.setFitWidth(W_WIDTH);
        player.setX((float)1280/2-idle.getWidth()/2);
        player.setY(BASELINE);
        live.setText("Lives: "+playerLives);
        live.setFont(new Font(28));
        live.setTextFill(WHITE);
        score.setText("Score: " + scor);
        score.setFont(new Font(28));
        score.setTextFill(WHITE);
        remain.setText("Remaining Enemies: "+ remainingEnemies);
        remain.setFont(new Font(28));
        remain.setTextFill(WHITE);
        live.setLayoutY(600);
        remain.setLayoutY(BASELINE);
        Pane objects = new Pane(background,player,live,score,remain);
        return objects;
    }

    public Scene GenLevel(int levelNum){
        lev = levelNum;
        halted = false;
        canvas = initiate();
        Scene level = new Scene(canvas);
        level.setOnKeyPressed(keyEvent -> {
            printEvent(keyEvent);
            if (keyEvent.getCode() == KeyCode.LEFT){
                player.setImage(fire);
                player.setScaleX(1);
                fireBall(0);
            } else if (keyEvent.getCode() == KeyCode.RIGHT){
                player.setImage(fire);
                player.setScaleX(-1);
                fireBall(1);
            }
        });
        timeline.play();
        return level;
    }

    void genEnemy(){
        Random rand = new Random();
        int direction = rand.nextInt() % 2;
        ImageView enemy = new ImageView(new Image("./resources/img/enemy.png"));
        enemy.setY(BASELINE+50);

        TranslateTransition transition  = new TranslateTransition(Duration.seconds(10), enemy);
        TranslateTransition stepBack  = new TranslateTransition(Duration.seconds(1), enemy);
        if (direction == 0){ // from Left
            enemy.setX(-200);
            enemy.setScaleX(-1);
            transition.setByX(2000);
            stepBack.setByX(-50);
        } else { // from Right
            enemy.setX(1278);
            transition.setByX(-2000);
            stepBack.setByX(50);
        }
        AnimationTimer enemyAT = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())){
                    if (!halted) {
                        if (direction == 0) {
                            enemy.setX(enemy.getX() - 3);
                        } else if (direction == 1) {
                            enemy.setX(enemy.getX() + 3);
                        }
                        transition.stop();
                        playerLives--;
                        if (playerLives < 1) {
                            halted = true;
                            timeline.stop();
                            for (AnimationTimer enemyAT : enemyTimers) {
                                enemyAT.stop();
                            }
                            enemyTimers.clear();
                            canvas.getChildren().clear();
                            lose();
                            return;
                        }
                        stepBack.play();
                        live.setText("Lives: " + playerLives);
                    }
                }

            }
        };
        stepBack.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                transition.play();
            }
        });
        transition.play();
        enemyTimers.add(enemyAT);
        enemyAT.start();
        canvas.getChildren().add(enemy);
    }

    void fireBall(int direction){
        MediaPlayer fireSound = new MediaPlayer(new Media(new File("src/resources/sound/firecast.wav").toURI().toString()));
        fireSound.play();
        ImageView fb = new ImageView(new Image("./resources/img/fireball2.gif"));
        fb.setY(450);
        TranslateTransition transition = new TranslateTransition(new Duration((float)1500/lev), fb);
        if (direction == 0){
            fb.setX(580);
            fb.setScaleX(-1);
            transition.setByX((float)-DEFAULT_VELOCITY*2/lev);
        } else {
            fb.setX(660);
            transition.setByX((float)DEFAULT_VELOCITY*2/lev);
        }
        canvas.getChildren().add(fb);
        transition.play();
        transition.setOnFinished(actionEvent -> {
            canvas.getChildren().remove(fb);
            scor--;
            score.setText("Score: " + scor);
        });
    }

    void goNextLevel(){
        if (lev < 3){
            mainStage.setScene(GenLevel(lev+1));
            remainingEnemies = 10;
        } else {
            win();
        }
    }

    void lose(){
        canvas.getChildren().clear();
        initiate();
        Pane lostScr = new Pane();
        lostScr.getChildren().add(background);
        Label ulost = new Label();
        ulost.setFont(new Font(42));
        ulost.setTextFill(WHITE);
        ulost.setLayoutX(400);
        ulost.setLayoutY(300);
        ulost.setText("You Lost on level "+ lev +"!");
        lostScr.getChildren().add(ulost);
        Scene lost = new Scene(lostScr);
        lost.setOnKeyPressed(keyEvent -> {
            printEvent(keyEvent);
            mainStage.show();
            if (keyEvent.getCode() == KeyCode.Q){
                System.exit(0);
            } else if (keyEvent.getCode() == KeyCode.R){
                initiate();
                mainStage.close();
                playerLives = 3;
                scor = 0;
                mainStage.setScene(GenLevel(1));
                mainStage.show();
            }
        });
        mainStage.setScene(lost);
        mainStage.show();
    }

    void win(){
        MediaPlayer winsound = new MediaPlayer(new Media(new File("src/resources/sound/won.mp3").toURI().toString()));
        winsound.play();
        Pane winScr = new Pane();
        winScr.getChildren().add(background);
        Label uwin = new Label();
        uwin.setFont(new Font(42));
        uwin.setTextFill(WHITE);
        uwin.setLayoutX(400);
        uwin.setLayoutY(300);
        uwin.setText("You Won on level "+ lev +"!");
        winScr.getChildren().add(uwin);
        Scene won = new Scene(winScr);
        won.setOnKeyPressed(keyEvent -> {
            printEvent(keyEvent);
            mainStage.show();
            if (keyEvent.getCode() == KeyCode.Q){
                System.exit(0);
            } else if (keyEvent.getCode() == KeyCode.R){
                initiate();
                mainStage.close();
                playerLives = 3;
                scor = 0;
                mainStage.setScene(GenLevel(1));
                mainStage.show();
            }
        });
        mainStage.setScene(won);
        mainStage.show();
    }


    void printEvent(KeyEvent event) {
        System.out.println(
                event.getEventType()
                        + ": {target:" + event.getTarget().getClass() + "},"
                        + ": {code:" + event.getCode() + "},"
                        + ": {text:" + event.getText() + "},"
                        + ": {character:" + event.getCharacter() + "}"
        );
    }
}
