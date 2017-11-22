package vkraji.networking;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends ChessNetworkConnection {

    private int port;

    public Server(int port, Consumer<Serializable> onRecieveCallback) {
        super(onRecieveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }

}
