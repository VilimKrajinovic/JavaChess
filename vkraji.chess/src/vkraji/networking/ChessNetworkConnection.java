package vkraji.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public abstract class ChessNetworkConnection {
    private ChessNetworkThread     connThread = new ChessNetworkThread();
    private Consumer<Serializable> onRecieveCallBack;

    public ChessNetworkConnection(Consumer<Serializable> onRecieveCallback) {
        this.onRecieveCallBack = onRecieveCallback;

        connThread.setDaemon(true);
    }

    /**
     * Init connection
     * 
     * @throws Exception
     */
    public void startConnection() throws Exception {
        connThread.start();
    }

    /**
     * Send Serializable data
     * 
     * @param data
     * @throws Exception
     */
    public void send(Serializable data) throws Exception {
        connThread.out.writeObject(data);
    }

    /**
     * Close connection
     * 
     * @throws Exception
     */
    public void closeConnection() throws Exception {
        connThread.socket.close();
    }

    protected abstract boolean isServer();

    protected abstract String getIP();

    protected abstract int getPort();

    private class ChessNetworkThread extends Thread {
        private Socket             socket;
        private ObjectOutputStream out;

        @Override
        public void run() {
            try (ServerSocket server = isServer() ? new ServerSocket(getPort())
                    : null;

                    Socket socket = isServer() ? server.accept()
                            : new Socket(getIP(), getPort());

                    ObjectOutputStream out = new ObjectOutputStream(
                            socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(
                            socket.getInputStream());) {

                onRecieveCallBack.accept("Connection established");
                this.socket = socket;
                this.out = out;

                socket.setTcpNoDelay(true);

                while (true) {
                    try {
                        Serializable data = (Serializable) in.readObject();
                        onRecieveCallBack.accept(data);
                        
                    } catch (SocketException e) {
                        System.out.println("Server closed...");
                        System.exit(1);
                    }
                }

            } catch (ConnectException e) {
                onRecieveCallBack.accept("Error establishing connection...");
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Connection closed...");
            }
        }
    }
}
