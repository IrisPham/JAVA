/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Visual-Studio
 */
public class test extends javax.swing.JFrame {

    /**
     * Creates new form test
     */
    public test() {
        initComponents();
        String text = "Phạm Hoài An";
        for (int i = 0; i <= 5; i++) {
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
                JLabel lb2 = new JLabel("psadasdsa");
                lb2.setForeground(Color.BLUE);
                panel2.add(hinh, BorderLayout.LINE_START);
                panel2.add(lb2,BorderLayout.PAGE_START);
                jPanel1.add(panel1);
                jPanel1.updateUI(); 
            } catch (IOException ex) {
                
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 380, 240));
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
