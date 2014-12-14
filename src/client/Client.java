package client;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import utils.EpicList;

/**
 *
 * @author Domas
 */
public class Client{
    EditorGUI editor;
    Socket csock;
    public Client(){
        VerificationGUI verGUI = new VerificationGUI(this);
        verGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void connect(){
        ConnectingGUI conGUI = new ConnectingGUI();
        conGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //TO-DO: connection with server
	try { csock = new Socket("jnedit.andrius4669.org", 61337); }
	catch(UnknownHostException e) { csock = null; }
	catch(IOException e) { csock = null; }

        if(csock != null) {
		conGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		conGUI.setVisible(false);
		conGUI.dispose();
		editor = new EditorGUI(this, EditorType.VIEW);
	} else {
		conGUI.connectionFailed();
	}
    }
    public void connect(String username, String password){
        ConnectingGUI conGUI = new ConnectingGUI();
        conGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //TO-DO: connection with server
        
        //IF Connection success
        conGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conGUI.setVisible(false);
        conGUI.dispose();
        editor = new EditorGUI(this, EditorType.VIEW);
        //ELSE IF Wrong username or password
        conGUI.badUserData();
        //ELSE
        conGUI.connectionFailed();
    }
    public static void main(String[] args) {
       // Client client = new Client();
        EpicList<String> test = new EpicList<String>();
        test.add("vienas");
        test.add("du");
  
                
       // EditorGUI eGUI = new EditorGUI(client, EditorType.EDIT);
        
    }
    
}
