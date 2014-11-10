package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Domas
 */
public class DeleteFileGUI extends JFrame{
    private final JLabel title;
    private final JButton yes, no;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final EditorGUI editor;
    private final int fileId;
    public DeleteFileGUI(EditorGUI eGUI, int id){
       super("Delete File");
       editor = eGUI;
       fileId = id;
       setSize(300, 120);
       setResizable(false);
       setLayout(new GridBagLayout());
        
       Toolkit tk = Toolkit.getDefaultToolkit();
       Dimension screenSize = tk.getScreenSize();
       setLocation(screenSize.width/2-150, screenSize.height/2-50);
        
       Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
       setIconImage(icon);
       
       title = new JLabel("Are you sure want to delete this file?");
       yes = new JButton("Yes");
       no = new JButton("No");
       yes.setPreferredSize(new Dimension(100, 30));
       no.setPreferredSize(new Dimension(100, 30));
       gbc.insets = new Insets(5, 5, 5, 5);
       gbc.gridwidth = 2;
       add(title, gbc);
       gbc.gridwidth = 1;
       gbc.gridy = 1;
       add(yes, gbc);
       gbc.gridx = 1;
       add(no, gbc);
       setVisible(true);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       yes.addActionListener(new onButtonClick());
       no.addActionListener(new onButtonClick());
       
    }
    private class onButtonClick implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == yes){
                editor.deleteFile(fileId);
                setVisible(false);
                dispose();
            }
            if(e.getSource() == no){
                setVisible(false);
                dispose();
            }
        }

        
        
    }
    
}
