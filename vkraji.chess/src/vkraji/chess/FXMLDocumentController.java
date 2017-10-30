/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author amd
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private BorderPane bpMain;

    @FXML
    private GridPane gpBoard;

    @FXML
    private Pane paneTest;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Rectangle r = new Rectangle(40,40);
        r.setFill(Color.BLUE);
        bpMain.setRight(r);

    }

}
