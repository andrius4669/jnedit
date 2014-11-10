package client;

import javax.swing.JFrame;

/**
 *
 * @author Domas
 */
public class Client{

    public Client(){
        VerificationGUI verGUI = new VerificationGUI(this);
        verGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void connect(){
        
    }
    public void connect(String username, String password){
        
    }
    public static void main(String[] args) {
       // ConnectingGUI cGUI = new ConnectingGUI();
        //AddFileGUI afGUI = new AddFileGUI();
       // eGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    
       
        //new VerificationGUI(); new ConnectingGUI();
    }
    
}
