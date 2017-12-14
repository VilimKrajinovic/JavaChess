/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.rmi;

import java.rmi.RemoteException;

/**
 *
 * @author amd
 */
public class ChatImplementation implements ChatInterface {

    private String name;

    public ChatImplementation(String name) {
        this.name = name;
    }
    
    
    
    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        String tmp;
        tmp = this.name + ": " + message;
        return tmp;
    }
    
}
