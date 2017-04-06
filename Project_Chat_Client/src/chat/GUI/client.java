/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.GUI;

import chat.Group.*;
import chat.client.*;
//import static chat.client.panelNameGroup.lbTextChatGroup;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
//import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Visual-Studio
 */
public final class client extends javax.swing.JFrame implements chat_client_interface {

    Socket sock_;
    BufferedReader fromServer_;
    PrintWriter sentServer_;

    //Biến lưu trữ infomation user
    String nameInfo_;
    String sdt;
    String mk;

    //Biến lưu trữ id để xử lí các btn và logchat
    private int idfriend;

    //Biến lưu trữ id để xử lí SMS chat và Danh bạ
    ArrayList<String> userNameFriend;
    ArrayList<Integer> userNumFriend;
    ArrayList<String> userPassFriend;
    HashMap<Integer, Integer> checkListFriends;
    ArrayList<String> LoadListChatFriends;
    ArrayList<Integer> LoadListChatFriends2;
    ArrayList<String> LoadListChatFriends3;

    //Biến lưu trữ số điện thoại và tên của all người dùng hệ thống
    HashMap<Integer, String> allClientOnServer;

    //Phần biến dành cho Group
    HashMap<String, panelNameGroup> handGroup;
    //Mảng lưu trữ các thành viên hiện tại là bạn bè dùng để tạo nhóm
    ArrayList<String> listAddUser;
    //Mảng lưu trữ các thành viên có trong 1 nhóm
    //Dùng để gửi chat
    ArrayList<String> userNumMemGroup;
    //Kết thúc phần biến dành cho nhóm
    //Phần biến dùng để xử lí file
    private boolean isCheckFile = false;

    public client(Socket sock_, BufferedReader fromServer_, PrintWriter sentServer, String sdt, String mk) {
        this.sock_ = sock_;
        this.fromServer_ = fromServer_;
        this.sentServer_ = sentServer;
        this.sdt = sdt;
        this.mk = mk;

        idfriend = 0;
        userNameFriend = new ArrayList<>();
        userNumFriend = new ArrayList<>();
        userPassFriend = new ArrayList<>();
        checkListFriends = new HashMap<>();
        LoadListChatFriends = new ArrayList<>();
        LoadListChatFriends2 = new ArrayList<>();
        LoadListChatFriends3 = new ArrayList<>();

        allClientOnServer = new HashMap<>();

        handGroup = new HashMap<>();
        listAddUser = new ArrayList<>();
        userNumMemGroup = new ArrayList<>();
        initComponents();
        initComponent2();
    }

    private void initComponent2() {
        //Cho Frame ra giữa màn hình
        setLocationRelativeTo(this);
        /*##########################################################*/
        //Thiet lap giao dien chat
        scrollPaneLogChat.getViewport().setOpaque(false);
        scrollPaneSMS.getViewport().setOpaque(false);
        scrollPaneDanhBa.getViewport().setOpaque(false);
        scrollPaneGroup.getViewport().setOpaque(false);
        scrollPanelCreateGroup.getViewport().setOpaque(false);

        //Ẩn giao diện chatBox
        panelChatIcon.setVisible(false);
        //Ẩn giao diện Chat, chat Group, Thêm, hiện danh bạ,Emoji
        pannelDanhBaChinh.setVisible(true);
        panelTinNhan.setVisible(false);
        panelThemTableTabTool.setVisible(false);
        panelSmsGroup.setVisible(false);
        PanelNoTiFiTool.setVisible(false);
        panelEmoji1.setVisible(false);
        isSitker = false;
        setSize(1170 - 355, 720);
        scrollPanelSticker.setVisible(false);
        //ẩn nút btnGroupInfo
        btnGroupInfo.setVisible(false);
        //Load Sticker 3 cột vào 20 dòng gồm 30 sticker
        loadSticker();
        //Load Emoji 6 dòng và 6 cột gồm 27 Emoji
        loadEmoji();
        /*##########################################################*/
        langNgheServer();
    }

    public void langNgheServer() {
        try {
            Thread xuliClient = new Thread(new xuLiClient(sock_, fromServer_));
            xuliClient.start();
        } catch (Exception e) {
            // jTAlog.append("Lỗi! Khong the tao Luong cho client! P.Thuc langNgheServer!\n");
        }

    }

    //Class tao luong nhan tu Server
    public class xuLiClient implements Runnable {

        Socket sockett;
        BufferedReader fromServerr;
        int i = 0;

        public xuLiClient(Socket a, BufferedReader b) {
            sockett = a;
            fromServerr = b;
        }

        @Override
        public void run() {
            try {
                String me;
                String[] data;
                while ((me = fromServerr.readLine()) != null) {
                    data = me.split(":");
                    switch (data[0]) {
                        case "info":
                            information(data[1]);
                            break;
                        case "Chat":
                            chatOnLy(data);
                            break;
                        case "ChatGroup":
                            chatGroup(data);
                            break;
                        case "ChatLoad":
                            if (data[3] != null) {
                                if (data.length == 4) {
                                    //Gửi tin nhắn sms
                                    getLoadChat(data[1], data[2], data[3]);
                                } else {
                                    //Load Sticker or Emoji
                                    if (data[3].equals("@@@@")) {
                                        //Load Sticker
                                        loadStickerOnChat("0" + data[1], Integer.parseInt(data[4]));
                                    } else {
                                        //Load Emoji
                                        loadEmojiOnChat("0" + data[1], Integer.parseInt(data[4]));
                                    }
                                }
                            }
                            break;
                        case "Friend":
                            if (data[1].equals("updateDanhBa")) {
                                panelDanhBa2.removeAll();
                                panelDanhBa2.updateUI();
                            } else {
                                getListFriend(data[1], Integer.parseInt(data[2]), data[3]);
                            }
                            break;
                        case "repDeleteFriends":
                            repDeleteFriends(data[1]);
                            break;
                        case "allUser":
                            allUser(data[1]);
                            break;
                        case "AddFriend":
                            try {
                                Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/notifi2.png"));
                                jButton15.setIcon(new ImageIcon(im));
                                jButton15.setForeground(new Color(30, 144, 255));
                            } catch (IOException e) {
                            }
                            //AddFriend:"+ix+":"+iy+":0
                            //01639997155:01639997154:0
                            Integer userNum = Integer.parseInt(data[2]);
                            String userName = allClientOnServer.get(Integer.parseInt(data[2]));
                            if (Integer.parseInt(data[3]) == 0) {
                                System.out.println(userName);
                                //Load dữ liệu lên panenl noti
                                try {
                                    Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                                    Image im2 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/AddFriends.png"));
                                    //Icon user
                                    JButton a = new JButton();
                                    a.setContentAreaFilled(false);
                                    a.setIcon((Icon) new ImageIcon(im));
                                    a.setBackground(new Color(245, 246, 248));
                                    //Tên người dùng
                                    JLabel t = new JLabel(userName);
                                    t.setForeground(new Color(30, 144, 255));
                                    Font f = new Font(userName, 2, 14);
                                    t.setFont(f);
                                    //Đồng ý kết bạn!
                                    JButton b = new JButton();
                                    b.setContentAreaFilled(false);
                                    b.setIcon((Icon) new ImageIcon(im2));
                                    b.setBackground(new Color(245, 246, 248));
                                    b.addActionListener((ActionEvent e) -> {
                                        int type = JOptionPane.showConfirmDialog(panelChatIcon, "Bạn đồng ý kết bạn với " + userName + "!", "Thông báo!", JOptionPane.YES_NO_OPTION);
                                        if (type == JOptionPane.YES_OPTION) {
                                            //Xác nhận yêu cầu kết bạn - Cập nhật lại là 1
                                            sentServer_.println("AddFriend:" + sdt + ":" + userNum + ":1");
                                            sentServer_.flush();
                                            //Xóa toàn bộ bạn trong danh bạ
                                            panelDanhBa2.removeAll();
                                            //Gửi yêu cầu cập nhật lại danh bạ sau khi thêm bạn
                                            sentServer_.println("Friend:" + sdt);
                                            sentServer_.flush();
                                            //Xóa toàn bộ danh sách chờ kết bạn
                                            panelNotifi.removeAll();
                                            //Yêu cầu gửi về danh sách lời mời chưa chấp nhận
                                            sentServer_.println("loadAddFriends:" + sdt);
                                            sentServer_.flush();
                                            //Hiện lên danh bạ và ẩn đi chat,cha group, tool thêm
                                            panelChatIcon.setVisible(true);
                                            pannelDanhBaChinh.setVisible(true);
                                            panelTinNhan.setVisible(false);
                                            panelThemTableTabTool.setVisible(false);
                                            panelSmsGroup.setVisible(false);
                                        }
                                    });
                                    //Panel quản lí 3 đối tượng trên    
                                    JPanel panel = new JPanel(new BorderLayout());
                                    panel.setMaximumSize(new Dimension(700, 75));
                                    panel.setBackground(new Color(245, 246, 248));
                                    panel.add(a, BorderLayout.LINE_START);
                                    panel.add(b, BorderLayout.LINE_END);
                                    panel.add(t, BorderLayout.CENTER);
                                    panelNotifi.add(panel);
                                    panelNotifi.updateUI();
                                    JScrollBar verticalBar = scrollPaneSMS.getVerticalScrollBar();
                                    // If we want to scroll to the top set this value to the minimum, else to the maximum
                                    //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                                    AdjustmentListener scroller = new AdjustmentListener() {
                                        @Override
                                        public void adjustmentValueChanged(AdjustmentEvent e) {
                                            Adjustable adjustable = e.getAdjustable();
                                            adjustable.setValue(verticalBar.getMaximum());
                                            // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                                            verticalBar.removeAdjustmentListener(this);
                                        }
                                    };
                                    verticalBar.addAdjustmentListener(scroller);
                                } catch (IOException e) {
                                }
                            } else {

                            }

                            break;
                        case "loadAddFriends":
                            try {
                                Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/notifi2.png"));
                                jButton15.setIcon(new ImageIcon(im));
                                jButton15.setForeground(new Color(30, 144, 255));
                            } catch (IOException e) {
                            }
                            break;
                        case "addGroupName":
                            addGroupName(data[1]);
                            break;
                        case "ListCreateGroup":
                            ListCreateGroup(data[1], data[2], data[2]);
                            break;
                        case "group":
                            handingGroup(data);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi nhận tin tin từ Server " + e.getMessage());
            }
        }
    }

    //Tất cả các phương thức dùng để xử lí Chat - Group - friends
    //Phần phương thức dùng để xử lí nhận chat, sicker emoji đơn và nhóm//
    @Override
    public void chatOnLy(String[] data) {
        //Client    :1639997156 Sent to: 1639997154
        if (data[1].equals(String.valueOf(idfriend))) {
            if (data[3] != null) {
                if (data.length == 4) {
                    //Gửi tin nhắn sms
                    getLoadChat(data[1], data[2], data[3]);
                } else {
                    //Load Sticker or Emoji
                    if (data[3].equals("@@@@")) {
                        //Load Sticker
                        loadStickerOnChat("0" + data[1], Integer.parseInt(data[4]));
                    } else {
                        //Load Emoji
                        loadEmojiOnChat("0" + data[1], Integer.parseInt(data[4]));
                    }
                }
            }
        }
    }

    @Override
    public void chatGroup(String[] data) {
        //sentServer_.println("ChatGroup:" + sdt + ":" + userNumMemGroup + ":" + jTextFieldchat.getText() + ":" + txtStatutHoTen.getText().trim());
        ////ChatGroup:số điện thoại người gửi:tin nhắn:tên nhóm
        //Để load chat nhóm
        /*Có 2 điều kiện là phải có tên nhóm*/
        String nameSent = allClientOnServer.get(Integer.parseInt(data[1]));
        if (data[3].equals(String.valueOf(txtStatutHoTen.getText().trim()))) {
            if (data[2] != null) {
                if (data.length == 4) {
                    //Gửi tin nhắn sms
                    //String userSent, String userRe, String textChat
                    String text = data[2];
                    try {
                        Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Receive.png"));
                        //                        int size = text.length();
                        Image test2 = test.getScaledInstance((int) ((/*size */15 + 15) * (6.6)), 60, Image.SCALE_SMOOTH);
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
                        Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                        JLabel hinh = new JLabel();
                        hinh.setIcon(new ImageIcon(test3));
                        hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                        JLabel lb2 = new JLabel(nameSent);
                        lb2.setForeground(Color.BLUE);
                        panel2.add(hinh, BorderLayout.LINE_START);
                        panel2.add(lb2, BorderLayout.PAGE_START);
                        panelLogChat.add(panel1);
                        panelLogChat.updateUI();
                        scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                            scrollPaneLogChat.repaint();
                        });
                        JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                        // If we want to scroll to the top set this value to the minimum, else to the maximum
                        //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                        AdjustmentListener scroller = new AdjustmentListener() {
                            @Override
                            public void adjustmentValueChanged(AdjustmentEvent e) {
                                Adjustable adjustable = e.getAdjustable();
                                adjustable.setValue(verticalBar.getMaximum());
                                panelEmoji1.updateUI();
                                // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                                verticalBar.removeAdjustmentListener(this);
                            }
                        };
                        verticalBar.addAdjustmentListener(scroller);
//            AdjustmentListener scroller2 = (AdjustmentEvent e) -> {
//                int type1 = e.getAdjustmentType();
//                   switch (type1) {
//                       case AdjustmentEvent.UNIT_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.UNIT_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.BLOCK_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.BLOCK_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.TRACK:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                   }
//               };
//            verticalBar.addAdjustmentListener(scroller2);
                    } catch (IOException e) {
                        System.out.println("Lỗi chỗ nà 222" + e.getMessage());
                    } catch (NullPointerException e) {
                        System.out.println("Lỗi chỗ này" + e.getMessage());
                    }
                } else {
//                    //Load Sticker or Emoji
//                    if (data[3].equals("@@@@")) {
//                        //Load Sticker
//                        loadStickerOnChat("0" + data[1], Integer.parseInt(data[4]));
//                    } else {
//                        //Load Emoji
//                        loadEmojiOnChat("0" + data[1], Integer.parseInt(data[4]));
//                    }
                }
            }
        }
    }

    @Override
    public void getLoadChat(String userSent, String userRe, String textChat) {
        //ChatLoad:1639997155:1639997154:Tao là Giang nè Phát cave
        String sent = userSent;
        String text = textChat.trim();
        if (sdt.equals("0" + sent)) {
            try {
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Sent.png"));
                int size = text.length();
                Image test2 = test.getScaledInstance((int) ((size + 15) * (6.6)), 65, Image.SCALE_SMOOTH);
                JLabel lb = new JLabel();
                lb.setIcon(new ImageIcon(test2));
                lb.setText(text);
                lb.setForeground(Color.WHITE);
                Font f = new Font(text, 2, 14);
                lb.setFont(f);
                lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                Image image3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                JLabel hinh = new JLabel();
                hinh.setIcon(new ImageIcon(image3));
                hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                JPanel panel1 = new JPanel(new BorderLayout());
                JPanel panel2 = new JPanel(new BorderLayout());
                panel1.setOpaque(false);
                panel2.setOpaque(false);
                panel1.add(panel2, BorderLayout.LINE_END);
                panel2.add(lb, BorderLayout.CENTER);
                panel2.add(hinh, BorderLayout.LINE_END);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());
                        panelEmoji1.updateUI();
                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
//            AdjustmentListener scroller2 = (AdjustmentEvent e) -> {
//                int type1 = e.getAdjustmentType();
//                   switch (type1) {
//                       case AdjustmentEvent.UNIT_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           break;
//                       case AdjustmentEvent.UNIT_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           break;
//                       case AdjustmentEvent.BLOCK_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           break;
//                       case AdjustmentEvent.BLOCK_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           break;
//                       case AdjustmentEvent.TRACK:
//                           scrollPaneLogChat.updateUI();
//                           break;
//                   }
//               };
//            verticalBar.addAdjustmentListener(scroller2);
                jTextFieldchat.setText("");
                jTextFieldchat.requestFocus();
            } catch (IOException | NullPointerException e) {
                System.out.println("Lỗi chỗ này" + e.getMessage());
            }
        } else if (sdt.equals("0" + userRe)) {
            try {
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Receive.png"));
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
                Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                JLabel hinh = new JLabel();
                hinh.setIcon(new ImageIcon(test3));
                hinh.setHorizontalTextPosition((int) CENTER_ALIGNMENT);
                panel2.add(hinh, BorderLayout.LINE_START);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());
                        panelEmoji1.updateUI();
                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
//            AdjustmentListener scroller2 = (AdjustmentEvent e) -> {
//                int type1 = e.getAdjustmentType();
//                   switch (type1) {
//                       case AdjustmentEvent.UNIT_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.UNIT_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.BLOCK_INCREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.BLOCK_DECREMENT:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                       case AdjustmentEvent.TRACK:
//                           scrollPaneLogChat.updateUI();
//                           panelLogChat.validate();
//                           break;
//                   }
//               };
//            verticalBar.addAdjustmentListener(scroller2);
            } catch (IOException e) {
                System.out.println("Lỗi chỗ nà 222" + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Lỗi chỗ này" + e.getMessage());
            }

        }

    }
    //Phần phương thức dùng để xử lí gửi lời mời kết bạn

    //Phần phương thức dùng để xử lí lấy thông tin cá nhân
    @Override
    public void information(String info) {
        nameInfo_ = info;
        lbWelCome1.setText("Chào " + nameInfo_);
        this.setTitle("CTU Talks - " + nameInfo_);
    }

    @Override
    public void allUser(String allUser) {
        //{1639997154=HoaiAn, 1639997155=HoaiAn, 1639997156=HoaiAn}
        allClientOnServer.clear();
        String dataHanding = allUser;
        String cat = dataHanding.substring(1, dataHanding.length() - 1); //Loại bỏ 2 dấu {}
        String[] dataHanding2 = cat.split(","); //Loại bỏ dấu ,
        for (String repx : dataHanding2) {
            String[] dataHanding3 = repx.split("=");//Loại bỏ dấu =
            //Put bào allClientOnServer
            allClientOnServer.put(Integer.parseInt(dataHanding3[0].trim()), dataHanding3[1].trim());
        }
    }

    //Phần phương thức dùng để xử lí tạo nhóm
    private void handingGroup(String[] data){
        switch (data[1]){
            case "createGroup":
                
                break;
            case "deletegrGroup":
                handGroupClient.deletegrGroup(data, handGroup, panelSmsGroupList, sentServer_,sdt);
                break;
            case "notifiGroup":
                break;
            case "loadGroup":
                handGroupClient.loadGroup(data, userNumMemGroup, txtStatutHoTen, btnGroupInfo, panelInfoGroupLog, allClientOnServer,handGroup,panelSmsGroupList,sentServer_,sdt,panelLogChat);
                break;
            case "chatGroup":
                switch (data[2]){
                    case "sms":
                        if (!"loadSms".equals(data[3])) {
                            handGroupClient.sms(data, txtStatutHoTen, panelLogChat, allClientOnServer, scrollPaneLogChat);
                        }else{
                            handGroupClient.loadSms(data, sdt, panelLogChat, allClientOnServer, scrollPaneLogChat);
                        }
                        
                        break;
                    case "emoij":
                        break;
                    case "sticker":
                        break;
                    case "picture":
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void addGroupName(String check) {
        if (check.equals("OK")) {
            //Hiện group chat nhóm, ẩn đi panel tool
            panelThemTableTabTool.setVisible(false);
            panelTinNhan.setVisible(false);
            pannelDanhBaChinh.setVisible(false);
            panelSmsGroup.setVisible(true);
            //Xóa List Group cập nhật danh sách mới
            panelSmsGroupList.removeAll();
            panelSmsGroupList.updateUI();
            //Gửi yêu cầu loadGroupName
            sentServer_.println("loadGroupName:" + sdt);
            sentServer_.flush();

        } else {
            JOptionPane.showConfirmDialog(panelChatIcon, "Tên nhóm đã tồn tại! Thử lại", "Thông báo", JOptionPane.OK_OPTION);
            textNameChatGroup_ThemTableTabTool.setText("");
            textNameChatGroup_ThemTableTabTool.requestFocus();
        }
    }

    @Override
    public void getListFriend(String userName, Integer userNum, String userPass) {
        String Name = userName, Pass = userPass;
        Integer Num = userNum;
        userNameFriend.add(Name);
        userNumFriend.add(Num);
        userPassFriend.add(Pass);
        checkListFriends.put(Num, 1);
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
            Image im2 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/Cancel.png"));
            //Icon user
            JButton a = new JButton();
            a.setContentAreaFilled(false);
            a.setIcon((Icon) new ImageIcon(im));
            a.setBackground(new Color(245, 246, 248));
            a.addActionListener((ActionEvent e) -> {
                idfriend = Num;
                //Chỗ này kiểm tra xem thằng kia đã có bên kia chat chưa
                //Nếu chưa thì thêm nó zô
                if (checkListFriends.get(Num) == 1) {
                    //sau khi thêm thì cho nó lại bằng 0
                    LoadListChatFriends.add(Name);
                    LoadListChatFriends2.add(Num);
                    LoadListChatFriends3.add(Pass);
                    //Gọi hàm getListChat để xử lí giao diện
                    checkListFriends.put(Num, 0);
                    getListChatFriend(Name, Num, Pass);
                    panelTinNhan.setVisible(true);
                    pannelDanhBaChinh.setVisible(false);
                }
                panelTinNhan.setVisible(true);
                pannelDanhBaChinh.setVisible(false);
            });
            //Tên người dùng
            JLabel t = new JLabel(Name);
            t.setForeground(new Color(30, 144, 255));
            Font f = new Font(Name, 2, 14);
            t.setFont(f);
            //Xóa bạn bè
            JButton b = new JButton();
            b.setContentAreaFilled(false);
            b.setIcon((Icon) new ImageIcon(im2));
            b.setBackground(new Color(245, 246, 248));
            b.addActionListener((ActionEvent e) -> {
                int type = JOptionPane.showConfirmDialog(panelChatIcon, "Bạn chắc chắn xóa người này!", "Nguy Hiểm!", JOptionPane.YES_NO_OPTION);
                if (type == JOptionPane.YES_OPTION) {
                    sentServer_.println("DeleteFriends:" + Name + ":" + Num + ":" + Pass);
                    sentServer_.flush();

                    userNameFriend.remove(Name);
                    userNumFriend.remove(Num);
                    userPassFriend.remove(Pass);
                    //Xóa ra khỏi list
                    checkListFriends.remove(Num);
                    LoadListChatFriends.remove(Name);
                    LoadListChatFriends2.remove(Num);
                    LoadListChatFriends3.remove(Pass);
                    //Xóa all trong panel SMS để cập nhật
                    txtStatutHoTen.setText("");
                    panelSMS.removeAll();
                    panelLogChat.removeAll();
                    panelLogChat.updateUI();
                    int i;
                    for (i = 0; i < LoadListChatFriends2.size(); i++) {
                        getListChatFriend(LoadListChatFriends.get(i), LoadListChatFriends2.get(i), LoadListChatFriends3.get(i));
                    }
                    panelSMS.updateUI();
                    System.out.println("sdt chính chủ" + sdt);
                    System.out.println(Num);
                    sentServer_.println("Friend:" + Num);
                    sentServer_.flush();
                }
            });
            //Panel quản lí 3 đối tượng trên    
            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(700, 75));
            panel.setBackground(new Color(245, 246, 248));
            panel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    idfriend = Num;
                    idfriend = Num;
                    //Chỗ này kiểm tra xem thằng kia đã có bên kia chat chưa
                    //Nếu chưa thì thêm nó zô
                    if (checkListFriends.get(Num) == 1) {
                        //sau khi thêm thì cho nó lại bằng 0
                        LoadListChatFriends.add(Name);
                        LoadListChatFriends2.add(Num);
                        LoadListChatFriends3.add(Pass);
                        //Gọi hàm getListChat để xử lí giao diện
                        checkListFriends.put(Num, 0);
                        getListChatFriend(Name, Num, Pass);
                        panelTinNhan.setVisible(true);
                        pannelDanhBaChinh.setVisible(false);
                    }
                    panelTinNhan.setVisible(true);
                    pannelDanhBaChinh.setVisible(false);
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
            panel.add(a, BorderLayout.LINE_START);
            panel.add(b, BorderLayout.LINE_END);
            panel.add(t, BorderLayout.CENTER);
            panelDanhBa2.add(panel);
            panelDanhBa2.updateUI();
            JScrollBar verticalBar = scrollPaneSMS.getVerticalScrollBar();
            // If we want to scroll to the top set this value to the minimum, else to the maximum
            //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
            AdjustmentListener scroller = new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    Adjustable adjustable = e.getAdjustable();
                    adjustable.setValue(verticalBar.getMaximum());
                    // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                    verticalBar.removeAdjustmentListener(this);
                }
            };
            verticalBar.addAdjustmentListener(scroller);
        } catch (IOException e) {
        }
    }

    @Override
    public void repDeleteFriends(String Statut) {
        if (Statut.equals("OK")) {
            JOptionPane.showConfirmDialog(panelChatIcon, "Đã xóa bạn thành công!");
            //Cập nhật lại ListFriends
            panelDanhBa2.removeAll();
            panelDanhBa2.updateUI();
            sentServer_.println("Friend:" + sdt);
            sentServer_.flush();
        } else {
            if (Statut.equals("NO")) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Xóa bạn thất bại!");
            }
        }
    }

    @Override
    public void getListChatFriend(String userName, Integer userNum, String userPass) {
        String Name = userName, Pass = userPass;
        Integer Num = userNum;
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
            Image im2 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/Cancel.png"));
            //Icon user
            JButton a = new JButton();
            a.setContentAreaFilled(false);
            a.setIcon((Icon) new ImageIcon(im));
            a.setBackground(new Color(245, 246, 248));
            a.addActionListener((ActionEvent e) -> {
                txtStatutHoTen.setText(Name);
                idfriend = Num;
                isCheckFile = true;
                btnGroupInfo.setVisible(false);
                panelLogChat.removeAll();
                sentServer_.println("ChatLoad:" + sdt + ":" + userNum);
                sentServer_.flush();
            });
            //Tên người dùng
            JLabel t = new JLabel(Name);
            t.setForeground(new Color(30, 144, 255));
            Font f = new Font(Name, 2, 14);
            t.setFont(f);
            //Xóa Chat
            JButton b = new JButton();
            //b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setIcon((Icon) new ImageIcon(im2));
            b.setBackground(new Color(245, 246, 248));
            b.addActionListener((ActionEvent e) -> {
                int type = JOptionPane.showConfirmDialog(panelChatIcon, "Xóa chat với" + Name, "Thông báo!", JOptionPane.YES_NO_OPTION);
                if (type == JOptionPane.YES_OPTION) {
                    LoadListChatFriends.remove(Name);
                    LoadListChatFriends2.remove(Num);
                    LoadListChatFriends3.remove(Pass);
                    //Xóa all trong panel SMS để cập nhật
                    txtStatutHoTen.setText("");
                    panelSMS.removeAll();
                    panelLogChat.removeAll();
                    panelLogChat.updateUI();
                    //Cập nhật lại checkList cho lần sau sử dụng
                    checkListFriends.put(Num, 1);
                    int i;
                    for (i = 0; i < LoadListChatFriends2.size(); i++) {
                        getListChatFriend(LoadListChatFriends.get(i), LoadListChatFriends2.get(i), LoadListChatFriends3.get(i));
                    }
                    panelSMS.updateUI();
                }
            });
            //Panel quản lí 3 đối tượng trên    
            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(700, 75));
            panel.setBackground(new Color(245, 246, 248));
            panel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    txtStatutHoTen.setText(Name);
                    idfriend = Num;
                    isCheckFile = true;
                    btnGroupInfo.setVisible(false);
                    panelLogChat.removeAll();
                    sentServer_.println("ChatLoad:" + sdt + ":" + userNum);
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
            panel.add(a, BorderLayout.LINE_START);
            panel.add(b, BorderLayout.LINE_END);
            panel.add(t, BorderLayout.CENTER);
            panelSMS.add(panel);
            panelSMS.updateUI();
            JScrollBar verticalBar = scrollPaneSMS.getVerticalScrollBar();
            // If we want to scroll to the top set this value to the minimum, else to the maximum
            //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
            AdjustmentListener scroller = new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    Adjustable adjustable = e.getAdjustable();
                    adjustable.setValue(verticalBar.getMaximum());
                    // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                    verticalBar.removeAdjustmentListener(this);
                }
            };
            verticalBar.addAdjustmentListener(scroller);
        } catch (IOException e) {
        }
    }

    @Override
    public void ListCreateGroup(String userName, String userNum, String userPass) {
        //Có 2 trường hợp xảy ra
        //Có bạn
        //Không có bạn
        if (userName.equals("updateList")) {
            panelCreateGroupLog.removeAll();
            panelCreateGroupLog.updateUI();
        } else {
            //ListCreateGroup:Nguyễn Văn Lộc:1639997155:HoaiAn
            if (userName != null && userNum != null && userPass != null) {
                panelUserNumAddGroup listPanelUser = new panelUserNumAddGroup(userName, userNum, lbUserAdd, listAddUser);
                panelCreateGroupLog.add(listPanelUser);
                panelCreateGroupLog.updateUI();
            }

        }
    }

    //Hết để xử lí Chat - Group - friends
    //Toàn bộ phần xử lí Sticker
    private Timer time;
    private int checkTime = 1;

    private void loadSticker() {
        GridLayout layout = new GridLayout(12, 3, 0, 0);
        panelSticker.setLayout(layout);
        int i = 1000;
        for (i = 1000; i <= 1033; i++) {
            try {
                int numSticker = i;
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/sticker/" + i + "/1.png"));
                Image test2 = test.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                btn.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (idfriend != 0) {
                            sentServer_.println("Chat:" + sdt + ":" + idfriend + ":@@@@:" + numSticker + "");
                            sentServer_.flush();
                            loadStickerOnChat(sdt, numSticker);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        anhdong(btn, numSticker);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        time.stop();
                    }
                });
                panelSticker.add(btn);
            } catch (IOException e) {
            }
        }
    }

    private void anhdong(JButton btn, int i) {
        time = new Timer(200, (ActionEvent e) -> {
            loadAnhDong(btn, i, checkTime);
            if (checkTime == 2) {
                checkTime = 0;
            }
            checkTime++;
        });
        time.start();

    }

    private void loadAnhDong(JButton btn, int i, int checkTime) {
        try {
            Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/sticker/" + i + "/" + checkTime + ".png"));
            Image test2 = test.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setIcon(new ImageIcon(test2));
            panelSticker.updateUI();
        } catch (IOException e) {
        }
    }

    private void loadStickerOnChat(String userSent, int i) {
        if (sdt.equals(userSent)) {
            try {
                int ii = i;
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/sticker/" + i + "/1.png"));
                Image test2 = test.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                btn.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        anhdong(btn, ii);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        time.stop();
                    }
                });
                JPanel panel1 = new JPanel(new BorderLayout());
                JPanel panel2 = new JPanel(new BorderLayout());
                panel1.setOpaque(false);
                panel2.setOpaque(false);
                panel1.add(panel2, BorderLayout.LINE_END);
                panel2.add(btn, BorderLayout.LINE_END);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());

                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi tải Sticker" + e.getMessage(), "Thông báo", JOptionPane.OK_OPTION);
            }
        } else {
            try {
                int ii = i;
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/sticker/" + i + "/1.png"));
                Image test2 = test.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                btn.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        anhdong(btn, ii);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        time.stop();
                    }
                });
                JPanel panel1 = new JPanel(new BorderLayout());
                JPanel panel2 = new JPanel(new BorderLayout());
                panel1.setOpaque(false);
                panel2.setOpaque(false);
                panel1.add(panel2, BorderLayout.LINE_START);
                panel2.add(btn, BorderLayout.LINE_START);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());

                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi tải Sticker" + e.getMessage(), "Thông báo", JOptionPane.OK_OPTION);
            }
        }
    }
    //Kết thúc phần xử lí Sticker!

    //Toàn bộ phần xử lí Emoji
    private void loadEmoji() {
        GridLayout layout = new GridLayout(6, 5, 0, 0);
        panelEmoji1.setLayout(layout);
        int i = 1;
        for (i = 1; i <= 27; i++) {
            try {
                int numEmoji = i;
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/emoji/emoji-" + i + ".png"));
                Image test2 = test.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                btn.addActionListener((ActionEvent e) -> {
                    if (idfriend != 0) {
                        sentServer_.println("Chat:" + sdt + ":" + idfriend + ":@@:" + numEmoji + "");
                        sentServer_.flush();
                        panelEmoji1.repaint();
                        loadEmojiOnChat(sdt, numEmoji);
                        panelEmoji1.repaint();
                    }
                });

                panelEmoji1.add(btn);
                panelEmoji1.updateUI();
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi load Emoji", "Thông báo", JOptionPane.OK_OPTION);
            }
        }
    }

    private void loadEmojiOnChat(String userSent, int i) {
        if (sdt.equals(userSent)) {
            try {
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/emoji/emoji-" + i + ".png"));
                Image test2 = test.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                JPanel panel1 = new JPanel(new BorderLayout());
                JPanel panel2 = new JPanel(new BorderLayout());
                panel1.setOpaque(false);
                panel2.setOpaque(false);
                panel1.add(panel2, BorderLayout.LINE_END);
                panel2.add(btn, BorderLayout.LINE_END);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());
                        panelEmoji1.updateUI();
                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi tải Sticker" + e.getMessage(), "Thông báo", JOptionPane.OK_OPTION);
            }
        } else {
            try {
                int ii = i;
                Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/emoji/emoji-" + i + ".png"));
                Image test2 = test.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                JButton btn = new JButton();
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setIcon(new ImageIcon(test2));
                JPanel panel1 = new JPanel(new BorderLayout());
                JPanel panel2 = new JPanel(new BorderLayout());
                panel1.setOpaque(false);
                panel2.setOpaque(false);
                panel1.add(panel2, BorderLayout.LINE_START);
                panel2.add(btn, BorderLayout.LINE_START);
                panelLogChat.add(panel1);
                panelLogChat.updateUI();
                scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                    scrollPaneLogChat.repaint();
                });
                JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());
                        panelEmoji1.updateUI();
                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(panelChatIcon, "Lỗi khi tải Sticker" + e.getMessage(), "Thông báo", JOptionPane.OK_OPTION);
            }
        }
    }
    //Kết thúc phần xử lí Emoji

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelInfoGroup = new javax.swing.JPanel();
        srcollPanelInfoGroup = new javax.swing.JScrollPane();
        panelInfoGroupLog = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        panelToolLeft = new javax.swing.JPanel();
        btnLogOutTool = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        btTopSetting = new javax.swing.JButton();
        panelChatIcon = new javax.swing.JPanel();
        scrollPanelSticker = new javax.swing.JScrollPane();
        panelSticker = new javax.swing.JPanel();
        btnSentPicturePanelChatIcon = new javax.swing.JButton();
        btnStickerPanelChatIcon1 = new javax.swing.JButton();
        btnToolSMS = new javax.swing.JButton();
        btnToolAdd = new javax.swing.JButton();
        btnToolGroup = new javax.swing.JButton();
        btnToolContacts = new javax.swing.JButton();
        panelThemTableTabTool = new javax.swing.JPanel();
        panelSearchFriends_ThemTableTabTool = new javax.swing.JPanel();
        textSearchFriends_ThemTableTabTool = new javax.swing.JTextField();
        btnSearchFriends_ThemTableTabTool = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnAddNameGroup_ThemTableTabTool = new javax.swing.JButton();
        textNameChatGroup_ThemTableTabTool = new javax.swing.JTextField();
        lbUserAdd = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        scrollPanelCreateGroup = new javax.swing.JScrollPane();
        panelCreateGroupLog = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        panelSmsGroup = new javax.swing.JPanel();
        scrollPaneGroup = new javax.swing.JScrollPane();
        panelSmsGroupList = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        pannelDanhBaChinh = new javax.swing.JPanel();
        scrollPaneDanhBa = new javax.swing.JScrollPane();
        panelDanhBa2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelTinNhan = new javax.swing.JPanel();
        scrollPaneSMS = new javax.swing.JScrollPane();
        panelSMS = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldchat = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButtonsent2 = new javax.swing.JButton();
        btnEmojiPicturePanelChatIcon = new javax.swing.JButton();
        panelTop_panelChatIcon = new javax.swing.JPanel();
        txtStatutHoTen = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        btnGroupInfo = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        scrollPaneLogChat = new javax.swing.JScrollPane();
        panelLogChat = new javax.swing.JPanel();
        PanelNoTiFiTool = new javax.swing.JPanel();
        srcollPanelNoTiFi = new javax.swing.JScrollPane();
        panelNotifi = new javax.swing.JPanel();
        btnClosePanelNoTiFiTool = new javax.swing.JButton();
        panelEmoji1 = new javax.swing.JPanel();
        panelEmoji = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        panelWelCome = new javax.swing.JPanel();
        lbWelCome1 = new javax.swing.JLabel();
        lbWelCome2 = new javax.swing.JLabel();
        lbWelCome3 = new javax.swing.JLabel();
        lbWelCome4 = new javax.swing.JLabel();
        PanelNoTiFiToolGroup = new javax.swing.JPanel();
        srcollPanelNoTiFiGroup = new javax.swing.JScrollPane();
        panelNotifi1 = new javax.swing.JPanel();
        btnClosePanelNoTiFiToolGroup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("dfsdfsdf");
        setSize(new java.awt.Dimension(1147, 650));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelInfoGroup.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoGroup.setBorder(null);
        panelInfoGroup.setForeground(new java.awt.Color(255, 255, 255));
        panelInfoGroup.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        srcollPanelInfoGroup.setBackground(new java.awt.Color(255, 255, 255));
        srcollPanelInfoGroup.setBorder(null);

        panelInfoGroupLog.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoGroupLog.setBorder(null);
        panelInfoGroupLog.setLayout(new javax.swing.BoxLayout(panelInfoGroupLog, javax.swing.BoxLayout.Y_AXIS));
        srcollPanelInfoGroup.setViewportView(panelInfoGroupLog);

        panelInfoGroup.add(srcollPanelInfoGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 84, 338, 590));

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(27, 130, 231));
        jLabel3.setText("Danh sách thành viên trong nhóm :");
        panelInfoGroup.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jButton2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(27, 130, 231));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Plus.png"))); // NOI18N
        jButton2.setText("Thêm Bạn");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        panelInfoGroup.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 60));

        jTextField1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(27, 130, 231));
        jTextField1.setText("Nhập tên thành viên cần thêm");
        panelInfoGroup.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 240, 40));

        getContentPane().add(panelInfoGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 350, 680));

        panelToolLeft.setBackground(new java.awt.Color(29, 35, 42));
        panelToolLeft.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogOutTool.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLogOutTool.setForeground(new java.awt.Color(255, 255, 255));
        btnLogOutTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/logout.png"))); // NOI18N
        btnLogOutTool.setText("    Đăng xuất");
        btnLogOutTool.setBorderPainted(false);
        btnLogOutTool.setContentAreaFilled(false);
        btnLogOutTool.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogOutToolMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogOutToolMouseExited(evt);
            }
        });
        btnLogOutTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutToolActionPerformed(evt);
            }
        });
        panelToolLeft.add(btnLogOutTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 380, 210, -1));

        jButton11.setBackground(new java.awt.Color(51, 51, 255));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/account.png"))); // NOI18N
        jButton11.setText("         Chat");
        jButton11.setBorderPainted(false);
        jButton11.setContentAreaFilled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 190, 170, -1));

        jButton12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/exit.png"))); // NOI18N
        jButton12.setText("       Thoát");
        jButton12.setBorderPainted(false);
        jButton12.setContentAreaFilled(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 430, 210, -1));

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/setting.png"))); // NOI18N
        jButton13.setText("      Cài đặt");
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 330, 210, -1));

        jButton14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/support.png"))); // NOI18N
        jButton14.setText("      Hỗ trợ");
        jButton14.setBorderPainted(false);
        jButton14.setContentAreaFilled(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 280, 150, -1));

        jButton21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/info.png"))); // NOI18N
        jButton21.setText("   Thông tin");
        jButton21.setBorderPainted(false);
        jButton21.setContentAreaFilled(false);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 230, 180, -1));

        jButton15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/notifi.png"))); // NOI18N
        jButton15.setText("    Thông báo");
        jButton15.setBorderPainted(false);
        jButton15.setContentAreaFilled(false);
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton15MouseEntered(evt);
            }
        });
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        panelToolLeft.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, -1, 52));

        btTopSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/menu/Menu.png"))); // NOI18N
        btTopSetting.setBorderPainted(false);
        btTopSetting.setContentAreaFilled(false);
        btTopSetting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btTopSettingMouseEntered(evt);
            }
        });
        btTopSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTopSettingActionPerformed(evt);
            }
        });
        panelToolLeft.add(btTopSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 50));

        getContentPane().add(panelToolLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 680));

        panelChatIcon.setBackground(new java.awt.Color(255, 255, 255));
        panelChatIcon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPanelSticker.setBackground(new java.awt.Color(255, 255, 255));

        panelSticker.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelStickerLayout = new javax.swing.GroupLayout(panelSticker);
        panelSticker.setLayout(panelStickerLayout);
        panelStickerLayout.setHorizontalGroup(
            panelStickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        panelStickerLayout.setVerticalGroup(
            panelStickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );

        scrollPanelSticker.setViewportView(panelSticker);

        panelChatIcon.add(scrollPanelSticker, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, 360, 690));

        btnSentPicturePanelChatIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Image.png"))); // NOI18N
        btnSentPicturePanelChatIcon.setBorderPainted(false);
        btnSentPicturePanelChatIcon.setContentAreaFilled(false);
        btnSentPicturePanelChatIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSentPicturePanelChatIconMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSentPicturePanelChatIconMouseExited(evt);
            }
        });
        btnSentPicturePanelChatIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSentPicturePanelChatIconActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnSentPicturePanelChatIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 560, 40, 50));

        btnStickerPanelChatIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Sticker.png"))); // NOI18N
        btnStickerPanelChatIcon1.setBorderPainted(false);
        btnStickerPanelChatIcon1.setContentAreaFilled(false);
        btnStickerPanelChatIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStickerPanelChatIcon1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStickerPanelChatIcon1MouseExited(evt);
            }
        });
        btnStickerPanelChatIcon1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStickerPanelChatIcon1ActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnStickerPanelChatIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 40, 50));

        btnToolSMS.setBackground(new java.awt.Color(255, 255, 255));
        btnToolSMS.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnToolSMS.setForeground(new java.awt.Color(29, 35, 42));
        btnToolSMS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Messagebtn.png"))); // NOI18N
        btnToolSMS.setText("Tin nhắn");
        btnToolSMS.setBorderPainted(false);
        btnToolSMS.setContentAreaFilled(false);
        btnToolSMS.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnToolSMS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolSMS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnToolSMS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToolSMSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToolSMSMouseExited(evt);
            }
        });
        btnToolSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToolSMSActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnToolSMS, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 80, 100));

        btnToolAdd.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnToolAdd.setForeground(new java.awt.Color(29, 35, 42));
        btnToolAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/them.png"))); // NOI18N
        btnToolAdd.setText("Thêm");
        btnToolAdd.setContentAreaFilled(false);
        btnToolAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnToolAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToolAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToolAddMouseExited(evt);
            }
        });
        btnToolAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToolAddActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnToolAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 580, 70, 100));

        btnToolGroup.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnToolGroup.setForeground(new java.awt.Color(29, 35, 42));
        btnToolGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Group.png"))); // NOI18N
        btnToolGroup.setText("Nhóm");
        btnToolGroup.setContentAreaFilled(false);
        btnToolGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnToolGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToolGroupMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToolGroupMouseExited(evt);
            }
        });
        btnToolGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToolGroupActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnToolGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 580, 70, 100));

        btnToolContacts.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnToolContacts.setForeground(new java.awt.Color(29, 35, 42));
        btnToolContacts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Contactbtn.png"))); // NOI18N
        btnToolContacts.setText("Danh bạ");
        btnToolContacts.setContentAreaFilled(false);
        btnToolContacts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolContacts.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnToolContacts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToolContactsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToolContactsMouseExited(evt);
            }
        });
        btnToolContacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToolContactsActionPerformed(evt);
            }
        });
        panelChatIcon.add(btnToolContacts, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 80, 100));

        panelThemTableTabTool.setBackground(new java.awt.Color(39, 45, 52));
        panelThemTableTabTool.setBorder(null);
        panelThemTableTabTool.setOpaque(false);
        panelThemTableTabTool.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSearchFriends_ThemTableTabTool.setBackground(new java.awt.Color(255, 255, 255));
        panelSearchFriends_ThemTableTabTool.setBorder(null);
        panelSearchFriends_ThemTableTabTool.setLayout(new java.awt.BorderLayout());
        panelThemTableTabTool.add(panelSearchFriends_ThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 260, 70));

        textSearchFriends_ThemTableTabTool.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        textSearchFriends_ThemTableTabTool.setForeground(new java.awt.Color(30, 142, 252));
        textSearchFriends_ThemTableTabTool.setText("Nhập số điện thoại để tìm bạn");
        textSearchFriends_ThemTableTabTool.setBorder(null);
        textSearchFriends_ThemTableTabTool.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                textSearchFriends_ThemTableTabToolMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                textSearchFriends_ThemTableTabToolMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textSearchFriends_ThemTableTabToolMousePressed(evt);
            }
        });
        panelThemTableTabTool.add(textSearchFriends_ThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 260, 30));

        btnSearchFriends_ThemTableTabTool.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSearchFriends_ThemTableTabTool.setForeground(new java.awt.Color(30, 142, 252));
        btnSearchFriends_ThemTableTabTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Search.png"))); // NOI18N
        btnSearchFriends_ThemTableTabTool.setText("Tìm bạn");
        btnSearchFriends_ThemTableTabTool.setBorderPainted(false);
        btnSearchFriends_ThemTableTabTool.setContentAreaFilled(false);
        btnSearchFriends_ThemTableTabTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchFriends_ThemTableTabToolActionPerformed(evt);
            }
        });
        panelThemTableTabTool.add(btnSearchFriends_ThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/resized/notibody_279x82.png"))); // NOI18N
        panelThemTableTabTool.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 290, 90));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/resized/notibody_279x82.png"))); // NOI18N
        panelThemTableTabTool.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 280, 90));

        btnAddNameGroup_ThemTableTabTool.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAddNameGroup_ThemTableTabTool.setForeground(new java.awt.Color(31, 144, 255));
        btnAddNameGroup_ThemTableTabTool.setText("Tạo");
        btnAddNameGroup_ThemTableTabTool.setBorderPainted(false);
        btnAddNameGroup_ThemTableTabTool.setContentAreaFilled(false);
        btnAddNameGroup_ThemTableTabTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNameGroup_ThemTableTabToolActionPerformed(evt);
            }
        });
        panelThemTableTabTool.add(btnAddNameGroup_ThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 80, 40));

        textNameChatGroup_ThemTableTabTool.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        textNameChatGroup_ThemTableTabTool.setForeground(new java.awt.Color(31, 144, 255));
        textNameChatGroup_ThemTableTabTool.setText("Nhập tên cho nhóm chat");
        textNameChatGroup_ThemTableTabTool.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textNameChatGroup_ThemTableTabToolMousePressed(evt);
            }
        });
        panelThemTableTabTool.add(textNameChatGroup_ThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 190, 40));

        lbUserAdd.setBackground(new java.awt.Color(39, 45, 52));
        lbUserAdd.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbUserAdd.setForeground(new java.awt.Color(31, 144, 255));
        panelThemTableTabTool.add(lbUserAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 220, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(31, 144, 255));
        jLabel8.setText("Tới:");
        panelThemTableTabTool.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, 30));

        scrollPanelCreateGroup.setBackground(new java.awt.Color(255, 255, 255));
        scrollPanelCreateGroup.setBorder(null);

        panelCreateGroupLog.setBackground(new java.awt.Color(255, 255, 255));
        panelCreateGroupLog.setBorder(null);
        panelCreateGroupLog.setOpaque(false);
        panelCreateGroupLog.setLayout(new javax.swing.BoxLayout(panelCreateGroupLog, javax.swing.BoxLayout.Y_AXIS));
        scrollPanelCreateGroup.setViewportView(panelCreateGroupLog);

        panelThemTableTabTool.add(scrollPanelCreateGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 260, 200));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/resized/Untitled-34_279x211.png"))); // NOI18N
        panelThemTableTabTool.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 280, 240));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/resized/notibody_279x82.png"))); // NOI18N
        panelThemTableTabTool.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 280, 80));

        panelChatIcon.add(panelThemTableTabTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 550));

        panelSmsGroup.setBackground(new java.awt.Color(39, 45, 52));
        panelSmsGroup.setBorder(null);
        panelSmsGroup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        panelSmsGroup.setOpaque(false);
        panelSmsGroup.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPaneGroup.setBorder(null);
        scrollPaneGroup.setViewportBorder(null);

        panelSmsGroupList.setBackground(new java.awt.Color(39, 45, 52));
        panelSmsGroupList.setBorder(null);
        panelSmsGroupList.setOpaque(false);
        panelSmsGroupList.setLayout(new javax.swing.BoxLayout(panelSmsGroupList, javax.swing.BoxLayout.PAGE_AXIS));
        scrollPaneGroup.setViewportView(panelSmsGroupList);

        panelSmsGroup.add(scrollPaneGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 510));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-34_3_280x531.png"))); // NOI18N
        panelSmsGroup.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 550));

        panelChatIcon.add(panelSmsGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 550));

        pannelDanhBaChinh.setBackground(new java.awt.Color(255, 255, 255));
        pannelDanhBaChinh.setOpaque(false);
        pannelDanhBaChinh.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPaneDanhBa.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneDanhBa.setBorder(null);
        scrollPaneDanhBa.setViewportBorder(null);

        panelDanhBa2.setBackground(new java.awt.Color(255, 255, 255));
        panelDanhBa2.setBorder(null);
        panelDanhBa2.setOpaque(false);
        panelDanhBa2.setLayout(new javax.swing.BoxLayout(panelDanhBa2, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneDanhBa.setViewportView(panelDanhBa2);

        pannelDanhBaChinh.add(scrollPaneDanhBa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 510));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-34_3_280x531.png"))); // NOI18N
        pannelDanhBaChinh.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 550));

        panelChatIcon.add(pannelDanhBaChinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 550));

        panelTinNhan.setBackground(new java.awt.Color(255, 255, 255));
        panelTinNhan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        panelTinNhan.setOpaque(false);
        panelTinNhan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPaneSMS.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneSMS.setBorder(null);
        scrollPaneSMS.setViewportBorder(null);

        panelSMS.setBackground(new java.awt.Color(255, 255, 255));
        panelSMS.setBorder(null);
        panelSMS.setOpaque(false);
        panelSMS.setLayout(new javax.swing.BoxLayout(panelSMS, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneSMS.setViewportView(panelSMS);

        panelTinNhan.add(scrollPaneSMS, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 510));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-34_3_280x531.png"))); // NOI18N
        panelTinNhan.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 280, 570));

        panelChatIcon.add(panelTinNhan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 550));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-27.png"))); // NOI18N
        panelChatIcon.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 300, 100));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-18.png"))); // NOI18N
        panelChatIcon.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 680));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-22.png"))); // NOI18N
        panelChatIcon.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 680));

        jTextFieldchat.setBackground(new java.awt.Color(250, 250, 250));
        jTextFieldchat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextFieldchat.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldchat.setText("Nhập tin nhắn");
        jTextFieldchat.setBorder(null);
        jTextFieldchat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextFieldchatMousePressed(evt);
            }
        });
        jTextFieldchat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldchatKeyPressed(evt);
            }
        });
        panelChatIcon.add(jTextFieldchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 620, 280, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/inputFiled_322x55.png"))); // NOI18N
        panelChatIcon.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 610, 330, 60));

        jButtonsent2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/Sent.png"))); // NOI18N
        jButtonsent2.setBorderPainted(false);
        jButtonsent2.setContentAreaFilled(false);
        jButtonsent2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonsent2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonsent2MouseExited(evt);
            }
        });
        jButtonsent2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsent2ActionPerformed(evt);
            }
        });
        panelChatIcon.add(jButtonsent2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 610, 50, 70));

        btnEmojiPicturePanelChatIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/emoji/btnEmoji.png"))); // NOI18N
        btnEmojiPicturePanelChatIcon.setBorderPainted(false);
        btnEmojiPicturePanelChatIcon.setContentAreaFilled(false);
        btnEmojiPicturePanelChatIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEmojiPicturePanelChatIconMousePressed(evt);
            }
        });
        panelChatIcon.add(btnEmojiPicturePanelChatIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 630, 30, 30));

        panelTop_panelChatIcon.setBackground(new java.awt.Color(234, 234, 235));
        panelTop_panelChatIcon.setBorder(null);
        panelTop_panelChatIcon.setMinimumSize(new java.awt.Dimension(583, 89));
        panelTop_panelChatIcon.setOpaque(false);
        panelTop_panelChatIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelTop_panelChatIconMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelTop_panelChatIconMouseExited(evt);
            }
        });
        panelTop_panelChatIcon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtStatutHoTen.setBackground(new java.awt.Color(30, 142, 252));
        txtStatutHoTen.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        panelTop_panelChatIcon.add(txtStatutHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 270, 30));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/user.png"))); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
        });
        panelTop_panelChatIcon.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 90, 100));

        jButton16.setBackground(new java.awt.Color(255, 255, 255));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Chat/onine.png"))); // NOI18N
        jButton16.setBorderPainted(false);
        jButton16.setContentAreaFilled(false);
        jButton16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton16MouseEntered(evt);
            }
        });
        panelTop_panelChatIcon.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 40, 20));

        btnGroupInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/ContactsTool/Info.png"))); // NOI18N
        btnGroupInfo.setBorderPainted(false);
        btnGroupInfo.setContentAreaFilled(false);
        btnGroupInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupInfoActionPerformed(evt);
            }
        });
        panelTop_panelChatIcon.add(btnGroupInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 30, 40));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-39_456x117.png"))); // NOI18N
        panelTop_panelChatIcon.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 110));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/Untitled-38_456x117.png"))); // NOI18N
        panelTop_panelChatIcon.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 110));

        panelChatIcon.add(panelTop_panelChatIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 460, 110));

        scrollPaneLogChat.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneLogChat.setBorder(null);
        scrollPaneLogChat.setForeground(new java.awt.Color(255, 255, 255));
        scrollPaneLogChat.setViewportBorder(null);
        scrollPaneLogChat.setPreferredSize(new java.awt.Dimension(460, 350));
        scrollPaneLogChat.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                scrollPaneLogChatMouseMoved(evt);
            }
        });
        scrollPaneLogChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                scrollPaneLogChatMouseEntered(evt);
            }
        });

        panelLogChat.setBackground(new java.awt.Color(255, 255, 255));
        panelLogChat.setBorder(null);
        panelLogChat.setOpaque(false);
        panelLogChat.setLayout(new javax.swing.BoxLayout(panelLogChat, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneLogChat.setViewportView(panelLogChat);

        panelChatIcon.add(scrollPaneLogChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 450, 560));

        getContentPane().add(panelChatIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 0, 1110, 680));

        PanelNoTiFiTool.setBackground(new java.awt.Color(255, 255, 255));
        PanelNoTiFiTool.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(39, 46, 53), 2));
        PanelNoTiFiTool.setForeground(new java.awt.Color(39, 46, 53));

        srcollPanelNoTiFi.setBackground(new java.awt.Color(255, 255, 255));

        panelNotifi.setBackground(new java.awt.Color(255, 255, 255));
        panelNotifi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelNotifi.setLayout(new javax.swing.BoxLayout(panelNotifi, javax.swing.BoxLayout.Y_AXIS));
        srcollPanelNoTiFi.setViewportView(panelNotifi);

        btnClosePanelNoTiFiTool.setBackground(new java.awt.Color(255, 255, 255));
        btnClosePanelNoTiFiTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/ContactsTool/Cancel.png"))); // NOI18N
        btnClosePanelNoTiFiTool.setBorderPainted(false);
        btnClosePanelNoTiFiTool.setContentAreaFilled(false);
        btnClosePanelNoTiFiTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosePanelNoTiFiToolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelNoTiFiToolLayout = new javax.swing.GroupLayout(PanelNoTiFiTool);
        PanelNoTiFiTool.setLayout(PanelNoTiFiToolLayout);
        PanelNoTiFiToolLayout.setHorizontalGroup(
            PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolLayout.createSequentialGroup()
                    .addContainerGap(19, Short.MAX_VALUE)
                    .addComponent(srcollPanelNoTiFi, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(22, Short.MAX_VALUE)))
            .addGroup(PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolLayout.createSequentialGroup()
                    .addGap(0, 284, Short.MAX_VALUE)
                    .addComponent(btnClosePanelNoTiFiTool, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        PanelNoTiFiToolLayout.setVerticalGroup(
            PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
            .addGroup(PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolLayout.createSequentialGroup()
                    .addContainerGap(34, Short.MAX_VALUE)
                    .addComponent(srcollPanelNoTiFi, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE)))
            .addGroup(PanelNoTiFiToolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelNoTiFiToolLayout.createSequentialGroup()
                    .addComponent(btnClosePanelNoTiFiTool)
                    .addGap(0, 369, Short.MAX_VALUE)))
        );

        getContentPane().add(PanelNoTiFiTool, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, 320, 410));

        panelEmoji1.setBackground(new java.awt.Color(255, 255, 255));
        panelEmoji1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 144, 255), 3));
        panelEmoji1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                panelEmoji1MouseMoved(evt);
            }
        });
        panelEmoji1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelEmoji1MouseExited(evt);
            }
        });
        panelEmoji1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelEmoji.setBackground(new java.awt.Color(255, 255, 255));
        panelEmoji.setBorder(null);
        panelEmoji.setOpaque(false);
        panelEmoji.setLayout(new java.awt.GridLayout(4, 4));
        panelEmoji1.add(panelEmoji, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 7, 310, 180));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/view/dock.png"))); // NOI18N
        panelEmoji1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 3, 325, 190));

        getContentPane().add(panelEmoji1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 400, 330, 200));

        panelWelCome.setBackground(new java.awt.Color(39, 46, 53));
        panelWelCome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbWelCome1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbWelCome1.setForeground(new java.awt.Color(255, 255, 255));
        lbWelCome1.setText("Chào");
        panelWelCome.add(lbWelCome1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 620, 50));

        lbWelCome2.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        lbWelCome2.setForeground(new java.awt.Color(0, 161, 242));
        lbWelCome2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/Welcome/Welcome.png"))); // NOI18N
        lbWelCome2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbWelCome2MousePressed(evt);
            }
        });
        panelWelCome.add(lbWelCome2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 100, 100));

        lbWelCome3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbWelCome3.setForeground(new java.awt.Color(255, 255, 255));
        lbWelCome3.setText("Chào mừng bạn đã quay trở lại CTU Talks PC");
        panelWelCome.add(lbWelCome3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, -1, -1));

        lbWelCome4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbWelCome4.setForeground(new java.awt.Color(255, 255, 255));
        lbWelCome4.setText(" Chọn vào biểu tượng chat để bắt đầu trò chuyện");
        panelWelCome.add(lbWelCome4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, -1, -1));

        getContentPane().add(panelWelCome, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 1110, 680));

        PanelNoTiFiToolGroup.setBackground(new java.awt.Color(255, 255, 255));
        PanelNoTiFiToolGroup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(39, 46, 53), 2));
        PanelNoTiFiToolGroup.setForeground(new java.awt.Color(39, 46, 53));

        srcollPanelNoTiFiGroup.setBackground(new java.awt.Color(255, 255, 255));

        panelNotifi1.setBackground(new java.awt.Color(255, 255, 255));
        panelNotifi1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelNotifi1.setLayout(new javax.swing.BoxLayout(panelNotifi1, javax.swing.BoxLayout.Y_AXIS));
        srcollPanelNoTiFiGroup.setViewportView(panelNotifi1);

        btnClosePanelNoTiFiToolGroup.setBackground(new java.awt.Color(255, 255, 255));
        btnClosePanelNoTiFiToolGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project_chat_server/image/ContactsTool/Cancel.png"))); // NOI18N
        btnClosePanelNoTiFiToolGroup.setBorderPainted(false);
        btnClosePanelNoTiFiToolGroup.setContentAreaFilled(false);
        btnClosePanelNoTiFiToolGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosePanelNoTiFiToolGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelNoTiFiToolGroupLayout = new javax.swing.GroupLayout(PanelNoTiFiToolGroup);
        PanelNoTiFiToolGroup.setLayout(PanelNoTiFiToolGroupLayout);
        PanelNoTiFiToolGroupLayout.setHorizontalGroup(
            PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
            .addGroup(PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolGroupLayout.createSequentialGroup()
                    .addContainerGap(19, Short.MAX_VALUE)
                    .addComponent(srcollPanelNoTiFiGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(22, Short.MAX_VALUE)))
            .addGroup(PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolGroupLayout.createSequentialGroup()
                    .addGap(0, 284, Short.MAX_VALUE)
                    .addComponent(btnClosePanelNoTiFiToolGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        PanelNoTiFiToolGroupLayout.setVerticalGroup(
            PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
            .addGroup(PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelNoTiFiToolGroupLayout.createSequentialGroup()
                    .addContainerGap(34, Short.MAX_VALUE)
                    .addComponent(srcollPanelNoTiFiGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE)))
            .addGroup(PanelNoTiFiToolGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelNoTiFiToolGroupLayout.createSequentialGroup()
                    .addComponent(btnClosePanelNoTiFiToolGroup)
                    .addGap(0, 369, Short.MAX_VALUE)))
        );

        getContentPane().add(PanelNoTiFiToolGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, 320, 410));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Phuong thuc tao su kien nut sentServer
    private void jButtonsent2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsent2ActionPerformed
        if (jTextFieldchat.getText().equals("") || jTextFieldchat.getText().equals("Nhập tin nhắn")) {
            jTextFieldchat.requestFocus();
        } else {
            //Gửi tin nhắn cho 1 người
            if (idfriend != 0) {
                sentServer_.println("Chat:" + sdt + ":" + idfriend + ":" + jTextFieldchat.getText());
                sentServer_.flush();
                try {
                    String textchat = jTextFieldchat.getText().trim();
                    Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Sent.png"));
                    int size = textchat.length();
                    Image test2 = test.getScaledInstance((int) ((size + 10) * (6.6)), 80, Image.SCALE_SMOOTH);
                    //Phần sms text
                    JLabel lb = new JLabel();
                    lb.setIcon(new ImageIcon(test2));
                    lb.setText(textchat);
                    lb.setForeground(Color.WHITE);
                    Font f = new Font(textchat, 2, 14);
                    lb.setFont(f);
                    lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);

                    JPanel panel1 = new JPanel(new BorderLayout());
                    JPanel panel2 = new JPanel(new BorderLayout());
                    panel1.setOpaque(false);
                    panel2.setOpaque(false);
                    panel1.add(panel2, BorderLayout.LINE_END);
                    panel2.add(lb, BorderLayout.LINE_START);
                    Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                    JLabel hinh = new JLabel();
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
                            panelEmoji1.updateUI();
                            // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                            verticalBar.removeAdjustmentListener(this);
                        }
                    };
                    scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                        scrollPaneLogChat.repaint();
                    });
                    verticalBar.addAdjustmentListener(scroller);
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                } catch (IOException e) {
                    JOptionPane.showConfirmDialog(this, "Lỗi Khi gửi tin nhắn!");
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                }
            }
            if (!userNumMemGroup.isEmpty()) {
                //group:chatGroup:sms:01639997154:[1639997155,1639997156,1639997154]:Pla Pla Pla:Phân Tihcs
                //group:chatGroup:sms:01639997155:Pla Pla Pla:Phân Tihcs
                sentServer_.println("group:chatGroup:sms:" + sdt + ":" + userNumMemGroup + ":" + jTextFieldchat.getText() + ":" + txtStatutHoTen.getText().trim());
                sentServer_.flush();
                try {
                    String textchat = jTextFieldchat.getText().trim();
                    Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Sent.png"));
                    int size = textchat.length();
                    Image test2 = test.getScaledInstance((int) ((size + 10) * (6.6)), 80, Image.SCALE_SMOOTH);
                    //Phần sms text
                    JLabel lb = new JLabel();
                    lb.setIcon(new ImageIcon(test2));
                    lb.setText(textchat);
                    lb.setForeground(Color.WHITE);
                    Font f = new Font(textchat, 2, 14);
                    lb.setFont(f);
                    lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);

                    JPanel panel1 = new JPanel(new BorderLayout());
                    JPanel panel2 = new JPanel(new BorderLayout());
                    panel1.setOpaque(false);
                    panel2.setOpaque(false);
                    panel1.add(panel2, BorderLayout.LINE_END);
                    panel2.add(lb, BorderLayout.LINE_START);
                    Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                    JLabel hinh = new JLabel();
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
                            panelEmoji1.updateUI();
                            // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                            verticalBar.removeAdjustmentListener(this);
                        }
                    };
                    scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                        scrollPaneLogChat.repaint();
                    });
                    verticalBar.addAdjustmentListener(scroller);
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                } catch (IOException e) {
                    JOptionPane.showConfirmDialog(this, "Lỗi Khi gửi tin nhắn!");
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_jButtonsent2ActionPerformed

    //Phương thức khi Frame Load
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //Yêu cầu server trả về info user
        sentServer_.println("info:" + sdt);
        sentServer_.flush();

        //Trả về danh sách bạn!
        sentServer_.println("Friend:" + sdt);
        sentServer_.flush();

        //Yêu cầu trả về danh sách Group
        sentServer_.println("group:loadGroup:" + sdt);
        sentServer_.flush();

        //yêu cầu trả về all người đăng kí sử dụng CTU Talks
        sentServer_.println("allUser");
        sentServer_.flush();

        //Yêu cầu thông báo có danh sách bạn!
        sentServer_.println("loadAddFriends:" + sdt);
        sentServer_.flush();


    }//GEN-LAST:event_formWindowOpened

    private void scrollPaneLogChatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPaneLogChatMouseEntered
//        // TODO add your handling code here:
//        scrollPaneLogChat.validate();
//        scrollPaneLogChat.updateUI();
//        panelLogChat.validate();
//        panelLogChat.updateUI();
//        System.out.println("dang gọi");
    }//GEN-LAST:event_scrollPaneLogChatMouseEntered

    private void btTopSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTopSettingActionPerformed
        if (panelToolLeft.getWidth() == 60) {
            panelToolLeft.setSize(165, 680);
            panelChatIcon.setLocation(58 + 105, 0);
        } else {
            panelToolLeft.setSize(60, 680);
            panelChatIcon.setLocation(58, 0);
        }
    }//GEN-LAST:event_btTopSettingActionPerformed

    private void btnLogOutToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutToolActionPerformed
        try {
            sentServer_.println("Disconnect:" + sdt);
            sentServer_.flush();
            sock_.close();
        } catch (IOException ex) {
        }
        dispose();
        new login().setVisible(true);
    }//GEN-LAST:event_btnLogOutToolActionPerformed

    private void btTopSettingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btTopSettingMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/Menu1.png"));
            btTopSetting.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btTopSettingMouseEntered
    boolean isChat = true;
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (isChat) {
            isChat = false;
            panelChatIcon.setVisible(false);
        } else {
            isChat = true;
            panelChatIcon.setVisible(true);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        sentServer_.println("Friend:01639997155");
        sentServer_.flush();
        System.out.println("đã gửi");

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed
    Boolean isToolNoTiFi = true;
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if (isToolNoTiFi) {
            isToolNoTiFi = false;
            PanelNoTiFiTool.setVisible(true);
            panelChatIcon.setVisible(false);
            try {
                Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/Cancel.png"));
                btnClosePanelNoTiFiTool.setIcon(new ImageIcon(im));
                Image im2 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/notifi.png"));
                jButton15.setIcon(new ImageIcon(im2));
            } catch (IOException e) {
            }
        } else {
            isToolNoTiFi = true;
            PanelNoTiFiTool.setVisible(false);
            panelChatIcon.setVisible(true);
        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void btnLogOutToolMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutToolMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/logout2.png"));
            btnLogOutTool.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnLogOutToolMouseEntered

    private void lbWelCome2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbWelCome2MousePressed
        panelWelCome.setVisible(false);
        panelChatIcon.setVisible(true);
    }//GEN-LAST:event_lbWelCome2MousePressed

    private void btnSentPicturePanelChatIconMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSentPicturePanelChatIconMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Image2.png"));
            btnSentPicturePanelChatIcon.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnSentPicturePanelChatIconMouseEntered

    private void btnSentPicturePanelChatIconMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSentPicturePanelChatIconMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Image.png"));
            btnSentPicturePanelChatIcon.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnSentPicturePanelChatIconMouseExited

    private void textSearchFriends_ThemTableTabToolMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textSearchFriends_ThemTableTabToolMouseEntered

    }//GEN-LAST:event_textSearchFriends_ThemTableTabToolMouseEntered

    private void textSearchFriends_ThemTableTabToolMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textSearchFriends_ThemTableTabToolMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_textSearchFriends_ThemTableTabToolMouseExited

    private void btnStickerPanelChatIcon1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStickerPanelChatIcon1MouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Sticker2.png"));
            btnStickerPanelChatIcon1.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnStickerPanelChatIcon1MouseEntered

    private void btnStickerPanelChatIcon1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStickerPanelChatIcon1MouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Sticker.png"));
            btnStickerPanelChatIcon1.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnStickerPanelChatIcon1MouseExited
    private boolean isSitker = true;
    private void btnStickerPanelChatIcon1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStickerPanelChatIcon1ActionPerformed
        if (isSitker) {
            isSitker = false;
            setSize(1170 - 355, 720);
            scrollPanelSticker.setVisible(false);

        } else {
            isSitker = true;
            setSize(1170, 720);
            scrollPanelSticker.setVisible(true);
        }
    }//GEN-LAST:event_btnStickerPanelChatIcon1ActionPerformed

    private void jButtonsent2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonsent2MouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Sent2.png"));
            jButtonsent2.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_jButtonsent2MouseEntered

    private void jButtonsent2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonsent2MouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Sent.png"));
            jButtonsent2.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_jButtonsent2MouseExited

    /*
    Yêu cầu tìm số điện thoại - trả về coi người đó có trong dataServer hay không!
    Phần này dung Timer và hashMap để tra cứu!
     */
    private void btnSearchFriends_ThemTableTabToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchFriends_ThemTableTabToolActionPerformed
        panelSearchFriends_ThemTableTabTool.removeAll();
        panelSearchFriends_ThemTableTabTool.updateUI();
        //yêu cầu trả về all người đăng kí sử dụng CTU Talks
        sentServer_.println("allUser:");
        sentServer_.flush();
        String userNum = textSearchFriends_ThemTableTabTool.getText().trim();
        Integer userNum2 = Integer.parseInt(userNum.trim());
        System.out.println(userNum2);
        if (allClientOnServer.containsKey(userNum2) && userNum2 != Integer.parseInt(sdt)) {
            String userName = allClientOnServer.get(userNum2);
            try {
                Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                Image im2 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/ContactsTool/AddFriends.png"));
                //Icon user
                JButton a = new JButton();
                a.setContentAreaFilled(false);
                a.setIcon((Icon) new ImageIcon(im));
                a.setBackground(new Color(245, 246, 248));
                //Tên người dùng
                JLabel t = new JLabel(userName);
                t.setForeground(new Color(30, 144, 255));
                Font f = new Font(userName, 2, 14);
                t.setFont(f);
                //Kết bạn
                JButton b = new JButton();
                b.setContentAreaFilled(false);
                b.setIcon((Icon) new ImageIcon(im2));
                b.setBackground(new Color(245, 246, 248));
                b.addActionListener((ActionEvent e) -> {
                    int type = JOptionPane.showConfirmDialog(panelChatIcon, "Bạn muốn kết bạn với " + userName + " !", "Kết bạn!", JOptionPane.YES_NO_OPTION);
                    if (type == JOptionPane.YES_OPTION) {
                        sentServer_.println("AddFriend:" + sdt + ":" + userNum + ":0");
                        sentServer_.flush();
                        JOptionPane.showConfirmDialog(panelChatIcon, "Đã gửi lời mời kết bạn " + userName + " !", "Kết bạn!", JOptionPane.OK_OPTION);
                        panelSearchFriends_ThemTableTabTool.removeAll();
                        panelSearchFriends_ThemTableTabTool.updateUI();
                        //Hiện lên danh bạ và ẩn đi chat,cha group, tool thêm
                        pannelDanhBaChinh.setVisible(true);
                        panelTinNhan.setVisible(false);
                        panelThemTableTabTool.setVisible(false);
                        panelSmsGroup.setVisible(false);
                    }
                });
                panelSearchFriends_ThemTableTabTool.add(a, BorderLayout.LINE_START);
                panelSearchFriends_ThemTableTabTool.add(t, BorderLayout.CENTER);
                panelSearchFriends_ThemTableTabTool.add(b, BorderLayout.LINE_END);
                panelSearchFriends_ThemTableTabTool.updateUI();

            } catch (IOException e) {
            }
        } else {
            JOptionPane.showConfirmDialog(panelChatIcon, textSearchFriends_ThemTableTabTool.getText() + " không tồn tại!");
            textSearchFriends_ThemTableTabTool.setText("Nhập số điện thoại để tìm bạn");

        }
    }//GEN-LAST:event_btnSearchFriends_ThemTableTabToolActionPerformed

    private void btnLogOutToolMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutToolMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/menu/logout.png"));
            btnLogOutTool.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnLogOutToolMouseExited


    private void btnToolSMSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolSMSMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Messagebtn2.png"));
            btnToolSMS.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolSMSMouseEntered

    private void btnToolSMSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolSMSMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Messagebtn.png"));
            btnToolSMS.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolSMSMouseExited
    //Xử lí sự kiện nút btnSmsTool
    private void btnToolSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolSMSActionPerformed
        while (!userNumMemGroup.isEmpty()) {
            userNumMemGroup.remove(0);
        }
        panelTinNhan.setVisible(true);
        pannelDanhBaChinh.setVisible(false);
        panelThemTableTabTool.setVisible(false);
        panelSmsGroup.setVisible(false);
    }//GEN-LAST:event_btnToolSMSActionPerformed

    private void btnToolContactsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolContactsMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Contactbtn2.png"));
            btnToolContacts.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolContactsMouseEntered

    private void btnToolContactsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolContactsMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Contactbtn.png"));
            btnToolContacts.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolContactsMouseExited
    //Xử lí sự kiện btnContact
    private void btnToolContactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolContactsActionPerformed
        while (!userNumMemGroup.isEmpty()) {
            userNumMemGroup.remove(0);
        }
        panelTinNhan.setVisible(false);
        pannelDanhBaChinh.setVisible(true);
        panelThemTableTabTool.setVisible(false);
        panelSmsGroup.setVisible(false);
    }//GEN-LAST:event_btnToolContactsActionPerformed

    private void btnToolGroupMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolGroupMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Group2.png"));
            btnToolGroup.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolGroupMouseEntered

    private void btnToolGroupMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolGroupMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/Group.png"));
            btnToolGroup.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolGroupMouseExited
    //Xử lí sự kiện btnSmsGroup
    private void btnToolGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolGroupActionPerformed
        panelTinNhan.setVisible(false);
        pannelDanhBaChinh.setVisible(false);
        panelSmsGroup.setVisible(true);
        panelThemTableTabTool.setVisible(false);
        idfriend = 0;
    }//GEN-LAST:event_btnToolGroupActionPerformed

    private void btnToolAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolAddMouseEntered
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/them2.png"));
            btnToolAdd.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolAddMouseEntered

    private void btnToolAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToolAddMouseExited
        try {
            Image im = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/Chat/them.png"));
            btnToolAdd.setIcon(new ImageIcon(im));
        } catch (IOException e) {
        }
    }//GEN-LAST:event_btnToolAddMouseExited
    //Xử lí sự kiện btnToolAdd
    private void btnToolAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolAddActionPerformed
        panelTinNhan.setVisible(false);
        pannelDanhBaChinh.setVisible(false);
        panelSmsGroup.setVisible(false);
        panelThemTableTabTool.setVisible(true);
        panelCreateGroupLog.removeAll();
        panelCreateGroupLog.updateUI();
        //Load dataUser on panelCreateGroup
        sentServer_.println("ListCreateGroup:" + sdt);
        sentServer_.flush();
    }//GEN-LAST:event_btnToolAddActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowIconified

    private void textSearchFriends_ThemTableTabToolMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textSearchFriends_ThemTableTabToolMousePressed
        textSearchFriends_ThemTableTabTool.setText("");
        textSearchFriends_ThemTableTabTool.requestFocus();
        panelSearchFriends_ThemTableTabTool.removeAll();
        panelSearchFriends_ThemTableTabTool.updateUI();
    }//GEN-LAST:event_textSearchFriends_ThemTableTabToolMousePressed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void btnClosePanelNoTiFiToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosePanelNoTiFiToolActionPerformed
        PanelNoTiFiTool.setVisible(false);
        panelChatIcon.setVisible(true);
    }//GEN-LAST:event_btnClosePanelNoTiFiToolActionPerformed

    private void jButton15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15MouseEntered

    private void panelEmoji1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEmoji1MouseExited
        panelEmoji1.updateUI();
    }//GEN-LAST:event_panelEmoji1MouseExited
    private Boolean checkEmoji = true;
    private void btnEmojiPicturePanelChatIconMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmojiPicturePanelChatIconMousePressed
        if (checkEmoji) {
            checkEmoji = false;
            panelEmoji1.setVisible(true);
        } else {
            checkEmoji = true;
            panelEmoji1.setVisible(false);
        }

    }//GEN-LAST:event_btnEmojiPicturePanelChatIconMousePressed

    private void panelTop_panelChatIconMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTop_panelChatIconMouseEntered

    }//GEN-LAST:event_panelTop_panelChatIconMouseEntered

    private void panelTop_panelChatIconMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTop_panelChatIconMouseExited

    }//GEN-LAST:event_panelTop_panelChatIconMouseExited

    private void jButton16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MouseEntered

    }//GEN-LAST:event_jButton16MouseEntered

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered

    }//GEN-LAST:event_jButton5MouseEntered

    private void panelEmoji1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelEmoji1MouseMoved
        panelEmoji1.updateUI();
    }//GEN-LAST:event_panelEmoji1MouseMoved

    private void scrollPaneLogChatMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPaneLogChatMouseMoved

    }//GEN-LAST:event_scrollPaneLogChatMouseMoved

    private void textNameChatGroup_ThemTableTabToolMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textNameChatGroup_ThemTableTabToolMousePressed
        textNameChatGroup_ThemTableTabTool.setText("");
        textNameChatGroup_ThemTableTabTool.requestFocus();
    }//GEN-LAST:event_textNameChatGroup_ThemTableTabToolMousePressed

    private void btnAddNameGroup_ThemTableTabToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNameGroup_ThemTableTabToolActionPerformed
        if (!"".equals(textNameChatGroup_ThemTableTabTool.getText()) && !"Nhập tên cho nhóm chat".equals(textNameChatGroup_ThemTableTabTool.getText()) && listAddUser.size() >= 2) {
            String nameGroupT = textNameChatGroup_ThemTableTabTool.getText().trim();
            listAddUser.add(sdt);
            sentServer_.println("addGroupName:" + sdt + ":" + nameGroupT + ":" + listAddUser);
            sentServer_.flush();
            while (!listAddUser.isEmpty()) {
                listAddUser.remove(0);
            }
            lbUserAdd.setText(listAddUser.toString());
            textNameChatGroup_ThemTableTabTool.setText("Nhập tên cho nhóm chat");
        } else {
            JOptionPane.showConfirmDialog(panelChatIcon, "Tên Group không được để trống or Thêm tối thiểu là 2 thành viên trong nhóm!");
            textNameChatGroup_ThemTableTabTool.setText("");
            textNameChatGroup_ThemTableTabTool.requestFocus();
        }
    }//GEN-LAST:event_btnAddNameGroup_ThemTableTabToolActionPerformed

    private void btnClosePanelNoTiFiToolGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosePanelNoTiFiToolGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClosePanelNoTiFiToolGroupActionPerformed

    private void jTextFieldchatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldchatMousePressed
        jTextFieldchat.setText("");
        jTextFieldchat.setForeground(Color.BLACK);
    }//GEN-LAST:event_jTextFieldchatMousePressed
    private File file2;
    private void btnSentPicturePanelChatIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSentPicturePanelChatIconActionPerformed
        //Gửi hình phải phân biệt giữa gửi đơn zs gửi nhóm
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(fileFilter);
        int selectFile = fileChooser.showDialog(this, "Chọn hình");
        File file = fileChooser.getSelectedFile().getAbsoluteFile();
        if (selectFile == JFileChooser.APPROVE_OPTION) {
            if (isCheckFile) {
                if (idfriend != 0) {
                    //idfriend != 0 thì gửi 1 - 1
                    System.out.println("Gửi hình cho" + idfriend);
                    sentFileToServer sentFileToServer = new sentFileToServer(sock_, panelLogChat, scrollPaneLogChat, "ordinary", sdt, String.valueOf(idfriend), listAddUser, file, fileChooser.getSelectedFile().getName());
                } else {
                    if (!userNumMemGroup.isEmpty()) {
                        //nếu userNumMemGroup khác rỗng thìhì gửi Group
                        System.out.println("Gửi hình cho group" + listAddUser);
                        //sentFileToServer sentFileToServer = new sentFileToServer(sock_, panelChatIcon, "group", sdt, "01639997155", listAddUser, fileChooser.getSelectedFile().getAbsolutePath(), fileChooser.getSelectedFile().getName());
                    }
                }
            } else {
                JOptionPane.showConfirmDialog(panelChatIcon, "Hãy chọn một bạn or một nhóm để gửi file");
            }

        }
    }//GEN-LAST:event_btnSentPicturePanelChatIconActionPerformed
    private boolean isCheckPnInfo = false;
    private void btnGroupInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupInfoActionPerformed
        if (isCheckPnInfo) {
            isCheckPnInfo = false;
            scrollPanelSticker.setVisible(true);
            panelInfoGroup.setVisible(false);
        } else {
            isCheckPnInfo = true;
            setSize(1170, 720);
            scrollPanelSticker.setVisible(false);
            panelInfoGroup.setVisible(true);
        }

    }//GEN-LAST:event_btnGroupInfoActionPerformed

    private void jTextFieldchatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldchatKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
            if (jTextFieldchat.getText().equals("") || jTextFieldchat.getText().equals("Nhập tin nhắn")) {
            jTextFieldchat.requestFocus();
        } else {
            //Gửi tin nhắn cho 1 người
            if (idfriend != 0) {
                sentServer_.println("Chat:" + sdt + ":" + idfriend + ":" + jTextFieldchat.getText());
                sentServer_.flush();
                try {
                    String textchat = jTextFieldchat.getText().trim();
                    Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Sent.png"));
                    int size = textchat.length();
                    Image test2 = test.getScaledInstance((int) ((size + 10) * (6.6)), 80, Image.SCALE_SMOOTH);
                    //Phần sms text
                    JLabel lb = new JLabel();
                    lb.setIcon(new ImageIcon(test2));
                    lb.setText(textchat);
                    lb.setForeground(Color.WHITE);
                    Font f = new Font(textchat, 2, 14);
                    lb.setFont(f);
                    lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);

                    JPanel panel1 = new JPanel(new BorderLayout());
                    JPanel panel2 = new JPanel(new BorderLayout());
                    panel1.setOpaque(false);
                    panel2.setOpaque(false);
                    panel1.add(panel2, BorderLayout.LINE_END);
                    panel2.add(lb, BorderLayout.LINE_START);
                    Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                    JLabel hinh = new JLabel();
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
                            panelEmoji1.updateUI();
                            // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                            verticalBar.removeAdjustmentListener(this);
                        }
                    };
                    scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                        scrollPaneLogChat.repaint();
                    });
                    verticalBar.addAdjustmentListener(scroller);
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                } catch (IOException e) {
                    JOptionPane.showConfirmDialog(this, "Lỗi Khi gửi tin nhắn!");
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                }
            }
            if (!userNumMemGroup.isEmpty()) {
                sentServer_.println("ChatGroup:" + sdt + ":" + userNumMemGroup + ":" + jTextFieldchat.getText() + ":" + txtStatutHoTen.getText().trim());
                sentServer_.flush();
                try {
                    String textchat = jTextFieldchat.getText().trim();
                    Image test = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/icon_Chat_Sent.png"));
                    int size = textchat.length();
                    Image test2 = test.getScaledInstance((int) ((size + 10) * (6.6)), 80, Image.SCALE_SMOOTH);
                    //Phần sms text
                    JLabel lb = new JLabel();
                    lb.setIcon(new ImageIcon(test2));
                    lb.setText(textchat);
                    lb.setForeground(Color.WHITE);
                    Font f = new Font(textchat, 2, 14);
                    lb.setFont(f);
                    lb.setHorizontalTextPosition((int) CENTER_ALIGNMENT);

                    JPanel panel1 = new JPanel(new BorderLayout());
                    JPanel panel2 = new JPanel(new BorderLayout());
                    panel1.setOpaque(false);
                    panel2.setOpaque(false);
                    panel1.add(panel2, BorderLayout.LINE_END);
                    panel2.add(lb, BorderLayout.LINE_START);
                    Image test3 = ImageIO.read(getClass().getClassLoader().getResource("project_chat_server/image/user_icon.png"));
                    JLabel hinh = new JLabel();
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
                            panelEmoji1.updateUI();
                            // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                            verticalBar.removeAdjustmentListener(this);
                        }
                    };
                    scrollPaneLogChat.getVerticalScrollBar().addAdjustmentListener((final AdjustmentEvent e) -> {
                        scrollPaneLogChat.repaint();
                    });
                    verticalBar.addAdjustmentListener(scroller);
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                } catch (IOException e) {
                    JOptionPane.showConfirmDialog(this, "Lỗi Khi gửi tin nhắn!");
                    jTextFieldchat.setText("");
                    jTextFieldchat.requestFocus();
                }
            }
        } 
        }
    }//GEN-LAST:event_jTextFieldchatKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelNoTiFiTool;
    private javax.swing.JPanel PanelNoTiFiToolGroup;
    private javax.swing.JButton btTopSetting;
    private javax.swing.JButton btnAddNameGroup_ThemTableTabTool;
    private javax.swing.JButton btnClosePanelNoTiFiTool;
    private javax.swing.JButton btnClosePanelNoTiFiToolGroup;
    private javax.swing.JButton btnEmojiPicturePanelChatIcon;
    private javax.swing.JButton btnGroupInfo;
    private javax.swing.JButton btnLogOutTool;
    private javax.swing.JButton btnSearchFriends_ThemTableTabTool;
    private javax.swing.JButton btnSentPicturePanelChatIcon;
    private javax.swing.JButton btnStickerPanelChatIcon1;
    private javax.swing.JButton btnToolAdd;
    private javax.swing.JButton btnToolContacts;
    private javax.swing.JButton btnToolGroup;
    private javax.swing.JButton btnToolSMS;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonsent2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldchat;
    private javax.swing.JLabel lbUserAdd;
    private javax.swing.JLabel lbWelCome1;
    private javax.swing.JLabel lbWelCome2;
    private javax.swing.JLabel lbWelCome3;
    private javax.swing.JLabel lbWelCome4;
    private javax.swing.JPanel panelChatIcon;
    private javax.swing.JPanel panelCreateGroupLog;
    private javax.swing.JPanel panelDanhBa2;
    private javax.swing.JPanel panelEmoji;
    private javax.swing.JPanel panelEmoji1;
    private javax.swing.JPanel panelInfoGroup;
    private javax.swing.JPanel panelInfoGroupLog;
    private javax.swing.JPanel panelLogChat;
    private javax.swing.JPanel panelNotifi;
    private javax.swing.JPanel panelNotifi1;
    private javax.swing.JPanel panelSMS;
    private javax.swing.JPanel panelSearchFriends_ThemTableTabTool;
    private javax.swing.JPanel panelSmsGroup;
    private javax.swing.JPanel panelSmsGroupList;
    private javax.swing.JPanel panelSticker;
    private javax.swing.JPanel panelThemTableTabTool;
    private javax.swing.JPanel panelTinNhan;
    private javax.swing.JPanel panelToolLeft;
    private javax.swing.JPanel panelTop_panelChatIcon;
    private javax.swing.JPanel panelWelCome;
    private javax.swing.JPanel pannelDanhBaChinh;
    private javax.swing.JScrollPane scrollPaneDanhBa;
    private javax.swing.JScrollPane scrollPaneGroup;
    private javax.swing.JScrollPane scrollPaneLogChat;
    private javax.swing.JScrollPane scrollPaneSMS;
    private javax.swing.JScrollPane scrollPanelCreateGroup;
    private javax.swing.JScrollPane scrollPanelSticker;
    private javax.swing.JScrollPane srcollPanelInfoGroup;
    private javax.swing.JScrollPane srcollPanelNoTiFi;
    private javax.swing.JScrollPane srcollPanelNoTiFiGroup;
    private javax.swing.JTextField textNameChatGroup_ThemTableTabTool;
    private javax.swing.JTextField textSearchFriends_ThemTableTabTool;
    private javax.swing.JLabel txtStatutHoTen;
    // End of variables declaration//GEN-END:variables
}
