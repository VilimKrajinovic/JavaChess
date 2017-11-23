/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkraji.common;

import java.io.File;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author amd
 */
public class Constants {

    public static final int    TIME            = 30 * 1000;
    public static final int    INTERVAL        = 1000;

    public static final int    VALUE_OF_PAWN   = 1;
    public static final int    VALUE_OF_KNIGHT = 3;
    public static final int    VALUE_OF_BISHOP = 3;
    public static final int    VALUE_OF_ROOK   = 5;
    public static final int    VALUE_OF_QUEEN  = 9;

    public static final String MOD_PRIVATE     = "Private";
    public static final String MOD_PUBLIC      = "Public";
    public static final String MOD_PROTECTED   = "Protected";
    public static final String MOD_STATIC      = "Static";
    public static final String MOD_ABSTRACT    = "Abstract";
    public static final String MOD_FINAL       = "Final";
    public static final String MOD_INTERFACE   = "Interface";

    public static final String FILE_NAME       = "board.dat";
    public static final String CONFIG_PATH     = "../";
    public static final String CONFIG_NAME     = "config.ini";
    public static final int    PORT_NUMBER     = 1234;

    private static int loadConfig(String fileName) throws NamingException{
        
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.fscontext.RefFSContextFactory");
        env.put(Context.PROVIDER_URL, "file:" + CONFIG_PATH);
        
        Context context = new InitialContext(env);
        Object in = context.lookup(fileName);
        File file = (File) in;
        
        return 1;
    }
}
