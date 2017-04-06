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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author Visual-Studio
 */
//Phần class xử lí pannelGroup
public class panelNameGroup extends JPanel {

    private Image btnUserImage;
    private Image btnClearImage;
    private JPanel backgroud;
    private JButton btnUser;
    private JButton btnClear;
    private JLabel lbNameGroup;
    //Hai biến này dùng để cập nhật từ bên ngoài
    public JLabel lbTextChatGroup;
    public ArrayList<String> memberUserNum = new ArrayList<>();
    private JButton btnGroupInfo;
    private PrintWriter sentServer_;
    private String nameGroup_;
    private String phoneNumberOfUser_;
    public panelNameGroup(ArrayList userNumMemGroup,JLabel txtStatutHoTen,JButton btnGroupInfo, String nameGroup, JPanel panelInfoGroupLog,HashMap allClientOnServer,PrintWriter sentServer_,String phoneNumberOfUser_) {
        try {
            btnUserImage = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/ContactGroup.png"));
            btnClearImage = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/Cancel.png"));
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(backgroud, "Lỗi khi load icon Group");
        }
        this.sentServer_ = sentServer_;
        this.nameGroup_ = nameGroup;
        this.phoneNumberOfUser_ = phoneNumberOfUser_;
        initComponents();
        lbNameGroup.setText(nameGroup);
        this.btnGroupInfo = btnGroupInfo;
        backgroud.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                txtStatutHoTen.setText(nameGroup);
                userNumMemGroup.clear();
                btnGroupInfo.setVisible(true);
                panelInfoGroupLog.removeAll();
                panelInfoGroupLog.updateUI();
                //cắt chuỗi ra
                String[] tamp;
                String tamp2 = memberUserNum.toString();
                String tamp3 = tamp2.substring(1, tamp2.length() - 1);
                tamp = tamp3.split(",");
                for (int i = 0; i < tamp.length; i++) {
                    userNumMemGroup.add(tamp[i].trim());
                    Object userName = allClientOnServer.get(Integer.parseInt(tamp[i].trim()));
                    panelListGroupMember pn = new panelListGroupMember(userName.toString(),tamp[i], lbNameGroup);
                    panelInfoGroupLog.add(pn);
                    panelInfoGroupLog.updateUI();
                }
                //group:chatGroup:sms:loadSms:tên người sử dụng + tên nhóm
                sentServer_.println("group:chatGroup:sms:loadSms:"+phoneNumberOfUser_+":"+nameGroup.trim());
                sentServer_.flush();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
       
    }

    public void updateTextChat(String textChat) {
        Font lbTextChat = new Font("", 3, 16);
        lbTextChatGroup.setForeground(Color.WHITE);
        lbTextChatGroup.setFont(lbTextChat);
        lbTextChatGroup.setText(textChat);
    }

    public void changeMemberUserNum(String userNum) {
        memberUserNum.add(userNum);
    }
    private void initComponents() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(350, 75));
        
        btnGroupInfo = new JButton();
        
        lbTextChatGroup = new JLabel();
        lbNameGroup = new JLabel();
        Font lbListUserName = new Font("", 3, 18);
        lbNameGroup.setForeground(new Color(30, 144, 255));
        lbNameGroup.setFont(lbListUserName);
        btnUser = new JButton();
        btnUser.setBorderPainted(false);
        btnUser.setContentAreaFilled(false);
        btnUser.setIcon(new ImageIcon(btnUserImage));

        backgroud = new JPanel();
        backgroud.setOpaque(false);
        backgroud.setLayout(new BorderLayout());

        btnClear = new JButton();
        btnClear.setContentAreaFilled(false);
        btnClear.setBorderPainted(false);
        btnClear.setIcon(new ImageIcon(btnClearImage));

        JSeparator Line = new JSeparator();
        Line.setOrientation(JSeparator.HORIZONTAL);
        Line.setBackground(new Color(62, 67, 74));

        backgroud.add(lbNameGroup, BorderLayout.CENTER);
        backgroud.add(lbTextChatGroup, BorderLayout.PAGE_END);

        add(btnUser, BorderLayout.LINE_START);
        add(backgroud, BorderLayout.CENTER);
        add(btnClear, BorderLayout.LINE_END);
        add(Line, BorderLayout.PAGE_START);
        
                //Rời nhóm đồng nghĩa xóa nhóm
        btnClear.addActionListener((ActionEvent e) -> {
        //- Gửi yêu cầu xóa nhóm gồm tên nhóm và số điện thoại hiện thời
           int type =  JOptionPane.showConfirmDialog(this,"Bạn muốn rời khỏi nhóm "+nameGroup_,"Thông báo",JOptionPane.YES_NO_OPTION);
           if(type == JOptionPane.YES_OPTION){
                sentServer_.println("group:deletegrGroup:"+nameGroup_+":"+phoneNumberOfUser_+":"+memberUserNum);
                sentServer_.flush();
           }
            
        });
    }
}

