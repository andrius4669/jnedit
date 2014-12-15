package client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.FileBuffer;
/**
 *
 * @author Domas
 */
public class EditorGUI extends JFrame {
    private final JLabel fileName;
    private final JButton addFile, deleteFile;
    
    private final JList fileList;
    private final JTextArea textArea;
    private final JScrollPane sp, sp2;
    private final DefaultListModel fileListModel;
    GridBagConstraints gbc = new GridBagConstraints();
    private final Client client;
    private final EditorType type;
    public EditorGUI(Client client, EditorType type){
        super("Online text editor");
        this.client = client;
        this.type = type;
        setSize(1000, 800);
        setMinimumSize(new Dimension(600, 400));
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(3, 3, 3, 3);
        
        addFile = new JButton("Add");
        addFile.setToolTipText("Add new file");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(addFile, gbc);
        
        deleteFile = new JButton("Delete");
        deleteFile.setToolTipText("Delete selected file");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(deleteFile, gbc);
        
        fileListModel = new DefaultListModel();
        
        
        fileList = new JList(fileListModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList.setSelectedIndex(0);
        fileList.setLayoutOrientation(JList.VERTICAL);
        fileList.setFont(new Font("Arial", PLAIN, 12));
        fileList.setBorder(new EmptyBorder(5,5, 5, 5));
        sp = new JScrollPane(fileList);
        sp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        add(sp, gbc);
        
        fileName = new JLabel("...");
        fileName.setToolTipText("File name");
        fileName.setFont(new Font("Arial", PLAIN, 20));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(fileName, gbc);
        
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", PLAIN, 14));
        textArea.setMargin(new Insets(5, 5, 5, 5));
        sp2 = new JScrollPane(textArea);
        sp2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(sp2, gbc);
        
       //*.addActionListener(new Handler());
        changeComponentsSize();
        fileList.addListSelectionListener(new onSelect());
        textArea.addKeyListener(new onWriting());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                 changeComponentsSize(); 
            }
        });
        this.addWindowStateListener((WindowEvent e) -> {
            changeComponentsSize();
        });
        if(type == EditorType.EDIT){
            addFile.addActionListener(new onClick());
            deleteFile.addActionListener(new onClick());
        }else{
            addFile.setEnabled(false);
            deleteFile.setEnabled(false);
            textArea.setEditable(false);
        }
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
     
        
        if(fileListModel.getSize() == 0) 
            textArea.setText("Choose file to edit");
    
        
    }
    public String getOpenedFileName(){
        if(fileList.isSelectionEmpty())return null;
        return (String) fileListModel.getElementAt(fileList.getSelectedIndex());
    }
    private void openFile(int id){
        String name = (String) fileListModel.getElementAt(id);
        for(FileBuffer fb : client.files){
            if(fb.getName().equals(name)){
                textArea.setText(fb.buf.toString());
                fileName.setText(fb.getName());
            }
        }
    }
    private void changeComponentsSize(){
        sp.setVisible(false);
        fileName.setVisible(false);
        sp2.setVisible(false);
        addFile.setVisible(false);
        deleteFile.setVisible(false);
        sp.setPreferredSize(new Dimension(200, getHeight()-86));
        fileName.setPreferredSize(new Dimension(getWidth()-240, 22));
        addFile.setPreferredSize(new Dimension(97, 26));
        deleteFile.setPreferredSize(new Dimension(97, 26)); 
        sp2.setPreferredSize(new Dimension(getWidth()-239, getHeight()-86));
        sp.setVisible(true);
        fileName.setVisible(true);
        sp2.setVisible(true);
        addFile.setVisible(true);
        deleteFile.setVisible(true);
    }
    public void updateOpenedFile(FileBuffer fb){

        textArea.setText(fb.buf.toString());
        fileName.setText(fb.getName());
        
    }
    public void updateFileList(){
        fileListModel.clear();
        for(FileBuffer fb:client.files)
            fileListModel.addElement(fb.getName());
    }
    public void addFile(String name){
        client.createFile(name);
        fileListModel.addElement(name);
    }
    public void deleteFile(int id){
        if(id == fileList.getSelectedIndex()){
            textArea.setText("Choose file to edit");
            fileName.setText("...");
        }
        String name = (String) fileListModel.get(id);
        fileListModel.remove(id);
        client.deleteFile(name);
    }
    private class onSelect implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!fileList.isSelectionEmpty())
                openFile(fileList.getSelectedIndex());
        }
        
    }
    private class onClick implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == addFile){
                new AddFileGUI(EditorGUI.this);
            }
            if(e.getSource() == deleteFile && !fileList.isSelectionEmpty()){
                new DeleteFileGUI(EditorGUI.this, fileList.getSelectedIndex());
            }
                
        }
    }
    private class onWriting implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    private class AutoUpdates implements Runnable{

        @Override
        public void run() {
            //Check for files and writings
        }
        
        
    }
    /*private class Handler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String s = "";
            if(e.getSource()==fileName)
                s=String.format("field 1: %s", e.getActionCommand());
            JOptionPane.showMessageDialog(null, s);
        }
    }*/
}
