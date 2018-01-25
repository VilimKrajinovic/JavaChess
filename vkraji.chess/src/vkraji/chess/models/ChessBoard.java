/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import vkraji.chess.FXMLChessController;
import vkraji.chess.models.pieces.Bishop;
import vkraji.chess.models.pieces.King;
import vkraji.chess.models.pieces.Knight;
import vkraji.chess.models.pieces.Movement;
import vkraji.chess.models.pieces.Pawn;
import vkraji.chess.models.pieces.Piece;
import vkraji.chess.models.pieces.Queen;
import vkraji.chess.models.pieces.Rook;
import vkraji.common.Constants;

/**
 *
 * @author amd
 */
public class ChessBoard extends GridPane {

    private Label lblChessTimer;
    private Thread timerThread;
    private final Object timerLock = new Object();
    private int timeLeft;
    //private Element rootElement;
    //private Document doc;
    Timer timer = new Timer();

    protected static Field[][] fields = new Field[8][8];
    private static Field selectedField = null;
    private boolean timerPaused = false;

    public ChessBoard(Label lblChessTimer, ChessColor playerColor) {
        super();

        this.getStylesheets().add("vkraji/chess/fxmlchess.css");
        this.lblChessTimer = lblChessTimer;

        try {
            int tmp = Constants.loadConfig(Constants.CONFIG_NAME);
            this.lblChessTimer.setText(Integer.toString(tmp / 1000));
            this.timeLeft = Constants.loadConfig(Constants.CONFIG_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if ((x + y) % 2 != 0) {
                    fields[x][y] = new Field(ChessColor.WHITE, x, y);
                } else {
                    fields[x][y] = new Field(ChessColor.BLACK, x, y);
                }

                if (playerColor == ChessColor.WHITE) {
                    this.add(fields[x][y], x, 7 - y);
                } else {
                    this.add(fields[x][y], 7 - x, y);
                }

                final int xPosition = x;
                final int yPosition = y;
                fields[x][y]
                        .setOnAction(e -> onFieldClick(xPosition, yPosition));
            }
        }

        createXML();
        createTimer();
        pauseTimer();
        this.initializeBoard();
    }

    public Field[][] getFields() {
        return fields;
    }

    private void createTimer() {
        this.timerThread = new Thread(() -> {
            while (timeLeft > 0) {
                synchronized (timerLock) {

                    try {
                        if (this.timerPaused == true) {
                            timerLock.wait();
                        }
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    lblChessTimer.setText(Integer
                                            .toString(timeLeft / 1000));
                                    if (timeLeft < 0) {
                                        timer.cancel();
                                    }
                                });
                            }
                        }, Calendar.getInstance()
                                .getTime(), Constants.INTERVAL);
                        timerLock.wait(Constants.INTERVAL);
                        timeLeft -= Constants.INTERVAL;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

        });

        this.timerThread.setName("Timer thread");
        this.timerThread.start();
    }

    public void setFields(Field[][] fields) {

        // when loading from file try to do something?
        ChessBoard.fields = fields;

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                this.add(fields[x][y], x, 7 - y);
            }
        }
    }

    public boolean sendMove(Move m) {
        try {
            FXMLChessController.connection.send(m);
            addMoveToXML(m);
        } catch (Exception e) {
            System.out.println("Failed to send move");
            return false;
        }

        return true;
    }

    private void onFieldClick(int x, int y) {
        Field clickedField = fields[x][y];

        // if a piece is trying to get moved
        if (ChessBoard.selectedField != null
                && ChessBoard.selectedField.isOccupied()
                && clickedField.getPieceColor() != ChessBoard.selectedField
                .getPieceColor()) {

            Move move = new Move(ChessBoard.selectedField.getX(),
                    ChessBoard.selectedField.getY(), x, y);

            if (this.processMove(move)) {
                if (this.sendMove(move)) {
                    pauseTimer();
                    this.setDisable(true);
                }
            }

            this.setSelectedField(null);

        } else {
            if (fields[x][y].getPiece() != null) {
                this.setSelectedField(fields[x][y]);
            }
        }
    }

    public Field getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(Field selectedField) {
        if (ChessBoard.selectedField != null) {
            ChessBoard.selectedField.getStyleClass()
                    .removeAll("chess-field-active");
        }
        ChessBoard.selectedField = selectedField;

        if (ChessBoard.selectedField != null) {
            ChessBoard.selectedField.getStyleClass().add("chess-field-active");
        }
    }

    private boolean processMove(Move move) {
        if (checkMove(move)) {
            Field oldField = fields[move.getOldX()][move.getOldY()];
            Field newField = fields[move.getNewX()][move.getNewY()];

            newField.setPiece(oldField.releasePiece());

            resetTimer();
            resumeTimer();

            return true;
        } else {
            return false;
        }
    }

    private void initializeBoard() {
        ChessBoard.fields[0][0].setPiece(new Rook(ChessColor.WHITE));
        ChessBoard.fields[1][0].setPiece(new Knight(ChessColor.WHITE));
        ChessBoard.fields[2][0].setPiece(new Bishop(ChessColor.WHITE));
        ChessBoard.fields[3][0].setPiece(new Queen(ChessColor.WHITE));
        ChessBoard.fields[4][0].setPiece(new King(ChessColor.WHITE));
        ChessBoard.fields[5][0].setPiece(new Bishop(ChessColor.WHITE));
        ChessBoard.fields[6][0].setPiece(new Knight(ChessColor.WHITE));
        ChessBoard.fields[7][0].setPiece(new Rook(ChessColor.WHITE));

        // pawns
        for (int i = 0; i < ChessBoard.fields[0].length; i++) {
            ChessBoard.fields[i][1].setPiece(new Pawn(ChessColor.WHITE));
        }

        // black pieces
        ChessBoard.fields[0][7].setPiece(new Rook(ChessColor.BLACK));
        ChessBoard.fields[1][7].setPiece(new Knight(ChessColor.BLACK));
        ChessBoard.fields[2][7].setPiece(new Bishop(ChessColor.BLACK));
        ChessBoard.fields[3][7].setPiece(new Queen(ChessColor.BLACK));
        ChessBoard.fields[4][7].setPiece(new King(ChessColor.BLACK));
        ChessBoard.fields[5][7].setPiece(new Bishop(ChessColor.BLACK));
        ChessBoard.fields[6][7].setPiece(new Knight(ChessColor.BLACK));
        ChessBoard.fields[7][7].setPiece(new Rook(ChessColor.BLACK));

        for (int i = 0; i < ChessBoard.fields[0].length; i++) {
            ChessBoard.fields[i][6].setPiece(new Pawn(ChessColor.BLACK));
        }
    }

    public void processOpponentMove(Move m) {
        if (processMove(m)) {
            // unlock board
            this.setDisable(false);
        }
    }

    @SuppressWarnings("unused")
    private boolean checkMove(Move move) {

        Field oldField;
        Field newField;
        Piece piece;
        Movement[] movement;

        if (move == null) {
            return false;
        }

        pauseTimer();

        oldField = fields[move.getOldX()][move.getOldY()];
        newField = fields[move.getNewX()][move.getOldY()];

        // TODO
        piece = oldField.getPiece();
        movement = piece.getMovement();

        // TODO JUST TO COMPILE; CHANGE LATER
        return true;
    }

    private void resetTimer() {
        try {
            this.timeLeft = Constants.loadConfig(Constants.CONFIG_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pauseTimer() {
        this.timerPaused = true;
    }

    private void resumeTimer() {
        synchronized (timerLock) {
            this.timerPaused = false;
            timerLock.notifyAll();
        }
    }

    private void createXML() {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("ChessGame");
            doc.appendChild(rootElement);

            Element ispit = doc.createElement("Moves");
            rootElement.appendChild(ispit);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("chessgame.xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addMoveToXML(Move m) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("chessgame.xml");
            Element root = document.getDocumentElement();

            Element move = document.createElement("Move");
            move.setAttribute("newX", Integer.toString(m.getNewX()));
            move.setAttribute("newY", Integer.toString(m.getNewY()));
            move.setAttribute("oldX", Integer.toString(m.getOldX()));
            move.setAttribute("oldY", Integer.toString(m.getOldY()));
            root.getChildNodes().item(0).appendChild(move);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("chessgame.xml");
            transformer.transform(source, result);

        } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ChessBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
