/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Compra;
import excepciones.PersistenciaException;

/**
 *
 * @author edw_v
 */
public interface IComprasDAO {
    public void agregar (Compra compra) throws PersistenciaException;
    public void actualizar(Compra compra) throws PersistenciaException;
    public Compra consultarUsuarioPorId(Long id)throws PersistenciaException;
    public void eliminar(Compra compra) throws PersistenciaException;
}
