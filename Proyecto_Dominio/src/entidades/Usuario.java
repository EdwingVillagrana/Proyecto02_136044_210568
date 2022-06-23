/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author edw_v
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "telefono", nullable = false, length = 12)
    private String telefono;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Compra> listaCompras;
    
    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Usuario(String nombre, String telefono, List<Compra> compras) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.listaCompras = compras;
    }

    public Usuario(Long id, String nombre, String telefono, List<Compra> compras) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.listaCompras = compras;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Compra> getCompras() {
        return listaCompras;
    }

    public void setCompras(List<Compra> compras) {
        this.listaCompras = compras;
    }
    
    public void addCompra(Compra compra){
        if(compra == null){
            throw new IllegalArgumentException("La compra no puede estar vacía");
        }
        compra.setUsuario(this);
        if(this.listaCompras == null){ 
            this.listaCompras = new LinkedList<>();
        }
        this.listaCompras.add(compra);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", telefono=" + telefono + '}';
    }
    
}