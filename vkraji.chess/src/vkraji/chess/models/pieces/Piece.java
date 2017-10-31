/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import javafx.scene.image.Image;
import vkraji.chess.models.ChessColor;

/**
 *
 * @author amd
 */
public abstract class Piece {
    private boolean hasMoved; //pawn movement
    private boolean usesSingleMove;
    private Image image; //TODO 
    private ChessColor color;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public abstract Movement[] getMovement();
    public abstract String getName();
    
    private String getColorName(){
        if(color == ChessColor.WHITE)
            return "white";
        else
            return "black";
    }
    
    public Piece(ChessColor color){
        this.color = color;
        
        //TODO: make it so that the correct image gets used for each piece
        
        String filename= getColorName() + "-" + this.getName()+".png";
        String path = "vkraji/chess/assets/";
        this.image = new Image(path+filename);
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
