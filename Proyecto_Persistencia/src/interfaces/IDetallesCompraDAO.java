/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Compra;
import entidades.DetallesCompra;
import entidades.Usuario;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author edw_v
 */
public interface IDetallesCompraDAO {
    public void agregar (DetallesCompra compra) throws PersistenciaException;
    public List<DetallesCompra> consultarPorIdCompra (Compra compra) throws PersistenciaException;
}
