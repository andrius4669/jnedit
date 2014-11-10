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
        //TO-DO: connection with server
        editor = new EditorGUI(this, EditorType.VIEW);
    }
    public void connect(String username, String password){
        //TO-DO: connection with server
        editor = new EditorGUI(this, EditorType.EDIT);
    }
    public static void main(String[] args) {
       // ConnectingGUI cGUI = new ConnectingGUI();
        //AddFileGUI afGUI = new AddFileGUI();
       // eGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    
       
        //new VerificationGUI(); new ConnectingGUI();
    }
    
}
