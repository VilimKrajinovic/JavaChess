/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import vkraji.chess.models.ChessBoard;
import vkraji.chess.models.Field;
import vkraji.chess.models.pieces.Constants;

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

    ChessBoard board = new ChessBoard();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public void onLoad() {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(Constants.FILE_NAME))) {
            Object readObject = reader.readObject();

            if (readObject instanceof Field[][]) {
                board.setFields((Field[][]) readObject);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EOFException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.FILE_NAME))) {
            oos.writeObject(board.getFields());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
