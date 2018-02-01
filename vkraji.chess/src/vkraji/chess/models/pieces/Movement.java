/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import java.io.Serializable;

/**
 *
 * @author amd
 */
public enum Movement implements Serializable {

    //X, Y
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1,0),
    LEFT(-1,0),
    
    UP_RIGHT(1,1),
    UP_LEFT(-1,1),
    DOWN_RIGHT(1,-1),
    DOWN_LEFT(-1,-1),
    
    //Pawn first move?
    UP_DOUBLE(0,2),
    DOWN_DOUBLE(0,-2),
    
    //Knight
    KNIGHT_LEFT_UP(-2, 1),
    KNIGHT_UP_LEFT(-1, 2),
    KNIGHT_UP_RIGHT(1, 2),
    KNIGHT_RIGHT_UP(2, 1),
    
    KNIGHT_RIGHT_DOWN(2, -1),
    KNIGHT_DOWN_RIGHT(1, -2),
    KNIGHT_DOWN_LEFT(-1, -2),
    KNIGHT_LEFT_DOWN(-2, -1),
    
    ;

    private int x;
    private int y;

    private Movement(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
