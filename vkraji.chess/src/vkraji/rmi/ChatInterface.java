/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author amd
 */
public interface ChatInterface extends Remote {
    String getName() throws RemoteException;
    String sendMessage(String message) throws RemoteException;
    ArrayList<String> getMessages() throws RemoteException;
    
}
