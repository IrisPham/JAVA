/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Group;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Visual-Studio
 */
//interface interface_panel_user_group  extends  JPanel{
//    //có 2 cái cần s
//}

abstract class abstract_panel_user_group extends JPanel{
    protected JLabel lbIcon;
    protected JLabel lbUserName;
    protected JLabel lbUserNum;
    protected JButton btnCheck;
    protected JPanel Component;
    protected boolean isCheck;
    public abstract_panel_user_group(String userName, String userNum, JLabel lbUserAdd) {
        initComponents();
        settingPanel(userName, userNum);
    }
    private void settingPanel(String userName, String userNum) {
        lbUserName.setText(userName);
        lbUserNum.setText(userNum);
    }
    private void initComponents() {//String userNum, JLabel lbUserAdd, ArrayList<String> listAddUser
        lbIcon = new JLabel();
        lbUserName = new JLabel();
        lbUserNum = new JLabel();
        btnCheck = new JButton();
        Component = new JPanel();
        isCheck = true;

        Font font = new Font("", 1, 12);
        lbUserName.setFont(font);
        lbUserNum.setFont(font);
        lbUserName.setForeground(new Color(31, 144, 255));
        lbUserNum.setForeground(new Color(31, 144, 255));
        try {
            Image iconUser = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/Gender.png"));
            Image iconCheck = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/checkmarkFalse.png"));
            lbIcon.setIcon(new ImageIcon(iconUser));
            btnCheck.setIcon(new ImageIcon(iconCheck));
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(this, "Sys:IconGroup");
        }

        btnCheck.setContentAreaFilled(false);
        btnCheck.setBorderPainted(false);
        
        Component.setBorder(BorderFactory.createEmptyBorder());
        Component.setOpaque(false);
        setLayout(new BorderLayout());
        setBackground(new Color(39, 45, 52));
        setMaximumSize(new Dimension(300, 50));
        setOpaque(false);
        Component.setLayout(new BorderLayout());
        Component.add(lbUserName, BorderLayout.CENTER);
        Component.add(lbUserNum, BorderLayout.PAGE_END);

        add(lbIcon, BorderLayout.LINE_START);
        add(btnCheck, BorderLayout.LINE_END);
        add(Component, BorderLayout.CENTER);

    }
    abstract public void selectUser(String userName, String userNum,JLabel lbUserAdd, ArrayList<String> listAddUser);
}
