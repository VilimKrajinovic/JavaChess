/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import vkraji.chess.models.ChessBoard;

/**
 * FXML Controller class
 *
 * @author amd
 */
public class FXMLChessController implements Initializable {

    @FXML
    private BorderPane bpMain;
    
    @FXML
    private Label lblTime;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ChessBoard board = new ChessBoard();
        board.setGridLinesVisible(true);
        bpMain.setCenter(board);
        
        Timer t = new Timer();
        int delay = 1000;
        
        TimerTask clockTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new TimeThread(lblTime));
            }
        };
        t.scheduleAtFixedRate(clockTask, 0, delay);
    }
}
