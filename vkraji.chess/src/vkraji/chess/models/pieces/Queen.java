/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import vkraji.chess.models.ChessColor;

/**
 *
 * @author amd
 */
public class Queen extends Piece {

    public Queen(ChessColor color){
        super(color);
        this.setValue(Constants.VALUE_OF_QUEEN);
    }
    
    @Override
    public Movement[] getMovement() {
        Movement[] movement = {
            Movement.UP,
            Movement.DOWN,
            Movement.LEFT,
            Movement.RIGHT,
            Movement.UP_LEFT,
            Movement.UP_RIGHT,
            Movement.DOWN_LEFT,
            Movement.DOWN_RIGHT
        };
        
        return movement;
    }

    @Override
    public String getName() {
        return "queen";
    }
    
}
