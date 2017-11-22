/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import java.io.Serializable;
import java.util.ArrayList;

import vkraji.chess.models.ChessColor;
import vkraji.common.Constants;

/**
 *
 * @author amd
 */
public class Pawn extends Piece implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Pawn(ChessColor color) {
        super(color);
        this.setValue(Constants.VALUE_OF_PAWN);
    }

    @Override
    public Movement[] getMovement() {
        Movement[] movement = {};

        if (this.getColor() == ChessColor.WHITE) {
            ArrayList<Movement> white = new ArrayList<>();
            white.add(Movement.UP);
            white.add(Movement.UP_RIGHT);
            white.add(Movement.UP_LEFT);

            if (!this.isHasMoved()) {
                white.add(Movement.UP_DOUBLE);
            }
            movement = white.toArray(movement);
        } else {
            ArrayList<Movement> black = new ArrayList<>();
            black.add(Movement.DOWN);
            black.add(Movement.DOWN_RIGHT);
            black.add(Movement.DOWN_LEFT);

            if (!this.isHasMoved()) {
                black.add(Movement.DOWN_DOUBLE);
            }
            movement = black.toArray(movement);
        }

        return movement;
    }

    @Override
    public String getName() {
        return "pawn";
    }

}
