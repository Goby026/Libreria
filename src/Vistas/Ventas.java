
package Vistas;

import Control.Conexion;
import Control.ConexionDemo;
import Control.MyiReportVisor;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Goby
 */
public final class Ventas extends javax.swing.JFrame {
    MyiReportVisor myiReportVisor;
    HashMap parametros = new HashMap();
    
    DefaultListModel lista_productos = new DefaultListModel();
    DefaultTableModel tabla_model;
    Connection con = new Conexion().conectar();
    
    
    public Ventas() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        cargarVentas();
        cargarTitulos();
        btn_quitar.setEnabled(false);
        btn_modificar.setEnabled(false);
        btn_eliminar_venta.setEnabled(false);
    }
    
    
    public void cargarTitulos(){
        String [] titulos = {"COD", "NOMBRE", "CANTIDAD","PRECIO", "SUBTOTAL"};
        tabla_model = new DefaultTableModel(null, titulos);
        tbl_productos.setModel(tabla_model);
        
        //TAMAÑOS DE LAS CELDAS
            tbl_productos.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbl_productos.getColumnModel().getColumn(1).setPreferredWidth(400);
            tbl_productos.getColumnModel().getColumn(2).setPreferredWidth(30);
            tbl_productos.getColumnModel().getColumn(3).setPreferredWidth(30);
            tbl_productos.getColumnModel().getColumn(4).setPreferredWidth(30);
    }
    
    
    public void cargarVentas(){
        String sql = "SELECT `id`, `fecha`FROM `venta`";
        String datos[] = new String[1];
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                datos[0] =rs.getString("id");
                lista_productos.addElement(datos[0]);
                lst_productos.setModel(lista_productos);
            }
            System.out.println("Se cargaron las ventas");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
    
    public void cargarDetalleVenta(int idVenta){
        limpiarTabla();
        String sql = "SELECT `idventa`, descripcion, `cantidad`, venta_detalle.precio, `subtotal` FROM `venta_detalle` INNER JOIN producto on venta_detalle.idproducto = producto.id WHERE idventa= "+idVenta+" ";
        String datos[]= new String[5];
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString("idventa");
                datos[1] = rs.getString("descripcion");
                datos[2] = rs.getString("cantidad");
                datos[3] = rs.getString("venta_detalle.precio");
                datos[4] = rs.getString("subtotal");
                tabla_model.addRow(datos);
            }
            tbl_productos.setModel(tabla_model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
    public double getIgv(){
        double igv=0.0;
        String sql = "SELECT  `igv_empresa` FROM `tconfiguracion`";
        
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                igv = rs.getDouble("igv_empresa");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return igv;
    }
    
    public int getIdProducto(String nombreProd){
        int id = 0;
        String sql = "SELECT `id` FROM `producto` WHERE `descripcion` = '"+nombreProd+"' ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return id;
    }
    
    public void calculos(){
        int filas = tbl_productos.getRowCount();
        double sub_total= 0.0;
        for (int i = 0; i < filas; i++) {
            sub_total = sub_total+Double.parseDouble(tbl_productos.getValueAt(i, 4).toString());            
        }
        txt_subtotal.setText(""+sub_total);
        double igv = sub_total* getIgv();
        double igvs= Redondear(igv);
        txt_igv.setText(""+igvs);
        double t = sub_total+igv;
        double total = Redondear(t);
        txt_total_pagar.setText(""+total);
    }
    
    public void modificarDetalleVenta(){
        int fila = tbl_productos.getSelectedRow();
        int cantidad = Integer.parseInt(tbl_productos.getValueAt(fila, 2).toString());
        double precio = Double.parseDouble(tbl_productos.getValueAt(fila, 3).toString());
        txt_cantidad.setText(""+cantidad);
        txt_precio.setText(""+precio);
    }
    
    
    public void cargarVentasDia(String fecha){
        
    }
    
    public void MostrarFormulario(){
        myiReportVisor = new MyiReportVisor(System.getProperty("user.dir")+"\\src\\Vistas\\pedidos.jrxml",parametros);
        myiReportVisor.setNombreArchivo("factura");
        myiReportVisor.exportarAPdf();
        myiReportVisor.dispose();
    }

    public double Redondear(double numero) {
        return Math.rint(numero * 100) / 100;
    }
    
    public double getTotal(){
        int filas = tbl_productos.getRowCount();
        double count=0.0;
        for (int i = 0; i < filas; i++) {
            count = count + Double.parseDouble(tbl_productos.getValueAt(i, 4).toString());
        }
        return count;
    }

    public void limpiarTabla() {
        for (int i = 0; i < tbl_productos.getRowCount(); i++) {
            tabla_model.removeRow(i);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_productos = new javax.swing.JTable();
        txt_igv = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_subtotal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_total_pagar = new javax.swing.JTextField();
        btn_realizar_venta = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txt_monto_recibido = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_vuelto = new javax.swing.JTextField();
        btn_quitar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst_productos = new javax.swing.JList();
        lbl_producto7 = new javax.swing.JLabel();
        btn_cargar = new javax.swing.JButton();
        btn_eliminar_venta = new javax.swing.JButton();
        lbl_producto8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        btn_modificar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_productos.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        tbl_productos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_productos.setGridColor(new java.awt.Color(0, 102, 255));
        tbl_productos.setSelectionBackground(new java.awt.Color(51, 204, 255));
        tbl_productos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_productosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_productos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 820, 270));

        txt_igv.setEditable(false);
        txt_igv.setBackground(new java.awt.Color(153, 255, 255));
        txt_igv.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_igv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_igv.setText("0");
        txt_igv.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txt_igv, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 490, 84, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("IGV");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 490, -1, -1));

        txt_subtotal.setEditable(false);
        txt_subtotal.setBackground(new java.awt.Color(153, 255, 255));
        txt_subtotal.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_subtotal.setForeground(new java.awt.Color(0, 51, 204));
        txt_subtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_subtotal.setText("0");
        txt_subtotal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txt_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 460, 84, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel10.setText("TOTAL A PAGAR");
        jLabel10.setToolTipText("");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 490, -1, -1));

        txt_total_pagar.setEditable(false);
        txt_total_pagar.setBackground(new java.awt.Color(153, 255, 255));
        txt_total_pagar.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_total_pagar.setForeground(new java.awt.Color(255, 0, 0));
        txt_total_pagar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_total_pagar.setText("0");
        txt_total_pagar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txt_total_pagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 490, 84, -1));

        btn_realizar_venta.setBackground(new java.awt.Color(0, 102, 204));
        btn_realizar_venta.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btn_realizar_venta.setForeground(new java.awt.Color(255, 255, 255));
        btn_realizar_venta.setText("REALIZAR VENTA");
        btn_realizar_venta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_realizar_venta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_realizar_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_realizar_ventaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_realizar_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 410, 139, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel11.setText("MONTO RECIBIDO");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 460, -1, -1));

        txt_monto_recibido.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_monto_recibido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_monto_recibido.setText("0");
        txt_monto_recibido.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txt_monto_recibido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_monto_recibidoActionPerformed(evt);
            }
        });
        getContentPane().add(txt_monto_recibido, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 460, 84, -1));

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel12.setText("VUELTO");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 520, -1, -1));

        txt_vuelto.setEditable(false);
        txt_vuelto.setBackground(new java.awt.Color(153, 255, 255));
        txt_vuelto.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_vuelto.setForeground(new java.awt.Color(0, 153, 0));
        txt_vuelto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_vuelto.setText("0");
        txt_vuelto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        getContentPane().add(txt_vuelto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 520, 84, -1));

        btn_quitar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btn_quitar.setText("QUITAR PEDIDO");
        btn_quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_quitarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_quitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 410, 150, 25));

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("VENTAS");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("SALIR");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 10, -1, -1));

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("MARTES 09 DE FEBRERO DEL 2016");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("11:00 PM");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 100));

        lst_productos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 204, 255), 1, true));
        lst_productos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lst_productosValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lst_productos);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 220, 310));

        lbl_producto7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lbl_producto7.setText("DETALLES DE VENTA");
        getContentPane().add(lbl_producto7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, -1, -1));

        btn_cargar.setBackground(new java.awt.Color(0, 153, 0));
        btn_cargar.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btn_cargar.setForeground(new java.awt.Color(255, 255, 255));
        btn_cargar.setText("Cargar Ventas");
        btn_cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cargarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_cargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 220, -1));

        btn_eliminar_venta.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btn_eliminar_venta.setText("ELIMINAR VENTA");
        btn_eliminar_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_ventaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_eliminar_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 150, 25));

        lbl_producto8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lbl_producto8.setText("LISTA DE VENTAS:");
        getContentPane().add(lbl_producto8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("PRECIO");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        txt_precio.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_precio.setForeground(new java.awt.Color(0, 51, 204));
        txt_precio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_precio.setText("0");
        txt_precio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(txt_precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 84, -1));

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("CANTIDAD");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        txt_cantidad.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_cantidad.setText("0");
        txt_cantidad.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(txt_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 84, -1));

        btn_modificar.setBackground(new java.awt.Color(0, 102, 255));
        btn_modificar.setForeground(new java.awt.Color(255, 255, 255));
        btn_modificar.setText("MODIFICAR");
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 110, 250, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("MODIFICACIONES");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 410, 260, 140));

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel15.setText("SUB-TOTAL");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 460, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_productosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_productosMouseClicked
        btn_quitar.setEnabled(true);
        btn_modificar.setEnabled(true);
        modificarDetalleVenta();
    }//GEN-LAST:event_tbl_productosMouseClicked

    private void btn_realizar_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_realizar_ventaActionPerformed
        int idVenta = Integer.parseInt(tbl_productos.getValueAt(0, 0).toString());
        double total = getTotal();
        parametros.put("idVenta", idVenta);
        parametros.put("total", total);
        MostrarFormulario();
    }//GEN-LAST:event_btn_realizar_ventaActionPerformed

    private void btn_quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_quitarActionPerformed

    }//GEN-LAST:event_btn_quitarActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        new Administracion().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void btn_cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cargarActionPerformed
        //String sql = "INSERT INTO clone_like(campos) SELECT (campos) FROM tabla_origen;";
        lista_productos.clear();
        cargarVentas();
    }//GEN-LAST:event_btn_cargarActionPerformed

    private void btn_eliminar_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_ventaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminar_ventaActionPerformed

    private void lst_productosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lst_productosValueChanged
        int idVenta = Integer.parseInt(lst_productos.getSelectedValue().toString());
        cargarDetalleVenta(idVenta);
        calculos();
        btn_modificar.setEnabled(false);
        btn_quitar.setEnabled(false);
        btn_eliminar_venta.setEnabled(true);
        txt_precio.setText("");
        txt_cantidad.setText("");
    }//GEN-LAST:event_lst_productosValueChanged

    private void txt_monto_recibidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_monto_recibidoActionPerformed
        double recibido = 0.0;
        double total = 0.0;
        recibido = Double.parseDouble(txt_monto_recibido.getText());
        total = Double.parseDouble(txt_total_pagar.getText());
        double v = recibido-total;
        double vuelto = Redondear(v);
        txt_vuelto.setText(""+vuelto);
    }//GEN-LAST:event_txt_monto_recibidoActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fila = tbl_productos.getSelectedRow();
        String prod = tbl_productos.getValueAt(fila, 1).toString();
        int idProd = getIdProducto(prod);
        int id = Integer.parseInt(tbl_productos.getValueAt(fila, 0).toString());
        int cantidad = Integer.parseInt(txt_cantidad.getText());
        double precio = Double.parseDouble(txt_precio.getText());
        String sql = "UPDATE `venta_detalle` SET `cantidad`="+cantidad+",`precio`="+precio+",`subtotal`=cantidad*precio WHERE idventa = "+id+" AND idproducto= "+idProd+" ";
        try {
            Statement st = con.createStatement();
            int res = st.executeUpdate(sql);
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizo correctamente");
                txt_cantidad.setText("");
                txt_precio.setText("");
                btn_modificar.setEnabled(false);
                cargarDetalleVenta(id);
                calculos();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        btn_quitar.setEnabled(false);
        btn_modificar.setEnabled(false);
        btn_eliminar_venta.setEnabled(false);
        txt_precio.setText("");
        txt_cantidad.setText("");
    }//GEN-LAST:event_formMouseClicked

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
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cargar;
    private javax.swing.JButton btn_eliminar_venta;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_quitar;
    private javax.swing.JButton btn_realizar_venta;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_producto7;
    private javax.swing.JLabel lbl_producto8;
    private javax.swing.JList lst_productos;
    public javax.swing.JTable tbl_productos;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_igv;
    private javax.swing.JTextField txt_monto_recibido;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_subtotal;
    private javax.swing.JTextField txt_total_pagar;
    private javax.swing.JTextField txt_vuelto;
    // End of variables declaration//GEN-END:variables
}
