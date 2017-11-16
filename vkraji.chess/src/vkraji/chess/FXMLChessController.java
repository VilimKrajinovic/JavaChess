/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import vkraji.common.Constants;

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
    
    @FXML
    private Label lblChessTimer;        

    ChessBoard board;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        board = new ChessBoard(lblChessTimer);
        //board.setGridLinesVisible(true);
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

        printDocumentation();
    }

    private void printDocumentation() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("documentation.txt"))) {

            bw.write(printClassDetails("vkraji.chess.models.ChessBoard"));
            bw.write(printClassDetails("vkraji.chess.models.Field"));
            bw.write(printClassDetails("vkraji.chess.models.Move"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Piece"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Bishop"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.King"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Knight"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Movement"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Pawn"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Queen"));
            bw.write(printClassDetails("vkraji.chess.models.pieces.Rook"));
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onLoad() {

        try (ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(Constants.FILE_NAME))) {

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
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Constants.FILE_NAME))) {
            oos.writeObject(board.getFields());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String printClassDetails(String name) {

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("//////////////////////////////////////////////////////////////////////////////////////////////////////"+System.lineSeparator());
            Class<?> tmp = Class.forName(name);
            sb.append("Name: " + tmp.getName()+ System.lineSeparator());

            sb.append("Hierarchy:");
            for (String s : getHierarchy(tmp)) {
                sb.append(s + "; ");
            }
            sb.append(System.lineSeparator());

            sb.append("Methods:");
            for (String s : getMethods(tmp)) {
                sb.append(s + "; ");
            }
            sb.append(System.lineSeparator());

            java.lang.reflect.Field[] fields = tmp.getDeclaredFields();
            sb.append("List of private fields: ");
            for (java.lang.reflect.Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    sb.append(System.lineSeparator() + field.getName());
                }
            }

            sb.append("List of public methods:");
            Method[] methods = tmp.getMethods();

            for (Method method : methods) {
                if (Modifier.isPublic(method.getModifiers())) {
                    sb.append(method.getName());
                    if (method.getParameters().length > 0) {
                        sb.append("\tMethod takes:"+System.lineSeparator());
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            sb.append("\t\t" + parameter.getType() + " " + parameter.getName());
                        }
                    } else {
                        sb.append("\tMethod doesnt take any paremeters."+System.lineSeparator());
                    }

                    sb.append("\tMethod returns:"+System.lineSeparator());
                    sb.append("\t\t" + method.getReturnType());
                    sb.append(System.lineSeparator());
                }
            }

            sb.append("Fields:");
            for (String s : getFields(tmp)) {
                sb.append(s + "; ");
            }
            sb.append(System.lineSeparator());
            return sb.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Iterable<String> getModifiers(Class<?> tmp) {
        List<String> modifiersList = new ArrayList<>();
        int mod = tmp.getModifiers();

        if (Modifier.isPrivate(mod)) {
            modifiersList.add(Constants.MOD_PRIVATE);
        }
        if (Modifier.isPublic(mod)) {
            modifiersList.add(Constants.MOD_PUBLIC);
        }
        if (Modifier.isProtected(mod)) {
            modifiersList.add(Constants.MOD_PROTECTED);
        }
        if (Modifier.isStatic(mod)) {
            modifiersList.add(Constants.MOD_STATIC);
        }
        if (Modifier.isAbstract(mod)) {
            modifiersList.add(Constants.MOD_ABSTRACT);
        }
        if (Modifier.isFinal(mod)) {
            modifiersList.add(Constants.MOD_FINAL);
        }
        if (Modifier.isInterface(mod)) {
            modifiersList.add(Constants.MOD_INTERFACE);
        }

        return modifiersList;
    }

    private Iterable<String> getMethods(Class<?> tmp) {
        List<String> methodList = new ArrayList<>();

        String fullMethod = "";
        for (Method m : tmp.getMethods()) {
            for (String s : getModifiers(m.getClass())) {
                fullMethod.concat(s + " ");
            }
            methodList.add(fullMethod + m.getName());
            fullMethod = "";
        }

        return methodList;
    }

    private Iterable<String> getFields(Class<?> tmp) {
        List<String> fieldList = new ArrayList<>();

        for (java.lang.reflect.Field f : tmp.getDeclaredFields()) {
            fieldList.add(f.getType() + ": " + f.getName());
        }

        return fieldList;
    }

    private Iterable<String> getHierarchy(Class<?> tmp) {
        List<String> classList = new ArrayList<>();

        Class<?> parentClass = tmp.getSuperclass();
        while (parentClass != null) {
            classList.add(parentClass.getName());
            parentClass = parentClass.getSuperclass();
        }

        return classList;
    }

}
