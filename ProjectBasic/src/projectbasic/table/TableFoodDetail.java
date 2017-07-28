/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.table;

/**
 *
 * @author Visual Studio
 */

import java.sql.Connection;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import projectbasic.db.Connect;

public class TableFoodDetail {
    private DefaultTableModel tableModel;
    private Connect connect;
    private String[] columName = {"STT", "Tên món", "Số lượng", "Đơn vị"
            , "Đơn giá","Giảm giá (%)", "Tổng tiền"};
    Object[][] data = {
            {1, "Bia Hà Nội chai lùn", 5, "Chai", 10000, 0, 50000 + " VND"},
            {2, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {3, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {4, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {5, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {6, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {7, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {8, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {9, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"},
            {10, "Bia Hà Nội chai dìa", 5, "Chai", 10000, 0, 50000 + " VND"}
        };

    public TableFoodDetail(JTable tableFoodDetai) {
        tableModel = new DefaultTableModel(data, columName);
        //Set Model for tableFoodDetail
        tableFoodDetai.setModel(tableModel);
        tableFoodDetai.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableFoodDetai.getColumnModel().getColumn(0).setPreferredWidth(27);
        tableFoodDetai.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableFoodDetai.getColumnModel().getColumn(2).setPreferredWidth(55);
        tableFoodDetai.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableFoodDetai.getColumnModel().getColumn(4).setPreferredWidth(60);
        tableFoodDetai.getColumnModel().getColumn(6).setPreferredWidth(110);

        //set Strig center for cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableFoodDetai.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableFoodDetai.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableFoodDetai.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableFoodDetai.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableFoodDetai.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tableFoodDetai.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

    }
    private Object getData(){
        connect.getRs("mot cau truy van lay ra du lieu");
       return null;
    }
}
