package client;

import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Domas
 */
public class ConnectingGUI extends JFrame implements Runnable{
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JLabel msg[] = new JLabel[2];
    private Thread thread = null;
    private Thread timer = null;
    private Client client;
    private String username = null;
    public ConnectingGUI(Client client, String username){
        super("Online text editor");
        this.client = client;
        this.username = username;
     
        setSize(300, 100);
        
        setResizable(false);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation(screenSize.width/2-150, screenSize.height/2-50);
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        setIconImage(icon);
        
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(3, 3, 3, 3);
       
        msg[0] = new JLabel("Please wait.");
        msg[1] = new JLabel("Connecting to server");
        msg[1].setFont(new Font("Arial", PLAIN, 12));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(msg[0], gbc);
        
        gbc.gridy = 1;
        add(msg[1], gbc);
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thread = new Thread(this);
        thread.start();
        timer = new Thread(new Timer());
        timer.start();



    }
    public ConnectingGUI(Client client){
        this(client, null);
    }
    public void connectionFailed(){
        msg[0].setText("Error!");
        msg[1].setText("Could not connect to server!");
    }
    public void badUserData(){
        msg[0].setText("Error!");
        msg[1].setText("Wrong administrator username or password!");
    }


    @Override
    public void run() {
        if(username == null){
            if(client.connect()){
                setVisible(false);
                dispose();
            }else
                connectionFailed();
        }else{
             if(client.connect(username)){
                setVisible(false);
                dispose();
            }else
                connectionFailed();
            
        }

        timer.stop();
    }
    private class Timer implements Runnable{
        private void animation(int i){

            try {Thread.sleep(500);} catch (Exception e) { };
            if(i>=3){
                msg[1].setText("Connecting to server");
                animation(0);
            }else{
                msg[1].setText(msg[1].getText()+".");
                animation(++i);
            }
        }
        @Override
        public void run() {
            animation(0);
        }
        
    
    }
}
