/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javax.print.attribute.standard.DateTimeAtCompleted;

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
        String hours, minutes, seconds;

        if (t.getHour() < 10) {
            hours = "0" + t.getHour();
        } else {
            hours = "" + t.getHour();
        }

        if (t.getMinute() < 10) {
            minutes = "0" + t.getMinute();
        } else {
            minutes = "" + t.getMinute();
        }

        if (t.getSecond() < 10) {
            seconds = "0" + t.getSecond();
        } else {
            seconds = "" + t.getSecond();
        }

        return hours + ":" + minutes + ":" + seconds;
    }
}
