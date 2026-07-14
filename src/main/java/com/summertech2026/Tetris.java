package com.summertech2026;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

/**
 * JavaFX Tetris
 */
public class Tetris extends Application {

    PauseTransition transition;
    AnimationTimer animation;

    Random random = new Random();
    Stage stage = null;
    Group root = new Group();
    Scene scene = new Scene(root);
    Rectangle ant = new Rectangle(160, 100);

    File scores = null;
    JSONObject scoresJSON = null;
    JSONArray scoreArray = null;

    int time = 0;

    String scoringMode = "ant";

    String[] subtitles = new String[] { "Summon 5 Ants", "Remove all Blocks", "Give ant Laser Eyes", "Instant Death",
            "Summon 5 Blocks", "Slow Ant", "Free Speed Coil", " Twenty-Sixth Amendment\r\n" + //
                    "\r\n" + //
                    "Section 1\r\n" + //
                    "\r\n" + //
                    "The right of citizens of the United States, who are eighteen years of age or older, to vote shall not be denied or abridged by the United States or by any State on account of age.\r\n"
                    + //
                    "\r\n" + //
                    "Section 2\r\n" + //
                    "\r\n" + //
                    "The Congress shall have power to enforce this article by appropriate legislation.",
            " Twenty-Sixth Amendment\r\n" + //
                    "\r\n" + //
                    "Section 1\r\n" + //
                    "\r\n" + //
                    "The right of citizens of the United States, who are fourty-four years of age or older, to vote shall not be denied or abridged by the United States or by any State on account of age.\r\n"
                    + //
                    "\r\n" + //
                    "Section 2\r\n" + //
                    "\r\n" + //
                    "The Congress shall have power to enforce this article by appropriate legislation.",
            "Make more popups?", "Make less popups?", "\"Instant\" Win" };
    EventHandler<ActionEvent>[] events = new EventHandler[subtitles.length];

    FileInputStream antFileR = null;
    Image antTextureImageR = null;
    ImagePattern antTextureR = null;
    FileInputStream antFileL = null;
    Image antTextureImageL = null;
    ImagePattern antTextureL = null;
    FileInputStream evilAntFileL = null;
    Image evilAntTextureImageL = null;
    ImagePattern evilAntTextureL = null;
    FileInputStream evilAntFileR = null;
    Image evilAntTextureImageR = null;
    ImagePattern evilAntTextureR = null;

    ImagePattern grassTexture = null;

    ArrayList<Popup> popups = new ArrayList<Popup>();

    ImagePattern window = null;

    ImagePattern warningTexture = null;

    Rectangle2D bounds = null;
    ObservableList<Rectangle> loadedBlocks = FXCollections.observableArrayList();
    ObservableList<WarningBlock> warnings = FXCollections.observableArrayList();
    ObservableList<Rectangle> ants = FXCollections.observableArrayList();

    Rectangle topAnt = ant;
    boolean isEvil = false;
    ArrayList<Rectangle> lasers = new ArrayList<Rectangle>();
    int currentLasers = 0;

    Group connectedAnts = new Group();

    boolean friction = true;

    double hAcceleration = 0;
    double maxHSpeed = 12;
    double hSpeed = 0;
    double fallSpeed = 5;
    int difficulty = 0;

    double popupAdsPerRound = 1;

    int score = 0;
    Label scoreLabel = new Label("Your Score: " + score);
    Label laserLabel = new Label("");

    boolean isDead = false;

    int currentFrame = 0;

    double timeSinceLastA = System.currentTimeMillis() - 1000;
    double timeSinceLastD = System.currentTimeMillis() - 1000;
    final int dashCD = 1000;

    PauseTransition popupAds = null;

    Comparator<JSONObject> cAnt = new Comparator<JSONObject>() {
        public int compare(JSONObject e, JSONObject otherE) {
            if (e.getString("Mode").equals(otherE.get("Mode"))) {
                if (e.getInt("Score") > (otherE.getInt("Score"))) {
                    return -1;
                } else if (e.getInt("Score") < (otherE.getInt("Score"))) {
                    return 1;
                } else
                    return 0;
            } else if (e.getString("Mode").equals("ant")) {
                return -1;
            } else
                return 1;

        }
    };
    Comparator<JSONObject> cTime = new Comparator<JSONObject>() {
        public int compare(JSONObject e, JSONObject otherE) {
            if (e.getString("Mode").equals(otherE.get("Mode"))) {
                if (e.getInt("Score") > (otherE.getInt("Score"))) {
                    return -1;
                } else if (e.getInt("Score") < (otherE.getInt("Score"))) {
                    return 1;
                } else
                    return 0;
            } else if (e.getString("Mode").equals("time")) {
                return -1;
            } else
                return 1;

        }
    };

    @Override
    public void start(Stage hi) throws IOException {

        difficulty = 2;

        stage = hi;
        bounds = Screen.getPrimary().getVisualBounds();
        scores = new File("src/main/resources/com/summertech2026/highScores.json");
        scoresJSON = new JSONObject(new String(Files.readAllBytes(Paths.get(scores.toURI()))));
        scoreArray = (JSONArray) scoresJSON.get("highScores");

        // scoreArray.getJSONObject(0).put("Name", "Bob");

        // // System.out.println(scoreArray.getJSONObject(0).get("Name"));

        // // System.out.println(scoresJSON.toString());
        // fileWriter.write(scoresJSON.toString());

        // fileWriter.close();

        warningTexture = new ImagePattern(
                new Image(new FileInputStream("src/main/resources/com/summertech2026/warning.png")));
        antFileR = new FileInputStream("src/main/resources/com/summertech2026/antR.png");
        antTextureImageR = new Image(antFileR);
        antTextureR = new ImagePattern(antTextureImageR);
        antFileL = new FileInputStream("src/main/resources/com/summertech2026/antL.png");
        antTextureImageL = new Image(antFileL);
        antTextureL = new ImagePattern(antTextureImageL);
        evilAntFileR = new FileInputStream("src/main/resources/com/summertech2026/evilAntR.png");
        evilAntTextureImageR = new Image(evilAntFileR);
        evilAntTextureR = new ImagePattern(evilAntTextureImageR);
        evilAntFileL = new FileInputStream("src/main/resources/com/summertech2026/evilAntL.png");
        evilAntTextureImageL = new Image(evilAntFileL);
        evilAntTextureL = new ImagePattern(evilAntTextureImageL);

        window = new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/window.png")));

        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setResizable(false);
        stage.setTitle("Ant Tetris");
        stage.setFullScreen(true);

        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        if (scoringMode.equals("time")) {
            scoreLabel.setText("Your Time: 0");
        }

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                switch (e.getButton()) {
                    case SECONDARY:
                        if (isDead == false && currentLasers > 0) {
                            currentLasers--;
                            laserLabel.setText("Your Lasers: " + currentLasers);
                            double mouseX = e.getX();
                            double mouseY = e.getY();

                            int offsetX = 60;

                            if (ant.getFill() == evilAntTextureL) {
                                offsetX = -offsetX - 10;
                            }

                            Rectangle temp = new Rectangle(10, 10);
                            temp.setArcHeight(100);
                            temp.setArcWidth(100);
                            temp.setFill(Color.RED);
                            temp.setTranslateX(connectedAnts.getTranslateX() + ant.getWidth() / 2 + offsetX);
                            temp.setTranslateY(ant.getTranslateY() + 40);
                            root.getChildren().add(temp);
                            lasers.add(temp);

                            TranslateTransition t = new TranslateTransition(Duration.seconds(0.5), temp);
                            t.setToX(mouseX);
                            t.setToY(mouseY);
                            t.setOnFinished(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent e) {
                                    root.getChildren().remove(temp);
                                    lasers.remove(temp);
                                }
                            });

                            t.play();

                            if (currentLasers == 0) {
                                isEvil = false;
                                laserLabel.setText("");
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        });

        stage.show();

        connectedAnts.getChildren().add(ant);

        handleKeyboard(scene);
        animation = new AnimationTimer() {
            @Override
            public void handle(long arg) {
                if (isDead == false) {
                    update(stage);
                }

            }
        };

        events[0] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i < 5; i++) {
                    createAnt();
                }
            }
        };
        events[4] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i < 5; i++) {
                    createBlock();
                }
            }
        };
        events[3] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                death();
            }
        };
        events[1] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                root.getChildren().removeAll(loadedBlocks);
                loadedBlocks.clear();
            }
        };
        events[5] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                hSpeed = 0;
                maxHSpeed = 6;
                friction = true;
            }
        };
        events[2] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (ant.getFill() == antTextureL) {
                    ant.setFill(evilAntTextureL);
                } else {
                    ant.setFill(evilAntTextureR);
                }
                isEvil = true;
                currentLasers = 5;
                laserLabel.setText("Your Lasers: " + currentLasers);
            }
        };
        events[6] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                friction = false;
                maxHSpeed = 16;
            }
        };
        events[7] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i < 18; i++) {
                    createAnt();
                }
            }
        };
        events[8] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i < 44; i++) {
                    createBlock();
                }
            }
        };
        events[9] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                popupAdsPerRound += 0.5;

            }
        };
        events[10] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                popupAdsPerRound -= 0.25;

            }
        };

        events[11] = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    grassTexture = new ImagePattern(
                            new Image(new FileInputStream("src/main/resources/com/summertech2026/grass.jpg")));
                } catch (FileNotFoundException q) {
                    q.printStackTrace();
                }

                scene.setFill(grassTexture);
            }
        };

        stage.setOnCloseRequest(event -> {

            System.exit(0);
        });
        titleScreen();
    }

    @SuppressWarnings("unchecked")
    public void titleScreen() throws FileNotFoundException {
        root.getChildren().clear();
        Label[] heightLabels = new Label[5];
        Label[] timeLabels = new Label[5];
        scene.setFill(new ImagePattern(
                new Image(new FileInputStream("src/main/resources/com/summertech2026/title.png"))));
        // System.out.println(bounds.getWidth());
        // System.out.println(bounds.getHeight());
        Button mainMenu = new Button();

        ImagePattern antScoring = new ImagePattern(
                new Image(new FileInputStream("src/main/resources/com/summertech2026/antScoring.png")));
        ImagePattern timeScoring = new ImagePattern(
                new Image(new FileInputStream("src/main/resources/com/summertech2026/timeScoring.png")));

        Rectangle play = new Rectangle(390, 130);
        Rectangle config = new Rectangle(100, 100);
        Rectangle back = new Rectangle(390, 130);
        Rectangle exit = new Rectangle(390, 130);
        Rectangle score = new Rectangle(390, 130);
        Rectangle mode = new Rectangle(390, 130);
        Label difficultyLabel = new Label("Difficulty");
        Label modeLabel = new Label("Scoring Mode");
        Slider difficultySlider = new Slider(1, 5, 2);

        mainMenu.setPrefHeight(130);
        mainMenu.setPrefWidth(390);
        mainMenu.setTranslateY(100);
        mainMenu.setTranslateX(bounds.getWidth() - back.getWidth() - 150);
        mainMenu.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        back.setTranslateX(bounds.getWidth() - back.getWidth() - 150);
        back.setTranslateY(100);
        play.setTranslateX(70);
        play.setTranslateY(360);
        difficultyLabel.setTranslateX(250);
        difficultyLabel.setTranslateY(280);
        difficultyLabel.setFont(new Font("System Bold", 36));
        modeLabel.setTranslateY(480);
        modeLabel.setFont(new Font("System Bold", 36));
        modeLabel.setTranslateX(180);
        difficultySlider.setTranslateX(250);
        difficultySlider.setTranslateY(360);
        difficultySlider.setMajorTickUnit(1);
        difficultySlider.setScaleX(3);
        difficultySlider.setScaleY(3);

        difficultySlider.setShowTickLabels(true);

        difficultySlider.valueProperty().addListener(event -> {
            difficultySlider.setValue(Math.round(difficultySlider.getValue()));
            difficulty = (int) Math.round(difficultySlider.getValue());
        });

        config.setTranslateX(bounds.getWidth() - 150 - config.getWidth());
        config.setTranslateY(80);
        score.setTranslateX(70);
        score.setTranslateY(560);
        mode.setTranslateX(100);
        mode.setTranslateY(560);
        exit.setTranslateX(70);
        exit.setTranslateY(760);
        exit.setFill(
                new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/exit.png"))));
        back.setFill(
                new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/back.png"))));
        play.setFill(
                new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/Play.png"))));
        config.setFill(
                new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/config.png"))));
        score.setFill(
                new ImagePattern(new Image(new FileInputStream("src/main/resources/com/summertech2026/score.png"))));
        mode.setFill(antScoring);

        ArrayList storage = new ArrayList();
        Iterator iterator = scoreArray.iterator();

        while (iterator.hasNext()) {
            storage.add(iterator.next());
        }

        Font font = Font.loadFont(new FileInputStream("src/main/resources/com/summertech2026/GameFont.ttf"), 80);
        Background bg = new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(8), Insets.EMPTY));

        Collections.sort(storage, cTime);
        for (int i = 0; i < 5; i++) {
            JSONObject temp = (JSONObject) storage.get(i);

            timeLabels[i] = new Label();
            timeLabels[i].setText(temp.getString("Name") + ":" + temp.getInt("Score"));
            timeLabels[i].setFont(font);
            timeLabels[i].setTranslateY(185 + 170 * i);
            timeLabels[i].setTranslateX(650);
            timeLabels[i].setBackground(bg);
        }
        Collections.sort(storage, cAnt);
        for (int i = 0; i < 5; i++) {
            JSONObject temp = (JSONObject) storage.get(i);
            heightLabels[i] = new Label();
            heightLabels[i].setText(temp.getString("Name") + ":" + temp.getInt("Score"));
            heightLabels[i].setFont(font);
            heightLabels[i].setTranslateY(185 + 170 * i);
            heightLabels[i].setTranslateX(185);
            heightLabels[i].setBackground(bg);
        }

        mainMenu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                root.getChildren().add(score);
                root.getChildren().add(play);
                root.getChildren().add(config);
                root.getChildren().add(exit);
                try {
                    scene.setFill(new ImagePattern(
                            new Image(new FileInputStream("src/main/resources/com/summertech2026/title.png"))));
                } catch (FileNotFoundException q) {
                    q.printStackTrace();
                }
            }
        });

        exit.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                exit.setScaleX(1.025);
                exit.setScaleY(1.025);
            }
        });
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent q) {
                System.exit(0);
            }
        });
        config.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent q) {
                root.getChildren().clear();
                root.getChildren().add(back);
                root.getChildren().add(mainMenu);
                root.getChildren().add(difficultySlider);
                root.getChildren().add(difficultyLabel);
                root.getChildren().add(modeLabel);
                root.getChildren().add(mode);
                try {
                    scene.setFill(new ImagePattern(
                            new Image(new FileInputStream("src/main/resources/com/summertech2026/settings.png"))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        mode.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                mode.setScaleX(1);
                mode.setScaleY(1);
            }
        });
        mode.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                mode.setScaleX(1.025);
                mode.setScaleY(1.025);
            }
        });
        mode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (scoringMode.equals("ant")) {
                    scoringMode = "time";
                    mode.setFill(timeScoring);
                } else {
                    scoringMode = "ant";
                    mode.setFill(antScoring);
                }
            }
        });
        exit.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                exit.setScaleX(1);
                exit.setScaleY(1);
            }
        });
        score.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                score.setScaleX(1.025);
                score.setScaleY(1.025);
            }
        });
        score.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent q) {
                root.getChildren().clear();
                root.getChildren().add(back);
                root.getChildren().add(mainMenu);
                for (int i = 0; i < heightLabels.length; i++) {
                    root.getChildren().add(heightLabels[i]);
                }
                for (int i = 0; i < timeLabels.length; i++) {
                    root.getChildren().add(timeLabels[i]);
                }
                try {
                    scene.setFill(new ImagePattern(
                            new Image(
                                    new FileInputStream("src/main/resources/com/summertech2026/scores.png"))));
                } catch (FileNotFoundException i) {
                    i.printStackTrace();

                }

            }
        });
        score.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                score.setScaleX(1);
                score.setScaleY(1);
            }
        });

        play.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                play.setScaleX(1.025);
                play.setScaleY(1.025);
            }
        });
        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent q) {
                startGame();
            }
        });
        play.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                play.setScaleX(1);
                play.setScaleY(1);
            }
        });

        root.getChildren().add(score);
        root.getChildren().add(play);
        root.getChildren().add(config);
        root.getChildren().add(exit);
    }

    public void startGame() {

        root.getChildren().clear();
        loadedBlocks.clear();

        scene.setFill(Color.WHITE);

        ant.setFill(antTextureL);
        ant.setTranslateY(bounds.getHeight() - ant.getHeight() + 50);
        ant.setTranslateX(0);
        connectedAnts.setTranslateX(0);
        root.getChildren().add(connectedAnts);

        score = 0;
        fallSpeed = difficulty * 2.5;
        if (scoringMode.equals("ant")) {
            scoreLabel.setText("Your Score: " + score);
        } else {
            scoreLabel.setText("Your Time: 0");
        }
        isDead = false;
        topAnt = ant;

        scoreLabel.setFont(new Font(60));
        root.getChildren().add(scoreLabel);

        laserLabel.setFont(new Font(60));
        laserLabel.setTranslateY(100);
        root.getChildren().add(laserLabel);

        animation.start();

        PauseTransition timer = new PauseTransition(Duration.seconds(1));
        timer.setOnFinished(e -> {
            if (isDead == false && scoringMode.equals("time")) {
                time++;
                scoreLabel.setText("Your Time: " + time);
                timer.playFromStart();
            }
        });
        timer.play();

        transition = new PauseTransition(Duration.seconds(1));
        transition.setOnFinished(e -> {
            try {
                transition
                        .setDuration(Duration.millis(random.nextDouble() * (1000 - fallSpeed * 10) + 500 - fallSpeed));
            } catch (IllegalArgumentException q) {
                System.out.println("yay you got the program to crash, now you actually win");
                System.exit(0);
            }
            if (isDead == false) {

                if (random.nextInt(2) == 0) {
                    createAnt();
                }
                createBlock();

                if (score > 30) {
                    createBlock();
                }
                if (score > 50) {
                    createBlock();
                }

                transition.playFromStart();
            }
        });

        popupAds = new PauseTransition(Duration.seconds(5));
        popupAds.setOnFinished(e -> {
            try {
                transition
                        .setDuration(Duration.seconds(random.nextInt(3) + 5));
            } catch (IllegalArgumentException q) {
                q.printStackTrace();
            }
            if (isDead == false) {

                for (int i = 0; i < Math.floor(popupAdsPerRound); i++) {
                    createAd();
                }
                popupAds.playFromStart();
            }
        });

        popupAds.playFromStart();
        transition.playFromStart();
    }

    public void createAd() {
        int popupType = random.nextInt(subtitles.length);
        Rectangle rectangle = new Rectangle(500, 500);
        Popup popup = new Popup();
        GridPane pane = new GridPane();
        Label title = new Label("To Hard?");
        Label subtitle = new Label(subtitles[popupType]);
        Button yes = new Button("Accept");
        Button no = new Button("Decline");

        pane.setMinWidth(rectangle.getWidth());
        pane.setAlignment(Pos.CENTER);

        title.setPrefWidth(400);
        title.setFont(new Font("System Bold", 30));
        title.setTranslateY(100);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);

        subtitle.setPrefWidth(400);
        if (popupType != 7 && popupType != 8) {
            subtitle.setFont(new Font("System Bold", 25));
            subtitle.setTranslateY(200);
            pane.add(title, 0, 0);
        } else {
            subtitle.setFont(new Font("System Bold", 8));
            subtitle.setTranslateY(100);
        }
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        rectangle.setFill(window);
        yes.setPrefWidth(300);
        yes.setFont(new Font("System Bold", 25));
        yes.setTranslateX(rectangle.getWidth() / 2 - 150);
        yes.setTranslateY(rectangle.getHeight() / 2 + 100);
        no.setPrefWidth(300);
        no.setFont(new Font("system Bold", 25));
        no.setTranslateX(rectangle.getWidth() / 2 - 150);
        no.setTranslateY(rectangle.getHeight() / 2);

        if (random.nextInt(6) == 0) {
            no.setTranslateY(rectangle.getHeight() / 2 + 100);
            yes.setTranslateY(rectangle.getHeight() / 2);
        }

        no.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                popup.hide();
            }
        });

        Button temp = new Button();
        temp.setOnAction(events[popupType]);

        yes.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                popup.hide();
                temp.fire();
            }
        });

        popup.setAnchorX(bounds.getWidth() - rectangle.getWidth() - random.nextDouble() * 1200);
        popup.setAnchorY(bounds.getHeight() - rectangle.getHeight() - random.nextDouble() * 800);

        popup.getContent().add(rectangle);
        pane.add(subtitle, 0, 0);
        popup.getContent().add(pane);
        popup.getContent().add(yes);
        popup.getContent().add(no);
        popups.add(popup);
        popup.show(stage);
    }

    public void createAnt() {
        Rectangle currentAnt = new Rectangle(160, 100);

        currentAnt.setFill(ant.getFill());
        currentAnt.setTranslateY(0 - currentAnt.getHeight());
        currentAnt.setTranslateX(random.nextDouble() * (bounds.getWidth() - currentAnt.getWidth()));

        root.getChildren().add(currentAnt);
        ants.add(currentAnt);
    }

    public void createBlock() {
        Rectangle block = new Rectangle(200, 200);
        WarningBlock warningBlock = new WarningBlock(currentFrame + 40, 200, 200);

        double xPos = random.nextDouble() * (bounds.getWidth() - block.getWidth());


        block.setTranslateX(xPos);
        warningBlock.setTranslateX(xPos);
        block.setTranslateY(0 - block.getHeight() - 40 * fallSpeed);
        warningBlock.setFill(warningTexture);

        loadedBlocks.add(block);
        warnings.add(warningBlock);
        root.getChildren().add(block);
        root.getChildren().add(warningBlock);
    }

    private void handleKeyboard(Scene currentScene) {
        currentScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {

                    case A:
                        if (isEvil) {
                            ant.setFill(evilAntTextureL);
                        } else {
                            ant.setFill(antTextureL);
                        }
                        hAcceleration = -1;
                        break;

                    case D:
                        if (isEvil) {
                            ant.setFill(evilAntTextureR);
                        } else {
                            ant.setFill(antTextureR);
                        }
                        hAcceleration = 1;
                        break;

                    case SPACE:
                        if (isDead) {
                            try {
                                titleScreen();
                            } catch (FileNotFoundException q) {
                                q.printStackTrace();
                            }
                        }
                        break;

                    // case NUMPAD0:
                    // createBlock();
                    // break;
                    // case NUMPAD1:
                    // createAnt();
                    // break;
                    // case NUMPAD2:
                    // win();
                    // break;
                    case F11:
                        stage.setFullScreen(!stage.isFullScreen());
                        break;

                    default:
                        break;
                }
            }

        });

        currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case A:
                        if (System.currentTimeMillis() - timeSinceLastA <= 300) {
                            timeSinceLastA = System.currentTimeMillis() - 1000;
                            connectedAnts.setTranslateX(connectedAnts.getTranslateX() - 300);
                        } else {
                            timeSinceLastA = System.currentTimeMillis();
                        }
                        if (friction) {
                            hSpeed = 0;
                        }
                        hAcceleration = 0;
                        break;
                    case D:
                        if (System.currentTimeMillis() - timeSinceLastD <= 300) {
                            timeSinceLastD = System.currentTimeMillis() - 1000;
                            connectedAnts.setTranslateX(connectedAnts.getTranslateX() + 300);
                        } else {
                            timeSinceLastD = System.currentTimeMillis();
                        }
                        if (friction) {
                            hSpeed = 0;
                        }
                        hAcceleration = 0;
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void death() {
        isDead = true;
        scene.setFill(Color.WHITE);
        root.getChildren().clear();
        friction = true;

        maxHSpeed = 12;

        Label youDied = new Label("You Lost");
        Label restart = new Label("Press space to try again!");
        TextField name = new TextField();
        Button submit = new Button("Submit");
        popupAdsPerRound = 1;

        youDied.setFont(new Font("MONOSPACED BOLD", 80));
        youDied.setPrefWidth(450);
        youDied.setTranslateX(bounds.getWidth() / 2 - youDied.getPrefWidth() / 2);
        restart.setFont(new Font("MONOSPACED BOLD", 40));
        restart.setPrefWidth(650);
        restart.setTranslateX(bounds.getWidth() / 2 - restart.getPrefWidth() / 2);
        restart.setTranslateY(bounds.getHeight() / 2);

        name.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> e, Number oldNumber, Number newNumber) {
                if (newNumber.intValue() > oldNumber.intValue() && name.getText().length() >= 4) {
                    name.setText(name.getText().substring(0, 4));
                }
            }
        });
        root.getChildren().add(name);
        name.getParent().requestFocus();
        name.setPromptText("Input Name:");
        name.setPrefWidth(400);
        name.setFont(new Font(36));
        name.setTranslateX(bounds.getWidth() / 2 - name.getPrefWidth() / 2);
        name.setTranslateY(600);
        submit.setTranslateY(800);
        submit.setFont(new Font(36));
        submit.setPrefWidth(400);
        submit.setTranslateX(bounds.getWidth() / 2 - submit.getPrefWidth() / 2);

        for (Popup i : popups) {
            i.hide();
        }

        submit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                JSONObject newScore = new JSONObject();
                if (scoringMode.equals("ant")) {
                    newScore.put("Score", score);
                } else {
                    newScore.put("Score", time);
                }
                newScore.put("Name", name.getText());
                newScore.put("Mode", scoringMode);
                scoreArray.put(newScore);
                root.getChildren().remove(submit);
                root.getChildren().remove(name);

                try {
                    FileWriter fileWriter = new FileWriter(scores);
                    fileWriter.write(scoresJSON.toString());
                    fileWriter.close();
                } catch (IOException q) {
                    q.printStackTrace();
                }
            }
        });

        root.getChildren().add(submit);
        root.getChildren().add(youDied);
        root.getChildren().add(restart);
        root.getChildren().add(scoreLabel);

    }

    private void win() {
        isEvil = false;
        currentLasers = 0;
        laserLabel.setText("");
        lasers.clear();
        friction = true;

        for (Popup i : popups) {
            i.hide();
        }

        root.getChildren().clear();
        connectedAnts.getChildren().clear();
        connectedAnts.getChildren().add(ant);
        root.getChildren().add(connectedAnts);
        root.getChildren().add(laserLabel);
        root.getChildren().add(scoreLabel);
        loadedBlocks.clear();
        warnings.clear();
        ants.clear();
        popups.clear();

        popupAdsPerRound += 0.5;
        maxHSpeed = 12;
        fallSpeed += difficulty * 2.5;
        topAnt = ant;
    }

    public boolean laserCollision(Rectangle block) {
        for (Rectangle s : lasers) {
            if (s.getBoundsInParent().intersects(block.getBoundsInParent())) {
                lasers.remove(s);
                root.getChildren().remove(s);
                return true;
            }
        }
        return false;
    }

    private void update(Stage stage) {
        currentFrame++;

        if (Math.abs(hSpeed) < maxHSpeed) {
            hSpeed += hAcceleration;
        } else if (hSpeed > 0) {
            hSpeed -= 0.05;
        } else if (hSpeed < 0) {
            hSpeed += 0.05;
        } else if (hSpeed < -maxHSpeed) {
            hSpeed = -maxHSpeed;
        } else if (hSpeed > maxHSpeed) {
            hSpeed = maxHSpeed;
        }

        connectedAnts.setTranslateX(connectedAnts.getTranslateX() + hSpeed);
        try {
            for (Rectangle s : loadedBlocks) {

                s.setTranslateY(s.getTranslateY() + fallSpeed);

                if (s.getTranslateY() >= bounds.getHeight() + 50) {
                    root.getChildren().remove(s);
                    loadedBlocks.remove(s);
                } else if (laserCollision(s)) {
                    root.getChildren().remove(s);
                    loadedBlocks.remove(s);
                } else if (s.getBoundsInParent().intersects(connectedAnts.getBoundsInParent())) {
                    if (score % 10 > 0) {
                        score--;
                        root.getChildren().remove(s);
                        loadedBlocks.remove(s);
                        connectedAnts.getChildren().remove(topAnt);
                        topAnt = (Rectangle) connectedAnts.getChildren().get(score % 10);
                        if (scoringMode.equals("ant")) {
                            scoreLabel.setText("Your Score: " + score);
                        } else {
                            scoreLabel.setText("Your Time: " + time);
                        }

                    } else {
                        death();
                    }
                }

            }

            for (WarningBlock s : warnings) {
                if (currentFrame >= s.frameOfDelete) {
                    root.getChildren().remove(s);
                    warnings.remove(s);

                }
            }

            for (Rectangle s : ants) {
                s.setTranslateY(s.getTranslateY() + fallSpeed);
                if (s.getTranslateY() >= bounds.getHeight() + 50) {
                    root.getChildren().remove(s);
                    ants.remove(s);
                } else if (s.getBoundsInParent().intersects(connectedAnts.getBoundsInParent())) {
                    score++;
                    if (scoringMode.equals("ant")) {
                        scoreLabel.setText("Your Score: " + score);
                    }

                    root.getChildren().remove(s);
                    connectedAnts.getChildren().add(s);
                    s.setTranslateX(ant.getTranslateX());
                    s.setTranslateY(topAnt.getTranslateY() - s.getHeight());
                    ants.remove(s);
                    topAnt = s;

                    if (topAnt.getTranslateY() <= 0) {

                        win();
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
        }
        if (connectedAnts.getTranslateX() < 0) {
            connectedAnts.setTranslateX(bounds.getWidth() - ant.getWidth());
        } else if (connectedAnts.getTranslateX() > bounds.getWidth() - ant.getWidth()) {
            connectedAnts.setTranslateX(0);
        }

    }

    public static void main(String[] args) {
        System.setProperty("quantum.multithreading", "false");
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("javafx.animation.pulse", "60");
        System.setProperty("javafx.animation.framerate", "60");
        launch(args);
    }
}

class WarningBlock extends Rectangle {
    public double frameOfDelete;

    public WarningBlock(double frame, int h, int w) {
        frameOfDelete = frame;

        setWidth(w);
        setHeight(h);
    }
}