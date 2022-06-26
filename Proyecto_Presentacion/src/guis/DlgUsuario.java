/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package guis;

import entidades.Usuario;
import excepciones.PersistenciaException;
import interfaces.IUsuariosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MoonA
 */
public class DlgUsuario extends javax.swing.JDialog {

    public final IUsuariosDAO usuariosDAO;

    /**
     * Creates new form dlgUsuario
     */
    public DlgUsuario(java.awt.Frame parent, boolean modal, IUsuariosDAO usuarioDAO) {
        super(parent, modal);
        this.usuariosDAO = usuarioDAO;
        initComponents();
        llenarTabla();
    }

    public void guardar() {
        //Condicion para validar el id recibido
        if (this.txtIdUsuario.getText().isEmpty()) {
            //Si no existe se agrega uno nuevo
            this.agregar();
        } else {
            //Si existe se actualiza el zoológico específico
            this.actualizar();
        }
    }

    public void agregar() {
        String nombreUsuario = txtNombre.getText();
        String telefono = txtTelefono.getText();

        Usuario usuario = new Usuario(nombreUsuario, telefono);

        String validacionCampos = validacionCamposGuardar(usuario);

        if (validacionCampos != null) {
            JOptionPane.showMessageDialog(this, validacionCampos, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                this.usuariosDAO.agregar(usuario);
                JOptionPane.showMessageDialog(this, "Usuario agregado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                llenarTabla();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizar() {
        Long idUsuario = Long.parseLong(txtIdUsuario.getText());
        String nombreUsuario = txtNombre.getText();
        String telefono = txtTelefono.getText();

        Usuario usuario = new Usuario(idUsuario, nombreUsuario, telefono);

        String validacionCampos = validacionCamposGuardar(usuario);

        if (validacionCampos != null) {
            JOptionPane.showMessageDialog(this, validacionCampos, "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            try {
                this.usuariosDAO.actualizar(usuario);
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                llenarTabla();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        Long idUsuarioSeleccionado = this.getIdUsuario();
        Usuario usuario = null;
        activarCampos();
        if (idUsuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Para editar debe seleccionar un usuario de la tabla", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            usuario = this.usuariosDAO.consultarUsuarioPorId(idUsuarioSeleccionado);
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Información", JOptionPane.WARNING_MESSAGE);
        }

        if (usuario != null) {
            llenarFormulario(usuario);
        }
    }

    private Long getIdUsuario() {

        int indiceColumnaIdUsuario = 0;
        int indiceFilaSeleccionada = this.tblUsuarios.getSelectedRow();

        if (indiceFilaSeleccionada == -1) {
            return null;
        }

        DefaultTableModel modelo = (DefaultTableModel) this.tblUsuarios.getModel();

        Long idUsuario = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaIdUsuario);
        return idUsuario;
    }

    public void cancelar() {
        limpiarFormulario();
        activarCampos();
    }

    private void llenarFormulario(Usuario usuario) {
        this.txtIdUsuario.setText(usuario.getId().toString());
        this.txtNombre.setText(usuario.getNombre());
        this.txtTelefono.setText(usuario.getTelefono());
    }

    private void limpiarFormulario() {
        this.txtIdUsuario.setText("");
        this.txtNombre.setText("");
        this.txtTelefono.setText("");
    }

    //FALTA CREAR UN CONSULTAR TODOS
    private void llenarTabla() {
        List<Usuario> listaUsuarios = null;
        try {
            listaUsuarios = this.usuariosDAO.consultarTodos();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.WARNING_MESSAGE);
        }

        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblUsuarios.getModel();

        modeloTabla.setRowCount(0);

        listaUsuarios.forEach(usuario -> {
            Object[] fila = new Object[3];
            fila[0] = usuario.getId();
            fila[1] = usuario.getNombre();
            fila[2] = usuario.getTelefono();
            modeloTabla.addRow(fila);
        });
    }

    public String validacionCamposGuardar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            return "Nombre del usuario es requerido";
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().isEmpty()) {
            return "El teléfono del usuario es requerido";
        }
        return null;
    }

    public boolean esNombreUsuarioDuplicado(String nombreUsuario) throws PersistenciaException {
        Usuario usuario = (Usuario) usuariosDAO.consultarUsuarioPorNombre(nombreUsuario);
        if (usuario != null) {
            return true;
        }
        return false;
    }

    public void desactivarCampos() {
        txtNombre.setEditable(false);
        txtTelefono.setEditable(false);
    }

    public void activarCampos() {
        txtNombre.setEditable(true);
        txtTelefono.setEditable(true);
    }

    public void busquedaPorNombre() {
        String nombreUsuarioBuscado = txtBuscarNombre.getText();
        List<Usuario> listaUsuarios;
        if (nombreUsuarioBuscado == null || nombreUsuarioBuscado.isEmpty()) {
            try {
                listaUsuarios = this.usuariosDAO.consultarTodos();
                llenarTablaBusqueda(listaUsuarios);
                txtBuscarNombre.setText("");
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {

            try {
                listaUsuarios = this.usuariosDAO.consultarUsuarioPorNombre(nombreUsuarioBuscado);
                llenarTablaBusqueda(listaUsuarios);
                txtBuscarNombre.setText("");
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                txtBuscarNombre.setText("");
            }
        }
    }
    
    private void llenarTablaBusqueda(List<Usuario> lista) {
        List<Usuario> listaUsuarios = lista;

        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblUsuarios.getModel();

        modeloTabla.setRowCount(0);

        listaUsuarios.forEach(usuario -> {
            Object[] fila = new Object[3];
            fila[0] = usuario.getId();
            fila[1] = usuario.getNombre();
            fila[2] = usuario.getTelefono();
            modeloTabla.addRow(fila);
        });
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
        jPanel1 = new javax.swing.JPanel();
        lblIdUsuario = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblTeléfono = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPaneUsuarios = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarNombre = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tabla de usuario");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblIdUsuario.setText("Id:");

        lblNombre.setText("Nombre:");

        lblTeléfono.setText("Teléfono");

        txtIdUsuario.setEnabled(false);

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
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

        jLabel5.setText("Datos del usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblTeléfono, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                                    .addComponent(lblIdUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtIdUsuario)
                                        .addComponent(txtNombre))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblIdUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtIdUsuario))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTeléfono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnCancelar))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id usuario", "Nombre", "Teléfono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneUsuarios.setViewportView(tblUsuarios);

        jLabel4.setText("Ingresa un nombre");

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
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnEditar)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(279, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPaneUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(94, Short.MAX_VALUE))
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
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        if (txtNombre.getText().length() >= 100) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        char caracter = evt.getKeyChar();
        if (Character.isLetter(caracter)) {
            getToolkit().beep();
            evt.consume();
        }

    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        busquedaPorNombre();
    }//GEN-LAST:event_btnBuscarActionPerformed

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
//            java.util.logging.Logger.getLogger(DlgUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DlgUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DlgUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DlgUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DlgUsuario dialog = new DlgUsuario(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneUsuarios;
    private javax.swing.JLabel lblIdUsuario;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTeléfono;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtBuscarNombre;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
