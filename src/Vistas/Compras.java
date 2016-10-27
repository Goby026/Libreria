
package Vistas;

import Control.Conexion;
import Control.ManejadorFechas;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Goby
 */
public final class Compras extends javax.swing.JFrame {

    Connection con = new Conexion().conectar();
    int posx, posy;
    DefaultTableModel model_prod;
    DefaultListModel lista_provee = new DefaultListModel();
    DefaultListModel lista_produc = new DefaultListModel();
    
    public Compras() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        cabeceraProductos();
    }
    
    public void cabeceraProductos(){
        String titulos[] = {"CODIGO","NOMBRE","P.UNITARIO","CANTIDAD","SUBTOTAL"};
        model_prod = new DefaultTableModel(null, titulos);
        tbl_productos_agregados.setModel(model_prod);
        lbl_igv.setText(""+ getIgv());
        
        //TAMAÑOS DE LAS CELDAS
            tbl_productos_agregados.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbl_productos_agregados.getColumnModel().getColumn(1).setPreferredWidth(170);
            tbl_productos_agregados.getColumnModel().getColumn(2).setPreferredWidth(30);
            tbl_productos_agregados.getColumnModel().getColumn(3).setPreferredWidth(30);
            tbl_productos_agregados.getColumnModel().getColumn(4).setPreferredWidth(30);            
    }
    
    public void cargarProveedores(){
        limpiarLista();
        String sql = "SELECT `nom_provee` FROM `tproveedor`";
        String datos[] = new String[1];
        try {
            Statement st =  con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                lista_provee.addElement(rs.getString("nom_provee"));
                list_proveedores.setModel(lista_provee);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void getProveedor(){
        String provee = list_proveedores.getSelectedValue().toString();
        String sql = "SELECT `nom_provee`, `ruc_provee`, `dir_provee`, `telf_provee`FROM `tproveedor` WHERE nom_provee = '"+provee+"' ";
        try {
            Statement st =  con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                txt_ruc.setText(rs.getString("ruc_provee"));
                txt_nom_raz.setText(rs.getString("nom_provee"));
                txt_celular.setText(rs.getString("telf_provee"));
                txt_direccion.setText(rs.getString("dir_provee"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void cargarProductos(){
        limpiarLista();
        String sql = "SELECT `descripcion` FROM `producto`";
        String datos[] = new String[1];
        try {
            Statement st =  con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                lista_produc.addElement(rs.getString("descripcion"));
                list_productos.setModel(lista_produc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void getProducto(){
        String prod = list_productos.getSelectedValue().toString();
        String sql = "SELECT `id`, `descripcion`, `stock_prod`, tmarca.nom_marca FROM `producto` INNER JOIN tmarca ON producto.id_marca = tmarca.id_marca WHERE `descripcion` = '"+prod+"' ";
        try {
            Statement st =  con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                txt_nombre_prod.setText(rs.getString("descripcion"));
                txt_stock.setText(rs.getString("stock_prod"));
                txt_codigo.setText(rs.getString("id"));
                txt_marca.setText(rs.getString("tmarca.nom_marca"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void buscarProducto(){
        limpiarLista();
        String prod = txt_buscar_productos.getText();
        String sql = "SELECT descripcion FROM producto WHERE descripcion LIKE '"+prod+"%' OR descripcion LIKE '%"+prod+"' ";
        String datos[] = new String[1];
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {                
                datos[0] = rs.getString("descripcion");
                lista_produc.addElement(datos[0]);
            }
            list_productos.setModel(lista_produc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void addProducto(int cod){
        Double precUni= 0.0;
        String sql = "SELECT `id`, `descripcion`FROM `producto` WHERE `id` = "+cod+" ";
        String datos[]= new String[5];
        int cantidad = Integer.parseInt(txt_cantidad.getText());
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                datos[0] = rs.getString("id");
                datos[1] = rs.getString("descripcion");
                datos[2] = txt_precio.getText();
                precUni = Double.parseDouble(datos[2]);
                datos[3] = String.valueOf(cantidad);
                datos[4] = String.valueOf(precUni*cantidad);
                model_prod.addRow(datos);
                tbl_productos_agregados.setModel(model_prod);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void limpiarLista(){
        lista_provee.clear();
        lista_produc.clear();
    }
    
    public void limpiarProductos(){
        txt_nombre_prod.setText("");
        txt_stock.setText("");
        txt_codigo.setText("");
        txt_marca.setText("");
        txt_precio.setText("");
        txt_cantidad.setText("");
    }
    
    public void limpiarProveedorDocumento(){
        txt_nom_raz.setText("");
        txt_ruc.setText("");
        txt_celular.setText("");
        txt_direccion.setText("");
    }
    
    public void limpiarTodo(){
        lbl_num_productos.setText("0");
        txt_subtotal.setText("0");
        txt_igv.setText("0");
        txt_total.setText("0");
        limpiarProveedorDocumento();
        limpiarTabla();
    }
    
    public void limpiarTabla() {
        for (int i = 0; i < tbl_productos_agregados.getRowCount(); i++) {
            model_prod.removeRow(i);
            i -= 1;
        }
    }
    
    
    public double subTotal(){
        double st = 0.0;
        int filas = tbl_productos_agregados.getRowCount();
        try {
            for (int i = 0; i < filas; i++) {
                st = st + Double.parseDouble(tbl_productos_agregados.getValueAt(i, 4).toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return st;
    }
    
    public void registrarCompra(){
        int numDoc = Integer.parseInt(txt_num_doc.getText());
        int tipoDoc = cmb_boleta.getSelectedIndex() + 1;
        String formaPago = cmb_form_pago.getSelectedItem().toString();
        String fec = new ManejadorFechas().getFechaActualMySQL();
        String hora = new ManejadorFechas().getHoraActual();
        int filas = tbl_productos_agregados.getRowCount();
        String provee = txt_nom_raz.getText();
        String sql = "INSERT INTO `tcompras`(`num_doc`, `idtipo`, `forma_pago`, `fecha_compra`, `hora_compra`, `proveedor`) VALUES (" + numDoc + "," + tipoDoc + ",'" + formaPago + "', '" + fec + "', '" + hora + "', '"+provee+"' )";
        try {
            Statement st = con.createStatement();
            int res = st.executeUpdate(sql);
            if (res>0) {
                System.out.println("Compra Registrada");
            } else {
                System.out.println("Error al registrar la compra");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public int getNumCompra(){
        int id=0;
        String sql = "SELECT MAX(`id_compra`) FROM `tcompras`";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                id = Integer.parseInt(rs.getString("MAX(`id_compra`)"));
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return id;
    }
    
    public void registrarDetalleCompra(){
        int filas = tbl_productos_agregados.getRowCount();
        int idcompra = getNumCompra();        
        try {
            Statement st = con.createStatement();
            for (int i = 0; i < filas; i++) {
                int idprod = Integer.parseInt(tbl_productos_agregados.getValueAt(i, 0).toString());
                double precioU = Double.parseDouble(tbl_productos_agregados.getValueAt(i, 2).toString());
                int cantidad = Integer.parseInt(tbl_productos_agregados.getValueAt(i, 3).toString());
                double subtotal = precioU*cantidad;
                String sql = "INSERT INTO `tdetallecompra`(`id_compra`, `id_prod`, `cantidad`, `precio`, `subtotal`) VALUES ("+idcompra+","+idprod+","+cantidad+","+precioU+","+subtotal+")";
                int res = st.executeUpdate(sql);
                if (res > 0) {
                    System.out.println("Detalle registrado");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public double getIgv(){
        double igv = 0.0;
        String sql = "SELECT `igv_empresa` FROM `tconfiguracion` WHERE `id_config` = 1";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                igv = Double.parseDouble(rs.getString("igv_empresa"));
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return igv;
    }
    
    public double calcularIgv(){
        double sigv = Double.parseDouble(txt_subtotal.getText());
        double igv = getIgv();
        try {
            sigv = sigv * igv;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return sigv;
    }
    
    public void totalPagar(){
        double sub = Double.parseDouble(txt_subtotal.getText());
        double igv = calcularIgv();
        double total = sub+igv;
        txt_total.setText(""+total);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frm_buscar_proveedor = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_provee = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btn_seleccionar_provee = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_proveedores = new javax.swing.JList();
        frm_buscar_productos = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_productos = new javax.swing.JList();
        btn_addProd = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txt_buscar_productos = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        pan_proveedor = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txt_celular = new javax.swing.JTextField();
        txt_ruc = new javax.swing.JTextField();
        txt_nom_raz = new javax.swing.JTextField();
        txt_direccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btn_buscar1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cmb_boleta = new javax.swing.JComboBox();
        txt_num_doc = new javax.swing.JTextField();
        cmb_form_pago = new javax.swing.JComboBox();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        pan_producto = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_codigo = new javax.swing.JTextField();
        txt_stock = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_nombre_prod = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        btn_quitar = new javax.swing.JButton();
        btn_agregar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_productos_agregados = new javax.swing.JTable();
        btn_buscar2 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txt_marca = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lbl_num_productos = new javax.swing.JLabel();
        lbl_igv = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_subtotal = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_igv = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();
        btn_comprar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        lbl_salir = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();

        frm_buscar_proveedor.setBounds(new java.awt.Rectangle(0, 0, 330, 440));
        frm_buscar_proveedor.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("BUSCAR PROVEEDOR");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        frm_buscar_proveedor.getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 90));
        frm_buscar_proveedor.getContentPane().add(txt_buscar_provee, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 230, -1));

        jLabel7.setText("BUSCAR");
        frm_buscar_proveedor.getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        btn_seleccionar_provee.setBackground(new java.awt.Color(0, 153, 204));
        btn_seleccionar_provee.setForeground(new java.awt.Color(255, 255, 255));
        btn_seleccionar_provee.setText("SELECCIONAR");
        btn_seleccionar_provee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_seleccionar_proveeActionPerformed(evt);
            }
        });
        frm_buscar_proveedor.getContentPane().add(btn_seleccionar_provee, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 320, -1));

        list_proveedores.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(list_proveedores);

        frm_buscar_proveedor.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 300, 220));

        frm_buscar_productos.setBounds(new java.awt.Rectangle(600, 300, 330, 480));
        frm_buscar_productos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 102, 0));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("BUSCAR PRODUCTOS");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        frm_buscar_productos.getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 90));

        list_productos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(list_productos);

        frm_buscar_productos.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 300, 230));

        btn_addProd.setBackground(new java.awt.Color(0, 153, 204));
        btn_addProd.setForeground(new java.awt.Color(255, 255, 255));
        btn_addProd.setText("AGREGAR");
        btn_addProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addProdActionPerformed(evt);
            }
        });
        frm_buscar_productos.getContentPane().add(btn_addProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 320, -1));

        jLabel26.setText("BUSCAR");
        frm_buscar_productos.getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        txt_buscar_productos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txt_buscar_productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_productosKeyReleased(evt);
            }
        });
        frm_buscar_productos.getContentPane().add(txt_buscar_productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 240, -1));

        jLabel28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel28.setText("MARCA");
        frm_buscar_productos.getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, -1));

        jTextField1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        frm_buscar_productos.getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pan_proveedor.setBackground(new java.awt.Color(204, 255, 255));
        pan_proveedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel13.setText("PROVEEDOR");
        pan_proveedor.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel4.setText("RUC");
        pan_proveedor.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel5.setText("NOMBRE, RAZON SOCIAL");
        pan_proveedor.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel1.setText("TELÉFONO/CELULAR");
        pan_proveedor.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        txt_celular.setEditable(false);
        txt_celular.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_celular.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_celular.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_proveedor.add(txt_celular, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 150, -1));

        txt_ruc.setEditable(false);
        txt_ruc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_ruc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ruc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_proveedor.add(txt_ruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 150, -1));

        txt_nom_raz.setEditable(false);
        txt_nom_raz.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txt_nom_raz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nom_raz.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_proveedor.add(txt_nom_raz, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 150, -1));

        txt_direccion.setEditable(false);
        txt_direccion.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_direccion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_direccion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_proveedor.add(txt_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 230, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel6.setText("DIRECCIÓN");
        pan_proveedor.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        btn_buscar1.setBackground(new java.awt.Color(0, 204, 0));
        btn_buscar1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btn_buscar1.setForeground(new java.awt.Color(255, 255, 255));
        btn_buscar1.setText("BUSCAR");
        btn_buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar1ActionPerformed(evt);
            }
        });
        pan_proveedor.add(btn_buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 90, 20));

        getContentPane().add(pan_proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 390, 160));

        jPanel3.setBackground(new java.awt.Color(102, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CALCULADORA");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("FECHA");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("FORMA DE PAGO");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("N° DE DOCUMENTO");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, -1));

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("TIPO DE DOCUMENTO");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("DOCUMENTO");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cmb_boleta.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmb_boleta.setForeground(new java.awt.Color(0, 0, 0));
        cmb_boleta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Boleta", "Factura" }));
        jPanel3.add(cmb_boleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 160, 20));

        txt_num_doc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_num_doc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_num_doc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel3.add(txt_num_doc, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 160, -1));

        cmb_form_pago.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmb_form_pago.setForeground(new java.awt.Color(0, 0, 0));
        cmb_form_pago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "Crédito" }));
        jPanel3.add(cmb_form_pago, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 160, 20));
        jPanel3.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 160, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 390, 160));

        pan_producto.setBackground(new java.awt.Color(0, 102, 102));
        pan_producto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("MARCA");
        pan_producto.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, -1));

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("STOCK");
        pan_producto.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        txt_codigo.setEditable(false);
        txt_codigo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_codigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_codigo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 80, -1));

        txt_stock.setEditable(false);
        txt_stock.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_stock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_stock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 50, -1));

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("NOMBRE");
        pan_producto.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("PRECIO");
        pan_producto.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, -1, -1));

        txt_nombre_prod.setEditable(false);
        txt_nombre_prod.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_nombre_prod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nombre_prod.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_nombre_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 370, -1));

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("CANTIDAD");
        pan_producto.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, -1, -1));

        txt_cantidad.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_cantidad.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, 80, -1));

        btn_quitar.setBackground(new java.awt.Color(255, 0, 0));
        btn_quitar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btn_quitar.setForeground(new java.awt.Color(255, 255, 255));
        btn_quitar.setText("QUITAR");
        btn_quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_quitarActionPerformed(evt);
            }
        });
        pan_producto.add(btn_quitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 120, 40));

        btn_agregar.setBackground(new java.awt.Color(255, 153, 0));
        btn_agregar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btn_agregar.setForeground(new java.awt.Color(255, 255, 255));
        btn_agregar.setText("AGREGAR");
        btn_agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarActionPerformed(evt);
            }
        });
        pan_producto.add(btn_agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 120, 40));

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("PRODUCTO");
        pan_producto.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        txt_precio.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_precio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_precio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 80, -1));

        jLabel25.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("LISTA DE PRODUCTOS AGREGADOS");
        pan_producto.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        tbl_productos_agregados.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tbl_productos_agregados.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_productos_agregados);

        pan_producto.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 760, 120));

        btn_buscar2.setBackground(new java.awt.Color(0, 204, 0));
        btn_buscar2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btn_buscar2.setForeground(new java.awt.Color(255, 255, 255));
        btn_buscar2.setText("BUSCAR");
        btn_buscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar2ActionPerformed(evt);
            }
        });
        pan_producto.add(btn_buscar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 0, 120, 40));

        jLabel27.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("CÓDIGO");
        pan_producto.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        txt_marca.setEditable(false);
        txt_marca.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_marca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_marca.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pan_producto.add(txt_marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 240, -1));

        getContentPane().add(pan_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 780, 240));

        jPanel5.setBackground(new java.awt.Color(255, 204, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel29.setText("PRODUCTOS AGREGADOS");
        jPanel5.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel24.setText("IGV ACTUAL:");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        lbl_num_productos.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lbl_num_productos.setText("........");
        jPanel5.add(lbl_num_productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, -1));

        lbl_igv.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lbl_igv.setText("........");
        jPanel5.add(lbl_igv, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 260, 110));

        jPanel6.setBackground(new java.awt.Color(102, 204, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("SUBTOTAL");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        txt_subtotal.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_subtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_subtotal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel6.add(txt_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 100, -1));

        jLabel22.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("IGV");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        txt_igv.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_igv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_igv.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel6.add(txt_igv, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 100, -1));

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("TOTAL");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        txt_total.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        txt_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_total.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jPanel6.add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 100, 40));

        btn_comprar.setBackground(new java.awt.Color(0, 153, 204));
        btn_comprar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btn_comprar.setForeground(new java.awt.Color(255, 255, 255));
        btn_comprar.setText("COMPRAR");
        btn_comprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_comprarActionPerformed(evt);
            }
        });
        jPanel6.add(btn_comprar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 400, -1));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 500, 520, 110));

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

        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("COMPRAS");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));

        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("11:00 PM");
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        lbl_fecha.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lbl_fecha.setForeground(new java.awt.Color(255, 255, 255));
        lbl_fecha.setText("Viernes, 24 de Diciembre del 2015");
        jPanel1.add(lbl_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 230, -1));

        lbl_salir.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbl_salir.setForeground(new java.awt.Color(255, 255, 255));
        lbl_salir.setText("SALIR");
        lbl_salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_salirMouseClicked(evt);
            }
        });
        jPanel1.add(lbl_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 100));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 780, 90));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("calc");
            p.waitFor();
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarActionPerformed
        int cod = Integer.parseInt(txt_codigo.getText());
        if (!txt_precio.getText().trim().isEmpty()) {
            if (!txt_cantidad.getText().trim().isEmpty()) {
                if (!txt_nombre_prod.getText().trim().isEmpty()) {
                    addProducto(cod);
                    double st = subTotal();
                    txt_subtotal.setText("" + st);
                    txt_igv.setText("" + calcularIgv());
                    lbl_num_productos.setText("" + tbl_productos_agregados.getRowCount());
                    totalPagar();
                    limpiarProductos();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
                txt_cantidad.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese precio");
            txt_precio.requestFocus();
        }
    }//GEN-LAST:event_btn_agregarActionPerformed

    private void lbl_salirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_salirMouseClicked
        new Administracion().setVisible(true);
        dispose();
    }//GEN-LAST:event_lbl_salirMouseClicked

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        posx = evt.getX();
        posy = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        Point point = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(point.x-posx, point.y-posy);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void btn_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar1ActionPerformed
        frm_buscar_proveedor.setVisible(true);
        cargarProveedores();
    }//GEN-LAST:event_btn_buscar1ActionPerformed

    private void btn_buscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar2ActionPerformed
        frm_buscar_productos.setVisible(true);
        cargarProductos();
    }//GEN-LAST:event_btn_buscar2ActionPerformed

    private void btn_addProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addProdActionPerformed
        getProducto();
        frm_buscar_productos.setVisible(false);
    }//GEN-LAST:event_btn_addProdActionPerformed

    private void btn_seleccionar_proveeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seleccionar_proveeActionPerformed
        getProveedor();
        frm_buscar_proveedor.setVisible(false);
    }//GEN-LAST:event_btn_seleccionar_proveeActionPerformed

    private void btn_quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_quitarActionPerformed
        int fila = tbl_productos_agregados.getSelectedRow();
        model_prod.removeRow(fila);
    }//GEN-LAST:event_btn_quitarActionPerformed

    private void btn_comprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_comprarActionPerformed
        int filas = tbl_productos_agregados.getRowCount();
        if (filas!=0) {
            registrarCompra();
            registrarDetalleCompra();
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(null, "LA LISTA ESTA VACIA, AGREGUE PRODUCTOS");
        }
    }//GEN-LAST:event_btn_comprarActionPerformed

    private void txt_buscar_productosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productosKeyReleased
        buscarProducto();
    }//GEN-LAST:event_txt_buscar_productosKeyReleased

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
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_addProd;
    private javax.swing.JButton btn_agregar;
    private javax.swing.JButton btn_buscar1;
    private javax.swing.JButton btn_buscar2;
    private javax.swing.JButton btn_comprar;
    private javax.swing.JButton btn_quitar;
    private javax.swing.JButton btn_seleccionar_provee;
    private javax.swing.JComboBox cmb_boleta;
    private javax.swing.JComboBox cmb_form_pago;
    private javax.swing.JDialog frm_buscar_productos;
    private javax.swing.JDialog frm_buscar_proveedor;
    private com.toedter.calendar.JDateChooser jDateChooser2;
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
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_igv;
    private javax.swing.JLabel lbl_num_productos;
    private javax.swing.JLabel lbl_salir;
    private javax.swing.JList list_productos;
    private javax.swing.JList list_proveedores;
    private javax.swing.JPanel pan_producto;
    private javax.swing.JPanel pan_proveedor;
    private javax.swing.JTable tbl_productos_agregados;
    private javax.swing.JTextField txt_buscar_productos;
    private javax.swing.JTextField txt_buscar_provee;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_celular;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_igv;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_nom_raz;
    private javax.swing.JTextField txt_nombre_prod;
    private javax.swing.JTextField txt_num_doc;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_stock;
    private javax.swing.JTextField txt_subtotal;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables

}
