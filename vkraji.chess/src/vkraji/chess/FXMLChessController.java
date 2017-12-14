/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.chess;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import vkraji.chess.models.ChessBoard;
import vkraji.chess.models.ChessColor;
import vkraji.chess.models.Field;
import vkraji.chess.models.Move;
import vkraji.networking.*;
import vkraji.common.Constants;
import vkraji.rmi.ChatImplementation;
import vkraji.rmi.ChatInterface;

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

    @FXML
    private TextArea txtChatArea;

    @FXML
    private TextField txtMessage;

    @FXML
    private Button btnSend;

    public static ChessNetworkConnection connection;
    ChessBoard board;
    ChessColor playerColor;
    ChatInterface server;
    ChatInterface client;
    Registry reg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        choosePlayerColor();
        board = new ChessBoard(lblChessTimer, playerColor);
        bpMain.setCenter(board);

        if (playerColor == ChessColor.WHITE) {
            FXMLChessController.connection = createServer();
            initServerChat();
        } else {
            FXMLChessController.connection = createClient();
            initClientChat();
            board.setDisable(true);
        }

        try {
            FXMLChessController.connection.startConnection();
        } catch (Exception e) {
            System.out.println("Error: Failed to start connection");
            System.exit(1);
        }

        Timer t = new Timer();
        int delay = 1000;
        TimerTask clockTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new TimeThread(lblTime));
            }
        };
        t.scheduleAtFixedRate(clockTask, 0, delay);

        DocumentationWriter.printDocumentation();
    }

    public void choosePlayerColor() {
        Alert newGameAlert = new Alert(AlertType.CONFIRMATION);
        newGameAlert.setTitle("Start new game");
        newGameAlert.setHeaderText(null);
        newGameAlert.setContentText("Pick team color");

        ButtonType buttonTypeWhite = new ButtonType("White (Server)");
        ButtonType buttonTypeBlack = new ButtonType("Black (Client)");

        newGameAlert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeBlack);
        Optional<ButtonType> result = newGameAlert.showAndWait();

        if (result.get() == buttonTypeWhite) {
            this.playerColor = ChessColor.WHITE;
        } else if (result.get() == buttonTypeBlack) {
            this.playerColor = ChessColor.BLACK;
        }
    }

    private Server createServer() {
        return new Server(Constants.PORT_NUMBER, data -> {
            Platform.runLater(() -> {
                if (data instanceof Move) {
                    board.processOpponentMove((Move) data);
                } else // if (data instanceof String)
                {
                    System.out.println(data.toString() + "\n");
                }
            });
        });
    }

    private Client createClient() {
        return new Client("localhost", Constants.PORT_NUMBER, data -> {
            Platform.runLater(() -> {
                if (data instanceof Move) {
                    board.processOpponentMove((Move) data);
                } else // if (data instanceof String)
                {
                    System.out.println(data.toString() + "\n");
                }
            });
        });
    }

    public void onLoad() {

        try (ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(Constants.FILE_NAME))) {

            Object readObject = reader.readObject();

            if (readObject instanceof Field[][]) {
                board.setFields((Field[][]) readObject);
            }

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void onSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Constants.FILE_NAME))) {
            oos.writeObject(board.getFields());

        } catch (IOException ex) {
            Logger.getLogger(FXMLChessController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void onOpenChatClick(){
        
    }
    
    public void sendMessage() {
        try {
            //TODO
            System.out.println("Trying to send");
            if (this.playerColor == ChessColor.WHITE) {
                this.reg = LocateRegistry.getRegistry();
                ChatInterface tmp = (ChatInterface) this.reg.lookup("client");
                if(tmp != null){
                    txtChatArea.appendText(tmp.sendMessage(txtMessage.getText()));
                }
            }
            
            if(this.playerColor == ChessColor.BLACK){
                this.reg = LocateRegistry.getRegistry();
                ChatInterface stub = (ChatInterface) this.reg.lookup("server");
                if(stub != null){
                    txtChatArea.appendText(stub.sendMessage(txtMessage.getText()));
                }
            }

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            System.out.println("Still no client");
        }
    }

    private void initServerChat() {
        try {

            this.server = new ChatImplementation(Constants.SERVER_DEFAULT_NAME);
            this.reg = LocateRegistry.createRegistry(1235);

            ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(server, 0);
            this.reg.rebind(Constants.SERVER_DEFAULT_NAME, stub);
            txtChatArea.appendText("Server ready, waiting for client...\n");
        } catch (RemoteException ex) {
            txtChatArea.appendText(ex.getMessage() + "\n");
        }
    }

    private void initClientChat() {
        try {

            this.client = new ChatImplementation(Constants.CLIENT_DEFAULT_NAME);
            this.reg = LocateRegistry.getRegistry();
            this.client = (ChatInterface) this.reg.lookup("server");

            txtChatArea.appendText("Client ready...\n");

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(FXMLChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class DocumentationWriter {

        public static void printDocumentation() {
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
                Logger.getLogger(FXMLChessController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        private static String printClassDetails(String name) {

            try {
                StringBuilder sb = new StringBuilder();

                sb.append("//////////////////////////////////////////////////////////////////////////////////////////////////////").append(System.lineSeparator());
                Class<?> tmp = Class.forName(name);
                sb.append("Name: ").append(tmp.getName()).append(System.lineSeparator());

                sb.append("Hierarchy:");
                for (String s : getHierarchy(tmp)) {
                    sb.append(s).append("; ");
                }
                sb.append(System.lineSeparator());

                sb.append("Methods:");
                for (String s : getMethods(tmp)) {
                    sb.append(s).append("; ");
                }
                sb.append(System.lineSeparator());

                java.lang.reflect.Field[] fields = tmp.getDeclaredFields();
                sb.append("List of private fields: ");
                for (java.lang.reflect.Field field : fields) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        sb.append(System.lineSeparator()).append(field.getName());
                    }
                }

                sb.append("List of public methods:");
                Method[] methods = tmp.getMethods();

                for (Method method : methods) {
                    if (Modifier.isPublic(method.getModifiers())) {
                        sb.append(method.getName());
                        if (method.getParameters().length > 0) {
                            sb.append("\tMethod takes:").append(System.lineSeparator());
                            Parameter[] parameters = method.getParameters();
                            for (Parameter parameter : parameters) {
                                sb.append("\t\t").append(parameter.getType()).append(" ").append(parameter.getName());
                            }
                        } else {
                            sb.append("\tMethod doesnt take any paremeters.").append(System.lineSeparator());
                        }

                        sb.append("\tMethod returns:").append(System.lineSeparator());
                        sb.append("\t\t").append(method.getReturnType());
                        sb.append(System.lineSeparator());
                    }
                }

                sb.append("Fields:");
                for (String s : getFields(tmp)) {
                    sb.append(s).append("; ");
                }
                sb.append(System.lineSeparator());
                return sb.toString();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FXMLChessController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            return null;
        }

        private static Iterable<String> getModifiers(Class<?> tmp) {
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

        private static Iterable<String> getMethods(Class<?> tmp) {
            List<String> methodList = new ArrayList<>();

            String fullMethod = "";
            for (Method m : tmp.getMethods()) {
                for (String s : getModifiers(m.getClass())) {
                    fullMethod.concat(" " + s);
                }
                methodList.add(fullMethod + m.getName());
                fullMethod = "";
            }

            return methodList;
        }

        private static Iterable<String> getFields(Class<?> tmp) {
            List<String> fieldList = new ArrayList<>();

            for (java.lang.reflect.Field f : tmp.getDeclaredFields()) {
                fieldList.add(f.getType() + ": " + f.getName());
            }

            return fieldList;
        }

        private static Iterable<String> getHierarchy(Class<?> tmp) {
            List<String> classList = new ArrayList<>();

            Class<?> parentClass = tmp.getSuperclass();
            while (parentClass != null) {
                classList.add(parentClass.getName());
                parentClass = parentClass.getSuperclass();
            }

            return classList;
        }
    }

}
