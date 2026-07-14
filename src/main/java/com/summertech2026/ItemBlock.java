package com.summertech2026;

import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class ItemBlock extends Rectangle{
    public Rectangle item = null;

    public ItemBlock(Rectangle item, ImagePattern texture) {
        this.item = item;
        setFill(texture);
        setWidth(100);
        setHeight(100);
    }

}
