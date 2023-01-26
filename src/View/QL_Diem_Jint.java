/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import Model.Grade;
import Model.Student;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author DUYAN
 */
public class QL_Diem_Jint extends javax.swing.JInternalFrame {

    ArrayList<Grade> list = new ArrayList<>();
    DefaultTableModel model;
    private Connection conn;
    int index = 0;

    public QL_Diem_Jint() {
        initComponents();

        tblBang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblBang.getTableHeader().setOpaque(false);
        tblBang.getTableHeader().setBackground(new Color(0, 153, 0));
        tblBang.getTableHeader().setForeground(new Color(0, 0, 0));
        tblBang.setRowHeight(30);

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
        bi.setNorthPane(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        model = (DefaultTableModel) tblBang.getModel();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLSV_FPT;user=sa;password=123");
        } catch (Exception e) {
            System.out.println(e);
        }
        list = (ArrayList<Grade>) getListGrade();
        loadDbToTable();
        loadDataToCbo();

    }

    public void loadDataToCbo() {
        try {
            String sql = "SELECT MASV FROM STUDENTS";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cboMasv.addItem(rs.getString("MASV"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean selectItem() {
        try {
            String sql = "SELECT * FROM STUDENTS WHERE MASV = ?";
            String ma = (String) cboMasv.getSelectedItem();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                txtMaSV.setText(ma);
                lblHotentxt.setText(rs.getString("Hoten"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public ArrayList<Grade> getListGrade() {
        try {
            String sql = "SELECT STUDENTS.MASV,Hoten,Tienganh,Tinhoc,GDTC,(Tienganh+Tinhoc+GDTC)/3 AS TBM FROM STUDENTS JOIN GRADE\n"
                    + " ON STUDENTS.MASV = GRADE.MASV ORDER BY TBM DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grade sv = new Grade();
                sv.setMasv(rs.getString("MASV"));
                sv.setName(rs.getString("Hoten"));
                sv.setAnh(Double.parseDouble(rs.getString("Tienganh")));
                sv.setTin(Double.parseDouble(rs.getString("Tinhoc")));
                sv.setGdtc(Double.parseDouble(rs.getString("GDTC")));
                sv.setAvg(Double.parseDouble(rs.getString("TBM")));
                list.add(sv);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public void fillTable() {
        model.setRowCount(0);
        for (Grade sv : list) {
            sv.masv = txtMaSV.getText();
            Object[] row = new Object[]{sv.getMasv(), sv.name, sv.getAnh(), sv.getTin(), sv.getGdtc(), sv.getAvg()};
            model.addRow(row);
        }
    }

    public void showDetail(int index) {
        lblHotentxt.setText(list.get(index).getName());
        txtMaSV.setText(list.get(index).getMasv());
        txtTienganh.setText(String.valueOf(list.get(index).getAnh()));
        txtTinhoc.setText(String.valueOf(list.get(index).getTin()));
        txtGDTC.setText(String.valueOf(list.get(index).getGdtc()));
        lblDiemTBtxt.setText(String.valueOf(list.get(index).getAvg()));
    }

    public void loadDbToTable() {
        try {
            model.setRowCount(0);
            String sql = "SELECT STUDENTS.MASV,Hoten,Tienganh,Tinhoc,GDTC,(Tienganh+Tinhoc+GDTC)/3 AS TBM FROM STUDENTS JOIN GRADE\n"
                    + " ON STUDENTS.MASV = GRADE.MASV ORDER BY TBM DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("MASV"));
                row.add(rs.getString("Hoten"));
                row.add(Double.parseDouble(rs.getString("Tienganh")));
                row.add(Double.parseDouble(rs.getString("Tinhoc")));
                row.add(Double.parseDouble(rs.getString("GDTC")));
                row.add(Double.parseDouble(rs.getString("TBM")));
                model.addRow(row);
            }
            tblBang.setModel(model);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean deleteGrade() {
        try {
            String ma = txtMaSV.getText();
            String sql = "DELETE FROM GRADE WHERE MASV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean updateGrade() {

        try {
            String ma = txtMaSV.getText();
            String sql = "UPDATE GRADE SET Tienganh = ?, Tinhoc = ?, GDTC = ? WHERE MASV = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(txtTienganh.getText()));
            ps.setDouble(2, Double.parseDouble(txtTinhoc.getText()));
            ps.setDouble(3, Double.parseDouble(txtGDTC.getText()));
            ps.setString(4, ma);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Print = new javax.swing.JMenuItem();
        Excel = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBang = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboMasv = new javax.swing.JComboBox<>();
        lblMaSV1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JLabel();
        lblHoten = new javax.swing.JLabel();
        lblHotentxt = new javax.swing.JLabel();
        lblMaSV2 = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        lblTienganh = new javax.swing.JLabel();
        txtTienganh = new javax.swing.JTextField();
        lblTinhoc = new javax.swing.JLabel();
        txtTinhoc = new javax.swing.JTextField();
        lblGDTC = new javax.swing.JLabel();
        txtGDTC = new javax.swing.JTextField();
        lblDiemTB = new javax.swing.JLabel();
        lblDiemTBtxt = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnback = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnnext = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnlast = new javax.swing.JLabel();

        Print.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_print_30px.png"))); // NOI18N
        Print.setText("Print");
        Print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PrintMousePressed(evt);
            }
        });
        jPopupMenu1.add(Print);

        Excel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Excel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_microsoft_excel_2019_30px.png"))); // NOI18N
        Excel.setText("Excel");
        Excel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ExcelMousePressed(evt);
            }
        });
        jPopupMenu1.add(Excel);

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1160, 610));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1160, 610));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNew.setBackground(new java.awt.Color(0, 204, 204));
        btnNew.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel1.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 109, 34));

        btnSave.setBackground(new java.awt.Color(0, 204, 204));
        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 109, 34));

        btnDelete.setBackground(new java.awt.Color(0, 204, 204));
        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 510, 109, 33));

        btnUpdate.setBackground(new java.awt.Color(0, 204, 204));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, 110, 36));

        tblBang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblBang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SV", "Họ tên", "Tiếng anh", "Tin học", "GDTC", "TBM"
            }
        ));
        tblBang.setFocusable(false);
        tblBang.setGridColor(new java.awt.Color(255, 255, 255));
        tblBang.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblBang.setRowHeight(30);
        tblBang.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblBang.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblBang.setShowVerticalLines(false);
        tblBang.getTableHeader().setReorderingAllowed(false);
        tblBang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBangMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblBangMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblBang);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 820, 570));

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));
        jPanel4.setPreferredSize(new java.awt.Dimension(1160, 48));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("QUẢN LÍ ĐIỂM SINH VIÊN");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 18, -1, -1));

        cboMasv.setBackground(new java.awt.Color(204, 204, 204));
        cboMasv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMasvActionPerformed(evt);
            }
        });
        jPanel4.add(cboMasv, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 208, -1));

        lblMaSV1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaSV1.setText("Mã SV:");
        jPanel4.add(lblMaSV1, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 17, -1, -1));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        btnSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSearch.setText("Search");
        btnSearch.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnSearchMouseMoved(evt);
            }
        });
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSearchMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 40));

        lblHoten.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHoten.setText("Họ tên SV:");
        jPanel1.add(lblHoten, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        lblHotentxt.setBackground(new java.awt.Color(204, 204, 204));
        lblHotentxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(lblHotentxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 160, 21));

        lblMaSV2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaSV2.setText("Mã SV:");
        jPanel1.add(lblMaSV2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        txtMaSV.setEditable(false);
        txtMaSV.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtMaSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 129, -1));

        lblTienganh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTienganh.setText("Tiếng anh:");
        jPanel1.add(lblTienganh, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        txtTienganh.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtTienganh, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 130, -1));

        lblTinhoc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTinhoc.setText("Tin học:");
        jPanel1.add(lblTinhoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        txtTinhoc.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtTinhoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 130, -1));

        lblGDTC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGDTC.setText("Giáo dục TC:");
        jPanel1.add(lblGDTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        txtGDTC.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtGDTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 130, -1));

        lblDiemTB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiemTB.setText("Điểm TB:");
        jPanel1.add(lblDiemTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        lblDiemTBtxt.setBackground(new java.awt.Color(204, 204, 204));
        lblDiemTBtxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(lblDiemTBtxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 51, 20));

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        btnFirst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_rewind_32px.png"))); // NOI18N
        btnFirst.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnFirstMouseMoved(evt);
            }
        });
        btnFirst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFirstMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnFirstMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, 70, 40));

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        btnback.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_back_to_32px.png"))); // NOI18N
        btnback.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnbackMouseMoved(evt);
            }
        });
        btnback.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnbackMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnbackMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnback, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnback, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 70, 40));

        jPanel6.setBackground(new java.awt.Color(0, 204, 204));

        btnnext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnnext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_next_page_32px.png"))); // NOI18N
        btnnext.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnnextMouseMoved(evt);
            }
        });
        btnnext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnnextMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnnextMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnnext, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnnext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, -1, -1));

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));

        btnlast.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnlast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_fast_forward_32px.png"))); // NOI18N
        btnlast.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnlastMouseMoved(evt);
            }
        });
        btnlast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnlastMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnlastMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnlast, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnlast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 300, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1181, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        cboMasv.setSelectedIndex(0);
        lblHotentxt.setText(null);
        txtMaSV.setText(null);
        txtTienganh.setText(null);
        txtTinhoc.setText(null);
        txtGDTC.setText(null);
        lblDiemTBtxt.setText(null);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Grade sv = new Grade();
        sv.name = lblHotentxt.getText();
        sv.anh = Double.parseDouble(txtTienganh.getText());
        sv.tin = Double.parseDouble(txtTinhoc.getText());
        sv.gdtc = Double.parseDouble(txtGDTC.getText());
        lblDiemTBtxt.setText(String.valueOf(sv.getAvg()));
        list.add(sv);
        try {
            String sql = "INSERT INTO GRADE(MASV, Tienganh, Tinhoc, GDTC) VALUES ( ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtMaSV.getText());
            ps.setDouble(2, sv.anh);
            ps.setDouble(3, sv.tin);
            ps.setDouble(4, sv.gdtc);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(rootPane, "Save successfully!");
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDbToTable();

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        index = tblBang.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(rootPane, "You have not selected row to delete");
        } else {
            this.deleteGrade();
            list.remove(index);
            fillTable();
            this.btnNewActionPerformed(evt);
            loadDbToTable();
            JOptionPane.showMessageDialog(rootPane, "Deleted successfully!");
        }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        index = tblBang.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "You have not selected row to update");
        } else {
            list.remove(index);
            this.updateGrade();
            Grade sv = new Grade();
            sv.name = lblHotentxt.getText();
            sv.anh = Double.parseDouble(txtTienganh.getText());
            sv.tin = Double.parseDouble(txtTinhoc.getText());
            sv.gdtc = Double.parseDouble(txtGDTC.getText());
            lblDiemTBtxt.setText(String.valueOf(sv.getAvg()));
            list.add(sv);

            loadDbToTable();
            JOptionPane.showMessageDialog(rootPane, "Update successful!");

        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void cboMasvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMasvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMasvActionPerformed

    private void btnSearchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMousePressed
        this.selectItem();
    }//GEN-LAST:event_btnSearchMousePressed

    private void btnFirstMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirstMousePressed
        showDetail(0);
    }//GEN-LAST:event_btnFirstMousePressed

    private void btnbackMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbackMousePressed
        index--;
        if (index < 0) {
            index = 0;
        }
        showDetail(index);
    }//GEN-LAST:event_btnbackMousePressed

    private void btnnextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnnextMousePressed
        index++;
        if (index >= getListGrade().size()) {
            index = getListGrade().size() - 1;
        }
        showDetail(index);
    }//GEN-LAST:event_btnnextMousePressed

    private void btnlastMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlastMousePressed
        index = getListGrade().size() - 1;
        showDetail(index);
    }//GEN-LAST:event_btnlastMousePressed

    private void btnFirstMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirstMouseMoved
        jPanel3.setBackground(new Color(0, 255, 255));
    }//GEN-LAST:event_btnFirstMouseMoved

    private void btnFirstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirstMouseExited
        jPanel3.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnFirstMouseExited

    private void btnbackMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbackMouseMoved
        jPanel5.setBackground(new Color(0, 255, 255));
    }//GEN-LAST:event_btnbackMouseMoved

    private void btnbackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbackMouseExited
        jPanel5.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnbackMouseExited

    private void btnnextMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnnextMouseMoved
        jPanel6.setBackground(new Color(0, 255, 255));
    }//GEN-LAST:event_btnnextMouseMoved

    private void btnnextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnnextMouseExited
        jPanel6.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnnextMouseExited

    private void btnlastMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlastMouseMoved
        jPanel7.setBackground(new Color(0, 255, 255));
    }//GEN-LAST:event_btnlastMouseMoved

    private void btnlastMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnlastMouseExited
        jPanel7.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnlastMouseExited

    private void btnSearchMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseMoved
        jPanel2.setBackground(new Color(0, 255, 255));
    }//GEN-LAST:event_btnSearchMouseMoved

    private void btnSearchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseExited
        jPanel2.setBackground(new Color(0, 153, 153));
    }//GEN-LAST:event_btnSearchMouseExited

    private void tblBangMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMousePressed
        try {
            index = tblBang.getSelectedRow();
            this.showDetail(index);
        } catch (Exception e) {
        }

    }//GEN-LAST:event_tblBangMousePressed

    private void tblBangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMouseReleased
        if (evt.isPopupTrigger()) {
            jPopupMenu1.show(tblBang, evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_tblBangMouseReleased

    private void PrintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PrintMousePressed
        MessageFormat header = new MessageFormat("Score management board");
        MessageFormat footer = new MessageFormat("Page(0, number, interger)");

        try {
            tblBang.print(JTable.PrintMode.NORMAL, header, footer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_PrintMousePressed

    private void ExcelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExcelMousePressed
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Student list");
            XSSFRow row = null;
            XSSFCell cell = null;
            row = sheet.createRow(2);
            row.setHeight((short) 500);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Student list");
            row = sheet.createRow(3);
            row.setHeight((short) 500);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("MaSV");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Họ và Tên");
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Tiếng anh");
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Tin học");
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("GDTC");
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Điểm TB");
            ArrayList<Grade> exportToExcel = new ArrayList<>();
            exportToExcel = list;
            for (int i = 0; i < exportToExcel.size(); i++) {
                Grade grade = exportToExcel.get(i);
                row = sheet.createRow((short) 4+i);
                row.setHeight((short) 500);
                row.createCell(0).setCellValue(grade.getMasv());
                row.createCell(1).setCellValue(grade.getName());
                row.createCell(2).setCellValue(grade.getAnh());
                row.createCell(3).setCellValue(grade.getTin());
                row.createCell(4).setCellValue(grade.getGdtc());
                row.createCell(5).setCellValue(grade.getAvg());
            }
            FileOutputStream fos = new FileOutputStream(new File("src\\Excel\\listDiemSV.xlsx"));
            workbook.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(null, "File export success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_ExcelMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Excel;
    private javax.swing.JMenuItem Print;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel btnFirst;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel btnback;
    private javax.swing.JLabel btnlast;
    private javax.swing.JLabel btnnext;
    private javax.swing.JComboBox<String> cboMasv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDiemTB;
    private javax.swing.JLabel lblDiemTBtxt;
    private javax.swing.JLabel lblGDTC;
    private javax.swing.JLabel lblHoten;
    private javax.swing.JLabel lblHotentxt;
    private javax.swing.JLabel lblMaSV1;
    private javax.swing.JLabel lblMaSV2;
    private javax.swing.JLabel lblTienganh;
    private javax.swing.JLabel lblTinhoc;
    private javax.swing.JTable tblBang;
    private javax.swing.JTextField txtGDTC;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtTienganh;
    private javax.swing.JTextField txtTinhoc;
    // End of variables declaration//GEN-END:variables
}
