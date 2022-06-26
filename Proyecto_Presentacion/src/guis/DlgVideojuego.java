/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package guis;

import entidades.Usuario;
import entidades.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IVideojuegosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MoonA
 */
public class DlgVideojuego extends javax.swing.JDialog {

    public final interfaces.IVideojuegosDAO videojuegosDAO;

    /**
     * Creates new form dlgVideojuego
     */
    public DlgVideojuego(java.awt.Frame parent, boolean modal, IVideojuegosDAO videojuegosDAO) {
        super(parent, modal);
        this.videojuegosDAO = videojuegosDAO;
        initComponents();
        llenarTabla();
    }

    public void guardar() {
        //Condicion para validar el id recibido
        if (this.txtIdVideojuego.getText().isEmpty()) {
            //Si no existe se agrega uno nuevo
            this.agregar();
        } else {
            //Si existe se actualiza el zoológico específico
            this.actualizar();
        }
    }

    public void agregar() {
        String nombreVideojuego = txtNombre.getText();
        String desarrolladora = txtDesarrolladora.getText();
        int stock = Integer.valueOf(txtStock.getText());
        double precio = Double.valueOf(txtPrecio.getText());

        Videojuego videojuego = new Videojuego(nombreVideojuego, desarrolladora, stock, precio);

        String validacionCampos = validacionCamposGuardar(videojuego);

        if (validacionCampos != null) {
            JOptionPane.showMessageDialog(this, validacionCampos, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                this.videojuegosDAO.agregar(videojuego);
                JOptionPane.showMessageDialog(this, "Videojuego agregado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                llenarTabla();

            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizar() {
        Long idVideojuego = Long.parseLong(txtIdVideojuego.getText());
        String nombreVideojuego = txtNombre.getText();
        String desarrolladora = txtDesarrolladora.getText();
        int stock = Integer.valueOf(txtStock.getText());
        double precio = Double.valueOf(txtPrecio.getText());

        Videojuego videojuego = new Videojuego(nombreVideojuego, desarrolladora, stock, precio);

        String validacionCampos = validacionCamposGuardar(videojuego);

        if (validacionCampos != null) {
            JOptionPane.showMessageDialog(this, validacionCampos, "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                this.videojuegosDAO.actualizar(videojuego);
                JOptionPane.showMessageDialog(this, "Videojuego actualizado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                llenarTabla();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void busquedaPorNombre() {
        String nombreVideojuegoBusqueda = txtBuscarNombre.getText();
        List<Videojuego> listaVideojuego;
        if (nombreVideojuegoBusqueda == null || nombreVideojuegoBusqueda.isEmpty()) {
            try {
                listaVideojuego = this.videojuegosDAO.consultarTodos();
                llenarTablaBusqueda(listaVideojuego);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {

            try {
                listaVideojuego = this.videojuegosDAO.consultarPorNombre(nombreVideojuegoBusqueda);
                llenarTablaBusqueda(listaVideojuego);
                txtBuscarNombre.setText("");
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                txtBuscarNombre.setText("");
            }
        }
    }

    private void editar() throws PersistenciaException {
        Long idVideojuegoSeleccionado = this.getIdVideojuego();
        Videojuego videojuego = null;
        activarCampos();
        if (idVideojuegoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Para editar debe seleccionar un videojuego de la tabla", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            videojuego = this.videojuegosDAO.consultarVideojuegoPorId(idVideojuegoSeleccionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Informacion", JOptionPane.WARNING_MESSAGE);
        }
        if (videojuego != null) {
            llenarFormulario(videojuego);
        }
    }

    private Long getIdVideojuego() {

        int indiceColumnaIdVideojuego = 0;
        int indiceFilaSeleccionada = this.tblVideojuegos.getSelectedRow();

        if (indiceFilaSeleccionada == -1) {
            return null;
        }

        DefaultTableModel modelo = (DefaultTableModel) this.tblVideojuegos.getModel();

        Long idVideojuego = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaIdVideojuego);
        return idVideojuego;
    }

    public void cancelar() {
        limpiarFormulario();
        activarCampos();
    }

    private void llenarFormulario(Videojuego videojuego) {
        this.txtIdVideojuego.setText(videojuego.getId().toString());
        this.txtNombre.setText(videojuego.getNombre());
        this.txtDesarrolladora.setText(videojuego.getDesarrolladora());
        this.txtStock.setText(videojuego.getStock().toString());
        this.txtPrecio.setText(videojuego.getPrecio().toString());

    }

    private void limpiarFormulario() {
        this.txtIdVideojuego.setText("");
        this.txtNombre.setText("");
        this.txtDesarrolladora.setText("");
        this.txtStock.setText("");
        this.txtPrecio.setText("");
    }

    //FALTA CREAR UN CONSULTAR TODOS
    private void llenarTabla() {
        List<Videojuego> listaVideojuegos = null;
        try {
            listaVideojuegos = this.videojuegosDAO.consultarTodos();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.WARNING_MESSAGE);
        }

        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblVideojuegos.getModel();

        modeloTabla.setRowCount(0);

        listaVideojuegos.forEach(videojuego -> {
            Object[] fila = new Object[5];
            fila[0] = videojuego.getId();
            fila[1] = videojuego.getNombre();
            fila[2] = videojuego.getDesarrolladora();
            fila[3] = videojuego.getStock();
            fila[4] = videojuego.getPrecio();

            modeloTabla.addRow(fila);
        });
    }

    private void llenarTablaBusqueda(List<Videojuego> lista) {
        List<Videojuego> listaVideojuegos = lista;

        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblVideojuegos.getModel();

        modeloTabla.setRowCount(0);

        listaVideojuegos.forEach(videojuego -> {
            Object[] fila = new Object[5];
            fila[0] = videojuego.getId();
            fila[1] = videojuego.getNombre();
            fila[2] = videojuego.getDesarrolladora();
            fila[3] = videojuego.getStock();
            fila[4] = videojuego.getPrecio();

            modeloTabla.addRow(fila);
        });
    }
    
    public String validacionCamposGuardar(Videojuego videojuego) {
        if (videojuego.getNombre() == null || videojuego.getNombre().isEmpty()) {
            return "Nombre del videojuego es requerido";
        }
        if (videojuego.getDesarrolladora() == null || videojuego.getDesarrolladora().isEmpty()) {
            return "La desarrolladora del juego es requerida";
        }
        if (videojuego.getStock() == null) {
            return "Stock del videojuego es requerido";
        }
        if (videojuego.getPrecio() == null) {
            return "Precio del videojuego es requerido";
        }
        return null;
    }

    public boolean esNombreVideojuegoDuplicado(String nombreVideojuego) throws PersistenciaException {
        Videojuego videojuego = (Videojuego) videojuegosDAO.consultarPorNombre(nombreVideojuego);
        if (videojuego != null) {
            return true;
        }
        return false;
    }

    public void desactivarCampos() {
        txtNombre.setEditable(false);
        txtDesarrolladora.setEditable(false);
        txtStock.setEditable(false);
        txtPrecio.setEditable(false);
    }

    public void activarCampos() {
        txtNombre.setEditable(true);
        txtDesarrolladora.setEditable(true);
        txtStock.setEditable(true);
        txtPrecio.setEditable(true);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdVideojuego = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtDesarrolladora = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPaneVideojuegos = new javax.swing.JScrollPane();
        tblVideojuegos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarNombre = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tabla de videojuego");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos del videojuego"));

        jLabel1.setText("Id:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Desarrolladora:");

        txtIdVideojuego.setEnabled(false);

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtDesarrolladora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDesarrolladoraKeyTyped(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jLabel5.setText("Stock:");

        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });

        jLabel6.setText("Precio:");

        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addGap(38, 38, 38)
                        .addComponent(btnCancelar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDesarrolladora, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdVideojuego))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDesarrolladora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAgregar))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Búsqueda de videojuegos"));

        tblVideojuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id usuario", "Nombre", "Desarrolladora", "Stock", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneVideojuegos.setViewportView(tblVideojuegos);

        jLabel4.setText("Nombre");

        txtBuscarNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarNombreActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneVideojuegos, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneVideojuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardar();

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try {
            editar();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void txtBuscarNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarNombreActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        busquedaPorNombre();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        if (txtNombre.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtDesarrolladoraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesarrolladoraKeyTyped
        if (txtDesarrolladora.getText().length() >= 100) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDesarrolladoraKeyTyped

    private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (Character.isLetter(caracter)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingresar solo números");
        }
    }//GEN-LAST:event_txtStockKeyTyped

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (Character.isLetter(caracter)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingresar solo números");
        }
    }//GEN-LAST:event_txtPrecioKeyTyped

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(DlgVideojuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DlgVideojuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DlgVideojuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DlgVideojuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DlgVideojuego dialog = new DlgVideojuego(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPaneVideojuegos;
    private javax.swing.JTable tblVideojuegos;
    private javax.swing.JTextField txtBuscarNombre;
    private javax.swing.JTextField txtDesarrolladora;
    private javax.swing.JTextField txtIdVideojuego;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
