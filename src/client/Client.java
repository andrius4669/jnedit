package client;

import javax.swing.JFrame;

/**
 *
 * @author Domas
 */
public class Client{
    EditorGUI editor;
    public Client(){
        VerificationGUI verGUI = new VerificationGUI(this);
        verGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void connect(){
        ConnectingGUI conGUI = new ConnectingGUI();
        conGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //TO-DO: connection with server

        //IF Connection success
        conGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conGUI.setVisible(false);
        conGUI.dispose();
        editor = new EditorGUI(this, EditorType.VIEW);
        //ELSE
        conGUI.connectionFailed();
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
        Client client = new Client();
       // EditorGUI eGUI = new EditorGUI(client, EditorType.EDIT);
        
    }
    
}
