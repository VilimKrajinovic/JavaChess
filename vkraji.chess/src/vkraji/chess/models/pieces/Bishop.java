/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models.pieces;

import vkraji.common.Constants;
import java.io.Serializable;
import vkraji.chess.models.ChessColor;

/**
 *
 * @author amd
 */
public class Bishop extends Piece implements Serializable {

    public Bishop(ChessColor color) {
        super(color);
        this.setValue(Constants.VALUE_OF_BISHOP);
    }

    @Override
    public Movement[] getMovement() {
        return new Movement[] {
                Movement.UP_LEFT,
                Movement.UP_RIGHT,
                Movement.DOWN_LEFT,
                Movement.DOWN_RIGHT };
    }

    @Override
    public String getName() {
        return "bishop";
    }

}
