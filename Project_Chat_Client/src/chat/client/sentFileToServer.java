/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.client;

import chat.GUI.frmViewPicture;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Visual-Studio
 */
public class sentFileToServer {

    private Socket clientSocket = null;
    private PrintWriter sentServer;
    private FileInputStream fis = null;
    private BufferedInputStream bis = null;
    private OutputStream os = null;
    private File myFile;
    private JPanel panelLogChat;
    private JScrollPane scrollPaneLogChat;
    public sentFileToServer(Socket clientSocket, JPanel panelLogChat,JScrollPane scrollPaneLogChat, String type, String userNumSent, String userNumReceive, ArrayList member, File fileAdress, String fileName) {
        this.clientSocket = clientSocket;
        this.panelLogChat = panelLogChat;
        this.scrollPaneLogChat = scrollPaneLogChat;
        try {
            sentServer = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(panelLogChat, "System: Null Socket");
        }
        //Sent is ordinary
        if (type.equals("ordinary")) {
            typeOfOrdinary(userNumSent, userNumReceive, fileAdress, fileName);
            
        } else {
            //typeOfGroup(type, userNumSent, member, fileAdress, fileName);
            System.out.println("gủi hình Group");
        }
    }

    private void typeOfOrdinary(String userNum, String userNumReceive, File fileAdress, String fileName) {
        sentServer.println("sentFile><ordinary><" + fileAdress + "><" + userNum + "><" + userNumReceive);
        sentServer.flush();
        sentServer.println("sentFile><ordinary><" + fileName + "><" + userNum + "><" + userNumReceive);
        sentServer.flush();
        sentServer.println("sentFile><file");
        sentServer.flush();
        myFile = fileAdress;
        loadImageOnChatSent(myFile,panelLogChat);
        try {
            //Tối đa 5 mb thôi 5242880
            if ((int) myFile.length() > 5242880) {
                JOptionPane.showConfirmDialog(panelLogChat,"Dung lượng phải nhỏ hơn 5 MB > "+(int) myFile.length());
            } else {
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = clientSocket.getOutputStream();
                JOptionPane.showConfirmDialog(panelLogChat,"Sending " + "git.docx" + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                JOptionPane.showConfirmDialog(panelLogChat,"Sending done");
                fis.close();
                bis.close();
            }
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(panelLogChat,"System:File"+e.getMessage());
        }
    }
    
    private void typeOfGroup(String type, String userNum, ArrayList member, String fileAdress, String fileName) {
        sentServer.println("sentFile><group><" + fileAdress + "><" + userNum + "><" + member);
        sentServer.flush();
        sentServer.println("sentFile><group><" + fileName + "><" + userNum + "><" + member);
        sentServer.flush();
        sentServer.println("sentFile><file");
        sentServer.flush();

        myFile = new File(fileAdress);
        try {
            //Tối đa 5 mb thôi 5242880
            if ((int) myFile.length() > 5242880) {
                JOptionPane.showConfirmDialog(panelLogChat,"Dung lượng phải nhỏ hơn 5 MB > "+(int) myFile.length());
                System.out.println("file quá lớn" + (int) myFile.length());
            } else {
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = clientSocket.getOutputStream();
                JOptionPane.showConfirmDialog(panelLogChat,"Sending " + "git.docx" + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                JOptionPane.showConfirmDialog(panelLogChat,"Sending done");
                fis.close();
                bis.close();
            }
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(panelLogChat,"System:File"+e.getMessage());
        }
    }
    
    public void loadImageOnChatSent(File image, JPanel panelLogChat){
        JPanel mainJPanel = new JPanel(new BorderLayout());
        JPanel main = new JPanel(new BorderLayout());
        JLabel lb = new JLabel();
        main.setMaximumSize(new Dimension(150,150));
        try {
            Image ima = ImageIO.read(image);
            Image ima2 = ima.getScaledInstance(150,150,Image.SCALE_SMOOTH);
            lb.setIcon(new ImageIcon(ima2));
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(panelLogChat,e.getMessage());
        }
        main.add(lb,BorderLayout.LINE_END);
        main.setOpaque(false);
        main.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                frmViewPicture fPicture = new frmViewPicture(myFile);
               fPicture.setVisible(true);
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
        mainJPanel.add(main,BorderLayout.LINE_END);
        mainJPanel.setOpaque(false);
        panelLogChat.add(mainJPanel);
        JScrollBar verticalBar = scrollPaneLogChat.getVerticalScrollBar();
                // If we want to scroll to the top set this value to the minimum, else to the maximum
                //int topOrBottom = direction.equals(ScrollDirection.UP) ? verticalBar.getMinimum() : verticalBar.getMaximum();
                AdjustmentListener scroller = new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        Adjustable adjustable = e.getAdjustable();
                        adjustable.setValue(verticalBar.getMaximum());
                        panelLogChat.updateUI();
                        // We have to remove the listener since otherwise the user would be unable to scroll afterwards
                        verticalBar.removeAdjustmentListener(this);
                    }
                };
                verticalBar.addAdjustmentListener(scroller);
    }
    public void loadImageOnChatReceive(File image, JPanel panelLogChat){
        JPanel mainJPanel = new JPanel(new BorderLayout());
        JPanel main = new JPanel();
        JLabel lb = new JLabel();
        main.setSize(new Dimension(600,400));
        main.setLayout(new BorderLayout());
        
        try {
            Image ima = ImageIO.read(image);
            ima.getScaledInstance(300,300,Image.SCALE_SMOOTH);
            lb.setIcon(new ImageIcon(ima));
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(panelLogChat,e.getMessage());
        }
        main.add(lb,BorderLayout.LINE_START);
        mainJPanel.add(main,BorderLayout.LINE_START);
    }
}

