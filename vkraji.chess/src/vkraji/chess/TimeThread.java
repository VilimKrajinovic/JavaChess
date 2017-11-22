/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.time.LocalDateTime;

import javafx.scene.control.Label;

/**
 *
 * @author amd
 */
public class TimeThread implements Runnable {

    private Label lblTime;

    public TimeThread(Label lblTime) {
        this.lblTime = lblTime;
    }

    @Override
    public void run() {
        LocalDateTime currentTime = LocalDateTime.now();

        lblTime.setText(getTime(currentTime));
    }

    private String getTime(LocalDateTime t) {
        String hours;
        String minutes;
        String seconds;

        if (t.getHour() < 10) {
            hours = "0" + t.getHour();
        } else {
            hours = Integer.toString(t.getHour());
        }

        if (t.getMinute() < 10) {
            minutes = "0" + t.getMinute();
        } else {
            minutes = Integer.toString(t.getMinute());
        }

        if (t.getSecond() < 10) {
            seconds = "0" + t.getSecond();
        } else {
            seconds = Integer.toString(t.getSecond());
        }

        return hours + ":" + minutes + ":" + seconds;
    }
}
