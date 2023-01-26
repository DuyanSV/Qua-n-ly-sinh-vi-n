/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author DUYAN
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        this.lab3.setVisible(false);
        this.lab4.setVisible(false);
        this.lab5.setVisible(false);
        this.btnsignup.setVisible(false);
        this.cboRole.setVisible(false);
        this.lblRole.setVisible(false);
        setLocationRelativeTo(null);
        txtPass.setEchoChar((char) 0);
        this.btnShowPass1.setVisible(false);

    }

    public boolean check() {
        if (txtUser.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty");
            txtUser.requestFocus();
            return false;
        } else if (txtPass.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty");
            txtPass.requestFocus();
            return false;
        } else if (txtUser.getText().equals("Username") || txtPass.getText().equals("Password")) {
            JOptionPane.showMessageDialog(this, "This account is invalid!");
            txtPass.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnExit = new javax.swing.JLabel();
        btnsignup = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        cboRole = new javax.swing.JComboBox<>();
        lab4 = new javax.swing.JLabel();
        lab5 = new javax.swing.JLabel();
        btnShowPass1 = new javax.swing.JLabel();
        lab3 = new javax.swing.JLabel();
        panel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtUser = new javax.swing.JTextField();
        txtPass = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        login = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lab8 = new javax.swing.JLabel();
        lab7 = new javax.swing.JLabel();
        btnShowPass = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSignIn = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSIGNUP = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(250, 100));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_sign_out_50px.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnExitMousePressed(evt);
            }
        });
        jPanel1.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 0, 50, 40));

        btnsignup.setBackground(new java.awt.Color(51, 51, 51));
        btnsignup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnsignup.setForeground(new java.awt.Color(255, 255, 255));
        btnsignup.setText("                           Sign up");
        btnsignup.setToolTipText("");
        btnsignup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnsignupMousePressed(evt);
            }
        });
        jPanel1.add(btnsignup, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 380, 280, 50));

        lblRole.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblRole.setForeground(new java.awt.Color(255, 255, 255));
        lblRole.setText("Role:");
        jPanel1.add(lblRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, -1, -1));

        cboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ðào tạo", "Giảng viên" }));
        jPanel1.add(cboRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 350, 100, -1));

        lab4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_checked_user_male_32px.png"))); // NOI18N
        lab4.setText("jLabel3");
        jPanel1.add(lab4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 330, 40, 50));

        lab5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_New_Post_32px.png"))); // NOI18N
        lab5.setText("jLabel3");
        jPanel1.add(lab5, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 220, 40, 50));

        btnShowPass1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_Invisible_23px.png"))); // NOI18N
        btnShowPass1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowPass1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnShowPass1MousePressed(evt);
            }
        });
        jPanel1.add(btnShowPass1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, -1, -1));

        lab3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lab3.setForeground(new java.awt.Color(255, 255, 255));
        lab3.setText("Sign up");
        jPanel1.add(lab3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, 150, 60));

        panel1.setBackground(new java.awt.Color(51, 51, 51));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(102, 204, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        panel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 130, 10));

        txtUser.setBackground(new java.awt.Color(51, 51, 51));
        txtUser.setForeground(new java.awt.Color(255, 255, 255));
        txtUser.setText("Username");
        txtUser.setBorder(null);
        txtUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtUserMousePressed(evt);
            }
        });
        panel1.add(txtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 230, 30));

        txtPass.setBackground(new java.awt.Color(51, 51, 51));
        txtPass.setForeground(new java.awt.Color(255, 255, 255));
        txtPass.setText("Password");
        txtPass.setToolTipText("");
        txtPass.setBorder(null);
        txtPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtPassMousePressed(evt);
            }
        });
        txtPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassKeyPressed(evt);
            }
        });
        panel1.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 230, 20));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_Lock_32px.png"))); // NOI18N
        panel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, 50));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 204, 255)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        login.setBackground(new java.awt.Color(51, 51, 51));
        login.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setText("                              Login");
        login.setToolTipText("");
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });
        jPanel5.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 50));

        panel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 280, 50));
        panel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 230, 20));
        panel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 230, 10));

        lab8.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lab8.setForeground(new java.awt.Color(255, 255, 255));
        lab8.setText("Sign in");
        panel1.add(lab8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 50));

        lab7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_User_35px_1.png"))); // NOI18N
        lab7.setText("jLabel3");
        panel1.add(lab7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 40, 50));

        btnShowPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_Invisible_23px.png"))); // NOI18N
        btnShowPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowPassMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnShowPassMousePressed(evt);
            }
        });
        panel1.add(btnShowPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        jPanel1.add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 320, 370));

        jPanel2.setBackground(new java.awt.Color(51, 204, 255));

        lblSignIn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblSignIn.setForeground(new java.awt.Color(51, 51, 51));
        lblSignIn.setText("     Sign in");
        lblSignIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblSignInMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSignIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSignIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 110, 40));

        jPanel3.setBackground(new java.awt.Color(51, 204, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSIGNUP.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSIGNUP.setForeground(new java.awt.Color(51, 51, 51));
        btnSIGNUP.setText("    Sign up");
        btnSIGNUP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSIGNUPMousePressed(evt);
            }
        });
        jPanel3.add(btnSIGNUP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 240, 110, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Hình-nền-thiên-nhiên-Nhật.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 530));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSIGNUPMousePressed(MouseEvent evt) {//GEN-FIRST:event_btnSIGNUPMousePressed
        int p = this.panel1.getX();
        if (p > -1) {
            Animacion.Animacion.mover_derecha(117, 510, 2, 3, panel1);
        }
        login.setVisible(false);
        lab7.setVisible(false);
        lab8.setVisible(false);
        lab3.setVisible(true);
        lab4.setVisible(true);
        lab5.setVisible(true);
        cboRole.setVisible(true);
        lblRole.setVisible(true);
        btnsignup.setVisible(true);
        btnShowPass.setVisible(false);
        btnShowPass1.setVisible(true);

    }//GEN-LAST:event_btnSIGNUPMousePressed

    private void lblSignInMousePressed(MouseEvent evt) {//GEN-FIRST:event_lblSignInMousePressed
        int p = this.panel1.getX();
        if (p > -1) {
            Animacion.Animacion.mover_izquierda(510, 117, 2, 3, panel1);
        }
        lab3.setVisible(false);
        lab4.setVisible(false);
        btnsignup.setVisible(false);
        login.setVisible(true);
        lab7.setVisible(true);
        lab8.setVisible(true);
        cboRole.setVisible(false);
        lab5.setVisible(false);
        lblRole.setVisible(false);
        btnShowPass.setVisible(true);
        btnShowPass1.setVisible(false);// TODO add your handling code here:
    }//GEN-LAST:event_lblSignInMousePressed

    private void btnExitMousePressed(MouseEvent evt) {//GEN-FIRST:event_btnExitMousePressed
        int ret = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ret == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
        }
    }//GEN-LAST:event_btnExitMousePressed

    private void txtUserMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserMousePressed
        txtUser.setText("");
    }//GEN-LAST:event_txtUserMousePressed

    private void txtPassMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPassMousePressed
        txtPass.setText("");
        txtPass.setEchoChar('\u25cf');
    }//GEN-LAST:event_txtPassMousePressed

    private void btnShowPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowPassMouseClicked
        txtPass.setEchoChar((char) 0);
    }//GEN-LAST:event_btnShowPassMouseClicked

    private void btnShowPassMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowPassMousePressed
        txtPass.setEchoChar('\u25cf');
    }//GEN-LAST:event_btnShowPassMousePressed

    private void btnShowPass1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowPass1MouseClicked
        txtPass.setEchoChar((char) 0);
    }//GEN-LAST:event_btnShowPass1MouseClicked

    private void btnShowPass1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowPass1MousePressed
        txtPass.setEchoChar('\u25cf');
    }//GEN-LAST:event_btnShowPass1MousePressed

    private void loginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginMousePressed
        if (check()) {
            try {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV_FPT;user=sa;password=123";
                Connection con = DriverManager.getConnection(url);
                String sql = "SELECT * FROM USERS WHERE usernames = ? and passwords = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtUser.getText());
                ps.setString(2, txtPass.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String role = rs.getString(3);
                    if (role.equals("Ðào tạo")) {
                        JOptionPane.showMessageDialog(rootPane, "Đăng nhập thành công");
                        new QLSV().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Đăng nhập thành công");
                        new QuanLyDiem().setVisible(true);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed!Wrong username or password");
                }
            } catch (HeadlessException | SQLException e) {
                System.out.println(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login failed!This account is invalid!");
        }
    }//GEN-LAST:event_loginMousePressed

    private void btnsignupMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnsignupMousePressed
        if (check()) {
            try {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV_FPT;user=sa;password=123";
                Connection con = DriverManager.getConnection(url);

                String sql = "INSERT INTO USERS values(?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, txtUser.getText());
                pst.setString(2, txtPass.getText());
                pst.setString(3, cboRole.getSelectedItem().toString());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Sign Up successfully");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Account used!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sign Up failed!Wrong username or password");
        }
    }//GEN-LAST:event_btnsignupMousePressed

    private void txtPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (check()) {
                try {
                    String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV_FPT;user=sa;password=123";
                    Connection con = DriverManager.getConnection(url);
                    String sql = "SELECT * FROM USERS WHERE usernames = ? and passwords = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, txtUser.getText());
                    ps.setString(2, txtPass.getText());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String role = rs.getString(3);
                        if (role.equals("Ðào tạo")) {
                            JOptionPane.showMessageDialog(rootPane, "Đăng nhập thành công");
                            new QLSV().setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Đăng nhập thành công");
                            new QuanLyDiem().setVisible(true);
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Login failed!Wrong username or password");
                    }
                } catch (HeadlessException | SQLException e) {
                    System.out.println(e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login failed!This account is invalid!");
            }
        }
    }//GEN-LAST:event_txtPassKeyPressed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnExit;
    private javax.swing.JLabel btnSIGNUP;
    private javax.swing.JLabel btnShowPass;
    private javax.swing.JLabel btnShowPass1;
    private javax.swing.JLabel btnsignup;
    private javax.swing.JComboBox<String> cboRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lab3;
    private javax.swing.JLabel lab4;
    private javax.swing.JLabel lab5;
    private javax.swing.JLabel lab7;
    private javax.swing.JLabel lab8;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSignIn;
    private javax.swing.JLabel login;
    private javax.swing.JPanel panel1;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
