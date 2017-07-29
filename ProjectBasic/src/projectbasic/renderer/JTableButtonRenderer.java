/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Visual Studio
 */
public class JTableButtonRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        if (isSelected) {
//            setForeground(table.getSelectionForeground());
//            setBackground(table.getSelectionBackground());
//            System.out.println(""+row + column);
//        } else {
//            setForeground(table.getForeground());
//            setBackground(new Color(13, 87, 176));
//        }
//        setText((value == null) ? "" : value.toString());
        JLabel label = new JLabel();
 
        if (value!=null) {
        label.setHorizontalAlignment(JLabel.CENTER);
        //value is parameter which filled by byteOfImage
        label.setIcon(new ImageIcon((Image) value));
        }
 
        return label;
    }
    

}
