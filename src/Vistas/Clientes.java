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
public final class Clientes extends javax.swing.JFrame {
     int posx, posy;
    
    DefaultTableModel model;
    
    public Clientes() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        btn_actualizar.setVisible(false);
        cabecera();
        cargarTabla();
        bloquear();
     }

    public void cabecera(){
        
        String[] cabecera = {"ID","NOMBRE","APE","TELF"};//,"DNI","DIR","EMAIL"};
        model = new DefaultTableModel(null, cabecera);
        tbl_reg_cli.setModel(model);
    } 
    
    
    public int validar_campos() {
        if (!txt_nom_cli.getText().trim().isEmpty()) {
            txt_nom_cli.transferFocus();
            if (!txt_ape_cli1.getText().trim().isEmpty()) {
                txt_ape_cli1.transferFocus();
                if (!txt_telf_cli.getText().trim().isEmpty()) {
                    txt_telf_cli.transferFocus();

                    return 1;

                } else {
                    JOptionPane.showMessageDialog(null, "INGRESE TELEFONO");
                    txt_telf_cli.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "INGRESE APELLIDO");
                txt_ape_cli1.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "INGRESE NOMBRE");
            txt_nom_cli.requestFocus();
        }

        return 0;
    }
    public void limpiarTabla() {
        for (int i = 0; i < tbl_reg_cli.getRowCount(); i++) {
            model.removeRow(i);
            i -= 1;
        }
    }
     public void cargarTabla(){
        Conexion con = new Conexion();
        Connection cc = con.conectar();
        String[] datos= new String[4];
        String sql = "SELECT id_cli, nom_cli, apell_cli, telf_cli FROM tcliente ";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString("id_cli");
                datos[1] = rs.getString("nom_cli");
                datos[2] = rs.getString("apell_cli");
                datos[3] = rs.getString("telf_cli");
               // datos[4] = rs.getString("dni_cli");
                //datos[5] = rs.getString("direc_cli");
                //datos[5] = rs.getString("email_cli");
                model.addRow(datos);
                tbl_reg_cli.setModel(model);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
     public void limpiar(){
        txt_nom_cli.setText("");
        txt_ape_cli1.setText("");
        txt_dni_cli.setText("");
        txt_telf_cli.setText("");
        txt_dir_cli.setText("");
        txt_email_cli.setText("");
        txt_nom_cli.requestFocus();
   
    }
     
    public void Reg_clientes(){
    
    String nom_cli = txt_nom_cli.getText();
    String ape_cli = txt_ape_cli1.getText();
    int telf_cli = Integer.parseInt(txt_telf_cli.getText());
    int dni_cli;
        if (!txt_dni_cli.getText().trim().isEmpty()) {
            dni_cli = Integer.parseInt(txt_dni_cli.getText());
        } else {
            dni_cli = 0;
        }
    String dir_cli = "-";
    dir_cli = txt_dir_cli.getText();
    String email_cli ="-";
    email_cli = txt_email_cli.getText();
    Conexion c = new Conexion();
    Connection cn = c.conectar();
    String sql = "INSERT INTO tcliente(nom_cli,apell_cli,telf_cli, dni_cli, direc_cli,email_cli) VALUES ('"+nom_cli+"','"+ape_cli+"',"+telf_cli+",'"+dni_cli+"','"+dir_cli+"','"+email_cli+"')";
        try {
            Statement st = cn.createStatement();
            int rs = st.executeUpdate(sql);
            if (rs > 0) {
            //JOptionPane.showMessageDialog(null, "USUARIO REGISTRADO CON EXITO");
                //0=si------1=no------2=cancelar
                limpiarTabla();
                cargarTabla();
               
       
                int opc = JOptionPane.showOptionDialog(btn_nuevo, "CLIENTE REGISTRADO, ¿DESEA REGISTRAR MAS CLIENTES?", "showOptionDialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"SI", "NO"}, "SI");
                if (opc == 0) {
                    limpiar();

                } else if (opc == 1) {
                    bloquear();
                }
            }
            
            
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }    
        
    }
    public void bloquear(){
        txt_nom_cli.setEnabled(false);
        txt_ape_cli1.setEnabled(false);
        txt_telf_cli.setEnabled(false);
        txt_dni_cli.setEnabled(false);
        txt_dir_cli.setEnabled(false);
        txt_email_cli.setEnabled(false);
        
    }
    public void desbloquear(){
        txt_nom_cli.setEnabled(true);
        txt_ape_cli1.setEnabled(true);
        txt_telf_cli.setEnabled(true);
        txt_dni_cli.setEnabled(true);
        txt_dir_cli.setEnabled(true);
        txt_email_cli.setEnabled(true);
        
        
    }
    public void capturar_mostrar_datos(int cod){
       Connection cc = new Conexion().conectar();
       String[] datos = new String[6];
       String sql = "SELECT nom_cli, apell_cli, telf_cli, dni_cli, direc_cli, email_cli  FROM tcliente  WHERE id_cli= "+cod+"";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                txt_nom_cli.setText(rs.getString("nom_cli"));
                txt_ape_cli1.setText(rs.getString("apell_cli"));
                txt_telf_cli.setText(rs.getString("telf_cli"));
                txt_dni_cli.setText(rs.getString("dni_cli"));
                txt_dir_cli.setText(rs.getString("direc_cli"));
                txt_email_cli.setText(rs.getString("email_cli"));
            }
            
        } catch (Exception e) {
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_nom_cli = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txt_telf_cli = new javax.swing.JTextField();
        txt_email_cli = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_dni_cli = new javax.swing.JTextField();
        txt_dir_cli = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_reg_cli = new javax.swing.JTable();
        txt_ape_cli1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        btn_eliminar = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        btn_nuevo = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btn_registrar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_modificar = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 102, 102));
        jLabel19.setText("NOMBRES");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("APELLIDOS");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        txt_nom_cli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_nom_cli.setForeground(new java.awt.Color(102, 102, 102));
        txt_nom_cli.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nom_cli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_nom_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nom_cliActionPerformed(evt);
            }
        });
        txt_nom_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nom_cliKeyTyped(evt);
            }
        });
        jPanel3.add(txt_nom_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 300, -1));

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 102));
        jLabel25.setText("TELEFONO");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        txt_telf_cli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_telf_cli.setForeground(new java.awt.Color(102, 102, 102));
        txt_telf_cli.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_telf_cli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_telf_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telf_cliActionPerformed(evt);
            }
        });
        txt_telf_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telf_cliKeyTyped(evt);
            }
        });
        jPanel3.add(txt_telf_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 300, -1));

        txt_email_cli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_email_cli.setForeground(new java.awt.Color(102, 102, 102));
        txt_email_cli.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_email_cli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_email_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_email_cliActionPerformed(evt);
            }
        });
        jPanel3.add(txt_email_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 300, -1));

        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 102));
        jLabel17.setText("DNI");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 102));
        jLabel11.setText("DIRECCION");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 102));
        jLabel12.setText("E-MAIL");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        txt_dni_cli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_dni_cli.setForeground(new java.awt.Color(102, 102, 102));
        txt_dni_cli.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dni_cli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_dni_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dni_cliActionPerformed(evt);
            }
        });
        txt_dni_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dni_cliKeyTyped(evt);
            }
        });
        jPanel3.add(txt_dni_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 300, -1));

        txt_dir_cli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_dir_cli.setForeground(new java.awt.Color(102, 102, 102));
        txt_dir_cli.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dir_cli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_dir_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dir_cliActionPerformed(evt);
            }
        });
        jPanel3.add(txt_dir_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 300, -1));

        tbl_reg_cli.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_reg_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_reg_cliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_reg_cli);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 310, 280));

        txt_ape_cli1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txt_ape_cli1.setForeground(new java.awt.Color(102, 102, 102));
        txt_ape_cli1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ape_cli1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        txt_ape_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ape_cli1ActionPerformed(evt);
            }
        });
        txt_ape_cli1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ape_cli1KeyTyped(evt);
            }
        });
        jPanel3.add(txt_ape_cli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 300, -1));

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("*");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 10, -1));

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 10, -1));

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("*");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 10, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 640, 320));

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
        jLabel21.setText("REGISTRO DE CLIENTES");
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

        btn_nuevo.setBackground(new java.awt.Color(0, 102, 255));
        btn_nuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_nuevoMouseClicked(evt);
            }
        });
        btn_nuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("NUEVO");
        jLabel24.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_nuevo.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 20));

        jPanel2.add(btn_nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 110, 40));

        btn_registrar.setBackground(new java.awt.Color(255, 51, 51));
        btn_registrar.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrar.setText("REGISTRAR");
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_registrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 110, 40));

        btn_actualizar.setBackground(new java.awt.Color(255, 255, 51));
        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/1456175768_adept_update.png"))); // NOI18N
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 30, 40));

        btn_modificar.setBackground(new java.awt.Color(255, 102, 0));
        btn_modificar.setForeground(new java.awt.Color(255, 255, 255));
        btn_modificar.setText("MODIFICAR");
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, 110, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 640, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        dispose();
        new Administracion().setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void btn_eliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseClicked
        int row = tbl_reg_cli.getSelectedRow();
        int cod = Integer.parseInt(tbl_reg_cli.getValueAt(row,0).toString());
        Conexion c = new Conexion();
        Connection cn = c.conectar();
        String sql = "DELETE FROM `tcliente` WHERE `id_cli` = "+cod+"";
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

        //new Administracion().setVisible(true);
    }//GEN-LAST:event_btn_eliminarMouseClicked

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        posx = evt.getX();
        posy = evt.getY();  
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        Point point = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(point.x-posx, point.y-posy);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void txt_nom_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nom_cliKeyTyped
            int tecla = (int)evt.getKeyChar();
            if(tecla>47 && tecla<58){
            evt.setKeyChar((char)KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO LETRAS");
            txt_nom_cli.requestFocus();
        }
    }//GEN-LAST:event_txt_nom_cliKeyTyped

    private void txt_telf_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telf_cliKeyTyped
        int tecla = (int) evt.getKeyChar();
        if (tecla > 64 && tecla < 91 || tecla > 96 && tecla < 123)  {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO NUMEROS");
            txt_telf_cli.requestFocus();

        }
    }//GEN-LAST:event_txt_telf_cliKeyTyped

    private void txt_nom_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nom_cliActionPerformed
        txt_nom_cli.transferFocus();
        txt_ape_cli1.requestFocus();
        
    }//GEN-LAST:event_txt_nom_cliActionPerformed

    private void txt_telf_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telf_cliActionPerformed
        txt_telf_cli.transferFocus();
         //txt_ape_cli1.requestFocus();
    }//GEN-LAST:event_txt_telf_cliActionPerformed

    private void txt_dni_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dni_cliActionPerformed
        txt_dni_cli.transferFocus();
        // txt_ape_cli1.requestFocus();
    }//GEN-LAST:event_txt_dni_cliActionPerformed

    private void txt_dir_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dir_cliActionPerformed
       txt_dir_cli.transferFocus();
    }//GEN-LAST:event_txt_dir_cliActionPerformed

    private void txt_email_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_email_cliActionPerformed
      
    }//GEN-LAST:event_txt_email_cliActionPerformed

    private void txt_ape_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ape_cli1ActionPerformed
         txt_ape_cli1.transferFocus();
    }//GEN-LAST:event_txt_ape_cli1ActionPerformed

    private void txt_ape_cli1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ape_cli1KeyTyped
       int tecla = (int)evt.getKeyChar();
        if(tecla>47 && tecla<58){
            evt.setKeyChar((char)KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO LETRAS");
            txt_ape_cli1.requestFocus();
            //txt_nombres.requestFocus();
        }
    }//GEN-LAST:event_txt_ape_cli1KeyTyped

    private void txt_dni_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dni_cliKeyTyped
      int tecla = (int) evt.getKeyChar();
        if (tecla > 64 && tecla < 91 || tecla > 96 && tecla < 123)  {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "INGRESE SOLO NUMEROS");
            txt_dni_cli.requestFocus();

        } else {
            if (txt_dni_cli.getText().trim().length()==8) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txt_dni_cliKeyTyped

    private void btn_nuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_nuevoMouseClicked
                limpiar();
                desbloquear();
                btn_registrar.setEnabled(true);
                txt_nom_cli.requestFocus();
    }//GEN-LAST:event_btn_nuevoMouseClicked

    private void tbl_reg_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_reg_cliMouseClicked
       
        btn_registrar.setEnabled(false);
        
        int fila = tbl_reg_cli.getSelectedRow();
        int id = Integer.parseInt(tbl_reg_cli.getValueAt(fila, 0).toString());
        capturar_mostrar_datos(id);
         
         /**/
    }//GEN-LAST:event_tbl_reg_cliMouseClicked

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
      if (validar_campos()!=0) {
            Reg_clientes();
            limpiar();
        }else{
            //comprobar();
        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        if (btn_modificar.isSelected()) {
            btn_modificar.setBackground(Color.green);
            btn_actualizar.setVisible(true);
            desbloquear();
            txt_nom_cli.requestFocus();
            
            
        } else {
            btn_modificar.setBackground(Color.orange);
            btn_actualizar.setVisible(false);
            bloquear();
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        if (!txt_nom_cli.getText().trim().isEmpty()) {
            String nom,ape,dir,email;
            int telf,dni;
            nom = txt_nom_cli.getText();
            ape = txt_ape_cli1.getText();
            telf = Integer.parseInt(txt_telf_cli.getText());
             if (!txt_dni_cli.getText().trim().isEmpty()) {
            dni = Integer.parseInt(txt_dni_cli.getText());
             } else {
            dni = 0;
             }
           // dni = Integer.parseInt(txt_dni_cli.getText());
            dir = txt_dir_cli.getText();
            email = txt_email_cli.getText();
            int fila = tbl_reg_cli.getSelectedRow();
            int cod = Integer.parseInt(tbl_reg_cli.getValueAt(fila,0).toString());
            Conexion con = new Conexion();
            Connection cc = con.conectar();
            String sql = "UPDATE tcliente SET nom_cli='"+nom+"',apell_cli = '"+ape+"',telf_cli="+telf+",dni_cli="+dni+",direc_cli= '"+dir+"',email_cli= '"+email+"'  WHERE id_cli = "+cod+" ";
            try {
                Statement st = cc.createStatement();
                int rs = st.executeUpdate(sql);
                if(rs>0){
                limpiarTabla();
                cargarTabla();
                JOptionPane.showMessageDialog(null, "REGISTRO ACTUALIZADO");
                }
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
           
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UN CLIENTE");
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

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
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JPanel btn_eliminar;
    private javax.swing.JToggleButton btn_modificar;
    private javax.swing.JPanel btn_nuevo;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_reg_cli;
    private javax.swing.JTextField txt_ape_cli1;
    private javax.swing.JTextField txt_dir_cli;
    private javax.swing.JTextField txt_dni_cli;
    private javax.swing.JTextField txt_email_cli;
    private javax.swing.JTextField txt_nom_cli;
    private javax.swing.JTextField txt_telf_cli;
    // End of variables declaration//GEN-END:variables

   
}
