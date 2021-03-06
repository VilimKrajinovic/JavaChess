/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import java.io.Serializable;

import vkraji.chess.models.ChessColor;

/**
 *
 * @author amd
 */
public abstract class Piece implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1187447247680053942L;
    private boolean    hasMoved;      // pawn movement
    private boolean    usesSingleMove;
    // transient private Image image; doesnt serialize, trying to save it with
    // string filepath
    private ChessColor color;
    private int        value;
    private String     filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public abstract Movement[] getMovement();

    public abstract String getName();

    private String getColorName() {
        if (color == ChessColor.WHITE) {
            return "white";
        } else {
            return "black";
        }
    }

    public Piece(ChessColor color) {
        this.color = color;

        String filename = getColorName() + "-" + this.getName() + ".png";
        String path = "vkraji/chess/assets/";
        this.filePath = path + filename;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public ChessColor getColor() {
        return color;
    }

    public void setColor(ChessColor color) {
        this.color = color;
    }

    public boolean isUsesSingleMove() {
        return usesSingleMove;
    }

    public void setUsesSingleMove(boolean usesSingleMove) {
        this.usesSingleMove = usesSingleMove;
    }
}
