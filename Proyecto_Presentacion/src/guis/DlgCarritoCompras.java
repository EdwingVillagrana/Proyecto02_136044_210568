/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package guis;

import entidades.Compra;
import entidades.DetallesCompra;
import entidades.Usuario;
import entidades.Videojuego;
import excepciones.PersistenciaException;
import interfaces.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DlgCarritoCompras extends javax.swing.JDialog {

    private Double subtotal;
    private Double total;
    private Double impuesto;
    private List<Videojuego> listaVideojuegos;
    private List<DetallesCompra> listaDetallesCompra;
    public final IVideojuegosDAO videojuegosDAO;
    public final IUsuariosDAO usuariosDAO;
    public final IComprasDAO comprasDAO;

    /**
     * Creates new form DlgCarritoCompras
     */
    public DlgCarritoCompras(java.awt.Frame parent, boolean modal, IUsuariosDAO usuariosDAO, IVideojuegosDAO videojuegosDAO, IComprasDAO comprasDAO) {
        super(parent, modal);
        this.comprasDAO = comprasDAO;
        this.videojuegosDAO = videojuegosDAO;
        this.usuariosDAO = usuariosDAO;
        listaVideojuegos = new LinkedList<>();
        listaDetallesCompra = new LinkedList<>();
        initComponents();
        busquedaPorNombre();
        llenarComboBoxUsuarios();
    }

    public void agregarCompra() {
        Usuario usuario = (Usuario) jComboBoxUsuarios.getSelectedItem();
        Compra compra = new Compra(Calendar.getInstance(), total, usuario);
        if (validaCampos()) {
            try {
                for (DetallesCompra detalles : listaDetallesCompra) {

                    compra.addDetalles(detalles);
                }
                comprasDAO.agregar(compra);
                actualizarStock();
                listaVideojuegos = videojuegosDAO.consultarTodos();
                llenarTablaVideojuegos(listaVideojuegos);
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Compra realizada con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            limpiarFormulario();
        }
    }

    public void cancelar() {
        limpiarFormulario();
    }

    public void agregarProductoAlCarrito() {

        int indiceColumna = 0;
        int indiceFilaSeleccionada = this.tblListaVideojuegos.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) this.tblListaVideojuegos.getModel();
        if (tblListaVideojuegos.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un videojuego", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {

            Long idVideojuego = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumna);
            try {
                Videojuego videojuegoSeleccionado = this.videojuegosDAO.consultarVideojuegoPorId(idVideojuego);
                llenarCarrito(videojuegoSeleccionado);
                llenarTablaSeleccionados(listaDetallesCompra);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private int nombreUsuarioComboBox(String nombreUsuario) {
        int index = -1;
        try {
            List<Usuario> listaUsuarios = this.usuariosDAO.consultarTodos();
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (listaUsuarios.get(i).getNombre() == nombreUsuario) {
                    index = i;
                    break;
                }
            }
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.WARNING_MESSAGE);
        }
        return index;
    }

    private void llenarCarrito(Videojuego videojuego) {

        if (!listaVideojuegos.contains(videojuego)) {

            listaVideojuegos.add(videojuego);
            DetallesCompra detallesCompra = new DetallesCompra(1, videojuego.getPrecio(), videojuego.getPrecio(), videojuego);
            listaDetallesCompra.add(detallesCompra);
        }
    }

    public void agregarCopia() {
        int indiceId = 0;
        int indiceFila = tblVideojuegosSeleccionados.getSelectedRow();
        Videojuego videojuego = null;

        if (tblVideojuegosSeleccionados.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un videojuego del carrito", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Long idVideojuego = (Long) tblVideojuegosSeleccionados.getValueAt(indiceFila, indiceId);

            try {
                videojuego = videojuegosDAO.consultarVideojuegoPorId(idVideojuego);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            if (stockDisponible(videojuego)) {
                for (DetallesCompra detalles : listaDetallesCompra) {
                    if (detalles.getVideojuego().equals(videojuego)) {
                        detalles.setNumeroCopias(detalles.getNumeroCopias() + 1);
                        break;
                    }
                }
                llenarTablaSeleccionados(listaDetallesCompra);
            } else {
                JOptionPane.showMessageDialog(this, "No hay suficiente stock", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void eliminarCopia() {
        int indiceId = 0;
        int indiceFila = tblVideojuegosSeleccionados.getSelectedRow();
        Videojuego videojuego = null;

        if (tblVideojuegosSeleccionados.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un videojuego del carrito", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Long idVideojuego = (Long) tblVideojuegosSeleccionados.getValueAt(indiceFila, indiceId);

            try {
                videojuego = videojuegosDAO.consultarVideojuegoPorId(idVideojuego);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            for (int i = 0; i < listaDetallesCompra.size(); i++) {
                if (listaDetallesCompra.get(i).getVideojuego().equals(videojuego)) {
                    listaDetallesCompra.get(i).setNumeroCopias(listaDetallesCompra.get(i).getNumeroCopias() - 1);
                    if (listaDetallesCompra.get(i).getNumeroCopias() == 0) {
                        listaDetallesCompra.remove(i);
                        listaVideojuegos.remove(videojuego);
                    }
                    break;
                }
            }
            llenarTablaSeleccionados(listaDetallesCompra);
        }
    }

    public void calculaImporteTotal() {

        subtotal = 0D;
        impuesto = 0D;
        total = 0D;

        for (DetallesCompra detalles : listaDetallesCompra) {
            subtotal += detalles.getImporte();
        }
        impuesto = subtotal * .16;
        total = impuesto + subtotal;
    }

    public void busquedaPorNombre() {
        String nombreVideojuegoBusqueda = txtBuscarNombreVideojuego.getText();
        List<Videojuego> listaVideojuego;
        if (nombreVideojuegoBusqueda == null || nombreVideojuegoBusqueda.isEmpty()) {
            try {
                listaVideojuego = this.videojuegosDAO.consultarTodos();
                llenarTablaVideojuegos(listaVideojuego);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {

            try {
                listaVideojuego = this.videojuegosDAO.consultarPorNombre(nombreVideojuegoBusqueda);
                llenarTablaVideojuegos(listaVideojuego);
                txtBuscarNombreVideojuego.setText("");
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                txtBuscarNombreVideojuego.setText("");
            }
        }
    }

    private void llenarTablaSeleccionados(List<DetallesCompra> lista) {
        List<DetallesCompra> listaDetallesCompra = lista;
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblVideojuegosSeleccionados.getModel();

        modeloTabla.setRowCount(0);

        listaDetallesCompra.forEach(detalles -> {
            Object[] fila = new Object[5];
            fila[0] = detalles.getVideojuego().getId();
            fila[1] = detalles.getVideojuego().getNombre();
            fila[2] = detalles.getVideojuego().getPrecio();
            fila[3] = detalles.getNumeroCopias();
            fila[4] = detalles.getImporte();

            modeloTabla.addRow(fila);
        });
        llenarFormulario();
    }

    private void llenarTablaVideojuegos(List<Videojuego> lista) {
        List<Videojuego> listaVideojuegos = lista;

        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblListaVideojuegos.getModel();

        modeloTabla.setRowCount(0);

        listaVideojuegos.forEach(videojuego -> {
            Object[] fila = new Object[4];
            fila[0] = videojuego.getId();
            fila[1] = videojuego.getNombre();
            fila[2] = videojuego.getPrecio();
            fila[3] = videojuego.getStock();

            modeloTabla.addRow(fila);
        });
    }

    public boolean stockDisponible(Videojuego videojuego) {
        int indiceCopias = 3;
        int indiceFila = tblVideojuegosSeleccionados.getSelectedRow();

        Integer numCopias = (Integer) tblVideojuegosSeleccionados.getValueAt(indiceFila, indiceCopias);

        if (numCopias < videojuego.getStock()) {
            return true;
        }
        return false;
    }

    private void limpiarFormulario() {
        this.jComboBoxUsuarios.setSelectedIndex(-1);
        this.txtSubtotal.setText("");
        this.txtTotal.setText("");
        this.listaVideojuegos.clear();
        this.listaDetallesCompra.clear();
        llenarTablaSeleccionados(listaDetallesCompra);
    }

    public void llenarFormulario() {
        calculaImporteTotal();
        this.txtSubtotal.setText(subtotal.toString());
        this.txtTotal.setText(total.toString());
    }

    public void llenarComboBoxUsuarios() {

        List<Usuario> listaUsuarios = null;

        try {
            listaUsuarios = usuariosDAO.consultarTodos();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Usuario usuario : listaUsuarios) {
            jComboBoxUsuarios.addItem(usuario);
        }
    }

    public void actualizarStock() {
        Videojuego videojuego;
        int numeroCopias;

        for (int i = 0; i < listaDetallesCompra.size(); i++) {
            numeroCopias = listaDetallesCompra.get(i).getNumeroCopias();
            videojuego = listaDetallesCompra.get(i).getVideojuego();
            videojuego.setStock(videojuego.getStock() - numeroCopias);
            try {
                videojuegosDAO.actualizar(videojuego);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Videojuego videojuegoSeleccionadoTablaVideojuegos() {
        Videojuego videojuegoSeleccionado = null;
        int indiceColumna = 0;
        int indiceFila = tblListaVideojuegos.getSelectedRow();

        Long idVideojuego = (Long) tblListaVideojuegos.getValueAt(indiceFila, indiceColumna);

        try {
            videojuegoSeleccionado = videojuegosDAO.consultarVideojuegoPorId(idVideojuego);
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
        return videojuegoSeleccionado;
    }

    public boolean validaCampos() {
        if (jComboBoxUsuarios.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (listaDetallesCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un videojuego", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
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
        lblUsuario = new javax.swing.JLabel();
        jComboBoxUsuarios = new javax.swing.JComboBox<>();
        lblFechaCompra = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtSubtotal = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnAgregarCopia = new javax.swing.JButton();
        btnEliminarCopia = new javax.swing.JButton();
        jScrollPaneVideojuegosSeleccionados = new javax.swing.JScrollPane();
        tblVideojuegosSeleccionados = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtBuscarNombreVideojuego = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPaneBuscarVideojuego = new javax.swing.JScrollPane();
        tblListaVideojuegos = new javax.swing.JTable();
        btnAgregarAlCarrito = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nueva Compra");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Compra"));
        jPanel1.setToolTipText("");

        lblUsuario.setText("Usuario");

        lblFechaCompra.setText("Subtotal");

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtSubtotal.setEditable(false);

        lblTotal.setText("Total");

        txtTotal.setEditable(false);

        btnAgregarCopia.setText("Agregar copia");
        btnAgregarCopia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCopiaActionPerformed(evt);
            }
        });

        btnEliminarCopia.setText("Eliminar copia");
        btnEliminarCopia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCopiaActionPerformed(evt);
            }
        });

        tblVideojuegosSeleccionados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Precio", "Copias", "Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
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
        jScrollPaneVideojuegosSeleccionados.setViewportView(tblVideojuegosSeleccionados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFechaCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPaneVideojuegosSeleccionados, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAgregarCopia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEliminarCopia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(jComboBoxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaCompra)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPaneVideojuegosSeleccionados, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRegistrar)
                            .addComponent(btnCancelar))
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAgregarCopia)
                        .addGap(59, 59, 59)
                        .addComponent(btnEliminarCopia)
                        .addGap(149, 149, 149))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Videojuegos Disponibles"));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre");

        tblListaVideojuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Precio", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneBuscarVideojuego.setViewportView(tblListaVideojuegos);

        btnAgregarAlCarrito.setText("Agregar al carrito");
        btnAgregarAlCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAlCarritoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarNombreVideojuego)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPaneBuscarVideojuego, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarAlCarrito)
                .addGap(204, 204, 204))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarNombreVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(jLabel1))
                .addGap(28, 28, 28)
                .addComponent(jScrollPaneBuscarVideojuego, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAgregarAlCarrito)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        agregarCompra();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        busquedaPorNombre();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAgregarAlCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAlCarritoActionPerformed
        agregarProductoAlCarrito();
    }//GEN-LAST:event_btnAgregarAlCarritoActionPerformed

    private void btnEliminarCopiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCopiaActionPerformed
        eliminarCopia();
    }//GEN-LAST:event_btnEliminarCopiaActionPerformed

    private void btnAgregarCopiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCopiaActionPerformed
        agregarCopia();
    }//GEN-LAST:event_btnAgregarCopiaActionPerformed

    /**
     * @param args the command line arguments
     */
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
//            java.util.logging.Logger.getLogger(DlgCarritoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DlgCarritoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DlgCarritoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DlgCarritoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DlgCarritoCompras dialog = new DlgCarritoCompras(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAgregarAlCarrito;
    private javax.swing.JButton btnAgregarCopia;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarCopia;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<Object> jComboBoxUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPaneBuscarVideojuego;
    private javax.swing.JScrollPane jScrollPaneVideojuegosSeleccionados;
    private javax.swing.JLabel lblFechaCompra;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTable tblListaVideojuegos;
    private javax.swing.JTable tblVideojuegosSeleccionados;
    private javax.swing.JTextField txtBuscarNombreVideojuego;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
