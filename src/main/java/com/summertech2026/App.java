package com.summertech2026;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX App
 */
public class App extends Application {

    private Scene scene;
    private Group root = new Group();
    private final int SCALE = 10;
    FileInputStream antFileR = null;
    Image antTextureImageR = null;
    FileInputStream antFileL = null;
    Image antTextureImageL = null;
    ImagePattern antTextureR = null;
    ImagePattern antTextureL = null;

    FileInputStream groundFile = null;
    Image groundTextureImage = null;
    ImagePattern groundTexture = null;
    FileInputStream brickFile = null;
    Image brickTextureImage = null;
    ImagePattern brickTexture = null;

    FileInputStream questionFile = null;
    Image questionTextureImage = null;
    ImagePattern questionTexture = null;
    FileInputStream inactiveFile = null;
    Image inactiveTextureImage = null;
    ImagePattern inactiveTexture = null;

    FileInputStream bgFile = null;
    Image bgTextureImage = null;
    ImagePattern bgTexture = null;

    FileInputStream stairFile = null;
    Image stairTextureImage = null;
    ImagePattern stairTexture = null;

    FileInputStream antFile = null;
    Image antTextureImage = null;
    ImagePattern antTexture = null;
    FileInputStream secretFile = null;
    Image secretTextureImage = null;
    ImagePattern secretTexture = null;

    FileInputStream keyFile = null;
    Image keyTextureImage = null;
    ImagePattern keyTexture = null;
    FileInputStream lockFile = null;
    Image lockTextureImage = null;
    ImagePattern lockTexture = null;
    Stage stage = null;

    FileInputStream poleFile = null;
    Image poleTextureImage = null;
    ImagePattern poleTexture = null;

    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    private ObservableList<Rectangle> loadedBlocks = null;
    private ObservableList<Rectangle> scr1Blocks = FXCollections.observableArrayList();
    private ObservableList<Rectangle> scr2Blocks = FXCollections.observableArrayList();

    Rectangle ant = null;
    double vertSpeed = 0;
    double hSpeed = 0;
    double hAcceleration = 0;
    final double maxHSpeed = 12;

    boolean verySecretBoolean = false;

    Group scr1 = new Group();
    Group scr2 = new Group();

    ArrayList<ObservableList<Rectangle>> screenBlocks = new ArrayList<ObservableList<Rectangle>>();
    Group[] screens = new Group[] { scr1, scr2 };
    int screenNum = 0;

    // final double maxVSpeed = 14;

    private boolean WPressed = false;
    private boolean canJump = false;
    private double gravity = 1;

    @Override
    public void start(Stage hi) throws IOException {

        stage = hi;

        screenBlocks.add(scr1Blocks);
        screenBlocks.add(scr2Blocks);

        antFileR = new FileInputStream("src/main/resources/com/summertech2026/antR.png");
        antTextureImageR = new Image(antFileR);
        antFileL = new FileInputStream("src/main/resources/com/summertech2026/antL.png");
        antTextureImageL = new Image(antFileL);
        antTextureR = new ImagePattern(antTextureImageR);
        antTextureL = new ImagePattern(antTextureImageL);

        questionFile = new FileInputStream("src/main/resources/com/summertech2026/question.png");
        questionTextureImage = new Image(questionFile);
        questionTexture = new ImagePattern(questionTextureImage);
        inactiveFile = new FileInputStream("src/main/resources/com/summertech2026/inactive.png");
        inactiveTextureImage = new Image(inactiveFile);
        inactiveTexture = new ImagePattern(inactiveTextureImage);

        antFile = new FileInputStream("src/main/resources/com/summertech2026/ant.jpg");
        antTextureImage = new Image(antFile);
        antTexture = new ImagePattern(antTextureImage);
        secretFile = new FileInputStream("src/main/resources/com/summertech2026/secret.png");
        secretTextureImage = new Image(secretFile);
        secretTexture = new ImagePattern(secretTextureImage);

        bgFile = new FileInputStream("src/main/resources/com/summertech2026/background.png");
        bgTextureImage = new Image(bgFile);
        bgTexture = new ImagePattern(bgTextureImage);

        poleFile = new FileInputStream("src/main/resources/com/summertech2026/pole.png");
        poleTextureImage = new Image(poleFile);
        poleTexture = new ImagePattern(poleTextureImage);
        stairFile = new FileInputStream("src/main/resources/com/summertech2026/stair.png");
        stairTextureImage = new Image(stairFile);
        stairTexture = new ImagePattern(stairTextureImage, 0, 0, 100, 100, false);

        groundFile = new FileInputStream("src/main/resources/com/summertech2026/ground.png");
        groundTextureImage = new Image(groundFile);
        groundTexture = new ImagePattern(groundTextureImage, 0, 0, (bounds.getHeight() / 4 + 50) / 2,
                (bounds.getHeight() / 4 + 50) / 2, false);

        brickFile = new FileInputStream("src/main/resources/com/summertech2026/brick.png");
        brickTextureImage = new Image(brickFile);
        brickTexture = new ImagePattern(brickTextureImage);

        keyFile = new FileInputStream("src/main/resources/com/summertech2026/key.png");
        keyTextureImage = new Image(keyFile);
        keyTexture = new ImagePattern(keyTextureImage);
        lockFile = new FileInputStream("src/main/resources/com/summertech2026/lock.png");
        lockTextureImage = new Image(lockFile);
        lockTexture = new ImagePattern(lockTextureImage);

        Rectangle base = new Rectangle(bounds.getWidth(), bounds.getHeight() / 4 + 50);
        Rectangle base2 = new Rectangle(bounds.getWidth(), bounds.getHeight() / 4 + 50);
        Rectangle bg = new Rectangle(bounds.getWidth(), bounds.getHeight() - base.getHeight() + 52.5);
        Key key = new Key(200, 200, lockTexture, keyTexture);
        ItemBlock block = new ItemBlock(null, questionTexture);
        Rectangle pole = new Rectangle(75, 825);
        Rectangle block2 = new Rectangle(base.getHeight() / 2, 600);
        Rectangle block3 = new Rectangle(100, 100);
        Rectangle block4 = new Rectangle(base.getHeight() / 2, 600);
        Rectangle block5 = new Rectangle(100, 100);
        Rectangle block6 = new Rectangle(100, 100);
        Rectangle block7 = new Rectangle(100, 100);
        ItemBlock block8 = new ItemBlock(null, questionTexture);
        ItemBlock block9 = new ItemBlock(null, questionTexture);
        ItemBlock block10 = new ItemBlock(key, questionTexture);
        Rectangle block11 = new Rectangle(100, 300);
        Rectangle block12 = new Rectangle(100, 200);
        Rectangle block13 = new Rectangle(100, 100);

        Comparator<Rectangle> c = new Comparator<Rectangle>() {
            public int compare(Rectangle e, Rectangle otherE) {
                if (e.toString().compareTo(otherE.toString()) > 0) {
                    return -1;
                } else if (e.toString().compareTo(otherE.toString()) < 0) {
                    return 1;
                } else
                    return 0;
            }
        };

        bg.setFill(bgTexture);
        bg.setViewOrder(1);

        scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        ant = new Rectangle(16 * SCALE, 10 * SCALE);

        ant.setTranslateX(bounds.getWidth() / 2);
        ant.setTranslateY(bounds.getHeight() / 2);

        base.setId("Arlo HATES COMPARATORS");
        base.setTranslateY((bounds.getHeight() / 4) * 3 + 2.5);
        base.setFill(groundTexture);

        base2.setId("Arlo HATES COMPARATORS");
        base2.setTranslateY((bounds.getHeight() / 4) * 3 + 2.5);
        base2.setFill(groundTexture);

        pole.setId("AAAAB");
        pole.setTranslateY(base2.getTranslateY() - pole.getHeight());
        pole.setTranslateX(bounds.getWidth() - pole.getWidth());
        pole.setFill(poleTexture);

        block.setFill(questionTexture);
        block.setTranslateY(bounds.getHeight() / 2 + 15);
        block.setTranslateX(450);
        block.setId("I HATE COMPARATORS");

        block5.setId("E HATE COMPARATORS");
        block5.setFill(brickTexture);
        block5.setTranslateY(bounds.getHeight() / 2 + 15);
        block5.setTranslateX(800);

        block6.setId("E HATE COMPARATORS");
        block6.setFill(brickTexture);
        block6.setTranslateY(bounds.getHeight() / 2 + 15);
        block6.setTranslateX(1000);

        block7.setId("E HATE COMPARATORS");
        block7.setFill(brickTexture);
        block7.setTranslateY(bounds.getHeight() / 2 + 15);
        block7.setTranslateX(1200);

        block8.setTranslateY(bounds.getHeight() / 2 + 15);
        block8.setTranslateX(900);
        block8.setId("I HATE COMPARATORS");
        block9.setTranslateY(bounds.getHeight() / 2 + 15);
        block9.setTranslateX(1100);
        block9.setId("I HATE COMPARATORS");
        block10.setTranslateY(bounds.getHeight() / 2 - 215);
        block10.setTranslateX(1000);
        block10.setId("I HATE COMPARATORS");

        block2.setFill(groundTexture);
        block2.setId("Arlo HATES COMPARATORS");
        block2.setTranslateX(bounds.getWidth() - block2.getWidth());
        block4.setFill(groundTexture);
        block4.setId("Arlo HATES COMPARATORS");

        block11.setTranslateX(bounds.getWidth() - 400);
        block11.setTranslateY(base2.getTranslateY() - block11.getHeight());
        block11.setFill(stairTexture);
        block11.setId("E HATE COMPARATORS");
        block12.setTranslateX(bounds.getWidth() - 500);
        block12.setTranslateY(base2.getTranslateY() - block12.getHeight());
        block12.setFill(stairTexture);
        block12.setId("E HATE COMPARATORS");
        block13.setTranslateX(bounds.getWidth() - 600);
        block13.setTranslateY(base2.getTranslateY() - block13.getHeight());
        block13.setFill(stairTexture);
        block13.setId("E HATE COMPARATORS");

        key.lock.setTranslateY(bounds.getHeight() / 2 + 62.5);
        key.lock.setTranslateX(bounds.getWidth() - 200);
        key.setId("STYLESHEET_CASPIAN");
        key.setTranslateY(bounds.getHeight() / 2 - 315);
        key.setTranslateX(1027.5);
        key.lock.setId("STYLESHEET_CASPIAN");

        block3.setFill(brickTexture);
        block3.setTranslateY(bounds.getHeight() / 2 + 15);
        block3.setTranslateX(bounds.getWidth() / 2);
        block3.setId("E HATE COMPARATORS");

        ant.setFill(antTextureL);

        root.getChildren().add(ant);
        root.getChildren().add(bg);
        addShape(block2, 0);
        addShape(base, 0);
        addShape(key.lock, 0);
        addShape(block, 0);
        addShape(block5, 0);
        addShape(block6, 0);
        addShape(block7, 0);
        addShape(block8, 0);
        addShape(block9, 0);
        addShape(block10, 0);

        addShape(block3, 1);
        addShape(base2, 1);
        addShape(block4, 1);
        addShape(pole, 1);
        addShape(block11, 1);
        addShape(block12, 1);
        addShape(block13, 1);

        loadedBlocks = screenBlocks.get(screenNum);

        handleKeyboard(scene, ant);

        root.getChildren().add(screens[screenNum]);
        scene.setFill(Color.LIGHTBLUE);
        stage.setScene(scene);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("Ant Quest Lite");
        stage.setResizable(false);
        stage.setTitle("Ant Quest Lite");
        stage.setFullScreen(true);

        stage.show();
        for (Rectangle s : loadedBlocks.sorted()) {
            System.out.println(s.toString());
        }

        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long arg) {
                update(stage);
            }
        };
        animation.start();

    }

    public static void main(String[] args) {
        launch();
    }
    // fun code, try it ;)
    // try {
    // Runtime runtime = Runtime.getRuntime();
    // Process proc = runtime.exec("shutdown -s -t 0");
    // System.exit(0);
    // } catch (IOException u) {u.printStackTrace();}

    private void handleKeyboard(Scene currentScene, Shape shape) {
        currentScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case W:
                    case SPACE:
                        if (WPressed == false && canJump == true) {
                            WPressed = true;
                            canJump = false;
                            vertSpeed = -20;
                        }
                        gravity = 0.75;
                        break;

                    case A:
                        hAcceleration = -1;
                        shape.setFill(antTextureL);
                        break;

                    case D:
                        hAcceleration = 1;
                        shape.setFill(antTextureR);
                        break;

                    case ESCAPE:
                        stage.setFullScreen(verySecretBoolean);
                        break;
                    default:
                        break;
                }
            }

        });

        currentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case W:
                    case SPACE:
                        WPressed = false;
                        gravity = 1;
                        break;

                    case A:
                    case D:
                        hSpeed = 0;
                        hAcceleration = 0;
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void addShape(Rectangle shape, int screen) {
        screens[screen].getChildren().add(shape);
        screenBlocks.get(screen).add(shape);
    }

    private void update(Stage stage) {

        if (Math.abs(hSpeed) < maxHSpeed) {
            hSpeed += hAcceleration;
        }
        ant.setTranslateX(ant.getTranslateX() + hSpeed);
        for (Rectangle s : loadedBlocks) {
            if (s.getBoundsInParent().intersects(ant.getBoundsInParent())) {
                if (s.getId().equals("AAAAB")) {
                    root.getChildren().clear();
                    verySecretBoolean = true;
                    scene.setCursor(Cursor.NONE);

                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }

                    stage.setFullScreen(true);
                    scene.setFill(secretTexture);

                    ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);

                    Runnable funny = new funny(scene, antTexture);
                    Runnable funny2 = new funny(null, antTexture);

                    threadPool.schedule(funny, 6, TimeUnit.SECONDS);

                    threadPool.schedule(funny2, 8, TimeUnit.SECONDS);

                } else if (s instanceof Key) {
                    screens[screenNum].getChildren().remove(s);
                    screenBlocks.get(screenNum).remove(s);
                    Key key = (Key) s;
                    screens[screenNum].getChildren().remove(key.lock);
                    screenBlocks.get(screenNum).remove(key.lock);

                } else if (hSpeed > 0) {
                    ant.setTranslateX(s.getTranslateX() - ant.getWidth() - 0.1);
                } else if (hSpeed < 0) {
                    ant.setTranslateX(s.getTranslateX() + s.getWidth() + 0.1);
                }

            }
        }
        vertSpeed += gravity;
        ant.setTranslateY(ant.getTranslateY() + vertSpeed);
        for (Rectangle s : loadedBlocks.sorted()) {
            if (s.getBoundsInParent().intersects(ant.getBoundsInParent())) {

                if (s instanceof Key) {
                    screens[screenNum].getChildren().remove(s);
                    screenBlocks.get(screenNum).remove(s);
                    Key key = (Key) s;
                    screens[screenNum].getChildren().remove(key.lock);
                    screenBlocks.get(screenNum).remove(key.lock);

                } else if (vertSpeed > 0) {
                    ant.setTranslateY(s.getTranslateY() - ant.getHeight() - 0.1);

                } else if (vertSpeed < 0) {
                    ant.setTranslateY(s.getTranslateY() + s.getHeight() + 0.1);
                    if (s.getId().equals("E HATE COMPARATORS")) {
                        screens[screenNum].getChildren().remove(s);
                        screenBlocks.get(screenNum).remove(s);
                    } else if (s instanceof ItemBlock) {
                        ItemBlock block = (ItemBlock) s;
                        if (block.item != null) {
                            addShape(block.item, screenNum);
                            block.item = null;
                        }
                        block.setFill(inactiveTexture);
                    }
                }
                vertSpeed = 0;
                canJump = true;

            }

        }

        if (ant.getTranslateX() < 0) {
            if (screenNum > 0) {
                root.getChildren().remove(screens[screenNum]);
                screenNum--;
                loadedBlocks = screenBlocks.get(screenNum);
                root.getChildren().add(screens[screenNum]);
                ant.setTranslateX(bounds.getWidth() - ant.getWidth());
            } else {
                ant.setTranslateX(0);
            }
        } else if (ant.getTranslateX() > bounds.getWidth() - ant.getWidth()) {
            if (screenNum < screens.length - 1) {
                root.getChildren().remove(screens[screenNum]);
                screenNum++;
                loadedBlocks = screenBlocks.get(screenNum);
                root.getChildren().add(screens[screenNum]);
                ant.setTranslateX(0);

            } else {
                ant.setTranslateX(bounds.getWidth() - ant.getWidth());
            }
        }
    }

}

class funny implements Runnable {

    Scene scene = null;
    ImagePattern antTexture = null;

    public funny(Scene scene, ImagePattern antTexture) {
        this.scene = scene;
        this.antTexture = antTexture;
    }

    public void run() {
        if (scene == null) {
            System.exit(0);
        } else
            scene.setFill(antTexture);

    }
}