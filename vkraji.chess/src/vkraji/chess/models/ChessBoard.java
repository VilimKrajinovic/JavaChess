/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess.models;

import javafx.scene.layout.GridPane;
import vkraji.chess.models.pieces.Bishop;
import vkraji.chess.models.pieces.King;
import vkraji.chess.models.pieces.Knight;
import vkraji.chess.models.pieces.Movement;
import vkraji.chess.models.pieces.Pawn;
import vkraji.chess.models.pieces.Piece;
import vkraji.chess.models.pieces.Queen;
import vkraji.chess.models.pieces.Rook;

/**
 *
 * @author amd
 */
public class ChessBoard extends GridPane {

    public Field[][] fields = new Field[8][8];
    public Field selectedField = null;

    public ChessBoard() {
        super();
        this.setMinSize(400, 400);
        this.setMaxSize(400, 400);

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if ((x + y) % 2 != 0) {
                    fields[x][y] = new Field(ChessColor.WHITE, x, y);
                } else {
                    fields[x][y] = new Field(ChessColor.BLACK, x, y);
                }

                //TODO: invert board somehow?
                this.add(fields[x][y], x, 7 - y);

                final int _x = x;
                final int _y = y;
                fields[x][y].setOnAction(e -> onFieldClick(_x, _y)); //lambda expression to use a function call
            }
        }

        this.initializeBoard();
    }

    private void onFieldClick(int x, int y) {
        Field clickedField = fields[x][y];

        //if a piece is trying to get moved
        if (this.selectedField != null
                && this.selectedField.isOccupied()
                && clickedField.getPieceColor() != this.selectedField.getPieceColor()) {
            
            Move move = new Move(this.selectedField.getX(), this.selectedField.getY(), x, y);
            
            this.processMove(move);
            
            this.selectedField=null;

        }else{
            if(fields[x][y].getPiece()!= null){
                setSelectedField(fields[x][y]);
            }
        }
    }

    public Field getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(Field selectedField) {
        this.selectedField = selectedField;
    }
    
    private void processMove(Move move){
        if(checkMove(move)){
            Field oldField= fields[move.getOldX()][move.getOldY()];
            Field newField= fields[move.getNewX()][move.getNewY()];
            
            newField.setPiece(oldField.releasePiece());
        }else{
            return;
        }
    }

    private void initializeBoard() {

        //TODO implement all chess pieces
        this.fields[0][0].setPiece(new Rook(ChessColor.     WHITE));
        this.fields[1][0].setPiece(new Knight(ChessColor.   WHITE));
        this.fields[2][0].setPiece(new Bishop(ChessColor.   WHITE));
        this.fields[3][0].setPiece(new Queen(ChessColor.    WHITE));
        this.fields[4][0].setPiece(new King(ChessColor.     WHITE));
        this.fields[5][0].setPiece(new Bishop(ChessColor.   WHITE));
        this.fields[6][0].setPiece(new Knight(ChessColor.   WHITE));
        this.fields[7][0].setPiece(new Rook(ChessColor.     WHITE));

        //pawns
        for (int i = 0; i < this.fields[0].length; i++) {
            this.fields[i][1].setPiece(new Pawn(ChessColor.WHITE));
        }

        // black pieces
        this.fields[0][7].setPiece(new Rook(ChessColor.     BLACK));
        this.fields[1][7].setPiece(new Knight(ChessColor.   BLACK));
        this.fields[2][7].setPiece(new Bishop(ChessColor.   BLACK));
        this.fields[3][7].setPiece(new Queen(ChessColor.    BLACK));
        this.fields[4][7].setPiece(new King(ChessColor.     BLACK));
        this.fields[5][7].setPiece(new Bishop(ChessColor.   BLACK));
        this.fields[6][7].setPiece(new Knight(ChessColor.   BLACK));
        this.fields[7][7].setPiece(new Rook(ChessColor.     BLACK));

        for (int i = 0; i < this.fields[0].length; i++) {
            this.fields[i][6].setPiece(new Pawn(ChessColor.BLACK));
        }
    }

    private boolean checkMove(Move move) {
        
        Field oldField;
        Field newField;
        Piece piece;
        Movement[] movement;
        
        if(move == null)
            return false;
        
        oldField = fields[move.getOldX()][move.getOldY()];
        newField = fields[move.getNewX()][move.getOldY()];
        
        //TODO
        piece=oldField.getPiece();
        movement = piece.getMovement();
        
        //JUST TO COMPILE; CHANGE LATER
        return true;
    }
}
