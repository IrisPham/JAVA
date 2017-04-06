/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Visual-Studio
 */
public class frmViewPicture extends javax.swing.JFrame{
    private JScrollPane scrollPane;
    public frmViewPicture(File fileAddress){
        initComponents();
        loadPic(fileAddress);
    }
    private void initComponents(){
        scrollPane = new JScrollPane();
        setTitle("Xem Ảnh");
        setLayout(new BorderLayout());
        Dimension sizeCurrentSize = Toolkit.getDefaultToolkit().getScreenSize();
        double widthSize = sizeCurrentSize.getWidth();
        double heightSize = sizeCurrentSize.getHeight();
        setSize((int) widthSize - 400,(int) heightSize - 150);
        setLocationRelativeTo(scrollPane);
    }
    private void loadPic(File fileAddress){
        try {
            BufferedImage readImage = ImageIO.read(fileAddress);
            int width = readImage.getWidth();
            int height = readImage.getHeight();
            System.out.println(width + ":" + height);
            Image readImage1 = ImageIO.read(fileAddress);
            scrollPane.setViewportView(new JLabel(new ImageIcon(readImage1)));
            add(scrollPane);
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(rootPane,"Không thể xem Hình Ảnh"+e.getMessage());
        }
    }
}
