/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Group;

import chat.Get.Image.chatImage;
import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author Visual-Studio
 */
//For Client
//    Tạo nhóm :  				group:createGroup:
//    Xóa nhóm :  				group:deletegrGroup:
//    Load nhóm : 				group:loadGroup:
//    Nhận thông báo mời nhóm :                 group:notifiGroup:
//    Gửi tin nhắn nhóm :                       group:chatGroup:sms:
//    Load tin nhắn nhóm off :                  group:chatGroup:sms:loadSms:
//    Gửi icon cảm xúc :                        group:chatGroup:emoij:
//    Gửi Sicker : 				group:chatGroup:sticker:
//    Gủi hình ảnh : 				group:chatGroup:picture:
public class handGroupClient {

    public static void deletegrGroup(String[] data, HashMap handGroup, JPanel panelSmsGroupList, PrintWriter sentServer_, String userPhoneNum) {
        switch (data[2]) {
            case "OK":
                panelNameGroup addUserNum = (panelNameGroup) handGroup.get(data[3].trim());
                JOptionPane.showConfirmDialog(addUserNum, "Đã xóa thành công!", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                panelSmsGroupList.removeAll();
                panelSmsGroupList.updateUI();
                sentServer_.println("group:loadGroup:" + userPhoneNum);
                sentServer_.flush();
                break;
            case "NO":
                panelNameGroup addUserNum2 = (panelNameGroup) handGroup.get(data[3].trim());
                JOptionPane.showConfirmDialog(addUserNum2, "Xóa nhóm " + data[3] + " thất bại hãy thử lại!" + data[4], "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                break;
            case "reLoadListGroup":
                panelSmsGroupList.removeAll();
                panelSmsGroupList.updateUI();
                sentServer_.println("group:loadGroup:" + data[3]);
                sentServer_.flush();
                //"group:chatGroup:sms:Đã rời nhóm:"+allClientOnServer.get(Integer.parseInt(tamp[i]))
                break;
            default:
                break;
        }
    }

    public static void loadGroup(String[] data, ArrayList userNumMemGroup, JLabel txtStatutHoTen, JButton btnGroupInfo, JPanel panelInfoGroupLog, HashMap allClientOnServer, HashMap handGroup, JPanel panelSmsGroupList, PrintWriter sentServer_, String phoneNumberOfUser_,JPanel panelLogChat) {
        if (data.length == 3) {
            String nameGroup = data[2].trim();
            //Gọi đối tượng panel add zô nhóm!
            //Truyền đối tượng txtStatutHoTen
            panelNameGroup creatGroup = new panelNameGroup(userNumMemGroup, txtStatutHoTen, btnGroupInfo, nameGroup, panelInfoGroupLog, allClientOnServer, sentServer_, phoneNumberOfUser_,panelLogChat);
            handGroup.put(nameGroup.trim(), creatGroup);
            panelSmsGroupList.add(creatGroup);
            panelSmsGroupList.updateUI();
        } else {
            String[] tamp;
            String tamp2 = data[3];
            String tamp3 = tamp2.substring(1, tamp2.length() - 1);
            tamp = tamp3.split(",");
            panelNameGroup addUserNum = (panelNameGroup) handGroup.get(data[2].trim());
            for (int i = 0; i < tamp.length; i++) {
                addUserNum.changeMemberUserNum(tamp[i].trim());
            }
        }
    }

    public static void sms(String[] data, JLabel txtStatutHoTen, JPanel panelLogChat, HashMap allClientOnServer, JScrollPane scrollPaneLogChat) {
        //group:chatGroup:sms:01639997154:[1639997155,1639997156,1639997154]:Pla Pla Pla:Phân Tihcs
        //group:chatGroup:sms:01639997155:Pla Pla Pla:Phân Tihcs
        if (data[5].trim().equals(txtStatutHoTen.getText())) {
            String text = data[4];
            //ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Receive.png"));
            chatImage cm = new chatImage();
            chatImage cm2 = new chatImage();
            Image test = cm.getImage("project_chat_server/image/icon_Chat_Receive.png");
            int size = text.length();
            Image test2 = test.getScaledInstance((int) ((size + 15) * (6.6)), 60, Image.SCALE_SMOOTH);
            //Phần sms text
            JLabel lb = new JLabel();
            lb.setIcon(new ImageIcon(test2));
            lb.setText(text);
            lb.setForeground(Color.BLUE);
            Font f = new Font(text, 2, 14);
            lb.setFont(f);
            lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
            JPanel panel1 = new JPanel(new BorderLayout());
            JPanel panel2 = new JPanel(new BorderLayout());
            panel1.setOpaque(false);
            panel2.setOpaque(false);
            panel1.add(panel2, BorderLayout.LINE_START);
            panel2.add(lb, BorderLayout.LINE_END);
            //Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
            Image test3 = cm.getImage("project_chat_server/image/user_icon.png");
            JLabel hinh = new JLabel();
            hinh.setIcon(new ImageIcon(test3));
            hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
            String userName = (String) allClientOnServer.get(Integer.parseInt(data[3].trim()));
            JLabel lb2 = new JLabel(userName);
            lb2.setForeground(Color.BLUE);
            panel2.add(hinh, BorderLayout.LINE_START);
            panel2.add(lb2, BorderLayout.PAGE_START);
            panelLogChat.add(panel1);
            panelLogChat.updateUI();
            JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
            // If we want to scroll to the top set this value to the minimum, else to the maximum
            //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
            AdjustmentListener scroller = new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    Adjustable adjustable = e.getAdjustable();
                    adjustable.setValue(verticalBar.getMaximum());
//                    panelEmoji1.updateUI();
                    // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                    verticalBar.removeAdjustmentListener(this);
                }
            };
            scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                scrollPaneLogChat.repaint();
            });
            verticalBar.addAdjustmentListener(scroller);
        }
    }

    public static void loadSms(String[] data, String sdt, JPanel panelLogChat, HashMap allClientOnServer, JScrollPane scrollPaneLogChat) {
        chatImage cm = new chatImage();
        JLabel lb= new JLabel();
        JLabel hinh = new JLabel();
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);
        panel2.setOpaque(false);
        //có 2 dang load tin nhắn offline
        //Ngừi nhận
        //Người gửu
        //1639997155	1639997154	ádasdasd	42
        //"group:chatGroup:sms:loadSms:"+rss.getString(1)+":"+rss.getString(2)+":"+rss.getString(3)
        if (("0" + data[4].trim()).equals(sdt)) {
            //Tin nhắn gửi
            try{
            String textchat = data[6];
            Image test = cm.getImage("project_chat_server/image/icon_Chat_Sent.png");
            int size = textchat.length();
            Image test2 = test.getScaledInstance((int) ((size + 10) * (6.6)), 80, Image.SCALE_SMOOTH);
            //Phần sms text
            lb.setIcon(new ImageIcon(test2));
            lb.setText(textchat);
            lb.setForeground(Color.WHITE);
            Font f = new Font(textchat, 2, 14);
            lb.setFont(f);
            lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
            panel1.add(panel2, BorderLayout.LINE_END);
            panel2.add(lb, BorderLayout.LINE_START);
            Image test3 = cm.getImage("project_chat_server/image/user_icon.png");
            hinh.setIcon(new ImageIcon(test3));
            hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
            panel2.add(hinh, BorderLayout.LINE_END);
            panelLogChat.add(panel1);
            panelLogChat.updateUI();
            JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
            // If we want to scroll to the top set this value to the minimum, else to the maximum
            //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
            AdjustmentListener scroller = new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    Adjustable adjustable = e.getAdjustable();
                    adjustable.setValue(verticalBar.getMaximum());
                    //panelEmoji1.updateUI();
                    // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                    verticalBar.removeAdjustmentListener(this);
                }
            };
            scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                scrollPaneLogChat.repaint();
            });
            verticalBar.addAdjustmentListener(scroller);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            return;
        } else {
            //Tin nhắn nhận
            try{
                String text = data[6];
                Image test = cm.getImage("project_chat_server/image/icon_Chat_Receive.png");
                int size = text.length();
                Image test2 = test.getScaledInstance((int) ((size + 15) * (6.6)), 60, Image.SCALE_SMOOTH);
                //Phần sms text
                lb.setIcon(new ImageIcon(test2));
                lb.setText(text);
                lb.setForeground(Color.BLUE);
                Font f = new Font(text, 2, 14);
                lb.setFont(f);
                lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                panel1.add(panel2, BorderLayout.LINE_START);
                panel2.add(lb, BorderLayout.LINE_END);
                Image test3 = cm.getImage("project_chat_server/image/user_icon.png");
                hinh.setIcon(new ImageIcon(test3));
                hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                String userName = (String) allClientOnServer.get(Integer.parseInt(data[4].trim()));
                JLabel lb2 = new JLabel(userName);
                lb2.setForeground(Color.BLUE);
                panel2.add(hinh, BorderLayout.LINE_START);
                panel2.add(lb2, BorderLayout.PAGE_START);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
            // If we want to scroll to the top set this value to the minimum, else to the maximum
            //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
            AdjustmentListener scroller = new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    Adjustable adjustable = e.getAdjustable();
                    adjustable.setValue(verticalBar.getMaximum());
                    //panelEmoji1.updateUI();
                    // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                    verticalBar.removeAdjustmentListener(this);
                }
            };
            scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                scrollPaneLogChat.repaint();
            });
            verticalBar.addAdjustmentListener(scroller);
            return;
            }catch(Exception e){
                System.out.println(e.getMessage());    
            }
        }
    }
}
