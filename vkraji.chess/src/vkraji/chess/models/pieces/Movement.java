/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

/**
 *
 * @author amd
 */
public enum Movement {

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
    DOWN_DOUBLE(0,-2)
    
    //@TODO : Knight logic
    
    ;

    private int x;
    private int y;

    private Movement(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
