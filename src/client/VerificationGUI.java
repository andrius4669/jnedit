/*
 * Copyright (c) 2014, Domas
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Domas
 */
public class VerificationGUI extends JFrame{
    JLabel title;
    JTextField username;
    JPasswordField password;
    JButton submit, guestAccess;
    GridBagConstraints gbc = new GridBagConstraints();
    public VerificationGUI(){
        super("Online text editor");
        setSize(265, 150);
        setResizable(false);
        setLayout(new GridBagLayout());
        
        Toolkit tk = Toolkit.getDefaultToolkit();
       Dimension screenSize = tk.getScreenSize();
       setLocation(screenSize.width/2-150, screenSize.height/2-50);
        
       Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
       setIconImage(icon);
        
        title = new JLabel("Administrator mode");
        
        username = new JTextField();
        username.setPreferredSize(new Dimension(246, 22));
        
        password = new JPasswordField();
        password.setPreferredSize(new Dimension(246, 22));
        
        submit = new JButton("Sign in");
        submit.setPreferredSize(new Dimension(120, 30));
        
        guestAccess = new JButton("Guest mode");
        guestAccess.setPreferredSize(new Dimension(120, 30));
        
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridy++;
        add(title, gbc);
        gbc.gridy++;
        add(username, gbc);
        gbc.gridy++;
        add(password, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(guestAccess, gbc);
        gbc.gridx = 1;
        add(submit, gbc);
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
}
