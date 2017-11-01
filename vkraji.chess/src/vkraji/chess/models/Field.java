/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import vkraji.chess.models.pieces.Piece;

/**
 *
 * @author amd
 */
public class Field extends Button implements Serializable {

    private int x;
    private int y;
    private Piece piece;

    public Field(ChessColor color, int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.piece = null; //add piece later
        this.setMaxSize(50, 50);
        this.setMinSize(50, 50);
    }

    public Piece releasePiece() {
        Piece tmp = this.piece;
        setPiece(null);
        return tmp;
    }

    public ChessColor getPieceColor() {
        if (this.piece != null) {
            return piece.getColor();
        } else {
            return null;
        }
    }

    public boolean isOccupied() {
        return this.piece != null;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (isOccupied()) {
            this.setGraphic(new ImageView(new Image(this.piece.getFilePath())));
        } else {
            this.setGraphic(new ImageView());
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
