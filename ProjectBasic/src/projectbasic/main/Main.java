/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.main;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import projectbasic.items.Place;
import projectbasic.renderer.JTableButtonRenderer;
import projectbasic.renderer.PlaceRenderer;
import projectbasic.table.TableFoodDetail;

/**
 *
 * @author Visual Studio
 */
public class Main extends javax.swing.JFrame {

    private JList<Place> listPlace;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        initialzation();
        panelPlace.setLayout(new BorderLayout());
        panelPlace.add(new JScrollPane(listPlace = createListPlace()), BorderLayout.CENTER);
        setdate();
        settingPanelPlaceDetail();
        settingPanleListFoodDetail();
        settingTableTotalBill();
        settingJlistFood();
    }

    private void initialzation() {
        getContentPane().setBackground(new Color(107, 70, 53));
        setResizable(false);
        //Set image for Button
        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_excel.png"));
            Image image2 = image.getScaledInstance(25,25,Image.SCALE_SMOOTH);
            btnExportFileExcel.setIcon(new ImageIcon(image2));
        } catch (IOException e) {
            System.out.println(""+e.getMessage());
        }
        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_statistical.jpg"));
            Image image2 = image.getScaledInstance(25,25,Image.SCALE_SMOOTH);
            btnStatistical.setIcon(new ImageIcon(image2));
        } catch (IOException e) {
            System.out.println(""+e.getMessage());
        }
    }

    private void setdate() {
        Date date = new Date();
        SimpleDateFormat fm = new SimpleDateFormat(" dd/MM/yyyy 'at' hh:mm:ss a ");
        lb_date.setText("" + fm.format(date));
    }

    private JList<Place> createListPlace() {
        // create List model
        DefaultListModel<Place> model = new DefaultListModel<>();
        // add item to model
        model.addElement(new Place("Tầng Trệt"));
        model.addElement(new Place("Tầng 2"));
        model.addElement(new Place("Tầng 3"));
        model.addElement(new Place("Tầng 4"));
        model.addElement(new Place("Tầng 5"));
        model.addElement(new Place("Sân vườn"));
        // create JList with model
        JList<Place> list = new JList<Place>(model);
        list.setCellRenderer(new PlaceRenderer());
        return list;
    }
    private void setActionForChooserGirdViewTable(JButton button,ActionListener at){
        button.addActionListener(at);
    }
    private void settingPanelPlaceDetail() {
        panelPlacedetail.setBorder(new TitledBorder("Khu 1"));
        panelPlacedetail.setLayout(new GridLayout(6, 3));
        for (int i = 0; i < 15; i++) {
            try {
                Image image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_table_full.png"));
                Image image2 = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JButton button = new JButton();
                button.setIcon(new ImageIcon(image2));
                button.setHorizontalTextPosition(SwingConstants.CENTER);
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setText("Bàn số " + i);
                button.addActionListener((e) -> {
                    System.out.println(button);
                    tableFoodDetai.removeRowSelectionInterval(0,tableFoodDetai.getRowCount() - 1);
                    new TableFoodDetail(tableFoodDetai);
                });
                panelPlacedetail.add(button);
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }
    }

    private void settingPanleListFoodDetail() {
        panelListFoodDetail.setLayout(new GridLayout(6, 2));
        for (int i = 0; i < 15; i++) {
            try {
                Image image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_FoodChicken.jpg"));
                Image image2 = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JLabel lb = new JLabel();
                lb.setIcon(new ImageIcon(image2));
                lb.setHorizontalTextPosition(SwingConstants.CENTER);
                lb.setVerticalTextPosition(SwingConstants.BOTTOM);
                lb.setText("Gà nấu canh chua");
//                button.addActionListener((ActionEvent e) -> {
//                    button.setEnabled(false);
//                });
                panelListFoodDetail.add(lb);
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }
    }
    
    private void settingJlistFood() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listModel.addElement("Tất cả món");
        listFood.setModel(listModel);
    }
    
    private void settingTableTotalBill(){
        Image image = null;
        Image image2 = null;
        Image image3 = null;
        Image image4 = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_pay.png"));
            image2 = image.getScaledInstance(110,20,Image.SCALE_SMOOTH);
            image3 = ImageIO.read(getClass().getClassLoader().getResource("projectbasic/src/ic_main_cancel.png"));
            image4 = image3.getScaledInstance(110,20,Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println(""+e.getMessage());
        }
        String[] columName = {"STT", "Mã hóa đơn", "Giảm giá", "Tiền giá trị gia tăng"
                , "Tiền chiết khấu","Tổng hóa đơn","Trạng thái"};
        Object[][] data = {
            {1,219,0,0,0,85000 + "VND",image2},
            {2,219,0,0,0,85000 + "VND",image2},
            {3,219,0,0,0,85000 + "VND",image4},
            {4,219,0,0,0,85000 + "VND",image2},
            {5,219,0,0,0,85000 + "VND",image4},
            {6,219,0,0,0,85000 + "VND",image4},
            {7,219,0,0,0,85000 + "VND",image2},
            
        };
        //setTableModel 
        DefaultTableModel tableModel = new DefaultTableModel(data, columName);
        //Set Model for tableFoodDetail
        tableTotalBill.setModel(tableModel);
        tableTotalBill.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableTotalBill.getColumnModel().getColumn(0).setPreferredWidth(27);
        tableTotalBill.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableTotalBill.getColumnModel().getColumn(2).setPreferredWidth(55);
        tableTotalBill.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableTotalBill.getColumnModel().getColumn(4).setPreferredWidth(60);
        tableTotalBill.getColumnModel().getColumn(6).setPreferredWidth(110);
        
        tableTotalBill.setRowHeight(28);

        //set Strig center for cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableTotalBill.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableTotalBill.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableTotalBill.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableTotalBill.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableTotalBill.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tableTotalBill.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
        //Insert button for cell[6] tableToalBill
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        tableTotalBill.getColumnModel().getColumn(6).setCellRenderer(buttonRenderer);
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        tabb_Main = new javax.swing.JTabbedPane();
        tabb_qlBan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFoodDetai = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        panelPlacedetail = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        panelPlace = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lb_date = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txttong_tien = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listFood = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        panelListFoodDetail = new javax.swing.JPanel();
        tabb_Thongke = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableTotalBill = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        btnExportFileExcel = new javax.swing.JButton();
        btnStatistical = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        tabb_qlThucDon = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField6 = new javax.swing.JTextField();
        btnThem_mon = new javax.swing.JButton();
        btnSua_mon = new javax.swing.JButton();
        btnXoa_thuc_don = new javax.swing.JButton();
        tabb_qlNhanSu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jtabNhanSu = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jlistNhanSu = new javax.swing.JList<>();
        btnThem_qlnv = new javax.swing.JButton();
        btnSua_qlnv = new javax.swing.JButton();
        btnXoa_qlnv = new javax.swing.JButton();
        btnTimKem_qlnv = new javax.swing.JButton();
        txtTimKiem_qlnv = new javax.swing.JTextField();
        txt_qlnv = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnShowChamCong = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(107, 70, 53));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("Bàn");

        jButton2.setText("Thực đơn");

        jButton3.setText("Hóa đơn");

        jButton4.setText("Thống kê");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        tabb_qlBan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableFoodDetai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableFoodDetai);

        tabb_qlBan.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 590, 220));

        jLabel1.setText("Ghi chú:");
        tabb_qlBan.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, -1, 40));

        jScrollPane2.setViewportView(jTextPane1);

        tabb_qlBan.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 510, 540, 40));

        panelPlacedetail.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane9.setViewportView(panelPlacedetail);

        tabb_qlBan.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 420, 240));

        javax.swing.GroupLayout panelPlaceLayout = new javax.swing.GroupLayout(panelPlace);
        panelPlace.setLayout(panelPlaceLayout);
        panelPlaceLayout.setHorizontalGroup(
            panelPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        panelPlaceLayout.setVerticalGroup(
            panelPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        jScrollPane10.setViewportView(panelPlace);

        tabb_qlBan.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, 240));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 2), "Thanh toán", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 51, 204))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Thời gian:");
        jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 34, -1, -1));

        jLabel4.setText("Hóa đơn:");
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 66, -1, -1));

        jLabel5.setText("Giảm giá:");
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 150, -1, -1));

        jLabel6.setText("Phụ phí:");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 176, -1, -1));

        jLabel8.setText("Tiền thừa:");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 295, -1, -1));

        jLabel9.setText("Thanh toán:");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 216, -1, -1));

        jLabel10.setText("Khách đưa:");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 254, -1, -1));

        lb_date.setText("jLabel7");
        jPanel7.add(lb_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 34, 119, -1));

        jLabel12.setText("jLabel12");
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 66, -1, -1));

        jLabel13.setText("%");
        jPanel7.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 150, -1, -1));

        jLabel14.setText("VNĐ");
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 176, -1, -1));

        jLabel15.setText("VNĐ");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 216, -1, -1));

        jLabel16.setText("VNĐ");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 254, -1, -1));

        jLabel17.setText("--");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 66, -1, -1));

        jLabel18.setText("VNĐ");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 295, -1, -1));
        jPanel7.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 147, 110, -1));
        jPanel7.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 173, 110, -1));
        jPanel7.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 213, 110, -1));
        jPanel7.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 289, 110, -1));
        jPanel7.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 251, 110, -1));

        jButton5.setText("Hủy hóa đơn");
        jPanel7.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 269, 100, 40));

        jButton6.setText("Thanh toán");
        jPanel7.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 220, 100, 40));

        jCheckBox1.setText("In hóa đơn");
        jPanel7.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, -1, -1));

        jLabel22.setText("jLabel22");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 66, -1, -1));

        jLabel7.setText("Tổng tiền:");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 112, -1, -1));
        jPanel7.add(txttong_tien, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 109, 110, -1));

        jLabel11.setText("VNĐ");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, -1, -1));

        tabb_qlBan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, 410, 330));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 2), "Menu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 204)), "Danh mục thực đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 204))); // NOI18N

        listFood.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listFood);

        jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelListFoodDetail.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane11.setViewportView(panelListFoodDetail);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                    .addComponent(jScrollPane11))
                .addContainerGap())
        );

        tabb_qlBan.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 370, 410, 190));

        tabb_Main.addTab("Quản lý bàn", tabb_qlBan);

        tabb_Thongke.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 2), "Tất cả hóa đơn"));
        jPanel15.setLayout(new java.awt.BorderLayout());

        tableTotalBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tableTotalBill);

        jPanel15.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Công nợ"));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTable4);

        jPanel16.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách số thực đơn đã bán"));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(jTable5);

        jPanel22.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabb_Thongke.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 630, 550));

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel48.setText("Tổng số tiền thu được tại");

        jLabel49.setText("quầy thu ngân:");

        jLabel50.setText("jLabel50");

        jLabel51.setText("(Tổng tiền TT + Tổng trả nợ - Tổng nợ )");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel49)
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(jLabel47))
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel48))))
                        .addGap(37, 37, 37)
                        .addComponent(jLabel50))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel51)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel51)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 310, 100));

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setText("Thống kê theo:");
        jPanel13.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 16, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel13.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 13, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel13.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 39, -1, -1));

        jLabel24.setText("Ngày:");
        jPanel13.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 42, -1, -1));

        jLabel25.setText("Tháng");
        jPanel13.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(108, 42, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel13.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 39, -1, -1));

        jLabel26.setText("Năm");
        jPanel13.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 42, -1, -1));

        jTextField7.setText("jTextField7");
        jPanel13.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 39, -1, -1));

        btnExportFileExcel.setText("Xuất file Excel");
        jPanel13.add(btnExportFileExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        btnStatistical.setText("Xem thống kê");
        jPanel13.add(btnStatistical, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));

        jPanel21.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 311, 120));

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setText("Tổng số hóa đơn đã hủy:");
        jPanel14.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, -1));

        jLabel28.setText("Tổng số tiền đã hủy:");
        jPanel14.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 79, -1, -1));

        jLabel29.setText("Tổng số % giảm đã hủy:");
        jPanel14.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 33, -1, -1));

        jLabel30.setText("Tổng số phụ phí đã hủy:");
        jPanel14.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 54, -1, -1));

        jLabel31.setText("jLabel31");
        jPanel14.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 13, -1, -1));

        jLabel32.setText("jLabel32");
        jPanel14.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 33, -1, -1));

        jLabel33.setText("jLabel33");
        jPanel14.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 54, -1, -1));

        jLabel34.setText("jLabel34");
        jPanel14.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 79, -1, -1));

        jPanel21.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 142, 310, 100));

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setText("Tổng số hóa đơn đã TT:");
        jPanel17.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, -1));

        jLabel37.setText("Tổng số phụ phí đã TT:");
        jPanel17.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 54, -1, -1));

        jLabel36.setText("Tổng số % giảm đã TT:");
        jPanel17.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 33, -1, -1));

        jLabel38.setText("Tổng số tiền đã TT:");
        jPanel17.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 79, -1, -1));

        jLabel39.setText("jLabel39");
        jPanel17.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 13, -1, -1));

        jLabel40.setText("jLabel40");
        jPanel17.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 33, -1, -1));

        jLabel41.setText("jLabel41");
        jPanel17.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 54, -1, -1));

        jLabel42.setText("jLabel42");
        jPanel17.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 79, -1, -1));

        jPanel21.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 310, 100));

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Tổng số hóa đơn nợ:");
        jPanel18.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, -1));

        jLabel44.setText("Tổng số tiền nợ:");
        jPanel18.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 45, -1, -1));

        jLabel45.setText("jLabel45");
        jPanel18.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 13, -1, -1));

        jLabel46.setText("jLabel46");
        jPanel18.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 45, -1, -1));

        jPanel21.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 310, 70));

        tabb_Thongke.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 380, 550));

        tabb_Main.addTab("Thống kê", tabb_Thongke);

        tabb_qlThucDon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 2), "Danh mục thực đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 204))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tabb_qlThucDon.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 190, 330));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 2), "Danh mục các món"));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable2);

        jPanel12.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        tabb_qlThucDon.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 11, 810, 460));
        tabb_qlThucDon.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 190, 32));

        btnThem_mon.setText("Thêm mới");
        tabb_qlThucDon.add(btnThem_mon, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 400, -1, -1));

        btnSua_mon.setText("Sửa món");
        tabb_qlThucDon.add(btnSua_mon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        btnXoa_thuc_don.setText("Xóa món đã chọn");
        tabb_qlThucDon.add(btnXoa_thuc_don, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 170, -1));

        tabb_Main.addTab("Quản lý thực đơn", tabb_qlThucDon);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 102), null), "Danh sách nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 204, 102))); // NOI18N

        jtabNhanSu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane12.setViewportView(jtabNhanSu);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null), "Danh mục chức vụ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 51, 255))); // NOI18N

        jlistNhanSu.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jlistNhanSu);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 420));

        btnThem_qlnv.setText("Thêm mới");

        btnSua_qlnv.setText("Sửa");

        btnXoa_qlnv.setText("Xóa mục đã chọn");

        btnTimKem_qlnv.setText("Tìm kiếm nhanh");

        txtTimKiem_qlnv.setText("jTextField8");

        txt_qlnv.setText("jTextField9");

        javax.swing.GroupLayout tabb_qlNhanSuLayout = new javax.swing.GroupLayout(tabb_qlNhanSu);
        tabb_qlNhanSu.setLayout(tabb_qlNhanSuLayout);
        tabb_qlNhanSuLayout.setHorizontalGroup(
            tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                        .addComponent(btnThem_qlnv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua_qlnv, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_qlnv))
                .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(btnTimKem_qlnv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem_qlnv, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(35, Short.MAX_VALUE))))
            .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(btnXoa_qlnv)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        tabb_qlNhanSuLayout.setVerticalGroup(
            tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane12))
                .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabb_qlNhanSuLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTimKem_qlnv)
                            .addComponent(txtTimKiem_qlnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabb_qlNhanSuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_qlnv, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tabb_qlNhanSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSua_qlnv)
                            .addComponent(btnThem_qlnv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(btnXoa_qlnv)
                .addGap(22, 22, 22))
        );

        tabb_Main.addTab("Quản lý nhân sự", tabb_qlNhanSu);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 0, 0));
        jLabel19.setText("Bảng chấm công");

        jLabel20.setText("Ngày chấm công:");

        jDateChooser1.setDateFormatString("dd/MM/yyyy");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane13.setViewportView(jTable1);

        btnShowChamCong.setText("Hiển thị");

        jButton10.setText("Cập nhật");

        jButton11.setText("Xuất File");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnShowChamCong)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 844, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel20))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowChamCong))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jButton10)
                        .addGap(41, 41, 41)
                        .addComponent(jButton11)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        tabb_Main.addTab("Bảng chấm công", jPanel4);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Bản quyền thuộc về xxx :v ");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabb_Main)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabb_Main, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(0, 0, 0))
        );

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
            UIManager.setLookAndFeel(new SyntheticaBlueSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportFileExcel;
    private javax.swing.JButton btnShowChamCong;
    private javax.swing.JButton btnStatistical;
    private javax.swing.JButton btnSua_mon;
    private javax.swing.JButton btnSua_qlnv;
    private javax.swing.JButton btnThem_mon;
    private javax.swing.JButton btnThem_qlnv;
    private javax.swing.JButton btnTimKem_qlnv;
    private javax.swing.JButton btnXoa_qlnv;
    private javax.swing.JButton btnXoa_thuc_don;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JList<String> jlistNhanSu;
    private javax.swing.JTable jtabNhanSu;
    private javax.swing.JLabel lb_date;
    private javax.swing.JList<String> listFood;
    private javax.swing.JPanel panelListFoodDetail;
    private javax.swing.JPanel panelPlace;
    private javax.swing.JPanel panelPlacedetail;
    private javax.swing.JTabbedPane tabb_Main;
    private javax.swing.JPanel tabb_Thongke;
    private javax.swing.JPanel tabb_qlBan;
    private javax.swing.JPanel tabb_qlNhanSu;
    private javax.swing.JPanel tabb_qlThucDon;
    private javax.swing.JTable tableFoodDetai;
    private javax.swing.JTable tableTotalBill;
    private javax.swing.JTextField txtTimKiem_qlnv;
    private javax.swing.JTextField txt_qlnv;
    private javax.swing.JTextField txttong_tien;
    // End of variables declaration//GEN-END:variables
}
