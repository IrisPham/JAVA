/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Group;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Visual-Studio
 */
public class panelUserNumAddGroup extends abstract_panel_user_group {
    private JLabel lbUserName;
    private JLabel lbUserNum;
    private JButton btnCheck;
    private JPanel Component;
    private boolean isCheck;
    public panelUserNumAddGroup(String userName, String userNum, JLabel lbUserAdd, ArrayList<String> listAddUser) {
        super(userName, userNum, lbUserAdd);
        lbIcon = super.lbIcon;
        lbUserName = super.lbUserName;
        lbUserNum = super.lbUserNum;
        btnCheck = super.btnCheck;
        Component = super.Component;
        isCheck = super.isCheck;
        selectUser(userName, userNum, lbUserAdd, listAddUser);
    }

    @Override
    public void selectUser(String userName, String userNum, JLabel lbUserAdd, ArrayList<String> listAddUser) {
        btnCheck.addActionListener((ActionEvent e) -> {
            if (isCheck) {
                isCheck = false;
                Image iconCheck;
                try {
                    iconCheck = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/checkmarkTrue.png"));
                    btnCheck.setIcon(new ImageIcon(iconCheck));
                } catch (IOException ex) {
                    JOptionPane.showConfirmDialog(Component, "Sys:IconGroup");
                }
                lbUserName.setForeground(new Color(30, 144, 255));
                lbUserNum.setForeground(new Color(30, 144, 255));
                listAddUser.add(userNum);
                lbUserAdd.setText(listAddUser.toString());
            } else {
                isCheck = true;
                Image iconCheck;
                try {
                    iconCheck = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/checkmarkFalse.png"));
                    btnCheck.setIcon(new ImageIcon(iconCheck));
                } catch (IOException ex) {
                    JOptionPane.showConfirmDialog(Component, "Sys:IconGroup");
                }
                lbUserName.setForeground(new Color(31, 144, 255));
                lbUserNum.setForeground(new Color(31, 144, 255));
                listAddUser.remove(userNum);
                lbUserAdd.setText(listAddUser.toString());
            }
        });
        
        Component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isCheck) {
                    isCheck = false;
                    Image iconCheck;
                    try {
                        iconCheck = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/checkmarkTrue.png"));
                        btnCheck.setIcon(new ImageIcon(iconCheck));
                    } catch (IOException ex) {
                        JOptionPane.showConfirmDialog(Component, "Sys:IconGroup");
                    }
                    lbUserName.setForeground(new Color(30, 144, 255));
                    lbUserNum.setForeground(new Color(30, 144, 255));
                    listAddUser.add(userNum);
                    lbUserAdd.setText(listAddUser.toString());
                } else {
                    isCheck = true;
                    Image iconCheck;
                    try {
                        iconCheck = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/checkmarkFalse.png"));
                        btnCheck.setIcon(new ImageIcon(iconCheck));
                    } catch (IOException ex) {
                        JOptionPane.showConfirmDialog(Component, "Sys:IconGroup");
                    }
                    lbUserName.setForeground(new Color(31, 144, 255));
                    lbUserNum.setForeground(new Color(31, 144, 255));
                    listAddUser.remove(userNum);
                    lbUserAdd.setText(listAddUser.toString());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
}
