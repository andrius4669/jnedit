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
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
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
        fileListModel.addElement("EditorGUI.java");
        fileListModel.addElement("Other.txt");
        fileListModel.addElement("Other.txt");
        fileListModel.addElement("Other.txt");
        fileListModel.addElement("Other.txt");
        fileListModel.addElement("Other.txt");
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
        
        fileName = new JLabel("EditorGUI.java");
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
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                 changeComponentsSize(); 
            }
        });
        this.addWindowStateListener((WindowEvent e) -> {
            changeComponentsSize();
        });
        addFile.addActionListener(new onClick());
        deleteFile.addActionListener(new onClick());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        textArea.setText("/*\n" +
" * To change this license header, choose License Headers in Project Properties.\n" +
" * To change this template file, choose Tools | Templates\n" +
" * and open the template in the editor.\n" +
" */\n" +
"\n" +
"package projektas;\n" +
"import java.awt.Color;\n" +
"import java.awt.Dimension;\n" +
"import java.awt.Font;\n" +
"import static java.awt.Font.PLAIN;\n" +
"import java.awt.GridBagConstraints;\n" +
"import java.awt.GridBagLayout;\n" +
"import java.awt.Insets;\n" +
"import java.awt.event.ActionEvent;\n" +
"import java.awt.event.ActionListener;\n" +
"import javax.swing.BorderFactory;\n" +
"import javax.swing.JButton;\n" +
"import javax.swing.JFrame;\n" +
"import javax.swing.JLabel;\n" +
"import javax.swing.JList;\n" +
"import javax.swing.JOptionPane;\n" +
"import javax.swing.JPasswordField;\n" +
"import javax.swing.JScrollPane;\n" +
"import javax.swing.JTextArea;\n" +
"import javax.swing.JTextField;\n" +
"import javax.swing.ListSelectionModel;\n" +
"import javax.swing.border.EmptyBorder;\n" +
"/**\n" +
" *\n" +
" * @author Domas\n" +
" */\n" +
"public class EditorGUI extends JFrame {\n" +
"        /**\n" +
"     * @param args the command line arguments\n" +
"     */\n" +
"   \n" +
"    private JLabel fileName;\n" +
"    private JButton save;\n" +
"    \n" +
"    private JList fileList;\n" +
"    private JTextArea textArea;\n" +
"    GridBagConstraints gbc = new GridBagConstraints();\n" +
"    public EditorGUI(){\n" +
"        super(\"Online text editor\");\n" +
"        setSize(1000, 800);\n" +
"        setLayout(new GridBagLayout());\n" +
"        gbc.insets = new Insets(3, 3, 3, 3);\n" +
"        fileList = new JList(new String[]{\"EditorUI.java\", \"Other.txt\", \"Other.txt\", \"Other.txt\", \"Other.txt\", \"Other.txt\", \"Other.txt\"});\n" +
"        \n" +
"        fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);\n" +
"        fileList.setLayoutOrientation(JList.VERTICAL);\n" +
"        fileList.setFont(new Font(\"Arial\", PLAIN, 12));\n" +
"        fileList.setBorder(new EmptyBorder(5,5, 5, 5));\n" +
"        \n" +
"        JScrollPane sp = new JScrollPane(fileList);\n" +
"        sp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));\n" +
"        sp.setPreferredSize(new Dimension(200, getHeight()-46));\n" +
"        gbc.gridx = 0;\n" +
"        gbc.gridy = 0;\n" +
"        gbc.gridheight = 2;\n" +
"        gbc.gridwidth = 1;\n" +
"        add(sp, gbc);\n" +
"        \n" +
"        \n" +
"        fileName = new JLabel(\"EditorUI.java\");\n" +
"        fileName.setToolTipText(\"File name\");\n" +
"        fileName.setFont(new Font(\"Arial\", PLAIN, 20));\n" +
"        fileName.setPreferredSize(new Dimension(getWidth()-228-86, 22));\n" +
"        gbc.gridx = 1;\n" +
"        gbc.gridy = 0;");
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
    public void addFile(String name){
        fileListModel.addElement(name);
    }
    public void deleteFile(int id){
        fileListModel.remove(id);
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
    /*private class Handler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String s = "";
            if(e.getSource()==fileName)
                s=String.format("field 1: %s", e.getActionCommand());
            JOptionPane.showMessageDialog(null, s);
        }
    }*/
}
