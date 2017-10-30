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
    
    public TimeThread(Label lblTime){
       this.lblTime = lblTime;
    }
    
    @Override
    public void run() {
        while(true){
            LocalDateTime currentTime = LocalDateTime.now();
            lblTime.setText(currentTime.toString());
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
