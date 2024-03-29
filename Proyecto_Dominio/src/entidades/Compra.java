/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author edw_v
 */
@Entity
@Table(name = "compras")
public class Compra implements Serializable {

    @Id
    @Column(name = "id_compra")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_compra", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaCompra;
    
    @Column(name = "total", nullable = false)
    private Double total;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "compra")
    private List<DetallesCompra> detallesCompra;

    public Compra() {
    }

    public Compra(Long id) {
        this.id = id;
    }

    public Compra(Calendar fechaCompra, Double total) {
        this.fechaCompra = fechaCompra;
        this.total = total;
    }

    public Compra(Calendar fechaCompra, Double total, Usuario usuario) {
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.usuario = usuario;
    }

    public Compra(Long id, Calendar fechaCompra, Double total, Usuario usuario) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.usuario = usuario;
    }

    public Compra(Long id, Calendar fechaCompra, Double total, Usuario usuario, List<DetallesCompra> detallesCompra) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.usuario = usuario;
        this.detallesCompra = detallesCompra;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Calendar getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Calendar fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    

    public void addDetalles(DetallesCompra detalles){
        if(detalles == null){
            throw new IllegalArgumentException("Los detalles no pueden ser null");
        }
        detalles.setCompra(this);
        if(this.detallesCompra == null){
            this.detallesCompra = new LinkedList<>();
        } this.detallesCompra.add(detalles);
        
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Compra{" + "id=" + id + ", fechaCompra=" + fechaCompra + ", total=" + total + ", usuario=" + usuario + ", detallesCompra=" + detallesCompra + '}';
    }

}
