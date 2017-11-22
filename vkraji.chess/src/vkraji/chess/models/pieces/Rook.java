/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import java.io.Serializable;

import vkraji.chess.models.ChessColor;
import vkraji.common.Constants;

/**
 *
 * @author amd
 */
public class Rook extends Piece implements Serializable {

    public Rook(ChessColor color) {
        super(color);
        this.setValue(Constants.VALUE_OF_ROOK);
    }

    @Override
    public Movement[] getMovement() {

        return new Movement[] {
                Movement.DOWN,
                Movement.UP,
                Movement.LEFT,
                Movement.RIGHT };
    }

    @Override
    public String getName() {
        return "rook";
    }

}
