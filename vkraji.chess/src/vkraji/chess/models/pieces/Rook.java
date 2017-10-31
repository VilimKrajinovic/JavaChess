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
public class Rook extends Piece {

    public Rook(ChessColor color){
        super(color);
        this.setValue(Constants.VALUE_OF_ROOK);
    }
    
    @Override
    public Movement[] getMovement() {
        Movement[] movement = {
            Movement.DOWN,
            Movement.UP,
            Movement.LEFT,
            Movement.RIGHT
        };
        return movement;
    }

    @Override
    public String getName() {
        return "rook";
    }

}
