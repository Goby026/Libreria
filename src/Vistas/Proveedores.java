/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Control.Conexion;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gaby
 */
public final class Proveedores extends javax.swing.JFrame {
    
     int posx, posy;
    
    DefaultTableModel proveedor_model;
    
    public Proveedores() {
        setUndecorated(true);
        initComponents();
        this.getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        //camposTransparentes();
        btn_actualizar.setVisible(false);
        titulos();
        cargarTabla();
        bloquear();
    }
    
    public void camposTransparentes(){
        //txtUsuario.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        //txtPass.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        Color c = new Color(0,0,1,0.06f);
        txt_razon.setBackground(c);
        txt_direccion.setBackground(c);
        txt_ruc.setBackground(c);
        txt_telf.setBackground(c);
        txt_ciudad.setBackground(c);
        txt_email.setBackground(c);
        txt_contacto.setBackground(c);
        txt_web.setBackground(c);
    }
    public void reg_provee(){
        String razon  = txt_razon.getText();
        String ruc = txt_ruc.getText();
        String dir  = txt_direccion.getText();
        String ciudad = txt_ciudad.getText();
        int telf = Integer.parseInt(txt_telf.getText());
        String contacto = txt_contacto.getText();
        String web = "-";
        web = txt_web.getText();
        String email ="-";
        email = txt_email.getText();
        Conexion c = new Conexion();
        Connection cn = c.conectar();
        String sql = "INSERT INTO tproveedor(nom_provee, ruc_provee, dir_provee, telf_provee, ciudad_provee, contacto_provee,email_provee, web_provee) VALUES ('"+razon+"',"+ruc+",'"+dir+"',"+telf+",'"+ciudad+"','"+contacto+"','"+email+"','"+web+"')";
        try {
            Statement st = cn.createStatement();
            int rs = st.executeUpdate(sql);
            if (rs > 0) {
            //JOptionPane.showMessageDialog(null, "USUARIO REGISTRADO CON EXITO");
                //0=si------1=no------2=cancelar
                limpiarTabla();
                cargarTabla();
               
       
                int opc = JOptionPane.showOptionDialog(btn_nuevo, "PROVEEDOR REGISTRADO, ¿DESEA REGISTRAR MAS PROVEEDORES?", "showOptionDialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"SI", "NO"}, "SI");
                if (opc == 0) {
                    limpiar();

                } else if (opc == 1) {
                   this.dispose();
                }
            }
            
            
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }  
    }
    public void bloquear(){
        txt_razon.setEnabled(false);
        txt_ruc.setEnabled(false);
        txt_direccion.setEnabled(false);
        txt_ciudad.setEnabled(false);
        txt_telf.setEnabled(false);
        txt_contacto.setEnabled(false);
        txt_email.setEnabled(false);
        txt_web.setEnabled(false);
        
    }
    public void desbloquear(){
        txt_razon.setEnabled(true);
        txt_ruc.setEnabled(true);
        txt_direccion.setEnabled(true);
        txt_ciudad.setEnabled(true);
        txt_telf.setEnabled(true);
        txt_contacto.setEnabled(true);
        txt_email.setEnabled(true);
        txt_web.setEnabled(true);
        
        
    }
    public void cargarTabla(){
        Conexion con = new Conexion();
        Connection cc = con.conectar();
        String[] datos= new String[5];
        String sql = "SELECT id_provee, nom_provee, ruc_provee, dir_provee, telf_provee FROM tproveedor ";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString("id_provee");
                datos[1] = rs.getString("nom_provee");
                datos[2] = rs.getString("ruc_provee");
                datos[3] = rs.getString("dir_provee");
                datos[4] = rs.getString("telf_provee");
                //datos[5] = rs.getString("direc_cli");
                //datos[5] = rs.getString("email_cli");
                proveedor_model.addRow(datos);
                tbl_proveedor.setModel(proveedor_model);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    public void titulos(){
        String titulos[] = {"CODIGO", "NOMBRE", "RUC", "DIRECCION", "TELEFONO"};
        proveedor_model = new DefaultTableModel(null, titulos);
        tbl_proveedor.setModel(proveedor_model);
    }
    public int validar_campos() {
        if (!txt_razon.getText().trim().isEmpty()) {
            txt_razon.transferFocus();
            if (!txt_ruc.getText().trim().isEmpty()) {
                txt_ruc.transferFocus();
                if (!txt_direccion.getText().trim().isEmpty()) {
                    txt_direccion.transferFocus();
                    if (!txt_ciudad.getText().trim().isEmpty()) {
                        txt_ciudad.transferFocus();
                        if (!txt_telf.getText().trim().isEmpty()) {
                              txt_telf.transferFocus();
                              if (!txt_contacto.getText().trim().isEmpty()) {
                                     txt_contacto.transferFocus();
                    return 1;
                } else {
                    JOptionPane.showMessageDialog(null, "INGRESE CONTACTO");
                    txt_contacto.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "INGRESE TELEFONO");
                txt_telf.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "INGRESE CUIDAD");
            txt_ciudad.requestFocus();
        }
                } else {
            JOptionPane.showMessageDialog(null, "INGRESE DIRECCION");
            txt_direccion.requestFocus();
          }
        }  else {
            JOptionPane.showMessageDialog(null, "INGRESE RUC");
            txt_ruc.requestFocus();
        } 
    }  else {
            JOptionPane.showMessageDialog(null, "INGRESE RAZON");
            txt_razon.requestFocus();
        }
        
        return 0;
    }

     public void limpiarTabla() {
        for (int i = 0; i < tbl_proveedor.getRowCount(); i++) {
            proveedor_model.removeRow(i);
            i -= 1;
        }
    }
    public void limpiar(){
        txt_razon.setText("");
        txt_ruc.setText("");
        txt_direccion.setText("");
        txt_ciudad.setText("");
        txt_telf.setText("");
        txt_contacto.setText("");
        txt_email.setText("");
        txt_web.setText("");
        txt_razon.requestFocus();
        
   
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        btn_eliminar = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        btn_nuevo = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        btn_actualizar = new javax.swing.JButton();
        btn_registrar = new javax.swing.JButton();
        btn_modificar = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_razon = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_direccion = new javax.swing.JTextField();
        txt_ruc = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_ciudad = new javax.swing.JTextField();
        txt_telf = new javax.swing.JTextField();
        txt_web = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_contacto = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_proveedor = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("RENALI DISTRIBUIDORES");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("MARTES 09 DE FEBRERO DEL 2016");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SALIR");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 90));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("REGISTRO DE PROVEEDORES");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        btn_eliminar.setBackground(new java.awt.Color(255, 0, 0));
        btn_eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_eliminarMouseClicked(evt);
            }
        });
        btn_eliminar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("ELIMINAR");
        jLabel23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_eliminar.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 20));

        jPanel2.add(btn_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 0, 110, 40));

        btn_nuevo.setBackground(new java.awt.Color(0, 51, 255));
        btn_nuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_nuevoMouseClicked(evt);
            }
        });
        btn_nuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setBackground(new java.awt.Color(51, 51, 255));
        jLabel26.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("NUEVO");
        jLabel26.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_nuevo.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 20));

        jPanel2.add(btn_nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 90, 40));

        btn_actualizar.setBackground(new java.awt.Color(255, 255, 51));
        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/1456175768_adept_update.png"))); // NOI18N
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 30, 40));

        btn_registrar.setBackground(new java.awt.Color(255, 51, 51));
        btn_registrar.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrar.setText("REGISTRAR");
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_registrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 110, 40));

        btn_modificar.setBackground(new java.awt.Color(255, 102, 0));
        btn_modificar.setForeground(new java.awt.Color(255, 255, 255));
        btn_modificar.setText("MODIFICAR");
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 100, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 640, 40));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("RUC");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        txt_razon.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_razon.setForeground(new java.awt.Color(102, 102, 102));
        txt_razon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_razon.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_razon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_razonActionPerformed(evt);
            }
        });
        txt_razon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_razonKeyTyped(evt);
            }
        });
        jPanel3.add(txt_razon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 330, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("NOMBRE");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("DIRECCIÓN");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        txt_direccion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_direccion.setForeground(new java.awt.Color(102, 102, 102));
        txt_direccion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_direccion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_direccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_direccionActionPerformed(evt);
            }
        });
        jPanel3.add(txt_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 620, -1));

        txt_ruc.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_ruc.setForeground(new java.awt.Color(102, 102, 102));
        txt_ruc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ruc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_ruc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_rucActionPerformed(evt);
            }
        });
        txt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_rucKeyTyped(evt);
            }
        });
        jPanel3.add(txt_ruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 30, 270, -1));

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 102));
        jLabel25.setText("CIUDAD");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 102));
        jLabel15.setText("TELEFONO ");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1, -1));

        txt_ciudad.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_ciudad.setForeground(new java.awt.Color(102, 102, 102));
        txt_ciudad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ciudad.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ciudadActionPerformed(evt);
            }
        });
        jPanel3.add(txt_ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 330, -1));

        txt_telf.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_telf.setForeground(new java.awt.Color(102, 102, 102));
        txt_telf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_telf.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_telf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telfActionPerformed(evt);
            }
        });
        txt_telf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telfKeyTyped(evt);
            }
        });
        jPanel3.add(txt_telf, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 270, -1));

        txt_web.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_web.setForeground(new java.awt.Color(102, 102, 102));
        txt_web.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_web.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_web.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_webActionPerformed(evt);
            }
        });
        jPanel3.add(txt_web, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, 270, -1));

        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 102));
        jLabel17.setText("CONTACTO");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 102));
        jLabel11.setText("E-MAIL");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 102));
        jLabel12.setText("PAG. WEB");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, -1, -1));

        txt_contacto.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_contacto.setForeground(new java.awt.Color(102, 102, 102));
        txt_contacto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_contacto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_contacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_contactoActionPerformed(evt);
            }
        });
        txt_contacto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_contactoKeyTyped(evt);
            }
        });
        jPanel3.add(txt_contacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 330, -1));

        txt_email.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_email.setForeground(new java.awt.Color(102, 102, 102));
        txt_email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_email.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });
        jPanel3.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 330, -1));

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("*");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 6, 10, 20));

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("*");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 20, -1));

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("*");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 10, 20));

        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 10, -1));

        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 10, -1));

        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 10, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 640, 270));

        tbl_proveedor.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_proveedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_proveedor);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 640, 180));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_eliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseClicked
        int row = tbl_proveedor.getSelectedRow();
        int cod = Integer.parseInt(tbl_proveedor.getValueAt(row,0).toString());
        Conexion c = new Conexion();
        Connection cn = c.conectar();
        String sql = "DELETE FROM `tproveedor` WHERE `id_provee` = "+cod+"";
        int opc = JOptionPane.showOptionDialog(btn_eliminar, "¿ESTA SEGURO QUE DESEA ELIMINAR ESTE REGISTRO?", "showOptionDialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"SI", "NO"}, "SI");
        if (opc == 0) {
            try {
                Statement st = cn.createStatement();
                int rs = st.executeUpdate(sql);

                if (rs > 0) {
                    limpiarTabla();
                    cargarTabla();
                    bloquear();
                    limpiar();

                } else {

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            JOptionPane.showMessageDialog(this, "REGISTRO ELIMINADO");
        }

        
       
    }//GEN-LAST:event_btn_eliminarMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        dispose();
        new Administracion().setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        posx = evt.getX();
        posy = evt.getY();  
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
       Point point = MouseInfo.getPointerInfo().getLocation();
       this.setLocation(point.x-posx, point.y-posy);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void txt_razonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_razonActionPerformed
        txt_razon.transferFocus();
    }//GEN-LAST:event_txt_razonActionPerformed

    private void txt_rucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_rucActionPerformed
       txt_ruc.transferFocus();;
    }//GEN-LAST:event_txt_rucActionPerformed

    private void txt_direccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_direccionActionPerformed
        txt_direccion.transferFocus();
    }//GEN-LAST:event_txt_direccionActionPerformed

    private void txt_ciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ciudadActionPerformed
      txt_ciudad.transferFocus();
    }//GEN-LAST:event_txt_ciudadActionPerformed

    private void txt_telfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telfActionPerformed
        txt_telf.transferFocus();
    }//GEN-LAST:event_txt_telfActionPerformed

    private void txt_contactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_contactoActionPerformed
      txt_contacto.transferFocus();
    }//GEN-LAST:event_txt_contactoActionPerformed

    private void txt_webActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_webActionPerformed
        txt_web.transferFocus();
    }//GEN-LAST:event_txt_webActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
      txt_email.transferFocus();
    }//GEN-LAST:event_txt_emailActionPerformed

    private void txt_razonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razonKeyTyped
        int tecla = (int)evt.getKeyChar();
        if(tecla>47 && tecla<58){
            evt.setKeyChar((char)KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO LETRAS");
            txt_razon.requestFocus();
            //txt_nombres.requestFocus();
        }
    }//GEN-LAST:event_txt_razonKeyTyped

    private void txt_rucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyTyped
       int tecla = (int) evt.getKeyChar();
        if (tecla > 64 && tecla < 91 || tecla > 96 && tecla < 123)  {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO NUMEROS");
            txt_ruc.requestFocus();

        } else {
            if (txt_ruc.getText().trim().length()==12) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txt_rucKeyTyped

    private void txt_telfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telfKeyTyped
     int tecla = (int) evt.getKeyChar();
        if (tecla > 64 && tecla < 91 || tecla > 96 && tecla < 123)  {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO NUMEROS");
            txt_ruc.requestFocus();
        }
    }//GEN-LAST:event_txt_telfKeyTyped

    private void txt_contactoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contactoKeyTyped
      int tecla = (int)evt.getKeyChar();
        if(tecla>47 && tecla<58){
            evt.setKeyChar((char)KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO LETRAS");
            txt_contacto.requestFocus();
            //txt_nombres.requestFocus();
        }
    }//GEN-LAST:event_txt_contactoKeyTyped

    private void btn_nuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_nuevoMouseClicked
                limpiar();
                desbloquear();
                btn_registrar.setEnabled(true);
                txt_razon.requestFocus(); 
              
    }//GEN-LAST:event_btn_nuevoMouseClicked

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
       if (validar_campos()!=0) {
            reg_provee();
            limpiar();
        }else{
            //comprobar();
        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        if (!txt_razon.getText().trim().isEmpty()) {
            String razon,ruc,dir,ciudad,contacto,email,web;
            int telf;
            razon = txt_razon.getText();
            ruc = txt_ruc.getText();
            dir = txt_direccion.getText();
            ciudad = txt_ciudad.getText();
            telf = Integer.parseInt(txt_telf.getText());
            contacto = txt_contacto.getText();
            email = txt_email.getText();
            web = txt_web.getText();
            int fila = tbl_proveedor.getSelectedRow();
            int cod = Integer.parseInt(tbl_proveedor.getValueAt(fila,0).toString());
            Conexion con = new Conexion();
            Connection cc = con.conectar();
            String sql = "UPDATE tproveedor SET nom_provee = '"+razon+"',ruc_provee = "+ruc+",dir_provee = '"+dir+"',telf_provee ="+telf+",ciudad_provee ='"+ciudad+"',contacto_provee = '"+contacto+"',email_provee ='"+email+"',web_provee = '"+web+"' WHERE id_provee  = "+cod+" ";
            try {
                Statement st = cc.createStatement();
                int rs = st.executeUpdate(sql);
                if(rs>0);
                limpiarTabla();
                cargarTabla();
                JOptionPane.showMessageDialog(null, "REGISTRO ACTUALIZADO");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
           
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UN CLIENTE");
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

    public void actualizar(){
        String razon  = txt_razon.getText();
        String ruc = txt_ruc.getText();
        String dir  = txt_direccion.getText();
        String ciudad = txt_ciudad.getText();
        int telf = Integer.parseInt(txt_telf.getText());
        String contacto = txt_contacto.getText();
        String web = "-";
        web = txt_web.getText();
        String email ="-";
        email = txt_email.getText();
        Conexion c = new Conexion();
        Connection cn = c.conectar();
        String sql = "INSERT INTO tproveedor(nom_provee, ruc_provee, dir_provee, telf_provee, ciudad_provee, contacto_provee,email_provee, web_provee) VALUES ('"+razon+"',"+ruc+",'"+dir+"',"+telf+",'"+ciudad+"','"+contacto+"','"+email+"','"+web+"')";
        try {
            Statement st = cn.createStatement();
            int rs = st.executeUpdate(sql);
            if (rs > 0) {
            JOptionPane.showMessageDialog(null, "REGISTRO ACTUALIZADO");
            }
        }catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
      if (btn_modificar.isSelected()) {
            btn_modificar.setBackground(Color.green);
            btn_actualizar.setVisible(true);
            desbloquear();
            txt_razon.requestFocus();
            
            
        } else {
            btn_modificar.setBackground(Color.orange);
            btn_actualizar.setVisible(false);
            bloquear();
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void tbl_proveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_proveedorMouseClicked
        btn_registrar.setEnabled(false);
        int fila = tbl_proveedor.getSelectedRow();
        int id = Integer.parseInt(tbl_proveedor.getValueAt(fila, 0).toString());
        capturar_mostrar_datos(id);
        
    }//GEN-LAST:event_tbl_proveedorMouseClicked
    public void capturar_mostrar_datos(int cod){
       Connection cc = new Conexion().conectar();
       String[] datos = new String[8];
       String sql = "SELECT  nom_provee, ruc_provee, dir_provee, telf_provee, ciudad_provee, contacto_provee, email_provee, web_provee FROM tproveedor  WHERE id_provee= "+cod+"";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                txt_razon.setText(rs.getString("nom_provee"));
                txt_ruc.setText(rs.getString("ruc_provee"));
                txt_direccion.setText(rs.getString("dir_provee"));
                txt_telf.setText(rs.getString("telf_provee"));
                txt_ciudad.setText(rs.getString("ciudad_provee"));
                txt_contacto.setText(rs.getString("contacto_provee"));
                txt_email.setText(rs.getString("email_provee"));
                txt_web.setText(rs.getString("web_provee"));
            }
            
        } catch (Exception e) {
        }
        
    }
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
            java.util.logging.Logger.getLogger(Proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Proveedores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JPanel btn_eliminar;
    private javax.swing.JToggleButton btn_modificar;
    private javax.swing.JPanel btn_nuevo;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_proveedor;
    private javax.swing.JTextField txt_ciudad;
    private javax.swing.JTextField txt_contacto;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_razon;
    private javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_telf;
    private javax.swing.JTextField txt_web;
    // End of variables declaration//GEN-END:variables
}

