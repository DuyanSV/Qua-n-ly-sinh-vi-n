/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import Model.Student;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
public class StudenManager_Jint extends javax.swing.JInternalFrame {

    ArrayList<Student> list = new ArrayList<>();
    DefaultTableModel model;
    int index = 0;
    private Connection conn;
    String imageName = null;

    public StudenManager_Jint() {
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

        rdoNam.setSelected(true);
        ButtonGroup groupGender = new ButtonGroup();
        groupGender.add(rdoNam);
        groupGender.add(rdoNu);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLSV_FPT;user=sa;password=123");
        } catch (Exception e) {
            System.out.println(e);
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        list = getListStudent();
        model = (DefaultTableModel) tblBang.getModel();
        loadDbToTable();
        fillTable();
    }

    public void upImage(String imageName) {
        ImageIcon icon = new ImageIcon("src\\Image\\" + imageName);
        Image image = icon.getImage();
        ImageIcon icon1 = new ImageIcon(image.getScaledInstance(lblimage.getWidth(), lblimage.getHeight(), image.SCALE_SMOOTH));
        lblimage.setIcon(icon1);
    }

    public boolean check() {
        if (txtMaSV.getText().equals("") || txtHoten.getText().equals("") || txtEmail.getText().equals("")
                || txtSdt.getText().equals("") || txtDiachi.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Please enter enough data!");
            return false;
        } else if (!(txtEmail.getText()).matches("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
            JOptionPane.showMessageDialog(rootPane, "Wrong email format!");
            txtEmail.requestFocus();
            return false;
        } else if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "You haven't chosen a gender");
            return false;
        }
        return true;
    }

    public void loadDbToTable() {
        try {
            model.setRowCount(0);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM STUDENTS ORDER BY MASV ASC");
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getBoolean(5));
                row.add(rs.getString(6));
                row.add(rs.getString(7));
                model.addRow(row);
            }
            tblBang.setModel(model);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fillTable() {
        model.setRowCount(0);
        for (Student s : list) {
            Object[] row = new Object[]{s.getMasv(), s.getName(), s.getEmail(), s.getPhone(), s.isGender() ? "Nam" : "Nữ", s.getAddress(), s.getImage()};
            model.addRow(row);
        }
    }

    public void showDetail(int index) {
        txtMaSV.setText(list.get(index).getMasv());
        txtHoten.setText(list.get(index).getName());
        txtEmail.setText(list.get(index).getEmail());
        txtSdt.setText(list.get(index).getPhone());
        if (list.get(index).isGender() == true) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
        txtDiachi.setText(list.get(index).getAddress());
        upImage(list.get(index).getImage());
    }

    public boolean deleteStudent() {
        try {
            String ma = txtMaSV.getText();
            String sql = "DELETE FROM STUDENTS WHERE MASV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean updateStudent() {
        try {
            String ma = txtMaSV.getText();
            String sql = "UPDATE STUDENTS SET Hoten = ?, Email = ?, SoDT = ?, GioiTinh = ?, DiaChi = ?, Hinh = ? WHERE MASV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtHoten.getText());
            ps.setString(2, txtEmail.getText());
            ps.setString(3, txtSdt.getText());
            boolean gt;
            if (rdoNam.isSelected()) {
                gt = true;
            } else {
                gt = false;
            }
            ps.setBoolean(4, gt);
            ps.setString(5, txtDiachi.getText());
            ps.setString(6, imageName);
            ps.setString(7, txtMaSV.getText());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean saveStudent(Student sv) {
        String sql = "INSERT INTO STUDENTS VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sv.getMasv());
            ps.setString(2, sv.getName());
            ps.setString(3, sv.getEmail());
            ps.setString(4, sv.getPhone());
            ps.setBoolean(5, sv.isGender());
            ps.setString(6, sv.getAddress());
            ps.setString(7, imageName);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public ArrayList<Student> getListStudent() {

        String sql = "SELECT * FROM STUDENTS";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student sv = new Student();
                sv.setMasv(rs.getString(1));
                sv.setName(rs.getString(2));
                sv.setEmail(rs.getString(3));
                sv.setPhone(rs.getString(4));
                sv.setGender(rs.getBoolean(5));
                sv.setAddress(rs.getString(6));
                sv.setImage(rs.getString(7));
                list.add(sv);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Print = new javax.swing.JMenuItem();
        Excel = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        txtSdt = new javax.swing.JTextField();
        lblimage = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        lblMaSV = new javax.swing.JLabel();
        rdoNu = new javax.swing.JRadioButton();
        lblHoten = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiachi = new javax.swing.JTextArea();
        lblEmail = new javax.swing.JLabel();
        lblSdt = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBang = new javax.swing.JTable();
        lblGioitinh = new javax.swing.JLabel();
        lblDiachi = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        txtHoten = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        txtEmail = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

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
        jPanel1.setPreferredSize(new java.awt.Dimension(1160, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUpdate.setBackground(new java.awt.Color(0, 204, 204));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 105, 41));

        txtSdt.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtSdt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 170, -1));

        lblimage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblimage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblimageMousePressed(evt);
            }
        });
        jPanel1.add(lblimage, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 170, 140));

        rdoNam.setText("Nam");
        jPanel1.add(rdoNam, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, -1, -1));

        lblMaSV.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaSV.setText("MaSV:");
        jPanel1.add(lblMaSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, -1, -1));

        rdoNu.setText("Nữ");
        jPanel1.add(rdoNu, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 380, -1, -1));

        lblHoten.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHoten.setText("Họ Tên:");
        jPanel1.add(lblHoten, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, -1, -1));

        txtDiachi.setBackground(new java.awt.Color(204, 204, 204));
        txtDiachi.setColumns(20);
        txtDiachi.setLineWrap(true);
        txtDiachi.setRows(5);
        txtDiachi.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setViewportView(txtDiachi);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 480, 170, 100));

        lblEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEmail.setText("Email:");
        jPanel1.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, -1, -1));

        lblSdt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSdt.setText("Số ĐT:");
        jPanel1.add(lblSdt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, -1, -1));

        tblBang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblBang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MaSV", "Họ và tên", "Email", "Số ĐT", "Giới tính", "Địa chỉ", "Ảnh"
            }
        ));
        tblBang.setFocusable(false);
        tblBang.setGridColor(new java.awt.Color(255, 255, 255));
        tblBang.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblBang.setRequestFocusEnabled(false);
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
        jScrollPane2.setViewportView(tblBang);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 810, 570));

        lblGioitinh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGioitinh.setText("Giới tính:");
        jPanel1.add(lblGioitinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        lblDiachi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiachi.setText("Địa chỉ:");
        jPanel1.add(lblDiachi, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 460, -1, -1));

        txtMaSV.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtMaSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 170, -1));

        btnSave.setBackground(new java.awt.Color(0, 204, 204));
        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 105, 41));

        txtHoten.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtHoten, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 170, -1));

        btnDelete.setBackground(new java.awt.Color(0, 204, 204));
        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 105, 41));

        txtEmail.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 170, -1));

        btnNew.setBackground(new java.awt.Color(0, 204, 204));
        btnNew.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel1.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 105, 41));

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 204));
        jLabel1.setText("QUẢN LÍ SINH VIÊN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(1025, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1180, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        index = tblBang.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(rootPane, "You have not selected row to update!");
        } else {
            list.remove(index);
            this.updateStudent();
            Student sv = new Student();
            sv.setMasv(txtMaSV.getText());
            sv.setName(txtHoten.getText());
            sv.setEmail(txtEmail.getText());
            sv.setPhone(txtSdt.getText());
            boolean gt;
            if (rdoNam.isSelected()) {
                gt = true;
            } else {
                gt = false;
            }
            sv.setGender(gt);
            sv.setAddress(txtDiachi.getText());
            sv.setImage(imageName);
            list.add(sv);
            fillTable();
            JOptionPane.showMessageDialog(this, "Update successful!");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        txtMaSV.setText("");
        txtHoten.setText("");
        txtEmail.setText("");
        txtSdt.setText("");
        txtDiachi.setText("");
        rdoNam.setSelected(true);
        lblimage.setIcon(null);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (check()) {
            Student sv = new Student();
            sv.setMasv(txtMaSV.getText());
            sv.setName(txtHoten.getText());
            sv.setEmail(txtEmail.getText());
            sv.setPhone(txtSdt.getText());
            boolean gt;
            if (rdoNam.isSelected()) {
                gt = true;
            } else {
                gt = false;
            }
            sv.setGender(gt);
            sv.setAddress(txtDiachi.getText());
            sv.setImage(imageName);
            if (saveStudent(sv)) {
                JOptionPane.showMessageDialog(rootPane, "Save successfully!");
                list.add(sv);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error");
            }
            fillTable();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        index = tblBang.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(rootPane, "You have not selected the row to delete!");
        } else {
            this.deleteStudent();
            list.remove(index);
            loadDbToTable();
            this.btnNewActionPerformed(evt);
            JOptionPane.showMessageDialog(rootPane, "Delete successfully!");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void lblimageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblimageMousePressed
        JFileChooser file = new JFileChooser("src\\Image\\");
        int kq = file.showOpenDialog(file);
        if (kq == JFileChooser.APPROVE_OPTION) {
            imageName = file.getSelectedFile().getName();
            upImage(imageName);
        } else {
            JOptionPane.showMessageDialog(rootPane, "You haven't selected a photo...");
        }
    }//GEN-LAST:event_lblimageMousePressed

    private void tblBangMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMousePressed
        try {
            index = tblBang.getSelectedRow();
            showDetail(index);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblBangMousePressed

    private void tblBangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMouseReleased
        if (evt.isPopupTrigger()) {
            jPopupMenu1.show(tblBang, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tblBangMouseReleased

    private void PrintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PrintMousePressed
        MessageFormat header = new MessageFormat("Student management board");
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
            cell.setCellValue("Email");
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Số ĐT");
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Giới tính");
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Địa chỉ");
            ArrayList<Student> exportToExcel = new ArrayList<>();
            exportToExcel = list;
            for (int i = 0; i < exportToExcel.size(); i++) {
                Student student = exportToExcel.get(i);
                row = sheet.createRow((short) 4+i);
                row.setHeight((short) 500);
                row.createCell(0).setCellValue(student.getMasv());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getEmail());
                row.createCell(3).setCellValue(student.getPhone());
                row.createCell(4).setCellValue(student.isGender()?"Nam":"Nữ");
                row.createCell(5).setCellValue(student.getAddress());
            }
            FileOutputStream fos = new FileOutputStream(new File("src\\Excel\\listSV.xlsx"));
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
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDiachi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioitinh;
    private javax.swing.JLabel lblHoten;
    private javax.swing.JLabel lblMaSV;
    private javax.swing.JLabel lblSdt;
    private javax.swing.JLabel lblimage;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblBang;
    private javax.swing.JTextArea txtDiachi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtSdt;
    // End of variables declaration//GEN-END:variables
}
