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
import javax.swing.JTextField;

/**
 *
 * @author Domas
 */
public class AddFileGUI extends JFrame{
    GridBagConstraints gbc = new GridBagConstraints();
    EditorGUI editor;
    JLabel title;
    JTextField textBox;
    JButton submit;
    public AddFileGUI(EditorGUI eGUI){
        super("Add file");
        editor = eGUI;
        
        setSize(300, 100);
        
        setResizable(false);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation(screenSize.width/2-150, screenSize.height/2-50);
        
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        setIconImage(icon);
        
        setLayout(new GridBagLayout());
        
        title = new JLabel("Enter file name:");
        textBox = new JTextField();
        submit = new JButton("Add");
        gbc.insets = new Insets(3, 3, 3, 3);
        textBox.setPreferredSize(new Dimension(200, 26));
        add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(textBox, gbc);
        gbc.gridx = 1;
        add(submit, gbc);
        submit.addActionListener(new onButtonClick());
        textBox.addActionListener(new onButtonClick());
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    private class onButtonClick implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if((e.getSource() == submit || e.getSource() == textBox) && !textBox.getText().equals("")){
                editor.addFile(textBox.getText());
                setVisible(false);
                dispose();
            }
                
        }
    }
    
}
