/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import java.util.ArrayList;
import vkraji.chess.models.ChessColor;

/**
 *
 * @author amd
 */
public class Knight extends Piece {

    
    public Knight(ChessColor color){
        super(color);
        this.setValue(Constants.VALUE_OF_KNIGHT);
        
    }
    
    @Override
    public Movement[] getMovement() {
        Movement[] movement =
            {
                Movement.KNIGHT_LEFT_UP,
                Movement.KNIGHT_UP_LEFT,
                Movement.KNIGHT_UP_RIGHT,
                Movement.KNIGHT_RIGHT_UP,
                Movement.KNIGHT_RIGHT_DOWN,
                Movement.KNIGHT_DOWN_RIGHT,
                Movement.KNIGHT_DOWN_LEFT,
                Movement.KNIGHT_LEFT_DOWN
            };
        
        return movement;
    }

    
    @Override
    public String getName() {
        return "knight";
    }
    
}
