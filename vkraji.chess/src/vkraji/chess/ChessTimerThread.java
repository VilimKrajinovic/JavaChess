/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.util.Observable;

import javafx.scene.control.Label;
import vkraji.common.Constants;

/**
 *
 * @author amd
 */
public class ChessTimerThread extends Observable implements Runnable {

    private Label lblChessTimer;
    private int   timeLeft;

    public ChessTimerThread(Label lblChessTimer) {
        this.lblChessTimer = lblChessTimer;
        this.timeLeft = Constants.TIME;
    }

    @Override
    public void run() {
        lblChessTimer.setText(Integer.toString(timeLeft / 1000));
        timeLeft -= Constants.INTERVAL;
    }

}
