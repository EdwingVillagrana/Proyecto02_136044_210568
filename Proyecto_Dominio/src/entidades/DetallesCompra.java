/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author edw_v
 */
@Entity
@Table(name = "detalles_compras")
public class DetallesCompra implements Serializable {

    @Id
    @Column(name = "id_detalles_compra")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_copias", nullable = false)
    private Integer numeroCopias;
    
    @Column(name = "precio", nullable = false)
    private Double precio;
    
    @Column(name = "importe", nullable = false)
    private Double importe;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_compra")
    private Compra compra;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_videojuego")
    private Videojuego videojuego;

    public DetallesCompra() {
    }

    public DetallesCompra(Long id) {
        this.id = id;
    }

    public DetallesCompra(Integer numeroCopias, Double precio, Double importe, Videojuego videojuego) {
        this.numeroCopias = numeroCopias;
        this.precio = precio;
        this.importe = importe;
        this.videojuego = videojuego;
    }

    public DetallesCompra(Integer numeroCopias, Double precio, Double importe, Compra compra, Videojuego videojuego) {
        this.numeroCopias = numeroCopias;
        this.precio = precio;
        this.importe = importe;
        this.compra = compra;
        this.videojuego = videojuego;
    }

    public DetallesCompra(Long id, Integer numeroCopias, Double precio, Double importe, Compra compra, Videojuego videojuego) {
        this.id = id;
        this.numeroCopias = numeroCopias;
        this.precio = precio;
        this.importe = importe;
        this.compra = compra;
        this.videojuego = videojuego;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroCopias() {
        return numeroCopias;
    }

    public void setNumeroCopias(Integer numeroCopias) {
        this.numeroCopias = numeroCopias;
        this.importe = this.precio*numeroCopias;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
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
        if (!(object instanceof DetallesCompra)) {
            return false;
        }
        DetallesCompra other = (DetallesCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
}
