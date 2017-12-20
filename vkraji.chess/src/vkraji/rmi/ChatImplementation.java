/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author amd
 */
public class ChatImplementation implements ChatInterface {

    private String name;
    private ArrayList<String> messages;

    public ChatImplementation(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    public void sendMessageOffline(String message) {
        messages.add(message + "\n");
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        String tmp;
        tmp = message + "\n";
        this.messages.add(tmp);
        return tmp;
    }

    @Override
    public ArrayList<String> getMessages() throws RemoteException {
        return messages;
    }

    @Override
    public ArrayList<String> getMessagesOffline() throws RemoteException {
        return messages;
    }

}
