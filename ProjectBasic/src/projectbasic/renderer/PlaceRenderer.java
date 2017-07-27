/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import projectbasic.items.Place;

/**
 *
 * @author Visual Studio
 */
public class PlaceRenderer extends JPanel implements ListCellRenderer<Place>{
    private JLabel lbNamePlace = new JLabel();
    private JLabel lbIconPlace = new JLabel();
    private JLabel lbStateTableInPlace = new JLabel();
    
    public PlaceRenderer(){
        setLayout(new BorderLayout());
        add(lbIconPlace,BorderLayout.WEST);
        add(lbNamePlace,BorderLayout.CENTER);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Place> list, Place value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_place.png"));
            Image image2 = image.getScaledInstance(65,65, image.SCALE_SMOOTH);
            lbIconPlace.setIcon(new ImageIcon(image2));
        } catch (IOException e) {
            System.out.println(""+e.getMessage());
        }
        lbNamePlace.setText(value.getNamePlace());
        return this;
    }
    
}
