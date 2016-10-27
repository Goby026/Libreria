/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;
import Control.Conexion;
import Control.ManejadorFechas;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import javax.swing.JOptionPane;

/**
 *
 * @author Goby
 */
public final class Asistencias extends javax.swing.JFrame implements Runnable{

    int posx, posy, tipo=1, bandera = -1;
    
    String hora, minutos, segundos, ampm;
    
    Calendar calendario;
    Thread h1;
    
    DefaultTableModel model;
    
    Connection con = new Conexion().conectar();
    
    public Asistencias() {
        setUndecorated(true);
        initComponents();
        this.getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        cabecera();
        h1 = new Thread(this);
        h1.start();
        setVisible(true);
        cargarAsistencia();
        cargarAnuncio();
    }
    
    public void calcular() {
        calendario = new GregorianCalendar();
        Date fechaHoraActual = new Date();        
        
        calendario.setTime(fechaHoraActual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        if (ampm.equals("PM")) {
            int h = calendario.get(Calendar.HOUR_OF_DAY) - 12;
            hora = h > 9 ? "" + h : "0" + h;
        } else {
            hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        }
        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }
    
    public String obtener_hora() {
        String hora = "";
        calcular();
        hora = (this.hora + ":" + minutos + ":" + segundos + " " + ampm);
        return hora;
    }
    
    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == h1) {
            calcular();
            lbl_hora_app.setText(hora + ":" + minutos + ":" + segundos + " " + ampm);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    public void cabecera(){
        String [] titulos = {"# Reg","Nombre","Apellido","Tipo","Hora","Min Retraso", "Faltas","FECHA"};
        model = new DefaultTableModel(null, titulos);
        tbl_asistencia.setModel(model);
    }
    
    
    
    public void registrarAsistencia(int dni){
        try {
            String hh = new ManejadorFechas().getHoraActual();
            String fecha = new ManejadorFechas().getFechaActualMySQL();
            String nom = getNombre(dni);
            int id_usu = getId(dni);
            String tipoMarcado = null;
            if (tipo==1) {
                tipoMarcado = "ENTRADA";
            } else if(tipo==2) {
                tipoMarcado = "SALIDA";
            }
            int tardanza = getMinRetraso();
            int falta = calcFaltas(Integer.parseInt(hora));
            
            String obs = txtObservaciones.getText();
            String sql = "INSERT INTO `tasistencia`(`hora_as`, `fecha_as`, `tipo_as`, `min_tarde_as`, `faltas_as`, `obs_as`, `id_usu`) VALUES ('"+hh+"','"+fecha+"','"+tipoMarcado+"','"+tardanza+"','"+falta+"','"+obs+"','"+id_usu+"')";
            Statement st = con.createStatement();
            int res = st.executeUpdate(sql);
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Buenos dias " + nom);
            } else {
            }
            
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    
    public void mostrarDetalles(){
        lbl_fecha.setText(""+ new ManejadorFechas().getFechaActual());
        lbl_entrada.setText("" + new ManejadorFechas().getHoraActual());
        int dni = Integer.parseInt(txtCodigo.getText());
        String nom = getNombre(dni)+" "+ getApellido(dni) ;
        lbl_nombres.setText(nom);
        //int tardanza = calcMinRetraso();
        int id = getId(dni);
        lbl_min_tarde.setText(""+setMinRetraso(id));
        int hor = calendario.get(Calendar.HOUR_OF_DAY);
        lbl_faltas.setText(""+calcFaltas(hor));
    }
    
    public int calcFaltas(int hora){
        int faltas = 0;
        if (hora>=9) {
                    faltas = faltas + 1;
                }
        return faltas;
    }
    
    public int setMinRetraso(int id){
        int min_ret = 0;
        String sql = "SELECT SUM(min_tarde_as) FROM tasistencia WHERE id_usu = "+id+" ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                min_ret = rs.getInt("SUM(min_tarde_as)");
            } else {
            }
        } catch (Exception e) {
        }
        return min_ret;
    }
    
    
    public int getMinRetraso(){
        int mm=0;
        int hor = calendario.get(Calendar.HOUR_OF_DAY);
        int min = calendario.get(Calendar.MINUTE);        
        try {
            if (hor >= 8) {
                if (min > 0) {
                    mm = min;
                }
            } else {
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return mm;
    }
    
    
    
    public String getNombre(int dni){
        String s=null;
        try {
            String sql = "SELECT nom_usu FROM tusuario WHERE `dni_usu` = '"+dni+"' ";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                s= rs.getString("nom_usu");
            } else {
                System.out.println("Error en la consulta");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return s;
    }
    
    public String getApellido(int dni){
        String s="";
        try {
            String sql = "SELECT ape_usu FROM tusuario WHERE `dni_usu` = '"+dni+"' ";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                s= rs.getString("ape_usu");
            } else {
                System.out.println("Error");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return s;
    }
    
    public int getId(int dni){
        int s=0;
        try {
            String sql = "SELECT id_usu FROM tusuario WHERE dni_usu = '"+dni+"' ";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                s= rs.getInt("id_usu");
            } else {
                System.out.println("Error en la consulta");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return s;
    }
    
    public void cargarAsistencia(){
        String sql = "SELECT id_as,nom_usu,ape_usu,tipo_as,hora_as,min_tarde_as,faltas_as,fecha_as  FROM tasistencia INNER JOIN tusuario ON tasistencia.id_usu = tusuario.id_usu ORDER BY id_as DESC";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String datos[] = new String[8];
            while (rs.next()) {                
                datos[0]=rs.getString("id_as");
                datos[1]=rs.getString("nom_usu");
                datos[2]=rs.getString("ape_usu");
                datos[3]=rs.getString("tipo_as");
                datos[4]=rs.getString("hora_as");
                datos[5]=rs.getString("min_tarde_as");
                datos[6]=rs.getString("faltas_as");
                datos[7]=rs.getString("fecha_as");
                model.addRow(datos);
                tbl_asistencia.setModel(model);
            }
            //TAMAÑOS DE LAS CELDAS
            tbl_asistencia.getColumnModel().getColumn(0).setPreferredWidth(40);
            tbl_asistencia.getColumnModel().getColumn(1).setPreferredWidth(70);
            tbl_asistencia.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbl_asistencia.getColumnModel().getColumn(3).setPreferredWidth(80);
            tbl_asistencia.getColumnModel().getColumn(4).setPreferredWidth(70);
            tbl_asistencia.getColumnModel().getColumn(5).setPreferredWidth(90);
            tbl_asistencia.getColumnModel().getColumn(6).setPreferredWidth(60);
            tbl_asistencia.getColumnModel().getColumn(7).setPreferredWidth(90);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void cargarAnuncio(){
        String sql = "SELECT anuncio_empresa FROM tconfiguracion";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                txa_avisos.setText(rs.getString("anuncio_empresa"));
            } else {
                txa_avisos.setText("No existen avisos pendientes");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    public void limpiarTabla() {
        for (int i = 0; i < tbl_asistencia.getRowCount(); i++) {
            model.removeRow(i);
            i -= 1;
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

        jLabel2 = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        btn_marcar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_asistencia = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txa_avisos = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbl_nombres = new javax.swing.JLabel();
        lbl_min_tarde = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbl_faltas = new javax.swing.JLabel();
        lbl_entrada = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbl_fecha_app = new javax.swing.JLabel();
        lbl_hora_app = new javax.swing.JLabel();
        lbl_salir = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel2.setText("CÓDIGO / DNI");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, -1, -1));

        txtObservaciones.setEditable(false);
        txtObservaciones.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtObservaciones.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtObservaciones.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txtObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 300, 500, 30));

        btn_marcar.setBackground(new java.awt.Color(0, 102, 204));
        btn_marcar.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btn_marcar.setForeground(new java.awt.Color(255, 255, 255));
        btn_marcar.setText("MARCAR");
        btn_marcar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_marcarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_marcar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 370, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("AVISOS");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 530, -1, -1));

        tbl_asistencia.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_asistencia);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 610, 190));

        txa_avisos.setColumns(20);
        txa_avisos.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txa_avisos.setLineWrap(true);
        txa_avisos.setRows(5);
        jScrollPane2.setViewportView(txa_avisos);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 610, 80));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setText("OBSERVACIONES:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, -1, -1));

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fecha.png"))); // NOI18N
        jLabel16.setText("Fecha:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        lbl_fecha.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_fecha.setForeground(new java.awt.Color(0, 102, 204));
        lbl_fecha.setText("___");
        getContentPane().add(lbl_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 120, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/employee.png"))); // NOI18N
        jLabel4.setText("Nombres y apellidos:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        lbl_nombres.setForeground(new java.awt.Color(0, 102, 204));
        lbl_nombres.setText("____");
        getContentPane().add(lbl_nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 160, -1));

        lbl_min_tarde.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_min_tarde.setForeground(new java.awt.Color(0, 102, 204));
        lbl_min_tarde.setText("___");
        getContentPane().add(lbl_min_tarde, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 70, -1));

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/code.png"))); // NOI18N
        jLabel9.setText("Entrada a las: ");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, -1, -1));

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/modificar.png"))); // NOI18N
        jLabel14.setText("Faltas:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 260, -1, -1));

        lbl_faltas.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_faltas.setForeground(new java.awt.Color(0, 102, 204));
        lbl_faltas.setText("__");
        getContentPane().add(lbl_faltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 260, -1, -1));

        lbl_entrada.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_entrada.setForeground(new java.awt.Color(0, 102, 204));
        lbl_entrada.setText("____");
        getContentPane().add(lbl_entrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 240, -1, -1));

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/alarm.png"))); // NOI18N
        jLabel13.setText("Tardanza acumulada:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 140, -1));

        txtCodigo.setFont(new java.awt.Font("SansSerif", 0, 36)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 370, 50));

        jPanel3.setBackground(new java.awt.Color(255, 102, 0));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel3MouseReleased(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ASISTENCIA");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, -1));

        lbl_fecha_app.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lbl_fecha_app.setForeground(new java.awt.Color(255, 255, 255));
        lbl_fecha_app.setText("MARTES 09 DE FEBRERO DEL 2016");
        jPanel3.add(lbl_fecha_app, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        lbl_hora_app.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_hora_app.setForeground(new java.awt.Color(255, 255, 255));
        lbl_hora_app.setText("08:11 p.m.");
        jPanel3.add(lbl_hora_app, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        lbl_salir.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbl_salir.setForeground(new java.awt.Color(255, 255, 255));
        lbl_salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exit.png"))); // NOI18N
        lbl_salir.setText("SALIR");
        lbl_salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_salirMouseClicked(evt);
            }
        });
        jPanel3.add(lbl_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 100));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 610, 90));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_marcarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_marcarActionPerformed
        //cambiar la relacion tusuario y tasistencia
        if (!txtCodigo.getText().trim().isEmpty()) {
            int dni = Integer.parseInt(txtCodigo.getText());
            String ape = getApellido(dni);
            //lbl_ddd.setText(ape);
            if (ape.equals("")) {
                JOptionPane.showMessageDialog(null, "Usuario incorrecto");
            } else {
                //JOptionPane.showMessageDialog(null, "Bien");
                try {
                    registrarAsistencia(dni);
                    mostrarDetalles();
                    limpiarTabla();
                    cargarAsistencia();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese codigo");
            txtCodigo.requestFocus();
        }
        
        
        
        
    }//GEN-LAST:event_btn_marcarActionPerformed

    private void lbl_salirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_salirMouseClicked
        new Administracion().setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_salirMouseClicked

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        posx = evt.getX();
        posy = evt.getY();
    }//GEN-LAST:event_jPanel3MousePressed

    private void jPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseReleased
        Point point = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(point.x-posx, point.y-posy);
    }//GEN-LAST:event_jPanel3MouseReleased

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
            java.util.logging.Logger.getLogger(Asistencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Asistencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Asistencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asistencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Asistencias().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_marcar;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_entrada;
    private javax.swing.JLabel lbl_faltas;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_fecha_app;
    private javax.swing.JLabel lbl_hora_app;
    private javax.swing.JLabel lbl_min_tarde;
    private javax.swing.JLabel lbl_nombres;
    private javax.swing.JLabel lbl_salir;
    private javax.swing.JTable tbl_asistencia;
    private javax.swing.JTextArea txa_avisos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
