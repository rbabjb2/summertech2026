package com.summertech2026;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;

public class Key extends Rectangle {

    public Rectangle lock;
    
    public Key(double width, double height, ImagePattern lockTexture, ImagePattern keyTexture) {

        lock = new Rectangle(width, height);
        lock.setFill(lockTexture);

        this.setFill(keyTexture);

        this.setWidth(45);
        this.setHeight(80);
    }
}